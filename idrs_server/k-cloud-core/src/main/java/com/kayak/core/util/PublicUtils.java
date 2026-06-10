package com.kayak.core.util;
import com.kayak.core.system.SysUtil;

public class PublicUtils {

    public static String getSysWordDay () throws Exception {
        String sysWordDay="";
        String systemParamsByParaid = SysUtil.getSystemParamsByParaid("10006");
        if ("0".equals(systemParamsByParaid)) {
            sysWordDay = DateUtil.getNowDate();
        } else {
            sysWordDay = SysUtil.getSystemParamsByParaid("10004");
        }
        return sysWordDay;
    }

}
