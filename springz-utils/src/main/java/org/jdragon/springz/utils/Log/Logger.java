package org.jdragon.springz.utils.Log;

import org.slf4j.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 19:05
 * @Description:
 */
public class Logger {
    private org.slf4j.Logger logger;

    public org.slf4j.Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public Logger(Class<?> clazz) {
        this.logger = getLogger(clazz);
    }

    public void trace(String tag, String... messages) {
        logger.trace(build(tag, messages));
    }

    public void info(String tag, String... messages) {
        logger.info(build(tag, messages));
    }

    public void debug(String tag, String... messages) {
        logger.debug(build(tag, messages));
    }

    public void warn(String tag, String... messages) {
        logger.warn(build(tag, messages));
    }

    public void error(String tag, String... messages) {
        logger.error(build(tag, messages));
    }

    public static String build(String tag, String... messages) {
        if(tag == null) return "";
        String linkTag = "===>";
        String separator = ":=:";
        StringBuilder template = new StringBuilder(tag);
        if (messages.length > 0) {
            template.append(linkTag).append(messages[0]);
            for (int i = 1; i < messages.length; i++) {
                template.append(separator).append(messages[i]);
            }
        }
        return template.toString();
    }
}
