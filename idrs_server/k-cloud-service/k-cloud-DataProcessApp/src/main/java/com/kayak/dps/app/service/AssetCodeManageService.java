package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.AssetCodeManageModelDao;
import com.kayak.dps.app.model.AssetCodeManageModel;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;


@Service
@APIDefine(desc = "资产代码管理服务", model = AssetCodeManageModel.class)
public class AssetCodeManageService {

    @Resource
    private AssetCodeManageModelDao assetCodeManageModelDao;


    @API(desc = "查询资产代码信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<AssetCodeManageModel> findAssetCodeManageModels(SqlParam<AssetCodeManageModel> params) throws Exception {
        return assetCodeManageModelDao.findAssetCodeManage(params);
    }

    @API(desc = "删除资产代码信息", auth = APIAuth.YES)
    public String deleteAssetCodeManage(SqlParam<AssetCodeManageModel> params) {
        try {
            assetCodeManageModelDao.deleteAssetCodeManage(params);
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "修改资产代码信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String updateAssetCodeManage(SqlParam<AssetCodeManageModel> params) {
        try {
            params.getModel().setUpdDate(DateUtil.getNowDate());
            params.getModel().setUpdTime(DateUtil.getNowTime());
            params.getModel().setEffectiveTime(DateUtil.getNowTime());
            params.getModel().setExpirationTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            assetCodeManageModelDao.updateAssetCodeManage(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "修改失败！", null).toString();
        }
    }

    @API(desc = "新增资产代码信息",operation = APIOperation.UPDATE, auth = APIAuth.YES)
    public String insertAssetCodeManage(SqlParam<AssetCodeManageModel> params) {
        try {
            params.getModel().setEffectiveTime(DateUtil.getNowTime());
            params.getModel().setExpirationTime(DateUtil.getNowTime());
            params.getModel().setUpdUser(Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            assetCodeManageModelDao.insertAssetCodeManage(params).getEffect();
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        }catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  "新增失败！", null).toString();
        }
    }
}
