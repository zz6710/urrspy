package com.kayak.pms.opFlow.engine.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.pms.opFlow.engine.AbstractService;
import com.kayak.pms.opFlow.engine.dao.SubmitParamsDao;
import com.kayak.pms.opFlow.engine.entity.SubmitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by daniel on 27/06/2017.
 */
@Service
@APIDefine(desc = "表单提交参数服务", model = SubmitParams.class)
public class SubmitParamsService extends AbstractService {

    @Autowired
    SubmitParamsDao submitParamsDao;

    @API(desc = "查询任务提交的表单参数", auth = APIAuth.NO)
    public String getSubmitParamsByTask(SqlParam<SubmitParams> param) throws Exception {
        return updateSuccess(submitParamsDao.getSubmitParamsByTask(param));
    }

    public void save(SubmitParams submitParams) throws Exception {
        submitParamsDao.save(submitParams);
    }

    public List<SubmitParams> getSubmitParamsByProcessInstanceId(String processInstanceId) {
        return submitParamsDao.getSubmitParamsByProcessInstanceId(processInstanceId);
    }
}
