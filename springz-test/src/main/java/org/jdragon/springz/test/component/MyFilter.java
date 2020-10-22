package org.jdragon.springz.test.component;


import org.jdragon.springz.scanner.TypeFilter;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.30 16:49
 * @Description:
 */
public class MyFilter implements TypeFilter {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean match(Class<?> clazz) {
//        logger.trace("Filter",clazz.getName());
        return false;
    }
}
