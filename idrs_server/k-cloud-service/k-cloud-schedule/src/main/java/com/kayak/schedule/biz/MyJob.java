package com.kayak.schedule.biz;

import com.google.common.base.Strings;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.schedule.dao.QuartzInfoDao;
import com.kayak.schedule.model.QuartzInfo;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * 通用Job类 过滤所有前置任务，通过反射处理，支持递归调用 Title: ast_web Description: 描述 Company: kayak
 * Makedate: 2015-12-29 下午3:39:49
 *
 * @author lixiao
 */
@Slf4j
public class MyJob implements Job {


	/**
	 * 调度工厂Bean
	 */
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (context != null) {
			JobExecutionContext context2 = null;
			QuartzInfo quartzInfo = (QuartzInfo) context.getMergedJobDataMap().get("quartzInfo");
			// 传入的所有前置任务数组
			String[] preJobClassPathV = (String[]) context.getMergedJobDataMap().get("pathArgs");
			String preJobGroup = quartzInfo.getJobGroup();
			String preJobName = quartzInfo.getJobName();
			for (int k = 0; k < preJobClassPathV.length; k++) {
				if (isNotNull(preJobClassPathV[k]) && isNotNull(preJobName) && isNotNull(preJobGroup)) {
					try {
						Object obj = Class.forName(preJobClassPathV[k]).newInstance();
						if (obj == null) {
							return;
						}
						Method[] methods = obj.getClass().getMethods();
						int i = 0;
						for (i = 0; i < methods.length; i++) {
							if (methods[i].getName().equals("execute")) {
								try {
									methods[i].invoke(obj, new Object[] { context2 });
								} catch (IllegalArgumentException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (InvocationTargetException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * \判断非空 描述 : <描述函数实现的功能>. <br>
	 * <p>
	 *
	 * @param str
	 * @return
	 * @author lixiao
	 */
	public boolean isNotNull(String str) {
		if ("".equals(str) || str == null) {
			return false;
		}
		return true;
	}

	public boolean checkIp() throws Exception {
		String osIp = "";
		String execIp = "";
		try {
			QuartzInfoDao quartzInfoDao = SysBeans.getBean("quartzInfoDao");
			String fullName = this.getClass().getCanonicalName();
			SqlRow quarz = quartzInfoDao.getTaskByPath(fullName);
			execIp = quarz.getString("exec_ip");
			if (Strings.isNullOrEmpty(execIp))
				return false;
			osIp=  getLinuxLocalIp();
		} catch (SocketException ex) {
			log.error("获取ip地址异常[{}]", ex);

		}
		log.info("当前系统ip为【{}】", osIp);
		if (execIp.equals(osIp)) {
			return true;
		}
		return false;
	}

	private static String getLinuxLocalIp() throws Exception {

		String sys_exec_ip = SysUtil.getSystemParams().get("80000080107").getString("paravalue");
		String hostip = SystemUtil.get("HOSTIP");
		// 获取本机IP
		if (StrUtil.isBlank(hostip)) {
			log.info("未获取hostIP");
			OsInfo osInfo = SystemUtil.getOsInfo();
			if (osInfo.isLinux()) {
				NetworkInterface eth0 = NetUtil.getNetworkInterface("eth1");
				List<InterfaceAddress> interfaceAddresses = eth0.getInterfaceAddresses();
				interfaceAddresses.contains(sys_exec_ip);
				hostip = sys_exec_ip;
			} else if (osInfo.isMac()) {
				hostip = NetUtil.getLocalMacAddress();
			} else if (osInfo.isWindows()) {
				HostInfo hostInfo = SystemUtil.getHostInfo();
				hostip = hostInfo.getAddress();
			} else {
				hostip = "ip未识别系统";
			}

		}

		return hostip;
	}


}
