package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsMonthInvRaise;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsMonthInvRaiseDao extends ComnDao {

	public SqlResult<DwsMonthInvRaise> findDwsMonthInvRaises(SqlParam<DwsMonthInvRaise> params) throws Exception {
	    String sql = "SELECT id,deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm" +
    				" FROM dws_month_inv_raise" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getId())) {
            sql += " and id = " + params.getModel().getId() + "";
        }
		if (Tools.isNotEmpty(params.getModel().getDealDate())) {
            sql += " and deal_date like '" + params.getModel().getDealDate() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCdPbc())) {
            sql += " and prdc_cd_pbc like '%" + params.getModel().getPrdcCdPbc() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCd())) {
            sql += " and prdc_cd like '%" + params.getModel().getPrdcCd() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getZonCd())) {
            sql += " and zon_cd like '%" + params.getModel().getZonCd() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvTyp())) {
            sql += " and inv_typ like '%" + params.getModel().getInvTyp() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getOrgnInvType())) {
            sql += " and orgn_inv_type like '%" + params.getModel().getOrgnInvType() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getBusiType())) {
            sql += " and busi_type like '%" + params.getModel().getBusiType() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getHoldAmt())) {
            sql += " and hold_amt like '%" + params.getModel().getHoldAmt() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getHoldVol())) {
            sql += " and hold_vol like '%" + params.getModel().getHoldVol() +"%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDt())) {
            sql += " and crt_dt = '" + params.getModel().getCrtDt() +"'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtTm())) {
            sql += " and crt_tm = '" + params.getModel().getCrtTm() +"'";
        }
		sql += " order by deal_date desc, crt_dt desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return super.update("INSERT INTO dws_month_inv_raise(deal_date,prdc_cd_pbc,prdc_cd,zon_cd,inv_typ,orgn_inv_type,busi_type,hold_amt,hold_vol,crt_dt,crt_tm) VALUES($S{dealDate},$S{prdcCdPbc},$S{prdcCd},$S{zonCd},$S{invTyp},$S{orgnInvType},$S{busiType},$D{holdAmt},$D{holdVol},$S{crtDt},$S{crtTm})",
				params.getModel());
	}
	
	public UpdateResult updateDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return super.update("UPDATE dws_month_inv_raise SET deal_date=$S{dealDate} ,prdc_cd_pbc=$S{prdcCdPbc} ,prdc_cd=$S{prdcCd} ,zon_cd=$S{zonCd} ,inv_typ=$S{invTyp} ,orgn_inv_type=$S{orgnInvType} ,busi_type=$S{busiType} ,hold_amt=$D{holdAmt} ,hold_vol=$D{holdVol} ,crt_dt=$S{crtDt} ,crt_tm=$S{crtTm}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsMonthInvRaise(SqlParam<DwsMonthInvRaise> params) throws Exception {
		return super.update("DELETE FROM dws_month_inv_raise WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsMonthInvRaise(DwsMonthInvRaise params) throws Exception {
		return super.update("DELETE FROM dws_month_inv_raise WHERE  deal_date=$S{dealDate} ",
				params);
	}

}
