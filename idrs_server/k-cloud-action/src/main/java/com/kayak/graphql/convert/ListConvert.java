package com.kayak.graphql.convert;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;

public class ListConvert implements Convert {

	@Override
	public Object convert(Field field, String value) {
		if (StringUtils.isBlank(value)) {
			return Collections.emptyList();
		}

		ParameterizedType genericType = (ParameterizedType) field.getGenericType();
		Type actualTypeArgument = genericType.getActualTypeArguments()[0];

		try {
			return JSONObject.parseArray(value, Class.forName(actualTypeArgument.getTypeName()));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getLocalizedMessage(), e);
		}
	}

}
