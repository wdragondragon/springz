package org.jdragon.springz.core;

import org.jdragon.springz.core.entry.BasePackageInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 16:06
 * @Description:
 */
public class BaseClassPackagesManager {
    private static final Map<String, BasePackageInfo> basePackageInfoMap = new HashMap<>();

    public static Map<String, BasePackageInfo> getBasePackageInfoMap(){
        return basePackageInfoMap;
    }

    public static void register(String packName, BasePackageInfo basePackageInfo) {
        basePackageInfoMap.put(packName, basePackageInfo);
    }

    public static String[] getBasePackages(String[] baseClassesName) {
        Set<String> basePackageSet = basePackageInfoMap.keySet();
        String[] basePackages = new String[basePackageSet.size()];
        if (basePackageSet.size() == 0) {
            basePackageInfoMap.put(baseClassesName[0], new BasePackageInfo());
            return baseClassesName;
        }
        int i = 0;
        for (String basePackage : basePackageSet) {
            basePackages[i] = basePackage;
            i++;
        }

        return basePackages;
    }
}
