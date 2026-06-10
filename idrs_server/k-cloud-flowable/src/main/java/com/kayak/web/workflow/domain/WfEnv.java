package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上下文配置对象 flow_env
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_env")
public class WfEnv extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 上下文id
     */
    @TableId(value = "env_id")
    private Long envId;
    /**
     * 上下文英文名称
     */
    private String name;
    /**
     * 上下文中文名称
     */
    private String displayName;

}
