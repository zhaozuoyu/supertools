package cn.zzy.flink.controller;

import java.util.UUID;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.zzy.flink.dto.AgentServiceInputDTO;
import cn.zzy.flink.kafka.AgentServiceKafkaProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/7
 */
@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @Value("${kafka.producer.topic}")
    private String topic;

    // @Autowired
    private AgentServiceKafkaProducer agentServiceKafkaProducer;

    @PostMapping("/agent-service")
    public ResponseEntity<String> agentService(@RequestBody AgentServiceInputDTO agentServiceInputDTO) {
        logger.info("post agent-service data :{}", JSON.toJSONString(agentServiceInputDTO));
        ResponseEntity<String> responseEntity;
        try {
            String key = UUID.randomUUID().toString().replace("-", "").toUpperCase();
            String msg = JSON.toJSONString(agentServiceInputDTO);
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, key, msg);
            Future<RecordMetadata> future = agentServiceKafkaProducer.send(producerRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (metadata != null) {
                        logger.info("message:{} sent to partition:{} offset:{}", key, metadata.partition(),
                            metadata.offset());
                    } else {
                        exception.printStackTrace();
                    }
                }
            });
            responseEntity = new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseEntity = new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("post agent-service data result:{}", JSON.toJSONString(responseEntity));
        return responseEntity;
    }
}
