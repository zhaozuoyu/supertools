package cn.zzy.netty.client.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.client.future.NettyClientFuture;
import cn.zzy.netty.client.rpc.RpcRequest;
import cn.zzy.netty.dto.base.RequestDTO;
import cn.zzy.netty.dto.base.ResponseDTO;
import cn.zzy.netty.dto.input.UserInputDTO;
import cn.zzy.netty.dto.output.UserOutputDTO;
import cn.zzy.netty.service.IUserService;

/**
 * @author zhaozuoyu
 * @date 2021/11/25
 */
@Service
public class UserServiceImplProxy implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplProxy.class);

    @Autowired
    private RpcRequest rpcRequest;

    @Override
    public ResponseDTO<UserOutputDTO> get(RequestDTO<UserInputDTO> requestDTO) {
        NettyClientFuture nettyClientFuture = rpcRequest.send(requestDTO);
        // TODO 这里需要实现异步获取调用结果
        Object object = nettyClientFuture.get();
        logger.info("remote producer call result:{}", JSON.toJSONString(object));
        return object != null ? (ResponseDTO<UserOutputDTO>)object : null;
    }
}
