package com.kayak.subject.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.DwdProdFsfaConfirmDao;
import com.kayak.subject.model.DwdProdFsfaConfirm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

@Service
@APIDefine(desc = "日间导入估值表数据查询服务", model = DwdProdFsfaConfirm.class)
public class DwdProdFsfaConfirmService {

	@Autowired
	private DwdProdFsfaConfirmDao dwdProdFsfaConfirmDao;

	@API(desc = "查询日间导入估值表数据查询信息", auth = APIAuth.YES)
	public SqlResult<DwdProdFsfaConfirm> findDwdProdFsfaConfirms(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		params.setMakeSql(true);
		return dwdProdFsfaConfirmDao.findDwdProdFsfaConfirms(params);
	}

	@API(desc = "添加日间导入估值表数据查询", params = "id,deal_date,prdc_cd,prdc_nm,dt_dt,gzb_stt,gnrt_tm,opt_naem", auth = APIAuth.NO)
	public int addDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return dwdProdFsfaConfirmDao.addDwdProdFsfaConfirm(params).getEffect();
	}
	
	@API(desc = "修改日间导入估值表数据查询", params = "id,deal_date,prdc_cd,prdc_nm,dt_dt,gzb_stt,gnrt_tm,opt_naem", auth = APIAuth.NO)
	public int updateDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return dwdProdFsfaConfirmDao.updateDwdProdFsfaConfirm(params).getEffect();
	}
	
	@API(desc = "删除日间导入估值表数据查询", params = "id,deal_date,prdc_cd,prdc_nm,dt_dt,gzb_stt,gnrt_tm,opt_naem", auth = APIAuth.NO)
	public int deleteDwdProdFsfaConfirm(SqlParam<DwdProdFsfaConfirm> params) throws Exception {
		return dwdProdFsfaConfirmDao.deleteDwdProdFsfaConfirm(params).getEffect();
	}

}
