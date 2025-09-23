package com.domino.smerp.common.support;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContext implements ApplicationContextAware {
    private static ApplicationContext CONTEXT;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }
    public static <T> T getBean(Class<T> type) {
        return CONTEXT.getBean(type);
    }
    public static Object getBean(String name) {
        return CONTEXT.getBean(name);
    }
}
