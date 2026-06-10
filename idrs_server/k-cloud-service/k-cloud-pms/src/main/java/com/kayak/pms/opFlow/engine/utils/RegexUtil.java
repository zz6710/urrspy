package com.kayak.pms.opFlow.engine.utils;

import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.constant.EnvExprTypeConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ddai
 * @date 2019-04-25 23:21
 */
public class RegexUtil {

    //    private static Pattern pattern = Pattern.compile(".*\\$\\[s|S|N|n]\\{(\\w+)}");
    private static Pattern pattern = Pattern.compile("\\$[S|s|N|n]\\{\\s*(\\w+)\\s*}");
    private static Pattern extractPattern = Pattern.compile("\\$[N|n|S|s]\\{\\s*\\S+\\s*\\}");

    public static String getSql(String sql, Map<String, Object> params) {
        String originalSql = sql;
        Matcher matcher = pattern.matcher(sql);
        while (matcher.find()) {
            String originalGroup = matcher.group();
            String fixedGroup = originalGroup.replaceAll("\\s+", "");
            String key = fixedGroup.substring(fixedGroup.indexOf("{") + 1, +fixedGroup.lastIndexOf("}"));
            if (fixedGroup.contains("$S{") || fixedGroup.contains("$s{")) {
                String value = (String) params.get(key);
                if (StringHelper.isEmpty(value)) {
                    throw new RuntimeException("sql: " + originalSql + " 对应的参数:" + key + " 值不存在");
                }
                sql = sql.replace(originalGroup, "'" + value + "'");
            }

            if (fixedGroup.contains("$N{") || fixedGroup.contains("$n{")) {
                Integer value = (Integer) params.get(key);
                if (value == null) {
                    throw new RuntimeException("sql: " + originalSql + " 对应的参数:" + key + " 值不存在");
                }
                sql = sql.replace(originalGroup, value.toString());
            }
        }
        return sql;
    }

    /**
     * 有中文字符. 不好做正则匹配
     *
     * @param text
     * @return
     */
    public static List<EnvExprModel> extractPlaceholder(String text) {
        List<EnvExprModel> result = new ArrayList<>();
        Matcher matcher = extractPattern.matcher(text);
        while (matcher.find()) {
            EnvExprModel eem = new EnvExprModel();
            String originalGroup = matcher.group();
            String key = originalGroup.substring(originalGroup.indexOf("{") + 1, originalGroup.lastIndexOf("}"));
            eem.setOriginal(originalGroup);
            eem.setOriginalTrimed(originalGroup.trim());
            eem.setKey(key);
            if (originalGroup.contains("$S") || originalGroup.contains("$s")) {
                eem.setType(EnvExprTypeConstant.STRING);
            } else {
                eem.setType(EnvExprTypeConstant.NUMBER);
            }
            result.add(eem);
        }
        return result;
    }

}
