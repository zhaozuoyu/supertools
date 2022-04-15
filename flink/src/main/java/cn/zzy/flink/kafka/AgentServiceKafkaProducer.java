package cn.zzy.flink.kafka;

import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.Serializer;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceKafkaProducer extends KafkaProducer<String, String> {

    public AgentServiceKafkaProducer(Map<String, Object> configs) {
        super(configs);
    }

    public AgentServiceKafkaProducer(Map<String, Object> configs, Serializer<String> keySerializer,
        Serializer<String> valueSerializer) {
        super(configs, keySerializer, valueSerializer);
    }

    public AgentServiceKafkaProducer(Properties properties) {
        super(properties);
    }

    public AgentServiceKafkaProducer(Properties properties, Serializer<String> keySerializer,
        Serializer<String> valueSerializer) {
        super(properties, keySerializer, valueSerializer);
    }

}
