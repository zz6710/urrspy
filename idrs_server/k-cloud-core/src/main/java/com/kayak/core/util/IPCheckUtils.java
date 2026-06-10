package com.kayak.core.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * IP检查工具类
 * 功能：检查客户端IP是否为内网IP，并将结果存入LocalStorage
 */
public class IPCheckUtils {
    protected static final Logger log = LoggerFactory.getLogger(IPCheckUtils.class);

    // 内网IP段白名单
    private static final Set<String> INTERNAL_IP_WHITELIST = new HashSet<>(Arrays.asList(
            // A类私有地址
            "10.0.0.0/8",
            // B类私有地址
            "172.16.0.0/12",
            // C类私有地址
            "192.168.0.0/16",
            // 本地回环地址
            "127.0.0.0/8",
            // 链路本地地址
            "169.254.0.0/16",
            // 运营商级NAT保留地址
            "100.64.0.0/10",
            // 多播地址
            "224.0.0.0/4",
            // 保留地址
            "240.0.0.0/4"
    ));

    // IPv6内网地址
    private static final Set<String> INTERNAL_IPV6_WHITELIST = new HashSet<>(Arrays.asList(
            "::1/128",            // 本地回环
            "fc00::/7",           // 唯一本地地址
            "fe80::/10",          // 链路本地地址
            "ff00::/8"            // 多播地址
    ));

    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$"
    );

    private IPCheckUtils() {
        // 工具类，防止实例化
    }


