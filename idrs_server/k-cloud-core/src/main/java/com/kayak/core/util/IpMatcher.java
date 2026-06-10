package com.kayak.core.util;


import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 生产级IP匹配工具类（支持IPv4/IPv6双协议）
 * 核心能力：自动识别IP类型、单IP精确匹配、CIDR网段匹配、多网段批量匹配
 */
public final class IpMatcher {

    // IPv4常量：地址长度（32位）、最大数值（2^32-1）
    private static final int IPV4_BIT_LENGTH = 32;
    private static final BigInteger IPV4_MAX = BigInteger.ONE.shiftLeft(IPV4_BIT_LENGTH).subtract(BigInteger.ONE);
    // IPv6常量：地址长度（128位）、最大数值（2^128-1）
    private static final int IPV6_BIT_LENGTH = 128;
    private static final BigInteger IPV6_MAX = BigInteger.ONE.shiftLeft(IPV6_BIT_LENGTH).subtract(BigInteger.ONE);

    // 私有构造，禁止实例化
    private IpMatcher() {
        throw new AssertionError("工具类禁止实例化");
    }

    /**
     * 单个IP精确匹配（自动适配IPv4/IPv6）
     *
     * @param ip       待匹配IP（如192.168.1.1 或 2001:0db8:85a3:0000:0000:8a2e:0370:7334）
     * @param targetIp 目标IP
     * @return 匹配结果：true=匹配，false=不匹配
     * @throws IpMatchException 当IP格式非法、类型不统一或解析失败时抛出
     */
    public static boolean matchSingleIp(String ip, String targetIp) {
        try {
            // 1. 空值校验
            validateIpNotNull(ip, "待匹配IP");
            validateIpNotNull(targetIp, "目标IP");
            // 2. 解析IP并转为BigInteger（适配IPv6的128位长度）
            BigInteger ipBigInt = ipToBigInteger(ip);
            BigInteger targetIpBigInt = ipToBigInteger(targetIp);
            // 3. 校验IP类型统一（避免IPv4与IPv6跨类型匹配）
            IpType ipType = getIpType(ip);
            IpType targetIpType = getIpType(targetIp);
            if (ipType != targetIpType) {
//                log.info("IP类型不统一：待匹配IP为" + ipType + "，目标IP为" + targetIpType);
                return false;
            }
            // 4. 数值比较
            return ipBigInt.equals(targetIpBigInt);

        } catch (Exception e) {
//            log.info("IP解析失败：待匹配IP=" + ip + "，目标IP=" + targetIp, e);
            return false;
        }
    }

    /**
     * 多个IP匹配（自动适配IPv4/IPv6）
     *
     * @param ip       待匹配IP（如192.168.1.1,192.168.1.2 或 2001:0db8:85a3:0000:0000:8a2e:0370:7334,2001:0db8:85a3:0000:0000:8a2e:0370:7335）
     * @param targetIpStrs 目标IP 数组-字符串形式 “,”分割
     * @return 匹配结果：true=匹配，false=不匹配
     * @throws IpMatchException 当IP格式非法、类型不统一或解析失败时抛出
     */
    public static boolean matchIpStrs(String ip, String targetIpStrs) {
        try {
            // 1. 空值校验
            validateIpNotNull(ip, "待匹配IP");
            validateIpNotNull(targetIpStrs, "目标IPStrs");
            String[] targetIpStr = targetIpStrs.split(",");
            for(String targetIp: targetIpStr){
                // 2. 解析IP并转为BigInteger（适配IPv6的128位长度）
                BigInteger ipBigInt = ipToBigInteger(ip);
                BigInteger targetIpBigInt = ipToBigInteger(targetIp);
                // 3. 校验IP类型统一（避免IPv4与IPv6跨类型匹配）
                IpType ipType = getIpType(ip);
                IpType targetIpType = getIpType(targetIp);
                if (ipType != targetIpType) {
//                log.info("IP类型不统一：待匹配IP为" + ipType + "，目标IP为" + targetIpType);
                    return false;
                }
                // 4. 数值比较
                if(ipBigInt.equals(targetIpBigInt)){
                    return ipBigInt.equals(targetIpBigInt);
                }
            }
            return false;

        } catch (Exception e) {
//            log.info("IP解析失败：待匹配IP=" + ip + "，目标IP=" + targetIp, e);
            return false;
        }
    }

