package cn.zzy.netty.client.handler;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.dto.output.UserOutputDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @author zhaozuoyu
 * @date 2021/11/8
 */
@Component
@ChannelHandler.Sharable
public class NettyClientChannelNotAutoCloseInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientChannelNotAutoCloseInboundHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            logger.info("netty client received: {}", ((ByteBuf)msg).toString(Charset.forName("utf-8")));
        } else if (msg instanceof UserOutputDTO) {
            UserOutputDTO userOutputDTO = (UserOutputDTO)msg;
            logger.info("netty client received: {}", JSON.toJSONString(userOutputDTO));
            // handler
        } else {
            logger.info("other type msg");
        }
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
