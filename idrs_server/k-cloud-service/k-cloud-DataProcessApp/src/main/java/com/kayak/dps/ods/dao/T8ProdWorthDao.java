package com.kayak.dps.ods.dao;


import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.dps.app.model.T8ProdWorth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class T8ProdWorthDao extends ComnDao {

	//产品信息净值查询
	public SqlResult<T8ProdWorth> findT8ProdWorths(SqlParam<T8ProdWorth> params) throws Exception {
		System.out.println(params.getModel());
		String sql="SELECT prod_cd,prod_nm,nav_dt,isu_dt,format(unt_nav,8) as unt_nav,format(acm_nav,8) as acm_nav,tot_ast,tot_lbl,tot_nav,tot_lot FROM DWD_PRD_PRD_NAV_INF " +
				" where 1=1 ";
		if (StringUtils.isNotEmpty(params.getModel().getProdCd())) {
			sql += " and PROD_CD like '%$U{prodCd}%' ";
		}
		if (StringUtils.equals(params.getModel().getNavDt(),"sysDate")) {
			sql += " and NAV_DT = (select paravalue from sys_param sp where sp.paraid = '10004' limit 1) ";
		}else if (StringUtils.isNotBlank(params.getModel().getNavDt()) && !StringUtils.equals(params.getModel().getNavDt(),"sysDate")) {
			sql += " and DATE(NAV_DT) = DATE($S{navDt}) ";
		}
		if(StringUtils.isNotBlank(params.getModel().getIsuDt())){
			sql += " and DATE(ISU_DT)= DATE($S{isuDt})";
		}
		if(StringUtils.isNotBlank(params.getModel().getProdCodes())){
			sql += " and PROD_CD in ("+params.getModel().getProdCodes()+")";
		}
		sql += "order by NAV_DT desc,PROD_CD";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return super.update("INSERT INTO dwd_prd_prd_nav_inf(prod_cd,prod_nm,nav_dt,isu_dt,unt_nav,acm_nav,tot_nav,tot_lot,tot_ast,tot_lbl,rct_7d_anl_yld,rct_1m_anl_yld,rct_3m_anl_yld,rct_6m_anl_yld,rct_1y_anl_yld,rct_2y_anl_yld,rct_3y_anl_yld,rct_5y_anl_yld,set_up_til_now_yld,last_opn_day_til_now_yld,now_anl_yld,rct_1d_grw_rat,rct_7d_grw_rat,rct_1m_grw_rat,rct_3m_grw_rat,rct_6m_grw_rat,rct_1y_grw_rat,rct_2y_grw_rat,rct_3y_grw_rat,rct_5y_grw_rat,set_up_til_now_grw_rat,last_opn_day_til_now_grw_rat,now_grw_rat,cnv_unt_nav,set_up_til_now_max_wdw,set_up_til_now_flct_rat,crt_dt,upd_dt) VALUES($S{prodCd},$S{prodNm},$S{navDt},$S{isuDt},$D{untNav},$D{acmNav},$D{totNav},$D{totLot},$D{totAst},$D{totLbl},$D{rct7dAnlYld},$D{rct1mAnlYld},$D{rct3mAnlYld},$D{rct6mAnlYld},$D{rct1yAnlYld},$D{rct2yAnlYld},$D{rct3yAnlYld},$D{rct5yAnlYld},$D{setUpTilNowYld},$D{lastOpnDayTilNowYld},$D{nowAnlYld},$D{rct1dGrwRat},$D{rct7dGrwRat},$D{rct1mGrwRat},$D{rct3mGrwRat},$D{rct6mGrwRat},$D{rct1yGrwRat},$D{rct2yGrwRat},$D{rct3yGrwRat},$D{rct5yGrwRat},$D{setUpTilNowGrwRat},$D{lastOpnDayTilNowGrwRat},$D{nowGrwRat},$D{cnvUntNav},$D{setUpTilNowMaxWdw},$D{setUpTilNowFlctRat},$S{crtDt},$S{updDt})"
				,DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return super.update("UPDATE dwd_prd_prd_nav_inf SET prod_nm=$S{prodNm} ,isu_dt=$S{isuDt} ,unt_nav=$D{untNav} ,acm_nav=$D{acmNav} ,tot_nav=$D{totNav} ,tot_lot=$D{totLot} ,tot_ast=$D{totAst} ,tot_lbl=$D{totLbl} ,rct_7d_anl_yld=$D{rct7dAnlYld} ,rct_1m_anl_yld=$D{rct1mAnlYld} ,rct_3m_anl_yld=$D{rct3mAnlYld} ,rct_6m_anl_yld=$D{rct6mAnlYld} ,rct_1y_anl_yld=$D{rct1yAnlYld} ,rct_2y_anl_yld=$D{rct2yAnlYld} ,rct_3y_anl_yld=$D{rct3yAnlYld} ,rct_5y_anl_yld=$D{rct5yAnlYld} ,set_up_til_now_yld=$D{setUpTilNowYld} ,last_opn_day_til_now_yld=$D{lastOpnDayTilNowYld} ,now_anl_yld=$D{nowAnlYld} ,rct_1d_grw_rat=$D{rct1dGrwRat} ,rct_7d_grw_rat=$D{rct7dGrwRat} ,rct_1m_grw_rat=$D{rct1mGrwRat} ,rct_3m_grw_rat=$D{rct3mGrwRat} ,rct_6m_grw_rat=$D{rct6mGrwRat} ,rct_1y_grw_rat=$D{rct1yGrwRat} ,rct_2y_grw_rat=$D{rct2yGrwRat} ,rct_3y_grw_rat=$D{rct3yGrwRat} ,rct_5y_grw_rat=$D{rct5yGrwRat} ,set_up_til_now_grw_rat=$D{setUpTilNowGrwRat} ,last_opn_day_til_now_grw_rat=$D{lastOpnDayTilNowGrwRat} ,now_grw_rat=$D{nowGrwRat} ,cnv_unt_nav=$D{cnvUntNav} ,set_up_til_now_max_wdw=$D{setUpTilNowMaxWdw} ,set_up_til_now_flct_rat=$D{setUpTilNowFlctRat} ,crt_dt=$S{crtDt} ,upd_dt=$S{updDt}  WHERE  prod_cd=$S{prodCd} AND nav_dt=$S{navDt} "
				,DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteT8ProdWorth(SqlParam<T8ProdWorth> params) throws Exception {
		return super.update("DELETE FROM dwd_prd_prd_nav_inf WHERE  prod_cd=$S{prodCd} AND nav_dt=$S{navDt} "
				,DataSourceProperty.PUB,params.getModel());
	}

	public SqlResult<T8ProdWorth> findProdWorthCdAndNm(SqlParam<T8ProdWorth> params) throws Exception {
		return super.findRows("SELECT DISTINCT prod_cd,PROD_NM  FROM DWD_PRD_PRD_BAS_INF " ,DataSourceProperty.PUB, params);
	}


	public SqlResult<T8ProdWorth> findT8ProdProdName(SqlParam<T8ProdWorth> params) throws Exception {
		return super.findRows("SELECT DISTINCT PROD_NM  FROM DWD_PRD_PRD_NAV_INF where prod_cd=$S{prodCd} " ,DataSourceProperty.PUB, params);
	}

	public SqlResult<T8ProdWorth> findDataByProdCode(SqlParam<T8ProdWorth> params) throws Exception {
		return super.findRows("select\n" +
				"\tformat(RCT_7D_ANL_YLD,8) RCT_7D_ANL_YLD,\n" +
				"\tformat(RCT_1M_ANL_YLD,8) RCT_1M_ANL_YLD,\n" +
				"\tformat(RCT_3M_ANL_YLD,8) RCT_3M_ANL_YLD,\n" +
				"\tformat(RCT_6M_ANL_YLD,8) RCT_6M_ANL_YLD,\n" +
				"\tformat(RCT_1Y_ANL_YLD,8) RCT_1Y_ANL_YLD,\n" +
				"\tformat(RCT_2Y_ANL_YLD,8) RCT_2Y_ANL_YLD,\n" +
				"\tformat(RCT_3Y_ANL_YLD,8) RCT_3Y_ANL_YLD,\n" +
				"\tformat(RCT_5Y_ANL_YLD,8) RCT_5Y_ANL_YLD,\n" +
				"\tformat(SET_UP_TIL_NOW_YLD,8) SET_UP_TIL_NOW_YLD,\n" +
				"\tformat(LAST_OPN_DAY_TIL_NOW_YLD,8) LAST_OPN_DAY_TIL_NOW_YLD,\n" +
				"\tformat(NOW_ANL_YLD,8) NOW_ANL_YLD,\n" +
				"\tformat(RCT_1D_GRW_RAT,8) RCT_1D_GRW_RAT,\n" +
				"\tformat(RCT_7D_GRW_RAT,8) RCT_7D_GRW_RAT,\n" +
				"\tformat(RCT_1M_GRW_RAT,8) RCT_1M_GRW_RAT,\n" +
				"\tformat(RCT_3M_GRW_RAT,8) RCT_3M_GRW_RAT,\n" +
				"\tformat(RCT_6M_GRW_RAT,8) RCT_6M_GRW_RAT,\n" +
				"\tformat(RCT_1Y_GRW_RAT,8) RCT_1Y_GRW_RAT,\n" +
				"\tformat(RCT_2Y_GRW_RAT,8) RCT_2Y_GRW_RAT,\n" +
				"\tformat(RCT_3Y_GRW_RAT,8) RCT_3Y_GRW_RAT,\n" +
				"\tformat(RCT_5Y_GRW_RAT,8) RCT_5Y_GRW_RAT,\n" +
				"\tformat(SET_UP_TIL_NOW_GRW_RAT,8) SET_UP_TIL_NOW_GRW_RAT,\n" +
				"\tformat(LAST_OPN_DAY_TIL_NOW_GRW_RAT,8) LAST_OPN_DAY_TIL_NOW_GRW_RAT,\n" +
				"\tformat(NOW_GRW_RAT,8) NOW_GRW_RAT,\n" +
				"\tCNV_UNT_NAV,\n" +
				"\tformat(SET_UP_TIL_NOW_MAX_WDW,8) SET_UP_TIL_NOW_MAX_WDW,\n" +
				"\tformat(SET_UP_TIL_NOW_FLCT_RAT,8) SET_UP_TIL_NOW_FLCT_RAT,\n" +
				"\tUNT_NAV_P4,\n" +
				"\tTOT_NAV_P4,\n" +
				"\tISU_STS\n" +
				"from\n" +
				"\tDWD_PRD_PRD_NAV_INF " +
				"WHERE  prod_cd=$S{prodCd} AND nav_dt=$S{navDt} limit 1 " ,DataSourceProperty.PUB, params);
	}

}
