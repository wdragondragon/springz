package org.jdragon.springz.test;

import org.jdragon.springz.core.annotation.Inject;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.test.domain.property.ConsumerProperty;
import org.jdragon.springz.test.domain.property.Consumer;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.Log.LoggerFactory;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.07 19:33
 * @Description:
 */
@Component
public class TestProperty {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private ConsumerProperty consumerProperty;

    @Inject
    private Consumer consumer;

    public void test(){
        logger.debug(consumer.toString());
        logger.debug(consumerProperty.toString());
    }
}
