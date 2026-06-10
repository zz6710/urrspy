package com.kayak.dps.valtabimp.excel;

//import com.kayak.excel.model.ZSValuation;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * <li>完成日期：2020/1/15</li>
 * <li>修改记录: 无</li>
 *
 * @author yangzh
 * @version 1.0.0
 */
public class MapUtil {

    public static Map<String, Object> toMap(Object object) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            ReflectionUtils.makeAccessible(field);
            Object value = ReflectionUtils.getField(field, object);
            map.put(field.getName(), value);
        }
        return map;
    }
}
