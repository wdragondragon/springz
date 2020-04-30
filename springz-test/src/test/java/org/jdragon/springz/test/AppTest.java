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
    public void test(){
        Class<?> clazz = App.class;
        System.out.println(clazz.getPackage().getName());
    }
}
