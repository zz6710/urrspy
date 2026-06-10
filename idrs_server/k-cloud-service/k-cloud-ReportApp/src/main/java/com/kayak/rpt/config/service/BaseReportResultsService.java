package com.kayak.rpt.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.config.dao.BaseReportResultDao;
import com.kayak.rpt.config.model.BaseReportResultModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "报送任务管理", model = BaseReportResultModel.class)
public class BaseReportResultsService {
    
    @Autowired
    private BaseReportResultDao baseReportResultDao;

    @API(desc = "报送任务管理信息查询", auth = APIAuth.NO)
    public SqlResult<BaseReportResultModel> findBaseReportResultInfo(SqlParam<BaseReportResultModel> params) throws Exception {
        params.setMakeSql(true);
        return baseReportResultDao.findBaseReportResultInfo(params);
    }

    @API(desc = "手工报送", auth = APIAuth.NO)
    public String updateBaseReportResultInfo(SqlParam<BaseReportResultModel> param) throws Exception {
        param.getModel().setReportSuccessNumber(param.getModel().getTotal());
        baseReportResultDao.updateAppDataInfo(param);
        baseReportResultDao.updateBaseReportResultInfo(param);
        return RequestSupport.updateReturnJson(true, "手工报送成功!", null).toString();
    }

    @API(desc = "撤销报送", auth = APIAuth.NO)
    public String cancelBaseReportResultInfo(SqlParam<BaseReportResultModel> param) throws Exception {
        baseReportResultDao.cancelAppDataInfo(param);
        baseReportResultDao.cancelBaseReportResultInfo(param);
        return RequestSupport.updateReturnJson(true, "撤销报送成功!", null).toString();
    }

}
