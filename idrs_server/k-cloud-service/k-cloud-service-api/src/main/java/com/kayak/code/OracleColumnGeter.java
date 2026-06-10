package com.kayak.code;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTableField;

public class OracleColumnGeter extends ColumnGeter {

	@Override
	public void getColumns(Statement stmt, List<DbTableField> tableFields, String table, String separator)
			throws Exception {
		ResultSet rs = stmt.executeQuery(
				"SELECT t.COLUMN_NAME, t.DATA_TYPE, t2.COMMENTS FROM user_tab_columns t LEFT JOIN user_col_comments t2 ON t.COLUMN_NAME = t2.COLUMN_NAME WHERE t.TABLE_NAME = t2.TABLE_NAME AND t.TABLE_NAME='"
						+ table.toUpperCase() + "' ORDER BY t.COLUMN_ID ");

		while (rs.next()) {
			String field = rs.getString("COLUMN_NAME")==null?"":rs.getString("COLUMN_NAME").toLowerCase();
			String fieldComment =rs.getString("COMMENTS")==null?"":rs.getString("COMMENTS");
			String type =rs.getString("DATA_TYPE")==null?"":rs.getString("DATA_TYPE").toLowerCase();

			DbTableField tableField = new DbTableField();
			String newField = null;
			// 切换成驼峰写法
			if (field.contains("_")) {
				String[] fields = field.split("_");
				for (String _field : fields) {
					if (newField == null) {
						newField = _field;
					} else {
						newField += _field.substring(0, 1).toUpperCase() + _field.substring(1);
					}
				}
			} else {
				newField = field;
			}

			// 去除type的()部分
			if (type.contains("(")) {
				type = type.substring(0, type.indexOf("("));
			}

			tableField.setField(newField);
			tableField.setDbField(field);
			tableField.setType(type);
			tableField.setComment(getCommentBySeparator(fieldComment, separator));

			tableFields.add(tableField);
		}

	}

}
