package com.kayak.pms.opFlow.engine.helper;

import java.util.Map;

/**
 * Created by daniel on 10/04/2017.
 */
public class QueryParamHelper {
    public static int getParam(Map<String, Object> params, String key) {
        try {
            Object o = params.get(key);
            if (o instanceof Integer){
                return  ((Integer) o).intValue();
            }else if(o instanceof String){
                return Integer.parseInt((String)o);
            }else {
                throw new IllegalArgumentException("参数能够转化为整数");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("参数不正确");
        }
    }
}
