package org.jdragon.springz.scanner;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 16:00
 * @Description:
 */
public class ScanManager {
    private final static List<ScanAction> scanActionList = new LinkedList<>();

    public static List<ScanAction> getScanActionList(){
        return scanActionList;
    }

    public static void registerScanAction(ScanAction scanAction){
        scanActionList.add(scanAction);
    }
}
