package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.report.dao.ReportConvertConfigDao;
import com.kayak.report.model.ReportConvertConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "报表模板转换配置服务", model = ReportConvertConfig.class)
public class ReportConvertConfigService {

    @Autowired
    private ReportConvertConfigDao reportConvertConfigDao;

    @API(desc = "获取报表转换配置列表", auth = APIAuth.YES)
    public List<ReportConvertConfig> findReportConvertConfig(String reportId) throws Exception{
        return reportConvertConfigDao.findReportConvertConfig(reportId);
    }
}
