package com.kayak.rpt.Investor.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvDataUtils {

    public static List<Map<String,String>> getNewChinese(List<Map<String,String>> list){
        List<Map<String,String>> lists = new ArrayList<>();
        if(list.size() > 0){
            for(int i = 0;i<list.size();i++){
                Map<String,String> map = list.get(i);
                String custName = map.get("custName");
                String custNameOri = map.get("custNameOri");
                String newCust = unicodeToChinese(custNameOri);
                if(!newCust.equals(custName)){
                    map.put("custName", newCust);//若unicode转码与客户名称不一致，则用转码后的中文替换客户名称
                    lists.add(map);
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
        String uni ="\\u4f46\\u8eab\\u65b9";
        System.out.println(unicodeToChinese(uni));
    }

}
