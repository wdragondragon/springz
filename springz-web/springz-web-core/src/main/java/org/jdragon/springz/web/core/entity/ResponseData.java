package org.jdragon.springz.web.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.16 00:21
 * @Description:
 */
@Data
@AllArgsConstructor
public class ResponseData {

    private byte[] data;

    private String contentType;
}
