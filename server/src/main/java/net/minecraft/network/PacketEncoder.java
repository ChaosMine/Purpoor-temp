package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

import static me.steinborn.krypton.mod.shared.network.util.VarIntUtil.VARINT21_SIZE_MAX;

public class PacketEncoder extends ChannelOutboundHandlerAdapter {
	private static final Logger LOGGER = LogManager.getLogger(PacketEncoder.class);

	private final EnumProtocolDirection direction;

	private EnumProtocol protocol;
	private Locale adventure$locale;

	public PacketEncoder(EnumProtocolDirection direction) {
		this.direction = direction;
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof Packet<?> packet) {
			var id = protocol.getId(this.direction, packet);

			var buf = ctx.alloc().ioBuffer();
			var ds = new PacketDataSerializer(buf);
			ds.adventure$locale = this.adventure$locale;

			ds.setIndex(VARINT21_SIZE_MAX, VARINT21_SIZE_MAX);

			ds.writeVarInt(id);

			try {
				packet.serialize(ds);
			} catch (Throwable throwable) {
				PacketEncoder.LOGGER.error(throwable);
				throwable.printStackTrace(); // Paper - WHAT WAS IT? WHO DID THIS TO YOU? WHAT DID YOU SEE?
				if (packet.a()) {
					throw new SkipEncodeException(throwable);
				} else {
					throw throwable;
				}
			}

			var packetLength = ds.readableBytes();
			if (packetLength > MAX_PACKET_SIZE) {
				throw new PacketTooLargeException(packet, packetLength);
			}

			ds.markWriterIndex();
			ctx.write(buf, promise);
			return;
		}
		ctx.write(msg, promise);
	}

	private static final int MAX_PACKET_SIZE = 2097152;

	public static class PacketTooLargeException extends RuntimeException {
		private final Packet<?> packet;

		PacketTooLargeException(Packet<?> packet, int packetLength) {
			super("PacketTooLarge - " + packet.getClass().getSimpleName() + " is " + packetLength + ". Max is " + MAX_PACKET_SIZE);
			this.packet = packet;
		}

		public Packet<?> getPacket() {
			return packet;
		}
	}


	public void setProtocol(EnumProtocol protocol) {
		this.protocol = protocol;
	}

	public void setAdventure$locale(Locale adventure$locale) {
		this.adventure$locale = adventure$locale;
	}
}
