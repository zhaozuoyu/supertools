package cn.zzy.flink.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhaozuoyu
 * @date 2021/12/10
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService flinkExecutorService() {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);
        return scheduledExecutorService;
    }

}
