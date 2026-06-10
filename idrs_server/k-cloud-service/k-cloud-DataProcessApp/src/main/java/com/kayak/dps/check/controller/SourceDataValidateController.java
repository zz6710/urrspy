package com.kayak.dps.check.controller;

import com.kayak.dps.check.service.SourceDataValidateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SourceDataValidateController {

    private static Logger log = LogManager.getLogger(SourceDataValidateController.class);

    @Autowired
    private SourceDataValidateService sourceDataValidateService;

    /**
     * 源数据层加工任务
     */
    @RequestMapping(value="/sourceDataTask.action")
    public void sourceDataTaskExecute(){
        String deal_date = "20210525";
        sourceDataValidateService.execute(deal_date);
    }

}
