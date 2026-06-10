package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "opFormParamRelationService",table = "op_form_param_relation")
public class OpFormParamRelation {

    @GraphQLField(label = "表单id", field = "form_id")
    private String formId;

    @GraphQLField(label = "参数代码", field = "param_code")
    private String paramCode;

    @GraphQLField(label = "取值情况",  field = "param_value")
    private String paramValue;

    @GraphQLField(label = "关联字段代码",  field = "link_param_code")
    private String linkParamCode;

    @GraphQLField(label = "显示标识",  field = "show_flag")
    private String showFlag;

    @GraphQLField(label = "非空标识",  field = "blank_flag")
    private String blankFlag;

    @GraphQLField(label = "编辑标识", field = "edit_flag")
    private String editFlag;

    @GraphQLField(label = "参数字段数据字典", field = "dict")
    private String dict;

    @GraphQLField(label = "默认值", field = "default_value")
    private String defaultValue;
}
