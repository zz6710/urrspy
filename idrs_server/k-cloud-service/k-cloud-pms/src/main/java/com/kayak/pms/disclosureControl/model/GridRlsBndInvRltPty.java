package com.kayak.pms.disclosureControl.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "gridRlsBndInvRltPtyService", table = "app_grid_rls_bnd_inv_rlt_pty_data")
public class GridRlsBndInvRltPty {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "公告版本id", sql = "notice_version_id = $S{noticeVersionId}" ,field = "notice_version_id")
    private String noticeVersionId;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "deal_dt = $S{dealDt}" ,field = "deal_dt")
    private String dealDt;
    @GraphQLField(kkhtml = "KFieldText", label = "序号", sql = "order_no = $S{orderNo}" ,field = "order_no")
    private String orderNo;
    @GraphQLField(kkhtml = "KFieldText", label = "关联方名称", sql = "affiliate_name LIKE '%$U{affiliateName}%'" ,field = "affiliate_name")
    private String affiliateName;
    @GraphQLField(kkhtml = "KFieldText", label = "余额(万元)", sql = "securities_code = $S{securitiesCode}" ,field = "securities_code")
    private String securitiesCode;
    @GraphQLField(kkhtml = "KFieldText", label = "证券简称", sql = "securities_name  LIKE '%$U{securitiesName}%'" ,field = "securities_name")
    private String securitiesName;
    @GraphQLField(kkhtml = "KFieldText", label = "交易金额（单位：元）", sql = "deal_amount = $S{dealAmount}" ,field = "deal_amount")
    private String dealAmount;
    @GraphQLField(kkhtml = "KFieldText", label = "发行方关联方式", sql = "party_relation = $S{partyRelation}" ,field = "party_relation")
    private String partyRelation;
}