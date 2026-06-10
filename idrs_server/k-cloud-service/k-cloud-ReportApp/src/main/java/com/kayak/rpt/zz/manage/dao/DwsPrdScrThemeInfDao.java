package com.kayak.rpt.zz.manage.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.DwsPrdScrThemeInf;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DwsPrdScrThemeInfDao extends ComnDao {

	public SqlResult<DwsPrdScrThemeInf> findDwsPrdScrThemeInfs(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return super.findRows("SELECT id,prod_cd,scr_cd,ass_debt_type,amount,invest_ways,mid_num,mid_scr_cd,report_date,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,in_ashare_repo,ex_ashare_repo,isoverdue,overdue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2 FROM dws_prd_scr_theme_inf", params);
	}

	public UpdateResult addDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return super.update("INSERT INTO dws_prd_scr_theme_inf(id,prod_cd,scr_cd,ass_debt_type,amount,invest_ways,mid_num,mid_scr_cd,report_date,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,in_ashare_repo,ex_ashare_repo,isoverdue,overdue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2) VALUES($AUTOIDI{id},$S{prodCd},$S{scrCd},$S{assDebtType},$D{amount},$S{investWays},$I{midNum},$S{midScrCd},$S{reportDate},$S{endDate},$I{rdmTrm},$S{scrTrm},$S{rateLevel},$S{spcBondF},$S{inAshareRepo},$S{exAshareRepo},$S{isoverdue},$D{overdueAmt},$S{isfintech},$S{isgreen},$S{isinclusive},$S{ispension},$S{isdigital},$S{ispollution},$S{cmpBlgZon},$S{proBlgZon},$S{cmpNm},$S{cmpSocialCd},$S{cmpBlgFintech},$S{fintechTyp1},$S{fintechTyp2},$S{fintechTyp3},$S{fintechTyp4},$S{fintechTyp5},$S{fintechTyp6},$S{fintechTyp7},$S{fintechTyp8},$S{cmpBlgGreen},$S{cmpBlgInclusive},$S{inclusiveTyp1},$S{inclusiveTyp2},$S{cmpBlgPension},$S{cmpBlgDigital},$S{digitalTyp1},$S{digitalTyp2})",
				params.getModel());
	}
	
	public UpdateResult updateDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return super.update("UPDATE dws_prd_scr_theme_inf SET prod_cd=$S{prodCd} ,scr_cd=$S{scrCd} ,ass_debt_type=$S{assDebtType} ,amount=$D{amount} ,invest_ways=$S{investWays} ,mid_num=$I{midNum} ,mid_scr_cd=$S{midScrCd} ,report_date=$S{reportDate} ,end_date=$S{endDate} ,rdm_trm=$I{rdmTrm} ,scr_trm=$S{scrTrm} ,rate_level=$S{rateLevel} ,spc_bond_f=$S{spcBondF} ,in_ashare_repo=$S{inAshareRepo} ,ex_ashare_repo=$S{exAshareRepo} ,isoverdue=$S{isoverdue} ,overdue_amt=$D{overdueAmt} ,isfintech=$S{isfintech} ,isgreen=$S{isgreen} ,isinclusive=$S{isinclusive} ,ispension=$S{ispension} ,isdigital=$S{isdigital} ,ispollution=$S{ispollution} ,cmp_blg_zon=$S{cmpBlgZon} ,pro_blg_zon=$S{proBlgZon} ,cmp_nm=$S{cmpNm} ,cmp_social_cd=$S{cmpSocialCd} ,cmp_blg_fintech=$S{cmpBlgFintech} ,fintech_typ1=$S{fintechTyp1} ,fintech_typ2=$S{fintechTyp2} ,fintech_typ3=$S{fintechTyp3} ,fintech_typ4=$S{fintechTyp4} ,fintech_typ5=$S{fintechTyp5} ,fintech_typ6=$S{fintechTyp6} ,fintech_typ7=$S{fintechTyp7} ,fintech_typ8=$S{fintechTyp8} ,cmp_blg_green=$S{cmpBlgGreen} ,cmp_blg_inclusive=$S{cmpBlgInclusive} ,inclusive_typ1=$S{inclusiveTyp1} ,inclusive_typ2=$S{inclusiveTyp2} ,cmp_blg_pension=$S{cmpBlgPension} ,cmp_blg_digital=$S{cmpBlgDigital} ,digital_typ1=$S{digitalTyp1} ,digital_typ2=$S{digitalTyp2}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return super.update("DELETE FROM dws_prd_scr_theme_inf WHERE  id=$I{id} ",
				params.getModel());
	}

}
