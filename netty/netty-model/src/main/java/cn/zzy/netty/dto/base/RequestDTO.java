package cn.zzy.netty.dto.base;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author zhaozuoyu
 * @date 2021/11/29
 */
public class RequestDTO<T> implements Serializable {

    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private Long uniqueId;
    private Map<String, String> header;
    private T body;

    public static Long newId() {
        return INVOKE_ID.getAndIncrement();
    }

    public Long getUniqueId() {
        return uniqueId;
    }

    public RequestDTO setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public RequestDTO setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public T getBody() {
        return body;
    }

    public RequestDTO setBody(T body) {
        this.body = body;
        return this;
    }
}
