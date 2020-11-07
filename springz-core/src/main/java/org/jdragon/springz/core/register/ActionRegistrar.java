package org.jdragon.springz.core.register;

import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.core.container.ScanActionContainer;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.21 20:41
 * @Description: 扫描拓展功能的注册器的扫描器
 */
public class ActionRegistrar implements ScanAction {

    private static final Logger logger = LoggerFactory.getLogger(ActionRegistrar.class);

    public ActionRegistrar() {
        logger.info("加载注册器", "ActionRegistrar");
    }

    @Override
    public void action(ClassInfo classInfo) {
        //判断是否为ScanAction的子类实现
        Class<?> clazz = classInfo.getClazz();
        if (ScanAction.class.equals(clazz)||
                !ScanAction.class.isAssignableFrom(clazz)) return;
        try {
            ScanAction scanAction = (ScanAction) clazz.newInstance();
            ScanActionContainer.registerScanAction(scanAction);
        } catch (IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Filter[] getFilters() {
        return new Filter[0];
    }
}
