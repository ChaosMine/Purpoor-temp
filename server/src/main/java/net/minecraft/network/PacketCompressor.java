package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf> {

    // Tuinity start - use Velocity natives
//    private final byte[] a = new byte[8192];
//    private final Deflater b;
    private int c;
    private final com.velocitypowered.natives.compression.VelocityCompressor compressor;

    public PacketCompressor(int i) {
        this.c = i;
//        this.b = new Deflater();
        this.compressor = com.velocitypowered.natives.util.Natives.compress.get().create(-1);
    }
    // Tuinity end

    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
        int i = bytebuf.readableBytes();
        PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf1);

        if (i < this.c) {
            packetdataserializer.d(0);
            packetdataserializer.writeBytes(bytebuf);
        } else {
            // Tuinity start - delegate to Velocity natives
//            byte[] abyte = new byte[i];
//
//            bytebuf.readBytes(abyte);
//            packetdataserializer.d(abyte.length);
//            this.b.setInput(abyte, 0, i);
//            this.b.finish();
//
//            while (!this.b.finished()) {
//                int j = this.b.deflate(this.a);
//
//                packetdataserializer.writeBytes(this.a, 0, j);
//            }
//
//            this.b.reset();
            packetdataserializer.d(i);
            ByteBuf source = com.velocitypowered.natives.util.MoreByteBufUtils.ensureCompatible(channelhandlercontext.alloc(),
                    this.compressor, bytebuf);
            try {
                this.compressor.deflate(source, bytebuf1);
            } finally {
                source.release();
            }
            // Tuinity end
        }

    }

    // Tuinity start
    @Override
    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) throws Exception {
        return com.velocitypowered.natives.util.MoreByteBufUtils.preferredBuffer(ctx.alloc(), this.compressor, msg.readableBytes() + 1);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        this.compressor.close();
    }
    // Tuinity end

    public void a(int i) {
        this.c = i;
    }
}
