package cn.zzy.netty.server.codec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.dto.output.UserOutputDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 服务端编码器
 * 
 * @author zhaozuoyu
 * @date 2022/02/09
 */
@Component
@ChannelHandler.Sharable
public class NettyServerMessageToByteEncoder extends MessageToByteEncoder<UserOutputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(NettyServerMessageToByteEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, UserOutputDTO msg, ByteBuf out) throws Exception {
        logger.info("encode msg:{}", JSON.toJSONString(msg));
        if (msg != null) {
            byte[] bytes = SerializationUtils.serialize(msg);
            out.writeBytes(bytes);
        }
    }
}
