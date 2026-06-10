package com.kayak.pms.onlineEdit.dao;

import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.onlineEdit.model.T8OnlineWordTableColumns;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;

@Repository
public class T8OnlineWordTableColumnsDao extends ComnDao {

	public SqlResult<T8OnlineWordTableColumns> findT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return super.findRows("SELECT id,table_name,sql_info,column_name,column_comment,default_value,is_disabled,status,dict,doc_column,data_type,data_digits,empty_default_val FROM t8_online_word_table_columns", params);
	}

	public UpdateResult addT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return super.update("INSERT INTO t8_online_word_table_columns(table_name,sql_info,column_name,column_comment,default_value,is_disabled,status,dict,doc_column,data_type,data_digits,empty_default_val,crt_date,crt_time,crt_user) " +
						"VALUES($S{tableName},$S{sqlInfo},$S{columnName},$S{columnComment},$S{defaultValue},$S{isDisabled},$S{status},$S{dict},$S{docColumn},$S{dataType},$S{dataDigits},$S{emptyDefaultVal},$S{crtDate},$S{crtTime},$S{crtUser})",
				params.getModel());
	}

	public UpdateResult updateT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return super.update("UPDATE t8_online_word_table_columns SET table_name=$S{tableName} ,sql_info=$S{sqlInfo},column_name=$S{columnName} ,column_comment=$S{columnComment},default_value=$S{defaultValue},is_disabled=$S{isDisabled} " +
						",status=$S{status},dict=$S{dict},doc_column=$S{docColumn},data_type=$S{dataType},data_digits=$S{dataDigits},empty_default_val=$S{emptyDefaultVal},upd_date=$S{updDate},upd_time=$S{updTime},upd_user=$S{updUser}  WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteT8OnlineWordTableColumns(SqlParam<T8OnlineWordTableColumns> params) throws Exception {
		return super.update("DELETE FROM t8_online_word_table_columns WHERE  id=$S{id} ",
				params.getModel());
	}
	
	public List<SqlRow> findT8OnlineWordTableColumns() throws Exception {
		return super.findRows("SELECT id,table_name,sql_info,column_name,column_comment,default_value,is_disabled,status,dict,doc_column,data_type,data_digits,empty_default_val FROM t8_online_word_table_columns");
	}

}
