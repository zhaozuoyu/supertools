package cn.zzy.netty.client.future;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import cn.zzy.netty.dto.base.ResponseDTO;

/**
 * @author zhaozuoyu
 * @date 2021/11/29
 */
public class NettyClientFuture {

    private static final Logger logger = LoggerFactory.getLogger(NettyClientFuture.class);

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    private long timeout = 3000;
    /**
     * 存储rpc返回结果
     */
    private static final Map<Long, NettyClientFuture> FUTURES = new ConcurrentHashMap<>();
    private long uniqueId;
    private ResponseDTO<?> responseDTO;

    public NettyClientFuture(long uniqueId) {
        this.uniqueId = uniqueId;
        FUTURES.put(uniqueId, this);
    }

    public Object get() {
        return this.get(this.timeout, TimeUnit.MILLISECONDS);
    }

    public Object get(long timeout, TimeUnit timeUnit) {
        if (responseDTO != null) {
            return responseDTO;
        } else {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (responseDTO == null) {
                    condition.await(timeout, timeUnit);
                    // 当返回结果或响应超时自动退出
                    if (responseDTO != null || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            } finally {
                lock.unlock();
            }
        }
        return responseDTO;
    }

    /**
     * 供netty-client 消息入站处理完后使用
     * 
     * @param response
     */
    public static void receive(ResponseDTO<?> response) {
        NettyClientFuture nettyClientFuture = FUTURES.remove(response.getUniqueId());
        if (nettyClientFuture != null) {
            nettyClientFuture.doReceive(response);
        } else {
            logger.info("rpc response timeout! uniqueId:{},body:{}", response.getUniqueId(),
                JSON.toJSON(response.getBody()));
        }
    }

    private void doReceive(ResponseDTO<?> response) {
        this.responseDTO = response;
        lock.lock();
        try {
            // 通知等待线程
            condition.signal();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

}
