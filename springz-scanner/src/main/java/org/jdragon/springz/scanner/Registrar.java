package org.jdragon.springz.scanner;


import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 22:26
 * @Description:
 */
public abstract class Registrar {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Map<String, BeanInfo> beanMap;

    protected ClassInfo classInfo;

    protected Registrar(Map<String, BeanInfo> beanMap) {
        this.beanMap = beanMap;
    }

    /**
     * @params: [definitionName, obj]
     * @return: void
     * @Description: 通用注册类，将definitionName作为key,obj作为value存到beanMap中
     **/
    public void register(String definitionName, Object obj, String scope) {
        //检查definitionName是否存在
        if (beanMap.containsKey(definitionName)) {
            Object existObj = beanMap.get(definitionName);
            logger.warn("已存在键名", definitionName, existObj.getClass().getName());
            logger.warn("请解决类键名冲突", definitionName, classInfo.getClassName());
            return;
        }
        //将对象放到map容器
        beanMap.put(definitionName, new BeanInfo(obj, scope));
        if (beanMap.containsKey(definitionName)) {
            logger.info("注册bean成功", definitionName, classInfo.getClassName());
        }
    }
}
