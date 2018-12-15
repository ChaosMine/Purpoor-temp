package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;

// Tuinity start
// We rewrite this class as the Velocity natives support in-place encryption
import io.netty.handler.codec.MessageToMessageEncoder; // An unfortunate import, but this is required to fix a compiler error
public class PacketEncrypter extends MessageToMessageEncoder<ByteBuf> {

    private final com.velocitypowered.natives.encryption.VelocityCipher cipher;
    //private final PacketEncryptionHandler a;

    public PacketEncrypter(javax.crypto.SecretKey key /* Cipher cipher */) throws java.security.GeneralSecurityException {
        // this.a = new PacketEncryptionHandler(cipher);
        this.cipher = com.velocitypowered.natives.util.Natives.cipher.get().forEncryption(key);
    }

//    protected void encode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, ByteBuf bytebuf1) throws Exception {
//        this.a.a(bytebuf, bytebuf1);
//    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, java.util.List<Object> out) throws Exception {
        ByteBuf compatible = com.velocitypowered.natives.util.MoreByteBufUtils.ensureCompatible(ctx.alloc(), this.cipher, msg);
        try {
            this.cipher.process(compatible);
            out.add(compatible);
        } catch (Exception e) {
            compatible.release(); // compatible will never be used if we throw an exception
            throw e;
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        cipher.close();
    }
}
// Tuinity end
