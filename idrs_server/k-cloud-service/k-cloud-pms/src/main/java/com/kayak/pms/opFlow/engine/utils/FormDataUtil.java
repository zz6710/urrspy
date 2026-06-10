package com.kayak.pms.opFlow.engine.utils;

import com.kayak.pms.opFlow.engine.entity.FormData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 24/04/2017.
 */
public class FormDataUtil {

    public static Map<String, Object> formData2Map(List<FormData> formDatas) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (formDatas != null) {
            for (FormData formData : formDatas) {
                result.put(formData.getFieldName(), formData.getFieldValue());
            }
        }
        return result;
    }
}

