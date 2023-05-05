package org.jdragon.springz.feign.core;

import org.jdragon.springz.utils.http.HttpException;

/**
 * <p></p>
 * <p>create time: 2021/12/29 0:47 </p>
 *
 * @author : Jdragon
 */
public interface ZFeignFallback {
    Object fallback(HttpException httpException);
}
