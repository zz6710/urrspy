package com.kayak.pms.opFlow.engine.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationContextHolder implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextHolder.class);

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext ac)
            throws BeansException {
        setContext(ac);
    }

    private static void setContext(ApplicationContext ac) {
        if (context == null) {
            context = ac;
        }
    }

    @SuppressWarnings("unchecked")
    public static RestTemplate getRestTemplate() {
        try {
            return context.getBean("restTemplate", RestTemplate.class);
        } catch (Exception e) {
            logger.info(" context not:restTemplate");
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz) {
        if (context == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, String qualifier) {
        if (context == null) {
            return null;
        }
        return context.getBean(qualifier, clazz);
    }

}
