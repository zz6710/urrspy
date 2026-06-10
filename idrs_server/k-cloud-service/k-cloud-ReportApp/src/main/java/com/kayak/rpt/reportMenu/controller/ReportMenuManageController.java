package com.kayak.rpt.reportMenu.controller;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.config.service.ReportValidationIndexService;
import com.kayak.rpt.reportMenu.service.ReportMenuManageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reportMenuMaintain")
public class ReportMenuManageController {

    private static Logger logger = LogManager.getLogger(ReportMenuManageController.class);

    @Autowired
    private ReportMenuManageService reportMenuManageService;



}
