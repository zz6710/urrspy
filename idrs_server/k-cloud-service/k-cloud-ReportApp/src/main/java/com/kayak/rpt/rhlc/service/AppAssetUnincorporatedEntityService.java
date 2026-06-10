package com.kayak.rpt.rhlc.service;

import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhlc.dao.AppAssetUnincorporatedEntityDao;
import com.kayak.rpt.rhlc.model.AppAssetUnincorporatedEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;

import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "非法人财务数据服务", model = AppAssetUnincorporatedEntity.class)
public class AppAssetUnincorporatedEntityService {

	@Autowired
	private AppAssetUnincorporatedEntityDao appAssetUnincorporatedEntityDao;

	@API(desc = "查询非法人财务数据信息", auth = APIAuth.YES)
	public SqlResult<AppAssetUnincorporatedEntity> findAppAssetUnincorporatedEntitys(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		params.setMakeSql(true);
		return appAssetUnincorporatedEntityDao.findAppAssetUnincorporatedEntitys(params);
	}

	public List<SqlRow> queryAppAssetUnincorporatedEntitys(Map<String, Object> params) throws Exception {
		return appAssetUnincorporatedEntityDao.findAppAssetUnincorporatedEntitys(params);
	}

	@API(desc = "添加非法人财务数据", params = "id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version", auth = APIAuth.NO)
	public int addAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return appAssetUnincorporatedEntityDao.addAppAssetUnincorporatedEntity(params).getEffect();
	}

	@API(desc = "导入", params = "id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version", auth = APIAuth.YES)
	public void impAppAssetUnincorporatedEntity(List<AppAssetUnincorporatedEntity> appAssetUnincorporatedEntities, Map<String, Object> params) throws Exception {
		appAssetUnincorporatedEntityDao.impAppAssetUnincorporatedEntity(appAssetUnincorporatedEntities, params);
	}
	
	@API(desc = "修改非法人财务数据", params = "id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version", auth = APIAuth.NO)
	public int updateAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return appAssetUnincorporatedEntityDao.updateAppAssetUnincorporatedEntity(params).getEffect();
	}
	
	@API(desc = "删除非法人财务数据", params = "id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version", auth = APIAuth.NO)
	public int deleteAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return appAssetUnincorporatedEntityDao.deleteAppAssetUnincorporatedEntity(params).getEffect();
	}

	@API(desc = "生成报表数据", params = "id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version", auth = APIAuth.YES)
	public String reloadData(SqlParam<AppAssetUnincorporatedEntity> params) {
		try {
			appAssetUnincorporatedEntityDao.reloadData(params);
		} catch (Exception e) {
			return RequestSupport.updateReturnJson(false, "生成报表数据失败！失败原因："+e.getMessage(), null).toString();
		}
		return RequestSupport.updateReturnJson(true, "生成报表数据成功！", null).toString();
	}

}
