package cn.zzy.flink.source;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.MQPullConsumerScheduleService;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.zzy.flink.dto.AgentServiceInputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/9
 */
public class AgentServiceRocketMQSourceFunctionBack extends RichParallelSourceFunction<AgentServiceInputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceRocketMQSourceFunctionBack.class);

    private String address;
    private String topic;
    private String group;
    private String tag;
    private static final BlockingQueue<AgentServiceInputDTO> blockingQueue = new ArrayBlockingQueue<>(10000);
    private String instanceName;

    private DefaultMQPushConsumer defaultMQPushConsumer;
    private DefaultMQPullConsumer defaultMQPullConsumer;
    private transient MQPullConsumerScheduleService consumerScheduleService;
    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Boolean init = Boolean.TRUE;

    public AgentServiceRocketMQSourceFunctionBack(String address, String topic, String group, String tag,
        String instanceName) {
        this.address = address;
        this.topic = topic;
        this.group = group;
        this.tag = tag;
        this.instanceName = instanceName;

        /*if (init.booleanValue()) {
            synchronized (init) {
                if (init.booleanValue()) {
                    RocketMQPollMessage rocketMQPollMessage = null;
                    try {
                        rocketMQPollMessage = new RocketMQPollMessage(address, topic, group, tag, instanceName);
                        rocketMQPollMessage.init();
                    } catch (MQClientException e) {
                        logger.error(e.getMessage(), e);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }*/
        logger.info("create new instance {}", this.toString());
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        logger.info("address:{},topic:{},group:{},tag:{},instanceName:{},this:{}", this.address, this.topic, this.group,
            this.tag, this.instanceName, this.toString());
        this.instanceName = UUID.randomUUID().toString().replace("-", "").toLowerCase();

        logger.info("开始启动rocket-mq消费者客户端 {}", this.toString());
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void run(final SourceContext<AgentServiceInputDTO> ctx) throws Exception {
        logger.info("execute {} method run address:{},topic:{},group:{},tag:{},instanceName:{}",
            this.getClass().getName(), this.address, this.topic, this.group, this.tag, this.instanceName);
        pull(ctx);
    }

    @Override
    public void cancel() {
        if (defaultMQPullConsumer != null) {
            defaultMQPullConsumer.shutdown();
            logger.info("already rocket-mq consumer instance closed! instanceName:{}",
                defaultMQPullConsumer.getInstanceName());
        }
        if (defaultMQPushConsumer != null) {
            defaultMQPushConsumer.shutdown();
            logger.info("already rocket-mq consumer instance closed! instanceName:{}",
                defaultMQPushConsumer.getInstanceName());
        }
        if (consumerScheduleService != null) {
            consumerScheduleService.shutdown();
            logger.info("already rocket-mq consumer instance closed! instanceName:{}",
                consumerScheduleService.getDefaultMQPullConsumer().getInstanceName());
        }
        logger.info("already rocket-mq consumer instance closed!");
    }

    /*private void pullSchedule(final SourceContext<AgentServiceInputDTO> ctx) throws MQClientException {
        consumerScheduleService = new MQPullConsumerScheduleService(this.group);
        // MQ NameService地址
        consumerScheduleService.getDefaultMQPullConsumer().setNamesrvAddr(this.address);
        consumerScheduleService.setMessageModel(MessageModel.CLUSTERING);
        consumerScheduleService.registerPullTaskCallback(this.topic, new PullTaskCallback() {
            @Override
            public void doPullTask(MessageQueue mq, PullTaskContext context) {
                MQPullConsumer consumer = context.getPullConsumer();
                try {
                    long offset = consumer.fetchConsumeOffset(mq, false);
                    if (offset < 0) {
                        offset = 0;
                    }
                    PullResult pullResult = consumer.pull(mq, tag, offset, 32);
                    logger.info("Consume from the queue:{} offset:{}", mq, offset);
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
                                    ctx.collect(agentServiceInputDTO);
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
                    // 设置下一下拉取的间隔时间
                    context.setPullNextDelayTimeMillis(1000);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
        consumerScheduleService.start();
    }*/

    private void pull(final SourceContext<AgentServiceInputDTO> ctx) throws MQClientException {
        defaultMQPullConsumer = new DefaultMQPullConsumer(this.group);
        defaultMQPullConsumer.setNamesrvAddr(this.address);
        defaultMQPullConsumer.setInstanceName(this.instanceName);
        defaultMQPullConsumer.start();
        // TODO change queue list when queue changed
        Set<MessageQueue> mqs = defaultMQPullConsumer.fetchSubscribeMessageQueues(this.topic);
        for (MessageQueue mq : mqs) {
            SINGLE_MQ:
            while (true) {
                try {
                    long offset = defaultMQPullConsumer.fetchConsumeOffset(mq, false);
                    if (offset < 0) {
                        offset = 0;
                    }
                    PullResult pullResult = defaultMQPullConsumer.pullBlockIfNotFound(mq, this.tag, offset, 16);
                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            List<MessageExt> messageExts = pullResult.getMsgFoundList();
                            for (MessageExt msg : messageExts) {
                                String msgId = msg.getMsgId();
                                String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                                logger.info("pull message msgId:{},body:{}", msgId, body);
                                if (StringUtils.isNotEmpty(body)) {
                                    AgentServiceInputDTO agentServiceInputDTO =
                                        objectMapper.readValue(body, AgentServiceInputDTO.class);
                                    ctx.collect(agentServiceInputDTO);
                                }
                            }
                            break;
                        case NO_MATCHED_MSG:
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        case OFFSET_ILLEGAL:
                            break;
                        default:
                            break;
                    }
                    defaultMQPullConsumer.updateConsumeOffset(mq, pullResult.getNextBeginOffset());
                } catch (Exception e) {
                    logger.info(e.getMessage(), e);
                }
            }
        }
    }

    private void push(final SourceContext<AgentServiceInputDTO> ctx) throws MQClientException, InterruptedException {
        defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setConsumerGroup(group);
        defaultMQPushConsumer.setNamesrvAddr(address);
        defaultMQPushConsumer.setInstanceName(instanceName);
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                if (msgs == null || msgs.size() <= 0) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                for (MessageExt msg : msgs) {
                    try {
                        String msgId = msg.getMsgId();
                        String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                        logger.info("received push message from broker msgId:{},body:{}", msgId, body);
                        if (StringUtils.isNotEmpty(body)) {
                            AgentServiceInputDTO agentServiceInputDTO =
                                objectMapper.readValue(body, AgentServiceInputDTO.class);
                            ctx.collect(agentServiceInputDTO);
                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    } catch (Error e) {
                        logger.error(e.getMessage(), e);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.subscribe(topic, tag);
        defaultMQPushConsumer.start();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();

    }

    /**
     * please use rocket-mq pull mode
     */
    public static class RocketMQPollMessage extends DefaultMQPushConsumer {
        private String address;
        private String topic;
        private String group;
        private String tag;
        private String instanceName;

        public RocketMQPollMessage(String address, String topic, String group, String tag, String instanceName)
            throws MQClientException {
            this.address = address;
            this.topic = topic;
            this.group = group;
            this.tag = tag;
            this.instanceName = instanceName;
        }

        public void init() throws MQClientException {
            super.setConsumerGroup(group);
            super.setNamesrvAddr(address);
            super.setInstanceName(instanceName);
            super.registerMessageListener(builderListener());
            super.subscribe(topic, tag);
            super.start();
            logger.info("rocket-mq consumer started!");
        }

        private MessageListenerConcurrently builderListener() {
            return new MessageListenerConcurrently() {
                @Override
                public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                    ConsumeConcurrentlyContext context) {
                    if (msgs == null || msgs.size() <= 0) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    for (MessageExt msg : msgs) {
                        try {
                            String msgId = msg.getMsgId();
                            String body = new String(msg.getBody(), Charset.forName("UTF-8"));
                            logger.info("received push message from broker msgId:{},body:{}", msgId, body);
                            if (StringUtils.isNotEmpty(body)) {
                                AgentServiceInputDTO agentServiceInputDTO =
                                    objectMapper.readValue(body, AgentServiceInputDTO.class);
                                blockingQueue.put(agentServiceInputDTO);
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        } catch (Error e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            };
        }
    }

}
