package cn.zzy.flink.aggregate;

import java.io.Serializable;

/**
 * @author zhaozuoyu
 * @date 2021/12/10
 */
public class AgentServiceAccumulator implements Serializable {

    private long visitCount;
    private long serviceCount;
    private long agentCount;
    private long userReplyCount;
    private long avgWaitingTime;
    private long avgSessionTime;
    private Double satisRate;
    private long onTimeCount;
    private long avgfirstreplytime;
    private long avgfirstreplytimes;

    private long avgDailougTime;
    private String date;
    /**
     * 处理的总元素个数
     */
    private long totalElementNumber;
    private long validWaitTimeNumber;
    private long validWaitTimeValue;
    private long validSessionTimeNumber;
    private long validSessionTimeValue;
    private long firstLevelNumber;
    private long secondLevelNumber;
    private long thirdLevelNumber;
    private long fourthLevelNumber;
    private long fiveLevelNumber;

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

    public long getAvgSessionTime() {
        return avgSessionTime;
    }

    public void setAvgSessionTime(long avgSessionTime) {
        this.avgSessionTime = avgSessionTime;
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

    public long getTotalElementNumber() {
        return totalElementNumber;
    }

    public void setTotalElementNumber(long totalElementNumber) {
        this.totalElementNumber = totalElementNumber;
    }

    public long getValidWaitTimeNumber() {
        return validWaitTimeNumber;
    }

    public void setValidWaitTimeNumber(long validWaitTimeNumber) {
        this.validWaitTimeNumber = validWaitTimeNumber;
    }

    public long getValidWaitTimeValue() {
        return validWaitTimeValue;
    }

    public void setValidWaitTimeValue(long validWaitTimeValue) {
        this.validWaitTimeValue = validWaitTimeValue;
    }

    public long getValidSessionTimeNumber() {
        return validSessionTimeNumber;
    }

    public void setValidSessionTimeNumber(long validSessionTimeNumber) {
        this.validSessionTimeNumber = validSessionTimeNumber;
    }

    public long getValidSessionTimeValue() {
        return validSessionTimeValue;
    }

    public void setValidSessionTimeValue(long validSessionTimeValue) {
        this.validSessionTimeValue = validSessionTimeValue;
    }

    public long getFirstLevelNumber() {
        return firstLevelNumber;
    }

    public void setFirstLevelNumber(long firstLevelNumber) {
        this.firstLevelNumber = firstLevelNumber;
    }

    public long getSecondLevelNumber() {
        return secondLevelNumber;
    }

    public void setSecondLevelNumber(long secondLevelNumber) {
        this.secondLevelNumber = secondLevelNumber;
    }

    public long getThirdLevelNumber() {
        return thirdLevelNumber;
    }

    public void setThirdLevelNumber(long thirdLevelNumber) {
        this.thirdLevelNumber = thirdLevelNumber;
    }

    public long getFourthLevelNumber() {
        return fourthLevelNumber;
    }

    public void setFourthLevelNumber(long fourthLevelNumber) {
        this.fourthLevelNumber = fourthLevelNumber;
    }

    public long getFiveLevelNumber() {
        return fiveLevelNumber;
    }

    public void setFiveLevelNumber(long fiveLevelNumber) {
        this.fiveLevelNumber = fiveLevelNumber;
    }
}
