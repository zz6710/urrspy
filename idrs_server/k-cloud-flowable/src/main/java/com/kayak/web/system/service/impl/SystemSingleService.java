package com.kayak.web.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.*;
import com.kayak.web.system.domain.*;
import com.kayak.web.system.mapper.SysOrgMapper;
import com.kayak.web.system.mapper.SysRoleMapper;
import com.kayak.web.system.mapper.SysUserMapper;
import com.kayak.web.system.mapper.SysUserRoleMapper;
import com.kayak.web.system.service.ISystemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjinqiao
 * @description 系统管理单体应用接口
 * @create 2022-09-07 15:48
 **/
@RequiredArgsConstructor
@Service
@Slf4j
public class SystemSingleService implements ISystemService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysOrgMapper sysOrgMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysUser> selectUserList(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(StringUtils.isNotEmpty(sysUser.getUsername()), SysUser::getUsername, sysUser.getUsername());
        queryWrapper.eq(StringUtils.isNotEmpty(sysUser.getUserid()), SysUser::getUserid, sysUser.getUserid());
        List<SysUser> userList = sysUserMapper.selectList(queryWrapper);
        return userList;
    }

    @Override
    public SysUser getUserInfo(String userid) {
        SysUser sysUser = sysUserMapper.selectById(userid);
        if (sysUser == null) {
            throw new WorkflowException("用户[" + userid + "]不存在");
        }
        return sysUser;
    }

    @Override
    public List<SysUser> getUserByRoleIds(List<String> roleIds) {
        if (CollectionUtil.isEmpty(roleIds)) {
            return Collections.emptyList();
        }
        List<SysUser> list = sysUserMapper.getUserByRoleIds(roleIds);
        return list;
    }

    @Override
    public List<SysRole> selectRoleList() {
        return sysRoleMapper.selectList();
    }

    @Override
    public List<String> findUserRoleByUserId(String userid) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUserRole::getUserid, userid);
        return sysUserRoleMapper.selectList(queryWrapper).stream().map(t -> t.getRoleid()).collect(Collectors.toList());
    }

    @Override
    public List<SysOrg> getLowerOrgs(String orgno) {
        LambdaQueryWrapper<SysOrg> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysOrg::getParentorgno, orgno);
        return sysOrgMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysUser> findUserByIds(Collection<String> userids) {
        if (CollectionUtil.isEmpty(userids)) {
            return Collections.EMPTY_LIST;
        }
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysUser::getUserid, userids);
        return sysUserMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysRole> findRoleByIds(Collection<String> allRoleId) {
        if (CollectionUtil.isEmpty(allRoleId)) {
            return Collections.EMPTY_LIST;
        }
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysRole::getRoleid, allRoleId);
        return sysRoleMapper.selectList(queryWrapper);
    }

    @Override
    public List<SysOrg> getOrg(String orgno) {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/org/getOrg.json", SysUtil.getCurrentUserId(), params);
        List list = (List) rspParams.get("rows");
        return BeanCopyUtils.toBeanList(list, SysOrg.class);
    }

    @Override
    public List<SystemParam> getSysParam(String paraid) {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> rspParams = (Map<String, Object>) RemoteInvokeUtil.requestPostForm("BaseServer", "/base/sys/getSysParam.json", SysUtil.getCurrentUserId(), MapUtil.builder(new HashMap<String, Object>()).put("paraid", JSONUtil.toJsonStr(paraid)).build());
        List list = (List) ((Map)rspParams.get("returndata")).get("rows");
        return BeanCopyUtils.toBeanList(list, SystemParam.class);
    }
}
