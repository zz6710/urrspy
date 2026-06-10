package com.kayak.pms.prod.service;

import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.prod.dao.T8ProdSyncDao;
import com.kayak.pms.prod.model.T8ProdSync;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "同步表数据服务", model = T8ProdSync.class)
public class T8ProdSyncService {

    @Autowired
    private T8ProdSyncDao t8ProdSyncDao;

    @API(desc = "查询同步表数据信息", auth = APIAuth.NO)
    public SqlResult<T8ProdSync> findT8ProdSyncs(SqlParam<T8ProdSync> params) throws Exception {
        params.setMakeSql(true);
        return t8ProdSyncDao.findT8ProdSyncs(params);
    }

    @API(desc = "添加同步表数据", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public String addT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        params.getModel().setCrtDate(date);
        params.getModel().setCrtTime(time);
        params.getModel().setCrtUser(username);
        Boolean flag = this.findTableIsExist(params);
        if (!flag) {
            return RequestSupport.updateReturnJson(false, "表名不存在", null).toString();
        }
        Boolean tableByName = findTableByName(params);
        if(tableByName){
            return RequestSupport.updateReturnJson(false, params.getModel().getTableName()+"数据库已存在", null).toString();
        }
        t8ProdSyncDao.addT8ProdSync(params);
        return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
    }

    @API(desc = "修改同步表数据", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public String updateT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//姓名
        params.getModel().setUpdDate(date);
        params.getModel().setUpdTime(time);
        params.getModel().setUpdUser(username);
        Boolean flag = this.findTableIsExist(params);
        if (!flag) {
            return RequestSupport.updateReturnJson(flag, "表名不存在", null).toString();
        }
        t8ProdSyncDao.updateT8ProdSync(params);
        return RequestSupport.updateReturnJson(true, "修改成功", null).toString();
    }

    @API(desc = "删除同步表数据", params = "id,table_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public int deleteT8ProdSync(SqlParam<T8ProdSync> params) throws Exception {
        return t8ProdSyncDao.deleteT8ProdSync(params).getEffect();
    }

    @API(desc = "查询表名", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public List<T8ProdSync> findTableName() throws Exception {
        return t8ProdSyncDao.findTableName();
    }

    //获取表字段  给下拉框使用
    @API(desc = "获取表所有字段", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public String getTableField(SqlParam<T8ProdSync> params) throws Exception {
        Boolean flag = findTableIsExist(params);
        if (!flag) {
          return   RequestSupport.updateReturnJson(true, "没有查询到数据", null).toString();
        }
        Map<String,Object> map=new HashMap<>();
        map.put("data",t8ProdSyncDao.getTableField(params));
        return   RequestSupport.updateReturnJson(true, "查询成功", map).toString();
    }

    //查询表是否存在
    @API(desc = "查询表是否存在", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public Boolean findTableIsExist(SqlParam<T8ProdSync> params) throws Exception {
        return t8ProdSyncDao.findTableIsExist(params);
    }

    //查询表是否存在
    @API(desc = "查询重复数据", params = "id,table_name,field_name,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user", auth = APIAuth.NO)
    public Boolean findTableByName(SqlParam<T8ProdSync> params) throws Exception {
        return t8ProdSyncDao.findTableByName( params);
    }


}
