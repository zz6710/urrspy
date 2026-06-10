package com.kayak.rpt.email.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发送邮件的 账户信息 表
 */
@Data
public class EmailAccount {
    @ApiModelProperty(name="id", value="主键id")
    private String id;
    @ApiModelProperty(name="account",  value="邮箱账号")
    private String account;
    @ApiModelProperty(name="password",  value="邮箱密码")
    private String password;
    @ApiModelProperty(name="emailAccountName",  value="邮箱名称")
    private String emailAccountName;
    @ApiModelProperty(name="limitSize",  value="邮箱限制大小")
    private String limitSize;
    @ApiModelProperty(name="effectFlag",  value="状态：参考DictCodeList枚举")
    private String effectFlag;
    @ApiModelProperty(name="createUser",  value="创建人")
    private String createUser;
    @ApiModelProperty(name="createTime",  value="创建时间")
    private String createTime;
    @ApiModelProperty(name="createDate",  value="创建日期")
    private String createDate;
    @ApiModelProperty(name="updateUser",  value="更新人")
    private String updateUser;
    @ApiModelProperty(name="updateTime",  value="更新时间")
    private String updateTime;
    @ApiModelProperty(name="updateDate",  value="更新日期")
    private String updateDate;
}
