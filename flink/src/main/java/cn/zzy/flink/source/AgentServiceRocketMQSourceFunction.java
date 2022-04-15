package cn.zzy.flink.source;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.*;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.zzy.flink.dto.AgentServiceInputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
public class AgentServiceRocketMQSourceFunction extends RichParallelSourceFunction<AgentServiceInputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceRocketMQSourceFunction.class);

    private static MQPullConsumerScheduleService consumerScheduleService;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static Boolean initialized = Boolean.FALSE;
    private boolean running = true;

    private String address;
    private String topic;
    private String group;
    private String tag;
    private String instanceName;

    public AgentServiceRocketMQSourceFunction(String address, String topic, String group, String tag,
        String instanceName) {
        this.address = address;
        this.topic = topic;
        this.group = group;
        this.tag = tag;
        this.instanceName = instanceName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        logger.info("开始启动rocket-mq消费者客户端 {}", this.toString());
        if (!initialized.booleanValue()) {
            synchronized (initialized) {
                if (!initialized.booleanValue()) {
                    consumerScheduleService = new MQPullConsumerScheduleService(this.group);
                    consumerScheduleService.getDefaultMQPullConsumer().setNamesrvAddr(this.address);
                    consumerScheduleService.getDefaultMQPullConsumer().registerMessageQueueListener(this.topic, null);
                    consumerScheduleService.setMessageModel(MessageModel.CLUSTERING);
                    consumerScheduleService.setPullThreadNums(4);
                    try {
                        consumerScheduleService.start();
                        initialized = Boolean.TRUE;
                    } catch (MQClientException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        while (!initialized.booleanValue()) {
            Thread.sleep(100);
        }
    }

    @Override
    public void run(final SourceFunction.SourceContext<AgentServiceInputDTO> ctx) throws InterruptedException {
        logger.info("execute {} method run address:{},topic:{},group:{},tag:{},instanceName:{}",
            this.getClass().getName(), address, topic, group, tag, instanceName);
        consumerScheduleService.registerPullTaskCallback(topic, new PullTaskCallback() {
            @Override
            public void doPullTask(MessageQueue mq, PullTaskContext context) {
                MQPullConsumer consumer = context.getPullConsumer();
                try {
                    long offset = consumer.fetchConsumeOffset(mq, false);
                    if (offset < 0) {
                        offset = 0;
                    }
                    PullResult pullResult = consumer.pullBlockIfNotFound(mq, tag, offset, 32);
                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            List<MessageExt> messageExts = pullResult.getMsgFoundList();
                            for (MessageExt msg : messageExts) {
                                String msgId = msg.getMsgId();
                                String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                                logger.info("received message msgId:{},body:{}", msgId, body);
                                if (StringUtils.isNotEmpty(body)) {
                                    AgentServiceInputDTO agentServiceInputDTO =
                                        objectMapper.readValue(body, AgentServiceInputDTO.class);
                                    try {
                                        // agentServiceInputDTO.setCreatetime(new Date());
                                        ctx.collect(agentServiceInputDTO);
                                    } catch (Exception e) {
                                        logger.error(e.getMessage(), e);
                                    }
                                }
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                    consumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
                    context.setPullNextDelayTimeMillis(1000);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        while (isRunning()) {
            Thread.sleep(100);
        }
        logger.info("***************already SourceContext closed!");
    }

    @Override
    public void cancel() {
        logger.info("********************* rocket-mq consumer instance closing *********************");
        if (consumerScheduleService != null) {
            consumerScheduleService.shutdown();
            logger.info("already rocket-mq consumer instance closed! instanceName:{}",
                consumerScheduleService.toString());
        }
        setRunning(false);
        logger.info("********************* already rocket-mq consumer instance closed *********************");
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
