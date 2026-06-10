package com.kayak.pms.shareSort.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodShareRationBakService", table = "t8_prod_share_ratio_bak")
public class ProdShareRationBak {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品份额分类表id", sql = "t8_prod_share_sort_id = $S{t8ProdShareSortId}", field = "t8_prod_share_sort_id")
    private String t8ProdShareSortId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "指数", sql = "ratio_index = $S{ratioIndex}", field = "ratio_index")
    private String ratioIndex;
    @GraphQLField(kkhtml = "KFieldText", label = "系数", sql = "coefficient = $S{coefficient}", field = "coefficient")
    private String coefficient;
    @GraphQLField(kkhtml = "KFieldText", label = "操作符", sql = "operator = $S{operator}" ,field = "operator")
    private String operator;
    @GraphQLField(kkhtml = "KFieldText", label = "指数类型", sql = "ratio_type = $S{ratioType}" ,field = "ratio_type")
    private String ratioType;
    @GraphQLField(kkhtml = "KFieldText", label = "固定值", sql = "fixed_value = $S{fixedValue}" ,field = "fixed_value")
    private String fixedValue;
    @GraphQLField(kkhtml = "KFieldText", label = "市场利率", sql = "market_rate = $S{marketRate}" ,field = "market_rate")
    private String marketRate;

}