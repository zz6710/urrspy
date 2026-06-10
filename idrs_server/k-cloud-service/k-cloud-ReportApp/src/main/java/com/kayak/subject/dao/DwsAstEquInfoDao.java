package com.kayak.subject.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsAstEquInfo;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsAstEquInfoDao extends ComnDao {

	public SqlResult<DwsAstEquInfo> findDwsAstEquInfos(SqlParam<DwsAstEquInfo> params) throws Exception {
	    String sql = "SELECT distinct id,prod_cd,prod_intr_cd,ast_cd,ast_thr_cd,ast_thr_nm,ast_typ_dbt_pjt,zon_cd,dbt_nm,dbt_typ,dbt_cd,idt_typ,inv_icm_pct,etp_scl,opn_dt,end_dt,exp_end_dt,intr_rt_typ,intr_rt," +
				"gur_typ,ccy_cd,intr_amt_bal,amt_bal,rgt_trd_plc,rgt_trd_plc_cd,shr_hld_inv_typ,shr_hld_tsf_cd,shr_hld_tsf_nm,inv_out_typ,act_dt,crt_date,crt_time,upd_date,upd_time, tech_flag, green_flag, spec_flag, aged_flag, num_core_flag, trans_org_out_table_f, trans_org_buy_back_f, base_asset_trans_dep,base_asset_ori_prot_amt,base_asset_inv_obj_idt,base_asset_inv_obj_scale " +
    				" FROM dws_ast_equ_info" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getProdCd())) {
            sql += " and prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdIntrCd())) {
            sql += " and prod_intr_cd like '%" + params.getModel().getProdIntrCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAstCd())) {
            sql += " and ast_cd like '%" + params.getModel().getAstCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAstThrCd())) {
            sql += " and ast_thr_cd = '" + params.getModel().getAstThrCd() + "' ";
        }
		if (Tools.isNotEmpty(params.getModel().getAstThrNm())) {
            sql += " and ast_thr_nm like '%" + params.getModel().getAstThrNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getDbtNm())) {
            sql += " and dbt_nm like '%" + params.getModel().getDbtNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getDbtCd())) {
            sql += " and dbt_cd like '%" + params.getModel().getDbtCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getOpnDt())) {
            sql += " and opn_dt like '%" + params.getModel().getOpnDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getEndDt())) {
            sql += " and end_dt like '%" + params.getModel().getEndDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIntrRtTyp())) {
            sql += " and intr_rt_typ like '%" + params.getModel().getIntrRtTyp() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIntrRt())) {
            sql += " and intr_rt like '%" + params.getModel().getIntrRt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCcyCd())) {
            sql += " and ccy_cd like '%" + params.getModel().getCcyCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getIntrAmtBal())) {
            sql += " and intr_amt_bal like '%" + params.getModel().getIntrAmtBal() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAmtBal())) {
            sql += " and amt_bal like '%" + params.getModel().getAmtBal() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
            sql += " and act_dt like '" + params.getModel().getActDt() + "%'";
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

		if (Tools.isNotEmpty(params.getModel().getTechFlag())) {
			sql += " and tech_flag = '" + params.getModel().getTechFlag() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getGreenFlag())) {
			sql += " and green_flag = '" + params.getModel().getGreenFlag() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getSpecFlag())) {
			sql += " and spec_flag = '" + params.getModel().getSpecFlag() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getAgedFlag())) {
			sql += " and aged_flag = '" + params.getModel().getAgedFlag() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getNumCoreFlag())) {
			sql += " and num_core_flag = '" + params.getModel().getNumCoreFlag() + "'";
		}

		sql += " order by act_dt desc, crt_date desc";
        return super.findRows(sql, params);
	}

	public UpdateResult addDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		return super.update("INSERT INTO dws_ast_equ_info(prod_cd,prod_intr_cd,ast_cd,ast_thr_cd,ast_thr_nm,ast_typ_dbt_pjt,zon_cd,dbt_nm,dbt_typ,dbt_cd,idt_typ,inv_icm_pct,etp_scl,opn_dt,end_dt,exp_end_dt,intr_rt_typ,intr_rt,gur_typ,ccy_cd,intr_amt_bal,amt_bal,rgt_trd_plc,rgt_trd_plc_cd,shr_hld_inv_typ,shr_hld_tsf_cd,shr_hld_tsf_nm,inv_out_typ,act_dt,crt_date,crt_time,upd_date,upd_time,tech_flag,green_flag,spec_flag,aged_flag,num_core_flag, trans_org_out_table_f, trans_org_buy_back_f, base_asset_trans_dep,base_asset_ori_prot_amt,base_asset_inv_obj_idt,base_asset_inv_obj_scale)" +
						" VALUES($S{prodCd},$S{prodIntrCd},$S{astCd},$S{astThrCd},$S{astThrNm},$S{astTypDbtPjt},$S{zonCd},$S{dbtNm},$S{dbtTyp},$S{dbtCd},$S{idtTyp},$S{invIcmPct},$S{etpScl},$S{opnDt},$S{endDt},$S{expEndDt},$S{intrRtTyp},$D{intrRt},$S{gurTyp},$S{ccyCd},$D{intrAmtBal},$D{amtBal},$S{rgtTrdPlc},$S{rgtTrdPlcCd},$S{shrHldInvTyp},$S{shrHldTsfCd},$S{shrHldTsfNm},$S{invOutTyp},$S{actDt},$D{crtDate},$S{crtTime},$D{updDate},$S{updTime},$S{techFlag},$S{greenFlag},$S{specFlag},$S{agedFlag},$S{numCoreFlag},$S{transOrgOutTableF},$S{transOrgBuyBackF},$S{baseAssetTransDep},$D{baseAssetOriProtAmt},$S{baseAssetInvObjIdt},$S{baseAssetInvObjScale})",
				params.getModel());
	}
	
	public UpdateResult updateDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		return super.update("UPDATE dws_ast_equ_info SET prod_cd=$S{prodCd} ,prod_intr_cd=$S{prodIntrCd} ,ast_cd=$S{astCd} ,ast_thr_cd=$S{astThrCd} ,ast_thr_nm=$S{astThrNm} ,ast_typ_dbt_pjt = $S{astTypDbtPjt} ,zon_cd = $S{zonCd} ,dbt_nm=$S{dbtNm} ,dbt_typ = $S{dbtTyp} ,dbt_cd=$S{dbtCd} ,idt_typ = $S{idtTyp} ,inv_icm_pct = $S{invIcmPct} ,etp_scl = $S{etpScl} ,opn_dt=$S{opnDt} ,end_dt=$S{endDt} ,exp_end_dt = $S{expEndDt} ,intr_rt_typ=$S{intrRtTyp} ,intr_rt=$D{intrRt} ,gur_typ = $S{gurTyp} ,ccy_cd=$S{ccyCd} ,intr_amt_bal=$D{intrAmtBal} ,amt_bal=$D{amtBal} ,rgt_trd_plc = $S{rgtTrdPlc} ,rgt_trd_plc_cd = $S{rgtTrdPlcCd} ,shr_hld_inv_typ = $S{shrHldInvTyp} ,shr_hld_tsf_cd = $S{shrHldTsfCd} ,shr_hld_tsf_nm = $S{shrHldTsfNm} ,inv_out_typ = $S{invOutTyp} ,act_dt=$S{actDt} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime},tech_flag=$S{techFlag},green_flag=$S{greenFlag},spec_flag=$S{specFlag},aged_flag=$S{agedFlag},num_core_flag=$S{numCoreFlag},trans_org_out_table_f=$S{transOrgOutTableF},trans_org_buy_back_f=$S{transOrgBuyBackF},base_asset_trans_dep=$S{baseAssetTransDep},base_asset_ori_prot_amt = $D{baseAssetOriProtAmt},base_asset_inv_obj_idt=$S{baseAssetInvObjIdt},base_asset_inv_obj_scale=$S{baseAssetInvObjScale}  WHERE id=$I{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsAstEquInfo(SqlParam<DwsAstEquInfo> params) throws Exception {
		return super.update("DELETE FROM dws_ast_equ_info WHERE id=$I{id}",
				params.getModel());
	}

	public UpdateResult deleteDwsAstEquInfo(DwsAstEquInfo params) throws Exception {
		return super.update("DELETE FROM dws_ast_equ_info WHERE act_dt = $S{actDt}",
				params);
	}

}
