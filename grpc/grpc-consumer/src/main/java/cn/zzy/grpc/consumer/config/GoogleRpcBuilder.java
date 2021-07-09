package cn.zzy.grpc.consumer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.ManagedChannel;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;

/**
 * @author zhaozuoyu
 * @date 2021/7/7
 */
public class GoogleRpcBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GoogleRpcBuilder.class);

    private ManagedChannel channel = null;

    public void builderChannel() {
        try {
            if (channel == null) {
                synchronized (this) {
                    channel = NettyChannelBuilder.forAddress("127.0.0.1", 9090).build();
                    logger.info("channel builder success!");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
