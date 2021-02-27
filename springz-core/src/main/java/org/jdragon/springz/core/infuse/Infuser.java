package org.jdragon.springz.core.infuse;


import org.jdragon.springz.core.utils.PostExecutor;
import org.jdragon.springz.core.utils.BeanHelper;
import org.jdragon.springz.scanner.BeanContainer;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.json.JsonUtils;

import java.lang.reflect.*;
import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 17:43
 * @Description: 注入者,用于注入已注册的组件
 */
public class Infuser implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Filter[] filters;

    public Infuser(Filter... filters) {
        this.filters = filters;
    }

    //用作注入原型时构造新对象时的缓存窗口推进
    protected final Stack<Object> beanStack = new Stack<>();

    //与beanStack存放的内存一致，只是用作充当索引用key来获取bean
    protected final Map<String, Object> cacheMap = new LinkedHashMap<>();

    protected final Map<String,BeanInfo> beanMap = BeanContainer.getBeanMap();

    /**
     * @Author: Jdragon
     * @Date: 2020.04.25 下午 8:15
     * @params: [beanInfo]
     * @return: void
     * @Description: 根据传入的beanInfo来获取已注册bean通过反射给注解下的字段赋值
     **/
    @Override
    public void action(ClassInfo classInfo) {
        String definitionName = classInfo.getDefinitionName();

        if (beanMap.containsKey(definitionName)) {
            BeanInfo beanInfo = beanMap.get(definitionName);
            Object bean = beanInfo.getBean();
            Class<?> clazz = classInfo.getClazz();
            beanStack.push(bean);
            this.analyze(definitionName, clazz);
            //注入的后置处理器
            PostExecutor.postConstruct(bean, beanInfo);
            //刷新缓存
            this.refresh();
        }
    }


    /**
     * @Description: 分析要注入的类，进行注入处理
     **/
    protected void analyze(String definitionName, Class<?> c) {
        Field[] fields = BeanHelper.getAutowiredField(c);
        for (Field field : fields) {
            //若已经注入过了，就不需要再注入了
            if (BeanHelper.getFieldValue(field, beanStack.peek()) != null) {
                continue;
            }

            //获取注入的组件在容器中的名称
            String fieldKey = BeanHelper.getAutowiredValue(field);
            if (!beanMap.containsKey(fieldKey)) {
                logger.warn("注入Bean失败", "找不到ObjKey", fieldKey);
                continue;
            }

            //先获取到容器中的单例bean
            Class<?> fieldType = field.getType();
            Object iBean = createAnalyzeBean(fieldKey, fieldType);
            if (infuse(beanStack.peek(), field, iBean)) {
                if (beanStack.size() == 1) {
                    logger.info("注入bean成功[类][字段]", definitionName, fieldKey);
                }
            } else {
                logger.info("注入bean失败[类][字段]", definitionName, fieldKey);
            }
        }
    }

    /**
     * @Description: 获取使用fieldKey对应的bean来生成新的bean
     * 判断单例多例后，经后置处理器处理过
     **/
    public Object createAnalyzeBean(String fieldKey, Class<?> fieldType) {
        BeanInfo infuserBeanInfo = beanMap.get(fieldKey);
        Object iBean = infuserBeanInfo.getBean();
        //cacheMap中存在bean，则为循环依赖，否则为字段新原型，这时才需要重新构建bean
        if (!cacheMap.containsKey(fieldKey)) {
            //原型要重新构造bean
            if (infuserBeanInfo.isPrototype()) {
                iBean = JsonUtils.jsonCopy(iBean, BeanHelper.getAutowiredField(fieldType));
            }
            cacheMap.put(fieldKey, iBean);
            beanStack.push(iBean);
            this.analyze(fieldKey, fieldType);
            //执行注入完这个bean要把缓存删除
            beanStack.pop();
            cacheMap.remove(fieldKey);
        } else {
            iBean = cacheMap.get(fieldKey);
        }

        return PostExecutor.postConstruct(iBean, infuserBeanInfo);
    }

    /**
     * @Description: 传入注入目标的targetKey，目标内需注入的field，注入对象的objectKey
     **/
    protected boolean infuse(Object targetObj, Field field, Object iBean) {
        field.setAccessible(true);
        try {
            //是否为静态字段
            if (Modifier.isStatic(field.getModifiers())) {
                field.set(null, iBean);
            } else {
                field.set(targetObj, iBean);
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void refresh() {
        beanStack.clear();
        cacheMap.clear();
    }

    @Override
    public Filter[] getFilters() {
        return filters;
    }

    @Override
    public Integer getOrder() {
        return 100;
    }
}
