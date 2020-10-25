package org.jdragon.springz.utils;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 18:11
 * @Description: 字符串工具类
 */
public class StringUtils {
    public static String firstLowerCase(String str) {
        return String.valueOf(str.charAt(0)).toLowerCase() + str.substring(1);
    }
}
