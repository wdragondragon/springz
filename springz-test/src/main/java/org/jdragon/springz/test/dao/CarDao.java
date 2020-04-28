package org.jdragon.springz.test.dao;

import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Repository;
import org.jdragon.springz.core.annotation.Resource;
import org.jdragon.springz.test.domain.Car;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.28 10:25
 * @Description: Resource与Qualifier测试类
 */
@Repository("carOneDao")
public class CarDao {
    @Resource("carOne")
    Car resourceCar;

    @Autowired
    @Qualifier("carOne")
    Car qualifierCar;

    public void resourceCar(){
        System.out.println("CarDao===>resourceCar:"+resourceCar);
    }

    public void qualifierCar(){
        System.out.println("CarDao===>qualifierCar:"+qualifierCar);
    }
}
