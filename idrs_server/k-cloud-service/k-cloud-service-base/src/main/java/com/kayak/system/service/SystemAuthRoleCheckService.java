package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.system.dao.SystemAuthOpCheckDao;
import com.kayak.system.dao.SystemAuthRoleCheckDao;
import com.kayak.system.model.SystemAuthOpCheck;
import com.kayak.system.model.SystemAuthRoleCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@APIDefine(desc = "角色授权审批服务", model = SystemAuthRoleCheck.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SystemAuthRoleCheckService {

    private final SystemAuthRoleCheckDao systemAuthRoleCheckDao;

    @API(desc = "新增角色授权审批条件",auth = APIAuth.YES)
    public String add(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        try {
            systemAuthRoleCheckDao.add(params);
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, "新增失败", null).toString();
        }
        CacheUtil.freshenGateway();
        return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
    }

    @API(desc = "删除角色授权审批条件",auth = APIAuth.YES)
    public String delete(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        try {
            systemAuthRoleCheckDao.deleteById(params);
        }catch (Exception e){
            return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
        }
        CacheUtil.freshenGateway();
        return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
    }

    @API(desc = "查询角色授权审批条件")
    public SqlResult<SystemAuthRoleCheck> find(SqlParam<SystemAuthRoleCheck> params) throws Exception {
        params.setMakeSql(true);
        SqlResult<SystemAuthRoleCheck> result = systemAuthRoleCheckDao.find(params);
        return result;
    }
}
