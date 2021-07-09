package cn.zzy.spring.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.zzy.spring.service.ProxyService;

/**
 * @author zhaozuoyu
 * @date 2020/11/30
 */
public class ProxyServiceImpl implements ProxyService {

    private static final Logger logger = LoggerFactory.getLogger(ProxyServiceImpl.class);

    @Override
    public void print() {
        logger.info("execute method:test()");
    }
}
