package com.kayak.rpt.zz.manage.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.dao.DaoService;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.zz.manage.dao.ZzCodeApplyHistoryDao;
import com.kayak.rpt.zz.manage.model.UnderAssetRegistInfo;
import com.kayak.rpt.zz.manage.model.ZzCodeApplyHistory;
import com.kayak.rpt.zz.operate.service.UnderAssetRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@APIDefine(desc = "中债登记编码申请记录表服务", model = ZzCodeApplyHistory.class)
public class ZzCodeApplyHistoryService {
    @Autowired
    private ZzCodeApplyHistoryDao zzCodeApplyHistoryDao;
    @Autowired
    protected DaoService daoService;
    @API(desc = "查询底层资产持仓管理信息", auth = APIAuth.YES)
    public SqlResult<ZzCodeApplyHistory> findZzCodeApplyHistorys(SqlParam<ZzCodeApplyHistory> params) throws Exception {
        params.setMakeSql(true);
        return zzCodeApplyHistoryDao.findZzCodeApplyHistorys(params);
    }
}
