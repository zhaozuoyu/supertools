package cn.zzy.flink.config;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 *
 * @author zhaozuoyu
 * @date 2021/8/12
 */
public class JacksonDateDeserializeConverter extends JsonDeserializer<Date> {

    private static Logger logger = LoggerFactory.getLogger(JacksonDateDeserializeConverter.class);

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {
        String text = jsonParser.getText();
        if (StringUtils.isNotBlank(text)) {
            try {
                Date date = null;
                if (text.contains("-") && text.contains(":")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date = sdf.parse(text);
                } else if (text.contains("-") && !text.contains(":")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    date = sdf.parse(text);
                } else if (text.matches("^\\d+$")) {
                    return new Date(new Long(text));
                } else {
                    logger.info("不支持的日期类型 {}", text);
                }
                return date;
            } catch (ParseException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return null;
    }

    @Override
    public Class<?> handledType() {
        return Date.class;
    }
}
