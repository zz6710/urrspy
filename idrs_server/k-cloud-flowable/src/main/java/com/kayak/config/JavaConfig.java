package com.kayak.config;

import com.kayak.filter.LocalRequestFilter;
import lombok.Data;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 配置文件
 * @author  yuanjinqiao
 * @data    2020-01-21
 */
@Data
@Configuration
public class JavaConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FilterRegistrationBean localRequestFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new LocalRequestFilter());
        registration.addUrlPatterns("*.json");
        registration.setName("localRequestFilter");
        registration.setOrder(1);
        return registration;
    }

}
