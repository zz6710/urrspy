package com.kayak.dps.service;

import com.kayak.core.util.DateUtil;
import com.kayakwise.kcloud.batch.extend.service.BatchDateService;
import org.springframework.stereotype.Service;

/**
 * @author xiamh
 * @date 2022/6/14 15:16
 */
@Service
public class PmsBatchDateService implements BatchDateService {

    @Override
    public String getCurrDateTime() {
        return DateUtil.getTimestamp14();
    }

    @Override
    public String getCurrDate() {
        return DateUtil.getNowDate();
    }

    @Override
    public String getCurrTime() {
        return DateUtil.getNowTime();
    }

    @Override
    public String getCurrentWorkdate(String batchModuleId) {
        String workDay="";
        try{
            workDay= DateUtil.getSysWordDay();
        }catch (Exception e){
        }
          return workDay;
    }
}
