package com.kayak.web.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.*;
import com.kayak.web.system.domain.SysOrg;
import com.kayak.web.system.domain.SysRole;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.system.domain.SystemParam;
import com.kayak.web.system.service.ISystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author yuanjinqiao
 * @description 系统管理微服务接口
 * @create 2022-09-07 15:47
 **/
@RequiredArgsConstructor
@Service
@Slf4j
public class SystemMicroService implements ISystemService {
    @Override
    public List<SysUser> selectUserList(SysUser sysUser) {
        Map<String, Object> params = RequestSupport.getParameters();
        params.putAll(BeanUtil.beanToMap(sysUser));
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/user/select.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysUser.class);
    }

    @Override
    public SysUser getUserInfo(String userid) {
        if (StringUtils.isEmpty(userid)) {
            throw new WorkflowException("用户id不能为空");
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserid(userid);
        List<SysUser> userList = selectUserList(sysUser);
        if (CollectionUtil.isEmpty(userList)) {
            throw new WorkflowException("用户[" + userid + "]不存在");
        }
        return userList.get(0);
    }

    @Override
    public List<SysUser> getUserByRoleIds(List<String> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        Map<String, Object> params = RequestSupport.getParameters();
        params.put("roleIds", roleIds);
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/user/getUserByRoleIds.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysUser.class);
    }

    @Override
    public List<SysRole> selectRoleList() {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/role/select.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysRole.class);
    }

    @Override
    public List<String> findUserRoleByUserId(String userid) {
        if (StringUtils.isEmpty(userid)) {
            throw new WorkflowException("用户id不能为空");
        }
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/user/findUserRoleById.json", SysUtil.getCurrentUserId(), MapUtil.builder(new HashMap<String, Object>()).put("userid", userid).build());
        return (List<String>) rspParams.get("rows");
    }

    @Override
    public List<SysOrg> getLowerOrgs(String orgno) {
        if (StringUtils.isEmpty(orgno)) {
            throw new WorkflowException("机构代码不能为空");
        }
        Map<String, Object> params = RequestSupport.getParameters();
        params.put("orgno", orgno);
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/org/getLowerOrgs.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysOrg.class);
    }

    @Override
    public List<SysUser> findUserByIds(Collection<String> userids) {
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/user/findUserByIds.json", SysUtil.getCurrentUserId(), MapUtil.builder(new HashMap<String, Object>()).put("userids", JSONUtil.toJsonStr(userids)).build());
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysUser.class);
    }

    @Override
    public List<SysRole> findRoleByIds(Collection<String> roleId) {
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/role/findRoleByIds.json", SysUtil.getCurrentUserId(), MapUtil.builder(new HashMap<String, Object>()).put("roleids", JSONUtil.toJsonStr(roleId)).build());
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysRole.class);
    }

    public List<SysOrg> getOrg(String orgno) {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/org/getOrg.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysOrg.class);
    }

    public List<SystemParam> getSysParam(String paraid) {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/sys/getSysParam.json", SysUtil.getCurrentUserId(), MapUtil.builder(new HashMap<String, Object>()).put("paraid", JSONUtil.toJsonStr(paraid)).build());
        List list = (List) ((Map)rspParams.get("returndata")).get("rows");
        return BeanCopyUtils.toBeanList(list, SystemParam.class);
    }

}
