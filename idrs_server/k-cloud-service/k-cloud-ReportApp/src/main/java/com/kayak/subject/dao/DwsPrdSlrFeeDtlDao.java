package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsPrdSlrFeeDtl;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsPrdSlrFeeDtlDao extends ComnDao {

	public SqlResult<DwsPrdSlrFeeDtl> findDwsPrdSlrFeeDtls(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
	    String sql = "SELECT id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm" +
    				" FROM dws_prd_slr_fee_dtl" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getId())) {
            sql += " and id = " + params.getModel().getId() + "";
        }
		if (Tools.isNotEmpty(params.getModel().getDealDate())) {
            sql += " and deal_date = '" + params.getModel().getDealDate() + "'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCd())) {
            sql += " and prdc_cd like '%" + params.getModel().getPrdcCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcNm())) {
            sql += " and prdc_nm like '%" + params.getModel().getPrdcNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getMotherPrdcCd())) {
            sql += " and mother_prdc_cd like '%" + params.getModel().getMotherPrdcCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getFeeType())) {
            sql += " and fee_type like '%" + params.getModel().getFeeType() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getFeeAmt())) {
            sql += " and fee_amt like '%" + params.getModel().getFeeAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getSlrCd())) {
            sql += " and slr_cd like '%" + params.getModel().getSlrCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getSlrNm())) {
            sql += " and slr_nm like '%" + params.getModel().getSlrNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDt())) {
            sql += " and crt_dt = '" + params.getModel().getCrtDt() + "'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtTm())) {
            sql += " and crt_tm = '" + params.getModel().getCrtTm() + "'";
        }
		sql += " order by deal_date desc, crt_dt desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return super.update("INSERT INTO dws_prd_slr_fee_dtl(id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,fee_type,fee_amt,slr_cd,slr_nm,crt_dt,crt_tm) VALUES($AUTOIDI{id},$S{dealDate},$S{prdcCd},$S{prdcNm},$S{motherPrdcCd},$S{feeType},$D{feeAmt},$S{slrCd},$S{slrNm},$S{crtDt},$S{crtTm})",
				params.getModel());
	}
	
	public UpdateResult updateDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return super.update("UPDATE dws_prd_slr_fee_dtl SET deal_date=$S{dealDate} ,prdc_cd=$S{prdcCd} ,prdc_nm=$S{prdcNm} ,mother_prdc_cd=$S{motherPrdcCd} ,fee_type=$S{feeType} ,fee_amt=$D{feeAmt} ,slr_cd=$S{slrCd} ,slr_nm=$S{slrNm} ,crt_dt=$S{crtDt} ,crt_tm=$S{crtTm}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsPrdSlrFeeDtl(SqlParam<DwsPrdSlrFeeDtl> params) throws Exception {
		return super.update("DELETE FROM dws_prd_slr_fee_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteDwsPrdSlrFeeDtl(DwsPrdSlrFeeDtl params) throws Exception {
		return super.update("DELETE FROM dws_prd_slr_fee_dtl WHERE deal_date = $S{dealDate}",
				params);
	}

}
