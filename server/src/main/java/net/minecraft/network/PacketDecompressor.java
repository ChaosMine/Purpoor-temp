package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.zip.Inflater;

public class PacketDecompressor extends ByteToMessageDecoder {

    // Tuinity start - use Velocity natives
    //private final Inflater a;
    private final com.velocitypowered.natives.compression.VelocityCompressor compressor;
    private int b;

    public PacketDecompressor(int i) {
        this.b = i;
        //this.a = new Inflater();
        this.compressor = com.velocitypowered.natives.util.Natives.compress.get().create(-1);
    }
    // Tuinity end

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        if (bytebuf.readableBytes() != 0) {
            PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf);
            int i = packetdataserializer.i();

            if (i == 0) {
                list.add(packetdataserializer.readBytes(packetdataserializer.readableBytes()));
            } else {
                if (i < this.b) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.b);
                }

                if (i > 2097152) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
                }

                // Tuinity start
                ByteBuf compatibleIn = com.velocitypowered.natives.util.MoreByteBufUtils.ensureCompatible(channelhandlercontext.alloc(), compressor, bytebuf);
                ByteBuf uncompressed = com.velocitypowered.natives.util.MoreByteBufUtils.preferredBuffer(channelhandlercontext.alloc(), compressor, i);
                try {
                    compressor.inflate(compatibleIn, uncompressed, i);
                    list.add(uncompressed);
                    bytebuf.clear();
                } catch (Exception e) {
                    uncompressed.release();
                    throw e;
                } finally {
                    compatibleIn.release();
                }
//                byte[] abyte = new byte[packetdataserializer.readableBytes()];
//
//                packetdataserializer.readBytes(abyte);
//                this.a.setInput(abyte);
//                byte[] abyte1 = new byte[i];
//
//                this.a.inflate(abyte1);
//                list.add(Unpooled.wrappedBuffer(abyte1));
//                this.a.reset();
                // Tuinity end
            }

        }
    }

    // Tuinity start
    @Override
    public void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        this.compressor.close();
    }
    // Tuinity end

    public void a(int i) {
        this.b = i;
    }
}
