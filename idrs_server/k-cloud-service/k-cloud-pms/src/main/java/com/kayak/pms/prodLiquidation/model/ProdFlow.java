package com.kayak.pms.prodLiquidation.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

import java.util.List;

/**
 * @author shexianyu
 * @date 2022/9/28 14:56
 * @desc
 */
@Data
@GraphQLModel(fetcher="prodFlowService",table="t8_prod_flow")
public class ProdFlow {

    @GraphQLField(label = "产品代码", sql = "prod_code = $S{prodCode}", field = "prod_code")
    private String prodCode;

    @GraphQLField(label = "流程状态", sql = "process_status = $S{processStatus}", field = "process_status")
    private String processStatus;

    @GraphQLField(label = "操作流程id", sql = "op_process_id = $S{opProcessId}", field = "op_process_id")
    private String opProcessId;

    @GraphQLField(label = "流程类型", sql = "type = $S{type}", field = "type")
    private String type;

    @GraphQLField(label = "创建者", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
    private String crtUser;
    @GraphQLField(label = "创建时间", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(label = "创建者", sql = "upd_user = $S{updUser}" ,field = "upd_user")
    private String updUser;
    @GraphQLField(label = "创建时间", sql = "upd_date = $S{updDate}" ,field = "upd_date")
    private String updDate;

    @GraphQLField(label = "参数下发状态", sql = "param_status = $S{paramStatus}" ,field = "param_status")
    private String paramStatus;

    @GraphQLField
    private String prodName;
    @GraphQLField
    private String prodStatus;
    @GraphQLField
    private String establishDate;
    @GraphQLField
    private String processDeadline;
    @GraphQLField
    private String endDate;
    @GraphQLField
    private String realEndDate;
    @GraphQLField
    private String prodSeries;
    @GraphQLField
    private String seriesName;

    @GraphQLField
    private List<ProdFlow> prodFlowList;

    //是否产品经理
    private String prodManager;

    @GraphQLField
    private String prodManagerId;
    @GraphQLField
    private String prodManagerName;
    @GraphQLField
    private String prodSonStatus;
    @GraphQLField
    private String creator;
    @GraphQLField
    private String processInstanceId;


}
