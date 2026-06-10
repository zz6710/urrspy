package com.kayak.jimureport.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.jimureport.report.dao.JmReportDao;
import com.kayak.jimureport.report.entity.JmReportMenuInfo;
import com.kayak.rpt.config.model.ReportValidationIndexModel;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;


@Service
@APIDefine(desc = "积木报表菜单", model = JmReportMenuInfo.class)
public class JmReportService {

    @Resource(name = "jmReportDao")
    private JmReportDao jmReportDao;

    @API(desc = "报表菜单信息查询", auth = APIAuth.NO)
    public SqlResult<JmReportMenuInfo> findReportMenuInfo(SqlParam<JmReportMenuInfo> params) throws Exception {
        return jmReportDao.selectReportMenuInfo(params);
    }

    @API(desc = "删除报表菜单", auth = APIAuth.NO)
    public String deleteReportMenuInfo(SqlParam<JmReportMenuInfo> params) throws Exception {
        try {
            Map<String, Object> parma = params.getParams();
            String sql = "";
            if (parma != null) {
                //删除报表
                if (!StringUtils.isEmpty(parma.get("menuid"))) {
                    sql = "update JIMU_REPORT set DEL_FLAG='1' " +
                            " where ID=(select DISTINCT JIMU_REPORT_ID FROM JIMU_SQLDICT WHERE ID=$S{menuid}) ";
                    jmReportDao.update(sql, parma);
                }
                //删除菜单数据
                if (!StringUtils.isEmpty(parma.get("menuid"))) {
                    //comnDao.update("JMREPORT0014", params);
                    sql = "delete from SYS_MENU where MENUID=$S{menuid} ";
                    jmReportDao.update(sql, parma);
                    sql = "delete from sys_role_right where menuid=$S{menuid}";
                    jmReportDao.update(sql, parma);
                }
                //删除报表SQL
                if (StringUtils.isEmpty(parma.get("menuid"))) {
                    sql = "delete from JIMU_SQLDICT where id=$S{menuid}";
                    jmReportDao.update(sql, parma);
                }
                // 删除查询条件
                if (!StringUtils.isEmpty(parma.get("jmmenuid"))) {
                    sql = "delete from t8_query_sql where menuid = $S{jmmenuid}";
                    jmReportDao.update(sql, parma);
                }
            }
        } catch (Exception e) {
            return RequestSupport.updateReturnJson(true, "菜单删除失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "菜单删除成功", null).toString();
    }
}
