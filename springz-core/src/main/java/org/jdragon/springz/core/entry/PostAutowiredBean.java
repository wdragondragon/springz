package org.jdragon.springz.core.entry;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.jdragon.springz.scanner.entry.BeanInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:10
 * @Description:
 */

public class PostAutowiredBean {

    private Object lastBean;

    private BeanInfo beanInfo;

    public PostAutowiredBean(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
        this.lastBean = beanInfo.getBean();
    }

    public void setLastBean(Object lastBean) {
        this.lastBean = lastBean;
    }

    public Object getLastBean() {
        return lastBean;
    }

    public BeanInfo getBeanInfo() {
        return beanInfo;
    }
}
