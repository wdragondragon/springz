package org.jdragon.springz.utils.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author JDragon
 * @Date 2021.03.16 上午 11:25
 * @Email 1061917196@qq.com
 * @Des: 用来对json的初始化与寄存，对json链式操作的优化
 */
public class FastJsonMemory {
    private JSON json;

    private FastJsonMemory(){}

    public FastJsonMemory(JSON json) {
        this.json = JSONObject.parseObject(JSONObject.toJSONString(json));
    }

    public FastJsonMemory(String string) {
        this.json = JSON.parseObject(string, JSON.class);
    }

    public FastJsonMemory(Object o) {
        this.json = JSON.parseObject(JSON.toJSONString(o), JSON.class);
    }

    public static FastJsonMemory init(JSON json) {
        FastJsonMemory fastJsonMemory = new FastJsonMemory();
        fastJsonMemory.json = JSONObject.parseObject(JSONObject.toJSONString(json));
        return fastJsonMemory;
    }

    public static FastJsonMemory init(String string) {
        FastJsonMemory fastJsonMemory = new FastJsonMemory();
        fastJsonMemory.json = JSON.parseObject(string, JSON.class);
        return fastJsonMemory;
    }

    public static FastJsonMemory init(Object o) {
        FastJsonMemory fastJsonMemory = new FastJsonMemory();
        fastJsonMemory.json = JSON.parseObject(JSON.toJSONString(o), JSON.class);
        return fastJsonMemory;
    }

    public <T> T get(String path, Class<T> clazz) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), clazz);
    }

    public <T> T get(String path, Type type) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), type);
    }

    public <T> T get(String path, TypeReference<T> type) {
        Object object = get(path);
        return JSON.parseObject(JSON.toJSONString(object), type);
    }

    private Object get(String path) {
        String[] split = path.split("\\.");
        Object object = json;
        Pattern r = Pattern.compile("(.*)\\[(\\d+)]");
        for (String s : split) {
            String key = s;
            Integer index = null;
            Matcher m = r.matcher(s);
            if (m.find()) {
                key = m.group(1);
                index = Integer.parseInt(m.group(2));
            }
            if (object instanceof JSONObject) {
                object = ((JSONObject) object).get(key);
            } else {
                return null;
            }
            if (index != null && object instanceof JSONArray) {
                object = ((JSONArray) object).get(index);
            }
        }
        return object;
    }
}
