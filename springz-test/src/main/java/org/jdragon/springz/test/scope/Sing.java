package org.jdragon.springz.test.scope;

import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Component;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 10:17
 * @Description:
 */
@Component

public class Sing {
    @Inject
    public Pro pro;

    @Inject
    public Pro2 pro2;

    @Inject
    public Sing2 sing2;
}
