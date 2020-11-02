package org.jdragon.springz.aop.core.utils;

import java.lang.reflect.Method;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.27 22:47
 * @Description: 用于匹配aop中的切面表达式
 */
public class PatternMatchUtils {
    /**
     * Match a String against the given pattern, supporting the following simple
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
     * arbitrary number of pattern parts), as well as direct equality.
     *
     * @param pattern the pattern to match against
     * @param str     the String to match
     * @return whether the String matches the given pattern
     */
    public static boolean simpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        if (firstIndex == 0) {
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            if ("".equals(part)) {
                return simpleMatch(pattern.substring(nextIndex), str);
            }
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstIndex
                && pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex))
                && simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }

    /**
     * Match a String against the given patterns, supporting the following simple
     * pattern styles: "xxx*", "*xxx", "*xxx*" and "xxx*yyy" matches (with an
     * arbitrary number of pattern parts), as well as direct equality.
     *
     * @param patterns the patterns to match against
     * @param str      the String to match
     * @return whether the String matches any of the given patterns
     */
    public static boolean simpleMatch(String[] patterns, String str) {
        if (patterns != null) {
            for (String pattern : patterns) {
                if (simpleMatch(pattern, str)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        String patterns = "org.jdragon.springz.test.service.UserAddService.*(..)";
        String str = "org.jdragon.springz.test.service.UserAddService.add(org.jdragon.springz.test.domain.User)";

        String patterns1 = "org.jdragon.springz.test.d*.*";
        String name = PatternMatchUtils.class.getName();
        System.out.println(name);
        System.out.println(PatternMatchUtils.simpleMatch(patterns,str));
        System.out.println(PatternMatchUtils.simpleMatch(patterns1,str));

        Method[] methods = PatternMatchUtils.class.getMethods();
        for (Method method : methods) {
            System.out.println(method);
        }
    }
}
