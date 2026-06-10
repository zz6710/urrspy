package com.kayak.graphql.convert;

import com.kayak.core.util.Tools;

import java.lang.reflect.Field;

public class DoubleConvert implements Convert {

	@Override
	public Object convert(Field field,String o) {
		if (Tools.strIsEmpty(o)) {
			return null;
		}

		return new Double(o);
	}

}
