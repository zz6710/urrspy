package com.kayak.dps.app.model;


import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "assT8OrgSheetService", table = "MID_ASS_PTY_ORG_BAS_INF")
public class AssT8OrgSheetModel {
    @GraphQLField(kkhtml = "KFieldText", label = "机构编码", sql = "ORG_NBR = $S{orgNbr}" ,field = "ORG_NBR")
    private String orgNbr;
    @GraphQLField(kkhtml = "KFieldText", label = "机构种类", sql = "ORG_TYP = $S{orgTyp}" ,field = "ORG_TYP")
    private String orgTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "同业机构类型", sql = "SAM_BUS_ORG_TYP = $S{samBusOrgTyp}" ,field = "SAM_BUS_ORG_TYP")
    private String samBusOrgTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
}
