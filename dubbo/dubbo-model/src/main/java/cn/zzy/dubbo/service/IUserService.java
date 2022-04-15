package cn.zzy.dubbo.service;

import cn.zzy.dubbo.dto.UserDTO;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
public interface IUserService {

    Boolean saveUser(UserDTO userDTO);

    UserDTO queryById(int id);

}
