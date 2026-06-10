package com.kayak.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * swagger 配置属性
 *
 * @author yuanjinqiao
 */
@Data
@Component
@ConfigurationProperties(prefix = "workflow.scheduled.thread-pool")
public class ThreadPoolProperties {
    private Integer minCount;

    private Integer maxCount;

    private Integer blockingQueueCount;
}
