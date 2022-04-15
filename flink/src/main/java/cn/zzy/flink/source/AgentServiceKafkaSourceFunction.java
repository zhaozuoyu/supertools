package cn.zzy.flink.source;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zzy.flink.dto.AgentServiceInputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceKafkaSourceFunction extends RichParallelSourceFunction<AgentServiceInputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceKafkaSourceFunction.class);

    private String bootstrapServers;
    private String topic;
    private String groupId;
    private String clientId;

    private KafkaConsumer<String, String> kafkaConsumer;

    public AgentServiceKafkaSourceFunction(String bootstrapServers, String topic, String groupId, String clientId) {
        this.bootstrapServers = bootstrapServers;
        this.topic = topic;
        this.groupId = groupId;
        this.clientId = clientId;
    }

    private void init() {
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
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList(topic));
        logger.info("already subscribe topic {}", topic);
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void run(final SourceContext<AgentServiceInputDTO> ctx) throws Exception {
        if (kafkaConsumer == null) {
            synchronized (this) {
                if (kafkaConsumer == null) {
                    init();
                }
            }
        }
        KafkaPollMessage kafkaPollMessage = new KafkaPollMessage(kafkaConsumer, ctx);
        new Thread(kafkaPollMessage).start();
    }

    @Override
    public void cancel() {

    }

    private static class KafkaPollMessage implements Runnable {
        private KafkaConsumer<String, String> kafkaConsumer;
        private SourceContext<AgentServiceInputDTO> ctx;

        public KafkaPollMessage(KafkaConsumer<String, String> kafkaConsumer, SourceContext<AgentServiceInputDTO> ctx) {
            this.kafkaConsumer = kafkaConsumer;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            while (true) {
                logger.info("pull msg");
                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));
                for (ConsumerRecord<String, String> record : records) {
                    logger.info(" received message from partition:{} key:{} value:{} at offset:{}", record.partition(),
                        record.key(), record.value(), record.offset());
                    if (StringUtils.isNotEmpty(record.value())) {
                        AgentServiceInputDTO agentServiceInputDTO =
                            JSON.parseObject(record.value(), AgentServiceInputDTO.class);
                        ctx.collect(agentServiceInputDTO);
                    }
                }
            }
        }
    }

}
