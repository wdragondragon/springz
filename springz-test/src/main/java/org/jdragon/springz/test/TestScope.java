package org.jdragon.springz.test;

import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.scope.Pro;
import org.jdragon.springz.test.scope.Pro2;
import org.jdragon.springz.test.scope.Sing;
import org.jdragon.springz.test.scope.Sing2;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.04 10:19
 * @Description:
 */
@Component
public class TestScope {
    @Inject
    Sing sing;

    @Inject
    Sing2 sing2;

    @Inject
    Pro pro;

    @Inject
    Pro2 pro2;

    @Inject
    Sing sing3;

    public void test() {
        System.out.println("单例测试");
        System.out.println(sing.toString() + sing2.toString());

        System.out.println("原型嵌套单例测试");
        System.out.println(pro.sing.toString() + pro.sing2.toString());
        System.out.println(pro2.sing.toString() + pro2.sing2.toString());
        System.out.println(sing2.sing.toString() + sing.sing2.toString());

        System.out.println("原型测试");
        System.out.println(pro.toString() + pro2.toString());

        System.out.println("单例嵌套原型测试");
        System.out.println(sing.pro.toString() + sing.pro2.toString());
        System.out.println(sing2.pro.toString() + sing2.pro2.toString());
        System.out.println(pro2.pro.toString() + pro.pro2.toString());

        System.out.println("单例中的原型特性测试");
        System.out.println(sing3.pro.toString() + sing3.pro2.toString());
    }
}
