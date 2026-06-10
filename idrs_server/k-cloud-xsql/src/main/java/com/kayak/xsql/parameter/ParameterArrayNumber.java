package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;
import java.util.Collection;

public class ParameterArrayNumber extends Parameter {
	@SuppressWarnings("unchecked")
	@Override
	public void sql(StringBuilder sb, Object params) {
		sb.append(fixed);
		Collection<Number> list = (Collection<Number>) getter.get(params);
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

		Collection<Number> list = (Collection<Number>) getter.get(params);
		for (Number v : list) {
			ps.setLong(index++, v.longValue());

			if (!flag) {
				sb.append(",");
				flag = true;
			}
			sb.append(fixed);
		}
		return index;
	}
}
