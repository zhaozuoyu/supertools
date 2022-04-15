package cn.zzy.flink.rocketmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
public class AgentServiceRocketMQProducer extends DefaultMQProducer {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceRocketMQProducer.class);
    private String address;
    private String topic;
    private String group;

    public AgentServiceRocketMQProducer(String address, String topic, String group) {
        this.address = address;
        this.topic = topic;
        this.group = group;
    }

    private void init() throws MQClientException {
        super.setNamesrvAddr(address);
        super.setProducerGroup(group);
        super.start();
        logger.info("already agent-service-rocket-mq-producer started!");
    }

    private void destroy() {
        super.shutdown();
    }
}
