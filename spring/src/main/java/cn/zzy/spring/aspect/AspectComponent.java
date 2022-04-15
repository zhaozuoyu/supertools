package cn.zzy.spring.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author zhaozuoyu
 * @date 2021/9/18
 */
@Aspect
@Component
public class AspectComponent {

    private static final Logger logger = LoggerFactory.getLogger(AspectComponent.class);


    @Before("cn.zzy.spring.aspect.PointcutComponent.execute()")
    public void before() {
        logger.info("**********************execute before method**********************");
    }

    @Before("cn.zzy.spring.aspect.PointcutComponent.execute()")
    public void before2() {
        logger.info("**********************execute before2 method**********************");
    }


    @After("cn.zzy.spring.aspect.PointcutComponent.execute()")
    public void after() {
        logger.info("**********************execute after method**********************");
    }

    @After("cn.zzy.spring.aspect.PointcutComponent.execute()")
    public void after2() {
        logger.info("**********************execute after2 method**********************");
    }
}
