package com.kayak.xsql.parameter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kayak.config.ConfigUitl;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.xsql.autoid.AutoId;
import com.kayak.xsql.autoid.DefaultAutoId;
import com.kayak.xsql.autoid.SnowFlakeAutoId;

public class ParameterPlanner {
	private static final Pattern VAR = Pattern.compile("\\$([a-zA-Z]+)\\{([_0-9a-zA-Z]+)\\}");

	private static final Map<String, Class<? extends Parameter>> map = new HashMap<>();
	static {
		map.put("S", ParameterString.class);
		map.put("I", ParameterInteger.class);
		map.put("L", ParameterLong.class);
		map.put("l", ParameterLong.class);
		map.put("D", ParameterDouble.class);
		map.put("T", ParameterDate.class);
		map.put("B", ParameterBigDecimal.class);
		map.put("P", ParameterTimestamp.class);
		map.put("M", ParameterArrayString.class);
		map.put("N", ParameterArrayNumber.class);
		map.put("U", ParameterUnknown.class);
		map.put("LS", ParameterListString.class);
		map.put("LN", ParameterListNumber.class);
		map.put("AUTOIDI", ParameterAutoIdI.class);
		map.put("AUTOIDL", ParameterAutoIdL.class);
		map.put("AUTOIDS", ParameterAutoIdS.class);
	}

	public ParameterHandler plan(String sql, Object param) throws Exception {
		Class<?> clazz = param.getClass();
		List<Parameter> segments = new ArrayList<>(); // 参数列表

		UpdateResult updateResult = null;

		int begin = 0; // 当前段的起始位置

		FieldGetterAutoId fieldGetterAutoId = null;

		Matcher m = VAR.matcher(sql);
		while (m.find()) {
			// 处理固定文本
			int end = m.start(); // 当前段固定文本的结束位置
			String fixed = sql.substring(begin, end); // 当前段的固定文本

			begin = m.end(); // 下一段的开始位置

			// 处理参数
			String type = m.group(1);
			String name = m.group(2);

			Class<? extends Parameter> c = map.get(type);
			if (c == null)
				throw new Exception("未知的参数类型: " + m.group());
			Parameter p = c.newInstance();

			p.setFixed(fixed);

			p.setParamName(name);

			// 根据不同的参数类型，选择不同的FieldGetter
			if (type.startsWith("AUTOID")) {
				updateResult = new UpdateResult();

				if (fieldGetterAutoId != null) {
					throw new SQLException("单个SQL只支持一个AUTOID字段");
				}

				String autoidImpl = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("sql.autoid.impl");

				AutoId autoId = null;

				if (!Tools.strIsEmpty(autoidImpl)) {
					autoId = (AutoId) Class.forName(autoidImpl).newInstance();
				}

				if (autoId == null) {
					String autoidType = SpringContextHolder.getApplicationContext().getEnvironment().getProperty("sql.autoid.type");
					if (Tools.strIsEmpty(autoidType)) {
						autoId = new DefaultAutoId();
					} else if ("snowFlake".equals(autoidType)) {
						autoId = new SnowFlakeAutoId();
					}
				}

				if (autoId == null) {
					throw new SQLException("AUTOID未配置正确的实现方式");
				}

				fieldGetterAutoId = new FieldGetterAutoId(sql, name, clazz, autoId, updateResult);

                p.setGetter(fieldGetterAutoId);
            } else if (name.startsWith("sys_user_")) {
                p.setGetter(new FieldGetterLogin(name));
            } else if(name.equals("SYSDATE")){
                p.setGetter(new FiledGetterSysDate());
            }else if(name.equals("SYSTIME")){
                p.setGetter(new FieldGetterSysTime());
            }else if(name.equals("SYSDATETIME")){
                p.setGetter(new FieldGetterSysDateTime());
            }else if(name.equals("SYSTIMESTAMP")){
                p.setGetter(new FieldGetterTimestamp());
            }else if(name.equals("UUID")){
                p.setGetter(new FieldGetterUUID());
            }else if (Object[].class.equals(clazz)) { // 直接通过参数行给出参数
                p.setGetter(new FieldGetterArray(segments.size()));
            } else if (Map.class.isAssignableFrom(clazz)) { // 通过Map给出参数
                p.setGetter(new FieldGetterMap(name));
            } else { // 通过JavaBean给出参数
                p.setGetter(new FieldGetterBean(clazz, name));
            }

			segments.add(p);
		}
		String tail = sql.substring(begin);

		return new ParameterHandler(segments, tail, updateResult, fieldGetterAutoId);
	}

}
