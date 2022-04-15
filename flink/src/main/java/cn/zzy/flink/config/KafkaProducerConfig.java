package cn.zzy.flink.config;

import java.util.Properties;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import cn.zzy.flink.kafka.AgentServiceKafkaProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
/*@Configuration*/
public class KafkaProducerConfig {

    @Value("${kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public AgentServiceKafkaProducer agentServiceProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "agent-service-producer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        AgentServiceKafkaProducer producer = new AgentServiceKafkaProducer(props);
        return producer;
    }
}
