package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import javax.crypto.Cipher;

public class PacketDecrypter extends MessageToMessageDecoder<ByteBuf> {

    // Tuinity start
    private final com.velocitypowered.natives.encryption.VelocityCipher cipher;
    //private final PacketEncryptionHandler a;

    public PacketDecrypter(javax.crypto.SecretKey key /* Cipher cipher */) throws java.security.GeneralSecurityException {
        //this.a = new PacketEncryptionHandler(cipher);
        this.cipher = com.velocitypowered.natives.util.Natives.cipher.get().forDecryption(key);
    }

    protected void decode(ChannelHandlerContext channelhandlercontext, ByteBuf bytebuf, List<Object> list) throws Exception {
        ByteBuf compatible = com.velocitypowered.natives.util.MoreByteBufUtils.ensureCompatible(channelhandlercontext.alloc(), cipher, bytebuf).slice();
        try {
            cipher.process(compatible);
            list.add(compatible);
        } catch (Exception e) {
            compatible.release(); // compatible will never be used if we throw an exception
            throw e;
        }
    }
    // Tuinity end
}
