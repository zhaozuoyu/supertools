package cn.zzy.dubbo.producer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author zhaozuoyu
 * @date 2020/12/7
 */
@Configuration
@ImportResource(locations = {"classpath:dubbo-producer.xml"})
public class DubboConfig {}
