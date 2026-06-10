package com.kayak.config.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.config.dao.KbatchTaskStepExecDao;
import com.kayak.config.model.KbatchTaskStepExec;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "清算任务步骤", model = KbatchTaskStepExec.class)
public class KbatchTaskStepExecService {

    @Autowired
    KbatchTaskStepExecDao taClearTaskStepExecDao;

    @API(desc="清算任务步骤信息查询", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<KbatchTaskStepExec> query(SqlParam<KbatchTaskStepExec> param)throws  Exception{
        return taClearTaskStepExecDao.queryByTargeCode(param);
    }

    @API(desc = "查询回滚任务", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<KbatchTaskStepExec> queryRevocation(SqlParam<KbatchTaskStepExec> params) throws Exception {
        params.setMakeSql(false);
        return taClearTaskStepExecDao.queryRevocation(params);
    }

}
