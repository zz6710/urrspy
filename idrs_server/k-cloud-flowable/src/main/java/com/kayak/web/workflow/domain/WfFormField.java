package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 单字段配置对象 flow_form_field
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("flow_form_field")
public class WfFormField extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 字段id
     */
    @TableId(value = "form_field_id")
    private Long formFieldId;
    /**
     * 字段英文名称
     */
    private String name;
    /**
     * 字段中文名称
     */
    private String displayName;
    /**
     * 表单类型
     */
    private String formType;
    /**
     * 字段配置
     */
    private String json;

}
