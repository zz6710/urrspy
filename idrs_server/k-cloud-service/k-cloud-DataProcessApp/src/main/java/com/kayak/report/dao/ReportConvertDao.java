package com.kayak.report.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.report.model.ReportConvert;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ReportConvertDao extends ComnDao {

    public SqlResult<ReportConvert> findReportConvert(SqlParam<ReportConvert> param) throws Exception{
        String strSql = "select t.id, t.report_id, t2.system_table_name_cn report_name, t.operaterno, t.operatername, t.crt_date, t.crt_time,t.up_filename,t.convert_filename,t.convert_filepath from base_rpt_convert_info t inner join app_table_info t2 on t.report_id = t2.system_table_name where 1=1";
        if (StringUtils.isNotBlank(param.getModel().getCrtDate())) {
            strSql = strSql + " and t.crt_date = '" + param.getModel().getCrtDate() + "'";
        }

        if (StringUtils.isNotBlank(param.getModel().getReportId())) {
            strSql = strSql + " and t.report_id = '" + param.getModel().getReportId() + "'";
        }

        if (StringUtils.isNotBlank(param.getModel().getOperatername())) {
            strSql = strSql + " and t.operatername like '%" + param.getModel().getOperatername() + "%'";
        }

        strSql = strSql + " order by t.crt_date desc, t.crt_time desc";

        return  super.findRows(strSql, param);
    }

    public SqlResult<ReportConvert> findReportTemplateList(SqlParam<ReportConvert> param) throws Exception{
        String strSql = "select distinct t.report_id,t2.system_table_name_cn report_name from base_rpt_convert_config t inner join app_table_info t2 on t.report_id = t2.system_table_name";
        return super.findRows(strSql, param);
    }

    public void addReportConvertInfo(ReportConvert report) throws Exception {
        Map<String, Object> currUseMap = SysUtil.getSysUserParamsByWmp();
        Object userObj = currUseMap.get("loginname");
        report.setOperaterno(userObj != null? userObj.toString(): ""); //获取用户ID
        report.setOperatername(SysUtil.getSysUserParamValue("sys_user_username").toString()); //获取用户的姓名
        String strSql = "insert into base_rpt_convert_info(id, report_id, operaterno, operatername, crt_date, crt_time, up_filename, convert_filename, convert_filepath) values(concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()), $S{reportId}, $S{operaterno}, $S{operatername}, date_format(curDate(), '%Y%m%d'), date_format(CURTIME(),'%H%i%s'), $S{upFilename},$S{convertFilename},$S{convertFilepath})";
        super.update(strSql, DataSourceProperty.PUB, report);
    }

    /**
     * 根据ID查询报表导出配置
     * @param id
     * @return
     * @throws Exception
     */
    public ReportConvert queryReportConvertInfo(String id) throws Exception{
        String strSql ="select id, report_id, operaterno, operatername, crt_date, crt_time, up_filename, convert_filename, convert_filepath from base_rpt_convert_info where id='"+id+"'";
        return super.findRow(ReportConvert.class, strSql, 0, null);
    }
}
