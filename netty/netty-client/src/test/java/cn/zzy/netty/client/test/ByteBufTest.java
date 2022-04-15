package cn.zzy.netty.client.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author zhaozuoyu
 * @date 2021/11/22
 */
public class ByteBufTest {

    private final Logger logger = LoggerFactory.getLogger(ByteBufTest.class);

    /**
     * 随机访问索引，通过此种方式访问时不会推进读索引和写索引，可以通过ByteBuf的readerIndex()或writerIndex()来分别推进读索引或写索引。
     */
    @Test
    public void readAndWriteTest() {
        ByteBuf buf = Unpooled.buffer(16);
        for (int i = 0; i < 16; i++) {
            buf.writeByte(i + 1);
        }
        for (int i = 0; i < buf.capacity(); i++) {
            logger.info("{}", buf.getByte(i));
        }
    }

    /**
     * 回收已经读取过的字节，discardReadBytes()将丢弃从索引0到readerIndex之间的字节。注意：此方法会引起内存复制，因为它需要移动ByteBuf中可读的字节到开始位置，这样的操作会影响性能，一般在需要马上
     * 释放内存的时候使用收益会比较大。
     */
    @Test
    public void discardReadBytesTest() {
        ByteBuf buf = Unpooled.buffer(16);
        for (int i = 0; i < 16; i++) {
            buf.writeByte(i + 1);
        }
        logger.info("current capacity is {}", buf.capacity());
        for (int i = 0; i < buf.capacity(); i++) {
            logger.info("{}", buf.readerIndex());
            if (i % 4 == 0) {
                buf.discardReadBytes();
                logger.info("current capacity is {}", buf.capacity());
            }
        }
    }
}
