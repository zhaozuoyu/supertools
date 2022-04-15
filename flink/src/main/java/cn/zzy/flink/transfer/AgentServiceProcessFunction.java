package cn.zzy.flink.transfer;

import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;

import cn.zzy.flink.dto.AgentServiceInputDTO;
import cn.zzy.flink.dto.AgentServiceOutputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceProcessFunction extends ProcessFunction<AgentServiceInputDTO, AgentServiceOutputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceProcessFunction.class);

    @Override
    public void processElement(AgentServiceInputDTO value, Context ctx, Collector<AgentServiceOutputDTO> out)
        throws Exception {
        logger.info("process element:{}", JSON.toJSONString(value));
        if (value != null) {
            // TODO process input element
            AgentServiceOutputDTO outputDTO = new AgentServiceOutputDTO();
            BeanUtils.copyProperties(value, outputDTO);
            out.collect(outputDTO);
        }
    }
}
