package com.kayak.pms.opFlow.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 业务审批表
 * @author  xiamh
 * @date    2020-01-25
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BusiInfo {

    private String busiId;
    private String server;              // 服务(微服务为服务名，http请求为ip:port , http方式需要判断，暂不支持)
    private String url;                 // 业务回调地址
    private String keys;                // 业务唯一键，逗号隔开(不能存在同样的在途数据)
    private String values;              // 业务唯一值，逗号隔开
    private String processId;           // 流程设计ID
    private String processInstanceId;   // 流程实例ID
    private String processStatus;       // 流程状态
    private String userid;              // 发起用户ID
    private String busStatus;           // 业务执行状态
    private String busReturnMsg;        // 业务执行结果
    private Integer callbackNum;        // 回调次数
    private String startDate;           // 发起日期
    private String startTime;           // 发起时间
    private String updateDate;          // 更新日期
    private String updateTime;          // 更新时间
    private String validateId;          // 校验规则
    private String remark;              // 备注

    private String creatorName;
    private String processDisplayName;

}