package com.kayak.pms.prod.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;


@Data
@GraphQLModel(fetcher = "t8ProdAccountFindInfoService",table = "T8_PROD_INFO")
public class T8ProdAccountFindInfo {
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "t.id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldSelect",label = "产品代码", sql = "t.prod_code IN ($S{prodCode})" ,field = "prod_code", kkhtmlExt="{\"data-action\":\"T8ProdInfo.findT8ProdInfos\",\"data-display-field\":\"prodCode,prodName\",\"data-value-field\":\"prodCode\"}")
    private String prodCode;
    @GraphQLField(kkhtml = "KFieldText",label = "产品名称", sql = "prod_name like '%$U{prodName}%'" ,field = "prod_name",kkhtmlDefault = true)
    private String prodName;
    @GraphQLField(kkhtml = "KFieldText", label = "募集方式", sql = "raise_type = $S{raiseType}" ,field = "raise_type")
    private String raiseType;
    @GraphQLField(kkhtml = "KFieldText", label = "产品模型", sql = "prod_mode = $S{prodMode}" ,field = "prod_mode")
    private String prodMode;
    @GraphQLField(kkhtml = "KFieldText", label = "生命周期状态", sql = "prod_status = $S{prodStatus}" ,field = "prod_status")
    private String prodStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "子状态", sql = "prod_son_status = $S{prodSonStatus}" ,field = "prod_son_status")
    private String prodSonStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "是否代码回收", sql = "is_recycle_code = $S{isRecycleCode}" ,field = "is_recycle_code")
    private String isRecycleCode;

    @GraphQLField(kkhtml = "KFieldText", label = "账户类型", sql = "account_type = $S{accountType}" ,field = "account_type")
    private String accountType;


    @GraphQLField(kkhtml = "KFieldText", label = "是否账户维护", sql = "is_maintain_account = $S{isMaintainAccount}" ,field = "is_maintain_account")
    private String isMaintainAccount;

}
