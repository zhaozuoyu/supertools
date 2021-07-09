package cn.zzy.nio.socket.server;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * socket处理类
 * 
 * @author zhaozuoyu
 * @date 2020/12/10
 */
public class SocketHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private Socket socket;

    public SocketHandler(Socket socket) {
        if (socket == null) {
            throw new RuntimeException("socket not be null!");
        }
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            try (InputStream inputStream = socket.getInputStream()) {
                try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                    char[] array = new char[1000];
                    int readLength;
                    while ((readLength = inputStreamReader.read(array)) != -1) {
                        String string = new String(array, 0, readLength);
                        logger.info("receive message:{}", string);
                    }
                } catch (Exception e) {
                    throw e;
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (socket != null) {
                    socket.close();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
