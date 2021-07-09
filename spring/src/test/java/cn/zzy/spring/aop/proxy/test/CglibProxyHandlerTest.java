package cn.zzy.spring.aop.proxy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.spring.ApplicationRun;
import cn.zzy.spring.aop.proxy.CglibProxyHandler;
import cn.zzy.spring.service.ProxyService;
import cn.zzy.spring.service.impl.ProxyServiceImpl;
import net.sf.cglib.proxy.Enhancer;

/**
 * @author zhaozuoyu
 * @date 2020/11/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class CglibProxyHandlerTest {

    private static final Logger logger = LoggerFactory.getLogger(CglibProxyHandlerTest.class);

    @Test
    public void cglibProxyTest() {
        try {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(ProxyServiceImpl.class);
            // 设置拦截器
            CglibProxyHandler cglibProxyHandler = new CglibProxyHandler();
            enhancer.setCallback(cglibProxyHandler);
            ProxyService proxyService = ProxyServiceImpl.class.cast(enhancer.create());
            proxyService.print();
            logger.info("cglibProxyHandler:{},proxyService:{}", cglibProxyHandler, proxyService);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