    /**
     * CIDR网段匹配（自动适配IPv4/IPv6，支持两种格式：网段/前缀 或 网段/子网掩码）
     *
     * @param ip   待匹配IP
     * @param cidr 目标网段（如192.168.1.0/24 或 2001:db8::/32）
     * @return 匹配结果：true=在网段内，false=不在网段内
     * @throws IpMatchException 当IP/网段非法、类型不统一或解析失败时抛出
     */
    public static boolean matchCidr(String ip, String cidr) {
        try {
            // 1. 空值校验
            validateIpNotNull(ip, "待匹配IP");
            validateCidrNotNull(cidr);
            // 2. 拆分CIDR（网络地址 + 前缀/子网掩码）
            String[] cidrParts = cidr.split("/");
            if (cidrParts.length != 2) {
//                log.info("CIDR格式非法：" + cidr + "，正确格式如192.168.1.0/24或2001:db8::/32");
                return false;
            }
            String networkIp = cidrParts[0];
            String maskPart = cidrParts[1];
            // 3. 解析IP与网段，校验类型统一
            IpType ipType = getIpType(ip);
            IpType networkType = getIpType(networkIp);
            if (ipType != networkType) {
//                log.info("IP与网段类型不统一：IP为" + ipType + "，网段为" + networkType);
                return false;
            }
            // 4. 转为BigInteger，获取当前IP类型的位长度
            BigInteger ipBigInt = ipToBigInteger(ip);
            BigInteger networkBigInt = ipToBigInteger(networkIp);
            int bitLength = (ipType == IpType.IPV4) ? IPV4_BIT_LENGTH : IPV6_BIT_LENGTH;
            // 5. 计算子网掩码（支持前缀数字或子网掩码格式）
            BigInteger maskBigInt;
            if (maskPart.matches("^\\d+$")) {
                // 前缀格式（如24、64）
                int prefix = Integer.parseInt(maskPart);
                validatePrefix(prefix, bitLength);
                maskBigInt = calculateMaskByPrefix(prefix, bitLength);
            } else {
                // 子网掩码格式（如255.255.255.0 或 ffff:ffff:ffff:ffff::）
                BigInteger maskTmp = ipToBigInteger(maskPart);
                // 校验掩码与IP类型统一
                if (getIpType(maskPart) != ipType) {
//                   log.info("子网掩码类型与IP不统一：IP为" + ipType + "，掩码为" + getIpType(maskPart));
                    return false;
                }
                validateSubnetMask(maskTmp, bitLength);
                maskBigInt = maskTmp;
            }
            // 6. 计算网段的起始（网络地址）与结束（广播地址）
            BigInteger networkStart = networkBigInt.and(maskBigInt);
            BigInteger networkEnd = networkStart.or(maskBigInt.not().and((ipType == IpType.IPV4) ? IPV4_MAX : IPV6_MAX));
            // 7. 判断IP是否在网段范围内
            return ipBigInt.compareTo(networkStart) >= 0 && ipBigInt.compareTo(networkEnd) <= 0;

        } catch (Exception e) {
//            log.info("IP/网段解析失败：待匹配IP=" + ip + "，网段=" + cidr, e);
            return false;
        }
    }

