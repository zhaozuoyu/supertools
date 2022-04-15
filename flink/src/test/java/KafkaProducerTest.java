import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import cn.zzy.flink.ApplicationRun;
import cn.zzy.flink.kafka.AgentServiceKafkaProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class KafkaProducerTest {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);

    @Autowired
    private AgentServiceKafkaProducer agentServiceKafkaProducer;

    @Test
    public void send() {
        try {
            String topic = "kafka";
            String key = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String msg = "hello world!";
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, msg);
            logger.info("msg:{}", JSON.toJSONString(producerRecord));
            Future<RecordMetadata> future = agentServiceKafkaProducer.send(producerRecord);
            logger.info("send msg to kafka is successful!");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
