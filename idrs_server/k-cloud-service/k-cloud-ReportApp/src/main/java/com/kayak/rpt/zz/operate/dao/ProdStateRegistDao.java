package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.ProdStateRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

@Repository
public class ProdStateRegistDao extends ComnDao {

	public SqlResult<ProdStateRegist> findProdStateRegists(SqlParam<ProdStateRegist> params) throws Exception {
		String sql = "SELECT T1.PROD_CODE,T1.BANK_CODE,T1.PROD_REG_ENC,T1.TOT_ASSETS,T1.RATE,T1.VALDATE,T1.DETAILS,T1.CREATE_DATE,T1.CREATE_TIME,T1.THEORY_REPORT_START_DATE,T1.THEORY_REPORT_END_DATE,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.SUMMIT_USER,OP_TYPE,T1.ID FROM app_prod_state_regist_info_remark T1 where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		if (Strings.isNotBlank(params.getModel().getValStartDate())) {
			sql += " and DATE(valdate) >= DATE($S{valStartDate}) and DATE(valdate) <= DATE($S{valEndDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addProdStateRegist(ProdStateRegist ProdStateRegist) throws Exception {
		return super.update("insert into app_prod_state_regist_info_remark(bank_code ,prod_code,prod_reg_enc ,tot_assets ,rate ,valdate ,details ,create_date,create_time ,theory_report_start_date ,theory_report_end_date ,register_serno ,imp_date ,register_date ,register_status ,op_type,summit_user) VALUES($S{bankCode} ,$S{prodCode},$S{prodRegEnc} ,$D{totAssets} ,$D{rate} ,$S{valdate} ,$S{details} ,date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{theoryReportStartDate} ,$S{theoryReportEndDate} ,(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual) ,$S{impDate} ,$S{registerDate} ,$S{registerStatus} ,$S{opType},$S{summitUser})",
				DataSourceProperty.PUB,ProdStateRegist);
	}

	public UpdateResult addImportProdStateRegist(ProdStateRegist ProdStateRegist) throws Exception {
		return super.update("insert into app_prod_state_regist_info_remark(bank_code ,prod_code ,tot_assets ,rate ,valdate ,details ,create_date ,theory_report_start_date ,theory_report_end_date ,register_serno ,imp_date ,register_date ,register_status ,op_type,summit_user) VALUES($S{bankCode} ,$S{prodCode} ,$D{totAssets} ,$D{rate} ,$S{valdate} ,$S{details} ,date_format(CURDATE(),'%Y%m%d') ,$S{theoryReportStartDate} ,$S{theoryReportEndDate} ,(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual) ,$S{impDate} ,$S{registerDate} ,$S{registerStatus} ,$S{opType},$S{summitUser})",
				DataSourceProperty.PUB,ProdStateRegist);
	}
}
