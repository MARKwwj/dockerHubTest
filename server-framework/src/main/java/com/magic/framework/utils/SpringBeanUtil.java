package com.magic.framework.utils;


import java.util.Map;

public class SpringBeanUtil  {

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return ContextUtil.getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return ContextUtil.getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return ContextUtil.getApplicationContext().getBean(name, clazz);
    }

    //通过Clazz返回指定的Beans
    public static <T> Map<String, T> getBeansByType(Class<T> clazz) {
        return ContextUtil.getApplicationContext().getBeansOfType(clazz);
    }
}
