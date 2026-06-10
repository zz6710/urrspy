package com.kayak.graphql.convert;

import com.kayak.core.util.Tools;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateConvert implements Convert {

	@Override
	@SneakyThrows
	public Object convert(Field field, String o) {
		if (Tools.strIsEmpty(o)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return new Date(sdf.parse(o).getTime());
	}

}
