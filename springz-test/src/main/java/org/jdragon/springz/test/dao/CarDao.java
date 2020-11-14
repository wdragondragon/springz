package org.jdragon.springz.test.dao;


import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Repository;
import org.jdragon.springz.core.annotation.Resource;
import org.jdragon.springz.test.domain.Car;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.28 10:25
 * @Description: Resource与Qualifier测试类
 */
@Repository("carOneDao")
public class CarDao {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource("carOne")
    Car resourceCar;

    @AutowiredZ
    @Qualifier("carOne")
    Car qualifierCar;

    @PostConstruct
    public void resourceCar(){
        int i = 1/0;
        logger.trace("CarDao","resourceCar:"+resourceCar);
    }

    public void qualifierCar(){
        logger.trace("CarDao","qualifierCar:"+qualifierCar);
    }
}
