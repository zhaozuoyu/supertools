package cn.zzy.netty.dto.input;

import java.io.Serializable;

/**
 * @author zhaozuoyu
 * @date 2021/11/23
 */
public class UserInputDTO implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private Integer gender;
    private Integer age;

    public UserInputDTO() {}

    public UserInputDTO(Integer id, String username, String password, Integer gender, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public UserInputDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserInputDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserInputDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getGender() {
        return gender;
    }

    public UserInputDTO setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserInputDTO setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
