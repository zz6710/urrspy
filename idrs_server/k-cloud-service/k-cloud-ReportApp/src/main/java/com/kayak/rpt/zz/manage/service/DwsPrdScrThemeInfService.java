package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.DwsPrdScrThemeInfDao;
import com.kayak.rpt.zz.manage.model.DwsPrdScrThemeInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

@Service
@APIDefine(desc = "产品维度打标中间表服务", model = DwsPrdScrThemeInf.class)
public class DwsPrdScrThemeInfService {

	@Autowired
	private DwsPrdScrThemeInfDao dwsPrdScrThemeInfDao;

	@API(desc = "查询产品维度打标中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsPrdScrThemeInf> findDwsPrdScrThemeInfs(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		params.setMakeSql(true);
		return dwsPrdScrThemeInfDao.findDwsPrdScrThemeInfs(params);
	}

	@API(desc = "添加产品维度打标中间表", params = "id,prod_cd,scr_cd,ass_debt_type,amount,invest_ways,mid_num,mid_scr_cd,report_date,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,in_ashare_repo,ex_ashare_repo,isoverdue,overdue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2", auth = APIAuth.NO)
	public int addDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return dwsPrdScrThemeInfDao.addDwsPrdScrThemeInf(params).getEffect();
	}
	
	@API(desc = "修改产品维度打标中间表", params = "id,prod_cd,scr_cd,ass_debt_type,amount,invest_ways,mid_num,mid_scr_cd,report_date,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,in_ashare_repo,ex_ashare_repo,isoverdue,overdue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2", auth = APIAuth.NO)
	public int updateDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return dwsPrdScrThemeInfDao.updateDwsPrdScrThemeInf(params).getEffect();
	}
	
	@API(desc = "删除产品维度打标中间表", params = "id,prod_cd,scr_cd,ass_debt_type,amount,invest_ways,mid_num,mid_scr_cd,report_date,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,in_ashare_repo,ex_ashare_repo,isoverdue,overdue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2", auth = APIAuth.NO)
	public int deleteDwsPrdScrThemeInf(SqlParam<DwsPrdScrThemeInf> params) throws Exception {
		return dwsPrdScrThemeInfDao.deleteDwsPrdScrThemeInf(params).getEffect();
	}

}
