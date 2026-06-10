package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//资产负债剩余期限信息
@Data
@GraphQLModel(fetcher = "ZG09Service",table = "app_pbc_report_zg09")
public class ZG09 {

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;

    @ExcelProperty(value = "发行机构代码")
    @GraphQLField(kkhtml = "KFieldText", label = "发行机构代码", sql = "isu_org_cd like '%$U{isuOrgCd}%'" ,field = "isu_org_cd")
    private String isuOrgCd;

    @ExcelProperty(value = "数据日期")
    @GraphQLField(kkhtml = "KFieldText", label = "实际报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;

    @ExcelIgnore
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;

    @ExcelProperty(value = "产品品种_资管")
    @GraphQLField(kkhtml = "KFieldText", label = "产品品种", sql = "prod_cate = $S{prodCate}" ,field = "prod_cate")
    private String prodCate;

    @ExcelProperty(value = "G000A_资产方-债务工具-总计")
    @GraphQLField(kkhtml = "KFieldText", label = "G000A_资产方-债务工具-总计", sql = "g000a = $S{g000a}" ,field = "g000a")
    private String g000a;
    @ExcelProperty(value = "G000B_资产方-股权-总计")
    @GraphQLField(kkhtml = "KFieldText", label = "G000B_资产方-股权-总计", sql = "g000b = $S{g000b}" ,field = "g000b")
    private String g000b;
    @ExcelProperty(value = "G000C_资产方-特定目的载体份额-总计")
    @GraphQLField(kkhtml = "KFieldText", label = "G000C_资产方-特定目的载体份额-总计", sql = "g000c = $S{g000c}" ,field = "g000c")
    private String g000c;
    @ExcelProperty(value = "G000D_负债及权益方-债务工具-总计")
    @GraphQLField(kkhtml = "KFieldText", label = "G000D_负债及权益方-债务工具-总计", sql = "g000d = $S{g000d}" ,field = "g000d")
    private String g000d;
    @ExcelProperty(value = "G000E_负债及权益方-实收本金-总计")
    @GraphQLField(kkhtml = "KFieldText", label = "G000E_负债及权益方-实收本金-总计", sql = "g000e = $S{g000e}" ,field = "g000e")
    private String g000e;
    @ExcelProperty(value = "G100A_资产方-债务工具-1个月（含）以下")
    @GraphQLField(kkhtml = "KFieldText", label = "G100A_资产方-债务工具-1个月（含）以下", sql = "g100a = $S{g100a}" ,field = "g100a")
    private String g100a;
    @ExcelProperty(value = "G100B_资产方-股权-1个月（含）以下")
    @GraphQLField(kkhtml = "KFieldText", label = "G100B_资产方-股权-1个月（含）以下", sql = "g100b = $S{g100b}" ,field = "g100b")
    private String g100b;
    @ExcelProperty(value = "G100C_资产方-特定目的载体份额-1个月（含）以下")
    @GraphQLField(kkhtml = "KFieldText", label = "G100C_资产方-特定目的载体份额-1个月（含）以下", sql = "g100c = $S{g100c}" ,field = "g100c")
    private String g100c;
    @ExcelProperty(value = "G100D_负债及权益方-债务工具-1个月（含）以下")
    @GraphQLField(kkhtml = "KFieldText", label = "G100D_负债及权益方-债务工具-1个月（含）以下", sql = "g100d = $S{g100d}" ,field = "g100d")
    private String g100d;
    @ExcelProperty(value = "G100E_负债及权益方-实收本金-1个月（含）以下")
    @GraphQLField(kkhtml = "KFieldText", label = "G100E_负债及权益方-实收本金-1个月（含）以下", sql = "g100e = $S{g100e}" ,field = "g100e")
    private String g100e;
    @ExcelProperty(value = "G200A_资产方-债务工具-1-3个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G200A_资产方-债务工具-1-3个月（含）", sql = "g200a = $S{g200a}" ,field = "g200a")
    private String g200a;
    @ExcelProperty(value = "G200B_资产方-股权-1-3个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G200B_资产方-股权-1-3个月（含）", sql = "g200b = $S{g200b}" ,field = "g200b")
    private String g200b;
    @ExcelProperty(value = "G200C_资产方-特定目的载体份额-1-3个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G200C_资产方-特定目的载体份额-1-3个月（含）", sql = "g200c = $S{g200c}" ,field = "g200c")
    private String g200c;
    @ExcelProperty(value = "G200D_负债及权益方-债务工具-1-3个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G200D_负债及权益方-债务工具-1-3个月（含）", sql = "g200d = $S{g200d}" ,field = "g200d")
    private String g200d;
    @ExcelProperty(value = "G200E_负债及权益方-实收本金-1-3个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G200E_负债及权益方-实收本金-1-3个月（含）", sql = "g200e = $S{g200e}" ,field = "g200e")
    private String g200e;
    @ExcelProperty(value = "G300A_资产方-债务工具-3-6个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G300A_资产方-债务工具-3-6个月（含）", sql = "g300a = $S{g300a}" ,field = "g300a")
    private String g300a;
    @ExcelProperty(value = "G300B_资产方-股权-3-6个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G300B_资产方-股权-3-6个月（含）", sql = "g300b = $S{g300b}" ,field = "g300b")
    private String g300b;
    @ExcelProperty(value = "G300C_资产方-特定目的载体份额-3-6个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G300C_资产方-特定目的载体份额-3-6个月（含）", sql = "g300c = $S{g300c}" ,field = "g300c")
    private String g300c;
    @ExcelProperty(value = "G300D_负债及权益方-债务工具-3-6个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G300D_负债及权益方-债务工具-3-6个月（含）", sql = "g300d = $S{g300d}" ,field = "g300d")
    private String g300d;
    @ExcelProperty(value = "G300E_负债及权益方-实收本金-3-6个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G300E_负债及权益方-实收本金-3-6个月（含）", sql = "g300e = $S{g300e}" ,field = "g300e")
    private String g300e;
    @ExcelProperty(value = "G400A_资产方-债务工具-6-12个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G400A_资产方-债务工具-6-12个月（含）", sql = "g400a = $S{g400a}" ,field = "g400a")
    private String g400a;
    @ExcelProperty(value = "G400B_资产方-股权-6-12个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G400B_资产方-股权-6-12个月（含）", sql = "g400b = $S{g400b}" ,field = "g400b")
    private String g400b;
    @ExcelProperty(value = "G400C_资产方-特定目的载体份额-6-12个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G400C_资产方-特定目的载体份额-6-12个月（含）", sql = "g400c = $S{g400c}" ,field = "g400c")
    private String g400c;
    @ExcelProperty(value = "G400D_负债及权益方-债务工具-6-12个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G400D_负债及权益方-债务工具-6-12个月（含）", sql = "g400d = $S{g400d}" ,field = "g400d")
    private String g400d;
    @ExcelProperty(value = "G400E_负债及权益方-实收本金-6-12个月（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G400E_负债及权益方-实收本金-6-12个月（含）", sql = "g400e = $S{g400e}" ,field = "g400e")
    private String g400e;
    @ExcelProperty(value = "G500A_资产方-债务工具-1-2年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G500A_资产方-债务工具-1-2年（含）", sql = "g500a = $S{g500a}" ,field = "g500a")
    private String g500a;
    @ExcelProperty(value = "G500B_资产方-股权-1-2年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G500B_资产方-股权-1-2年（含）", sql = "g500b = $S{g500b}" ,field = "g500b")
    private String g500b;
    @ExcelProperty(value = "G500C_资产方-特定目的载体份额-1-2年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G500C_资产方-特定目的载体份额-1-2年（含）", sql = "g500c = $S{g500c}" ,field = "g500c")
    private String g500c;
    @ExcelProperty(value = "G500D_负债及权益方-债务工具-1-2年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G500D_负债及权益方-债务工具-1-2年（含）", sql = "g500d = $S{g500d}" ,field = "g500d")
    private String g500d;
    @ExcelProperty(value = "G500E_负债及权益方-实收本金-1-2年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G500E_负债及权益方-实收本金-1-2年（含）", sql = "g500e = $S{g500e}" ,field = "g500e")
    private String g500e;
    @ExcelProperty(value = "G600A_资产方-债务工具-2-3年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G600A_资产方-债务工具-2-3年（含）", sql = "g600a = $S{g600a}" ,field = "g600a")
    private String g600a;
    @ExcelProperty(value = "G600B_资产方-股权-2-3年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G600B_资产方-股权-2-3年（含）", sql = "g600b = $S{g600b}" ,field = "g600b")
    private String g600b;
    @ExcelProperty(value = "G600C_资产方-特定目的载体份额-2-3年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G600C_资产方-特定目的载体份额-2-3年（含）", sql = "g600c = $S{g600c}" ,field = "g600c")
    private String g600c;
    @ExcelProperty(value = "G600D_负债及权益方-债务工具-2-3年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G600D_负债及权益方-债务工具-2-3年（含）", sql = "g600d = $S{g600d}" ,field = "g600d")
    private String g600d;
    @ExcelProperty(value = "G600E_负债及权益方-实收本金-2-3年（含）")
    @GraphQLField(kkhtml = "KFieldText", label = "G600E_负债及权益方-实收本金-2-3年（含）", sql = "g600e = $S{g600e}" ,field = "g600e")
    private String g600e;
    @ExcelProperty(value = "G700A_资产方-债务工具-3年以上")
    @GraphQLField(kkhtml = "KFieldText", label = "G700A_资产方-债务工具-3年以上", sql = "g700a = $S{g700a}" ,field = "g700a")
    private String g700a;
    @ExcelProperty(value = "G700B_资产方-股权-3年以上")
    @GraphQLField(kkhtml = "KFieldText", label = "G700B_资产方-股权-3年以上", sql = "g700b = $S{g700b}" ,field = "g700b")
    private String g700b;
    @ExcelProperty(value = "G700C_资产方-特定目的载体份额-3年以上")
    @GraphQLField(kkhtml = "KFieldText", label = "G700C_资产方-特定目的载体份额-3年以上", sql = "g700c = $S{g700c}" ,field = "g700c")
    private String g700c;
    @ExcelProperty(value = "G700D_负债及权益方-债务工具-3年以上")
    @GraphQLField(kkhtml = "KFieldText", label = "G700D_负债及权益方-债务工具-3年以上", sql = "g700d = $S{g700d}" ,field = "g700d")
    private String g700d;
    @ExcelProperty(value = "G700E_负债及权益方-实收本金-3年以上")
    @GraphQLField(kkhtml = "KFieldText", label = "G700E_负债及权益方-实收本金-3年以上", sql = "g700e = $S{g700e}" ,field = "g700e")
    private String g700e;
    @ExcelProperty(value = "G800A_资产方-债务工具-无固定期限")
    @GraphQLField(kkhtml = "KFieldText", label = "G800A_资产方-债务工具-无固定期限", sql = "g800a = $S{g800a}" ,field = "g800a")
    private String g800a;
    @ExcelProperty(value = "G800B_资产方-股权-无固定期限")
    @GraphQLField(kkhtml = "KFieldText", label = "G800B_资产方-股权-无固定期限", sql = "g800b = $S{g800b}" ,field = "g800b")
    private String g800b;
    @ExcelProperty(value = "G800C_资产方-特定目的载体份额-无固定期限")
    @GraphQLField(kkhtml = "KFieldText", label = "G800C_资产方-特定目的载体份额-无固定期限", sql = "g800c = $S{g800c}" ,field = "g800c")
    private String g800c;
    @ExcelProperty(value = "G800D_负债及权益方-债务工具-无固定期限")
    @GraphQLField(kkhtml = "KFieldText", label = "G800D_负债及权益方-债务工具-无固定期限", sql = "g800d = $S{g800d}" ,field = "g800d")
    private String g800d;
    @ExcelProperty(value = "G800E_负债及权益方-实收本金-无固定期限")
    @GraphQLField(kkhtml = "KFieldText", label = "G800E_负债及权益方-实收本金-无固定期限", sql = "g800e = $S{g800e}" ,field = "g800e")
    private String g800e;
    @ExcelProperty(value = "00001_附报-金融机构表内资产余额（本外币）")
    @GraphQLField(kkhtml = "KFieldText", label = "00001_附报-金融机构表内资产余额（本外币）", sql = "g0001 = $S{g0001}" ,field = "g0001")
    private String g0001;
    @ExcelProperty(value = "00002_附报-金融机构表内金融资产余额（本外币）")
    @GraphQLField(kkhtml = "KFieldText", label = "00002_附报-金融机构表内金融资产余额（本外币）", sql = "g0002 = $S{g0002}" ,field = "g0002")
    private String g0002;
    @ExcelProperty(value = "信托产品口径")
    @GraphQLField(kkhtml = "KFieldText", label = "信托产品口径", sql = "trust_prod = $S{trustProd}" ,field = "trust_prod")
    private String trustProd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsuOrgCd() {
        return isuOrgCd;
    }

    public void setIsuOrgCd(String isuOrgCd) {
        this.isuOrgCd = isuOrgCd;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getTheoryReportStartDate() {
        return theoryReportStartDate;
    }

    public void setTheoryReportStartDate(String theoryReportStartDate) {
        this.theoryReportStartDate = theoryReportStartDate;
    }

    public String getTheoryReportEndDate() {
        return theoryReportEndDate;
    }

    public void setTheoryReportEndDate(String theoryReportEndDate) {
        this.theoryReportEndDate = theoryReportEndDate;
    }

    public String getRegisterStatus() {
        return registerStatus;
    }

    public void setRegisterStatus(String registerStatus) {
        this.registerStatus = registerStatus;
    }

    public String getProdCate() {
        return prodCate;
    }

    public void setProdCate(String prodCate) {
        this.prodCate = prodCate;
    }

    public String getG000a() {
        return g000a;
    }

    public void setG000a(String g000a) {
        this.g000a = g000a;
    }

    public String getG000b() {
        return g000b;
    }

    public void setG000b(String g000b) {
        this.g000b = g000b;
    }

    public String getG000c() {
        return g000c;
    }

    public void setG000c(String g000c) {
        this.g000c = g000c;
    }

    public String getG000d() {
        return g000d;
    }

    public void setG000d(String g000d) {
        this.g000d = g000d;
    }

    public String getG000e() {
        return g000e;
    }

    public void setG000e(String g000e) {
        this.g000e = g000e;
    }

    public String getG100a() {
        return g100a;
    }

    public void setG100a(String g100a) {
        this.g100a = g100a;
    }

    public String getG100b() {
        return g100b;
    }

    public void setG100b(String g100b) {
        this.g100b = g100b;
    }

    public String getG100c() {
        return g100c;
    }

    public void setG100c(String g100c) {
        this.g100c = g100c;
    }

    public String getG100d() {
        return g100d;
    }

    public void setG100d(String g100d) {
        this.g100d = g100d;
    }

    public String getG100e() {
        return g100e;
    }

    public void setG100e(String g100e) {
        this.g100e = g100e;
    }

    public String getG200a() {
        return g200a;
    }

    public void setG200a(String g200a) {
        this.g200a = g200a;
    }

    public String getG200b() {
        return g200b;
    }

    public void setG200b(String g200b) {
        this.g200b = g200b;
    }

    public String getG200c() {
        return g200c;
    }

    public void setG200c(String g200c) {
        this.g200c = g200c;
    }

    public String getG200d() {
        return g200d;
    }

    public void setG200d(String g200d) {
        this.g200d = g200d;
    }

    public String getG200e() {
        return g200e;
    }

    public void setG200e(String g200e) {
        this.g200e = g200e;
    }

    public String getG300a() {
        return g300a;
    }

    public void setG300a(String g300a) {
        this.g300a = g300a;
    }

    public String getG300b() {
        return g300b;
    }

    public void setG300b(String g300b) {
        this.g300b = g300b;
    }

    public String getG300c() {
        return g300c;
    }

    public void setG300c(String g300c) {
        this.g300c = g300c;
    }

    public String getG300d() {
        return g300d;
    }

    public void setG300d(String g300d) {
        this.g300d = g300d;
    }

    public String getG300e() {
        return g300e;
    }

    public void setG300e(String g300e) {
        this.g300e = g300e;
    }

    public String getG400a() {
        return g400a;
    }

    public void setG400a(String g400a) {
        this.g400a = g400a;
    }

    public String getG400b() {
        return g400b;
    }

    public void setG400b(String g400b) {
        this.g400b = g400b;
    }

    public String getG400c() {
        return g400c;
    }

    public void setG400c(String g400c) {
        this.g400c = g400c;
    }

    public String getG400d() {
        return g400d;
    }

    public void setG400d(String g400d) {
        this.g400d = g400d;
    }

    public String getG400e() {
        return g400e;
    }

    public void setG400e(String g400e) {
        this.g400e = g400e;
    }

    public String getG500a() {
        return g500a;
    }

    public void setG500a(String g500a) {
        this.g500a = g500a;
    }

    public String getG500b() {
        return g500b;
    }

    public void setG500b(String g500b) {
        this.g500b = g500b;
    }

    public String getG500c() {
        return g500c;
    }

    public void setG500c(String g500c) {
        this.g500c = g500c;
    }

    public String getG500d() {
        return g500d;
    }

    public void setG500d(String g500d) {
        this.g500d = g500d;
    }

    public String getG500e() {
        return g500e;
    }

    public void setG500e(String g500e) {
        this.g500e = g500e;
    }

    public String getG600a() {
        return g600a;
    }

    public void setG600a(String g600a) {
        this.g600a = g600a;
    }

    public String getG600b() {
        return g600b;
    }

    public void setG600b(String g600b) {
        this.g600b = g600b;
    }

    public String getG600c() {
        return g600c;
    }

    public void setG600c(String g600c) {
        this.g600c = g600c;
    }

    public String getG600d() {
        return g600d;
    }

    public void setG600d(String g600d) {
        this.g600d = g600d;
    }

    public String getG600e() {
        return g600e;
    }

    public void setG600e(String g600e) {
        this.g600e = g600e;
    }

    public String getG700a() {
        return g700a;
    }

    public void setG700a(String g700a) {
        this.g700a = g700a;
    }

    public String getG700b() {
        return g700b;
    }

    public void setG700b(String g700b) {
        this.g700b = g700b;
    }

    public String getG700c() {
        return g700c;
    }

    public void setG700c(String g700c) {
        this.g700c = g700c;
    }

    public String getG700d() {
        return g700d;
    }

    public void setG700d(String g700d) {
        this.g700d = g700d;
    }

    public String getG700e() {
        return g700e;
    }

    public void setG700e(String g700e) {
        this.g700e = g700e;
    }

    public String getG800a() {
        return g800a;
    }

    public void setG800a(String g800a) {
        this.g800a = g800a;
    }

    public String getG800b() {
        return g800b;
    }

    public void setG800b(String g800b) {
        this.g800b = g800b;
    }

    public String getG800c() {
        return g800c;
    }

    public void setG800c(String g800c) {
        this.g800c = g800c;
    }

    public String getG800d() {
        return g800d;
    }

    public void setG800d(String g800d) {
        this.g800d = g800d;
    }

    public String getG800e() {
        return g800e;
    }

    public void setG800e(String g800e) {
        this.g800e = g800e;
    }

    public String getG0001() {
        return g0001;
    }

    public void setG0001(String g0001) {
        this.g0001 = g0001;
    }

    public String getG0002() {
        return g0002;
    }

    public void setG0002(String g0002) {
        this.g0002 = g0002;
    }
}
