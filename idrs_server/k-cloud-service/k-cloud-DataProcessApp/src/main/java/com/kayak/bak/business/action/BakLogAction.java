package com.kayak.bak.business.action;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.bak.business.service.BakLogService;
import com.kayak.bak.model.po.SysBakLogPO;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 操作记录
 */
@Controller
@APIDefine(desc = "操作记录", model = SysBakLogPO.class)
public class BakLogAction {

    @Resource
    private BakLogService bakLogService;

    @API(desc = "查询归档记录列表", auth = APIAuth.NO)
    public SqlResult<SysBakLogPO> getBakLogList(SqlParam<SysBakLogPO> params) throws Exception {
        return bakLogService.getBakLogList(params);
    }

    @API(desc = "删除归档记录", auth = APIAuth.NO)
    public String deleteBakLog(SqlParam<SysBakLogPO> params) throws Exception {
        bakLogService.deleteBakConfig(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }
}
