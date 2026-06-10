package com.kayak.core.util;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public class LogFileUtil {
    private static final Logger logger2= LoggerFactory.getLogger(LogFileUtil.class);
    @Value("${log.security:true}")
    private static boolean logSecurity;
    public static void infoSecurity(Map<String,Object> logMap) {
        String params=null!=logMap.get("params")?logMap.get("params").toString():"";
        String userid=logMap.containsKey("userid")?logMap.get("userid").toString():SysUtil.getSysUserParams().get("userid").toString();
        logger2.info("操作者userid:"+ userid
                +" "+"ip:"+ NetworkUtil.getIpAddress(RequestSupport.getLocalRequest()) +" "+"事件类型:"+logMap.get("operation")+" "+"结果:"+logMap.get("result")+" "+"参数:"+params);
    }
}
