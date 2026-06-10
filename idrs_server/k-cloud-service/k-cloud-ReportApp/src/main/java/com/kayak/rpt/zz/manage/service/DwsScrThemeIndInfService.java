package com.kayak.rpt.zz.manage.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.DwsScrThemeIndInfDao;
import com.kayak.rpt.zz.manage.model.DwsScrThemeIndInf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "资产维度打标中间表服务", model = DwsScrThemeIndInf.class)
public class DwsScrThemeIndInfService {

	@Autowired
	private DwsScrThemeIndInfDao dwsScrThemeIndInfDao;

	@API(desc = "查询资产维度打标中间表信息", auth = APIAuth.YES)
	public SqlResult<DwsScrThemeIndInf> findDwsScrThemeIndInfs(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		params.setMakeSql(true);
		return dwsScrThemeIndInfDao.findDwsScrThemeIndInfs(params);
	}

	@API(desc = "添加资产维度打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int addDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return dwsScrThemeIndInfDao.addDwsScrThemeIndInf(params).getEffect();
	}
	
	@API(desc = "修改资产维度打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public int updateDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return dwsScrThemeIndInfDao.updateDwsScrThemeIndInf(params).getEffect();
	}
	
	@API(desc = "删除资产维度打标中间表", params = "id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.NO)
	public int deleteDwsScrThemeIndInf(SqlParam<DwsScrThemeIndInf> params) throws Exception {
		return dwsScrThemeIndInfDao.deleteDwsScrThemeIndInf(params).getEffect();
	}

	@API(desc = "导入", params = "id,report_date,scr_cd,ass_debt_type,amount,isfintech,isgreen,isinclusive,ispension,isdigital,ispollution,cmp_blg_zon,pro_blg_zon,cmp_nm,cmp_social_cd,cmp_blg_fintech,fintech_typ1,fintech_typ2,fintech_typ3,fintech_typ4,fintech_typ5,fintech_typ6,fintech_typ7,fintech_typ8,cmp_blg_green,cmp_blg_inclusive,inclusive_typ1,inclusive_typ2,cmp_blg_pension,cmp_blg_digital,digital_typ1,digital_typ2,deal_date,crt_date,crt_time,upd_date,upd_time", auth = APIAuth.YES)
	public void importDwsScrThemeIndInf(List<DwsScrThemeIndInf> dwsScrThemeIndInfs, Map<String, Object> params) throws Exception {
		dwsScrThemeIndInfDao.importDwsScrThemeIndInf(dwsScrThemeIndInfs, params);
	}

}
