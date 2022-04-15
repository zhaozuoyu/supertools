package cn.zzy.flink.sink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.zzy.flink.dto.AgentServiceOutputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/8
 */
public class AgentServiceSinkWindowFunction extends RichSinkFunction<AgentServiceOutputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceSinkWindowFunction.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void invoke(AgentServiceOutputDTO value, Context context) throws Exception {
        logger.info("sink data:{}", objectMapper.writeValueAsString(value));
        // TODO output db
    }

}
