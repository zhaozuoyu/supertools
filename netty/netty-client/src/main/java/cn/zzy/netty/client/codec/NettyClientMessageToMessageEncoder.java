package cn.zzy.netty.client.codec;

import java.util.List;

import cn.zzy.netty.dto.input.UserInputDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

/**
 * @author zhaozuoyu
 * @date 2021/11/23
 */
public class NettyClientMessageToMessageEncoder extends MessageToMessageEncoder<UserInputDTO> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UserInputDTO msg, List<Object> out) throws Exception {

    }
}
