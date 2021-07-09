package cn.zzy.grpc.consumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import cn.zzy.grpc.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * @author zhaozuoyu
 * @date 2021/7/7
 */
@Configuration
public class GoogleRpcConfig {

    private static final Logger logger = LoggerFactory.getLogger(GoogleRpcConfig.class);

    // @Bean
    public ManagedChannel managedChannel() {
        ManagedChannel channel = NettyChannelBuilder.forAddress("127.0.0.1", 9090).build();
        logger.info("channel builder success!");
        return channel;
    }

    // @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub() {
        UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub =
            UserServiceGrpc.newBlockingStub(managedChannel());
        return userServiceBlockingStub;
    }

}
