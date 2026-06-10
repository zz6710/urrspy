package com.kayak.pms.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
   
    @Bean
    public logInterceptor saveLogInterceptor(){
        return new logInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        
        registry.addInterceptor(saveLogInterceptor())
        .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}