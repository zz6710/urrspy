package com.kayak.pms.chinaBondSubmit.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.chinaBondSubmit.model.TrFileresults;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class TrFileresultsDao extends ComnDao {

	public SqlResult<TrFileresults> findTrFileresultss(SqlParam<TrFileresults> params) throws Exception {
		return super.findRows("SELECT register_date,file_type,filename,fileno,register_serno,errormsg,errorcode FROM app_zz_file_results", params);
	}

	public UpdateResult addTrFileresults(SqlParam<TrFileresults> params) throws Exception {
		return super.update("INSERT INTO app_zz_file_results(register_date,file_type,filename,fileno,register_serno,errormsg,errorcode) VALUES($S{registerDate},$S{fileType},$S{filename},$S{fileno},$S{registerSerno},$S{errormsg},$S{errorcode})",
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
