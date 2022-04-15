package cn.zzy.flink.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhaozuoyu
 * @date 2021/12/10
 */
public class AgentServiceInputDTO implements Serializable {

    private String sessiontype;
    /**
     * 坐席首次响应访客消息的次数
     */
    private long agentfirstreplys;
    /**
     * 坐席首次响应访客消息的平均时长
     */
    private long avgfirstreplytime;
    /**
     * 等待时长
     */
    private int waittingtime;
    /**
     * 会话时长
     */
    private long sessiontimes;
    /**
     * 评价分值
     */
    private String satislevel;
    private String status;
    private Date createtime;
    /**
     * 总咨询记录数量
     */
    private int userasks;
    private String orgi;
    /**
     * 留言来源
     */
    private String leaveSource;

    private String id;
    private String agentusername;
    private String agentno;
    private String username;
    private String userid;

    public String getSessiontype() {
        return sessiontype;
    }

    public void setSessiontype(String sessiontype) {
        this.sessiontype = sessiontype;
    }

    public long getAgentfirstreplys() {
        return agentfirstreplys;
    }

    public void setAgentfirstreplys(long agentfirstreplys) {
        this.agentfirstreplys = agentfirstreplys;
    }

    public long getAvgfirstreplytime() {
        return avgfirstreplytime;
    }

    public void setAvgfirstreplytime(long avgfirstreplytime) {
        this.avgfirstreplytime = avgfirstreplytime;
    }

    public int getWaittingtime() {
        return waittingtime;
    }

    public void setWaittingtime(int waittingtime) {
        this.waittingtime = waittingtime;
    }

    public long getSessiontimes() {
        return sessiontimes;
    }

    public void setSessiontimes(long sessiontimes) {
        this.sessiontimes = sessiontimes;
    }

    public String getSatislevel() {
        return satislevel;
    }

    public void setSatislevel(String satislevel) {
        this.satislevel = satislevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public int getUserasks() {
        return userasks;
    }

    public void setUserasks(int userasks) {
        this.userasks = userasks;
    }

    public String getOrgi() {
        return orgi;
    }

    public void setOrgi(String orgi) {
        this.orgi = orgi;
    }

    public String getLeaveSource() {
        return leaveSource;
    }

    public void setLeaveSource(String leaveSource) {
        this.leaveSource = leaveSource;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgentusername() {
        return agentusername;
    }

    public void setAgentusername(String agentusername) {
        this.agentusername = agentusername;
    }

    public String getAgentno() {
        return agentno;
    }

    public void setAgentno(String agentno) {
        this.agentno = agentno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
