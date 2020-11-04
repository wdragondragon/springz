package org.jdragon.springz.core.infuse;


import org.jdragon.springz.core.annotation.AutowiredZ;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.annotation.Resource;

import org.jdragon.springz.core.processor.BeanPostProcessor;
import org.jdragon.springz.core.entry.PostAutowiredBean;
import org.jdragon.springz.core.processor.PostProcessorContext;
import org.jdragon.springz.scanner.Filter;
import org.jdragon.springz.scanner.Registrar;
import org.jdragon.springz.scanner.ScanAction;
import org.jdragon.springz.scanner.entry.BeanInfo;
import org.jdragon.springz.scanner.entry.ClassInfo;
import org.jdragon.springz.utils.Bean2Utils;
import org.jdragon.springz.utils.Log.LoggerFactory;
import org.jdragon.springz.utils.Log.Logger;
import org.jdragon.springz.utils.StrUtil;


import java.lang.reflect.*;
import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 17:43
 * @Description: 注入者
 */
public class Infuser extends Registrar implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Filter[] filters;

    public Infuser(Filter... filters) {
        this.filters = filters;
    }

    //用作注入原型时构造新对象时的缓存窗口推进
    private final Stack<Object> beanStack = new Stack<>();

    //与beanStack存放的内存一致，只是用作充当索引用key来获取bean
    private final Map<String, Object> cacheMap = new LinkedHashMap<>();


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
            beanStack.push(beanMap.get(definitionName).getBean());
            this.analyze(definitionName, classInfo.getClazz());
            this.refresh();
        }
    }


    public void analyze(String definitionName, Class<?> c) {
        if (!beanMap.containsKey(definitionName)) return;

        Field[] fields = getAutowiredField(c);
        for (Field field : fields) {
            String filedKey = getAutowiredValue(field);
            if (!beanMap.containsKey(filedKey)) {
                logger.warn("注入Bean失败", "找不到ObjKey", filedKey);
                continue;
            }

            Class<?> fieldType = field.getType();
            BeanInfo infuserBeanInfo = beanMap.get(filedKey);
            Object iBean = infuserBeanInfo.getBean();
            //原型要重新构造bean
            if (infuserBeanInfo.getScope().equals(BeanInfo.PROTOTYPE)) {
                //cacheMap中存在bean，则为循环依赖，否则为字段新原型，这时才需要重新构建bean
                if (!cacheMap.containsKey(filedKey)) {
                    iBean = Bean2Utils.copy(iBean, this.getAutowiredField(fieldType));
                    cacheMap.put(filedKey, iBean);
                    beanStack.push(iBean);
                    this.analyze(filedKey, fieldType);
                    //执行注入完这个bean要把缓存删除
                    beanStack.pop();
                    cacheMap.remove(filedKey);
                } else {
                    iBean = cacheMap.get(filedKey);
                }
            }

            iBean = invokePostProcessor(infuserBeanInfo, iBean);//后置处理

            if (this.infuse(beanStack.peek(), field, iBean)) {
                if (beanStack.size() == 1) {
                    logger.info("注入bean成功[类][字段]", definitionName, filedKey);
                }
            } else {
                logger.info("注入bean失败[类][字段]", definitionName, filedKey);
            }
        }
    }

    /**
     * @Description: 根据类获取需要注入的字段
     **/
    private Field[] getAutowiredField(Class<?> c) {
        List<Field> autoField = new ArrayList<>();
        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(AutowiredZ.class) ||
                    field.isAnnotationPresent(Resource.class)) {
                autoField.add(field);
            }
        }
        return autoField.toArray(new Field[0]);
    }

    /**
     * @params: [field]
     * @return: java.lang.String
     * @Description: 获取需注入field的key
     **/
    public String getAutowiredValue(Field field) {
        String filedKey;
        if (field.isAnnotationPresent(AutowiredZ.class)) {
            String autowiredValue = field.getType().getSimpleName();
            //检测是否有qualifier注解，有则使用注解值来获取注入组件
            Qualifier qualifier = field.getAnnotation(Qualifier.class);
            String infuseKey = qualifier == null ? autowiredValue : qualifier.value();
            filedKey = StrUtil.firstLowerCase(infuseKey);
        } else {
            Resource resource = field.getAnnotation(Resource.class);
            String resourceValue = resource.value();
            String infuseKey = resourceValue.isEmpty() ? field.getName() : resourceValue;
            filedKey = StrUtil.firstLowerCase(infuseKey);
        }

        return filedKey;
    }

    /**
     * @Description: 传入注入目标的targetKey，目标内需注入的field，注入对象的objectKey
     **/
    private boolean infuse(Object targetObj, Field field, Object iBean) {
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


    private Object invokePostProcessor(BeanInfo beanInfo, Object lastBean) {
        PostAutowiredBean postAutowiredBean = new PostAutowiredBean(beanInfo, lastBean);

        for (BeanPostProcessor beanPostProcessor : PostProcessorContext.get()) {
            postAutowiredBean = beanPostProcessor.postProcessAfterInitialization(postAutowiredBean);
        }
        return postAutowiredBean.getLastBean();
    }

    private void refresh() {
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
