package cn.zzy.spring.aop.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author zhaozuoyu
 * @date 2020/11/19
 */
public class CglibProxyHandler implements MethodInterceptor, Serializable {

    private static Logger logger = LoggerFactory.getLogger(CglibProxyHandler.class);

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        logger.info("before");
        Object result = methodProxy.invokeSuper(object, args);
        logger.info("after");
        return result;
    }

}
