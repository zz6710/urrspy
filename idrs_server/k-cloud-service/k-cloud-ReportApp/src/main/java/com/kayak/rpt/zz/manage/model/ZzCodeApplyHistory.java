package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "zzCodeApplyHistoryService",table = "zz_code_apply_history")
public class ZzCodeApplyHistory {
    @GraphQLField(kkhtml = "KFieldText", label = "id", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "产品登记编码", sql = "prod_reg_enc = $S{prodRegEnc}" ,field = "prod_reg_enc")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "行内标识码", sql = "inner_code  =$S{innerCode}" ,field = "inner_code")
    private String innerCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
    private String prodNm;
    @GraphQLField(kkhtml = "KFieldText", label = "登记文件全路径", sql = "direct_zip_dir = $S{directZipDir}" ,field = "direct_zip_dir")
    private String directZipDir;
    @GraphQLField(kkhtml = "KFieldText", label = "登记文件名称", sql = "direct_zip_nm = $S{directZipNm}" ,field = "direct_zip_nm")
    private String directZipNm;
    @GraphQLField(kkhtml = "KFieldText", label = "审核结果", sql = "chek_result = $S{chekResult}" ,field = "chek_result")
    private String chekResult;
    @GraphQLField(kkhtml = "KFieldText", label = "审核意见", sql = "chek_opinion = $S{chekOpinion}" ,field = "chek_opinion")
    private String chekOpinion;
    @GraphQLField(kkhtml = "KFieldText", label = "交易流水号", sql = "serial_no = $S{serialNo}" ,field = "serial_no")
    private String serialNo;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "created_time = $S{createdTime}" ,field = "created_time")
    private String createdTime;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;

}
