package com.kayak.pms.netValue.util;

import com.kayak.pms.disclosureControl.dao.DisclosureWorkdayDao;
import com.kayak.pms.disclosureControl.model.DisclosureWorkday;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.kayak.pms.netValue.util
 * user:rennannan
 * date:2021/6/25 10:03
 * function:
 */
public class NetValUtil {
    @Autowired
    private DisclosureWorkdayDao disclosureWorkdayDao;

    /**
     * 功能：根据披露日期规则计算基准日期需要加的天数
     * 作者：rennannan
     * 日期：20210622
     *
     * @param netValueDateRule
     * @return
     */
    public static int getAddDays(String netValueDateRule) {
        int addDays = 0;
        switch (netValueDateRule) {
            case "01"://T+0
                addDays = 0;
                break;
            case "02"://T+1
                addDays = 1;
                break;
            case "03"://T+2
                addDays = 2;
                break;

            case "04"://T+3
                addDays = 3;
                break;
            case "05"://T+4
                addDays = 4;
                break;
            case "06"://T+5
                addDays = 5;
                break;

            case "07"://T+6
                addDays = 6;
                break;
            case "08"://T+7
                addDays = 7;
                break;

        }
        return addDays;
    }

}
