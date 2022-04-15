package cn.zzy.netty.client.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.zzy.netty.client.codec.NettyClientByteToMessageCodec;
import cn.zzy.netty.client.codec.NettyClientByteToMessageDecoder;
import cn.zzy.netty.client.codec.NettyClientLengthFieldBasedFrameDecoder;
import cn.zzy.netty.client.codec.NettyClientMessageToByteEncoder;
import cn.zzy.netty.client.handler.NettyClientChannelInboundHandler;
import cn.zzy.netty.client.handler.NettyClientChannelNotAutoCloseInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author zhaozuoyu
 * @date 2021/11/19
 */
@Component
public class NettyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private NettyClientChannelInboundHandler nettyClientChannelInboundHandler;
    @Autowired
    private NettyClientChannelNotAutoCloseInboundHandler nettyClientChannelNotAutoCloseInboundHandler;
    @Autowired
    private NettyClientByteToMessageCodec nettyClientByteToMessageCodec;
    @Autowired
    private NettyClientLengthFieldBasedFrameDecoder nettyClientLengthFieldBasedFrameDecoder;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        // 注意nettyClientLengthFieldBasedFrameDecoder要添加在nettyClientByteToMessageCodec前面
        /*socketChannel.pipeline().addLast(nettyClientLengthFieldBasedFrameDecoder).addLast(nettyClientByteToMessageCodec)
            .addLast(this.nettyClientChannelNotAutoCloseInboundHandler);*/
        /* socketChannel.pipeline().addLast(this.nettyClientChannelNotAutoCloseInboundHandler);*/
        ChannelPipeline pipeline = socketChannel.pipeline();
        // add channel inbound handler to pipeline
        pipeline.addLast(new NettyClientByteToMessageDecoder())
            .addLast(this.nettyClientChannelNotAutoCloseInboundHandler);
        // add channel outbound handler to pipeline
        pipeline.addLast(new NettyClientMessageToByteEncoder());
    }
}
