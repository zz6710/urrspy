package com.kayak.graphql.convert;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertUtil {

	private static Map<String, Convert> convertMap = new HashMap<String, Convert>();

	static {
		convertMap.put(String.class.getName(), new StringConvert());
		convertMap.put(Integer.class.getName(), new IntegerConvert());
		convertMap.put(Double.class.getName(), new DoubleConvert());
		convertMap.put(List.class.getName(), new ListConvert());
		convertMap.put(Timestamp.class.getName(), new TimestampConvert());
		convertMap.put(Date.class.getName(), new DateConvert());
		convertMap.put(BigDecimal.class.getName(),new BigDecimalConvert());
	}

	public static Object convert(Field field, String value) throws Exception {

		String fieldType = field.getType().getName();

		Convert convert = convertMap.get(fieldType);

		if (convert == null) {
			throw new Exception("不支持的数据格式转换类型【" + fieldType + "】");
		}

		try {
			return convert.convert(field, value);
		} catch (Exception e) {
			throw new Exception("数据格式转换异常，字段：" + field.getName() + "，数据类型：" + fieldType + "，参数值：" + value, e);
		}
	}

}
