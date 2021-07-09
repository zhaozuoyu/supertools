package cn.zzy.nio.socket.client.test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.zzy.nio.ApplicationRun;

/**
 * @author zhaozuoyu
 * @date 2020/12/10
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApplicationRun.class)
public class SocketClientTest {

    private static final Logger logger = LoggerFactory.getLogger(SocketClientTest.class);

    @Value("${socket.host}")
    private String host;
    @Value("${socket.port}")
    private int port;

    @Test
    public void sayHelloTest() throws IOException {
        String[] names = {"Alice", "Linda", "James", "Jack", "Elise"};
        for (int i = 0; i < names.length; i++) {
            Socket socket = null;
            try {
                socket = new Socket(this.host, this.port);
                OutputStream outputStream = socket.getOutputStream();
                byte[] msg = ("Hello Everyone! I'm " + names[i]).getBytes();
                outputStream.write(msg);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        }
    }

}
