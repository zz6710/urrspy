package com.kayak.cache.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {
    @Value("${spring.redis.host:127.0.0.1}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.timeout:30000}")
    private int timeout;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.pool.max-active:8}")
    private int maxActive;
    @Value("${spring.redis.pool.max-wait:-1}")
    private int maxWait;
    @Value("${spring.redis.pool.max-idle:8}")
    private int maxIdle;
    @Value("${spring.redis.pool.min-idle:0}")
    private int minIdle;
    @Value("${spring.redis.style:single}")
    private String style;
    @Value("${spring.redis.database:0}")
    private int DB;
    @Value("${spring.redis.nodes:null}")
    private String nodes;
    @Value("${spring.redis.cluster.max-redirects:0}")
    private Integer redirects;
    @Value("${spring.redis.sentinel.master:null}")
    private String master;
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() throws Exception {
        JedisConnectionFactory factory;
        if("single".equals(style)||"none".equals(style)){
            RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host,port);
            if(StringUtils.isNotEmpty(password)){
                config.setPassword(RedisPassword.of(password));
            }
            config.setDatabase(DB);
            factory = new JedisConnectionFactory(config);

            factory.setTimeout(timeout); //设置连接超时时间
        }else if("cluster".equals(style)){
            Map<String, Object> source = new HashMap<>();
            source.put("spring.redis.cluster.nodes", nodes);
            source.put("spring.redis.cluster.timeout", timeout);
            RedisClusterConfiguration config = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
            if(StringUtils.isNotEmpty(password)){
                config.setPassword(RedisPassword.of(password));
            }
            config.setMaxRedirects(redirects.intValue());
            factory = new JedisConnectionFactory(config);
            // TODO: 下面这步很重要
            factory.afterPropertiesSet();
        }else if("sentinel".equals(style)){
            String[] split = nodes.split(",");
            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();
            for (int i = 0; i < split.length; i++) {
                String[] node = split[i].split(":");
                RedisNode redisNode = new RedisNode(node[0], Integer.parseInt(node[1]));
                redisNode.setName(master);
                redisSentinelConfiguration.addSentinel(redisNode);
            }
            redisSentinelConfiguration.setDatabase(DB);
            redisSentinelConfiguration.setMaster(master);
            redisSentinelConfiguration.setPassword(RedisPassword.of(password));
            factory = new JedisConnectionFactory(redisSentinelConfiguration);
            factory.afterPropertiesSet();
        }else{
            throw new Exception(String.format("redis类型配置错误:%s",style));
        }
        factory.getPoolConfig().setMaxIdle(style.equals("none")?0:maxIdle);
        factory.getPoolConfig().setMinIdle(style.equals("none")?0:minIdle);
        factory.getPoolConfig().setMaxTotal(style.equals("none")?0:maxActive);
        factory.getPoolConfig().setMaxWaitMillis(style.equals("none")?-1:maxWait);
        return factory;
    }

//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        // Number of seconds before expiration. Defaults to unlimited (0)
//        cacheManager.setDefaultExpiration(10); //设置key-value超时时间
//        return cacheManager;
//    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template); //设置序列化工具，这样ReportBean不需要实现Serializable接口
        template.afterPropertiesSet();
        return template;
    }

    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxTotal(maxActive);
        jedisPoolConfig.setMinIdle(minIdle);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool(JedisPoolConfig jedisPoolConfig) {
        if (StringUtils.isBlank(password)) {
            password = null;
        }
        return new JedisPool(jedisPoolConfig, host, port, timeout, password, DB);
    }
}