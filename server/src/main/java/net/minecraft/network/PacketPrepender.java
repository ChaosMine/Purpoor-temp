package net.minecraft.network;

import com.velocitypowered.proxy.protocol.ProtocolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@Sharable
public class PacketPrepender extends ChannelOutboundHandlerAdapter {
	public static final PacketPrepender INSTANCE = new PacketPrepender();

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (msg instanceof ByteBuf buf) {
			var packetSize = buf.readableBytes();
			buf.setIndex(0, 0);

			ProtocolUtils.write21BitVarInt(buf, packetSize);
			buf.resetWriterIndex();
		}
		ctx.write(msg, promise);
	}
}
