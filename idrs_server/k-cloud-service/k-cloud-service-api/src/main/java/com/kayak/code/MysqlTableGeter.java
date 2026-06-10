package com.kayak.code;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTable;
import com.kayak.core.util.Tools;

public class MysqlTableGeter extends TableGeter {

	@Override
	public void getTables(Statement stmt, List<DbTable> dbTables, String separator) throws Exception {
		ResultSet rs = stmt.executeQuery("show table status");

		while (rs.next()) {
			String name = rs.getString("Name");
			String comment = rs.getString("Comment");

			DbTable dbTable = new DbTable();

			if(Tools.isNotBlank(name)){
				dbTable.setName(name);
			}
			if(Tools.isNotBlank(comment)){
				dbTable.setComment(getCommentBySeparator(comment, separator));
			}

			dbTables.add(dbTable);
		}
	}

}
