package org.jdragon.springz.feign.core;

import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.core.register.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.feign.annotation.ZFeign;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 22:21
 * @Description:
 */
public class FeignRegistrar extends Registrar implements ScanAction {

    private static final Logger logger = LoggerFactory.getLogger(FeignRegistrar.class);

    public FeignRegistrar() {
        logger.info("加载注册器", "FeignRegistrar");
    }

    @Override
    public void action(ClassInfo classInfo) {
        Class<?> clazz = classInfo.getClazz();
        if (!clazz.isAnnotationPresent(ZFeign.class)) {
            return;
        }

        Object o = DynaProxyHttp.getInstance(clazz);

        register(classInfo, o, BeanInfo.SINGLETON);
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
