package com.kayak.xsql.parameter;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kayak.core.desensitized.Desensitized;
import com.kayak.graphql.annotation.GraphQLField;

public class FieldGetterBean extends FieldGetter {
	private static Logger log = LoggerFactory.getLogger(FieldGetterBean.class);

	private Field field;
	private Desensitized desensitized;

	public FieldGetterBean(Class<?> clazz, String name) {
		try {
			for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
				try {
					field = c.getDeclaredField(name);
				} catch (NoSuchFieldException e) {
					continue;
				}

				if (!field.isAccessible())
					field.setAccessible(true);
				break;
			}
			if (field == null)
				throw new Exception("字段不存在: " + clazz.getName() + "." + name);

			GraphQLField graphQLField = field.getAnnotation(GraphQLField.class);

			if (graphQLField != null) {
				Class<?> displayClass = graphQLField.desensitized();
				if (displayClass != null) {
					desensitized = (Desensitized) displayClass.newInstance();
				}
			}
		} catch (Exception e) {
			log.warn("不能访问Bean的字段: " + clazz.getName() + ": " + name, e);
		}
	}

	@Override
	public Object get(Object bean) {
		if (field == null)
			return null;

		try {
			return field.get(bean);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Desensitized getDesensitized() {
		return desensitized;
	}

}
