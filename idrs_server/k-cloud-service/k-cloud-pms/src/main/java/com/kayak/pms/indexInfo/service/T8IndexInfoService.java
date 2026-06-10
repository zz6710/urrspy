package com.kayak.pms.indexInfo.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.indexInfo.dao.T8IndexInfoDao;
import com.kayak.pms.indexInfo.model.T8IndexInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

/**
 * @program: k-cloud
 * @description: 指数信息服务
 * @author: WangZhenXin
 * @create: 2021-01-13 17:34
 * @memo 备注信息
 */
@APIDefine(desc = "指数信息服务", model = T8IndexInfo.class)
@Service
public class T8IndexInfoService {
    private static final Logger logger = LoggerFactory.getLogger(T8IndexInfoService.class);

    @Autowired
    private T8IndexInfoDao t8IndexInfoDao;


    @API(desc = "查询指数信息",auth = APIAuth.YES,operation = APIOperation.SELECT)
    public SqlResult<T8IndexInfo> find1(SqlParam<T8IndexInfo> param) throws Exception {
        return find(param);
    }

    @API(desc = "查询指数信息",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<T8IndexInfo> find(SqlParam<T8IndexInfo> param) throws Exception {
       return t8IndexInfoDao.find(param);
    }

    @API(desc = "新增指数信息",auth = APIAuth.YES,operation = APIOperation.INSTER)
    public String add(SqlParam<T8IndexInfo> param) throws Exception {
        Date now = new Date();
        param.getModel().setCreateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        param.getModel().setCreateTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        param.getModel().setCreateUserId((String) SysUtil.getSysUserParamValue("sys_user_userid"));
        param.getModel().setCreateUserName((String) SysUtil.getSysUserParamValue("sys_user_username"));
        int add = t8IndexInfoDao.add(param);
        if (add>0){
            return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
        }else {
            return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
        }
    }

    @API(desc = "修改指数信息",auth = APIAuth.YES,operation = APIOperation.UPDATE)
    public String update(SqlParam<T8IndexInfo> param) throws Exception {
        Date now = new Date();
        param.getModel().setUpdateDate(DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT));
        param.getModel().setUpdateTime(DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT));
        int add = t8IndexInfoDao.update(param);
        if (add>0){
            return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
        }else {
            return RequestSupport.updateReturnJson(false, "修改失败", null).toString();
        }
    }

    @API(desc = "删除指数信息",auth = APIAuth.YES,operation = APIOperation.DELETE)
    public String delete(SqlParam<T8IndexInfo> param) throws Exception {
        SqlRow sqlRow = t8IndexInfoDao.checkIndexInfo(param);
        String con = sqlRow.getString("con");
        if (Integer.parseInt(con)>0){
            return RequestSupport.updateReturnJson(false, "该指数已被引用", null).toString();
        }
        int add = t8IndexInfoDao.delete(param.getModel().getId());
        if (add>0){
            return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
        }else {
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }
    }

    @API(desc = "检查指数信息是否已被使用",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> checkIndexInfo(SqlParam<T8IndexInfo> param) throws Exception {
        SqlRow sqlRow = t8IndexInfoDao.checkIndexInfo(param);
        SqlResult<SqlRow> sqlResult = new SqlResult<>();
        sqlResult.setResults(sqlRow.size());
        sqlResult.setRows(Collections.singletonList(sqlRow));
        sqlResult.setDesensitized(false);
        return sqlResult;
    }


}
