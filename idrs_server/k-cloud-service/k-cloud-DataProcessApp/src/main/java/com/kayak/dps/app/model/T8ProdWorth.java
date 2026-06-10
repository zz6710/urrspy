package com.kayak.dps.app.model;

import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@GraphQLModel(fetcher = "t8ProdWorthService",table = "DWD_PRD_PRD_NAV_INF")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class T8ProdWorth {
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "产品代码", sql = "prod_cd = $S{prodCd}" ,field = "prod_cd")
   private String prodCd;
   @GraphQLField(kkhtml = "KFieldText", label = "产品名称", sql = "prod_nm = $S{prodNm}" ,field = "prod_nm")
   private String prodNm;
   @GraphQLField(key = true ,kkhtml = "KFieldText", label = "净值日期")
   private String navDt;
   @GraphQLField(kkhtml = "KFieldText", label = "发布日期")
   private String isuDt;
   @GraphQLField(kkhtml = "KFieldText", label = "单位净值", sql = "unt_nav = $S{untNav}" ,field = "unt_nav")
   private String untNav;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值", sql = "acm_nav = $S{acmNav}" ,field = "acm_nav")
   private String acmNav;
   @GraphQLField(kkhtml = "KFieldText", label = "总净值", sql = "tot_nav = $S{totNav}" ,field = "tot_nav")
   private String totNav;
   @GraphQLField(kkhtml = "KFieldText", label = "总份额", sql = "tot_lot = $S{totLot}" ,field = "tot_lot")
   private String totLot;
   @GraphQLField(kkhtml = "KFieldText", label = "总资产", sql = "tot_ast = $S{totAst}" ,field = "tot_ast")
   private String totAst;
   @GraphQLField(kkhtml = "KFieldText", label = "总负债", sql = "tot_lbl = $S{totLbl}" ,field = "tot_lbl")
   private String totLbl;
   @GraphQLField(kkhtml = "KFieldText", label = "近七日年化收益率", sql = "rct_7d_anl_yld = $S{rct7dAnlYld}" ,field = "rct_7d_anl_yld")
   private String rct7dAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近一个月年化收益率", sql = "rct_1m_anl_yld = $S{rct1mAnlYld}" ,field = "rct_1m_anl_yld")
   private String rct1mAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近三个月年化收益率", sql = "rct_3m_anl_yld = $S{rct3mAnlYld}" ,field = "rct_3m_anl_yld")
   private String rct3mAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近六个月年化收益率", sql = "rct_6m_anl_yld = $S{rct6mAnlYld}" ,field = "rct_6m_anl_yld")
   private String rct6mAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近一年年化收益率", sql = "rct_1y_anl_yld = $S{rct1yAnlYld}" ,field = "rct_1y_anl_yld")
   private String rct1yAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近两年年化收益率", sql = "rct_2y_anl_yld = $S{rct2yAnlYld}" ,field = "rct_2y_anl_yld")
   private String rct2yAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近三年年化收益率", sql = "rct_3y_anl_yld = $S{rct3yAnlYld}" ,field = "rct_3y_anl_yld")
   private String rct3yAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近五年年化收益率", sql = "rct_5y_anl_yld = $S{rct5yAnlYld}" ,field = "rct_5y_anl_yld")
   private String rct5yAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "成立至今收益率", sql = "set_up_til_now_yld = $S{setUpTilNowYld}" ,field = "set_up_til_now_yld")
   private String setUpTilNowYld;
   @GraphQLField(kkhtml = "KFieldText", label = "上一开放日至今收益率", sql = "last_opn_day_til_now_yld = $S{lastOpnDayTilNowYld}" ,field = "last_opn_day_til_now_yld")
   private String lastOpnDayTilNowYld;
   @GraphQLField(kkhtml = "KFieldText", label = "今年年化收益率", sql = "now_anl_yld = $S{nowAnlYld}" ,field = "now_anl_yld")
   private String nowAnlYld;
   @GraphQLField(kkhtml = "KFieldText", label = "近1日净值增长率", sql = "rct_1d_grw_rat = $S{rct1dGrwRat}" ,field = "rct_1d_grw_rat")
   private String rct1dGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近7日净值增长率", sql = "rct_7d_grw_rat = $S{rct7dGrwRat}" ,field = "rct_7d_grw_rat")
   private String rct7dGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近一个月净值增长率", sql = "rct_1m_grw_rat = $S{rct1mGrwRat}" ,field = "rct_1m_grw_rat")
   private String rct1mGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近三个月净值增长率", sql = "rct_3m_grw_rat = $S{rct3mGrwRat}" ,field = "rct_3m_grw_rat")
   private String rct3mGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近六个月净值增长率", sql = "rct_6m_grw_rat = $S{rct6mGrwRat}" ,field = "rct_6m_grw_rat")
   private String rct6mGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近一年净值增长率", sql = "rct_1y_grw_rat = $S{rct1yGrwRat}" ,field = "rct_1y_grw_rat")
   private String rct1yGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近两年净值增长率", sql = "rct_2y_grw_rat = $S{rct2yGrwRat}" ,field = "rct_2y_grw_rat")
   private String rct2yGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近三年净值增长率", sql = "rct_3y_grw_rat = $S{rct3yGrwRat}" ,field = "rct_3y_grw_rat")
   private String rct3yGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "近五年净值增长率", sql = "rct_5y_grw_rat = $S{rct5yGrwRat}" ,field = "rct_5y_grw_rat")
   private String rct5yGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "成立至今净值增长率", sql = "set_up_til_now_grw_rat = $S{setUpTilNowGrwRat}" ,field = "set_up_til_now_grw_rat")
   private String setUpTilNowGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "上一开放日至今净值增长率", sql = "last_opn_day_til_now_grw_rat = $S{lastOpnDayTilNowGrwRat}" ,field = "last_opn_day_til_now_grw_rat")
   private String lastOpnDayTilNowGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "今年净值增长率", sql = "now_grw_rat = $S{nowGrwRat}" ,field = "now_grw_rat")
   private String nowGrwRat;
   @GraphQLField(kkhtml = "KFieldText", label = "折算单位净值", sql = "cnv_unt_nav = $S{cnvUntNav}" ,field = "cnv_unt_nav")
   private String cnvUntNav;
   @GraphQLField(kkhtml = "KFieldText", label = "成立至今最大回撤", sql = "set_up_til_now_max_wdw = $S{setUpTilNowMaxWdw}" ,field = "set_up_til_now_max_wdw")
   private String setUpTilNowMaxWdw;
   @GraphQLField(kkhtml = "KFieldText", label = "成立至今波动率", sql = "set_up_til_now_flct_rat = $S{setUpTilNowFlctRat}" ,field = "set_up_til_now_flct_rat")
   private String setUpTilNowFlctRat;
   @GraphQLField(kkhtml = "KFieldText", label = "创建日期", sql = "crt_dt = $S{crtDt}" ,field = "crt_dt")
   private String crtDt;
   @GraphQLField(kkhtml = "KFieldText", label = "更新日期", sql = "upd_dt = $S{updDt}" ,field = "upd_dt")
   private String updDt;
   @GraphQLField(label = "起息日开始时间")
   private String startDate;

   @GraphQLField(label = "起息日截止时间")
   private String endDate;
   @GraphQLField(label = "到期日开始时间")
   private String isuStartDate;

   @GraphQLField(label = "到期日截止时间")
   private String isuEndDate;
   @GraphQLField( kkhtml = "KFieldText", label = "批量查询产品代码", sql = "PROD_CD in ($S{prodCodes})" ,field = "prodCodes")
   private String prodCodes;
   @GraphQLField(kkhtml = "KFieldText", label = "单位净值", sql = "UNT_NAV_P4 = $S{untNavP4}" ,field = "UNT_NAV_P4")
   private String untNavP4;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值", sql = "tot_nav_P4 = $S{totNavP4}" ,field = "tot_nav_P4")
   private String totNavP4;
   @GraphQLField(kkhtml = "KFieldText", label = "累计净值", sql = "isu_sts = $S{isuSts}" ,field = "isu_sts")
   private String isuSts;

}