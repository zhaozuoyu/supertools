package cn.zzy.netty.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * @author zhaozuoyu
 * @date 2022/2/15
 */
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            ByteBuf byteBuf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("heartbeat", CharsetUtil.UTF_8));
            // 发送远程心跳信息，并在失败时关闭该连接
            ctx.writeAndFlush(byteBuf).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } else {
            // 当不是IdleStateEvent事件，所以将它传递给下一个ChannelInboundHandler
            super.userEventTriggered(ctx, evt);
        }

    }
}
