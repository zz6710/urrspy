package com.kayak.rpt.zz.feedback.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.feedback.model.TrFileresults;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class TrFileresultsDao extends ComnDao {

	public SqlResult<TrFileresults> findTrFileresultss(SqlParam<TrFileresults> params) throws Exception {
		String sql="SELECT id,register_date,file_type,filename,fileno,register_serno,errormsg,errorcode,report_date,sys_table_name,crt_time FROM app_zz_file_results where 1=1 ";
		sql = sql+" order by id desc";
		return super.findRows(sql, params);
	}

	public UpdateResult addTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return super.update("INSERT INTO app_zz_file_results(register_date,file_type,filename,fileno,register_serno,errormsg,errorcode,report_date,sys_table_name) VALUES($S{registerDate},$S{fileType},$S{filename},$S{fileno},$S{registerSerno},$S{errormsg},$S{errorcode},$S{reportDate},$S{sysTableName})",
				params.getModel());
	}

	public UpdateResult updateTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return super.update("UPDATE app_zz_file_results SET register_date=$S{registerDate} ,file_type=$S{fileType} ,filename=$S{filename} ,fileno=$S{fileno} ,register_serno=$S{registerSerno} ,errormsg=$S{errormsg} ,errorcode=$S{errorcode}  WHERE ",
				params.getModel());
	}

	public UpdateResult deleteTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return super.update("DELETE FROM app_zz_file_results WHERE ",
				params.getModel());
	}

}
