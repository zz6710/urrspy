package com.kayak.web.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_org")
public class SysOrg {
    @TableId(value = "orgno")
    private String orgno;

    private String orgname;

    /**
     * 上级机构ID，第一级机构的上级机构为ROOT
     */
    private String parentorgno;

}
