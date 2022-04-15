import java.time.Duration;
import java.util.Collections;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.flink.ApplicationRun;
import cn.zzy.flink.kafka.AgentServiceKafkaConsumer;

/**
 * @author zhaozuoyu
 * @date 2021/12/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class KafkaConsumerTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerTest.class);

    @Autowired
    private AgentServiceKafkaConsumer agentServiceKafkaConsumer;

    @Test
    public void receive() {
        String topic = "kafka";
        try {
            agentServiceKafkaConsumer.subscribe(Collections.singletonList(topic));
            logger.info("already subscribe topic {}", topic);
            ConsumerRecords<String, String> records = agentServiceKafkaConsumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                logger.info(" received message from partition:{} key:{} value:{} at offset:{}", record.partition(),
                    record.key(), record.value(), record.offset());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
