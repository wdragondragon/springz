package org.jdragon.springz.core.postProcessor;

import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.utils.Bean2Utils;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.02 11:39
 * @Description:
 */
public class ScopePostProcessor implements BeanPostProcessor {
    @Override
    public PostAutowiredBean postProcessAfterInitialization(PostAutowiredBean postAutowiredBean) {

        BeanInfo iBeanInfo = postAutowiredBean.getBeanInfo();
        Object lastBean = postAutowiredBean.getLastBean();
        if (iBeanInfo.getScope().equals(BeanInfo.SINGLETON)) {
            lastBean = iBeanInfo.getBean();
        } else {
            try {
                lastBean = Bean2Utils.copy(lastBean);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        postAutowiredBean.setLastBean(lastBean);
        return postAutowiredBean;
    }
}