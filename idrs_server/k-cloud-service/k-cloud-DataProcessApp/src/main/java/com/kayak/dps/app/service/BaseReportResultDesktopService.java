package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.app.dao.BaseReportResultDesktopDao;
import com.kayak.dps.app.model.BaseReportResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@APIDefine(desc = "报送结果服务类", model = BaseReportResult.class)
public class BaseReportResultDesktopService {

    @Autowired
    private BaseReportResultDesktopDao baseReportResultDesktopDao;

    @API(desc = "查询今日待报送数据", auth = APIAuth.NO)
    public SqlResult<BaseReportResult> findReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
        params.setMakeSql(false);
        String check = params.getModel().getCheckType();
        if("1".equals(check)){
            return baseReportResultDesktopDao.findReportResultInfo(params);
        }else if("2".equals(check)){
            return baseReportResultDesktopDao.findNextReportResultInfo(params);
        }else{
            return baseReportResultDesktopDao.findTodayReportResultInfo(params);
        }
    }

    @API(desc = "查询延期待报送数据", auth = APIAuth.NO)
    public SqlResult<BaseReportResult> findNextReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
        params.setMakeSql(false);
        String workDay = this.getSysWordDay();
        params.getModel().setWorkDay(workDay);
        return baseReportResultDesktopDao.findNextReportResultInfo(params);
    }

    @API(desc = "查询今日已报送数据", auth = APIAuth.NO)
    public SqlResult<BaseReportResult> findTodayReportResultInfo(SqlParam<BaseReportResult> params) throws Exception {
        params.setMakeSql(false);
        String workDay = this.getSysWordDay();
        params.getModel().setWorkDay(workDay);
        return baseReportResultDesktopDao.findTodayReportResultInfo(params);
    }

    public String getSysWordDay () throws Exception {
        String sysWordDay="";
        String systemParamsByParaid = SysUtil.getSystemParamsByParaid("10006");
        if ("0".equals(systemParamsByParaid)) {
            sysWordDay = DateUtil.getNowDate();
        } else {
            sysWordDay = SysUtil.getSystemParamsByParaid("10004");
        }
        return sysWordDay;
    }


}
