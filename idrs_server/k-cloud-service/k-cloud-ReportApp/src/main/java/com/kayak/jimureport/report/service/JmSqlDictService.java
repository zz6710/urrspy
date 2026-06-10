package com.kayak.jimureport.report.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.jimureport.report.dao.JmReportDao;
import com.kayak.jimureport.report.entity.JmSqlDictInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Map;


@Service
@APIDefine(desc = "积木报表SQLDICT", model = JmSqlDictInfo.class)
public class JmSqlDictService {

    @Resource(name = "jmReportDao")
    private JmReportDao jmReportDao;

    @API(desc = "报表sqldict信息查询", auth = APIAuth.NO)
    public SqlResult<JmSqlDictInfo> findJmSqlDictInfo(SqlParam<JmSqlDictInfo> params) throws Exception {
        return jmReportDao.selectJmSqlDictInfo(params);
    }

    @API(desc = "删除报表sqldict", auth = APIAuth.NO)
    public String deleteJmSqlDictInfo(SqlParam<JmSqlDictInfo> params) throws Exception {
        try {

        } catch (Exception e) {
            return RequestSupport.updateReturnJson(true, "菜单删除失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "菜单删除成功", null).toString();
    }
}
