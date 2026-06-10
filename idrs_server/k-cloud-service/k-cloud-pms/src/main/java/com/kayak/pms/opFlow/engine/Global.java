package com.kayak.pms.opFlow.engine;

import com.kayak.core.system.SysBeans;
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Global {
	private static final Logger log = LoggerFactory.getLogger(Global.class);

	private List<String> globalFiles = new ArrayList<String>();

	private Properties globalProp = new Properties();

	public Global() {
		globalFiles.add("/global");
		init();
	}

	/**
	 * 获取加载好的配置信息
	 *
	 * @return
	 */
	public Properties getGlobalProp() {
		if (!isInited())
			init();

		return globalProp;
	}

	/**
	 * 保存最后加载的配置文件的最后更新时间<br />
	 * 用于在init时判断文件更新时间有变化才重新加载
	 */
	private Map<String, Long> lastModified = null;

	/**
	 * 加载全局配置文件
	 */
	public synchronized void init() {
		List<File> files = new ArrayList<File>();
		// 收集所有指定的文件和文件夹下的global-*.properties文件

		for (String gfile : getGlobalFiles()) {

			Resource[] resources = FileUtil.getResources("classpath*:" + gfile + "/global*.properties");

			if (resources != null && resources.length > 0) {
				for (Resource resource : resources) {
					InputStream in = null;
					try {
						in = resource.getInputStream();
						globalProp.load(in);

						String filepath = resource.getURI().getPath();
						getLastModified().put(filepath, resource.lastModified());
						log.info("加载全局配置文件成功：" + filepath);
					} catch (IOException e) {
						log.error(e.getMessage(), e);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
								log.error(e.getMessage(), e);
							}
						}
					}
				}
			}
		}
	}

	public void destroy() {
		lastModified.clear();
		lastModified = null;
		globalProp.clear();
		globalProp = null;
	}

	public String getGlobalConfig(String prop, String def) {
		Properties p = this.getGlobalProp();
		String value = p.getProperty(prop);
		if (value == null || "".equals(value.trim())) {
			if (def == null || "".equals(def.trim())) {
				return "";
			}
			return def.trim();
		}
		return value.trim();
	}

	/**
	 * 读取全局配置信息
	 *
	 * @param propKey
	 *            要读取的配置变量名
	 * @param defaultValue
	 *            默认值，当找不到propKey或其值为空时，返回defaultValue
	 * @return
	 */
	public static String getGlobalConf(String propKey, String defaultValue) {
		Global global = SysBeans.getBean("global");
		return global.getGlobalConfig(propKey, defaultValue);
	}

	/**
	 * 读取全局配置信息
	 *
	 * @param propKey
	 *            要读取的配置变量名
	 * @return
	 */
	public static String getGlobalConf(String propKey) {
		return getGlobalConf(propKey, null);
	}

	private static final String REX = "\\{([\\w.]+)\\}";
	private static final Pattern patternParam = Pattern.compile(REX);

	/**
	 * 为str注入使用大括号{}引用的全局参数变量值
	 *
	 * @param str
	 * @return
	 */
	public String setGlobalParam(String str) {
		StringBuilder sb = new StringBuilder(str);
		Matcher matcher = patternParam.matcher(str);
		while (matcher.find()) {
			// 获取参数名称
			String name = sb.substring(matcher.start() + 1, matcher.end() - 1);
			// 获取参数值
			String val = this.getGlobalConfig(name, "");
			sb.replace(matcher.start(), matcher.end(), val);
		}
		return sb.toString();
	}

	/**
	 * 系统字符转换的编码方式
	 */
	public static final String charset() {
		return getGlobalConf("Global.charset", "utf-8");
	}

	/**
	 * 系统管理员登录名称
	 */
	public static final String ADMIN_LOGINAME() {
		return getGlobalConf("Global.ADMIN_LOGINAME", "admin");
	}

	private static int PARAM_COLUMN_LEN = -1;

	/**
	 * 保存参数值字段的长度
	 */
	public static final int PARAM_COLUMN_LEN() {
		if (PARAM_COLUMN_LEN == -1)
			PARAM_COLUMN_LEN = Tools.str2Int(getGlobalConf("Global.PARAM_COLUMN_LEN", "1024"));
		return PARAM_COLUMN_LEN;
	}

	private static Boolean LOGIN_VALIDATE_CODE = null;

	/**
	 * 登录时是否需要验证码
	 */
	public static final Boolean LOGIN_VALIDATE_CODE() {
		if (LOGIN_VALIDATE_CODE == null)
			LOGIN_VALIDATE_CODE = Tools.str2Boolean(getGlobalConf("Global.LOGIN_VALIDATE_CODE", "true"));
		return LOGIN_VALIDATE_CODE;
	}

	/**
	 * 从客户端提交来的查询是否需要记录日志的参数名称<br />
	 * 当前端调用comnQuery时，只要传来这个名称的参数值为true就为这次查询记录日志
	 */
	public static final String QUERY_NEEDLOG() {
		return getGlobalConf("Global.QUERY_NEEDLOG", "bizr2_query_needlog");
	}

	/**
	 * @return the lastModified
	 */
	public long getLastModified(String filepathname) {
		return lastModified.get(filepathname);
	}

	public void reload() {
		init();
	}

	public boolean isInited() {
		return !getLastModified().isEmpty();
	}

	public List<String> getGlobalFiles() {
		return this.globalFiles;
	}

	/**
	 * @param globalFiles
	 *            the globalFiles to set
	 */
	public void setGlobalFiles(List<String> globalFiles) {
		if (globalFiles == null || globalFiles.isEmpty())
			return;
		getGlobalFiles().addAll(globalFiles);
	}

	/**
	 * @return the lastModified
	 */
	public Map<String, Long> getLastModified() {
		if (this.lastModified == null) {
			this.lastModified = new HashMap<String, Long>();
		}
		return lastModified;
	}

}