package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.dao.DataCheckDao;
import com.kayak.system.model.DataCheckModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
@APIDefine(desc = "首页数据质量情况", model = DataCheckModel.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DataCheckService {

    @Resource
    private DataCheckDao dataCheckDao;

    @API(desc = "指标校验结果", auth = APIAuth.NO)
    public SqlResult<DataCheckModel> findIndicatorCheckRemind(SqlParam<DataCheckModel> params) throws Exception {
        return dataCheckDao.findIndicatorCheckRemind(params);

    }

    @API(desc = "指标校验结果(首页记录不通过和校验预警)", auth = APIAuth.YES)
    public SqlResult<DataCheckModel> findIndicatorCheck(SqlParam<DataCheckModel> params) throws Exception {
        return dataCheckDao.findIndicatorCheck(params);

    }
    @API(desc = "基础数据补录提醒", auth = APIAuth.YES)
    public SqlResult<DataCheckModel> findSubmitRemind(SqlParam<DataCheckModel> params) throws Exception {
        return dataCheckDao.findSubmitRemind(params);
    }
}
