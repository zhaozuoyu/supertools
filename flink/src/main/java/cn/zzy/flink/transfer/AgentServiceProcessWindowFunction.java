package cn.zzy.flink.transfer;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.zzy.flink.dto.AgentServiceInputDTO;

/**
 * 注意，该事件在一个窗口结束时触发
 * 
 * @author zhaozuoyu
 * @date 2021/12/8
 */
public class AgentServiceProcessWindowFunction
    extends ProcessWindowFunction<AgentServiceInputDTO, AgentServiceInputDTO, String, TimeWindow> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceProcessWindowFunction.class);

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        threadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public void process(String s, Context context, Iterable<AgentServiceInputDTO> iterable,
        Collector<AgentServiceInputDTO> collector) throws Exception {
        logger.info("******************* execute process *******************");
        Iterator<AgentServiceInputDTO> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            AgentServiceInputDTO next = iterator.next();
            logger.info("process element:{}", objectMapper.writeValueAsString(next));
            collector.collect(next);
        }
    }
}
