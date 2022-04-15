package cn.zzy.netty.service;

import cn.zzy.netty.dto.base.RequestDTO;
import cn.zzy.netty.dto.base.ResponseDTO;
import cn.zzy.netty.dto.input.UserInputDTO;
import cn.zzy.netty.dto.output.UserOutputDTO;

/**
 * @author zhaozuoyu
 * @date 2021/11/25
 */
public interface IUserService {

    ResponseDTO<UserOutputDTO> get(RequestDTO<UserInputDTO> requestDTO);
}
