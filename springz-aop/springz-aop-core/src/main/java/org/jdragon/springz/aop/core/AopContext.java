package org.jdragon.springz.aop.core;

import lombok.Getter;
import org.jdragon.springz.aop.core.entity.PointCutInfo;
import org.jdragon.springz.aop.core.utils.PatternMatchUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.28 00:11
 * @Description:
 */
public class AopContext {
    @Getter
    private static final List<PointCutInfo> pointCutInfoList = new ArrayList<>();

    public static void addPointCut(PointCutInfo pointCutInfo) {
        pointCutInfoList.add(pointCutInfo);
    }

    public static List<PointCutInfo> get(String className) {
        List<PointCutInfo> pointCutInfos = new ArrayList<>();
        for (PointCutInfo pointCutInfo : pointCutInfoList) {
            String pointCutClass = pointCutInfo.getPointCut().value();
            if (PatternMatchUtils.simpleMatch(pointCutClass, className)) {
                pointCutInfos.add(pointCutInfo);
            }
        }

        return pointCutInfos.stream()
                .sorted(Comparator.comparing(PointCutInfo::getOrder))
                .collect(Collectors.toList());
    }
}
