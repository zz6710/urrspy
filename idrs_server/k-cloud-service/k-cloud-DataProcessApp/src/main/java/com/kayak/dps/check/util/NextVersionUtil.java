package com.kayak.dps.check.util;

import com.kayak.core.util.Tools;

public class NextVersionUtil {
    /**
     * @功能描述:处理下一版本号
     * @params:[VNumber]
     */
    public static String getNextVersion(String VNumber) throws Exception {
        String newVNumber="";
        if(Tools.isNotEmpty(VNumber) && !VNumber.equals("")){
            String[] data = VNumber.split("V");
            String nowVersion = data[1];
            String[] number = nowVersion.split("\\.");
            String prefix = number[0];
            String suffix = number[1];
            if("9".equals(suffix)){
                Integer pre = Integer.parseInt(prefix)+1;
                newVNumber = "V"+pre+".0";
            }else{
                Integer suf = Integer.parseInt(suffix)+1;
                newVNumber = "V"+prefix+"."+suf;
            }
        }else{
            newVNumber="V1.0";
        }
        return newVNumber;
    }
}
