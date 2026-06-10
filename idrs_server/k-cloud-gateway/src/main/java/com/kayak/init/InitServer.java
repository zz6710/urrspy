package com.kayak.init;

import com.kayak.config.ConfigUitl;
import com.kayak.graphql.autoconfigure.GraphQLAnnotationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitServer {

	private static Logger log = LoggerFactory.getLogger(InitServer.class);

	@Autowired
	private GraphQLAnnotationImpl graphQLAnnotationImpl;

	boolean isInit = false;

	@Value("${cache.detection}")
	private String detection;

	/*@Autowired
	private JedisPool jedisPool;*/

	@PostConstruct
	public void init() {
		synchronized (InitServer.class) {
			if (!isInit) {
				try {
					initServer();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			isInit = true;
		}
	}

	private void initServer() throws Exception {
		// 监听模型变化，刷新网关缓存，以及graphQL配置
		if(detection.equals("nacos")){
			ConfigUitl.addNacosConfigListener("kcloud_gateway", (String config) -> {
				try {
					graphQLAnnotationImpl.loadConfig();

					log.info("网关权限缓存，graphQL配置刷新成功");
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}

			});
		}
		/*else if (Objects.equals(detection, "redis")) {
			new Thread(() -> jedisPool.getResource().subscribe(new JedisPubSub() {
				@Override
				public void onMessage(String channel, String message) {
					try {
						graphQLAnnotationImpl.loadConfig();
						log.info("网关权限缓存，graphQL配置刷新成功");
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			}, "kcloud_gateway")).start();
		}*/
	}

}
