package cn.zzy.netty.dto.base;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zhaozuoyu
 * @date 2021/11/29
 */
public class ResponseDTO<T> implements Serializable {

    private Long uniqueId;
    private Map<String, String> header;
    private T body;

    public Long getUniqueId() {
        return uniqueId;
    }

    public ResponseDTO setUniqueId(Long uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public ResponseDTO setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public T getBody() {
        return body;
    }

    public ResponseDTO setBody(T body) {
        this.body = body;
        return this;
    }
}
