package org.jdragon.springz.test;


import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.dao.Http;
import org.jdragon.springz.test.domain.RobotPostOrder;
import org.jdragon.springz.test.service.feign.ClGroupMap;
import org.jdragon.springz.test.service.feign.HttpTest;
import org.jdragon.springz.test.service.feign.Robot;
import org.jdragon.springz.test.service.feign.RobotResult;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.http.response.Result;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.29 13:13
 * @Description:
 */

@Component
public class TestFeign {

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @AutowiredZ
    private HttpTest httpTest;

    @AutowiredZ
    private Robot robot;

    @AutowiredZ
    private RobotResult robotResult;

    @AutowiredZ
    private ClGroupMap clGroupMap;

    public void testHttp() {
//        System.out.println(httpTest.http());
//        System.out.println(httpTest.http1(1));
//        System.out.println(httpTest.http2(1));
//        System.out.println(httpTest.http5(1));
//        System.out.println(httpTest.http6(1));
//        System.out.println(httpTest.http7(new Http(1)));
//        System.out.println(httpTest.http8());

        List<RobotPostOrder> postOrder = robot.getPostOrder();
        for (RobotPostOrder robotPostOrder : postOrder) {
            System.out.println(robotPostOrder);
        }
        Result<List<RobotPostOrder>> postOrder1 = robotResult.getPostOrder();
        System.out.println(postOrder1);


        System.out.println(clGroupMap.getGroupMap());
    }
}
