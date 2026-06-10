package com.kayak.dps.ods.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.model.DownLoadFileInfo;
import com.kayak.dps.ods.dao.DownLoadFileDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
@APIDefine(desc = "下载过滤信息", model = DownLoadFileInfo.class)
public class DownLoadFileService {
    @Resource(name = "downLoadFileDao")
    private DownLoadFileDao downLoadFileDao;


    /**
     * 查询表名信息
     * @param params
     * @throws Exception
     */
    @API(desc = "查询表名信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<DownLoadFileInfo> findTableName(SqlParam<DownLoadFileInfo> params) throws Exception {
        return downLoadFileDao.findTableName(params);
    }

    /**
     * 查询对应表名字段信息
     * @param params
     * @throws Exception
     */
    @API(desc = "查询对应表名字段信息",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<DownLoadFileInfo> findColumnName(SqlParam<DownLoadFileInfo> params) throws Exception {
        return downLoadFileDao.findColumnName(params);
    }

}
