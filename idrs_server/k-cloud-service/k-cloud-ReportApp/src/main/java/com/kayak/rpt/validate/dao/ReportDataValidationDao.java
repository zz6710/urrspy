package com.kayak.rpt.validate.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.validate.model.ReportValidationModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportDataValidationDao extends ComnDao {

    /**
     * 查询接口调用日志信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<ReportValidationModel> queryReportValidationResult(SqlParam<ReportValidationModel> params) throws Exception {
        // 20230223暂时去掉校验结果联表查询
        String userid=SysUtil.getLoginUserid();
        String sql = "select b.id, b.topic, b.validate_type, b.validate_result, b.reason, r.table_name as validate_table, b.index_name as column_code, " +
                "       b.validate_row, b.validate_column, b.create_date, b.create_time, b.index_code, b.index_name, b.data_id, r.report_table, b.deal_date ,t.index_detail" +
                "  from base_data_validation b " +
                "  join base_report_info r on b.validate_table = r.report_table " +
                "  join base_reportdata_index_config t on t.index_code = b.index_code" +
                " where 1 = 1";
        if (StringUtils.isNotBlank(params.getModel().getReportTable())) {
            sql = sql + " and b.validate_table like '%" + params.getModel().getReportTable() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getValidateType())) {
            sql = sql + " and b.validate_type = '"+params.getModel().getValidateType()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexCode())) {
            sql = sql + " and b.index_code = '"+params.getModel().getIndexCode()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getIndexName())) {
            sql = sql + " and t.index_name like '%" + params.getModel().getIndexName() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getValidateResult())) {
            sql = sql + " and b.validate_result = '"+params.getModel().getValidateResult()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getCreateDate())) {
            sql = sql + " and b.create_date = '"+params.getModel().getCreateDate()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getDealDate())) {
            sql = sql + " and b.deal_date = '"+params.getModel().getDealDate()+"' ";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportType())) {
            sql = sql + " and r.report_catgory = '"+params.getModel().getReportType()+"' ";
        }
        if(!"admin".equals(userid)){
            sql = sql +" and r.menuid in(select k2.menuid from sys_user k inner join sys_user_role k1 on k.userid=k1.userid inner join sys_role_menu k2 on k1.roleid=k2.roleid where k.userid="+userid+" )";
        }
        sql += " order by b.create_date desc, b.create_time desc ";
        return super.findRows(sql,params);
    }

    /**
     * 查询所有校验报表
     * @return
     * @throws Exception
     */
    public List<SqlRow> getCheckTableList() throws Exception {
        String sql = "select b.report_table as value, b.table_name as text FROM base_report_info b ";
        return super.findRows(sql, DataSourceProperty.PUB);
    }

    /**
     * 根据报送日期查询数据日期
     * @param dParams
     * @return
     * @throws Exception
     */
    public String checkDataDate (SqlParam<ReportValidationModel> dParams) throws Exception {
        String checkSql = ExeQuery.queryExeId("VALIDATEEU001");
        Map<String, Object> params = dParams.getParams();
        return super.findRow(checkSql, params).getString("theory_start_date");
    }


}
