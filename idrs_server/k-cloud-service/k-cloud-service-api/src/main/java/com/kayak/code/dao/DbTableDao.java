package com.kayak.code.dao;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.code.DbInfoFactory;
import com.kayak.code.model.DbTable;
import com.kayak.code.model.DbTableField;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class DbTableDao extends ComnDao {

	@Value("${table.content.separator: }")
	private String separator;

	public SqlResult<DbTable> findTables(SqlParam<DbTable> params) throws Exception {

		List<DbTable> dbTables = new ArrayList<DbTable>();

		try (AutoCloseable ca = daoService.selectDataSource(0)) {
			Connection conn = daoService.getConnection();
			Statement stmt = conn.createStatement();
			DbInfoFactory.getTableGeter(daoService.getDbType(0)).getTables(stmt, dbTables, separator);
		}

		return SqlResult.build(dbTables);
	}

	public SqlResult<DbTableField> findTableFields(SqlParam<DbTableField> params) throws Exception {

		List<DbTableField> tableFields = new ArrayList<DbTableField>();

		try (AutoCloseable ca = daoService.selectDataSource(0)){
			Connection conn = daoService.getConnection();

			Statement stmt = conn.createStatement();
			DbInfoFactory.getColumnGeter(daoService.getDbType(0)).getColumns(stmt, tableFields,
					params.getModel().getTable(), separator);

		}


		return SqlResult.build(tableFields);
	}

}
