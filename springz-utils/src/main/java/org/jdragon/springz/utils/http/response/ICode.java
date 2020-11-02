package org.jdragon.springz.utils.http.response;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.06.23 22:26
 * @Description:
 *  对ResultCode返回前端字符串的抽象接口
 *  返回前端的字符需要实现该接口的枚举类来代替魔术变量
 */
public interface ICode {

    Long getCode();

    String getMessage();

}

