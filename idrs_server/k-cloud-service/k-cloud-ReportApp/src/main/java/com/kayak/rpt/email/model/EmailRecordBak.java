package com.kayak.rpt.email.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class EmailRecordBak implements Serializable {
    private static final long serialVersionUID = 1L;

   /* private String emailLogNo;
    private String businessNo;
    private String businessType;
    private String sender;
    private String receiver;
    private String cc;
    private String emailSubject;
    private String emailBody;
    @ApiModelProperty(name = "附件", value = "attach_name")
    private String attachName;
    @ApiModelProperty(name = "文件路径", value = "filePath")
    private String filePath;
    private String failReason;
    private String sendStatus;
    private String sendDate;
    private String sendTime;
    private String sendUserId;
    private String sendUserName;
    @ApiModelProperty(name="id", value="id")
    private String id;

    @ApiModelProperty(name="状态(0-已生效,1-已停用)",  value="is_disabled")
    private String status;

    @ApiModelProperty(name="状态",  value="effectflag")
    private String effectflag;

    @ApiModelProperty(name="创建日期",  value="createdate")
    private String createdate;

    @ApiModelProperty(name="创建时间",  value="createtime")
    private String createtime;

    @ApiModelProperty(name="创建人",  value="createuser")
    private String createuser;

    @ApiModelProperty(name="更新日期",  value="updatedate")
    private String updatedate;

    @ApiModelProperty(name="更新时间",  value="updatetime")
    private String updatetime;

    @ApiModelProperty(name="更新人",  value="updateuser")
    private String updateuser;
*/

   /* public String toString() {
        Users user = DisCommonUtils.getCurrentUsers();
        this.sendUserId = super.getCreateuser();
        this.sendUserName = user.getUsername();
        this.sendDate = super.getCreatedate().replaceAll("-", "");
        this.sendTime = super.getCreatetime().replaceAll(":","");
    }*/
}
