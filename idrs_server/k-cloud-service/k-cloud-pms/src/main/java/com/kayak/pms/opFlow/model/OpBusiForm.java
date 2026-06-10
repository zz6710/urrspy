package com.kayak.pms.opFlow.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

@Data
@GraphQLModel(fetcher = "opBusiFormService", table = "op_busi_form")
public class OpBusiForm {

    @GraphQLField(label = "功能id", sql = "busi_id = $S{busiId}", field = "busi_id")
    private String busiId;

    @GraphQLField(label = "表单ID", sql = "form_id = $S{formId}", field = "form_id")
    private String formId;

    @GraphQLField(label = "表单排序", sql = "order_no = $S{orderNo}", field = "order_no")
    private String orderNo;

    @GraphQLField
    private List<OpBusiForm> list;
}
