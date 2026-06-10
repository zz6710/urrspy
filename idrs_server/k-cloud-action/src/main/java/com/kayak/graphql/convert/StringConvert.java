package com.kayak.graphql.convert;

import java.lang.reflect.Field;

public class StringConvert implements Convert {

	@Override
	public Object convert(Field field, String value) {
		return value;
	}

}
