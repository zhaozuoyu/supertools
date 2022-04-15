package cn.zzy.flink.controller;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

import cn.zzy.flink.dto.AgentServiceInputDTO;
import cn.zzy.flink.rocketmq.AgentServiceRocketMQProducer;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
@RestController
@RequestMapping("/rocket-mq")
public class RocketMQController {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQController.class);

    @Value("${rocket-mq.topic}")
    private String rocketMQTopic;

    @Autowired
    private AgentServiceRocketMQProducer agentServiceRocketMQProducer;

    @PostMapping("/agent-service")
    public ResponseEntity<String> agentService(@RequestBody AgentServiceInputDTO agentServiceInputDTO) {
        // logger.info("post agent-service data :{}", JSON.toJSONString(agentServiceInputDTO));
        ResponseEntity<String> responseEntity;
        try {
            String msg = JSON.toJSONString(agentServiceInputDTO);
            Message message = new Message();
            message.setTopic(rocketMQTopic);
            message.setBody(msg.getBytes(Charset.forName("UTF-8")));
            SendResult sendResult = agentServiceRocketMQProducer.send(message);
            // logger.info(JSON.toJSONString(sendResult));
            responseEntity = new ResponseEntity<>("success", HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseEntity = new ResponseEntity<>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // logger.info("post agent-service data result:{}", JSON.toJSONString(responseEntity));
        return responseEntity;
    }
}
