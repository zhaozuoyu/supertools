package cn.zzy.flink.sink;

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zzy.flink.dto.AgentServiceOutputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceSinkFunction extends RichSinkFunction<AgentServiceOutputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceSinkFunction.class);

    @Override
    public void invoke(AgentServiceOutputDTO value, Context context) throws Exception {
        logger.info("sink data:{}", JSON.toJSONString(value));
        if (value != null) {
            // TODO output data to hive
        }
    }
}
