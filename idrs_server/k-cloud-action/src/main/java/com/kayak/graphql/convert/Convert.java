package com.kayak.graphql.convert;

import java.lang.reflect.Field;

public interface Convert {

	public Object convert(Field field, String value);

}
