package cn.zzy.spring.service.rmi.impl;

import cn.zzy.spring.service.rmi.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author zhaozuoyu
 * @date 2021/10/11
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public HelloServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public String sayHello(String nickname) throws RemoteException {
        logger.info("{} say hello world",nickname);
        String result = "already received from "+nickname+" say!";
        return result;
    }
}
