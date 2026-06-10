package com.kayak.rpt.reportMenu.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.config.model.ReportValidationIndexModel;
import com.kayak.rpt.reportMenu.model.ReportMenuModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportMenuManageDao extends ComnDao {

    /**
     * 查询校验指标信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<ReportMenuModel> queryReportMenuInfo(SqlParam<ReportMenuModel> params) throws Exception {
        String sql = "select a.moduleid, a.menuid id, a.menuname name, a.upperid, a.url, a.iconcls, a.icon, a.loadorder, a.status, a.pageid, a.fastcode, " +
                "       a.functype, a.remark, a.menutype, b.obj_type, b.init_sql, a.reporturl  " +
                "  from sys_menu a left join sys_report_condition b on a.menuid = b.for_table " +
                " where 1=1 ";
        if (StringUtils.isNotBlank(params.getModel().getMenuId())) {
            sql = sql + " and a.menuid like '%" + params.getModel().getMenuId() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getMenuName())) {
            sql = sql + " and a.menuname like '%" + params.getModel().getMenuName() + "%'";
        }
        sql += " order by a.menuid ";
        return super.findRows(sql, 0, params);
    }



}
