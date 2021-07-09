package cn.zzy.dubbo.consumer.service.person;

import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author zhaozuoyu
 * @date 2020/12/1
 */
@SPI
public interface Person {

    void say();
}
