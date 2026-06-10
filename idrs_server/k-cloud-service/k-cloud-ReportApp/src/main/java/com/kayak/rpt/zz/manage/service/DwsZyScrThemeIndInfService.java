package com.kayak.rpt.zz.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.DwsZyScrThemeIndInfDao;
import com.kayak.rpt.zz.manage.model.DwsZyScrThemeIndInf;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "自营资产打标中间表服务", model = DwsZyScrThemeIndInf.class)
public class DwsZyScrThemeIndInfService {

	@Autowired
	private DwsZyScrThemeIndInfDao dwsZyScrThemeIndInfDao;

	@API(desc = "查询自营资产打标中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsZyScrThemeIndInf> findDwsZyScrThemeIndInfs(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		params.setMakeSql(true);
		return dwsZyScrThemeIndInfDao.findDwsZyScrThemeIndInfs(params);
	}

	@API(desc = "添加自营资产打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int addDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return dwsZyScrThemeIndInfDao.addDwsZyScrThemeIndInf(params).getEffect();
	}
	
	@API(desc = "修改自营资产打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int updateDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return dwsZyScrThemeIndInfDao.updateDwsZyScrThemeIndInf(params).getEffect();
	}
	
	@API(desc = "删除自营资产打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int deleteDwsZyScrThemeIndInf(SqlParam<DwsZyScrThemeIndInf> params) throws Exception {
		return dwsZyScrThemeIndInfDao.deleteDwsZyScrThemeIndInf(params).getEffect();
	}

	@API(desc = "导入", params = "id,report_date,scr_cd,ass_debt_type,amount,end_date,rdm_trm,scr_trm,rate_level,spc_bond_f,isoverdue,overdue_amt,accrue_amt,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public void importDwsZyScrThemeIndInf(List<DwsZyScrThemeIndInf> dwsZyScrThemeIndInfs, Map<String, Object> params) throws Exception {
		dwsZyScrThemeIndInfDao.importDwsZyScrThemeIndInf(dwsZyScrThemeIndInfs, params);
	}

}
