package cn.zzy.dubbo.consumer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author zhaozuoyu
 * @date 2020/12/7
 */
@Configuration
@ImportResource(locations = {"classpath:dubbo-consumer.xml"})
public class DubboConfig {}
