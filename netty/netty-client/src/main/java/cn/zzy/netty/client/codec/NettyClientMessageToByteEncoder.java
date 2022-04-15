package cn.zzy.netty.client.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.dto.input.UserInputDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zhaozuoyu
 * @date 2021/11/24
 */
public class NettyClientMessageToByteEncoder extends MessageToByteEncoder<UserInputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientMessageToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, UserInputDTO msg, ByteBuf out) throws Exception {
        logger.info("netty client encode msg:{}", JSON.toJSONString(msg));
        if (msg != null) {
            byte[] bytes = SerializationUtils.serialize(msg);
            out.writeBytes(bytes);
        }
    }
}
