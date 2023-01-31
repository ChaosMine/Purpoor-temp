package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.minecraft.network.protocol.EnumProtocolDirection;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
	private final EnumProtocolDirection direction;

	private EnumProtocol protocol;

	public PacketDecoder(EnumProtocolDirection direction) {
		this.direction = direction;
	}

	protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf buf, List<Object> list) throws Exception {
		if (buf.readableBytes() != 0) {
			var packetdataserializer = new PacketDataSerializer(buf);
			var id = packetdataserializer.readVarInt();
			var packet = protocol.getById(this.direction, id);

			if (packet == null) {
				throw new IOException("Bad packet id " + id);
			} else {
				packet.deserialize(packetdataserializer);
				if (packetdataserializer.readableBytes() > 0) {
					throw new IOException("Packet not fully read, broken client?");
				} else {
					list.add(packet);
				}
			}
		}
	}

	public void setProtocol(EnumProtocol protocol) {
		this.protocol = protocol;
	}
}
