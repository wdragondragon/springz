package org.jdragon.springz.utils.http;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.01 10:32
 * @Description:
 */
public class HttpException extends RuntimeException {
    private String message;

    public HttpException() {
    }

    public HttpException(String message) {
        this.message = message;
    }

    public HttpException(Throwable e) {
        super(e);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
