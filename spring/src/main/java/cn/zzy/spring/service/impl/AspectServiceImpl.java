package cn.zzy.spring.service.impl;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import cn.zzy.spring.service.AspectService;

/**
 * @author zhaozuoyu
 * @date 2020/11/19
 */
@Aspect
@Component
public class AspectServiceImpl implements AspectService {

    private static final Logger logger = LoggerFactory.getLogger(AspectServiceImpl.class);

    @Override
    @Order(2)
    @Before("execute()")
    public void before() {
        logger.info("**********************execute before method**********************");
    }

    @Order(1)
    @Before("execute()")
    public void before2() {
        logger.info("**********************execute before2 method**********************");
    }

    @Override
    @After("execute()")
    public void after() {
        logger.info("**********************execute after method**********************");
    }

    @After("execute()")
    public void after2() {
        logger.info("**********************execute after2 method**********************");
    }

    @Override
    @Pointcut("execution(* *.execute(..))")
    public void execute() {
        logger.info("**********************execute pointcut method**********************");
    }
}
