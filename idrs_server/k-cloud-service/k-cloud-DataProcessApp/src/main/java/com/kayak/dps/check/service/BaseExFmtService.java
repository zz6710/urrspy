package com.kayak.dps.check.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.check.dao.BaseExFmtDao;
import com.kayak.dps.check.model.BaseExFmtModel;
import com.kayak.dps.check.model.BaseExSeatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "报送接口字段配置服务", model = BaseExFmtModel.class)
public class BaseExFmtService {

    @Autowired
    private BaseExFmtDao baseExFmtDao;
    @API(desc = "查询报送接口字段配置信息", auth = APIAuth.YES)
    public SqlResult<BaseExFmtModel> findBaseExFmts(SqlParam<BaseExFmtModel> params) throws Exception {
        return baseExFmtDao.findBaseExFmts(params);
    }

    @API(desc = "删除报送接口字段配置信息", auth = APIAuth.YES)
    public  String delBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {
       baseExFmtDao.delBaseExFmt(params);
      return  RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "修改报送接口字段配置信息", auth = APIAuth.YES)
    public  int upBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {
        return   baseExFmtDao.upBaseExFmt(params);
    }
    @API(desc = "新增报送接口字段配置信息", auth = APIAuth.YES)
    public  int addBaseExFmt(SqlParam<BaseExFmtModel> params) throws Exception {
        return   baseExFmtDao.addBaseExFmt(params);
    }
}
