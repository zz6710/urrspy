package com.kayak.context;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *配置属性  在配置中心配置对应的参数
 *
 */
@Data
@Component
//@ConfigurationProperties(prefix = "email.scheduled.threadpool")
@Configuration
public class EmailThreadPoolProperties {
//    @Value("${email.scheduled.threadpool.minCount}")
    private Integer minCount;
//    @Value("${email.scheduled.threadpool.maxCount}")
    private Integer maxCount;
//    @Value("${email.scheduled.threadpool.blockingQueueCount}")
    private Integer blockingQueueCount;

    //发邮件相关的本地临时路径，不指定易混乱
//    @Value("${email.scheduled.localFilePath}")
    private String rptEmailtempLocalFilePath;
}
