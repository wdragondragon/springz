package org.jdragon.springz.utils.http.response;



/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.03.18 17:47
 * @Description: 返回结果枚举
 */
public enum ResultCode implements ICode{

    NORMAL(20000L, "正常响应"),
    AUTH_FAIL(20001L, "认证失败"),
    PERMISSIONS_NOT_ENOUGH(20002L, "权限不足"),
    SYSTEM_ERROR(20003L, "预期异常"),
    PARAMS_ERROR(20004L,"参数非法"),
    SYSTEM_UN_KNOW_ERROR(20005L,"超预期异常");

    private Long code;

    private String message;

    ResultCode(Long code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
