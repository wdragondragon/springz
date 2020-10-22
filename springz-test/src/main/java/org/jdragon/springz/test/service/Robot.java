package org.jdragon.springz.test.service;

import com.jdragon.common.response.normal.Result;
import org.jdragon.springz.annotation.feign.GetMapping;
import org.jdragon.springz.annotation.feign.ZFeign;
import org.jdragon.springz.test.domain.RobotPostOrder;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 18:22
 * @Description:
 */

@ZFeign(baseUrl = "http://localhost:8081", basePath = "/robot",depth = "result")
public interface Robot {
    @GetMapping("/getPostOrder")
    List<RobotPostOrder> getPostOrder();
}
