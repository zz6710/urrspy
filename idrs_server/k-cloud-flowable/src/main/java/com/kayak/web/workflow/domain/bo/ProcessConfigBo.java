package com.kayak.web.workflow.domain.bo;

import com.kayak.web.workflow.domain.WfEnv;
import com.kayak.web.workflow.domain.WfEnvItem;
import com.kayak.web.workflow.domain.WfFormField;
import lombok.Data;

import java.util.List;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-12-14 16:09
 **/
@Data
public class ProcessConfigBo {
    /**
     * 上下文项
     */
    List<WfEnvItem> envItems;
    /**
     * 上下文
     */
    WfEnv env;
    /**
     * 表单字段
     */
    List<WfFormField> formFields;
}
