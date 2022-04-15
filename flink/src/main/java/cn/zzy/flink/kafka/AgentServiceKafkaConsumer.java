package cn.zzy.flink.kafka;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceKafkaConsumer extends KafkaConsumer<String, String> {

    public AgentServiceKafkaConsumer(Map<String, Object> configs) {
        super(configs);
    }

    public AgentServiceKafkaConsumer(Map<String, Object> configs, Deserializer<String> keyDeserializer,
        Deserializer<String> valueDeserializer) {
        super(configs, keyDeserializer, valueDeserializer);
    }

    public AgentServiceKafkaConsumer(Properties properties) {
        super(properties);
    }

    public AgentServiceKafkaConsumer(Properties properties, Deserializer<String> keyDeserializer,
        Deserializer<String> valueDeserializer) {
        super(properties, keyDeserializer, valueDeserializer);
    }
}
