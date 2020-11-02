package org.jdragon.springz.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.ValueFilter;

import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.07.28 22:45
 * @Description: fastjson工具类
 */
public class JsonUtils {

    public static <T> T str2Object(String json,Class<T> t){
        return JSON.parseObject(json, t);
    }

    public static <T> T json2Object(JSON json,Class<T> t){
        return JSON.toJavaObject(json,t);
    }

    public static JSON object2Json(Object t){
        return (JSON)JSON.toJSON(t);
    }
    public static String object2Str(Object t){
        JSON json = (JSON)JSON.toJSON(t);
        return json.toJSONString();
    }

    public static String object2StrIncludeNull(Object t){
        ValueFilter valueFilter = (o, s, o1) -> o1 == null ? "" : o1;
        return JSON.toJSONString(t,valueFilter);
    }

    public static Map<String,Object> str2Map(String json){
        return JSON.parseObject(json,new TypeReference<Map<String,Object>>(){});
    }

    public static Map<String,Object> json2Map(JSON json){
        return JSON.parseObject(json.toJSONString(),new TypeReference<Map<String,Object>>(){});
    }

    public static Map<String,Object> object2Map(Object o){
        return json2Map(object2Json(o));
    }
}
