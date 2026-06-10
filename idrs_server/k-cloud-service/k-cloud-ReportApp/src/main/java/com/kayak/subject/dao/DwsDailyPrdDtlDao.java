package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsDailyPrdDtl;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsDailyPrdDtlDao extends ComnDao {

	public SqlResult<DwsDailyPrdDtl> findDwsDailyPrdDtls(SqlParam<DwsDailyPrdDtl> params) throws Exception {
	    String sql = "SELECT id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm" +
    				" FROM dws_daily_prd_dtl" +
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
		if (Tools.isNotEmpty(params.getModel().getSspAmt())) {
            sql += " and ssp_amt like '%" + params.getModel().getSspAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getSsbAmt())) {
            sql += " and ssb_amt like '%" + params.getModel().getSsbAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getRdmAmt())) {
            sql += " and rdm_amt like '%" + params.getModel().getRdmAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getExpAmt())) {
            sql += " and exp_amt like '%" + params.getModel().getExpAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getSbtAmtF())) {
            sql += " and sbt_amt_f like '%" + params.getModel().getSbtAmtF() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvYldAmtDly())) {
            sql += " and inv_yld_amt_dly like '%" + params.getModel().getInvYldAmtDly() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getRdmYldAmt())) {
            sql += " and rdm_yld_amt like '%" + params.getModel().getRdmYldAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getExpYldAmt())) {
            sql += " and exp_yld_amt like '%" + params.getModel().getExpYldAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getShrSbtYldAmtF())) {
            sql += " and shr_sbt_yld_amt_f like '%" + params.getModel().getShrSbtYldAmtF() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCshDvd())) {
            sql += " and csh_dvd like '%" + params.getModel().getCshDvd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getShrRvtDvd())) {
            sql += " and shr_rvt_dvd like '%" + params.getModel().getShrRvtDvd() + "%'";
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

	public UpdateResult addDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return super.update("INSERT INTO dws_daily_prd_dtl(id,deal_date,prdc_cd,prdc_nm,mother_prdc_cd,ssp_amt,ssb_amt,rdm_amt,exp_amt,sbt_amt_f,inv_yld_amt_dly,rdm_yld_amt,exp_yld_amt,shr_sbt_yld_amt_f,csh_dvd,shr_rvt_dvd,crt_dt,crt_tm) VALUES($AUTOIDI{id},$S{dealDate},$S{prdcCd},$S{prdcNm},$S{motherPrdcCd},$D{sspAmt},$D{ssbAmt},$D{rdmAmt},$D{expAmt},$D{sbtAmtF},$D{invYldAmtDly},$D{rdmYldAmt},$D{expYldAmt},$D{shrSbtYldAmtF},$D{cshDvd},$D{shrRvtDvd},$S{crtDt},$S{crtTm})",
				params.getModel());
	}
	
	public UpdateResult updateDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return super.update("UPDATE dws_daily_prd_dtl SET deal_date=$S{dealDate} ,prdc_cd=$S{prdcCd} ,prdc_nm=$S{prdcNm} ,mother_prdc_cd=$S{motherPrdcCd} ,ssp_amt=$D{sspAmt} ,ssb_amt=$D{ssbAmt} ,rdm_amt=$D{rdmAmt} ,exp_amt=$D{expAmt} ,sbt_amt_f=$D{sbtAmtF} ,inv_yld_amt_dly=$D{invYldAmtDly} ,rdm_yld_amt=$D{rdmYldAmt} ,exp_yld_amt=$D{expYldAmt} ,shr_sbt_yld_amt_f=$D{shrSbtYldAmtF} ,csh_dvd=$D{cshDvd} ,shr_rvt_dvd=$D{shrRvtDvd} ,crt_dt=$S{crtDt} ,crt_tm=$S{crtTm}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsDailyPrdDtl(SqlParam<DwsDailyPrdDtl> params) throws Exception {
		return super.update("DELETE FROM dws_daily_prd_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

    public UpdateResult deleteDwsDailyPrdDtl(DwsDailyPrdDtl params) throws Exception {
        return super.update("DELETE FROM dws_daily_prd_dtl WHERE deal_date = $S{dealDate}",
                params);
    }

}
