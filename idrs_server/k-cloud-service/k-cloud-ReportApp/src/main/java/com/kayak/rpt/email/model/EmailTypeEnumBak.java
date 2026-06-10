package com.kayak.rpt.email.model;

public enum EmailTypeEnumBak {

    TRUSTEE_APPROVAL_EMAIL("1", "托管行审批"),
    NOTICE_EMAIL("2", "通知邮件"),
    NOTICE_PUBLISH_EMAIL("3", "报告发布"),
    PUBLISH_REPORT_EMAIL("4", "发行登记表");
    private String val;
    private String desc;

    EmailTypeEnumBak(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static String getType(String val) {
        for (EmailTypeEnumBak typeEnum : EmailTypeEnumBak.values()) {
            if (typeEnum.getVal().equals(val))
                return typeEnum.getDesc();
        }
        return null;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
