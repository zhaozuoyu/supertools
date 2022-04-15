package cn.zzy.spring.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaozuoyu
 * @date 2021/10/12
 */
public class JdkInvocationHandler implements InvocationHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("before");
        Object result = method.invoke(proxy, args);
        logger.info("after");
        return result;
    }
}