//        log.info("IP1 Proxy-Client-IP："+request.getHeader("Proxy-Client-IP")+
//                "\r IP1 WL-Proxy-Client-IP："+request.getHeader("WL-Proxy-Client-IP")+
//                "\r IP1 HTTP_CLIENT_IP："+request.getHeader("HTTP_CLIENT_IP")+
//                "\r IP1 X-Forwarded-For："+ request.getHeader("X-Forwarded-For")+
//                "\r IP1 HTTP_X_FORWARDED_FOR："+ request.getHeader("HTTP_X_FORWARDED_FOR")+
//                "\r IP1 HTTP_X_FORWARDED："+ request.getHeader("HTTP_X_FORWARDED")+
//                "\r IP1 HTTP_FORWARDED："+ request.getHeader("HTTP_FORWARDED")+
//                "\r IP1 HTTP_FORWARDED_FOR："+ request.getHeader("HTTP_FORWARDED_FOR")+
//                "\r IP1 HTTP_X_CLUSTER_CLIENT_IP："+ request.getHeader("HTTP_X_CLUSTER_CLIENT_IP")+
//                "\r IP1 HTTP_VIA："+ request.getHeader("HTTP_VIA")+
//                "\r IP1 RemoteAddr："+ request.getRemoteAddr()
//                );
    /**
     * 获取客户端真实IP地址
     * 支持代理服务器（Nginx，Apache等）
     */
    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }

        log.info("IP1 Proxy-Client-IP："+request.getHeader("Proxy-Client-IP")+
                "\r IP1 WL-Proxy-Client-IP："+request.getHeader("WL-Proxy-Client-IP")+
                "\r IP1 HTTP_CLIENT_IP："+request.getHeader("HTTP_CLIENT_IP")+
                "\r IP1 X-Real-IP ："+ request.getHeader("X-Real-IP")+
                "\r IP1 X-Forwarded-For："+ request.getHeader("X-Forwarded-For")+
                "\r IP1 HTTP_X_FORWARDED_FOR："+ request.getHeader("HTTP_X_FORWARDED_FOR")+
                "\r IP1 HTTP_X_FORWARDED："+ request.getHeader("HTTP_X_FORWARDED")+
                "\r IP1 HTTP_FORWARDED："+ request.getHeader("HTTP_FORWARDED")+
                "\r IP1 HTTP_FORWARDED_FOR："+ request.getHeader("HTTP_FORWARDED_FOR")+
                "\r IP1 HTTP_X_CLUSTER_CLIENT_IP："+ request.getHeader("HTTP_X_CLUSTER_CLIENT_IP")+
                "\r IP1 HTTP_VIA："+ request.getHeader("HTTP_VIA")+
                "\r IP1 Header RemoteAddr："+ request.getHeader("REMOTE_ADDR")+
                "\r IP1 params RemoteAddr："+ request.getRemoteAddr()
        );

        System.out.println("IP1x Proxy-Client-IP："+String.valueOf(request.getHeader("Proxy-Client-IP"))+
                "\r\n IP1 WL-Proxy-Client-IP："+String.valueOf(request.getHeader("WL-Proxy-Client-IP"))+
                "\r\n IP1 HTTP_CLIENT_IP："+String.valueOf(request.getHeader("HTTP_CLIENT_IP"))+
                "\r\n IP1 X-Real-IP ："+ String.valueOf(request.getHeader("X-Real-IP"))+
                "\r\n IP1 X-Forwarded-For："+ String.valueOf(request.getHeader("X-Forwarded-For"))+
                "\r\n IP1 HTTP_X_FORWARDED_FOR："+ String.valueOf(request.getHeader("HTTP_X_FORWARDED_FOR"))+
                "\r\n IP1 HTTP_X_FORWARDED："+ String.valueOf(request.getHeader("HTTP_X_FORWARDED"))+
                "\r\n IP1 HTTP_FORWARDED："+ String.valueOf(request.getHeader("HTTP_FORWARDED"))+
                "\r\n IP1 HTTP_FORWARDED_FOR："+ String.valueOf(request.getHeader("HTTP_FORWARDED_FOR"))+
                "\r\n IP1 HTTP_X_CLUSTER_CLIENT_IP："+ String.valueOf(request.getHeader("HTTP_X_CLUSTER_CLIENT_IP"))+
                "\r\n IP1 HTTP_VIA："+ String.valueOf(request.getHeader("HTTP_VIA"))+
                "\r\n IP1 Header HOST："+ String.valueOf(request.getHeader("HOST"))+
                "\r\n IP1 Header RemoteAddr："+ String.valueOf(request.getHeader("REMOTE_ADDR"))+
                "\r\n IP1x params RemoteAddr："+ request.getRemoteAddr()
        );

        String[] headers = {
                "X-Real-IP",
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        String ip = null;
        for (String header : headers) {
            ip = request.getHeader(header);
            // 处理多个IP的情况（如X-Forwarded-For: client, proxy1, proxy2）
            if (ip != null && ip.contains(",")) {
                ip = ip.split(",")[0].trim();
            }
            if (isValidIp(ip)) {
                break;
            }
        }
        // 如果从header中获取不到，使用request.getRemoteAddr()
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }



        return ip != null ? ip.trim() : "0.0.0.0";
    }

    /**
     * 检查IP是否为内网IP
     */
    public static boolean isInternalIp(String ip) {
        if (!isValidIp(ip)) {
            return false;
        }

        // 判断IPv4还是IPv6
        if (isIPv4(ip)) {
            return isInternalIPv4(ip);
        } else if (isIPv6(ip)) {
            return isInternalIPv6(ip);
        }

        return false;
    }

    /**
     * 检查IPv4是否为内网IP
     */
    private static boolean isInternalIPv4(String ip) {
        try {
            long ipLong = ipToLong(ip);

            for (String cidr : INTERNAL_IP_WHITELIST) {
                if (isIpInRange(ipLong, cidr)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // 记录日志，生产环境应使用日志框架
//         log.error("检查内网IP时发生错误: {}", ip, e);
        }

        return false;
    }

    /**
     * 检查IPv6是否为内网IP
     */
    private static boolean isInternalIPv6(String ip) {
        try {
            for (String cidr : INTERNAL_IPV6_WHITELIST) {
                if (isIPv6InRange(ip, cidr)) {
                    return true;
                }
            }
        } catch (Exception e) {
            // 记录日志
        }

        return false;
    }

    /**
     * 将IP地址转换为long
     */
    private static long ipToLong(String ip) {
        String[] ipSegments = ip.split("\\.");
        long result = 0;
        for (int i = 0; i < 4; i++) {
            result += Long.parseLong(ipSegments[i]) << (24 - (8 * i));
        }
        return result;
    }

    /**
     * 判断IP是否在CIDR范围内
     */
    private static boolean isIpInRange(long ipLong, String cidr) {
        if (!cidr.contains("/")) {
            return ipLong == ipToLong(cidr);
        }

        String[] parts = cidr.split("/");
        String network = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        long networkLong = ipToLong(network);
        long mask = (0xFFFFFFFFL) << (32 - prefixLength);

        return (ipLong & mask) == (networkLong & mask);
    }

    /**
     * 判断IPv6是否在CIDR范围内（简化版）
     */
    private static boolean isIPv6InRange(String ip, String cidr) {
        if (!cidr.contains("/")) {
            return ip.equalsIgnoreCase(cidr);
        }

        String[] parts = cidr.split("/");
        String network = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        try {
            InetAddress ipAddr = InetAddress.getByName(ip);
            InetAddress networkAddr = InetAddress.getByName(network);

            byte[] ipBytes = ipAddr.getAddress();
            byte[] networkBytes = networkAddr.getAddress();

            if (ipBytes.length != networkBytes.length) {
                return false;
            }

            // 计算需要检查的字节数
            int fullBytes = prefixLength / 8;
            int remainingBits = prefixLength % 8;

            // 检查完整字节
            for (int i = 0; i < fullBytes; i++) {
                if (ipBytes[i] != networkBytes[i]) {
                    return false;
                }
            }

            // 检查剩余位
            if (remainingBits > 0 && fullBytes < ipBytes.length) {
                byte mask = (byte) (0xFF << (8 - remainingBits));
                if ((ipBytes[fullBytes] & mask) != (networkBytes[fullBytes] & mask)) {
                    return false;
                }
            }

            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 验证IP地址格式
     */
    public static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)
                && (isIPv4(ip) || isIPv6(ip));
    }

    /**
     * 判断是否为IPv4地址
     */
    public static boolean isIPv4(String ip) {
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * 判断是否为IPv6地址
     */
    public static boolean isIPv6(String ip) {
        return IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * 判断是否可展示(是否不在黑名单ip网段内)
     * 可展示-不在黑名单  true 1  默认true  1
     * 在黑名单网段   false 0
     */
    public static int checkInValidIp(String ip,String ipListStr) {
        try{
            String []  arrs = ipListStr.split(",");
            for(String str : arrs){
                if(str.equals(ip)){
                    return 0;
                }
            }
        }catch (Exception e){
            return 1;
        }
        return 1;
    }


    /** 此处的代码需要通过登录接口 放到前台执行
     * 生成LocalStorage存储的JavaScript代码
     */
    public static String generateLocalStorageJs(boolean isInternal) {
        return String.format(
                "<script type=\"text/javascript\">" +
                        "try {" +
                        "   localStorage.setItem('checkFlag', '%s');" +
                        "   localStorage.setItem('checkFlagTimestamp', new Date().getTime());" +
                        "} catch (e) {" +
                        "   console.error('无法写入LocalStorage:', e);" +
                        "}" +
                        "</script>",
                isInternal ? "internal" : "external"
        );
    }

    /**
     * 生成LocalStorage存储的JSON数据（用于API响应）
     */
    public static String generateLocalStorageJson(boolean isInternal) {
        return String.format(
                "{\"checkFlag\":\"%s\",\"timestamp\":%d}",
                isInternal ? "internal" : "external",
                System.currentTimeMillis()
        );
    }
}