package com.kayak.xsql.parameter;

import java.sql.PreparedStatement;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.xsql.convert.Converter;

public class ParameterInteger extends Parameter {
	private Converter<Integer> converter = Converter.getConverter(Integer.class);

	@Override
	public int set(StringBuilder sb, PreparedStatement ps, Object params, int index) throws Exception {
		Object v = getter.get(params);

		checkParam(v);

		int value = converter.convert(v, Integer.class);

		ps.setInt(index, value);

		Desensitized desensitized = getter.getDesensitized();

		if (desensitized != null) {
			sb.append(fixed).append(desensitized.desensitized(value));
		} else {
			sb.append(fixed).append(value);
		}
		return index + 1;
	}

    @Override
    public void setParams(StringBuilder sb, Object params) throws Exception {
        Object v = getter.get(params);

        int value = converter.convert(v, Integer.class);
        sb.append(fixed).append(value);
    }

}
