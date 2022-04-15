package cn.zzy.netty.client.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author zhaozuoyu
 * @date 2021/11/23
 */
public class NettyClientByteToMessageDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientByteToMessageDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        if (length == 0) {
            logger.info("netty client msg decoded already");
            return;
        }
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);
        Object object = SerializationUtils.deserialize(bytes);
        logger.info("netty client decode msg:{}", object.toString());
        out.add(object);
    }

    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        super.decodeLast(ctx, in, out);
    }
}
