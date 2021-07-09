package cn.zzy.dubbo.consumer.service.member;

import com.alibaba.dubbo.common.extension.SPI;

import cn.zzy.dubbo.dto.UserDTO;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
@SPI
public interface MemberService {

    Boolean saveUser(UserDTO userDTO);
}
