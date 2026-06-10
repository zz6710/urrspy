package com.kayak.rpt.config.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.config.service.ReportValidationIndexService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/reportIndexConfig")
public class ReportDataValidationIndexController {

    private static Logger logger = LogManager.getLogger(ReportDataValidationIndexController.class);

    @Autowired
    private ReportValidationIndexService reportValidationIndexService;

    /**
     * 新增报送数据校验指标
     * @return
     */
    @RequestMapping(value="/addReportIndex.action")
    public String reportIndexConfigAdd() {
        logger.info("****************开始处理:新增报送数据校验指标*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            //判断下指标代码是否有重复的
            List<SqlRow> list = reportValidationIndexService.findReportValidationIndexConfigInformationByIndex(params);
            if (CollectionUtil.isNotEmpty(list)) {
                return RequestSupport.updateReturnJson(false,"当前指标代码已存在，请重新输入！",null).toString();
            }

            reportValidationIndexService.addReportValidationIndexMethod(params);
            return RequestSupport.updateReturnJson(true,"新增报送数据校验指标完成！",null).toString();
        } catch (Exception e) {
            logger.error("新增报送数据校验指标异常: ", e);
            logger.info("****************新增报送数据校验指标 处理结束*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    /**
     * 修改报送数据校验指标
     * @return
     */
    @RequestMapping(value="/updateReportIndex.action")
    public String reportIndexConfigUpdate() {
        logger.info("****************开始处理:修改报送数据校验指标*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            reportValidationIndexService.updateReportValidationIndexMethod(params);
            return RequestSupport.updateReturnJson(true,"修改报送数据校验指标完成！",null).toString();
        } catch (Exception e) {
            logger.error("修改报送数据校验指标异常: ", e);
            logger.info("****************修改报送数据校验指标 处理结束*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


}
