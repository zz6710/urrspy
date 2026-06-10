package com.kayak.dps.valtabimp.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "odsReadAssetsReportService")
public class OdsReadAssetsReport {

    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;

    @GraphQLField
    private String assetCode;
    @GraphQLField
    private String t8ValReporttabId;
    @GraphQLField
    private String note;
    @GraphQLField(kkhtml = "KFieldText", label = "录入柜员", sql = "inputuser = $S{inputuser}" ,field = "inputuser")
    private String inputuser;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_date = $S{crtDate}" ,field = "crt_date")
    private String crtDate;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;

    @GraphQLField(kkhtml = "KFieldText", label = "是否产品或者资产1", sql = "isprodorasset = $S{isprodorasset}" ,field = "isprodorasset")
    private String isprodorasset;
    @GraphQLField
    private String isprodorassetname;
    @GraphQLField
    private String reporttabName;
}
