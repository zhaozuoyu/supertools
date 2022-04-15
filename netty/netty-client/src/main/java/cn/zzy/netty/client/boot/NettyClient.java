package cn.zzy.netty.client.boot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 时间服务器客户端启动类
 *
 * @author zhaozuoyu
 * @date 2021/11/8
 */
@Component
public class NettyClient implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    @Value("${netty.time.server.host}")
    private String host;
    @Value("${netty.time.server.port}")
    private Integer port;

    @Autowired
    private NettyClientChannelInitializer nettyClientChannelInitializer;

    private Channel channel;
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    @PostConstruct
    public void init() {
        new Thread(this).start();
    }

    @PreDestroy
    public void destroy() {
        eventLoopGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        eventLoopGroup = null;
        channel = null;
    }

    @Override
    public void run() {
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).remoteAddress(this.host, this.port)
                .handler(this.nettyClientChannelInitializer);
            ChannelFuture channelFuture = bootstrap.connect().sync();
            logger.info("{} started and listen on {}", this.getClass().getName(),
                channelFuture.channel().localAddress());
            this.channel = channelFuture.channel();
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public Channel getChannel() {
        return channel;
    }

}
