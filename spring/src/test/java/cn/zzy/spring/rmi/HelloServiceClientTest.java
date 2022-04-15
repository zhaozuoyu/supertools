package cn.zzy.spring.rmi;

import cn.zzy.spring.service.rmi.HelloService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author zhaozuoyu
 * @date 2021/10/11
 */
public class HelloServiceClientTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Test
    public void sayHelloTest(){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HelloService proxyHelloService = (HelloService) registry.lookup("helloService");
            logger.info("already find remote interface helloService {}",proxyHelloService);
            String result = proxyHelloService.sayHello("Alice");
            logger.info("response:{}",result);
        } catch (RemoteException e) {
            logger.error(e.getMessage(),e);
        } catch (NotBoundException e) {
            logger.error(e.getMessage(),e);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
