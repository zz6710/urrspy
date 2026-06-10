package com.kayak.pms.printTemp.utils;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;

/**
 * @program: k-cloud
 * @description: 网络服务工具类
 * @author: WangZhenXin
 * @create: 2020-12-29 18:29
 * @memo 备注信息
 */
public class NetWorkUtils {

    /**
     * 获取服务端口号
     * @return String
     * @throws Exception
     */
    public static String getLocalPort() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("*:type=Connector,*"), null);
        if (objectNames == null || objectNames.size() <= 0) {
            throw new IllegalStateException("Cannot get the names of MBeans controlled by the MBean server.");
        }
        for (ObjectName objectName : objectNames) {
            String protocol = String.valueOf(mBeanServer.getAttribute(objectName, "protocol"));
            String port = String.valueOf(mBeanServer.getAttribute(objectName, "port"));
            // windows下属性名称为HTTP/1.1, linux下为org.apache.coyote.http11.Http11NioProtocol
            if (protocol.equals("HTTP/1.1") || protocol.equals("org.apache.coyote.http11.Http11NioProtocol")) {
                return port;
            }
        }
        throw new IllegalStateException("Failed to get the HTTP port of the current server");
    }


    /**
     * 获取服务端Ip地址
     * @throws Exception
     */
    public static String getLocalIP() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();
        byte[] ipAddr = addr.getAddress();
        String ipAddrStr = "";
        for (int i = 0; i < ipAddr.length; i++) {
            if (i > 0) {
                ipAddrStr += ".";
            }
            ipAddrStr += ipAddr[i] & 0xFF;
        }
        return ipAddrStr;
    }

    /**
     * 生成访问预览文件的URL
     * @return URL
     * @throws Exception Exception
     */
    public static String generateUrl(String fileName) throws Exception {
        String localIP = NetWorkUtils.getLocalIP();
        String localPort = NetWorkUtils.getLocalPort();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder url = stringBuilder.append("http://").append(localIP).append(":").append(localPort).append("/").append(fileName);
        return url.toString();
    }
}
