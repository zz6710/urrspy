package com.kayak.utils;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.kayak.utils.spring.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.io.StringReader;
import java.util.Properties;

@Slf4j
public class ConfigUitl {

	/**
	 * 获取Nacos配置
	 * 
	 * @param serverAddr
	 * @param namespace
	 * @param dataId
	 * @param group
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValue(String serverAddr, String namespace, String dataId, String group,
			String key) throws Exception {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		if (!Tools.strIsEmpty(namespace)) {
			properties.put(PropertyKeyConst.NAMESPACE, namespace);
		}
		ConfigService configService = NacosFactory.createConfigService(properties);
		String content = configService.getConfig(dataId, group, 5000);

		Properties nacosProperties = new Properties();
		nacosProperties.load(new StringReader(content));

		return nacosProperties.getProperty(key) == null ? "" : nacosProperties.getProperty(key);
	}

	/**
	 * 获取Nacos配置
	 * 
	 * @param serverAddr Nacos IP地址及端口，IP:端口
	 * @param dataId
	 * @param group
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValue(String serverAddr, String dataId, String group, String key)
			throws Exception {
		return getNacosConfigValue(serverAddr, ApplicationContextUtils.getApplicationContext().getEnvironment()
				.getProperty("spring.cloud.nacos.config.namespace"), dataId, group, key);
	}

	/**
	 * 获取Nacos配置
	 * 
	 * @param dataId
	 * @param group
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValue(String dataId, String group, String key) throws Exception {
		Environment evn = ApplicationContextUtils.getApplicationContext().getEnvironment();
		String serverAddr = evn.getProperty("spring.cloud.nacos.config.server-addr");
		return getNacosConfigValue(serverAddr, dataId, group, key);
	}

	/**
	 * 获取Nacos配置
	 * 
	 * @param dataId
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValue(String dataId, String key) throws Exception {
		return getNacosConfigValue(dataId, "DEFAULT_GROUP", key);
	}

	/**
	 * 获取配置
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String getConfigValue(String key) throws Exception {
		return ApplicationContextUtils.getApplicationContext().getEnvironment().getProperty(key);
	}

	/**
	 * 获取配置文件
	 * 
	 * @param serverAddr
	 * @param namespace
	 * @param dataId
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValueText(String serverAddr, String namespace, String dataId, String group)
			throws Exception {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
		if (!Tools.strIsEmpty(namespace)) {
			properties.put(PropertyKeyConst.NAMESPACE, namespace);
		}
		ConfigService configService = NacosFactory.createConfigService(properties);
		String content = configService.getConfig(dataId, group, 5000);

		return content;
	}

	/**
	 * 获取配置文件
	 * 
	 * @param dataId
	 * @param group
	 * @return
	 * @throws Exception
	 */
	public static String getNacosConfigValueText(String dataId, String group) throws Exception {
		Environment evn = ApplicationContextUtils.getApplicationContext().getEnvironment();
		String serverAddr = evn.getProperty("spring.cloud.nacos.config.server-addr");
		String namespace = evn.getProperty("spring.cloud.nacos.config.namespace");
		return getNacosConfigValueText(serverAddr, namespace, dataId, group);
	}

    public static void publicNacosConfig(String dataId, String content) throws Exception {
        Environment evn = ApplicationContextUtils.getApplicationContext().getEnvironment();
        String serverAddr = evn.getProperty("spring.cloud.nacos.config.server-addr");
        String username = evn.getProperty("spring.cloud.nacos.username");
        String password = evn.getProperty("spring.cloud.nacos.password");

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.USERNAME, username);
        properties.put(PropertyKeyConst.PASSWORD, password);

        ConfigService configService = NacosFactory.createConfigService(properties);
        boolean flag = configService.publishConfig(dataId, "DEFAULT_GROUP", content);
        if (flag) {
            log.info("发布配置成功:{}", dataId);
        } else {
            log.info("发布配置失败:{}", dataId);
        }
    }

}
