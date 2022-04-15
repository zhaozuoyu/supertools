package cn.zzy.flink.job;

import java.time.Duration;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import cn.zzy.flink.aggregate.AgentServiceAggregateFunction;
import cn.zzy.flink.dto.AgentServiceInputDTO;
import cn.zzy.flink.sink.AgentServiceSinkWindowFunction;
import cn.zzy.flink.source.AgentServiceRocketMQSourceFunction;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
@Component
public class AgentServiceJobHandler implements Runnable, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceJobHandler.class);

    @Value("${kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.consumer.topic}")
    private String kafkaTopic;
    @Value("${kafka.consumer.group-id}")
    private String kafkaConsumerGroup;
    @Value("${kafka.consumer.client-id}")
    private String kafkaClientId;

    @Value("${rocket-mq.address}")
    private String rocketMQAddress;
    @Value("${rocket-mq.topic}")
    private String rocketMQTopic;
    @Value("${rocket-mq.consumer.group}")
    private String rocketMQConsumerGroup;
    @Value("${rocket-mq.consumer.tag}")
    private String rocketMQConsumerTag;
    @Value("${rocket-mq.consumer.instance-name}")
    private String rocketMQConsumerInstanceName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        /*new AgentServiceRocketMQSourceFunction(this.rocketMQAddress, this.rocketMQTopic,
                this.rocketMQConsumerGroup, this.rocketMQConsumerTag)*/
        /*new AgentServiceKafkaSourceFunction(this.bootstrapServers, this.kafkaTopic,
               this.kafkaConsumerGroup, this.kafkaClientId)*/
        /*DataStream<AgentServiceInputDTO> dataStream = env
            .addSource(new AgentServiceRocketMQSourceFunction(this.rocketMQAddress, this.rocketMQTopic,
                this.rocketMQConsumerGroup, this.rocketMQConsumerTag, this.rocketMQConsumerInstanceName))
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<AgentServiceInputDTO>forBoundedOutOfOrderness(Duration.ofSeconds(40))
                    .withTimestampAssigner((event, timestamp) -> event.getCreatetime().getTime()))
            .name("agentService");
        
        DataStream<AgentServiceInputDTO> processWindowFunction =
            dataStream.keyBy(AgentServiceInputDTO::getOrgi).window(TumblingEventTimeWindows.of(Time.seconds(40)))
                .process(new AgentServiceProcessWindowFunction()).name("agentServiceProcessWindowFunction");
        // aggregation data
        DataStream<AgentServiceOutputDTO> aggregate = processWindowFunction
            .windowAll(TumblingEventTimeWindows.of(Time.seconds(40))).aggregate(new AgentServiceAggregateFunction());
        // 输出结果
        aggregate.addSink(new AgentServiceSinkWindowFunction()).name("agentServiceSinkWindowFunction");*/
        env.addSource(new AgentServiceRocketMQSourceFunction(this.rocketMQAddress, this.rocketMQTopic,
            this.rocketMQConsumerGroup, this.rocketMQConsumerTag, this.rocketMQConsumerInstanceName))
            .name("agentServiceRocketMQSourceFunction")
            .assignTimestampsAndWatermarks(
                WatermarkStrategy.<AgentServiceInputDTO>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                    .withTimestampAssigner(new SerializableTimestampAssigner<AgentServiceInputDTO>() {
                        @Override
                        public long extractTimestamp(AgentServiceInputDTO element, long recordTimestamp) {
                            return element.getCreatetime().getTime();
                        }

                    }))
            .setParallelism(1).keyBy(AgentServiceInputDTO::getOrgi)
            .window(TumblingEventTimeWindows.of(Time.seconds(20))).aggregate(new AgentServiceAggregateFunction())
            .name("agentServiceAggregateFunction").addSink(new AgentServiceSinkWindowFunction())
            .name("agentServiceSinkWindowFunction");
        try {
            env.execute("agentServiceJobHandler");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
