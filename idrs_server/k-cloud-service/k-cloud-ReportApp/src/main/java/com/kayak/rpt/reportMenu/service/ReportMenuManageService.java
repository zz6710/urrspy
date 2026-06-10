package com.kayak.rpt.reportMenu.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.reportMenu.dao.ReportMenuManageDao;
import com.kayak.rpt.reportMenu.model.ReportMenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "报表菜单信息", model = ReportMenuModel.class)
public class ReportMenuManageService {
    
    @Autowired
    private ReportMenuManageDao reportMenuManageDao;

    /**
     * 查询报表菜单维护信息
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "查询报表菜单信息", auth = APIAuth.NO)
    public SqlResult<ReportMenuModel> queryReportMenuInfo(SqlParam<ReportMenuModel> params) throws Exception {
        return reportMenuManageDao.queryReportMenuInfo(params);
    }


}
