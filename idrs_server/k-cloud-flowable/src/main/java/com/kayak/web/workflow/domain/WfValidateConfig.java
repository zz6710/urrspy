package com.kayak.web.workflow.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 回调返回参数校验配置对象 flow_validate_config
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Data
@TableName("flow_validate_config")
public class WfValidateConfig {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private String id;
    /**
     * 校验名称
     */
    private String name;
    /**
     * 校验规则
     */
    private String rule;
    /**
     * 备注
     */
    private String remark;

}
