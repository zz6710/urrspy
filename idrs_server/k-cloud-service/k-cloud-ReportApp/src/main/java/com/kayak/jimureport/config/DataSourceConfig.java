package com.kayak.jimureport.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * 1. @description:
 * 2. @author: zhangzhen
 * 3. @time: 2026/4/7
 */
@Configuration
public class DataSourceConfig {
    @Bean(name = "minidaoDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource minidaoDataSource() {
        return DataSourceBuilder.create().build();
    }
}
