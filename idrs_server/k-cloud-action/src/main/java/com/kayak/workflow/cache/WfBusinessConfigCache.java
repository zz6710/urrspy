package com.kayak.workflow.cache;

import com.kayak.config.ConfigUitl;
import com.kayak.core.sql.SqlRow;
import com.kayak.workflow.dao.WorkFlowDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author libo
 */
@Component
@Slf4j
public class WfBusinessConfigCache {

    @Autowired
    private WorkFlowDao workFlowDao;

    @Value("${cache.detection}")
    private String detection;

    @Autowired
    private JedisPool jedisPool;

    private ConcurrentMap<String, Object> cacheMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws Exception {
        addListener();
        initCache();
        return;
    }

    private void addListener() throws Exception {
        if(detection.equals("nacos")){
            ConfigUitl.addNacosConfigListener("kcloud_wf_busi_config", (String config) -> {
                initCache();
            });
        } else if (Objects.equals(detection, "redis")) {
            new Thread(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
                @Override
                public void onMessage(String channel, String message) {
                    initCache();
                }
            }, "kcloud_wf_busi_config")).start();
        }
    }

    public void initCache() {
        try {
            List<SqlRow> data = workFlowDao.findAllConfig();
            cacheMap.clear();
            if (CollectionUtils.isEmpty(data)) {
                return;
            }

            for (SqlRow o : data) {
                cacheMap.put(o.getString("server"), o);
            }
            log.info("更新工作流业务关联配置缓存");
        } catch (Exception e) {
            throw new RuntimeException("更新工作流业务关联配置缓存异常", e);
        }
    }

    public Object get(String server) {
        return cacheMap.get(server);
    }

}
