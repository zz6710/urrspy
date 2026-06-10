package com.kayak.bak.business.action;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.bak.business.service.BakCollectionService;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 归档数据仓库
 */
@Controller
@APIDefine(desc = "归档数据仓库", model = SysBakCollectionPO.class)
public class BakCollectionAction {

    @Autowired
    private BakCollectionService bakCollectionService;

    @API(desc = "查询归档仓库列表", auth = APIAuth.YES)
    public SqlResult<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakCollectionPO> params) throws Exception {
        return bakCollectionService.getBakCollectionList(params);
    }

    @API(desc = "新增归档仓库信息", auth = APIAuth.YES)
    public void addBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        bakCollectionService.addBakCollection(params);
    }

    @API(desc = "修改集合表信息", auth = APIAuth.YES)
    public String updateBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        bakCollectionService.updateBakCollection(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "删除集合表信息", auth = APIAuth.YES)
    public String deleteBakCollection(SqlParam<SysBakCollectionPO> params) throws Exception {
        bakCollectionService.deleteBakCollection(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }
}
