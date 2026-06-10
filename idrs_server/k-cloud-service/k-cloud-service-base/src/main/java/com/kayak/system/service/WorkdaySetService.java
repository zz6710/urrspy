package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.dao.WorkdayDao;
import com.kayak.system.model.WorkdayItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@APIDefine(desc = "工作日集合服务", model = WorkdayItem.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkdaySetService {

    private final WorkdayDao workdayDao;

    @API(desc = "查询工作日列表", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<WorkdayItem> find(SqlParam<WorkdayItem> params) throws Exception {
        return workdayDao.find(params);
    }

    @API(desc = "查询工作日最大最小值", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<WorkdayItem> findMaxMin(SqlParam<WorkdayItem> params) throws Exception {
        return workdayDao.findMaxMin(params);
    }
    
    
    @API(desc = "查询工作日列表", auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<WorkdayItem> findByProdId(SqlParam<WorkdayItem> params) throws Exception {
        return workdayDao.findByProdId(params);
    }
}