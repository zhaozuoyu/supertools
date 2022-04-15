package cn.zzy.spring.aop.aspect.test;

import cn.zzy.spring.aspect.PointcutComponent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.spring.ApplicationRun;

/**
 * @author zhaozuoyu
 * @date 2020/11/30
 */
@RunWith(SpringRunner.class)
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@SpringBootTest(classes = ApplicationRun.class)
public class AspectTest {

    private static final Logger logger = LoggerFactory.getLogger(AspectTest.class);

    @Autowired
    private PointcutComponent pointcutComponent;

    @Test
    public void aspectTest() {
        try {
            pointcutComponent.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
