package com.kayak.dps.app.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assetCodeManageService", table = "base_asset_code_management")
public class AssetCodeManageModel {
    @GraphQLField(kkhtml="KFieldText", label = "ID")
    private String id;
    @GraphQLField(kkhtml="KFieldText", label = "新资产代码")
    private String scrCd;
    @GraphQLField(kkhtml="KFieldText", label = "旧资产代码")
    private String oldScrCd;
    @GraphQLField(kkhtml="KFieldText", label = "资产名称")
    private String scrNm;
    @GraphQLField(kkhtml="KFieldText", label = "市场")
    private String trxMkt;
    @GraphQLField(kkhtml="KFieldText", label = "资产分类")
    private String assetType;
    @GraphQLField(kkhtml="KFieldText", label = "生效状态")
    private String status;
    @GraphQLField(kkhtml="KFieldText", label = "数据来源")
    private String dataSource;
    @GraphQLField(kkhtml="KFieldText", label = "生效日期")
    private String effectiveDate;
    @GraphQLField(kkhtml="KFieldText", label = "生效时间")
    private String effectiveTime;
    @GraphQLField(kkhtml="KFieldText", label = "失效日期")
    private String expirationDate;
    @GraphQLField(kkhtml="KFieldText", label = "失效时间")
    private String expirationTime;
    @GraphQLField(kkhtml="KFieldText", label = "修改日期")
    private String updDate;
    @GraphQLField(kkhtml="KFieldText", label = "修改时间")
    private String updTime;
    @GraphQLField(kkhtml="KFieldText", label = "修改人")
    private String updUser;
}
