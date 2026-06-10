package com.kayak.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.report.dao.ExcelByTemplateDao;
import com.kayak.report.model.ExcelByTemplate;
import com.kayak.report.model.ReportConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "二维报表根据模板导出数据", model = ReportConvert.class)
public class ExcelByTemplateService {

    @Autowired
    private ExcelByTemplateDao excelByTemplateDao;

    @API(desc = "二维报表模板导出列表", auth = APIAuth.NO)
    public SqlResult<ExcelByTemplate> findExcelByTemplateConfig(SqlParam<ExcelByTemplate> params) throws Exception {
        return excelByTemplateDao.findExcelByTemplateConfig(params);
    }

}
