package com.kayak.rpt.zz.historyInfo.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.Data;

@Data
@GraphQLModel(fetcher = "appNavInfoReghService",table = "app_nav_info_reg_h")
public class AppNavInfoRegh {
    @GraphQLField(kkhtml = "KFieldText", label = "*发行机构代码", sql = "bank_code = $S{bankCode}" ,field = "bank_code")
    private String bankCode;
    @GraphQLField(kkhtml = "KFieldText", label = "*产品登记编码", sql = "prod_reg_enc like '%$U{prodRegEnc}%'" ,field = "prod_reg_enc")
    private String prodRegEnc;
    @GraphQLField(kkhtml = "KFieldText", label = "*净值登记类型", sql = "nav_reg_type = $S{navRegType}" ,field = "nav_reg_type")
    private String navRegType;
    @GraphQLField(kkhtml = "KFieldText", label = "*产品子份额代码", sql = "son_share_code like '%$U{sonShareCode}%'" ,field = "son_share_code")
    private String sonShareCode;
    @GraphQLField(kkhtml = "KFieldText", label = "*币种", sql = "cny = $S{cny}" ,field = "cny")
    private String cny;
    @GraphQLField(kkhtml = "KFieldText", label = "*净值", sql = "nav = $S{nav}" ,field = "nav")
    private String nav;
    @GraphQLField(kkhtml = "KFieldText", label = "*折算人民币净值", sql = "rmb_nav = $S{rmbNav}" ,field = "rmb_nav")
    private String rmbNav;
    @GraphQLField(kkhtml = "KFieldText", label = "达基净值", sql = "dj_nav = $S{djNav}" ,field = "dj_nav")
    private String djNav;
    @GraphQLField(kkhtml = "KFieldText", label = "*累计净值", sql = "total_nav = $S{totalNav}" ,field = "total_nav")
    private String totalNav;
    @GraphQLField(kkhtml = "KFieldText", label = "*折算人民币累计净值", sql = "rmb_total_nav = $S{rmbTotalNav}" ,field = "rmb_total_nav")
    private String rmbTotalNav;
    @GraphQLField(kkhtml = "KFieldText", label = "*复权净值", sql = "fq_nav = $S{fqNav}" ,field = "fq_nav")
    private String fqNav;
    @GraphQLField(kkhtml = "KFieldText", label = "*折算人民币复权净值", sql = "rmb_fq_nav = $S{rmbFqNav}" ,field = "rmb_fq_nav")
    private String rmbFqNav;
    @GraphQLField(kkhtml = "KFieldText", label = "*估值依据", sql = "nav_cal_type = $S{navCalType}" ,field = "nav_cal_type")
    private String navCalType;
    @GraphQLField(kkhtml = "KFieldText", label = "*份额", sql = "share = $S{share}" ,field = "share")
    private String share;
    @GraphQLField(kkhtml = "KFieldText", label = "*净值日期", sql = "nav_date = $S{navDate}" ,field = "nav_date")
    private String navDate;
    @GraphQLField(kkhtml = "KFieldText", label = "披露日期", sql = "disclosure_date = $S{disclosureDate}" ,field = "disclosure_date")
    private String disclosureDate;
    @GraphQLField(kkhtml = "KFieldText", label = "*存续余额（元）", sql = "remain_bal = $S{remainBal}" ,field = "remain_bal")
    private String remainBal;
    @GraphQLField(kkhtml = "KFieldText", label = "*折算人民币存续余额（元）", sql = "rmb_remain_bal = $S{rmbRemainBal}" ,field = "rmb_remain_bal")
    private String rmbRemainBal;
    @GraphQLField(kkhtml = "KFieldText", label = "备注", sql = "details = $S{details}" ,field = "details")
    private String details;
    @GraphQLField(kkhtml = "KFieldText", label = "登记日期", sql = "register_date = $S{registerDate}" ,field = "register_date")
    private String registerDate;
    @GraphQLField(kkhtml = "KFieldText", label = "登记流水号", sql = "register_serno = $S{registerSerno}" ,field = "register_serno")
    private String registerSerno;
    @GraphQLField(kkhtml = "KFieldText", label = "登记状态", sql = "register_status = $S{registerStatus}" ,field = "register_status")
    private String registerStatus;
    @GraphQLField(kkhtml = "KFieldText", label = "新增日期", sql = "create_date = $S{createDate}" ,field = "create_date")
    private String createDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送起始日期", sql = "theory_report_start_date = $S{theoryReportStartDate}" ,field = "theory_report_start_date")
    private String theoryReportStartDate;
    @GraphQLField(kkhtml = "KFieldText", label = "理论报送截止日期", sql = "theory_report_end_date = $S{theoryReportEndDate}" ,field = "theory_report_end_date")
    private String theoryReportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期", sql = "report_date = $S{reportDate}" ,field = "report_date")
    private String reportDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期起始", sql = "report_date >= $S{reportBeginDate}" ,field = "report_begin_date")
    private String reportBeginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "报送日期终止", sql = "report_date <= $S{reportEndDate}" ,field = "report_end_date")
    private String reportEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日期起始", sql = "nav_date >= $S{navBeginDate}" ,field = "nav_begin_date")
    private String navBeginDate;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日期终止", sql = "nav_date <= $S{navEndDate}" ,field = "nav_end_date")
    private String navEndDate;
    @GraphQLField(kkhtml = "KFieldText", label = "数据日期", sql = "data_date like '%$U{dataDate}%'" ,field = "data_date")
    private String dataDate;
    @GraphQLField(key = true ,kkhtml = "KFieldText", label = "", sql = "id = $S{id}" ,field = "id")
    private String id;
    @GraphQLField(kkhtml = "KFieldText", label = "创建人", sql = "crt_user = $S{crtUser}" ,field = "crt_user")
    private String crtUser;
    @GraphQLField(kkhtml = "KFieldText", label = "更新人", sql = "upd_user = $S{updUser}" ,field = "upd_user")
    private String updUser;
    @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
    private String crtDt;
    @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
    private String updDt;
    @GraphQLField(kkhtml = "KFieldText", label = "创建时间", sql = "crt_time = $S{crtTime}" ,field = "crt_time")
    private String crtTime;
    @GraphQLField(kkhtml = "KFieldText", label = "更新时间", sql = "upd_time = $S{updTime}" ,field = "upd_time")
    private String updTime;
    @GraphQLField(kkhtml = "KFieldText", label = "母产品代码", sql = "mother_fund_code = $S{motherFundCode}" ,field = "mother_fund_code")
    private String motherFundCode;
    @GraphQLField(kkhtml = "KFieldText", label = "产品类型", sql = "open_type = $S{openType}" ,field = "open_type")
    private String openType;
    @GraphQLField(kkhtml = "KFieldText", label = "0份额", sql = "vol_zero_flag = $S{volZeroFlag}" ,field = "vol_zero_flag")
    private String volZeroFlag;
    @GraphQLField(kkhtml = "KFieldText", label = "产品成立日", sql = "establish_date = $S{establishDate}" ,field = "establish_date")
    private String establishDate;
    @GraphQLField(kkhtml = "KFieldText", label = "产品到期日", sql = "end_date = $S{endDate}" ,field = "end_date")
    private String endDate;
    @GraphQLField(kkhtml = "KFieldText", label = "基准日", sql = "jz_date = $S{jzDate}" ,field = "jz_date")
    private String jzDate;
    @GraphQLField(kkhtml = "KFieldText", label = "计算达基净值业绩比较基准", sql = "prfr_bnch = $S{prfrBnch}" ,field = "prfr_bnch")
    private String prfrBnch;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩基准类型", sql = "prfr_bnch_typ = $S{prfrBnchTyp}" ,field = "prfr_bnch_typ")
    private String prfrBnchTyp;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩基准上限", sql = "intr_rt_upp = $S{intrRtUpp}" ,field = "intr_rt_upp")
    private String intrRtUpp;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩基准下限", sql = "intr_rt_flr = $S{intrRtFlr}" ,field = "intr_rt_flr")
    private String intrRtFlr;
    @GraphQLField(kkhtml = "KFieldText", label = "业绩基准说明", sql = "prfr_bnch_typ_dscr = $S{prfrBnchTypDscr}" ,field = "prfr_bnch_typ_dscr")
    private String prfrBnchTypDscr;
    @GraphQLField(kkhtml = "KFieldText", label = "万份收益", sql = "en_dwjjsy = $S{en_dwjjsy}" ,field = "en_dwjjsy")
    private String enDwjjsy;
    @GraphQLField(kkhtml = "KFieldText", label = "是否迁移产品", sql = "is_prod_transfer = $S{is_prod_transfer}" ,field = "is_prod_transfer")
    private String isProdTransfer;
    @GraphQLField(kkhtml = "KFieldText", label = "上一基准日单位净值", sql = "lst_jz_nav = $S{lst_jz_nav}" ,field = "lst_jz_nav")
    private String lstJzNav;
    @GraphQLField(kkhtml = "KFieldText", label = "迁移产品迁移净值", sql = "transfer_nav = $S{transfer_nav}" ,field = "transfer_nav")
    private String transferNav;
    @GraphQLField(kkhtml = "KFieldText", label = "上一工作日单位净值", sql = "lst_wkd_nav = $S{lst_wkd_nav}" ,field = "lst_wkd_nav")
    private String lstWkdNav;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日指数", sql = "nav_dt_index = $S{nav_dt_index}" ,field = "nav_dt_index")
    private String navDtIndex;
    @GraphQLField(kkhtml = "KFieldText", label = "净值日上一工作日指数", sql = "lst_wkd_index = $S{lst_wkd_index}" ,field = "lst_wkd_index")
    private String lstWkdIndex;
    @GraphQLField(kkhtml = "KFieldText", sql = "filter0_vol = $S{filter0Vol}" ,field = "filter0_vol")
    private String filter0Vol;
}