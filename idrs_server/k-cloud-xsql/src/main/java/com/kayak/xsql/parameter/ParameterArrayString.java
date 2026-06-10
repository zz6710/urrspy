package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;
import java.util.List;

public class ParameterArrayString extends Parameter {
	@SuppressWarnings("unchecked")
	@Override
	public void sql(StringBuilder sb, Object params) {
		sb.append(fixed);
		List<String> list = (List<String>) getter.get(params);
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (i > 0)
				sb.append(", ");
			sb.append("?");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		sb.append(fixed);
		boolean flag = false;

		List<String> list = (List<String>) getter.get(params);
		for (String v : list) {
			ps.setString(index++, v);

			if (!flag) {
				sb.append(", ");
				flag = true;
			}
			sb.append("'");
			sb.append(v);
			sb.append("'");
		}
		return index;
	}
}
