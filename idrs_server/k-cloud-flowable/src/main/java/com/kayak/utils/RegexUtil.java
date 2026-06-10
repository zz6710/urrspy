package com.kayak.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuanjinqiao
 * @date 2019-04-25 23:21
 */
public class RegexUtil {

    private static Pattern sqlPattern = Pattern.compile("\\$[S|s|N|n]\\{(.*?)}");

    private static Pattern urlPattern = Pattern.compile("\\$\\{(.*?)}");

    public static String getSql(String sql, Map<String, Object> params) {
        String originalSql = sql;
        Matcher matcher = sqlPattern.matcher(sql);
        while (matcher.find()) {
            String originalGroup = matcher.group();
            String fixedGroup = originalGroup.replaceAll("\\s+", "");
            String key = fixedGroup.substring(fixedGroup.indexOf("{") + 1, +fixedGroup.lastIndexOf("}"));
            if (fixedGroup.contains("$S{") || fixedGroup.contains("$s{")) {
                String value = (String) StringUtils.getJsonValue(params, key);
                if (StringUtils.isEmpty(value)) {
                    throw new RuntimeException("sql: " + originalSql + " 对应的参数:" + key + " 值不存在");
                }
                sql = sql.replace(originalGroup, "\"" + value + "\"");
            }

            if (fixedGroup.contains("$N{") || fixedGroup.contains("$n{")) {
                Object value = StringUtils.getJsonValue(params, key);
                if (value == null) {
                    throw new RuntimeException("sql: " + originalSql + " 对应的参数:" + key + " 值不存在");
                }
                BigDecimal number = new BigDecimal(value.toString());
                sql = sql.replace(originalGroup, number.toString());
            }
        }
        return sql;
    }

    public static String getUrl(String url, Map<String, Object> params) {
        String originalSql = url;
        Matcher matcher = urlPattern.matcher(url);
        while (matcher.find()) {
            String group = matcher.group();
            String key = group.substring(group.indexOf("{") + 1, +group.lastIndexOf("}"));
            Object value = StringUtils.getJsonValue(params, key);
            if (value == null) {
                throw new RuntimeException("url: " + originalSql + " 对应的参数:" + key + " 值不存在");
            }
            url = url.replace(group, value.toString());
        }
        return url;
    }

    public static void main(String[] args) {
        Map<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("roleid", 1.3);
        objectObjectHashMap.put("rolename", "222");
        objectObjectHashMap.put("a", "3");

        Map<String, Object> map = new HashMap<>();
        map.put("userid", "userid111");
        map.put("username", "name111");
        objectObjectHashMap.put("user", map);

        String url = getUrl("WorkflowServer/test/getLeader.json?roleid=${roleid}&rolename=${user.username}", objectObjectHashMap);
        System.out.println(url);
    }

    public static String getNumber(String str) {
        //先判断有没有整数，如果没有整数那就肯定就没有小数
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(str);
        String result = "";
        if (m.find()) {
            Map<Integer, String> map = new TreeMap();
            Pattern p2 = Pattern.compile("(\\d+\\.\\d+)");
            m = p2.matcher(str);
            //遍历小数部分
            while (m.find()) {
                result = m.group(1) == null ? "" : m.group(1);
                int i = str.indexOf(result);
                String s = str.substring(i, i + result.length());
                map.put(i, s);
                //排除小数的整数部分和另一个整数相同的情况下，寻找整数位置出现错误的可能，还有就是寻找重复的小数
                // 例子中是排除第二个345.56时第一个345.56产生干扰和寻找整数345的位置时，前面的小数345.56会干扰
                str = str.substring(0, i) + str.substring(i + result.length());
            }
            //遍历整数
            Pattern p3 = Pattern.compile("(\\d+)");
            m = p3.matcher(str);
            while (m.find()) {
                result = m.group(1) == null ? "" : m.group(1);
                int i = str.indexOf(result);
                //排除jia567.23.23在第一轮过滤之后留下来的jia.23对整数23产生干扰
                if (String.valueOf(str.charAt(i - 1)).equals(".")) {
                    //将这个字符串删除
                    str = str.substring(0, i - 1) + str.substring(i + result.length());
                    continue;
                }
                String s = str.substring(i, i + result.length());
                map.put(i, s);
                str = str.substring(0, i) + str.substring(i + result.length());
            }
            result = "";
            for (Map.Entry<Integer, String> e : map.entrySet()) {
                result += e.getValue() + ",";
            }
            result = result.substring(0, result.length() - 1);
        } else {
            result = "";
        }
        return result;
    }
}
