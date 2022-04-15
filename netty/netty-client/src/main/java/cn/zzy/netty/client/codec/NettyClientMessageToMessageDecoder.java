package cn.zzy.netty.client.codec;

import java.util.List;

import cn.zzy.netty.dto.output.UserOutputDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author zhaozuoyu
 * @date 2021/11/23
 */
public class NettyClientMessageToMessageDecoder extends MessageToMessageDecoder<UserOutputDTO> {

    @Override
    protected void decode(ChannelHandlerContext ctx, UserOutputDTO msg, List<Object> out) throws Exception {
        out.add(msg);
    }
}
