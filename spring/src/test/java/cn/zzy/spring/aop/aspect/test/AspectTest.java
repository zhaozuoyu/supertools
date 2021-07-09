package cn.zzy.spring.aop.aspect.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.spring.ApplicationRun;
import cn.zzy.spring.service.AspectService;

/**
 * @author zhaozuoyu
 * @date 2020/11/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class AspectTest {

    private static final Logger logger = LoggerFactory.getLogger(AspectTest.class);

    @Autowired
    private AspectService aspectService;

    @Test
    public void aspectTest() {
        try {
            aspectService.execute();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
