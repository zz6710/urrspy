package com.kayak.clear.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * Java 网络工具类
 *
 * @author mosy [mosy@kayak.com.cn]
 * @date 2019.09.03
 */
public class NetUtils {
    private final static Logger log = LoggerFactory.getLogger(NetUtils.class);

    private static String ip;
    private static String hostname;

    /**
     * 获取主机IP
     * <br/>报错了,获取不到就返回 NULL
     */
    public static String getHostIP() {
        if (ip != null) {
            return ip;
        }

        synchronized (NetUtils.class) {
            if (ip != null) {
                return ip;
            }

            String ipTmp = getCurrHostIP();
            ipTmp = ipTmp != null ? ipTmp.trim() : ipTmp;
            if (ipTmp != null && !"".equals(ipTmp)) {
                ip = ipTmp;
            } else {
                ip = "192.168.8.136";
            }
        }

        return ip;
    }

    private static final Pattern ipv4Pattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}");

    /**
     * 获取机器IP地址
     *
     * @return
     */
    public static String getCurrHostIP() {
        String      sIP         = "";
        InetAddress inetAddress = null;

        try {
            // 优先获取第一网口
            NetworkInterface defaultNi = NetworkInterface.getByName("eth0");
            // 存在指定网口,并且该网口为可用状态
            if (defaultNi != null && defaultNi.isUp() && defaultNi.getInetAddresses().hasMoreElements()) {
                InetAddress defualtInetAddress = defaultNi.getInetAddresses().nextElement();
                // 排除回环地址,IPv6
                if (!defualtInetAddress.isLoopbackAddress() && ipv4Pattern.matcher(defualtInetAddress.getHostAddress()).matches()) {
                    return defualtInetAddress.getHostAddress();
                }
            }

            // 没有找到默认网口
            boolean                       bFindIP       = false;
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                if (bFindIP) {
                    break;
                }
                NetworkInterface         ni  = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                // 跳过不可用网口
                if (ni.isUp() == false) {
                    continue;
                }
                while (ips.hasMoreElements()) {
                    InetAddress ia = ips.nextElement();
                    // 排除回环地址,IPv6
                    if (!ia.isLoopbackAddress() && ipv4Pattern.matcher(ia.getHostAddress()).matches()) {
                        bFindIP = true;
                        inetAddress = ia;
                        break;
                    }
                }
            }

            // 最后找不到了就只能这个样子了
            if (inetAddress == null) {
                inetAddress = InetAddress.getLocalHost();
            }
        } catch (Exception e) {
            log.error("获取IP地址信息失败:[{}]", e.getMessage(), e);
        }

        if (null != inetAddress) {
            sIP = inetAddress.getHostAddress();
        }
        return sIP;
    }

    /**
     * 获取主机名
     * <br/>报错了,获取不到就返回 NULL
     */
    public static String getHostName() {
        if (hostname != null) {
            return hostname;
        }

        synchronized (NetUtils.class) {
            InetAddress address = null;
            try {
                address = InetAddress.getLocalHost();
                hostname = address.getHostName();
            } catch (UnknownHostException e) {
                log.error("获取主机名失败", e);
            }
        }

        return hostname;
    }

}
