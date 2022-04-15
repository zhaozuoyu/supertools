package cn.zzy.netty.client.codec;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import cn.zzy.netty.dto.input.UserInputDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 * 自定义编解码器
 * 
 * @author zhaozuoyu
 * @date 2021/11/24
 */
@Component
public class NettyClientByteToMessageCodec extends ByteToMessageCodec<UserInputDTO> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UserInputDTO msg, ByteBuf out) throws Exception {
        byte[] bytes = SerializationUtils.serialize(msg);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        byte[] bytes = new byte[length];
        in.getBytes(0, bytes);
        Object object = SerializationUtils.deserialize(bytes);
        out.add(object);
    }
}
