package com.kayak.rpt.dataMerge.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "customerDataMergeService",table = "base_account_merge_order")
public class CustomerDataMergeModel {

    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;

    @GraphQLField(label = "数据日期(起始)" ,field = "cstm_dt_f")
    private String cstmDtF;

    @GraphQLField(label = "数据日期(结束)" ,field = "cstm_dt_e")
    private String cstmDtE;

    @GraphQLField(label = "客户识别标识(从)" ,field = "cstm_acc_f")
    private String cstmAccF;

    @GraphQLField(label = "客户识别标识(到)" ,field = "cstm_acc_t")
    private String cstmAccT;

    @GraphQLField(label = "操作日期" ,field = "opt_dt")
    private String optDt;

    @GraphQLField(label = "操作时间" ,field = "opt_tm")
    private String optTm;

    @GraphQLField(label = "操作员编号" ,field = "opt_user_id")
    private String optUserId;

    @GraphQLField(label = "操作员名称" ,field = "opt_user_nm")
    private String optUserNm;

    @GraphQLField(label = "合并状态" ,field = "mrg_sts")
    private String mrgSts;

}
