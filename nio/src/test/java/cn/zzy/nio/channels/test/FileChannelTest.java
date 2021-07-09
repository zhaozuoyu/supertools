package cn.zzy.nio.channels.test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaozuoyu
 * @date 2020/12/24
 */
public class FileChannelTest {

    private static final Logger logger = LoggerFactory.getLogger(FileChannelTest.class);

    /**
     * 批量写入字节数据
     */
    @Test
    public void batchWriteTest() {
        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\text.txt";
        logger.info("文件输出路径：{}", filePath);
        try (FileOutputStream outputStream = new FileOutputStream(new File(filePath))) {
            FileChannel fileChannel = outputStream.getChannel();
            ByteBuffer firstByteBuffer = ByteBuffer.wrap("abcde".getBytes());
            ByteBuffer secondByteBuffer = ByteBuffer.wrap("12345".getBytes());
            ByteBuffer thirdByteBuffer = ByteBuffer.wrap("d1e1f1".getBytes());
            secondByteBuffer.position(1);
            secondByteBuffer.limit(3);
            thirdByteBuffer.position(2);
            thirdByteBuffer.limit(4);
            ByteBuffer[] byteBuffers = {firstByteBuffer, secondByteBuffer, thirdByteBuffer};
            // 注意：这里的offset是针对字节缓冲区数组的偏移量
            fileChannel.write(byteBuffers, 2, 1);
            fileChannel.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
