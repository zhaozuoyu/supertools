package cn.zzy.netty.server.boot;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.zzy.netty.server.codec.NettyServerByteToMessageDecoder;
import cn.zzy.netty.server.codec.NettyServerMessageToByteEncoder;
import cn.zzy.netty.server.handler.HeartbeatHandler;
import cn.zzy.netty.server.handler.NettyServerChannelInboundHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author zhaozuoyu
 * @date 2021/11/20
 */
@Component
public class NettyServerChannelInitializer extends ChannelInitializer<Channel> {

    @Autowired
    private NettyServerChannelInboundHandler nettyServerChannelInboundHandler;
    @Autowired
    private NettyServerByteToMessageDecoder nettyServerByteToMessageDecoder;
    @Autowired
    private NettyServerMessageToByteEncoder nettyServerMessageToByteEncoder;

    @Override
    protected void initChannel(Channel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 为了通讯安全可将SslHandler作为第一个ChannelHandler添加到ChannelPipeline中
        // 如果连接超过60秒没有接收或者发送任何数据，IdleStateHandler将在被触发时发送一个IdleStateEvent事件来调用fireUserEventTriggered()
        pipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler());
        // add channel inbound handler to pipeline
        pipeline.addLast(new NettyServerByteToMessageDecoder()).addLast(this.nettyServerChannelInboundHandler);
        // add channel outbound handler to pipeline
        pipeline.addLast(nettyServerMessageToByteEncoder);
    }
}
