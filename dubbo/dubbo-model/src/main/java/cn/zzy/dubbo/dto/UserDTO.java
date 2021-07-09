package cn.zzy.dubbo.dto;

import java.io.Serializable;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
public class UserDTO implements Serializable {

    private Integer id;
    private String username;
    private String password;
    private Integer gender;
    private Integer age;

    public UserDTO() {}

    public UserDTO(Integer id, String username, String password, Integer gender, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public UserDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public Integer getGender() {
        return gender;
    }

    public UserDTO setGender(Integer gender) {
        this.gender = gender;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public UserDTO setAge(Integer age) {
        this.age = age;
        return this;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
