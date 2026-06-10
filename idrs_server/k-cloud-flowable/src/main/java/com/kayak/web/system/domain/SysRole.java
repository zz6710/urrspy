package com.kayak.web.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-07 16:56
 **/
@Data
@TableName("sys_role")
public class SysRole {
    @TableId(value = "roleid")
    private String roleid;

    private String rolename;
}
