package com.kayak.web.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_user")
public class SysUser {
    @TableId(value = "userid")
    private String userid;

    private String username;

    private String orgno;
}
