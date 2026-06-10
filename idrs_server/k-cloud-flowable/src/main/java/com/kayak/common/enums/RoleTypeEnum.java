package com.kayak.common.enums;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-10-17 17:14
 **/
public enum RoleTypeEnum {
    /**
     * 说明
     */
    UPPER_ORG("upperOrg", "上级机构"),
    PEER_ORG("peerOrg", "同级机构");

    /**
     * 类型
     */
    private final String type;

    /**
     * 说明
     */
    private final String remark;

    RoleTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
