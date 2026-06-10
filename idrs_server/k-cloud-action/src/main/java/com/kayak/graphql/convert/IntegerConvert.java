package com.kayak.graphql.convert;

import java.lang.reflect.Field;

import com.kayak.core.util.Tools;

public class IntegerConvert implements Convert {

	@Override
	public Object convert(Field field, String o) {
		if (Tools.strIsEmpty(o)) {
			return null;
		}

		return new Integer(o);
	}

}
