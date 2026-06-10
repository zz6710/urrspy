package com.kayak.subject.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsAstMngPlanInfo;
import com.kayak.subject.model.DwsCounterPartyInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DwsAstMngPlanInfoDao extends ComnDao {

	public List<DwsAstMngPlanInfo> findDwsAstMngPlanInfoByProdNm(String cntrProdNm, String lastMonthLastDay) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("cntrProdNm", cntrProdNm);
		params.put("lastMonthLastDay", lastMonthLastDay);// 上月最后一天
		String sql = "SELECT id,prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt,crt_date,crt_time,upd_date,upd_time" +
				" from (select *, row_number() over (partition by PROD_NM order by PROD_UP_DT desc) as row_num" +
				" FROM dws_ast_mng_plan_info" +
				" where prod_nm= $S{cntrProdNm}" +
				" and (prod_actl_end_dt = null or prod_actl_end_dt = '' or prod_actl_end_dt > $S{lastMonthLastDay})" +
				" ) t where row_num = 1";
		return super.findRows(DwsAstMngPlanInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public SqlResult<DwsAstMngPlanInfo> findDwsAstMngPlanInfos(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
	    String sql = "SELECT id,prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt,crt_date,crt_time,upd_date,upd_time" +
    				" FROM dws_ast_mng_plan_info" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getProdCd())) {
            sql += " and prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdNm())) {
            sql += " and prod_nm like '%" + params.getModel().getProdNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIssuerOrgnCd())) {
            sql += " and issuer_orgn_cd like '%" + params.getModel().getIssuerOrgnCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIssuerOrgnNm())) {
            sql += " and issuer_orgn_nm like '%" + params.getModel().getIssuerOrgnNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdBredCd())) {
            sql += " and prod_bred_cd like '%" + params.getModel().getProdBredCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdOpnDt())) {
            sql += " and prod_opn_dt like '%" + params.getModel().getProdOpnDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdUpDt())) {
            sql += " and prod_up_dt like '%" + params.getModel().getProdUpDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdExpcEndDt())) {
            sql += " and prod_expc_end_dt like '%" + params.getModel().getProdExpcEndDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdActlEndDt())) {
            sql += " and prod_actl_end_dt like '%" + params.getModel().getProdActlEndDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
            sql += " and act_dt = '" + params.getModel().getActDt() + "'";
        }

		if (Tools.isNotEmpty(params.getModel().getCrtDate())) {
			sql += " and crt_date = '" + params.getModel().getCrtDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getCrtTime())) {
			sql += " and crt_time = '" + params.getModel().getCrtTime() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdDate())) {
			sql += " and upd_date = '" + params.getModel().getUpdDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdTime())) {
			sql += " and upd_time = '" + params.getModel().getUpdTime() + "'";
		}
		sql += " order by act_dt desc, crt_date desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return super.update("INSERT INTO dws_ast_mng_plan_info(prod_cd,prod_nm,issuer_orgn_cd,issuer_orgn_nm,prod_bred_cd,prod_opn_dt,prod_up_dt,prod_expc_end_dt,prod_actl_end_dt,act_dt,crt_date,crt_time,upd_date,upd_time) VALUES($S{prodCd},$S{prodNm},$S{issuerOrgnCd},$S{issuerOrgnNm},$S{prodBredCd},$S{prodOpnDt},$S{prodUpDt},$S{prodExpcEndDt},$S{prodActlEndDt},$S{actDt},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return super.update("UPDATE dws_ast_mng_plan_info SET prod_cd=$S{prodCd} ,prod_nm=$S{prodNm} ,issuer_orgn_cd=$S{issuerOrgnCd} ,issuer_orgn_nm=$S{issuerOrgnNm} ,prod_bred_cd=$S{prodBredCd} ,prod_opn_dt=$S{prodOpnDt} ,prod_up_dt=$S{prodUpDt} ,prod_expc_end_dt=$S{prodExpcEndDt} ,prod_actl_end_dt=$S{prodActlEndDt} ,act_dt=$S{actDt} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime} WHERE id=$I{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstMngPlanInfo(SqlParam<DwsAstMngPlanInfo> params) throws Exception {
		return super.update("DELETE FROM dws_ast_mng_plan_info WHERE id=$I{id}",
				params.getModel());
	}

	public UpdateResult deleteDwsAstMngPlanInfo(DwsAstMngPlanInfo params) throws Exception {
		return super.update("DELETE FROM dws_ast_mng_plan_info WHERE act_dt = $S{actDt}",
				params);
	}

	public UpdateResult truncateDwsAstMngPlanInfo(DwsAstMngPlanInfo params) throws Exception {
		return super.update("TRUNCATE table dws_ast_mng_plan_info",
				params);
	}

}
