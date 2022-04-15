package cn.zzy.flink.aggregate;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.zzy.flink.dto.AgentServiceInputDTO;
import cn.zzy.flink.dto.AgentServiceOutputDTO;

/**
 * accumulator use please see http://www.itxm.cn/post/26260.html
 *
 * @author zhaozuoyu
 * @date 2021/12/8
 */
public class AgentServiceAggregateFunction
    implements AggregateFunction<AgentServiceInputDTO, AgentServiceAccumulator, AgentServiceOutputDTO> {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceAggregateFunction.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static final String ZERO_SESSION_TYPE = "0";
    private static final String ONE_SESSION_TYPE = "1";
    private static final String END_STATUS = "end";
    private static final String FIRST_LEVEL = "1";
    private static final String SECOND_LEVEL = "2";
    private static final String THIRD_LEVEL = "3";
    private static final String FOURTH_LEVEL = "4";
    private static final String FIVE_LEVEL = "5";
    private static final int AVG_FIRST_REPLY_TIME = 90000;

    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<>();
    static {
        threadLocal.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public AgentServiceAccumulator createAccumulator() {
        logger.info("init an accumulator");
        return new AgentServiceAccumulator();
    }

    @Override
    public AgentServiceAccumulator add(AgentServiceInputDTO value, AgentServiceAccumulator accumulator) {
        AgentServiceAccumulator latest = new AgentServiceAccumulator();
        try {
            logger.info("add element to accumulator value:{}", objectMapper.writeValueAsString(value));
            latest.setTotalElementNumber(accumulator.getTotalElementNumber());
            if (ZERO_SESSION_TYPE.equals(value.getSessiontype())) {
                latest.setVisitCount(accumulator.getVisitCount() + 1);
            }
            if (value.getAgentfirstreplys() > 0 && ZERO_SESSION_TYPE.equals(value.getSessiontype())) {
                latest.setServiceCount(accumulator.getServiceCount() + 1);
            }
            if (value.getAgentfirstreplys() > 0 && value.getAvgfirstreplytime() <= AVG_FIRST_REPLY_TIME) {
                latest.setOnTimeCount(accumulator.getOnTimeCount() + 1);
            }
            latest.setAvgfirstreplytimes(accumulator.getAvgfirstreplytimes() + value.getAvgfirstreplytime());
            if (value.getAgentfirstreplys() > 0) {
                latest.setOnTimeCount(accumulator.getOnTimeCount() + 1);
            }
            if (value.getAgentfirstreplys() > 0) {
                latest.setValidWaitTimeNumber(accumulator.getValidWaitTimeNumber() + 1);
                latest.setValidWaitTimeValue(accumulator.getValidWaitTimeValue() + value.getWaittingtime());
            }
            if (value.getAgentfirstreplys() > 0) {
                latest.setValidSessionTimeNumber(accumulator.getValidSessionTimeNumber() + 1);
                latest.setValidSessionTimeValue(accumulator.getValidSessionTimeValue() + value.getSessiontimes());
            }
            if (FIVE_LEVEL.equals(value.getSatislevel()) && END_STATUS.equals(value.getStatus())) {
                latest.setFiveLevelNumber(accumulator.getFiveLevelNumber() + 1);
            }
            if (FOURTH_LEVEL.equals(value.getSatislevel()) && END_STATUS.equals(value.getStatus())) {
                latest.setFourthLevelNumber(accumulator.getFourthLevelNumber() + 1);
            }
            if (THIRD_LEVEL.equals(value.getSatislevel()) && END_STATUS.equals(value.getStatus())) {
                latest.setThirdLevelNumber(accumulator.getThirdLevelNumber() + 1);
            }
            if (SECOND_LEVEL.equals(value.getSatislevel()) && END_STATUS.equals(value.getStatus())) {
                latest.setSecondLevelNumber(accumulator.getSecondLevelNumber() + 1);
            }
            if (FIRST_LEVEL.equals(value.getSatislevel()) && END_STATUS.equals(value.getStatus())) {
                latest.setFirstLevelNumber(accumulator.getFirstLevelNumber() + 1);
            }
            if (StringUtils.isEmpty(accumulator.getDate())) {
                if (value.getCreatetime() != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    accumulator.setDate(sdf.format(value.getCreatetime()));
                }
            }
            if (ONE_SESSION_TYPE.equals(value.getSessiontype())) {
                latest.setAgentCount(accumulator.getAgentCount() + 1);
            }
            if (ONE_SESSION_TYPE.equals(value.getSessiontype()) && value.getUserasks() > 0) {
                latest.setUserReplyCount(accumulator.getUserReplyCount() + 1);
            }

            latest.setDate(value.getCreatetime() != null ? threadLocal.get().format(value.getCreatetime()) : null);
            logger.info("add element to accumulator result:{}", objectMapper.writeValueAsString(latest));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return latest;
    }

    @Override
    public AgentServiceOutputDTO getResult(AgentServiceAccumulator accumulator) {
        AgentServiceOutputDTO outputDTO = new AgentServiceOutputDTO();
        try {
            logger.info("get a output type from accumulator {}", objectMapper.writeValueAsString(accumulator));
            outputDTO.setDate(accumulator.getDate());
            outputDTO.setVisitCount(accumulator.getVisitCount());
            outputDTO.setServiceCount(accumulator.getServiceCount());
            outputDTO.setAgentCount(accumulator.getAgentCount());
            // 注意：评均等待时长和评价会话时长均无法再聚合过程中计算出结果，这里进行特殊处理
            if (accumulator.getValidWaitTimeNumber() > 0) {
                accumulator
                    .setAvgWaitingTime(accumulator.getValidWaitTimeValue() / accumulator.getValidWaitTimeNumber());
            }
            if (accumulator.getValidSessionTimeNumber() > 0) {
                accumulator.setAvgSessionTime(
                    accumulator.getValidSessionTimeValue() / accumulator.getValidSessionTimeNumber());
            }
            outputDTO.setAvgWaitingTime(accumulator.getAvgWaitingTime());
            outputDTO.setOnTimeCount(accumulator.getOnTimeCount());
            long totalLevelNumber = accumulator.getFiveLevelNumber() * 5 + accumulator.getFourthLevelNumber() * 4
                + accumulator.getThirdLevelNumber() * 3 + accumulator.getSecondLevelNumber() * 2
                + accumulator.getFirstLevelNumber();
            if (totalLevelNumber > 0) {
                long levelNumber = accumulator.getFiveLevelNumber() * 5 + accumulator.getFourthLevelNumber() * 4;
                double v = (levelNumber / totalLevelNumber);
                outputDTO.setSatisRate(v);
            }
            if (accumulator.getServiceCount() > 0) {
                double v = (accumulator.getAvgfirstreplytimes() / accumulator.getServiceCount());
                outputDTO.setAvgfirstreplytime(Math.round(v));
            }
            outputDTO.setAvgDailougTime(accumulator.getAvgSessionTime());
            logger.info("get a output type result {}", objectMapper.writeValueAsString(outputDTO));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return outputDTO;
    }

    @Override
    public AgentServiceAccumulator merge(AgentServiceAccumulator a, AgentServiceAccumulator b) {
        AgentServiceAccumulator latest = new AgentServiceAccumulator();
        try {
            logger.info("merge two accumulator a {}", objectMapper.writeValueAsString(a));
            logger.info("merge two accumulator b {}", objectMapper.writeValueAsString(b));

            latest.setTotalElementNumber(a.getTotalElementNumber() + b.getTotalElementNumber());
            latest.setVisitCount(a.getVisitCount() + b.getVisitCount());
            latest.setServiceCount(a.getServiceCount() + b.getServiceCount());
            latest.setOnTimeCount(a.getOnTimeCount() + a.getOnTimeCount());
            latest.setAvgfirstreplytimes(a.getAvgfirstreplytimes() + b.getAvgfirstreplytimes());
            latest.setOnTimeCount(a.getOnTimeCount() + b.getOnTimeCount());
            latest.setValidWaitTimeNumber(a.getValidWaitTimeNumber() + b.getValidWaitTimeNumber());
            latest.setValidWaitTimeValue(a.getValidWaitTimeValue() + b.getValidWaitTimeValue());
            latest.setValidSessionTimeNumber(a.getValidSessionTimeNumber() + b.getValidSessionTimeNumber());
            latest.setValidSessionTimeValue(a.getValidSessionTimeValue() + b.getValidSessionTimeValue());

            latest.setFiveLevelNumber(a.getFiveLevelNumber() + b.getFiveLevelNumber());
            latest.setFourthLevelNumber(a.getFourthLevelNumber() + b.getFourthLevelNumber());
            latest.setThirdLevelNumber(a.getThirdLevelNumber() + b.getThirdLevelNumber());
            latest.setSecondLevelNumber(a.getSecondLevelNumber() + b.getSecondLevelNumber());
            latest.setFirstLevelNumber(a.getFirstLevelNumber() + b.getFirstLevelNumber());
            latest.setDate(StringUtils.isNotEmpty(latest.getDate()) ? a.getDate() : b.getDate());

            latest.setAgentCount(a.getAgentCount() + b.getAgentCount());
            latest.setUserReplyCount(a.getUserReplyCount() + b.getUserReplyCount());
            logger.info("merge two accumulator result {}", objectMapper.writeValueAsString(latest));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return latest;
    }
}
