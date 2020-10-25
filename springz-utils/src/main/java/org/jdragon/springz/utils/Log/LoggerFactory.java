package org.jdragon.springz.utils.Log;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 18:13
 * @Description: Log字符串创建类
 */
public class LoggerFactory {
    public static Logger getLogger(Class<?> clazz) {
        return new Logger(clazz);
    }

    private LoggerFactory() {
    }
}
