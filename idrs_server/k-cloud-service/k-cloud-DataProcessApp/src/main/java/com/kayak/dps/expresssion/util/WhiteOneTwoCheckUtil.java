package com.kayak.dps.expresssion.util;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.dps.expresssion.enums.ExpressionWhiteEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 白名单检查与校验
 */
public class WhiteOneTwoCheckUtil {

    //汉字白名单
    private static final String[] char1 = {"\\u4E00", "\\u9FA5"};
    private static final String[] char2 = {"\\uE815", "\\uE864"};
    private static final List<String[]> whiteCh = Arrays.asList(char1, char2);

    //汉字符号白名单
    private static final String[] char10 = {"\\uFF01", "\\uFF5E"};
    private static final String[] char11 = {"\\uFFE0", "\\uFFE5"};
    private static final String[] char12 = {"\\u3000", "\\u3003"};
    private static final String[] char13 = {"\\u3005", "\\u3017"};
    private static final String[] char14 = {"\\u301D", "\\u301E"};
    private static final String[] char15 = {"\\u2010", "\\u2010"};
    private static final String[] char16 = {"\\u2013", "\\u2016"};
    private static final String[] char17 = {"\\u2018", "\\u2019"};
    private static final String[] char18 = {"\\u201C", "\\u201D"};
    private static final String[] char19 = {"\\u2025", "\\u2026"};
    private static final String[] char20 = {"\\u2030", "\\u2030"};
    private static final String[] char21 = {"\\u2032", "\\u2033"};
    private static final String[] char22 = {"\\u2035", "\\u2035"};
    private static final String[] char23 = {"\\u203B", "\\u203B"};
    private static final String[] char24 = {"\\u00B7", "\\u00B7"};
    private static final List<String[]> whiteChCode = Arrays.asList(
            char10, char11, char12, char13, char14, char10, char11, char12, char13, char14, char15, char16, char17,
            char18, char19, char17, char18, char19, char20, char21, char22, char23, char24);

    //阿拉伯数字白名单
    private static final String[] char25 = {"\\u0030", "\\u0039"};
    private static final List<String[]> whiteNumber = new ArrayList<>();

    static {
        whiteNumber.add(char25);
    }

    //英文字母白名单
    private static final String[] char26 = {"\\u0041", "\\u005A"};
    private static final String[] char27 = {"\\u0061", "\\u007A"};
    private static final List<String[]> whiteEn = Arrays.asList(char26, char27);

    //罗马数字白名单
    private static final String[] char28 = {"\\u2160", "\\u216B"};
    private static final List<String[]> whiteRom = new ArrayList<>();

    static {
        whiteRom.add(char28);
    }

    //英文字符（半角符号）白名单
    private static final String[] char29 = {"\\u0020", "\\u0029"};
    private static final String[] char30 = {"\\u002A", "\\u002F"};
    private static final String[] char31 = {"\\u003A", "\\u003F"};
    private static final String[] char32 = {"\\u0040", "\\u0040"};
    private static final String[] char33 = {"\\u005B", "\\u005F"};
    private static final String[] char34 = {"\\u0060", "\\u0060"};
    private static final String[] char35 = {"\\u007B", "\\u007B"};
    private static final String[] char36 = {"\\u007D", "\\u007E"};
    private static final List<String[]> whiteEnCode = Arrays.asList(char29, char30, char31, char32, char33, char34, char35, char36);

    /**
     * 校验全部白名单
     * @param val
     * @return
     */
    public static Boolean whiteAll(String val) {
        String[] whiteArr = new String[]{"en","encode","ch","chcode","row","number"};
        return whiteX(val, whiteArr);
    }

    /**
     * 校验选择的白名单
     * @param val
     * @param valueData
     * @return
     */
    public static Boolean whiteX(String val, String[] valueData) {
        if (ObjectUtil.isEmpty(valueData)) {
            return false;
        }
        //循环匹配每一个字节
        String[] valArr = val.split("");
        for (int i = 0; i < valArr.length; i++) {
//            String valChar = Integer.toHexString(val.charAt(i)).toUpperCase();
            //默认为通过
            int retInt = 0;
            //循环匹配每一个白名单列表
            for (int j = 0; j < valueData.length; j++) {
                if (ExpressionWhiteEnum.WHITE_CH.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteCh)) {
                        retInt++;
                    }
                }
                if (ExpressionWhiteEnum.WHITE_CH_CODE.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteChCode)) {
                        retInt++;
                    }
                }
                if (ExpressionWhiteEnum.WHITE_EN.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteEn)) {
                        retInt++;
                    }
                }
                if (ExpressionWhiteEnum.WHITE_EN_CODE.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteEnCode)) {
                        retInt++;
                    }
                }
                if (ExpressionWhiteEnum.WHITE_ROM.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteRom)) {
                        retInt++;
                    }
                }
                if (ExpressionWhiteEnum.WHITE_NUMBER.getVal().equalsIgnoreCase(valueData[j])) {
                    if (checkWhite(valArr[i], whiteNumber)) {
                        retInt++;
                    }
                }
            }
            // 不符合任意白名单配置
            if (retInt < 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * 白名单通用测试校验
     * @param val
     * @param charList
     * @return
     */
    private static Boolean checkWhite(String val, List<String[]> charList) {
        if (ObjectUtil.isEmpty(val)) {
            return false;
        }
        for (int i = 0; i < charList.size(); i++) {
            String[] whiteArr = charList.get(i);
            String left = UnicodeUtil.toString(whiteArr[0]);
            String right = UnicodeUtil.toString(whiteArr[1]);
            if (val.compareTo(left) >= 0 && val.compareTo(right) <= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将字符串转成unicode
     * @param str 待转字符串
     * @return unicode字符串
     */
    public static String convert(String str) {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            sb.append("\\\\u");
            //取出高8位
            j = (c >>>8);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            //取出低8位
            j = (c & 0xFF);
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return (new String(sb));
    }

}
