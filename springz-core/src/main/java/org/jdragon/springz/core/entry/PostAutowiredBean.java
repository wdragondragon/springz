package org.jdragon.springz.core.entry;

import org.jdragon.springz.scanner.entry.BeanInfo;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:10
 * @Description: 主要用于处理信息的beanInfo和存放bean处理后的生成lastBean
 */

public class PostAutowiredBean {

    private Object lastBean;

    private BeanInfo beanInfo;

    public PostAutowiredBean(BeanInfo beanInfo) {
        this.beanInfo = beanInfo;
        this.lastBean = beanInfo.getBean();
    }


    public PostAutowiredBean(BeanInfo beanInfo,Object lastBean){
        this.beanInfo = beanInfo;
        this.lastBean = lastBean;
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
