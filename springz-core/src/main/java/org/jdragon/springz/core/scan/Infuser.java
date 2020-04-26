package org.jdragon.springz.core.scan;

import org.jdragon.springz.core.annotation.Autowired;
import org.jdragon.springz.core.annotation.Qualifier;
import org.jdragon.springz.core.entry.BeanInfo;
import org.jdragon.springz.core.entry.ClassInfo;
import org.jdragon.springz.utils.Bean2Utils;
import org.jdragon.springz.utils.LogBuilder;
import org.jdragon.springz.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.25 17:43
 * @Description: 注入者
 */
public class Infuser implements ScanAction {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Map<String, BeanInfo> beanMap;

    public Infuser(Map<String, BeanInfo> beanMap) {
        this.beanMap = beanMap;
    }

    /**
     * @Author: Jdragon
     * @Date: 2020.04.25 下午 8:15
     * @params: [beanInfo]
     * @return: void
     * @Description: 根据传入的beanInfo来获取已注册bean通过反射给注解下的字段赋值
     **/
    @Override
    public void action(ClassInfo classInfo) {
        try {

            String definitionName = classInfo.getDefinitionName();

            //反射构建对象
            Class<?> c = classInfo.getClazz();

            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                //只有field中有Autowired才继续
                if (!field.isAnnotationPresent(Autowired.class)) {
                    continue;
                }
                Class<?> fieldClass = field.getType();
                String simpleName = fieldClass.getSimpleName();
                //检测是否有qualifier注解，有则使用注解值来获取注入组件
                if (field.isAnnotationPresent(Qualifier.class)) {
                    Qualifier qualifier = field.getDeclaredAnnotation(Qualifier.class);
                    simpleName = qualifier.value();
                }

                simpleName = StringUtils.firstLowerCase(simpleName);

                BeanInfo iBeanInfo = beanMap.get(simpleName);

                Object iBean;
                if(iBeanInfo.getScope().equals(BeanInfo.SINGLETON)){
                    iBean = iBeanInfo.getBean();
                }else{
                    Object oldBean = iBeanInfo.getBean();
                    iBean = Bean2Utils.copy(oldBean);
                }

                field.setAccessible(true);
                //是否为静态字段
                if(Modifier.isStatic(field.getModifiers())){
                    field.set(null, iBean);
                }else{
                    field.set(beanMap.get(definitionName).getBean(), iBean);
                }
                logger.info(LogBuilder.build("注入对象成功",definitionName,simpleName));

            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch ( InstantiationException e){
            logger.error(LogBuilder.build("缺失空参构造器"));
        }
    }
}
