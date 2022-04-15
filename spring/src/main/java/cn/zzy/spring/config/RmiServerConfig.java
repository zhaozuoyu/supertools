package cn.zzy.spring.config;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.zzy.spring.service.rmi.HelloService;
import cn.zzy.spring.service.rmi.impl.HelloServiceImpl;

/**
 * @author zhaozuoyu
 * @date 2021/10/11
 */
@Configuration
public class RmiServerConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     * @return
     * @throws RemoteException
     */
    @Bean
    public HelloService helloService() throws RemoteException {
        HelloService proxyHelloService =null;
        HelloService helloService = new HelloServiceImpl();
        try {
            // 还可以通过 Naming.bind("helloService",helloService); 注册
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("helloService",helloService);
            proxyHelloService = (HelloService) registry.lookup("helloService");
            logger.info("already service helloService registry {}",proxyHelloService);
        } catch (AlreadyBoundException e) {
            logger.error(e.getMessage(),e);
        } catch (NotBoundException e) {
            logger.error(e.getMessage(),e);
        }
        return proxyHelloService;
    }
}
