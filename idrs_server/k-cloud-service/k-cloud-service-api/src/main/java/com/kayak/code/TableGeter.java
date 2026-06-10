package com.kayak.code;

import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTable;

public abstract class TableGeter extends CommentGeter {

	public abstract void getTables(Statement stmt, List<DbTable> dbTables, String separator) throws Exception;

}
