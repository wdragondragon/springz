package org.jdragon.springz.test;

import static org.junit.Assert.assertTrue;

import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Controller;
import org.jdragon.springz.test.controller.UserController;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    @Test
    public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class<?> clazz = UserController.class;
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass = annotation.annotationType();

            if (aClass.isAnnotationPresent(Controller.class)) {
                //检测是否有value是否设值
                System.out.println("yes");
            }

        }
    }
    public static String getAnnotationAttribute(Annotation annotation, String attribute) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<? extends Annotation> aClass = annotation.getClass();
        Method method = aClass.getDeclaredMethod(attribute);
        return (String) method.invoke(annotation);
    }
}
