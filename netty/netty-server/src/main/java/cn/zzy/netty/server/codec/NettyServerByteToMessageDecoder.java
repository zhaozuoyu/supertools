package cn.zzy.netty.server.codec;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.TooLongFrameException;

/**
 * 服务端解码器
 * 
 * @author zhaozuoyu
 * @date 2022/02/09
 */
@Component
public class NettyServerByteToMessageDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerByteToMessageDecoder.class);

    private static final int MAX_FRAME_SIZE = 1024;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int length = in.readableBytes();
        if (length == 0) {
            logger.info("msg decoded already");
            return;
        } else if (length > MAX_FRAME_SIZE) {
            // 跳过所有可读字节，抛出异常并通知ChannelHandler
            in.skipBytes(length);
            throw new TooLongFrameException("frame too big!");
        }
        byte[] bytes = new byte[length];
        // in.getBytes(0, bytes);
        // 注意：这里一定要移动读指针，不然会抛出 decode() did not read anything but decoded a message
        in.readBytes(bytes, 0, length);

        Object object = SerializationUtils.deserialize(bytes);
        logger.info("decode msg:{}", object.toString());
        out.add(object);
    }

    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        super.decodeLast(ctx, in, out);
    }
}
