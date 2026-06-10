package com.kayak.dps.outLands.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "outLandsRaiseService", table = "min_ast_outlands_raise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutLandsRaise {
    //id
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    //产品代码
    @GraphQLField(label = "产品代码", sql = "PROD_CD = $S{prodCd}", field = "PROD_CD")
    private String prodCd;
    //产品名称
    @GraphQLField(label = "产品名称", sql = "PROD_NM = $S{prodNm}", field = "PROD_NM")
    private String prodNm;
    //境外客户募集余额
    @GraphQLField(label = "境外客户募集余额", sql = "CLC_BAL = $S{clcBal}", field = "CLC_BAL")
    private String clcBal;
    //数据日期
    @GraphQLField(label = "数据日期", sql = "DATA_DATE = $S{dataDate}", field = "DATA_DATE")
    private String dataDate;
    //创建日期
    @GraphQLField(label = "创建日期", sql = "CRT_DATE = $S{crtDate}", field = "CRT_DATE")
    private String crtDate;
    @GraphQLField(label = "创建时间", sql = "CRT_TIME = $S{crtTime}", field = "CRT_TIME")
    private String crtTime;

    @GraphQLField(label = "数据日期起始日")
    private String dataDateStart ;
    @GraphQLField(label = "数据日期结束日")
    private String dataDateEnd ;


}
