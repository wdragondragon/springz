package org.jdragon.springz.test.service.feign;

import org.jdragon.springz.feign.annotation.ZFeign;
import org.jdragon.springz.web.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.10.31 14:02
 * @Description:
 */

@ZFeign(baseUrl = "https://cl.tyu.wiki", basePath = "/robot", depth = "result")
public interface ClGroupMap {
    @PostMapping("/query/getGroupMap")
    List<Map<String, String>> getGroupMap();
}
