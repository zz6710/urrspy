package com.kayak.cache.util;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kayak.cache.Cache;
import com.kayak.config.ConfigUitl;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.Tools;
import redis.clients.jedis.JedisPool;

public class CacheUtil {

    private static final Logger log = LoggerFactory.getLogger(CacheUtil.class);

    private static Cache cache;


    public static Map<String, List<Map<String, Object>>> customSearchFieldMap=new ConcurrentHashMap<>();
    public static Map<String, List<Map<String, Object>>> customDefaultSearchFieldMap=new ConcurrentHashMap<>();

    private final static RestTemplate restTemplate = new RestTemplate();

    private static JedisPool jedisPool;

    private static JedisPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (CacheUtil.class) {
                if (jedisPool == null) {
                    jedisPool = SysBeans.getBean("jedisPool");
                }
            }
        }
        return jedisPool;
    }

    public static List<SqlRow> getDict(String dict) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        return cache.getDict(dict);
    }

    public static void deleteDict(String dict) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        cache.deleteDict(dict);
    }

    public static String getDictItem(String dict, String key) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        return cache.getDictItem(dict, key);
    }

    public static String getDictItemKey(String dict, String val) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        return cache.getDictItemKey(dict, val);
    }

    public static void delDictItem(String dict, String key) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        cache.delDictItem(dict, key);
    }

    public static String getSystemParam(String key) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        return cache.getSystemParam(key);
    }

    public static void clearSystemParams() {
        cache.clearSystemParam();
    }

    /**
     * 初始化工作流业务配置缓存
     */
    public static void initFlowCache() {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        cache.initFlowCache();
    }

    /**
     * 获取工作流业务配置
     *
     * @param server
     * @return
     */
    public static Object getFlowConfig(String server) {
        if (cache == null) {
            cache = SysBeans.getBean("cache");
        }
        return cache.getFlowConfig(server);
    }


    /**
     * 刷新数字字典缓存
     *
     * @param dict
     */
    public static void freshenDict(String dict) {

        String detection = null;

        try {
            detection = ConfigUitl.getConfigValue("cache.detection");
        } catch (Exception e1) {
        }

        String key = "kcloud_dict";
        String content = Tools.getStringFromDate("yyyy-MM-dd hh:mm:ss", new Date());
        content += "|" + dict;
        if ("nacos".equals(detection)) {
            try {
                ConfigUitl.publicNacosConfig(key, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else if("redis".equals(detection)){
            getJedisPool().getResource().publish(key, content);
        }else {
            new Thread(() -> {
                DiscoveryClient discoveryClient = getDiscoveryClient();

                Map<String, Object> params = new HashMap<String, Object>();

                params.put("dict", dict);

                List<String> services = discoveryClient.getServices();
                for (String service : services) {
                    if ("GatewayServer".equals(service)) {
                        continue;
                    }
                    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
                    for (ServiceInstance serviceInstance : serviceInstances) {
                        try {
                            requestPostForm(serviceInstance, "/dict/reload.json", params);
                        } catch (Exception e) {
                        }
                    }

                }
            }).start();
        }
    }

    /**
     * 刷新系统参数缓存
     */
    public static void freshenSystemParam() {
        String detection = null;
        try {
            detection = ConfigUitl.getConfigValue("cache.detection");
        } catch (Exception e1) {
        }

        String key = "kcloud_system_params";
        String content = Tools.getStringFromDate("yyyy-MM-dd hh:mm:ss", new Date());
        if ("nacos".equals(detection)) {
            try {
                ConfigUitl.publicNacosConfig(key, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else if("redis".equals(detection)){
            getJedisPool().getResource().publish(key, content);
        } else {
            new Thread(() -> {
                DiscoveryClient discoveryClient = getDiscoveryClient();

                Map<String, Object> params = new HashMap<String, Object>();

                List<String> services = discoveryClient.getServices();
                for (String service : services) {
                    if ("GatewayServer".equals(service)) {
                        continue;
                    }
                    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
                    for (ServiceInstance serviceInstance : serviceInstances) {
                        try {
                            requestPostForm(serviceInstance, "/system/reload.json", params);
                        } catch (Exception e) {
                        }
                    }

                }
            }).start();
        }

    }

    /**
     * 刷新工作流缓存
     */
    public static void freshenFlow() {

        String detection = null;
        try {
            detection = ConfigUitl.getConfigValue("cache.detection");
        } catch (Exception e1) {
        }

        String key = "kcloud_wf_busi_config";
        String content = Tools.getStringFromDate("yyyy-MM-dd hh:mm:ss", new Date());
        if ("nacos".equals(detection)) {
            try {
                ConfigUitl.publicNacosConfig(key, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else if("redis".equals(detection)){
            getJedisPool().getResource().publish(key, content);
        } else {
            new Thread(() -> {
                DiscoveryClient discoveryClient = getDiscoveryClient();

                Map<String, Object> params = new HashMap<String, Object>();

                List<String> services = discoveryClient.getServices();
                for (String service : services) {
                    if ("GatewayServer".equals(service)) {
                        continue;
                    }
                    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
                    for (ServiceInstance serviceInstance : serviceInstances) {
                        try {
                            requestPostForm(serviceInstance, "/flow/reload.json", params);
                        } catch (Exception e) {
                        }
                    }

                }
            }).start();
        }

    }

    /**
     * 刷新工作流缓存
     */
    public static void freshenGateway() {

        String detection = null;
        try {
            detection = ConfigUitl.getConfigValue("cache.detection");
        } catch (Exception e1) {
        }

        String key = "kcloud_gateway";
        String content = UUID.randomUUID().toString();
        if ("nacos".equals(detection)) {
            try {
                ConfigUitl.publicNacosConfig(key, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else if("redis".equals(detection)){
            getJedisPool().getResource().publish(key, content);
        } else {
            /*new Thread(() -> {
                DiscoveryClient discoveryClient = getDiscoveryClient();

                Map<String, Object> params = new HashMap<String, Object>();
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances("GatewayServer");
                for (ServiceInstance serviceInstance : serviceInstances) {
                    try {
                        requestPostForm(serviceInstance, "/graphql/reloadConfig.json", params);
                    } catch (Exception e) {
                    }
                }
            }).start();*/
        }

    }

    public static void freshenSearchField() {

        String detection = null;
        try {
            detection = ConfigUitl.getConfigValue("cache.detection");
        } catch (Exception e1) {
        }

        String key = "CustomSearchField";
        String content = UUID.randomUUID().toString();
        if ("nacos".equals(detection)) {
            try {
                ConfigUitl.publicNacosConfig(key, content);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }else if("redis".equals(detection)){
            getJedisPool().getResource().publish(key, content);
        } else {
            new Thread(() -> {
                DiscoveryClient discoveryClient = getDiscoveryClient();

                Map<String, Object> params = new HashMap<String, Object>();

                List<String> services = discoveryClient.getServices();
                for (String service : services) {
                    if ("GatewayServer".equals(service)) {
                        continue;
                    }
                    List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
                    for (ServiceInstance serviceInstance : serviceInstances) {
                        try {
                            requestPostForm(serviceInstance, "/searchField/reload.json", params);
                        } catch (Exception e) {
                        }
                    }

                }
            }).start();
        }

    }

    public static Object requestPostForm(ServiceInstance serviceInstance, String url, Map<String, Object> params) {
        url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + url;
        // headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();

        if (params != null && !params.isEmpty()) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                postParameters.add(key, params.get(key));
            }
        }

        // HttpEntity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, requestHeaders);

        Object body = restTemplate.postForEntity(url, requestEntity, Object.class).getBody();

        return body;
    }

    private static DiscoveryClient getDiscoveryClient() {
        String discoveryClientName = null;
        try {
            discoveryClientName = ConfigUitl.getConfigValue("cache.discoveryClientName");
        } catch (Exception e1) {
            discoveryClientName = "discoveryClient";
        }
        if (Tools.strIsEmpty(discoveryClientName)) {
            discoveryClientName = "discoveryClient";
        }

        DiscoveryClient discoveryClient = null;
        try {
            discoveryClient = SysBeans.getBean(discoveryClientName);
        } catch (Exception e1) {
            discoveryClient = SysBeans.getBean(DiscoveryClient.class);
        }

        if (discoveryClient == null) {
            discoveryClient = SysBeans.getBean(DiscoveryClient.class);
        }


        if (discoveryClient == null) {
            log.error("获取discoveryClient失败，discoveryClientName：" + discoveryClientName);
        }
        return discoveryClient;
    }

}
