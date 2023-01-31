package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import me.steinborn.krypton.mod.shared.network.VarintByteDecoder;

import java.util.List;

import static me.steinborn.krypton.mod.shared.network.util.WellKnownExceptions.BAD_LENGTH_CACHED;
import static me.steinborn.krypton.mod.shared.network.util.WellKnownExceptions.VARINT_BIG_CACHED;

public class PacketSplitter extends ByteToMessageDecoder {
	private final VarintByteDecoder reader = new VarintByteDecoder();

	public PacketSplitter() {}

	public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (!ctx.channel().isActive()) {
			in.clear();
			return;
		}

		reader.reset();

		int varintEnd = in.forEachByte(reader);
		if (varintEnd == -1) {
			// We tried to go beyond the end of the buffer. This is probably a good sign that the
			// buffer was too short to hold a proper varint.
			if (reader.getResult() == VarintByteDecoder.DecodeResult.RUN_OF_ZEROES) {
				// Special case where the entire packet is just a run of zeroes. We ignore them all.
				in.clear();
			}
			return;
		}

		if (reader.getResult() == VarintByteDecoder.DecodeResult.RUN_OF_ZEROES) {
			// this will return to the point where the next varint starts
			in.readerIndex(varintEnd);
		} else if (reader.getResult() == VarintByteDecoder.DecodeResult.SUCCESS) {
			int readVarint = reader.getReadVarint();
			int bytesRead = reader.getBytesRead();
			if (readVarint < 0) {
				in.clear();
				throw BAD_LENGTH_CACHED;
			} else if (readVarint == 0) {
				// skip over the empty packet(s) and ignore it
				in.readerIndex(varintEnd + 1);
			} else {
				int minimumRead = bytesRead + readVarint;
				if (in.isReadable(minimumRead)) {
					out.add(in.retainedSlice(varintEnd + 1, readVarint));
					in.skipBytes(minimumRead);
				}
			}
		} else if (reader.getResult() == VarintByteDecoder.DecodeResult.TOO_BIG) {
			in.clear();
			throw VARINT_BIG_CACHED;
		}
	}
}
