package org.jdragon.springz.core.container;

import org.jdragon.springz.scanner.ScanAction;

import java.util.Arrays;
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
