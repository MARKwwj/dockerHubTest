package com.magic.framework.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class ContextUtil {
    private static ApplicationContext applicationContext;
    private static Environment environment;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setEnvironment(Environment env) {
        environment = env;
    }

    public static Environment getEnvironment() {
        return environment;
    }
}
