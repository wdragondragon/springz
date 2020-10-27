package org.jdragon.springz.core.register;

import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 20:41
 * @Description:
 */
public class ActionRegistrar extends Registrar implements ScanAction {

    private static final Logger logger = LoggerFactory.getLogger(ActionRegistrar.class);

    List<ScanAction> scanActionList;

//    Map<String, BeanInfo> beanInfoMap;

    public ActionRegistrar(List<ScanAction> scanActionList) {
        this.scanActionList = scanActionList;
//        this.beanInfoMap = beanInfoMap;
        logger.info("加载注册器", "ActionRegistrar");
    }

    @Override
    public void action(ClassInfo classInfo) {
        //判断是否为ScanAction的子类实现
        Class<?> clazz = classInfo.getClazz();
        if (ScanAction.class.equals(clazz)) return;
        if (!ScanAction.class.isAssignableFrom(clazz)) return;
        try {
            Constructor constructor = clazz.getConstructor();
            ScanAction scanAction = (ScanAction) constructor.newInstance();
            scanActionList.add(scanAction);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
