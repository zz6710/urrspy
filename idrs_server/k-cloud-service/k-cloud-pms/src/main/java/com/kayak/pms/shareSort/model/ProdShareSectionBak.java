package com.kayak.pms.shareSort.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "prodShareSectionBakService", table = "t8_prod_share_section_bak")
public class ProdShareSectionBak {
    @GraphQLField(key = true, kkhtml = "KFieldText", label = "", sql = "id = $S{id}", field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品份额分类表id", sql = "t8_prod_share_sort_id = $S{t8ProdShareSortId}", field = "t8_prod_share_sort_id")
    private String t8ProdShareSortId;
    @GraphQLField(kkhtml = "KFieldText", label = "产品id", sql = "t8_prod_info_id = $S{t8ProdInfoId}", field = "t8_prod_info_id")
    private String t8ProdInfoId;
    @GraphQLField(kkhtml = "KFieldText", label = "分段小值区间", sql = "dimension1_min = $S{dimension1Min}", field = "dimension1_min")
    private String dimension1Min;
    @GraphQLField(kkhtml = "KFieldText", label = "分段大值区间", sql = "dimension1_max = $S{dimension1Max}", field = "dimension1_max")
    private String dimension1Max;
    @GraphQLField(kkhtml = "KFieldText", label = "分段计提比例", sql = "rate_accrual = $S{rateAccrual}", field = "rate_accrual")
    private String rateAccrual;

}