package org.jdragon.springz.scanner;


import org.jdragon.springz.scanner.entry.ClassInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.20 22:44
 * @Description:
 */
public interface Filter {
    public boolean isAgree(ClassInfo classInfo);
}
