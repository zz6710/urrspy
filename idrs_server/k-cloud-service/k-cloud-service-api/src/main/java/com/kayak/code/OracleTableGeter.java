package com.kayak.code;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTable;
import com.kayak.core.util.Tools;

public class OracleTableGeter extends TableGeter {

	@Override
	public void getTables(Statement stmt, List<DbTable> dbTables, String separator) throws Exception {
		ResultSet rs = stmt.executeQuery(
				"SELECT t.TABLE_NAME, t2.COMMENTS FROM  user_tables t LEFT JOIN user_tab_comments t2 ON t.TABLE_NAME = t2.TABLE_NAME");

		while (rs.next()) {
			String name = rs.getString("TABLE_NAME");
			String comment = rs.getString("COMMENTS");

			DbTable dbTable = new DbTable();

			if(Tools.isNotBlank(name)){
				dbTable.setName(name);
			}

			if (Tools.isNotBlank(comment)) {
				dbTable.setComment(getCommentBySeparator(comment, separator));
			}

			dbTables.add(dbTable);
		}
	}

}
