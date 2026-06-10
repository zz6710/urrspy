package com.kayak.common.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-10-12 14:46
 **/
@Data
@NoArgsConstructor
public class Execution implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 流程实例id
     */
    String processInstanceId;
    /**
     * 流程定义id
     */
    String processDefinitionId;
    /**
     * 流程key
     */
    String processKey;
    /**
     * 流程状态
     */
    String processStatus;
    /**
     * 流程变量(包括申请表单提交的参数)
     */
    Map<String, Object> variables;
    /**
     * 扩展属性
     */
    Map<String, Object> extensionProperty;
    /**
     * 注入字段
     */
    Map<String, String> fieldExtension;
    /**
     * url查询的结果，key为参数名，value为url的结果
     */
    Map<String,Object> urlData;

}
