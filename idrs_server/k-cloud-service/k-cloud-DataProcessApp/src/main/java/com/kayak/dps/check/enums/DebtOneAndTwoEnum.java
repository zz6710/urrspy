package com.kayak.dps.check.enums;

import lombok.Getter;

/**
 * @Author lll
 * @Date 2025/7/22 9:33
 * @Description 中债直连枚举
 * @Version 1.0
 */
@Getter
public enum DebtOneAndTwoEnum {
    // 直连任务对应报表-发送文件任务
    P021A("104", "募集总量登记"),
    P021B("105", "存续期登记"),
    P021C("106", "资产负债登记"),
    P021D("108", "交易登记"),
    P021E("109", "估值信息登记"),
    P021F("110", "底层资产信息登记"),
    P021G("111", "终止登记"),
    P021H("112", "从业人员登记"),
    P021I("113", "公募/私募产品报告登记"),
    P021J("114", "公募/私募产品发行登记"),
    P021K("115", "产品状态登记"),
    P021L("117", "资产持仓登记"),
    P021M("118", "净值信息登记"),

    // 直连任务对应报表-接收文件任务
    P021AR("104", "募集总量登记"),
    P021BR("105", "存续期登记"),
    P021CR("106", "资产负债登记"),
    P021DR("108", "交易登记"),
    P021ER("109", "估值信息登记"),
    P021FR("110", "底层资产信息登记"),
    P021GR("111", "终止登记"),
    P021HR("112", "从业人员登记"),
    P021IR("113", "公募/私募产品报告登记"),
    P021JR("114", "公募/私募产品发行登记"),
    P021KR("115", "产品状态登记"),
    P021LR("117", "资产持仓登记"),
    P021MR("118", "净值信息登记");

    // 中债报送表
    private final String registerFile;
    // 表中文描述
    private final String registerComment;

    DebtOneAndTwoEnum(String registerFile, String registerComment) {
        this.registerFile = registerFile;
        this.registerComment = registerComment;
    }

    public static DebtOneAndTwoEnum getEnumVal(String val) throws Exception {
        for (DebtOneAndTwoEnum type : DebtOneAndTwoEnum.values()) {
            if (val.equals(type.name()))
                return type;
        }
        throw new Exception("未找到匹配的任务ID项");
    }
}
