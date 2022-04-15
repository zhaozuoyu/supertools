package cn.zzy.netty.client.codec;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 处理粘包拆包问题，类似于ObjectDecoder，但没有进行反序列化
 * 
 * @author zhaozuoyu
 * @date 2021/11/24
 */
@Component
public class NettyClientLengthFieldBasedFrameDecoder extends LengthFieldBasedFrameDecoder {

    public NettyClientLengthFieldBasedFrameDecoder() {
        this(1315271800, 0, 4, 0, 4);
    }

    public NettyClientLengthFieldBasedFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength,
        int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        return super.decode(ctx, in);
    }
}
