package com.kayak.pms.basePublish.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.basePublish.dao.DpbReportInfoDao;
import com.kayak.pms.basePublish.model.DisclosureChannelRule;
import com.kayak.pms.basePublish.model.DpbReportInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @BelongsProject: idrs3
 * @BelongsPackage: com.kayak.pms.basePublish.service
 * @Author: wangchenglin
 * @CreateTime: 2023/02/15  22:20
 * @Description:
 * @Version: 1.0
 */
@Service
@APIDefine(desc = "报表查询服务", model = DpbReportInfo.class)
public class DpbReportInfoService {

    @Autowired
    private DpbReportInfoDao dpbReportInfoDao;

    @API(desc = "首页查询报送提醒", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DpbReportInfo> findSubmitRemind(SqlParam<DpbReportInfo> params) throws Exception {
        return dpbReportInfoDao.findSubmitRemind(params);
    }

    @API(desc = "首页查询指标校验提醒", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<DpbReportInfo> findIndicatorCheckRemind(SqlParam<DpbReportInfo> params) throws Exception {
        return dpbReportInfoDao.findIndicatorCheckRemind(params);

    }

}
