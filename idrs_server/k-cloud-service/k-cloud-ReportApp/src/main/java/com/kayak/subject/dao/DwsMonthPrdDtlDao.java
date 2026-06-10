package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsMonthPrdDtl;
import org.springframework.stereotype.Repository;

@Repository
public class DwsMonthPrdDtlDao extends ComnDao {

	public SqlResult<DwsMonthPrdDtl> findDwsMonthPrdDtls(SqlParam<DwsMonthPrdDtl> params) throws Exception {
	    String sql = "SELECT id,deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq" +
    				" FROM dws_month_prd_dtl" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getId())) {
            sql += " and id = '" + params.getModel().getId() + "'";
        }
		if (Tools.isNotEmpty(params.getModel().getDealDate())) {
            sql += " and deal_date like '" + params.getModel().getDealDate() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdcCd())) {
            sql += " and prdc_cd like '%" + params.getModel().getPrdcCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getTotRaiseTt())) {
            sql += " and tot_raise_tt like '%" + params.getModel().getTotRaiseTt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getNetRaiseTt())) {
            sql += " and net_raise_tt like '%" + params.getModel().getNetRaiseTt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getBalEt())) {
            sql += " and bal_et like '%" + params.getModel().getBalEt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCurPayTt())) {
            sql += " and cur_pay_tt like '%" + params.getModel().getCurPayTt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvAcvYieldTt())) {
            sql += " and inv_acv_yield_tt like '%" + params.getModel().getInvAcvYieldTt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getBnkAcvYieldTt())) {
            sql += " and bnk_acv_yield_tt like '%" + params.getModel().getBnkAcvYieldTt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getRaiseType())) {
            sql += " and raise_type like '%" + params.getModel().getRaiseType() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvProp())) {
            sql += " and inv_prop like '%" + params.getModel().getInvProp() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getOptMod())) {
            sql += " and opt_mod like '%" + params.getModel().getOptMod() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPrdTrm())) {
            sql += " and prd_trm like '%" + params.getModel().getPrdTrm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldNtr())) {
            sql += " and inv_hld_ntr like '%" + params.getModel().getInvHldNtr() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldLgOrg())) {
            sql += " and inv_hld_lg_org like '%" + params.getModel().getInvHldLgOrg() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldNonFncOrg())) {
            sql += " and inv_hld_non_fnc_org like '%" + params.getModel().getInvHldNonFncOrg() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldBnkFncOrg())) {
            sql += " and inv_hld_bnk_fnc_org like '%" + params.getModel().getInvHldBnkFncOrg() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldIsrFncOrg())) {
            sql += " and inv_hld_isr_fnc_org like '%" + params.getModel().getInvHldIsrFncOrg() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldTstCpn())) {
            sql += " and inv_hld_tst_cpn like '%" + params.getModel().getInvHldTstCpn() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldSctCpn())) {
            sql += " and inv_hld_sct_cpn like '%" + params.getModel().getInvHldSctCpn() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldFndCpn())) {
            sql += " and inv_hld_fnd_cpn like '%" + params.getModel().getInvHldFndCpn() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldOtrFncOrg())) {
            sql += " and inv_hld_otr_fnc_org like '%" + params.getModel().getInvHldOtrFncOrg() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getInvHldFncOrgPrd())) {
            sql += " and inv_hld_fnc_org_prd like '%" + params.getModel().getInvHldFncOrgPrd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCurCny())) {
            sql += " and cur_cny like '%" + params.getModel().getCurCny() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCurUsd())) {
            sql += " and cur_usd like '%" + params.getModel().getCurUsd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCurEur())) {
            sql += " and cur_eur like '%" + params.getModel().getCurEur() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCurOtr())) {
            sql += " and cur_otr like '%" + params.getModel().getCurOtr() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCshMngF())) {
            sql += " and csh_mng_f like '%" + params.getModel().getCshMngF() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getMngMth())) {
            sql += " and mng_mth like '%" + params.getModel().getMngMth() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getVltnMthd())) {
            sql += " and vltn_mthd like '%" + params.getModel().getVltnMthd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getBlgFinSamBusF())) {
            sql += " and blg_fin_sam_bus_f like '%" + params.getModel().getBlgFinSamBusF() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAvgRmnTrm())) {
            sql += " and avg_rmn_trm like '%" + params.getModel().getAvgRmnTrm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIsProdTsf())) {
            sql += " and is_prod_tsf like '%" + params.getModel().getIsProdTsf() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getTsfFndAmt())) {
            sql += " and tsf_fnd_amt like '%" + params.getModel().getTsfFndAmt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getPenInvPrdF())) {
            sql += " and pen_inv_prd_f like '%" + params.getModel().getPenInvPrdF() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getMthAnlYield())) {
            sql += " and mth_anl_yield like '%" + params.getModel().getMthAnlYield() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getWgtPrice())) {
            sql += " and wgt_price like '%" + params.getModel().getWgtPrice() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getRskLev())) {
            sql += " and rsk_lev like '%" + params.getModel().getRskLev() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIsSealPrdPast())) {
            sql += " and is_seal_prd_past like '%" + params.getModel().getIsSealPrdPast() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getFoundDt())) {
            sql += " and found_dt like '%" + params.getModel().getFoundDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getMtuDt())) {
            sql += " and mtu_dt like '%" + params.getModel().getMtuDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDt())) {
            sql += " and crt_dt = '" + params.getModel().getCrtDt() + "'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtTm())) {
            sql += " and crt_tm = '" + params.getModel().getCrtTm() + "'";
        }
		if (Tools.isNotEmpty(params.getModel().getYwrq())) {
            sql += " and ywrq like '%" + params.getModel().getYwrq() + "%'";
        }
        sql += " order by deal_date desc, crt_dt desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return super.update("INSERT INTO dws_month_prd_dtl(deal_date,prdc_cd,tot_raise_tt,net_raise_tt,bal_et,cur_pay_tt,inv_acv_yield_tt,bnk_acv_yield_tt,raise_type,inv_prop,opt_mod,prd_trm,inv_hld_ntr,inv_hld_lg_org,inv_hld_non_fnc_org,inv_hld_bnk_fnc_org,inv_hld_isr_fnc_org,inv_hld_tst_cpn,inv_hld_sct_cpn,inv_hld_fnd_cpn,inv_hld_otr_fnc_org,inv_hld_fnc_org_prd,cur_cny,cur_usd,cur_eur,cur_otr,csh_mng_f,mng_mth,vltn_mthd,blg_fin_sam_bus_f,avg_rmn_trm,is_prod_tsf,tsf_fnd_amt,pen_inv_prd_f,per_pen_inv_prod_f,mth_anl_yield,wgt_price,rsk_lev,is_seal_prd_past,found_dt,mtu_dt,crt_dt,crt_tm,ywrq) VALUES($S{dealDate},$S{prdcCd},$D{totRaiseTt},$D{netRaiseTt},$D{balEt},$D{curPayTt},$D{invAcvYieldTt},$D{bnkAcvYieldTt},$S{raiseType},$S{invProp},$S{optMod},$S{prdTrm},$D{invHldNtr},$D{invHldLgOrg},$D{invHldNonFncOrg},$D{invHldBnkFncOrg},$D{invHldIsrFncOrg},$D{invHldTstCpn},$D{invHldSctCpn},$D{invHldFndCpn},$D{invHldOtrFncOrg},$D{invHldFncOrgPrd},$D{curCny},$D{curUsd},$D{curEur},$D{curOtr},$S{cshMngF},$S{mngMth},$S{vltnMthd},$S{blgFinSamBusF},$S{avgRmnTrm},$S{isProdTsf},$S{tsfFndAmt},$S{penInvPrdF},$S{perPenInvProdF},$D{mthAnlYield},$D{wgtPrice},$S{rskLev},$S{isSealPrdPast},$S{foundDt},$S{mtuDt},$S{crtDt},$S{crtTm},$S{ywrq})",
				params.getModel());
	}
	
	public UpdateResult updateDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return super.update("UPDATE dws_month_prd_dtl SET deal_date=$S{dealDate} ,prdc_cd=$S{prdcCd} ,tot_raise_tt=$D{totRaiseTt} ,net_raise_tt=$D{netRaiseTt} ,bal_et=$D{balEt} ,cur_pay_tt=$D{curPayTt} ,inv_acv_yield_tt=$D{invAcvYieldTt} ,bnk_acv_yield_tt=$D{bnkAcvYieldTt} ,raise_type=$S{raiseType} ,inv_prop=$S{invProp} ,opt_mod=$S{optMod} ,prd_trm=$S{prdTrm} ,inv_hld_ntr=$D{invHldNtr} ,inv_hld_lg_org=$D{invHldLgOrg} ,inv_hld_non_fnc_org=$D{invHldNonFncOrg} ,inv_hld_bnk_fnc_org=$D{invHldBnkFncOrg} ,inv_hld_isr_fnc_org=$D{invHldIsrFncOrg} ,inv_hld_tst_cpn=$D{invHldTstCpn} ,inv_hld_sct_cpn=$D{invHldSctCpn} ,inv_hld_fnd_cpn=$D{invHldFndCpn} ,inv_hld_otr_fnc_org=$D{invHldOtrFncOrg} ,inv_hld_fnc_org_prd=$D{invHldFncOrgPrd} ,cur_cny=$D{curCny} ,cur_usd=$D{curUsd} ,cur_eur=$D{curEur} ,cur_otr=$D{curOtr} ,csh_mng_f=$S{cshMngF} ,mng_mth=$S{mngMth} ,vltn_mthd=$S{vltnMthd} ,blg_fin_sam_bus_f=$S{blgFinSamBusF} ,avg_rmn_trm=$S{avgRmnTrm} ,is_prod_tsf=$S{isProdTsf} ,tsf_fnd_amt=$S{tsfFndAmt} ,pen_inv_prd_f=$S{penInvPrdF} ,per_pen_inv_prod_f=$S{perPenInvProdF} ,mth_anl_yield=$D{mthAnlYield} ,wgt_price=$D{wgtPrice} ,rsk_lev=$S{rskLev} ,is_seal_prd_past=$S{isSealPrdPast} ,found_dt=$S{foundDt} ,mtu_dt=$S{mtuDt} ,crt_dt=$S{crtDt} ,crt_tm=$S{crtTm} ,ywrq=$S{ywrq}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsMonthPrdDtl(SqlParam<DwsMonthPrdDtl> params) throws Exception {
		return super.update("DELETE FROM dws_month_prd_dtl WHERE  id=$I{id} ",
				params.getModel());
	}

    public UpdateResult deleteDwsMonthPrdDtl(DwsMonthPrdDtl params) throws Exception {
        return super.update("DELETE FROM dws_month_prd_dtl WHERE deal_date = $S{dealDate}",
                params);
    }

}
