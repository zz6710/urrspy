package com.kayak.web.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_param")
public class SystemParam {
    @TableId(value = "paraid")
    private String paraid;

    private String paravalue;

    private String paraname;

}
