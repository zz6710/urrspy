package com.kayak.dps.expresssion.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 理财一、二期白名单配置
 */
public class WhiteABData {

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
}
