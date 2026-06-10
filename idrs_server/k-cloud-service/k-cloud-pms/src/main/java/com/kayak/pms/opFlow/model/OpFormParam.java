package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "opFormParamService",table = "op_form_param")
public class OpFormParam {
    @GraphQLField(label = "表单ID", field = "form_id")
    private String formId;

    @GraphQLField(label = "参数代码", field = "param_code")
    private String paramCode;

    @GraphQLField(label = "参数名称", field = "form_type")
    private String paramName;

    @GraphQLField(label = "输入类型", field = "func_type")
    private String funcType;

    @GraphQLField(label = "字段长度", field = "fieldL_length")
    private String fieldLength;

    @GraphQLField(label = "字段精度", field = "field_precision")
    private String fieldPrecision;

    @GraphQLField(label = "显示标识", field = "show_flag")
    private String showFlag;

    @GraphQLField(label = "必填标识", field = "blank_flag")
    private String blankFlag;

    @GraphQLField(label = "编辑标识", field = "edit_flag")
    private String editFlag;

    @GraphQLField(label = "最大值", field = "max_value")
    private String maxValue;

    @GraphQLField(label = "最小值", field = "min_value")
    private String minValue;

    @GraphQLField(label = "默认值", field = "default_value")
    private String defaultValue;

    @GraphQLField(label = "选项来源", field = "data_way")
    private String dataWay;

    @GraphQLField(label = "数据字典或接口", field = "dict")
    private String dict;

    @GraphQLField(label = "空白提示", field = "placeholder")
    private String placeholder;

    @GraphQLField(label = "排序", field = "order_no")
    private Integer orderNo;

    @GraphQLField
    private List<OpFormParam> params;

    @GraphQLField
    private List<OpFormParamRelation> relations;

    @GraphQLField
    private String processInstanceId;

    private String fieldValue;
}
