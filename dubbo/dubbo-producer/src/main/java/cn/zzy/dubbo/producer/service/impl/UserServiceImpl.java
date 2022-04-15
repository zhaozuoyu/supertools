package cn.zzy.dubbo.producer.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zzy.dubbo.dto.UserDTO;
import cn.zzy.dubbo.service.IUserService;

/**
 * 注意，这里的@Service是阿里dubbo的注解
 * 
 * @author zhaozuoyu
 * @date 2020/12/1
 */

public class UserServiceImpl implements IUserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public Boolean saveUser(UserDTO userDTO) {
        logger.info("userDTO:{}", JSON.toJSONString(userDTO));
        return true;
    }

    @Override
    public UserDTO queryById(int id) {
        UserDTO userDTO =
            new UserDTO().setId(id).setUsername("elise").setAge(22).setGender(1).setPassword("elise.password");
        return userDTO;
    }
}
