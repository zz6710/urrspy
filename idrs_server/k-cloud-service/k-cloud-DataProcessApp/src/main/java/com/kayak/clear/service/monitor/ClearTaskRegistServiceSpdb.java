package com.kayak.clear.service.monitor;

import com.kayak.config.constants.STGConstants;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.ods.service.DealPortFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务注册服务
 * @author zhangz1
 * @date 2022/6/16 15:38
 */
@Service
@Scope("prototype")
public class ClearTaskRegistServiceSpdb {

    private final Logger log = LoggerFactory.getLogger(ClearTaskRegistServiceSpdb.class);

    @Autowired
    private DealPortFileService dealPortFileService;

    /**
     * CISPEQ001定时任务
     */
    public void registProcess() throws Exception{
        log.info(" ###### CISPEQ001定时任务开始");
        Map<String, Object> params =new HashMap<>();
        params.put("portType", STGConstants.STG_PORT_TYPE_CSP);
        params.put("portDir", STGConstants.STG_DATA_DIR_SEND);
        params.put("dealDate", DateUtil.getNowDate());//处理日期
        params.put("dealType", STGConstants.STG_DATA_HANDLE_DAYS);//按天处理
        params.put("portCode", "send_cisp_prod_info");//接口代码
        try {
            dealPortFileService.dealAllPortInfo(params);
            //处理ok文件
        }catch (Exception e){
            log.error("CISPEQ001定时任务出错！错误信息:[{}]", e.getMessage(), e);
            throw e;
        }
        log.info(" ###### CISPEQ001定时任务完成");
    }

}
