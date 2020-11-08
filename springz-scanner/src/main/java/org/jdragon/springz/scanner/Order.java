package org.jdragon.springz.scanner;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 12:51
 * @Description:
 */
public interface Order {
    //返回行为的优先级，默认为0
    default Integer getOrder(){
        return 0;
    }
}
