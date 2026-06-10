package com.kayak.dps.check.enums;

import lombok.Getter;

/**
 * @Author lll
 * @Date 2025/7/22 9:33
 * @Description 中债直连枚举
 * @Version 1.0
 */
@Getter
public enum DebtThreeEnum {
    // 直连任务对应报表-发送文件任务
    P022A("201", "投资者身份信息登记"),
    P022B("202", "投资者持有信息登记"),
    P022C("203", "投资者明细信息登记"),

    // 直连任务对应报表-接收文件任务
    P022AR("201", "投资者身份信息登记"),
    P022BR("202", "投资者持有信息登记"),
    P022CR("203", "投资者明细信息登记");

    // 中债报送表
    private final String registerFile;
    // 表中文描述
    private final String registerComment;

    DebtThreeEnum(String registerFile, String registerComment) {
        this.registerFile = registerFile;
        this.registerComment = registerComment;
    }

    public static DebtThreeEnum getEnumVal(String val) throws Exception {
        for (DebtThreeEnum type : DebtThreeEnum.values()) {
            if (val.equals(type.name()))
                return type;
        }
        throw new Exception("未找到匹配的任务ID项");
    }
}
