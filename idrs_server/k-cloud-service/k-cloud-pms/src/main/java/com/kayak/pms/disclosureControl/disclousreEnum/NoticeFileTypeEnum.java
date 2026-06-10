package com.kayak.pms.disclosureControl.disclousreEnum;

/**信披类型枚举类定义
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/11/13 10:41
 */
public enum NoticeFileTypeEnum {
    DOCX(".docx", "1"),
    PDF(".pdf", "2"),
    ZIP(".zip", "3"),
    DOC(".doc", "4"),
    TXT(".txt", "5");

    // 成员变量
    private String name;
    private String val;

    // 构造方法
    private NoticeFileTypeEnum(String name, String val) {
        this.name = name;
        this.val = val;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
