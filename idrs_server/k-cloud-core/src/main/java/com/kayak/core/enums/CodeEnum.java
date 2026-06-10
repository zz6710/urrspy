package com.kayak.core.enums;

import java.util.ArrayList;
import java.util.List;


public enum CodeEnum {

    CODE_SELECT(" select "),
    CODE_UPDATE(" update "),
    CODE_DELETE(" delete "),
    CODE_AND("and("),
    CODE_OR("or("),
    CODE_SLEEP("sleep("),
    CODE_3(" sleep ");
    CodeEnum(String val) {
        this.val = val;
    }

    private String val;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public static List<String> getCodeList() {
        List<String> codeList = new ArrayList<>();
        for (CodeEnum value : CodeEnum.values()) {
            codeList.add(value.val);
        }
        return codeList;
    }

}
