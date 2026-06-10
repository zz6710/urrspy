package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.report.model.ReportConvertConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReportConvertConfigDao  extends ComnDao {

    /**
     * 获取报表转换配置列表
     * @param reportId
     * @return
     * @throws Exception
     */
    public List<ReportConvertConfig> findReportConvertConfig(String reportId) throws Exception{
        String strSql = "select report_id, target_column ,source_column , target_order  from base_rpt_convert_config where report_id = '"+reportId+ "' order by target_order";
        return super.findRows(ReportConvertConfig.class, strSql, 0, null);
    }
}
