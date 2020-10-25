package org.jdragon.springz.test.service;

import com.jdragon.common.response.normal.Result;

import org.jdragon.springz.feign.annotation.ZFeign;
import org.jdragon.springz.test.domain.RobotPostOrder;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.22 18:22
 * @Description:
 */

@ZFeign(baseUrl = "http://localhost:8081", basePath = "/robot")
public interface RobotResult {
    @GetMapping("/getPostOrder")
    Result<List<RobotPostOrder>> getPostOrder();
}
