package org.jdragon.springz.utils;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.26 18:13
 * @Description: Log字符串创建类
 */
public class LogBuilder {
    public static String build(String tag,String...messages){
        String linkTag = "===>";
        String separator = ":=:";
        StringBuilder template = new StringBuilder(tag + linkTag);
        if(messages.length>0){
            template.append(messages[0]);
            for(int i = 1;i<messages.length;i++){
                template.append(separator).append(messages[i]);
            }
        }
        return template.toString();
    }
}
