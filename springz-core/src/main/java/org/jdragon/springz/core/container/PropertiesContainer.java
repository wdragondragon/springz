package org.jdragon.springz.core.container;

import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.05 22:58
 * @Description:
 */
public class PropertiesContainer {

    private final static String DEFAULT_PATH = "classpath:";

    public final static String DEFAULT_SOURCE = DEFAULT_PATH + "application.yml";

    private final static Map<String, Map<String, Object>> propertiesCacheMap = new HashMap<>();

    public static Map<String, Object> getProperties(String source) {
        if (!propertiesCacheMap.containsKey(source)) {
            PropertiesContainer.load(source);
        }
        return propertiesCacheMap.get(source);
    }

    public static Object getPropertyValue(String key) {
        return getPropertyValue(key, DEFAULT_SOURCE);
    }

    public static Object getPropertyValue(String prefix, String key, String source) {
        if (source.isEmpty()) {
            source = DEFAULT_SOURCE;
        }
        if (prefix.isEmpty()) {
            return getPropertyValue(key, source);
        } else {
            return getPropertyValue(prefix + "." + key, source);
        }
    }

    public static Object getPropertyValue(String key, String source) {
        String[] split = key.split("\\.");
        Map<?, ?> resultJson = getProperties(source);
        for (String s : split) {
            Object temp = resultJson.get(s);
            if (!(temp instanceof Map)) {
                return temp;
            }
            resultJson = (Map<?, ?>) temp;
        }
        return resultJson;
    }

    private static void load(String source) {
        Yaml yaml = new Yaml();
        InputStream inputStream;
        if (source.contains(DEFAULT_PATH)) {
            inputStream = PropertiesContainer.class
                    .getClassLoader()
                    .getResourceAsStream(source.replaceAll(DEFAULT_PATH, ""));
        } else {
            try {
                inputStream = new FileInputStream(source);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }
        Map<String, Object> loadJson = yaml.load(inputStream);

        propertiesCacheMap.put(source, loadJson);
    }
}
