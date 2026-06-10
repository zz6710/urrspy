package com.kayak.code;

import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTableField;

public abstract class ColumnGeter extends CommentGeter {

	public abstract void getColumns(Statement stmt, List<DbTableField> tableFields, String table, String separator) throws Exception;

}
