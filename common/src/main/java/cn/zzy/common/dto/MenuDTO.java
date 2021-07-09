package cn.zzy.common.dto;

import cn.zzy.common.annotation.Menu;

/**
 * @author zhaozuoyu
 * @date 2020/11/27
 */
public class MenuDTO {

    @Menu(type = "tv")
    private String tv;
    @Menu(id = 1, type = "airConditioner")
    private String airConditioner;

    public MenuDTO() {}

    public MenuDTO(String tv, String airConditioner) {
        this.tv = tv;
        this.airConditioner = airConditioner;
    }

    public String getTv() {
        return tv;
    }

    public void setTv(String tv) {
        this.tv = tv;
    }

    public String getAirConditioner() {
        return airConditioner;
    }

    public void setAirConditioner(String airConditioner) {
        this.airConditioner = airConditioner;
    }
}
