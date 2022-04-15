package cn.zzy.flink.rocketmq;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;

import cn.zzy.flink.dto.AgentServiceInputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
public class AgentServiceRocketMQConsumer extends DefaultMQPushConsumer {
    @Value("${rocket-mq.address}")
    private String rocketMQAddress;
    @Value("${rocket-mq.topic}")
    private String rocketMQTopic;
    @Value("${rocket-mq.consumer.group}")
    private String rocketMQConsumerGroup;
    @Value("${rocket-mq.consumer.tag}")
    private String rocketMQConsumerTag;
    @Value("${rocket-mq.consumer.instance-name}")
    private String rocketMQConsumerInstanceName;

    private SourceFunction.SourceContext<AgentServiceInputDTO> ctx;

    public AgentServiceRocketMQConsumer(String rocketMQAddress, String rocketMQTopic, String rocketMQConsumerGroup,
        String rocketMQConsumerTag, String rocketMQConsumerInstanceName) {
        this.rocketMQAddress = rocketMQAddress;
        this.rocketMQTopic = rocketMQTopic;
        this.rocketMQConsumerGroup = rocketMQConsumerGroup;
        this.rocketMQConsumerTag = rocketMQConsumerTag;
        this.rocketMQConsumerInstanceName = rocketMQConsumerInstanceName;
    }
}
