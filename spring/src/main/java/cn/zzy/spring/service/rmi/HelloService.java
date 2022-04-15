package cn.zzy.spring.service.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author zhaozuoyu
 * @date 2021/10/11
 */
public interface HelloService extends Remote{

    String sayHello(String nickname) throws RemoteException;
}
