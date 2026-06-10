package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsMonthNavInf;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsMonthNavInfDao extends ComnDao {

	public SqlResult<DwsMonthNavInf> findDwsMonthNavInfs(SqlParam<DwsMonthNavInf> params) throws Exception {
	    String sql = "SELECT id,deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term" +
    				" FROM dws_month_nav_inf" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getId())) {
            sql += " and id = " + params.getModel().getId() +"";
        }
		if (Tools.isNotEmpty(params.getModel().getDealDate())) {
            sql += " and deal_date like '" + params.getModel().getDealDate() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCdPbc())) {
            sql += " and prdc_cd_pbc like '%" + params.getModel().getPrdcCdPbc() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCd())) {
            sql += " and prdc_cd like '%" + params.getModel().getPrdcCd() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getUntNav())) {
            sql += " and unt_nav like '%" + params.getModel().getUntNav() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAcmNav())) {
            sql += " and acm_nav like '%" + params.getModel().getAcmNav() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getRct1mGrwRat())) {
            sql += " and rct_1m_grw_rat like '%" + params.getModel().getRct1mGrwRat() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDt())) {
            sql += " and crt_dt = '" + params.getModel().getCrtDt() +"'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtTm())) {
            sql += " and crt_tm = '" + params.getModel().getCrtTm() +"'";
        }
		if (Tools.isNotEmpty(params.getModel().getRemainingTerm())) {
			sql += " and remaining_term = '" + params.getModel().getRemainingTerm() +"'";
		}
		sql += " order by deal_date desc, crt_dt desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		return super.update("INSERT INTO dws_month_nav_inf(deal_date,prdc_cd_pbc,prdc_cd,unt_nav,acm_nav,rct_1m_grw_rat,crt_dt,crt_tm,remaining_days,remaining_term) VALUES($S{dealDate},$S{prdcCdPbc},$S{prdcCd},$D{untNav},$D{acmNav},$D{rct1mGrwRat},$S{crtDt},$S{crtTm},$S{remainingDays},$S{remainingTerm})",
				params.getModel());
	}
	
	public UpdateResult updateDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		return super.update("UPDATE dws_month_nav_inf SET deal_date=$S{dealDate} ,prdc_cd_pbc=$S{prdcCdPbc} ,prdc_cd=$S{prdcCd} ,unt_nav=$D{untNav} ,acm_nav=$D{acmNav} ,rct_1m_grw_rat=$D{rct1mGrwRat} ,crt_dt=$S{crtDt} ,crt_tm=$S{crtTm} ,remaining_days=$S{remainingDays} ,remaining_term=$S{remainingTerm}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsMonthNavInf(SqlParam<DwsMonthNavInf> params) throws Exception {
		return super.update("DELETE FROM dws_month_nav_inf WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsMonthNavInf(DwsMonthNavInf params) throws Exception {
		return super.update("DELETE FROM dws_month_nav_inf WHERE deal_date=$S{dealDate}", params);
	}

}
