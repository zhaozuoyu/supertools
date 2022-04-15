package cn.zzy.netty.server.boot;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 时间服务器启动类
 *
 * @author zhaozuoyu
 * @date 2021/11/8
 */
@Component
public class NettyServer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Value("${netty.time.server.port}")
    private Integer port;

    @Autowired
    private NettyServerChannelInitializer nettyServerChannelInitializer;

    private Channel channel;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() {
        new Thread(this).start();
    }

    @PreDestroy
    public void destroy() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        channel.closeFuture().syncUninterruptibly();
        workerGroup = null;
        bossGroup = null;
        channel = null;
    }

    @Override
    public void run() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        // 注意：ServerBootstrap父类AbstractBootstrap<ServerBootstrap, ServerChannel>
        // 其中NioServerSocketChannel继承自AbstractChannel和ServerChannel
        // channel pipeline 是在channel构造方法中调用父类构造方法过程中初始化的
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        try {
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(this.port)
                .childHandler(this.nettyServerChannelInitializer);
            ChannelFuture channelFuture = serverBootstrap.bind().sync();
            logger.info("{} started and listen on {}", this.getClass().getName(),
                channelFuture.channel().localAddress());
            this.channel = channelFuture.channel();
            this.channel.closeFuture().sync();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public Channel getChannel() {
        return channel;
    }
}
