package cn.zzy.grpc.producer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzy.grpc.producer.service.UserServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * @author zhaozuoyu
 * @date 2021/7/7
 */
public class GoogleRpcBuilder {

    private static final Logger logger = LoggerFactory.getLogger(GoogleRpcBuilder.class);

    private Server server = null;

    public void init() {
        try {
            if (server == null) {
                synchronized (this) {
                    server = ServerBuilder.forPort(9090).addService(new UserServiceImpl()).build();
                    server.start();
                    logger.info("google remote procedure call server producer had started already!");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void destroy() {
        if (server != null) {
            try {
                server.awaitTermination();
                logger.info("google remote procedure call server producer had closed already!");
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
