package com.kayak.rpt.email.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 邮件对象
 */
@Data
public class EmailInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name="sendEmailName",value = "发件方名称")
    private String emailAcctNoName;
    @ApiModelProperty(name="receiveEMailAccount",value = "接收方")
    private String receiveMailAccount;
    @ApiModelProperty(name="cc",value = "抄送方，多个用分号分隔")
    private String cc;
    @ApiModelProperty(name="attachFilePath",value = "附件地址，多个用分号分隔")
    private String attachFilePath;
    @ApiModelProperty(name="bizType",value = "业务类型")
    private String bizType;
    @ApiModelProperty(name="bizNo",value = "业务流水号")
    private String bizNo;
    @ApiModelProperty(name="emailFileName",value = "附件名称，多个用分号分隔")
    private String emailFileName;
    @ApiModelProperty(name="emailTitle",value = "邮件标题")
    private String emailTitle;
    @ApiModelProperty(name="channelName",value = "渠道名称")
    private String channelName;
    @ApiModelProperty(name="fileCount",value = "附件数量")
    private String fileCount;

    @ApiModelProperty(name="relateFileId",value = "关联文件表ID")
    private String relateFileId;
    @ApiModelProperty(name="reportDate",value = "报告日期")
    private String reportDate;
    @ApiModelProperty(name="lastReportDate",value = "最晚报告日期")
    private String lastReportDate;

    @ApiModelProperty(name="userId",value = "用户ID")
    private String userId;
    @ApiModelProperty(name="userName",value = "收件人名称")
    private String userName;
    @ApiModelProperty(name="noticeNumber",value = "公告条数")
    private String noticeNumber;
    @ApiModelProperty(name="bizSubType",value = "业务公告子类型")
    private String bizSubType;

    @ApiModelProperty(name="bizSubTypeDisPlay",value = "业务公告子类型中文描述")
    private String bizSubTypeDisPlay;

    @ApiModelProperty(name="displayContents",value = "公告审批流程处理完发通知给经办人展示的内容")
    private String displayContents;


}
