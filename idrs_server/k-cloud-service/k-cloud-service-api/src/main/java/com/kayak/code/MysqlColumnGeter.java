package com.kayak.code;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.kayak.code.model.DbTableField;

public class MysqlColumnGeter extends ColumnGeter {

	@Override
	public void getColumns(Statement stmt, List<DbTableField> tableFields, String table, String separator)
			throws Exception {
		ResultSet rs = stmt.executeQuery("show full columns from " + table);

		while (rs.next()) {
			String field = rs.getString("Field").toLowerCase();
			String key = rs.getString("Key");
			String fieldComment = rs.getString("Comment");
			String type = rs.getString("Type");

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
			tableField.setKey("PRI".equals(key));
			tableField.setType(type);
			tableField.setComment(getCommentBySeparator(fieldComment, separator));

			tableFields.add(tableField);
		}

	}

}
