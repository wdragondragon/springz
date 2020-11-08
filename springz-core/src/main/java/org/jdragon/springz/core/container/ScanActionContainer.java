package org.jdragon.springz.core.container;

import org.jdragon.springz.core.filter.BaseFilter;
import org.jdragon.springz.core.infuse.Infuser;
import org.jdragon.springz.core.register.ActionRegistrar;
import org.jdragon.springz.core.register.MethodComponentRegistrar;
import org.jdragon.springz.core.register.PostProcessorRegistrar;
import org.jdragon.springz.core.register.TypeComponentRegistrar;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.Scanner;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 16:00
 * @Description:
 */
public class ScanActionContainer {

    private final static List<ScanAction> scanActionList = new LinkedList<>();

    public static List<ScanAction> getScanActionList(){
        return scanActionList;
    }

    public static void registerScanAction(ScanAction...scanAction){
        scanActionList.addAll(Arrays.asList(scanAction));
    }
}
