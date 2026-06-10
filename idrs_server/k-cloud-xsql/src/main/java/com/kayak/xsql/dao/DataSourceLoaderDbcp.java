package com.kayak.xsql.dao;

import com.kayak.config.ConfigUitl;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.util.Tools;
import com.kayak.xsql.XsqlUtils;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Properties;

public class DataSourceLoaderDbcp implements DataSourceLoader {
	private static final Logger log = LoggerFactory.getLogger(DataSourceLoaderDbcp.class);

	public DataSource load(String name, String driver, String prefix) throws Exception {
		String url = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".url");
		String user = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".user");
		String password = SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".password");
		int max = Tools.str2Int(SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".max"));
		int maxWaitMillis = Tools.str2Int(SpringContextHolder.getApplicationContext().getEnvironment().getProperty(prefix + ".maxWaitMillis"));
		String publicKey = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("publicKey");
		boolean isEncryption = Boolean.parseBoolean(SpringContextHolder.getApplicationContext().getEnvironment().getProperty("isEncryption"));

		if (maxWaitMillis == 0) {
			maxWaitMillis = 30000;
		}
		// 是否开启加密
		if (isEncryption) {
			password = XsqlUtils.parsePwd(publicKey, password);
		}
		log.info("数据库连接: {}", name);
		log.info("    url: {}", url);
		log.info("    user: {}", user);
		log.info("    max: {}", max);
		log.info("    maxWaitMillis: {}", maxWaitMillis);
		log.info("    driver: {}", driver);
		Properties prop = new Properties();
		prop.setProperty("driverClassName", driver);
		prop.setProperty("url", url);
		if (user != null)
			prop.setProperty("username", user);
		if (password != null)
			prop.setProperty("password", password);

		// maxActive: 最大连接数量
		//判断是否数仓连接配置,如果是,减少连接数
		if(url.contains("netwealth")||url.contains("bta")||url.contains("ewfa")){
			prop.setProperty("maxActive", "" + 3);
			prop.setProperty("minIdle", "-1");
			prop.setProperty("maxIdle", "-1");
			prop.setProperty("initialSize", "0");
			prop.setProperty("maxWaitMillis", "" + 10000);
		}else{
			prop.setProperty("maxTotal", "" + max);
			prop.setProperty("minIdle", "5");
			prop.setProperty("maxIdle", "10");
			prop.setProperty("initialSize", "5");
			prop.setProperty("maxWaitMillis", "" + maxWaitMillis);
		}

		prop.setProperty("logAbandoned", "true");
		prop.setProperty("removeAbandonedTimeout", "" + (1 * 60)); // 秒
		prop.setProperty("numTestsPerEvictionRun", "" + max);
		prop.setProperty("testWhileIdle", "true");
		prop.setProperty("timeBetweenEvictionRunsMillis", "" + (3600 * 1000));
		prop.setProperty("minEvictableIdleTimeMillis", "" + (600 * 1000));
		String _driver = Optional.ofNullable(driver).orElse("");
		if (_driver.contains("oracle")) {
			// Oracle
			prop.setProperty("validationQuery", "SELECT 'x' FROM dual");
		} else if (_driver.contains("db2")) {
			prop.setProperty("validationQuery", "SELECT 1 FROM sysibm.sysdummy1");
		} else {
			// MySQL
			// driver为null时也默认此种写法。如果有其他情况再判断
			prop.setProperty("validationQuery", "SELECT 1");
		}

		DataSource ds = BasicDataSourceFactory.createDataSource(prop);

		return ds;
	}
}
