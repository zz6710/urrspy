package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.BaseExMapModel;
import com.kayak.dps.ods.dao.BaseExMapDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@APIDefine(desc = "字典映射配置", model = BaseExMapModel.class)
public class BaseExMapService {
    @Resource(name = "baseExMapDao")
    private BaseExMapDao baseExMapDao;


    @API(desc = "字典映射配置页面查询", auth = APIAuth.YES)
    public SqlResult<BaseExMapModel> findBaseExMapModels(SqlParam<BaseExMapModel> params) throws Exception {
        params.setMakeSql(false);
        return baseExMapDao.findBaseExMapModels(params);
    }
    @API(desc = "添加字典映射", auth = APIAuth.YES)
    public int addBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.addBaseExMapModel(params).getEffect();
    }

    @API(desc = "修改字典映射", auth = APIAuth.YES)
    public int updateBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.updateBaseExMapModel(params).getEffect();
    }

    @API(desc = "删除字典映射", auth = APIAuth.YES)
    public int deleteBaseExMapModel(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.deleteBaseExMapModel(params).getEffect();
    }
    @API(desc = "查询查询字典名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<BaseExMapModel> findDictByNm(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.findDictByNm(params);
    }
    @API(desc = "查询字典",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<BaseExMapModel> findSysDictItemInfo(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.findSysDictItemInfo(params);
    }

    @API(desc = "查询字典名称",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<BaseExMapModel> findSysDictName(SqlParam<BaseExMapModel> params) throws Exception {
        return baseExMapDao.findSysDictName(params);
    }
}
