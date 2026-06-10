package com.kayak.dps.check.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.clear.utils.Tools;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.check.dao.T8portInfoDao;
import com.kayak.dps.check.model.T8PortInfoModel;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.graphql.model.FetcherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "接口信息管理表服务", model = T8PortInfoModel.class)
public class T8PortInfoService {
    @Autowired
    private T8portInfoDao t8portInfoDao;

    @Autowired
    private DealPortFileService dealPortFileService;

    @API(desc = "查询接口信息管理表信息", auth = APIAuth.YES)
    public SqlResult<T8PortInfoModel> findPortInformation(SqlParam<T8PortInfoModel> params) throws Exception {
        return t8portInfoDao.findPortInformation(params);
    }

    /**
     * 删除接口信息
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "删除接口信息管理信息", auth = APIAuth.YES)
    public String deletePortInformation(SqlParam<T8PortInfoModel> params) throws Exception {
        t8portInfoDao.delPortInformation(params);
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }


    /**
     * 接口停用执行方法
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "停用", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String stopStatus(SqlParam<T8PortInfoModel> params) throws Exception {
        boolean result = t8portInfoDao.stopStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "停用成功" : "停用失败", null).toString();
    }

    /**
     * 接口启用执行方法
     * @param params
     * @return
     * @throws Exception
     */
    @API(desc = "启用", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String recoverStatus(SqlParam<T8PortInfoModel> params) throws Exception {
        boolean result = t8portInfoDao.recoverStatus(params) > 0;
        return RequestSupport.updateReturnJson(result, result ? "启用成功" : "启用失败", null).toString();
    }

    /**
     * 新增接口信息
     * @param params
     * @throws Exception
     */
    @API(desc = "新增接口信息", auth = APIAuth.YES)
    public int addPortInfo(SqlParam<T8PortInfoModel> params) throws Exception {
        params.getModel().setInputuser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
        return t8portInfoDao.addPortInformation(params);
    }

    /**
     * 修改接口信息
     * @param params
     * @throws Exception
     */
    @API(desc = "更新接口信息", auth = APIAuth.YES)
    public int updPortInfo(SqlParam<T8PortInfoModel> params) throws Exception {
        params.getModel().setInputuser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
        return t8portInfoDao.updPortInformation(params);
    }

    /**
     * 修改接口信息
     * @param modelParams
     * @throws Exception
     */
    @API(desc = "查询接口下载路径", auth = APIAuth.YES)
    public SqlResult<T8PortInfoModel> queryPortAddressByPortType(SqlParam<T8PortInfoModel> modelParams) throws Exception {
        Map<String, String> sqlParams = new HashMap<>();
        modelParams.getModel().setInputuser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
        sqlParams.put("port_type", modelParams.getModel().getPortType());
        sqlParams.put("deal_date", modelParams.getModel().getDealDate());
        return t8portInfoDao.queryPortAddressByPortType(modelParams);
    }

    /**
     * 根据接口类型查询返回接口文件
     * @param params
     * @return
     * @throws Exception
     */
    public List<SqlRow> queryPortManageInfoByPortType (Map<String, Object> params) throws Exception {
        return t8portInfoDao.queryPortManageInfoByPortType(params);
    }


}
