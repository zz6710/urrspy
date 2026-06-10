package com.kayak.xsql.autoid;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.kayak.core.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.config.ConfigUitl;
import com.kayak.core.util.Tools;

/**
 * 雪花算法
 * 
 */
public class SnowIdUtils {

	private static final Logger log = LoggerFactory.getLogger(SnowIdUtils.class);

	/**
	 * 私有的 静态内部类
	 */
	private static class SnowFlake {

		/**
		 * 内部类对象（单例模式）
		 */
		private static final SnowIdUtils.SnowFlake SNOW_FLAKE = new SnowIdUtils.SnowFlake();
		/**
		 * 起始的时间戳
		 */
		private final long START_TIMESTAMP = 1585644015575L;
		/**
		 * 序列号占用位数
		 */
		private final long SEQUENCE_BIT = 12;
		/**
		 * 机器标识占用位数
		 */
		private final long MACHINE_BIT = 10;
		/**
		 * 时间戳位移位数
		 */
		private final long TIMESTAMP_LEFT = SEQUENCE_BIT + MACHINE_BIT;
		/**
		 * 最大序列号 （4095）
		 */
		private final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
		/**
		 * 最大机器编号 （1023）
		 */
		private final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);
		/**
		 * 生成id机器标识部分
		 */
		private long machineIdPart;
		/**
		 * 序列号
		 */
		private long sequence = 0L;
		/**
		 * 上一次时间戳
		 */
		private long lastStamp = -1L;

		/**
		 * 构造函数初始化机器编码
		 */
		private SnowFlake() {

			// TODO 未考虑相同IP可能会出现相同机器码的情况

			// 生成机器码
//			List<String> ips = getLocalIPList();
//
//			Long ip_ = Long.parseLong(ips.get(0).replaceAll("\\.", ""));
//
//			// 这里取128,为后续机器Ip调整做准备。
//			long machine_id = ip_.hashCode() % 128;
//
//			long localIp = machine_id + (int) (Math.random() * 1000);
//			while (localIp > MAX_MACHINE_ID) {
//				localIp = machine_id + (int) (Math.random() * 1000);
//			}

			long localIp = 0;
			try {
				localIp = Tools.str2Int(SpringContextHolder.getApplicationContext().getEnvironment().getProperty("sql.autoid.snowFlake.machineId"));
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

			// localIp & MAX_MACHINE_ID最大不会超过1023,在左位移12位
			machineIdPart = (localIp & MAX_MACHINE_ID) << SEQUENCE_BIT;
		}

		/**
		 * 获取雪花ID
		 */
		public synchronized long nextId() {
			long currentStamp = timeGen();
			// 避免机器时钟回拨
			while (currentStamp < lastStamp) {
				// //服务器时钟被调整了,ID生成器停止服务.
				throw new RuntimeException(String.format("时钟已经回拨.  Refusing to generate id for %d milliseconds",
						lastStamp - currentStamp));
			}
			if (currentStamp == lastStamp) {
				// 每次+1
				sequence = (sequence + 1) & MAX_SEQUENCE;
				// 毫秒内序列溢出
				if (sequence == 0) {
					// 阻塞到下一个毫秒,获得新的时间戳
					currentStamp = getNextMill();
				}
			} else {
				// 不同毫秒内，序列号置0
				sequence = 0L;
			}
			lastStamp = currentStamp;
			// 时间戳部分+机器标识部分+序列号部分
			return (currentStamp - START_TIMESTAMP) << TIMESTAMP_LEFT | machineIdPart | sequence;
		}

		/**
		 * 阻塞到下一个毫秒，直到获得新的时间戳
		 */
		private long getNextMill() {
			long mill = timeGen();
			//
			while (mill <= lastStamp) {
				mill = timeGen();
			}
			return mill;
		}

		/**
		 * 返回以毫秒为单位的当前时间
		 */
		protected long timeGen() {
			return System.currentTimeMillis();
		}

		/**
		 * 获取IP列表
		 * 
		 * @return
		 */
		public static List<String> getLocalIPList() {
			List<String> ipList = new ArrayList<String>();
			try {
				Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
				NetworkInterface networkInterface;
				Enumeration<InetAddress> inetAddresses;
				InetAddress inetAddress;
				String ip;
				while (networkInterfaces.hasMoreElements()) {
					networkInterface = networkInterfaces.nextElement();
					inetAddresses = networkInterface.getInetAddresses();
					while (inetAddresses.hasMoreElements()) {
						inetAddress = inetAddresses.nextElement();
						if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
							ip = inetAddress.getHostAddress();
							ipList.add(ip);
						}
					}
				}
			} catch (SocketException e) {
				log.error("获取ip列表失败");
			}
			return ipList;
		}
	}

	/**
	 * 获取long类型雪花ID
	 */
	public static long uniqueLong() {
		return SnowIdUtils.SnowFlake.SNOW_FLAKE.nextId();
	}
}
