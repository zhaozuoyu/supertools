package cn.zzy.netty.client.controller;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.client.boot.NettyClient;
import cn.zzy.netty.dto.base.RequestDTO;
import cn.zzy.netty.dto.base.ResponseDTO;
import cn.zzy.netty.dto.input.UserInputDTO;
import cn.zzy.netty.dto.output.UserOutputDTO;
import cn.zzy.netty.service.IUserService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;

/**
 * @author zhaozuoyu
 * @date 2021/11/9
 */
@RestController
@RequestMapping("/netty/client")
public class NettyClientController {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientController.class);

    @Autowired
    private NettyClient nettyClient;
    @Autowired
    private IUserService userService;

    @GetMapping("/users/{id}")
    public ResponseEntity<ResponseDTO<UserOutputDTO>> get(HttpServletRequest request, @PathVariable Integer id) {
        logger.info("query user information id:{}", id);
        ResponseEntity<ResponseDTO<UserOutputDTO>> responseEntity;
        UserInputDTO userInputDTO = new UserInputDTO().setId(id);
        RequestDTO<UserInputDTO> requestDTO = new RequestDTO<>().setUniqueId(RequestDTO.newId()).setBody(userInputDTO);
        try {
            byte[] bytes = SerializationUtils.serialize(userInputDTO);
            ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
            this.nettyClient.getChannel().writeAndFlush(userInputDTO);
            /*ResponseDTO<UserOutputDTO> responseDTO = userService.get(requestDTO);*/
            ResponseDTO<UserOutputDTO> responseDTO = null;
            responseEntity = new ResponseEntity<>(responseDTO, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("query user information result:{}", JSON.toJSONString(responseEntity));
        return responseEntity;
    }

    @GetMapping("/write")
    public ResponseEntity<String> write(@Valid String msg) {
        logger.info("msg:{}", msg);
        try {
            // 复合缓存区、堆缓冲区、直接缓冲区
            CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
            ByteBuf heapBuf = Unpooled.buffer(8);
            ByteBuf directBuf = Unpooled.directBuffer(16);
            // 添加ByteBuf到CompositeByteBuf
            compositeByteBuf.addComponents(heapBuf, directBuf);
            // 删除第一个ByteBuf
            compositeByteBuf.removeComponent(0);
            Iterator<ByteBuf> iterator = compositeByteBuf.iterator();
            while (iterator.hasNext()) {
                ByteBuf byteBuf = iterator.next();
                // 使用数组访问数据
                if (byteBuf.hasArray()) {
                    int length = byteBuf.readableBytes();
                    byte[] bytes = new byte[length];
                    byteBuf.getBytes(0, bytes);
                }
            }
            ByteBuf byteBuf = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
            ChannelFuture channelFuture = this.nettyClient.getChannel().writeAndFlush(byteBuf);
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        logger.info("write successful");
                    } else {
                        logger.info("write fail");
                        logger.error(future.cause().getMessage(), future.cause());
                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity<String>(msg, HttpStatus.OK);
    }

}
