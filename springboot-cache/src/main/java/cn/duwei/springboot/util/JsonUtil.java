package cn.duwei.springboot.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper;


    static {
        JsonFactory jf = new JsonFactory();
        jf.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jf.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        jf.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        mapper = new ObjectMapper(jf);
    }

    /**
     * 将对象转化为json串
     * @param value
     * @return
     */
    public static String toJson(Object value) {
        if (null == value) {
            return null;
        }
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            log.error("Json write bean to string exception, ", e);
        }
        return null;
    }

    /**
     * 将json串转化为对象
     * @param json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, TypeReference<T> obj) {
        if (null == json || "".equals(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, obj);
        } catch (Exception e) {
            log.error("Json string write to obj exception, ", e);
        }
        return null;
    }

    /**
     * 将json串转化为对象
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        if (null == json || "".equals(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("Json string write to bean exception, ", e);
        }
        return null;
    }


}
