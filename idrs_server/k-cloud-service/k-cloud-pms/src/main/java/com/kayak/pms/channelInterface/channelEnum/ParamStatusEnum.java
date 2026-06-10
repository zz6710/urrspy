package com.kayak.pms.channelInterface.channelEnum;

/**
 * @BelongsProject: pms_server
 * @BelongsPackage: com.kayak.pms.channelInterface.channelEnum
 * @Author: wangchenglin
 * @CreateTime: 2023/03/02  09:31
 * @Description: 参数下发状态枚举类
 * @Version: 1.0
 */

public enum ParamStatusEnum {

    PARAM_STATUS_0_未确认("0"),
    PARAM_STATUS_1_待审批("1"),
    PARAM_STATUS_2_审批中("2"),
    PARAM_STATUS_3_审批通过("3"),
    PARAM_STATUS_4_审批拒绝("4"),
    PARAM_STATUS_5_审批撤销("5"),
    PARAM_STATUS_6_待下发("6"),
    PARAM_STATUS_7_已下发处理("7"),
    PARAM_STATUS_8_下发失败("8"),
    PARAM_STATUS_9_无需下发("9");




    private ParamStatusEnum(String code) {
        this.status = code;
    }
    private String status;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
