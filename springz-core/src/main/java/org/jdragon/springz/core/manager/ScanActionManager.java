package org.jdragon.springz.core.manager;

import org.jdragon.springz.core.container.ScanActionContainer;
import org.jdragon.springz.core.filter.BaseFilter;
import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.core.register.ActionRegistrar;
import org.jdragon.springz.core.register.MethodComponentRegistrar;
import org.jdragon.springz.core.register.PostProcessorRegistrar;
import org.jdragon.springz.core.register.TypeComponentRegistrar;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.Scanner;

import java.util.Comparator;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.08 00:13
 * @Description:
 */
public class ScanActionManager {

    private static Scanner scanner;

    public static Scanner newScan(String... scanBasePackages) {
        return new Scanner(scanBasePackages);
    }

    public static void initScan(String... scanBasePackages) {
        scanner = newScan(scanBasePackages);
    }

    public static void registerScanAction() {
        Filter baseFilter = new BaseFilter();

        ScanActionContainer.registerScanAction(
                new TypeComponentRegistrar(baseFilter),//@Component扫描注册 -99
                new MethodComponentRegistrar(baseFilter),//@Bean扫描注册 -98
                new PostProcessorRegistrar(),//加载bean初始化完成的后置处理器 -97
                new Infuser(baseFilter));//注入 100

        scanner.action(new ActionRegistrar()).doScan(); //拓展注册器
        //根据order优先级来排序注册先后
        ScanActionContainer.getScanActionList().sort(Comparator.comparing(ScanAction::getOrder));
    }

    public static void doScan() {
        ScanActionContainer.getScanActionList().forEach(scanAction -> scanner.action(scanAction).doScan());
    }
}
