package cn.zzy.netty.handler;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 处理服务端channel
 * 
 * @author zhaozuoyu
 * @date 2020/12/9
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(DiscardServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 默默地丢弃收到的数据
        if (msg instanceof ByteBuf) {
            // 1.丢弃消息
            /*((ByteBuf)msg).release();*/
            // 2.打印收到的消息
            /*ByteBuf in = (ByteBuf)msg;
            try {
                while (in.isReadable()) {
                    System.out.print((char)in.readByte());
                    System.out.flush();
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }*/
            // 3.消息应答
            ctx.write(msg);
            logger.info("msg={}", ((ByteBuf)msg).toString(Charset.forName("utf-8")));
            ctx.flush();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 当出现异常就关闭连接
        logger.error(cause.getMessage(), cause);
        ctx.close();
    }
}
