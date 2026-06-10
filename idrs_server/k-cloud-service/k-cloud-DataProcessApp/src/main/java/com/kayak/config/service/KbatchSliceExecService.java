package com.kayak.config.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.config.dao.KbatchSliceExecDao;
import com.kayak.config.model.KbatchSliceExec;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "分片任务信息查询", model = KbatchSliceExec.class)
public class KbatchSliceExecService {

    @Autowired
    KbatchSliceExecDao taTaskSliceExecDao;

    /**
     * 清算任务管理分片任务
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<KbatchSliceExec> query(SqlParam<KbatchSliceExec> param)throws  Exception{
        return taTaskSliceExecDao.queryByTargeCode(param);
    }

    public SqlResult<KbatchSliceExec> queryStepNo(SqlParam<KbatchSliceExec> param)throws  Exception{
        return taTaskSliceExecDao.queryStepNo(param);
    }

    public SqlResult<KbatchSliceExec> querySliceStatus(SqlParam<KbatchSliceExec> param)throws  Exception{
        return taTaskSliceExecDao.querySliceStatus(param);
    }

    public SqlResult<KbatchSliceExec> queryDatasource(SqlParam<KbatchSliceExec> param)throws  Exception{
        return taTaskSliceExecDao.queryDatasource(param);
    }

    public SqlResult<KbatchSliceExec> queryServerIp(SqlParam<KbatchSliceExec> param)throws  Exception{
        return taTaskSliceExecDao.queryServerIp(param);
    }

}
