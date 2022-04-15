package cn.zzy.netty.server.codec;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 编解码器灵活扩展案例
 * 
 * @author zhaozuoyu
 * @date 2022/2/14
 */
public class NettyServerCombinedByteMessageCodec
    extends CombinedChannelDuplexHandler<NettyServerByteToMessageDecoder, NettyServerMessageToByteEncoder> {

    public NettyServerCombinedByteMessageCodec() {
        super(new NettyServerByteToMessageDecoder(), new NettyServerMessageToByteEncoder());
    }
}
