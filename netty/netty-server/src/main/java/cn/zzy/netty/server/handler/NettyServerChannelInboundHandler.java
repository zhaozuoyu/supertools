package cn.zzy.netty.server.handler;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.dto.input.UserInputDTO;
import cn.zzy.netty.dto.output.UserOutputDTO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author zhaozuoyu
 * @date 2020/12/9
 */
@Component
@ChannelHandler.Sharable
public class NettyServerChannelInboundHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerChannelInboundHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            logger.info("netty server received msg:{}", ((ByteBuf)msg).toString(Charset.forName("utf-8")));
            ctx.writeAndFlush(msg);
        } else if (msg instanceof UserInputDTO) {
            UserInputDTO userInputDTO = (UserInputDTO)msg;
            logger.info("netty server received msg:{}", JSON.toJSONString(userInputDTO));
            // start a new thread to execute task
            ctx.executor().execute(new Runnable() {
                @Override
                public void run() {
                    // business handler msg
                    UserOutputDTO userOutputDTO = this.getOutputDTO(userInputDTO);
                    // 当管道中添加了编码器时，直接写入原始输出对象即可，不需要转换为ByteBuf
                    byte[] bytes = SerializationUtils.serialize(userOutputDTO);
                    ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                    // 注意：一般使用Channel写入或刷新消息，不要使用ChannelHandlerContext，因为当前ChannelHandlerContext可能不处于管道尾部导致编码器失效
                    Channel channel = ctx.channel();
                    channel.writeAndFlush(userOutputDTO);
                }

                private UserOutputDTO getOutputDTO(UserInputDTO userInputDTO) {
                    UserOutputDTO userOutputDTO = new UserOutputDTO().setId(userInputDTO.getId()).setUsername("Alice");
                    return userOutputDTO;
                }
            });
        } else {
            logger.info("other type msg");
        }
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
