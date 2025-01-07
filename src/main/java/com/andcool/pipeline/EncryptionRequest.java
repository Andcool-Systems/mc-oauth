package com.andcool.pipeline;

import com.andcool.OAuthServer;
import com.andcool.config.UserConfig;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import com.andcool.bytebuf.ByteBufUtils;

import java.io.IOException;

public class EncryptionRequest {
    public static void sendEncryptionRequest(ChannelHandlerContext ctx, int protocolVersion) throws IOException {
        ByteBuf out = ctx.alloc().buffer();
        ByteBufUtils.writeVarInt(out, 0x01); // Packet ID
        ByteBufUtils.writeUTF8(out, UserConfig.SERVER_ID); // Server ID
        byte[] publicKey = OAuthServer.KEY_PAIR.getPublic().getEncoded();

        // Write server's public key
        ByteBufUtils.writeVarInt(out, publicKey.length);
        out.writeBytes(publicKey);

        // Write server's verify token
        ByteBufUtils.writeVarInt(out, OAuthServer.VERIFY_TOKEN.length);
        out.writeBytes(OAuthServer.VERIFY_TOKEN);

        // For widest client protocol version support
        if (protocolVersion >= 766) {
            out.writeBoolean(true);
        }

        ByteBufUtils.sendPacket(ctx, out);
    }
}
