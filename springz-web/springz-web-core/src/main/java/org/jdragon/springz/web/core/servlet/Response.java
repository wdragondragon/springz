package org.jdragon.springz.web.core.servlet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.26 12:43
 * @Description:
 */
public class Response {

    public static final String CRLF = "\r\n";
    public static final String BANK = " ";

    private String contentType = "application/json;charset=utf8";

    private StringBuilder headerInfo;

    private StringBuilder content;
    private BufferedWriter wr;
    private int len;

    public Response() {
        // TODO Auto-generated constructor stub
        headerInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    public Response(OutputStream os) {
        this();
        wr = new BufferedWriter(new OutputStreamWriter(os));
    }

    public void createHeaderInfo(int code) {
        System.out.println("code is " + code);
        headerInfo.append("HTTP/1.1").append(BANK);
        switch (code) {
            case 200:
                headerInfo.append(code).append(BANK).append("OK");
                break;
            case 404:
                headerInfo.append(code).append(BANK).append("404 not found");
                break;
            case 500:
                headerInfo.append(code).append(BANK).append("error");
                break;
            default:
                headerInfo.append(code).append(BANK).append("error");
                break;
        }
        headerInfo.append(CRLF);
        headerInfo.append("Server:jdragon server/0.1").append(CRLF);
        headerInfo.append("Date:").append(new Date()).append(CRLF);
        headerInfo.append("Content-type:").append(contentType).append(CRLF);
        //正文长度，字节长度
        headerInfo.append("Content-Length:").append(len).append(CRLF);
        //空行分隔符
        headerInfo.append(CRLF);
    }

    public Response println(String msg) {
        //System.out.println(msg);
        if (content == null)
            System.out.println(msg);
        content.append(msg);
        len += msg.getBytes().length;
        return this;
    }

    public void pushToClient(int code) throws IOException {
        if (wr == null) {
            code = 500;
        }
        createHeaderInfo(code);
        wr.write(headerInfo.toString());
        wr.write(content.toString());
        wr.flush();
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
