package cn.zzy.nio.socket.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 一个简单的线程池处理Socket案例
 *
 * @author zhaozuoyu
 * @date 2020/12/10
 */
@Component
public class ServerSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(ServerSocketListener.class);

    private volatile AtomicInteger totalConnect = new AtomicInteger();

    @Value("${socket.port}")
    private int port;
    /**
     * 设置最大等待队列长度，如果队列已满，则拒绝该连接，default=50
     */
    @Value("${socket.backlog}")
    private int backlog;

    @Autowired
    @Qualifier("socketExecutor")
    private Executor socketExecutor;

    @PostConstruct
    public void init() {
        try {
            initServerSocket();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * socket服务启动，阻塞形式
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    private void initServerSocket() throws IOException, InterruptedException {
        /*
         * bindAddr参数可以在ServerSocket的多宿主机上使用
         * 多宿主机代表一台计算机有两块网卡，每个网卡有不同的ip地址；
         * 或者一台计算机有1块网卡，但这块网卡有多个ip地址的情况
         */
        InetAddress bindAddr = InetAddress.getLocalHost();
        ServerSocket serverSocket = new ServerSocket(this.port, this.backlog);
        // serverSocket.bind(new InetSocketAddress(this.port), this.backlog); 当使用ServerSocket默认构造函数时使用
        // 注意timeout必须在进入阻塞操作前被启用才能生效，value值必须大于0，等于0为无穷大超时值，default=0
        serverSocket.setSoTimeout(0);
        while (true) {
            Socket socket = serverSocket.accept();
            int currentNumber = totalConnect.getAndIncrement();
            logger.info("当前为第{}个连接", currentNumber + 1);
            socketExecutor.execute(new SocketHandler(socket));
        }
    }
}
