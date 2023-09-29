package dev.kamillaova.paper.server;

import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.minecraft.network.EnumProtocol;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;

import static me.steinborn.krypton.mod.shared.network.util.VarIntUtil.VARINT21_SIZE_MAX;

public final class PacketUtils {
	public static ByteBuf bakePacket(
		ByteBufAllocator alloc,
		EnumProtocol protocol,
		Packet<?> packet
	) {
		try {
			var buf = new PacketDataSerializer(alloc.ioBuffer());
			buf.setIndex(VARINT21_SIZE_MAX, VARINT21_SIZE_MAX);

			buf.writeVarInt(protocol.getId(EnumProtocolDirection.CLIENTBOUND, packet));
			packet.serialize(buf);
			buf.markWriterIndex();

			var packetSize = buf.readableBytes();

			buf.setIndex(0, 0);

			ProtocolUtils.write21BitVarInt(buf, packetSize); // TODO: Optimize - use default VarInt
			buf.resetWriterIndex();

			return buf;
		} catch (Exception e) {
			throw new IllegalStateException("Unable to bake packet", e);
		}
	}
}
