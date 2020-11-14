package org.jdragon.springz.test;

import org.jdragon.springz.core.SpringzContext;
import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.controller.UserController;
import org.jdragon.springz.test.domain.User;
import org.jdragon.springz.utils.MethodUtils;
import org.jdragon.springz.web.annotation.RequestMethod;
import org.jdragon.springz.web.core.RouteMethodMapper;
import org.jdragon.springz.web.core.entity.RouteInfo;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.11 10:53
 * @Description:
 */
@Component
public class TestWeb {
    @AutowiredZ
    UserController userController;
    public void test(){
        Map<RequestMethod, Map<String, RouteInfo>> routeMapping = RouteMethodMapper.getRouteMapping();

        RouteInfo routeInfo = routeMapping.get(RequestMethod.GET).get("/user/add");
        System.out.println(routeInfo);
        Method bindMethod = routeInfo.getBindMethod();
//        Object bindBean = SpringzContext.getBean(routeInfo.getBindBeanName());
        Object bindBean = routeInfo.getBindObj();
        MethodUtils.invoke(bindBean,bindMethod, SpringzContext.getBean(User.class));
//        userController.add(SpringzContext.getBean(User.class));
    }
}
