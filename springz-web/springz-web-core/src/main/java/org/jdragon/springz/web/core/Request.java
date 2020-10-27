package org.jdragon.springz.web.core;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:43
 * @Description:
 */
public class Request {

    //请求路径
    private String url;

    //请求方法
    private String method;

    //请求协议
    private String protocol;

    //请求属性值
    private final Map<String, String> attributes = new HashMap<>();

    //请求头
    private final Map<String, String> header = new HashMap<>();

    private BufferedReader bufferedReader;

    public static final String CRLF = "\r\n";

    public static final String BANK = " ";

    public Request(InputStream is) {
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            this.bufferedReader = new BufferedReader(inputStreamReader);
            create();
        } catch (IOException e) {
            this.bufferedReader = null;
            System.out.println("Request 创建错误");
        }
    }


    /**
     * 得到请求信息
     *
     * @throws IOException
     */
    private void create() throws IOException {
        String requestInfo = bufferedReader.readLine();

        String[] requestMetaInfo = requestInfo.split(" ");
        method = requestMetaInfo[0];
        url = requestMetaInfo[1];
        protocol = requestMetaInfo[2];

//        if ("/favicon.ico".equals(url)) return;

        while ((requestInfo = bufferedReader.readLine()) != null && !requestInfo.isEmpty()) {
            System.out.println(requestInfo);
            int splitIndex = requestInfo.indexOf(':');
            if (splitIndex == -1) continue;
            String key = requestInfo.substring(0, splitIndex).trim();
            String value = requestInfo.substring(splitIndex + 1).trim();
            header.put(key, value);
        }

        int paramsIndex = url.indexOf('?');
        if (paramsIndex == -1) return;

        String paramsStr = url.substring(paramsIndex + 1);
        url = url.substring(paramsIndex);

        String[] params = paramsStr.split("&");
        for (String param : params) {
            String[] split = param.split("=");
            attributes.put(split[0], split[1]);
        }
    }

    /**
     * 解决中文编码问题
     */
    private String decode(String value, String code) {
        try {
            return java.net.URLDecoder.decode(value, code);
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String getMethod() {
        return this.method;
    }


    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }
}
