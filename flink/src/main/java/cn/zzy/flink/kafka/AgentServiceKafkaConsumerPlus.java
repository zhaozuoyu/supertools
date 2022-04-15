package cn.zzy.flink.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import kafka.utils.ShutdownableThread;

/**
 * @author zhaozuoyu
 * @date 2021/12/7
 */
// @Component
public class AgentServiceKafkaConsumerPlus extends ShutdownableThread {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceKafkaConsumerPlus.class);

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.consumer.topic}")
    private String topic;
    @Value("${kafka.consumer.group-id}")
    private String groupId;
    @Value("${kafka.consumer.client-id}")
    private String clientId;

    private KafkaConsumer<String, String> kafkaConsumer;

    public AgentServiceKafkaConsumerPlus() {
        this(null, false);
    }

    public AgentServiceKafkaConsumerPlus(String name, boolean isInterruptible) {
        super(name, isInterruptible);
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, this.clientId);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.kafkaConsumer = new KafkaConsumer<>(props);
    }

    @Override
    public void doWork() {
        try {
            kafkaConsumer.subscribe(Collections.singletonList(topic));
            logger.info("already subscribe topic {}", topic);
            ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info(" received message from partition:{} key:{} value:{} at offset:{}", record.partition(),
                    record.key(), record.value(), record.offset());
                // TODO process msg
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
