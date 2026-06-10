package com.kayak.bak.business.service;

import com.kayak.bak.business.dao.BakLogDao;
import com.kayak.bak.model.po.SysBakLogPO;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class BakLogService {

    @Resource
    private BakLogDao bakLogDao;

    /**
     * 查询归档集合列表
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SysBakLogPO> getBakLogList(SqlParam<SysBakLogPO> params) throws Exception {
        return bakLogDao.getBakLogList(params);
    }

    /**
     * 删除归档记录
     * @param params
     * @return
     * @throws Exception
     */
    public void deleteBakConfig(SqlParam<SysBakLogPO> params) throws Exception {
        bakLogDao.deleteBakLog(params);
    }

}
