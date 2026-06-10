package com.kayak.config.utils;

import com.kayak.dps.ods.exception.TxtFileException;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 清算任务工具类
 */
public class UnicodeToChineseUtil {

    public List<Map<String,String>> getNewChinese(List<Map<String,String>> list){
        List<Map<String,String>> lists = new ArrayList<>();
        if(list.size() > 0){
            for(int i = 0;i<list.size();i++){
                Map<String,String> map = list.get(i);
                String custName = map.get("custName");
                String custNameOri = map.get("custNameOri");
                String newCust = unicodeToChinese(custNameOri);
                if(!newCust.equals(custName)){
                    lists.add(list.get(i));
                }
            }
        }
        return lists;
    }

    /**
     * 根据unicode码值获取中文
     * @param unicodeStr
     * @return
     */
    public static String unicodeToChinese(String unicodeStr) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(unicodeStr);
        char ch;
        while (matcher.find()) {
            // 将匹配到的Unicode编码转换为字符
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            // 替换字符串中的Unicode编码为对应的字符
            unicodeStr = unicodeStr.replace(matcher.group(1), String.valueOf(ch));
        }
        return unicodeStr;
    }



    public static void main(String[] args) {
       String aa = "郑金\uE1E2";
       String unicode = StringEscapeUtils.escapeJava(aa);
       System.out.println(unicode);
       String bb = unicodeToChinese(aa);
       System.out.println(bb);
       String last = getLastUnicodeChar(bb);
       System.out.println(last);
    }

    public static String getLastUnicodeChar(String str) {
        if (str == null || str.isEmpty()) return "";

        int endIndex = str.length();
        int lastCodePoint = str.codePointBefore(endIndex);
        return new String(Character.toChars(lastCodePoint));
    }


}
