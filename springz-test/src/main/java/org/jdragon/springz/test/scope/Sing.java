package org.jdragon.springz.test.scope;

import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Scope;
import org.jdragon.springz.scanner.entry.BeanInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 10:17
 * @Description:
 */
@Component

public class Sing {
    @AutowiredZ
    public Pro pro;

    @AutowiredZ
    public Pro2 pro2;

    @AutowiredZ
    public Sing2 sing2;
}
