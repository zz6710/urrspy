package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "opFormInfoService",table = "op_form_info")
public class OpFormInfo {
    @GraphQLField(label = "表单ID", sql = "form_id = $S{formId}", field = "form_id")
    private String formId;

    @GraphQLField(label = "表单名称", sql = "form_name like '%$U{formName}%'", field = "form_name", kkhtml = "KFieldText")
    private String formName;

    @GraphQLField(label = "表单类型", sql = "form_type = $S{formType}", field = "form_type", kkhtml = "KFieldSelect", kkhtmlExt = "{\"data-dict\":\"op_form_type\"}")
    private String formType;

    @GraphQLField(label = "组件路径", sql = "comp_path = $S{compPath}", field = "comp_path")
    private String compPath;

    @GraphQLField
    private List<String> formIdList;

    private List<OpFormParam> params;
}
