package com.kayak.pms.disclosureControl.disclousreEnum;

/**信披类型枚举类定义
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/11/13 10:41
 */
public enum DisclosureTypeEnum {
    ONE("产品销售文件", "1"),
    TWO("发行公告", "2"),
    THREE("到期公告", "3"),
    FOUR("运作公告", "4"),
    FIVE ("定期报告", "5"),
    SIX ("整体报告", "6"),
    SEVEN ("重大事项报告", "7"),
    EIGHT ("临时公告", "8"),
    NINE ("净值报告", "9"),
    //TEN("待定", "10),
    ELEVEN("手工报告", "11"),;
    // 成员变量
    private String name;
    private String val;
    // 构造方法
    private DisclosureTypeEnum(String name, String val) {
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
