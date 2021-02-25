package com.magic.framework;

import com.magic.framework.utils.ContextUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ServerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ContextUtil.setEnvironment(applicationContext.getEnvironment());
        ContextUtil.setApplicationContext(applicationContext);
    }
}
