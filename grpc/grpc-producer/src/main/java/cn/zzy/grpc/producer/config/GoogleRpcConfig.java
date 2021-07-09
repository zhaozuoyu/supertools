package cn.zzy.grpc.producer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaozuoyu
 * @date 2021/7/7
 */
@Configuration
public class GoogleRpcConfig {

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public GoogleRpcBuilder googleRpcBuilder() {
        GoogleRpcBuilder googleRpcBuilder = new GoogleRpcBuilder();
        return googleRpcBuilder;
    }

}
