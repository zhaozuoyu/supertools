package cn.zzy.flink.dto;

import java.io.Serializable;

/**
 * @author zhaozuoyu
 * @date 2021/12/6
 */
public class AgentServiceOutputDTO implements Serializable {

    private long visitCount;
    private long serviceCount;
    private long agentCount;
    private long userReplyCount;
    private long avgWaitingTime;
    private Double satisRate;
    private long onTimeCount;
    private long avgfirstreplytime;
    private long avgfirstreplytimes;
    private long avgDailougTime;
    private String date;

    public long getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(long visitCount) {
        this.visitCount = visitCount;
    }

    public long getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(long serviceCount) {
        this.serviceCount = serviceCount;
    }

    public long getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(long agentCount) {
        this.agentCount = agentCount;
    }

    public long getUserReplyCount() {
        return userReplyCount;
    }

    public void setUserReplyCount(long userReplyCount) {
        this.userReplyCount = userReplyCount;
    }

    public long getAvgWaitingTime() {
        return avgWaitingTime;
    }

    public void setAvgWaitingTime(long avgWaitingTime) {
        this.avgWaitingTime = avgWaitingTime;
    }

    public Double getSatisRate() {
        return satisRate;
    }

    public void setSatisRate(Double satisRate) {
        this.satisRate = satisRate;
    }

    public long getOnTimeCount() {
        return onTimeCount;
    }

    public void setOnTimeCount(long onTimeCount) {
        this.onTimeCount = onTimeCount;
    }

    public long getAvgfirstreplytime() {
        return avgfirstreplytime;
    }

    public void setAvgfirstreplytime(long avgfirstreplytime) {
        this.avgfirstreplytime = avgfirstreplytime;
    }

    public long getAvgfirstreplytimes() {
        return avgfirstreplytimes;
    }

    public void setAvgfirstreplytimes(long avgfirstreplytimes) {
        this.avgfirstreplytimes = avgfirstreplytimes;
    }

    public long getAvgDailougTime() {
        return avgDailougTime;
    }

    public void setAvgDailougTime(long avgDailougTime) {
        this.avgDailougTime = avgDailougTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
