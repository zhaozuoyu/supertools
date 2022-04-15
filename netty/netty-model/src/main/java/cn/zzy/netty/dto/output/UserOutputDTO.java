package cn.zzy.netty.dto.output;

import java.io.Serializable;

/**
 * @author zhaozuoyu
 * @date 2021/11/23
 */
public class UserOutputDTO implements Serializable {

    private Integer id;
    private String username;

    public UserOutputDTO() {}

    public UserOutputDTO(Integer id, String username, String password, Integer gender, Integer age) {
        this.id = id;
        this.username = username;
    }

    public Integer getId() {
        return id;
    }

    public UserOutputDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserOutputDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
