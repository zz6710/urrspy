package com.kayak.pms.disclosureControl.disclousreEnum;

/**信批错误类型
 * @author WangTao
 * @version 1.0
 * @date 2022/07/15
 */
public enum ErrorTypeEnum {
    DISCLOSURE_NOTICE_PUB_ERR("公告文件发布异常", "21"),
    DISCLOSURE_NOTICE_FILE_HANDLE_ERR("公告文件处理异常", "22");

    // 成员变量
    private String value;
    private String name;

    // 构造方法
    private ErrorTypeEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}
