package com.kayak.pms.disclosureControl.disclousreEnum;

/**首页待办类型枚举类定义
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/11/13 10:54
 */
public enum  OperationTypeEnum {
    ONE("投资经理披露确认", "1"),/*定期报告*/
    TWO("数据补录", "2"),/*定期报告*/
    THREE("审批拒绝分发", "3"),/*定期报告*/
    FOUR("审批拒绝后补录", "4"),/*定期报告*/
    FIVE ("托管拒绝后分发", "5"),/*定期报告*/
    SIX ("托管拒绝后补录", "6"),/*定期报告*/
    SEVEN ("发起审批", "7"),/*定期报告*/
    EIGHT ("发送托管", "8"),/*定期报告*/
    NINE ("填写托管意见", "9"),/*定期报告*/
    TEN("复核托管意见", "10"),/*定期报告*/
    ELEVEN("信披经理披露确认", "11"),/*定期报告*/
    TWELVE("底层数据导入", "12"),
    thirteen("定期报告模板审批", "13"),/*定期报告*/
    fourteen("参数调整", "14"),/*发行公告*/;
    // 成员变量
    private String name;
    private String val;
    // 构造方法
    private OperationTypeEnum(String val) {
        this.val = val;
    }
    // 构造方法
    private OperationTypeEnum(String name, String val) {
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
