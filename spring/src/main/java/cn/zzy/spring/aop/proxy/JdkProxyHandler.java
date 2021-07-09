package cn.zzy.spring.aop.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaozuoyu
 * @date 2020/11/19
 */
public class JdkProxyHandler implements InvocationHandler, Serializable {

    private static Logger logger = LoggerFactory.getLogger(JdkProxyHandler.class);

    private Object target;

    public JdkProxyHandler(Object target) {
        super();
        this.target = target;
    }

    public void print() {
        logger.info("print");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("before");
        Object result = method.invoke(proxy, args);
        logger.info("after");
        return result;
    }

    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
            this.target.getClass().getInterfaces(), this);
    }

}
