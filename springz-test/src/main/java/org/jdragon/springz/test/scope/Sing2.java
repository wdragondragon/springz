package org.jdragon.springz.test.scope;

import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 10:18
 * @Description:
 */
@Component
public class Sing2 {
    @AutowiredZ
    public Pro pro;

    @AutowiredZ
    public Pro2 pro2;

    @AutowiredZ
    public Sing sing;
}
