package cn.zzy.netty.client.rpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.client.boot.NettyClient;
import cn.zzy.netty.client.future.NettyClientFuture;
import cn.zzy.netty.dto.base.RequestDTO;
import io.netty.channel.Channel;

/**
 * @author zhaozuoyu
 * @date 2021/11/29
 */
@Component
public class RpcRequest {

    private static final Logger logger = LoggerFactory.getLogger(RpcRequest.class);

    @Autowired
    private NettyClient nettyClient;

    public NettyClientFuture send(RequestDTO<?> requestDTO) {
        Channel channel = nettyClient.getChannel();
        logger.info("send message to netty server,arguments:{}", JSON.toJSONString(requestDTO));
        channel.writeAndFlush(requestDTO);
        return new NettyClientFuture(requestDTO.getUniqueId());
    }
}
