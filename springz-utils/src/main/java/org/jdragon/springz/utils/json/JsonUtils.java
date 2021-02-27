package org.jdragon.springz.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import org.jdragon.springz.utils.StrUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.28 22:45
 * @Description: fastjson工具类
 */
public class JsonUtils {

    public static <T> T str2Object(String json, Class<T> t) {
        return JSON.parseObject(JSON.toJSONString(json), t);
    }

    public static <T> T json2Object(JSON json, Class<T> t) {
        return JSON.toJavaObject(json, t);
    }

    public static <T> T json2Object(String json, Class<T> t) {
        return JSON.parseObject(json, t);
    }

    public static JSON object2Json(Object t) {
        return (JSON) JSON.toJSON(t);
    }

    public static <T> T object2Object(Object object,Class<T> t){
        return JSON.parseObject(JSONObject.toJSONString(object),t);
    }

    public static <T> List<T> listMap2Obj(List<Map<?,?>> listMap, Class<T> t){
        List<T> list = new ArrayList<>(8);
        for (Map<?, ?> map : listMap) {
            T t1 = object2Object(map, t);
            list.add(t1);
        }
        return list;
    }

    public static String object2Str(Object t) {
        JSON json = (JSON) JSON.toJSON(t);
        return json.toJSONString();
    }

    public static String object2StrIncludeNull(Object t) {
        ValueFilter valueFilter = (o, s, o1) -> o1 == null ? "" : o1;
        return JSON.toJSONString(t, valueFilter);
    }

    public static Map<String, Object> str2Map(String json) {
        return JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, Object> json2Map(JSON json) {
        return JSON.parseObject(json.toJSONString(), new TypeReference<Map<String, Object>>() {
        });
    }

    public static Map<String, Object> object2Map(Object o) {
        return json2Map(object2Json(o));
    }

    public static byte[] object2Byte(Object o) {
        return JSON.toJSONBytes(o);
    }

    public static byte[] object2ByteIncludeNull(Object o) {
        return JSON.toJSONBytes(o, SerializerFeature.WriteMapNullValue);
    }


    /**
     * @author: Jdragon
     * @Description: 使用json方式克隆
     **/
    public static <T> T jsonCopy(T source, Field... ignoreField) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
        filter.getExcludes().addAll(Arrays.stream(ignoreField).map(Field::getName).collect(Collectors.toSet()));
        return JSON.parseObject(JSON.toJSONString(source, filter), (Type) source.getClass());
    }


    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     * @since 3.3.0
     */
    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号判定为JSONObject字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     * @since 3.3.0
     */
    public static boolean isJsonObj(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return StrUtil.isWrap(str.trim(), '{', '}');
    }

    /**
     * 是否为JSONArray字符串，首尾都为中括号判定为JSONArray字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     * @since 3.3.0
     */
    public static boolean isJsonArray(String str) {
        if (StrUtil.isBlank(str)) {
            return false;
        }
        return StrUtil.isWrap(str.trim(), '[', ']');
    }
}
