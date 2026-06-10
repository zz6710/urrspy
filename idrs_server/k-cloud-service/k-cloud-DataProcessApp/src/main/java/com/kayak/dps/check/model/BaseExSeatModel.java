package com.kayak.dps.check.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "baseExSeatService",table = "base_ex_seat")
public class BaseExSeatModel {
    @GraphQLField(key = true , label = "id" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", sql = "fcode = $S{fcode}", label = "源机构代码" ,field = "fcode")
    private String fcode;
    @GraphQLField(label = "包id" ,field = "extpid")
    private String extpid;
    @GraphQLField(label = "包模式" ,field = "exmode")
    private String exmode;
    @GraphQLField(label = "包文件名模式" ,field = "fnmfmt")
    private String fnmfmt;
    @GraphQLField(label = "是否需要包头信息" ,field = "oheader")
    private String oheader;
    @GraphQLField(label = "是否需要字段名称" ,field = "oitmnm")
    private String oitmnm;
    @GraphQLField(label = "是否需要字段名称定长" ,field = "oitmnmfl")
    private String oitmnmfl;
    @GraphQLField(label = "是否需要记录总数" ,field = "oreccnt")
    private String oreccnt;
    @GraphQLField(label = "是否需要分割符号" ,field = "osymbol")
    private String osymbol;
    @GraphQLField(label = "全量导入" ,field = "oflddef")
    private String oflddef;
    @GraphQLField(label = "对应数据表名" ,field = "extab")
    private String extab;
    @GraphQLField(label = "包格式代码" , sql = "exfmtid = $S{exfmtid}",field = "exfmtid")
    private String exfmtid;
    @GraphQLField(label = "索引文件名称模式" ,field = "indexfile")
    private String indexfile;
    @GraphQLField(label = "接口方向" ,field = "tcode")
    private String tcode;

}
