package com.kayak.rpt.rhzg.model;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

//债券等资产配置情况信息
@Data
@GraphQLModel(fetcher = "ZG10Service",table = "app_pbc_report_zg10")
public class ZG10 {

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

    @ExcelProperty(value = "h10000_一、债务证券（按评级分类）")
    @GraphQLField(kkhtml = "KFieldText", label = "h10000_一、债务证券（按评级分类）", sql = "h10000 = $S{h10000}" ,field = "h10000")
    private String h10000;
    @ExcelProperty(value = "h15000_其中：债务证券（金融）")
    @GraphQLField(kkhtml = "KFieldText", label = "h15000_其中：债务证券（金融）", sql = "h15000 = $S{h15000}" ,field = "h15000")
    private String h15000;
    @ExcelProperty(value = "h15100_AA+（含）以上")
    @GraphQLField(kkhtml = "KFieldText", label = "h15100_AA+（含）以上", sql = "h15100 = $S{h15100}" ,field = "h15100")
    private String h15100;
    @ExcelProperty(value = "h15200_AA+以下")
    @GraphQLField(kkhtml = "KFieldText", label = "h15200_AA+以下", sql = "h15200 = $S{h15200}" ,field = "h15200")
    private String h15200;
    @ExcelProperty(value = "h15300_无评级")
    @GraphQLField(kkhtml = "KFieldText", label = "h15300_无评级", sql = "h15300 = $S{h15300}" ,field = "h15300")
    private String h15300;
    @ExcelProperty(value = "h16000_债务证券（非金融）")
    @GraphQLField(kkhtml = "KFieldText", label = "h16000_债务证券（非金融）", sql = "h16000 = $S{h16000}" ,field = "h16000")
    private String h16000;
    @ExcelProperty(value = "h16100_AA+（含）以上")
    @GraphQLField(kkhtml = "KFieldText", label = "h16100_AA+（含）以上", sql = "h16100 = $S{h16100}" ,field = "h16100")
    private String h16100;
    @ExcelProperty(value = "h16200_AA+以下")
    @GraphQLField(kkhtml = "KFieldText", label = "h16200_AA+以下", sql = "h16200 = $S{h16200}" ,field = "h16200")
    private String h16200;
    @ExcelProperty(value = "h16300_无评级")
    @GraphQLField(kkhtml = "KFieldText", label = "h16300_无评级", sql = "h16300 = $S{h16300}" ,field = "h16300")
    private String h16300;
    @ExcelProperty(value = "h20000_二、非金融企业债券按券种分类")
    @GraphQLField(kkhtml = "KFieldText", label = "h20000_二、非金融企业债券按券种分类", sql = "h20000 = $S{h20000}" ,field = "h20000")
    private String h20000;
    @ExcelProperty(value = "h21000_其中：企业债")
    @GraphQLField(kkhtml = "KFieldText", label = "h21000_其中：企业债", sql = "h21000 = $S{h21000}" ,field = "h21000")
    private String h21000;
    @ExcelProperty(value = "h22000_公司债")
    @GraphQLField(kkhtml = "KFieldText", label = "h22000_公司债", sql = "h22000 = $S{h22000}" ,field = "h22000")
    private String h22000;
    @ExcelProperty(value = "h23000_非金融企业债务融资工具")
    @GraphQLField(kkhtml = "KFieldText", label = "h23000_非金融企业债务融资工具", sql = "h23000 = $S{h23000}" ,field = "h23000")
    private String h23000;
    @ExcelProperty(value = "h30000_三、银行资本补充工具合计")
    @GraphQLField(kkhtml = "KFieldText", label = "h30000_三、银行资本补充工具合计", sql = "h30000 = $S{h30000}" ,field = "h30000")
    private String h30000;
    @ExcelProperty(value = "h31000_其中：优先股")
    @GraphQLField(kkhtml = "KFieldText", label = "h31000_其中：优先股", sql = "h31000 = $S{h31000}" ,field = "h31000")
    private String h31000;
    @ExcelProperty(value = "h32000_永续债（债性）")
    @GraphQLField(kkhtml = "KFieldText", label = "h32000_永续债（债性）", sql = "h32000 = $S{h32000}" ,field = "h32000")
    private String h32000;
    @ExcelProperty(value = "h33000_永续债（股性）")
    @GraphQLField(kkhtml = "KFieldText", label = "h33000_永续债（股性）", sql = "h33000 = $S{h33000}" ,field = "h33000")
    private String h33000;
    @ExcelProperty(value = "h34000_二级资本债")
    @GraphQLField(kkhtml = "KFieldText", label = "h34000_二级资本债", sql = "h34000 = $S{h34000}" ,field = "h34000")
    private String h34000;
    @ExcelProperty(value = "h40000_四、债券逆回购")
    @GraphQLField(kkhtml = "KFieldText", label = "h40000_四、债券逆回购", sql = "h40000 = $S{h40000}" ,field = "h40000")
    private String h40000;
    @ExcelProperty(value = "h41000_住户")
    @GraphQLField(kkhtml = "KFieldText", label = "h41000_住户", sql = "h41000 = $S{h41000}" ,field = "h41000")
    private String h41000;
    @ExcelProperty(value = "h42000_广义政府")
    @GraphQLField(kkhtml = "KFieldText", label = "h42000_广义政府", sql = "h42000 = $S{h42000}" ,field = "h42000")
    private String h42000;
    @ExcelProperty(value = "h43000_非金融企业")
    @GraphQLField(kkhtml = "KFieldText", label = "h43000_非金融企业", sql = "h43000 = $S{h43000}" ,field = "h43000")
    private String h43000;
    @ExcelProperty(value = "h44000_银行业存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "h44000_银行业存款类金融机构", sql = "h44000 = $S{h44000}" ,field = "h44000")
    private String h44000;
    @ExcelProperty(value = "h45000_银行业非存款类金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "h45000_银行业非存款类金融机构", sql = "h45000 = $S{h45000}" ,field = "h45000")
    private String h45000;
    @ExcelProperty(value = "h46000_非银行业金融机构")
    @GraphQLField(kkhtml = "KFieldText", label = "h46000_非银行业金融机构", sql = "h46000 = $S{h46000}" ,field = "h46000")
    private String h46000;
    @ExcelProperty(value = "h46100_其中：中央交易对手方")
    @GraphQLField(kkhtml = "KFieldText", label = "h46100_其中：中央交易对手方", sql = "h46100 = $S{h46100}" ,field = "h46100")
    private String h46100;
    @ExcelProperty(value = "h47000_特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "h47000_特定目的载体", sql = "h47000 = $S{h47000}" ,field = "h47000")
    private String h47000;
    @ExcelProperty(value = "h47100_银行非保本理财")
    @GraphQLField(kkhtml = "KFieldText", label = "h47100_银行非保本理财", sql = "h47100 = $S{h47100}" ,field = "h47100")
    private String h47100;
    @ExcelProperty(value = "h47200_信托公司信托产品")
    @GraphQLField(kkhtml = "KFieldText", label = "h47200_信托公司信托产品", sql = "h47200 = $S{h47200}" ,field = "h47200")
    private String h47200;
    @ExcelProperty(value = "h47300_证券公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "h47300_证券公司及其子公司资管产品", sql = "h47300 = $S{h47300}" ,field = "h47300")
    private String h47300;
    @ExcelProperty(value = "h47400_基金管理公司及其子公司专户")
    @GraphQLField(kkhtml = "KFieldText", label = "h47400_基金管理公司及其子公司专户", sql = "h47400 = $S{h47400}" ,field = "h47400")
    private String h47400;
    @ExcelProperty(value = "h47500_期货公司及其子公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "h47500_期货公司及其子公司资管产品", sql = "h47500 = $S{h47500}" ,field = "h47500")
    private String h47500;
    @ExcelProperty(value = "h47600_保险资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "h47600_保险资管产品", sql = "h47600 = $S{h47600}" ,field = "h47600")
    private String h47600;
    @ExcelProperty(value = "h47700_金融资产投资公司资管产品")
    @GraphQLField(kkhtml = "KFieldText", label = "h47700_金融资产投资公司资管产品", sql = "h47700 = $S{h47700}" ,field = "h47700")
    private String h47700;
    @ExcelProperty(value = "h47800_公募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "h47800_公募基金", sql = "h47800 = $S{h47800}" ,field = "h47800")
    private String h47800;
    @ExcelProperty(value = "h47900_私募机构私募基金")
    @GraphQLField(kkhtml = "KFieldText", label = "h47900_私募机构私募基金", sql = "h47900 = $S{h47900}" ,field = "h47900")
    private String h47900;
    @ExcelProperty(value = "h47a00_其他特定目的载体")
    @GraphQLField(kkhtml = "KFieldText", label = "h47a00_其他特定目的载体", sql = "h47a00 = $S{h47a00}" ,field = "h47a00")
    private String h47a00;
    @ExcelProperty(value = "h48000_境外")
    @GraphQLField(kkhtml = "KFieldText", label = "h48000_境外", sql = "h48000 = $S{h48000}" ,field = "h48000")
    private String h48000;
    @ExcelProperty(value = "h50000_五、资产减值准备")
    @GraphQLField(kkhtml = "KFieldText", label = "h50000_五、资产减值准备", sql = "h50000 = $S{h50000}" ,field = "h50000")
    private String h50000;
    @ExcelProperty(value = "h51000_存款")
    @GraphQLField(kkhtml = "KFieldText", label = "h51000_存款", sql = "h51000 = $S{h51000}" ,field = "h51000")
    private String h51000;
    @ExcelProperty(value = "h52000_存单")
    @GraphQLField(kkhtml = "KFieldText", label = "h52000_存单", sql = "h52000 = $S{h52000}" ,field = "h52000")
    private String h52000;
    @ExcelProperty(value = "h53000_债务证券")
    @GraphQLField(kkhtml = "KFieldText", label = "h53000_债务证券", sql = "h53000 = $S{h53000}" ,field = "h53000")
    private String h53000;
    @ExcelProperty(value = "h54000_除回购和拆借外贷款")
    @GraphQLField(kkhtml = "KFieldText", label = "h54000_除回购和拆借外贷款", sql = "h54000 = $S{h54000}" ,field = "h54000")
    private String h54000;
    @ExcelProperty(value = "h55000_回购和拆借（含借款）")
    @GraphQLField(kkhtml = "KFieldText", label = "h55000_回购和拆借（含借款）", sql = "h55000 = $S{h55000}" ,field = "h55000")
    private String h55000;
    @ExcelProperty(value = "h5b000_其他债权")
    @GraphQLField(kkhtml = "KFieldText", label = "h5b000_其他债权", sql = "h5b000 = $S{h5b000}" ,field = "h5b000")
    private String h5b000;
    @ExcelProperty(value = "h57000_股权及特定目的载体份额")
    @GraphQLField(kkhtml = "KFieldText", label = "h57000_股权及特定目的载体份额", sql = "h57000 = $S{h57000}" ,field = "h57000")
    private String h57000;
    @ExcelProperty(value = "h58000_金融衍生工具")
    @GraphQLField(kkhtml = "KFieldText", label = "h58000_金融衍生工具", sql = "h58000 = $S{h58000}" ,field = "h58000")
    private String h58000;
    @ExcelProperty(value = "h59000_应收账款")
    @GraphQLField(kkhtml = "KFieldText", label = "h59000_应收账款", sql = "h59000 = $S{h59000}" ,field = "h59000")
    private String h59000;
    @ExcelProperty(value = "h5a000_其他")
    @GraphQLField(kkhtml = "KFieldText", label = "h5a000_其他", sql = "h5a000 = $S{h5a000}" ,field = "h5a000")
    private String h5a000;
    @ExcelProperty(value = "信托产品口径")
    @GraphQLField(kkhtml = "KFieldText", label = "信托产品口径", sql = "trust_prod = $S{trustProd}" ,field = "trust_prod")
    private String trustProd;
}
