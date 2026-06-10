package com.kayak.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.KryoCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;

/**
 * @author YangKai
 * created on 2023/5/31
 */
@Configuration
@Slf4j
public class RedissonConfig {

    @Value("${spring.redis.username:}")
    private String username;

    @Bean("redissonClient")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient redissonClient(RedisProperties redisProperties) {

        Config config = initConfig(redisProperties);
        return Redisson.create(config);
    }

    private Config initConfig(RedisProperties redisProperties) {
        Config config = new Config();
        config.setThreads(5);
        config.setNettyThreads(5);
        config.setCodec(new KryoCodec());
        config.setReferenceEnabled(false);
        config.setLockWatchdogTimeout(40000);
        config.setKeepPubSubOrder(false);
        log.info("获取redis用户名：" + username);
//        if (Objects.isNull(redisProperties.getCluster())) {
        if (Objects.isNull(redisProperties.getCluster()) && Objects.isNull(redisProperties.getSentinel())) {
            // use single redis server
            SingleServerConfig singleServerConfig = config.useSingleServer();
            singleServerConfig.setDatabase(redisProperties.getDatabase());
            singleServerConfig.setConnectionMinimumIdleSize(3);
            singleServerConfig.setIdleConnectionTimeout(10000);
            // use "rediss://host:port" for SSL connection
            singleServerConfig.setAddress(String.format("redis://%s:%s", redisProperties.getHost(), redisProperties.getPort()));
//            singleServerConfig.setPassword(redisProperties.getPassword());
            // 用户名放到url参数
//            if(StringUtils.isNotEmpty(username)){
//                singleServerConfig.setUsername(username);
//            }
        } else if (Objects.nonNull(redisProperties.getCluster())) {
            ClusterServersConfig clusterServersConfig = config.useClusterServers();
            clusterServersConfig.setPassword(redisProperties.getPassword());
            clusterServersConfig.setIdleConnectionTimeout(10000);
            clusterServersConfig.setScanInterval(2000);
            // 关键修复：正确处理节点列表
            List<String> nodes = redisProperties.getCluster().getNodes();
            String[] nodeAddresses = nodes.stream()
                    .map(node -> {
                        // 移除任何空格或非法字符
                        String cleanNode = node.trim();
                        // 添加协议前缀（根据是否使用SSL决定）
                        return "redis://" + cleanNode;
                    })
                    .toArray(String[]::new);
            clusterServersConfig.addNodeAddress(nodeAddresses);
            if(StringUtils.isNotEmpty(username)){
                clusterServersConfig.setUsername(username);
            }
        } else if (Objects.nonNull(redisProperties.getSentinel())) {
            SentinelServersConfig sentinelServerConfig = config.useSentinelServers();
            sentinelServerConfig.setPassword(redisProperties.getPassword());
            sentinelServerConfig.setMasterName(redisProperties.getSentinel().getMaster());
            sentinelServerConfig.setSentinelAddresses(redisProperties.getSentinel().getNodes());
            sentinelServerConfig.setIdleConnectionTimeout(10000);
            if(StringUtils.isNotEmpty(username)){
                sentinelServerConfig.setUsername(username);
            }
        }
        return config;
    }

}
