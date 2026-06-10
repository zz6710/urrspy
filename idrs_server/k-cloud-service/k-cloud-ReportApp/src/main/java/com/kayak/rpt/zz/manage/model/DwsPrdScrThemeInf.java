package com.kayak.rpt.zz.manage.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@GraphQLModel(fetcher = "dwsPrdScrThemeInfService",table = "dws_prd_scr_theme_inf")
@Data
public class DwsPrdScrThemeInf {

   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "ID", sql = "id = $S{id}" ,field = "id")
   private String id;
   @GraphQLField(kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "资产代码", sql = "scr_cd like '%$U{scrCd}%'" ,field = "scr_cd")
   private String scrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "资产负债类别", sql = "ass_debt_type = $S{assDebtType}" ,field = "ass_debt_type")
   private String assDebtType;
   @GraphQLField(kkhtml = "KFieldText", label = "投资金额", sql = "amount = $S{amount}" ,field = "amount")
   private String amount;
   @GraphQLField(kkhtml = "KFieldText", label = "直接或间接投资", sql = "invest_ways = $S{investWays}" ,field = "invest_ways")
   private String investWays;
   @GraphQLField(kkhtml = "KFieldText", label = "中间层数", sql = "mid_num = $S{midNum}" ,field = "mid_num")
   private String midNum;
   @GraphQLField(kkhtml = "KFieldText", label = "中间层行内资产/负债编码", sql = "mid_scr_cd like '%$U{midScrCd}%'" ,field = "mid_scr_cd")
   private String midScrCd;
   @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "report_date like '$U{reportDate}%'" ,field = "report_date")
   private String reportDate;
   @GraphQLField(kkhtml = "KFieldText", label = "资产到期日", sql = "end_date = $S{endDate}" ,field = "end_date")
   private String endDate;
   @GraphQLField(kkhtml = "KFieldText", label = "剩余期限", sql = "rdm_trm = $S{rdmTrm}" ,field = "rdm_trm")
   private String rdmTrm;
   @GraphQLField(kkhtml = "KFieldText", label = "资产期限结构", sql = "scr_trm = $S{scrTrm}" ,field = "scr_trm")
   private String scrTrm;
   @GraphQLField(kkhtml = "KFieldText", label = "债券信用等级", sql = "rate_level = $S{rateLevel}" ,field = "rate_level")
   private String rateLevel;
   @GraphQLField(kkhtml = "KFieldText", label = "是否专项债券", sql = "spc_bond_f = $S{spcBondF}" ,field = "spc_bond_f")
   private String spcBondF;
   @GraphQLField(kkhtml = "KFieldText", label = "是否场内股票质押回购", sql = "in_ashare_repo = $S{inAshareRepo}" ,field = "in_ashare_repo")
   private String inAshareRepo;
   @GraphQLField(kkhtml = "KFieldText", label = "是否场外股票质押融资", sql = "ex_ashare_repo = $S{exAshareRepo}" ,field = "ex_ashare_repo")
   private String exAshareRepo;
   @GraphQLField(kkhtml = "KFieldText", label = "是否逾期", sql = "isoverdue = $S{isoverdue}" ,field = "isoverdue")
   private String isoverdue;
   @GraphQLField(kkhtml = "KFieldText", label = "逾期资产（金额）", sql = "overdue_amt = $S{overdueAmt}" ,field = "overdue_amt")
   private String overdueAmt;
   @GraphQLField(kkhtml = "KFieldText", label = "是否科技金融", sql = "isfintech = $S{isfintech}" ,field = "isfintech")
   private String isfintech;
   @GraphQLField(kkhtml = "KFieldText", label = "是否绿色金融", sql = "isgreen = $S{isgreen}" ,field = "isgreen")
   private String isgreen;
   @GraphQLField(kkhtml = "KFieldText", label = "是否普惠金融", sql = "isinclusive = $S{isinclusive}" ,field = "isinclusive")
   private String isinclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "是否养老金融", sql = "ispension = $S{ispension}" ,field = "ispension")
   private String ispension;
   @GraphQLField(kkhtml = "KFieldText", label = "是否数字金融", sql = "isdigital = $S{isdigital}" ,field = "isdigital")
   private String isdigital;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高污染高能耗", sql = "ispollution = $S{ispollution}" ,field = "ispollution")
   private String ispollution;
   @GraphQLField(kkhtml = "KFieldText", label = "募资主体注册地", sql = "cmp_blg_zon = $S{cmpBlgZon}" ,field = "cmp_blg_zon")
   private String cmpBlgZon;
   @GraphQLField(kkhtml = "KFieldText", label = "项目所属地", sql = "pro_blg_zon = $S{proBlgZon}" ,field = "pro_blg_zon")
   private String proBlgZon;
   @GraphQLField(kkhtml = "KFieldText", label = "被投企业全称", sql = "cmp_nm = $S{cmpNm}" ,field = "cmp_nm")
   private String cmpNm;
   @GraphQLField(kkhtml = "KFieldText", label = "被投企业统一社会信用代码", sql = "cmp_social_cd = $S{cmpSocialCd}" ,field = "cmp_social_cd")
   private String cmpSocialCd;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所属科技相关产业", sql = "cmp_blg_fintech = $S{cmpBlgFintech}" ,field = "cmp_blg_fintech")
   private String cmpBlgFintech;
   @GraphQLField(kkhtml = "KFieldText", label = "高技术制造业分类", sql = "fintech_typ1 = $S{fintechTyp1}" ,field = "fintech_typ1")
   private String fintechTyp1;
   @GraphQLField(kkhtml = "KFieldText", label = "高技术服务业分类", sql = "fintech_typ2 = $S{fintechTyp2}" ,field = "fintech_typ2")
   private String fintechTyp2;
   @GraphQLField(kkhtml = "KFieldText", label = "战略性新兴产业分类", sql = "fintech_typ3 = $S{fintechTyp3}" ,field = "fintech_typ3")
   private String fintechTyp3;
   @GraphQLField(kkhtml = "KFieldText", label = "知识产权（专利）密集型产业分类", sql = "fintech_typ4 = $S{fintechTyp4}" ,field = "fintech_typ4")
   private String fintechTyp4;
   @GraphQLField(kkhtml = "KFieldText", label = "是否高新技术企业", sql = "fintech_typ5 = $S{fintechTyp5}" ,field = "fintech_typ5")
   private String fintechTyp5;
   @GraphQLField(kkhtml = "KFieldText", label = "是否科技型中小企业", sql = "fintech_typ6 = $S{fintechTyp6}" ,field = "fintech_typ6")
   private String fintechTyp6;
   @GraphQLField(kkhtml = "KFieldText", label = "是否“专精特新”中小企业", sql = "fintech_typ7 = $S{fintechTyp7}" ,field = "fintech_typ7")
   private String fintechTyp7;
   @GraphQLField(kkhtml = "KFieldText", label = "其他科技型企业", sql = "fintech_typ8 = $S{fintechTyp8}" ,field = "fintech_typ8")
   private String fintechTyp8;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所属绿色低碳产业", sql = "cmp_blg_green = $S{cmpBlgGreen}" ,field = "cmp_blg_green")
   private String cmpBlgGreen;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所属普惠领域", sql = "cmp_blg_inclusive = $S{cmpBlgInclusive}" ,field = "cmp_blg_inclusive")
   private String cmpBlgInclusive;
   @GraphQLField(kkhtml = "KFieldText", label = "中小微企业分类", sql = "inclusive_typ1 = $S{inclusiveTyp1}" ,field = "inclusive_typ1")
   private String inclusiveTyp1;
   @GraphQLField(kkhtml = "KFieldText", label = "三农领域分类", sql = "inclusive_typ2 = $S{inclusiveTyp2}" ,field = "inclusive_typ2")
   private String inclusiveTyp2;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所属养老产业", sql = "cmp_blg_pension = $S{cmpBlgPension}" ,field = "cmp_blg_pension")
   private String cmpBlgPension;
   @GraphQLField(kkhtml = "KFieldText", label = "企业所属数字行业", sql = "cmp_blg_digital = $S{cmpBlgDigital}" ,field = "cmp_blg_digital")
   private String cmpBlgDigital;
   @GraphQLField(kkhtml = "KFieldText", label = "数字产业化分类", sql = "digital_typ1 = $S{digitalTyp1}" ,field = "digital_typ1")
   private String digitalTyp1;
   @GraphQLField(kkhtml = "KFieldText", label = "产业数字化分类", sql = "digital_typ2 = $S{digitalTyp2}" ,field = "digital_typ2")
   private String digitalTyp2;

}