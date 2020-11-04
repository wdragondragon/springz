package org.jdragon.springz.scanner;


import org.jdragon.springz.scanner.entry.ClassInfo;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 15:42
 * @Description: 策略模式进行的扫描器不同行为的复用接口
 */
public interface ScanAction {
    /**
     * 在扫描器的扫描之后进行的行为
     *
     * @params: [beanInfo]
     */
    void action(ClassInfo classInfo);

    Filter[] getFilters();

    //返回扫描行为的优先级，默认为0
    default Integer getOrder(){
        return 0;
    }
}
