package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.manage.model.ProdStateRegistInfo;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ProdStateRegistInfoDao extends ComnDao {

	public SqlResult<ProdStateRegistInfo> findProdStateRegistInfos(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String sql = "SELECT T1.ID,T1.PROD_CODE,T1.BANK_CODE,T1.PROD_REG_ENC,T1.TOT_ASSETS,T1.RATE,T1.VALDATE,T1.DETAILS,T1.CREATE_DATE,T1.THEORY_REPORT_START_DATE,T1.THEORY_REPORT_END_DATE,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.sys_data_status,T1.sys_data_version,T1.sys_data_source,ARS.audit_status FROM app_prod_state_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_state_regist_info' where 1=1 and sys_data_status='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(valdate) = "+ params.getModel().getStartDate();
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
//			sql += " and prod_reg_enc=$S{prodRegEnc}"; //当model中定义了其他格式，比如模糊查询赋值，即使不makesql为false,此处再写自动拼接的等于，sql也会报错
			sql += " and T1.prod_reg_enc like '%"+ params.getModel().getProdRegEnc()+"%'";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql += " and T1.register_status = '"+params.getModel().getRegisterStatus()+"'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno like '" + params.getModel().getRegisterSerno() + "'";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public int findProdStateRegistInfosCount(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String sql = "SELECT count(1) FROM app_prod_state_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_state_regist_info' where  T1.sys_data_status='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(T1.valdate) = "+ params.getModel().getStartDate();
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and T1.prod_reg_enc like '%"+ params.getModel().getProdRegEnc()+"%'";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql += " and T1.register_status = '"+params.getModel().getRegisterStatus()+"'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno like '" + params.getModel().getRegisterSerno() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public SqlResult<ProdStateRegistInfo> findProdStateRegistInfoById(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String sql = "SELECT T1.PROD_CODE,T1.BANK_CODE,T1.PROD_REG_ENC,T1.TOT_ASSETS,T1.RATE,T1.VALDATE,T1.DETAILS,T1.CREATE_DATE,T1.THEORY_REPORT_START_DATE,T1.THEORY_REPORT_END_DATE,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.sys_data_status,T1.sys_data_version,T1.sys_data_source,ARS.audit_status FROM app_prod_state_regist_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_state_regist_info' where 1=1 and sys_data_status='1' ";
		if (Strings.isNotBlank(params.getModel().getId())) {
			sql += " and T1.id = '"+params.getModel().getId()+"'";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}
	public UpdateResult addProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		return super.update("insert into app_prod_state_regist_info(bank_code ,prod_code ,tot_assets ,rate ,valdate ,details ,create_date ,theory_report_start_date ,theory_report_end_date ,register_serno ,imp_date ,register_date ,register_status ,sys_data_source ,sys_data_status ,sys_data_version) VALUES($S{bankCode} ,$S{prodCode} ,$D{totAssets} ,$D{rate} ,$S{valdate} ,$S{details} ,date_format(CURDATE(),'%Y%m%d') ,$S{theoryReportStartDate} ,$S{theoryReportEndDate} ,(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual) ,$S{impDate} ,$S{registerDate} ,$S{registerStatus} ,$S{sysDataSource} ,$S{sysDataStatus} ,$S{sysDataVersion})",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		return super.update("UPDATE app_prod_state_regist_info SET bank_code=$S{bankCode},tot_assets=$D{totAssets},rate=$D{rate},valdate=$S{valdate},prod_code=$S{prodCode},details=$S{details}  WHERE register_serno=$S{registerSerno}",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult deleteProdStateRegistInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		return super.update("DELETE FROM app_prod_state_regist_info WHERE register_serno=$S{registerSerno}  " ,
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportProdStateRegistInfo(Object param) throws Exception {
		return super.update("insert into app_prod_state_regist_info(bank_code ,prod_reg_enc ,tot_assets ,rate ,valdate ,details ,create_date ,theory_report_start_date ,theory_report_end_date ,register_serno ,imp_date ,register_date ,register_status ,sys_data_source ,sys_data_status ,sys_data_version) VALUES($S{bankCode} ,$S{prodRegEnc} ,$D{totAssets} ,$D{rate} ,$S{valdate} ,$S{details} ,date_format(CURDATE(),'%Y%m%d') ,$S{valdate} ,$S{theoryReportEndDate} ,(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual) ,date_format(CURDATE(),'%Y%m%d') ,$S{registerDate} ,'0' ,$S{sysDataSource} ,'1' ,$S{sysDataVersion})",
				DataSourceProperty.PUB,param);
	}

	public UpdateResult deleteImportProdStateRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_prod_state_regist_info where theory_report_start_date = $S{theoryReportStartDate} ", params);
	}

	public SqlResult<ProdStateRegistInfo> selectProdStateRegistInfos(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String sql = "select t.bank_code ," +
				"t.prod_code ," +
				"t.tot_assets," +
				"t.rate ,"+
				"t.valdate,"+
				"t.details ,"+
				"t.register_serno ,"+
				"t.register_date ,"+
				"from app_prod_state_regist_info t " ;
		if (Strings.isNotBlank(params.getModel().getValdate())) {
			sql += "where valdate = $S{valdate}";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}
	public int findProdStateRegistInfoFailStatus(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String sql = "SELECT count(1) FROM";
		String reportDate = startDate;
		sql += " app_prod_state_regist_info";
		sql += " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_prod_state_regist_info' where T1.sys_data_status ='1' and T1.register_status in (0,1) ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(T1.valdate) = "+ params.getModel().getStartDate();
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and T1.prod_reg_enc like '%"+ params.getModel().getProdRegEnc()+"%'";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql += " and T1.register_status = '"+params.getModel().getRegisterStatus()+"'";
		}

		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno like '" + params.getModel().getRegisterSerno() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}
	public UpdateResult updateProdStateRegistInfoStatus(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String queryEndDate=params.getModel().getEndDate(); //日期必须单独拼接，避免因日期为空，错更新数据
		String queryStartDate=params.getModel().getStartDate();
		String sql = "update app_prod_state_regist_info SET register_status='3' WHERE sys_data_status='1'  and valdate='"+queryStartDate+"'";
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and prod_reg_enc like '%"+ params.getModel().getProdRegEnc()+"%'";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql += " and register_status = '"+params.getModel().getRegisterStatus()+"'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno like '" + params.getModel().getRegisterSerno() + "'";
		}
//		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
//			sql += " and register_status = $S{registerStatus}";
//		}
		return super.update(sql, DataSourceProperty.PUB,params.getModel());
	}
	public void updateBaseReportResultInfo(SqlParam<ProdStateRegistInfo> params) throws Exception {
		String queryEndDate=params.getModel().getEndDate();
		String queryStartDate=params.getModel().getStartDate();
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_prod_state_regist_info' and theory_report_start_date in (select theory_report_start_date from app_prod_state_regist_info where  valdate='"+queryStartDate+"' ) ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}
}
