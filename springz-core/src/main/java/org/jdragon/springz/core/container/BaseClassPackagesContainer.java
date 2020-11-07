package org.jdragon.springz.core.container;

import org.jdragon.springz.core.entry.BasePackageInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 16:06
 * @Description: 放着需要扫描的包的基础信息
 */
public class BaseClassPackagesContainer {

    private static final Map<String, BasePackageInfo> basePackageInfoMap = new HashMap<>();

    public static Map<String, BasePackageInfo> getBasePackageInfoMap() {
        return basePackageInfoMap;
    }


    public static void register(String packName, BasePackageInfo basePackageInfo) {
        basePackageInfoMap.put(packName, basePackageInfo);
    }


    public static String[] getBasePackagesName(String baseClassesName) {
        Set<String> basePackageSet = basePackageInfoMap.keySet();
        String[] basePackages = new String[basePackageSet.size()];
        if (basePackageSet.size() == 0) {
            basePackageInfoMap.put(baseClassesName, new BasePackageInfo());
            return new String[]{baseClassesName};
        }
        int i = 0;
        for (String basePackage : basePackageSet) {
            basePackages[i] = basePackage;
            i++;
        }
        return basePackages;
    }
}