    /**
     * 多网段-黑名单批量匹配（只要匹配任一网段即返回 0，自动适配IPv4/IPv6）
     *
     * @param ip    待匹配IP
     * @param cidrStrs 目标网段数组
     * @return 匹配结果：0=匹配任一网段 ，1=均不匹配
     */
    public static int matchAnyCidr(String ip, String cidrStrs) throws Exception {

        validateIpNotNull(ip, "待匹配IP");
        if(cidrStrs==null){
            return 1;
        }
        String[] cidrs = cidrStrs.split(",");
        if (Objects.isNull(cidrs) || cidrs.length == 0) {
            return 1;
        }
        for (String cidr : cidrs) {
            if (matchCidr(ip, cidr)) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * 有扩展性范围网段配置时     简化配置-代码流程复杂了
     * @param ip
     * @param cidrStrsEx  黑名单网段 例：192.168.12-15.0/24,192.168.60-73.0/24
     * @return 1 不在网段范围内， 0 在网段范围内
     * @throws Exception
     */
    public static int matchAnyCidrEx(String ip, String cidrStrsEx) throws Exception {

        validateIpNotNull(ip, "待匹配IP");
        if(cidrStrsEx==null||"".equals(cidrStrsEx)){
            return 1;
        }
        String[] cidrsPre = cidrStrsEx.split(",");
        if (Objects.isNull(cidrsPre) || cidrsPre.length == 0) {
            return 1;
        }
        List<String>  exList = new ArrayList<>();
        for (String cidrExs : cidrsPre) {
            List<String> list = splitSubnetRange(cidrExs);
            exList.addAll(list);
        }
        for( String  cidrs: exList){
            if (matchCidr(ip, cidrs)){
                return 0;
            }
        }
        return 1;
    }
    // ------------------------------ 核心辅助方法 ------------------------------

    /**
     * IP转为BigInteger（适配IPv4的32位和IPv6的128位）
     *
     * @param ip IP地址字符串
     * @return IP对应的BigInteger值
     */
    private static BigInteger ipToBigInteger(String ip) throws UnknownHostException {
        byte[] ipBytes = InetAddress.getByName(ip).getAddress();
        return new BigInteger(1, ipBytes); // 1表示正数，避免符号位影响
    }

    /**
     * 识别IP类型（IPv4/IPv6）
     *
     * @param ip IP地址字符串
     * @return IpType枚举
     */
    private static IpType getIpType(String ip) throws UnknownHostException {
        byte[] ipBytes = InetAddress.getByName(ip).getAddress();
        return (ipBytes.length == 4) ? IpType.IPV4 : IpType.IPV6;
    }

    /**
     * 按前缀计算子网掩码（支持IPv4/IPv6）
     *
     * @param prefix    前缀（IPv4：0-32，IPv6：0-128）
     * @param bitLength IP类型的位长度（32或128）
     * @return 子网掩码对应的BigInteger
     */
    private static BigInteger calculateMaskByPrefix(int prefix, int bitLength) {
        if (prefix == 0) {
            return BigInteger.ZERO;
        }
        // 左移（位长度 - 前缀）位，再与最大值取与（确保位数正确）
        return BigInteger.ONE.shiftLeft(bitLength - prefix).subtract(BigInteger.ONE)
                .not().and((bitLength == IPV4_BIT_LENGTH) ? IPV4_MAX : IPV6_MAX);
    }

    // ------------------------------ 校验方法 ------------------------------

    private static void validateIpNotNull(String ip, String ipDesc) throws Exception {
        if (Objects.isNull(ip) || ip.trim().isEmpty()) {
            throw new Exception(ipDesc + "不能为空");
        }
    }

    private static void validateCidrNotNull(String cidr) throws Exception {
        if (Objects.isNull(cidr) || cidr.trim().isEmpty()) {
            throw new Exception("目标网段不能为空");
        }
    }

    /**
     * 校验前缀合法性（与IP类型的位长度匹配）
     *
     * @param prefix    前缀值
     * @param bitLength IP类型的位长度（32或128）
     */
    private static void validatePrefix(int prefix, int bitLength) throws Exception {
        if (prefix < 0 || prefix > bitLength) {
            String ipTypeDesc = (bitLength == IPV4_BIT_LENGTH) ? "IPv4" : "IPv6";
            throw new Exception(ipTypeDesc + "前缀非法：" + prefix + "，需在0-" + bitLength + "之间");
        }
    }

    /**
     * 校验子网掩码合法性（必须是连续1后接连续0）
     *
     * @param maskBigInt 掩码的BigInteger值
     * @param bitLength  IP类型的位长度（32或128）
     */
    private static void validateSubnetMask(BigInteger maskBigInt, int bitLength) throws Exception {
        // 合法掩码特征：mask | (mask - 1) == 最大值（如IPv4的0xFFFFFFFF）
        BigInteger max = (bitLength == IPV4_BIT_LENGTH) ? IPV4_MAX : IPV6_MAX;
        if (maskBigInt.or(maskBigInt.subtract(BigInteger.ONE)).compareTo(max) != 0) {
            String maskStr = bigIntegerToIp(maskBigInt, bitLength);
            throw new Exception("子网掩码非法：" + maskStr + "，需为连续1后接连续0的格式");
        }
    }

    /**
     * BigInteger转回IP字符串（用于错误提示）
     *
     * @param ipBigInt  IP的BigInteger值
     * @param bitLength IP类型的位长度（32或128）
     * @return 点分十进制（IPv4）或冒分十六进制（IPv6）字符串
     */
    private static String bigIntegerToIp(BigInteger ipBigInt, int bitLength) {
        try {
            int byteLength = bitLength / 8;
            byte[] ipBytes = ipBigInt.toByteArray();
            // 处理字节长度不一致（如BigInteger去掉前导0后的长度 shorter than 4/16）
            byte[] adjustedBytes = new byte[byteLength];
            System.arraycopy(ipBytes, Math.max(0, ipBytes.length - byteLength),
                    adjustedBytes, Math.max(0, byteLength - ipBytes.length),
                    Math.min(ipBytes.length, byteLength));
            return InetAddress.getByAddress(adjustedBytes).getHostAddress();
        } catch (UnknownHostException e) {
            return ipBigInt.toString(16); // 异常时返回十六进制字符串，确保提示不丢失
        }
    }

    // ------------------------------ 枚举与异常定义 ------------------------------

    /**
     * IP类型枚举（IPv4/IPv6）
     */
    public enum IpType {
        IPV4, IPV6
    }

    /**
     * IP匹配自定义异常（统一异常入口，包含明确错误信息）
     */
    public static class IpMatchException extends Exception {
        public IpMatchException(String message) {
            super(message);
        }

        public IpMatchException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    // ------------------------------ 测试方法 ------------------------------
    public static void main(String[] args) throws Exception {
        try {
            // 1. IPv4测试
            System.out.println("=== IPv4测试 ===");
            System.out.println("单IP匹配：" + matchSingleIp("192.168.2.100", "192.168.1.100")); // true
            System.out.println("单IP匹配：" + matchIpStrs("192.168.2.100", "192.168.1.100,192.168.1.101,192.168.1.102")); // true
            System.out.println("单IP匹配：" + matchIpStrs("", "192.168.1.100,192.168.1.101,192.168.1.102")); // true
            System.out.println("单IP匹配：" + matchIpStrs("", "192.168.1.100,")); // true
            System.out.println("单IP匹配：" + matchIpStrs("", "192.168.1.100")); // true
            System.out.println("单IP匹配：" + matchIpStrs("", "")); // true
            System.out.println("CIDR匹配（前缀）：" + matchCidr("192.168.1.100", "192.168.1.0/24")); // true
//            System.out.println("CIDR匹配（掩码）：" + matchCidr("10.0.5.6", "10.0.0.0/255.255.0.0")); // true
            System.out.println("多网段匹配：" + matchAnyCidr("192.168.2.1", "192.168.1.0/24,192.168.2.0/24")); // true
            System.out.println("多网段匹配：" + matchAnyCidr("192.160.2.1", "192.168.2.0/24,")); // true
            System.out.println("多网段匹配：" + matchAnyCidr("192.168.2.1", ",")); // true
            System.out.println("多网段匹配：" + matchAnyCidr("192.168.2.1", null)); // true
            System.out.println("多网段匹配：" + matchAnyCidr("192.168.2.1", "")); // true


            //扩展网段配置测试
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.2.0","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.2.1","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.3.0","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.1.0","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.15.225","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.69.1","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.72.1","192.168.2-15.0/24,192.168.69-73.0/24"));
            System.out.println("拓展网段匹配："+ matchAnyCidrEx("192.168.74.1","192.168.2-15.0/24,192.168.69-73.0/24"));


            // 2. IPv6测试
//            System.out.println("\n=== IPv6测试 ===");
//            String ipv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
//            String ipv6Target = "2001:db8:85a3::8a2e:370:7334"; // 简化写法
//            String ipv6Cidr = "2001:db8::/32";
//            System.out.println("单IP匹配（简化写法）：" + matchSingleIp(ipv6, ipv6Target)); // true
//            System.out.println("CIDR匹配：" + matchCidr(ipv6, ipv6Cidr)); // true
//            System.out.println("多网段匹配：" + matchAnyCidr(ipv6, "2001:db8::/32, fc00::/7")); // true

        } catch (IpMatchException e) {
            System.err.println("IP匹配失败：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 将包含范围的网段拆分成单个网段
     * @param input 输入字符串，格式如 "192.168.12-15.1/24"
     * @return 拆分后的单个网段列表
     */
    public static List<String> splitSubnetRange(String input) {
        List<String> result = new ArrayList<>();

        try {
            // 分离IP部分和掩码部分
            String[] parts = input.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("输入格式错误，应包含掩码（如 /24）");
            }

            String ipPart = parts[0];
            String mask = parts[1];

            // 分离IP地址的四个部分
            String[] octets = ipPart.split("\\.");
            if (octets.length != 4) {
                throw new IllegalArgumentException("IP地址格式错误");
            }

            // 检查第三段是否包含范围
            if (octets[2].contains("-")) {
                // 解析范围
                String[] rangeParts = octets[2].split("-");
                if (rangeParts.length != 2) {
                    throw new IllegalArgumentException("范围格式错误");
                }

                int start = Integer.parseInt(rangeParts[0]);
                int end = Integer.parseInt(rangeParts[1]);

                // 验证范围
                if (start < 0 || start > 255 || end < 0 || end > 255) {
                    throw new IllegalArgumentException("IP段值应在0-255之间");
                }
                if (start > end) {
                    throw new IllegalArgumentException("起始值不能大于结束值");
                }

                // 生成所有网段
                for (int i = start; i <= end; i++) {
                    String subnet = octets[0] + "." + octets[1] + "." + i + "." + octets[3] + "/" + mask;
                    result.add(subnet);
                }
            }
            // 检查第四段是否包含范围
            else if (octets[3].contains("-")) {
                // 解析范围
                String[] rangeParts = octets[3].split("-");
                if (rangeParts.length != 2) {
                    throw new IllegalArgumentException("范围格式错误");
                }

                int start = Integer.parseInt(rangeParts[0]);
                int end = Integer.parseInt(rangeParts[1]);

                // 验证范围
                if (start < 0 || start > 255 || end < 0 || end > 255) {
                    throw new IllegalArgumentException("IP段值应在0-255之间");
                }
                if (start > end) {
                    throw new IllegalArgumentException("起始值不能大于结束值");
                }

                // 生成所有网段
                for (int i = start; i <= end; i++) {
                    String subnet = octets[0] + "." + octets[1] + "." + octets[2] + "." + i + "/" + mask;
                    result.add(subnet);
                }
            }
            // 如果没有范围，直接返回原值
            else {
                result.add(input);
            }

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("IP地址包含非数字字符", e);
        }

        return result;
    }

    /**
     * 暂不涉及
     * 扩展版本：支持多个位置有范围
     * 例如 "192.168.12-15.10-20/24"
     */
   /* public static List<String> splitSubnetRangeAdvanced(String input) {
        List<String> result = new ArrayList<>();

        try {
            String[] parts = input.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("输入格式错误");
            }

            String ipPart = parts[0];
            String mask = parts[1];

            // 获取所有可能的组合
            List<String> ipCombinations = generateIPCombinations(ipPart);

            for (String ip : ipCombinations) {
                result.add(ip + "/" + mask);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("解析错误: " + e.getMessage(), e);
        }

        return result;
    }*/

    /**
     * 生成IP地址的所有可能组合
     */
   /* private static List<String> generateIPCombinations(String ipPart) {
        List<String> combinations = new ArrayList<>();
        String[] octets = ipPart.split("\\.");

        // 处理每个段
        List<List<Integer>> segments = new ArrayList<>();
        for (String octet : octets) {
            List<Integer> segmentValues = new ArrayList<>();

            if (octet.contains("-")) {
                String[] rangeParts = octet.split("-");
                int start = Integer.parseInt(rangeParts[0]);
                int end = Integer.parseInt(rangeParts[1]);

                for (int i = start; i <= end; i++) {
                    segmentValues.add(i);
                }
            } else {
                segmentValues.add(Integer.parseInt(octet));
            }

            segments.add(segmentValues);
        }

        // 生成所有组合
        generateCombinationsRecursive(segments, 0, new ArrayList<>(), combinations);

        return combinations;
    }*/

    /**
     * 递归生成所有组合
     */
  /*  private static void generateCombinationsRecursive(List<List<Integer>> segments,
                                                      int index,
                                                      List<Integer> current,
                                                      List<String> result) {
        if (index == segments.size()) {
            // 构建IP地址字符串
            StringBuilder ip = new StringBuilder();
            for (int i = 0; i < current.size(); i++) {
                ip.append(current.get(i));
                if (i < current.size() - 1) {
                    ip.append(".");
                }
            }
            result.add(ip.toString());
            return;
        }

        for (int value : segments.get(index)) {
            current.add(value);
            generateCombinationsRecursive(segments, index + 1, current, result);
            current.remove(current.size() - 1);
        }
    }*/
}