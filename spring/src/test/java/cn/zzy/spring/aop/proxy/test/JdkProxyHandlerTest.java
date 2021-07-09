package cn.zzy.spring.aop.proxy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.spring.ApplicationRun;
import cn.zzy.spring.aop.proxy.JdkProxyHandler;
import cn.zzy.spring.service.ProxyService;
import cn.zzy.spring.service.impl.ProxyServiceImpl;

/**
 * @author zhaozuoyu
 * @date 2020/11/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class JdkProxyHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(JdkProxyHandlerTest.class);

    /**
     * 此测试方法引起死循环
     */
    @Test
    @Deprecated
    public void jdkProxyTest() {
        try {
            ProxyService proxyService = new ProxyServiceImpl();
            JdkProxyHandler jdkProxyHandler = new JdkProxyHandler(proxyService);
            ProxyService proxy = (ProxyService)jdkProxyHandler.getProxy();
            proxy.print();
            logger.info("jdkProxyHandler:{},proxyService:{}", jdkProxyHandler, proxyService);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
