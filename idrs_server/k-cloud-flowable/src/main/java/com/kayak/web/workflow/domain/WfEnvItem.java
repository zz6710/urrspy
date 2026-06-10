package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 上下文项配置对象 flow_env_item
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_env_item")
public class WfEnvItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上下文项id
     */
    @TableId(value = "env_item_id")
    private Long envItemId;
    /**
     * 上下文id
     */
    private Long envId;
    /**
     * 键
     */
    private String itemKey;
    /**
     * 值
     */
    private String itemValue;

    /**
     * 类型
     */
    private String itemType;

}
