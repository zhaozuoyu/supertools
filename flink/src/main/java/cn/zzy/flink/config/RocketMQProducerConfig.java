package cn.zzy.flink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.zzy.flink.rocketmq.AgentServiceRocketMQProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
@Configuration
public class RocketMQProducerConfig {

    @Value("${rocket-mq.address}")
    private String address;
    @Value("${rocket-mq.topic}")
    private String topic;
    @Value("${rocket-mq.producer.group}")
    private String group;

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public AgentServiceRocketMQProducer agentServiceRocketMQProducer() {
        AgentServiceRocketMQProducer producer = new AgentServiceRocketMQProducer(address, topic, group);
        return producer;
    }

}
