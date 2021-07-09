package cn.zzy.dubbo.consumer.service.member.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.zzy.dubbo.consumer.service.member.MemberService;
import cn.zzy.dubbo.dto.UserDTO;
import cn.zzy.dubbo.service.IUserService;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private IUserService userService;

    @Override
    public Boolean saveUser(UserDTO userDTO) {
        try {
            Boolean result = userService.saveUser(userDTO);
            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
