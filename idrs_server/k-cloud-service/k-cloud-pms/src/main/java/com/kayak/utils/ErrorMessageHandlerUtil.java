package com.kayak.utils;

import com.kayak.core.system.SysBeans;
import com.kayak.pms.basePublish.dao.DisclosureErrorHandlerDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessageHandlerUtil {

    private static Logger log = LogManager.getLogger(ErrorMessageHandlerUtil.class);

    private static DisclosureErrorHandlerDao disclosureErrorHandlerDao = SysBeans.getBean("disclosureErrorHandlerDao");

    /**
     * 各类异常信息记录处理方法
     * @param error_code 异常类型
     * @param error_info 异常信息
     */
    public static void ErrorInfoRecordHandle(String error_code, String error_info) {
        Map<String, Object> params = new HashMap<>();
        params.put("error_code", error_code);
        params.put("error_info", error_info);
        try{
            disclosureErrorHandlerDao.insertErrorMessage(params);
        }catch (Exception e){
            log.info("报错信息插入语句执行异常!");
        }
    }

}
