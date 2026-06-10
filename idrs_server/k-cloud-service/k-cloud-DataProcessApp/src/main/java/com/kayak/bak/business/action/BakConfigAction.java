package com.kayak.bak.business.action;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.bak.business.service.BakConfigService;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.SourceConfigDTO;
import com.kayak.bak.model.dto.SourceTableDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.bak.model.request.GetFieldInfoRequest;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@APIDefine(desc = "归档数据配置", model = SysBakConfigPO.class)
public class BakConfigAction {

    @Autowired
    private BakConfigService bakConfigService;

    @API(desc = "查询归档配置列表", auth = APIAuth.NO)
    public SqlResult<SysBakConfigPO> getBakConfigList(SqlParam<SysBakConfigPO> params) throws Exception {
        return bakConfigService.getBakConfigList(params);
    }

    @API(desc = "删除归档配置信息", auth = APIAuth.NO)
    public String deleteBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        bakConfigService.deleteBakConfig(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "修改归档配置信息", auth = APIAuth.NO)
    public String updateBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        bakConfigService.updateBakConfig(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "新增归档配置信息", auth = APIAuth.NO)
    public String addBakConfig(SqlParam<SysBakConfigPO> params) throws Exception {
        bakConfigService.addBakConfig(params);
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "查询归档仓库列表", auth = APIAuth.NO)
    public List<SysBakCollectionPO> getBakCollectionList(SqlParam<SysBakConfigPO> params) throws Exception {
        return bakConfigService.getBakCollectionList(params);
    }

    /**
     * 拓展接口，查询单表字段信息
     * @param request
     * @return
     * @throws Exception
     */
    @API(desc = "查询单表字段信息", auth = APIAuth.NO)
    public List<BakFieldDTO> getFieldInfo(GetFieldInfoRequest request) throws Exception {
        return bakConfigService.getFieldInfo(request);
    }

    /**
     * 查询库列表
     * @return
     * @throws Exception
     */
    @API(desc = "查询库列表", auth = APIAuth.NO)
    public SqlResult<SourceConfigDTO> getDbList(SqlParam<SysBakConfigPO> params) throws Exception {
        SqlResult sqlResult = new SqlResult();
        sqlResult.setRows(bakConfigService.getDbList());
        return sqlResult;
    }

    /**
     * 查询表列表
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "查询表列表", auth = APIAuth.NO)
    public SqlResult<SourceTableDTO> getTableList(SqlParam<SysBakConfigPO> params) throws Exception {
        SqlResult sqlResult = new SqlResult();
        sqlResult.setRows(bakConfigService.getTableList(params.getModel().getTargetDb()));
        return sqlResult;
    }
}
