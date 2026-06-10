package com.kayak.graphql.convert;

import com.kayak.core.util.Tools;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampConvert implements Convert {

	@Override
	@SneakyThrows
	public Object convert(Field field, String o) {
		if (Tools.strIsEmpty(o)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(o);
		return new Timestamp(date.getTime());
	}

}
