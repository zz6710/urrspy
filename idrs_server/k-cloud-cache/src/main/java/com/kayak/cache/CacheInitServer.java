package com.kayak.cache;

import com.kayak.cache.util.CacheUtil;
import com.kayak.config.ConfigUitl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.Objects;

@Service
public class CacheInitServer implements ApplicationRunner {

    private static Logger log = LoggerFactory.getLogger(CacheInitServer.class);

    boolean isInit = false;

    @Value("${cache.detection}")
    private String detection;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void run(ApplicationArguments args) {
        synchronized (CacheInitServer.class) {
            if (!isInit) {
                try {
                    if("nacos".equals(detection)){
                        initCacheByNacos();
                    }else if("redis".equals(detection)){
                        initCacheByRedis();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            isInit = true;
        }
    }

    public void initCacheByRedis() throws Exception {

        new Thread(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                if (Objects.equals(channel, "kcloud_dict")) {
                    // 监听数据字典缓存编号
                    String dict = message.substring(message.indexOf("|") + 1);
                    log.info("清除数字字典缓存：" + dict);
                    CacheUtil.deleteDict(dict);
                } else if (Objects.equals(channel, "kcloud_system_params")) {
                    // 监听系统参数编号
                    log.info("清除系统参数缓存");
                    CacheUtil.clearSystemParams();
                } else if (Objects.equals(channel, "kcloud_wf_busi_config")) {
                    // 监听工作流缓存编号
                    log.info(" 工作流缓存更新 ");
                    CacheUtil.initFlowCache();
                }
            }
        }, "kcloud_dict", "kcloud_system_params", "kcloud_wf_busi_config")).start();

        // 工作流缓存更新
        CacheUtil.initFlowCache();
    }

    public void initCacheByNacos() throws Exception {
		// 监听数据字典缓存编号
		ConfigUitl.addNacosConfigListener("kcloud_dict", (String config) -> {
			String dict = config.substring(config.indexOf("|") + 1);
			log.info("清除数字字典缓存：" + dict);

			CacheUtil.deleteDict(dict);

		});

		// 监听系统参数编号
		ConfigUitl.addNacosConfigListener("kcloud_system_params", (String config) -> {
			log.info("清除系统参数缓存");
			CacheUtil.clearSystemParams();
		});

		// 监听工作流缓存编号
		ConfigUitl.addNacosConfigListener("kcloud_wf_busi_config", (String config) -> {
			log.info(" 工作流缓存更新 ");
			CacheUtil.initFlowCache();
		});
		// 工作流缓存更新
		CacheUtil.initFlowCache();
	}

}
