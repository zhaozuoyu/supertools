package cn.zzy.netty.boot;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.zzy.netty.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author zhaozuoyu
 * @date 2020/12/9
 */
@Component
public class DiscardServer {

    private static final Logger logger = LoggerFactory.getLogger(DiscardServer.class);

    @PostConstruct
    public void init() {
        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap serverBootstrap = new ServerBootstrap();
                serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new DiscardServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

                // 绑定端口，开始接收进来的连接
                ChannelFuture channelFuture = serverBootstrap.bind(5230).sync();

                // 等待服务器 socket 关闭 。
                // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器。
                channelFuture.channel().closeFuture().sync();
            } finally {
                workerGroup.shutdownGracefully();
                bossGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

}
