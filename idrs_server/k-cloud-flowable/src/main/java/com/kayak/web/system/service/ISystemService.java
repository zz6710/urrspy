package com.kayak.web.system.service;

import com.kayak.web.system.domain.SysOrg;
import com.kayak.web.system.domain.SysRole;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.system.domain.SystemParam;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author yuanjinqiao
 * @description
 * @create 2022-09-07 15:46
 **/
public interface ISystemService {
    /**
     * 查询用户列表
     *
     * @param sysUser
     * @return
     */
    List<SysUser> selectUserList(SysUser sysUser);

    /**
     * 查询单个用户信息
     *
     * @param userid
     * @return
     */
    SysUser getUserInfo(String userid);

    /**
     * 根据角色Id获取用户
     *
     * @param roleIds
     * @return
     */
    List<SysUser> getUserByRoleIds(List<String> roleIds);

    /**
     * 查询所有角色
     *
     * @return
     */
    List<SysRole> selectRoleList();

    /**
     * 查询用户的角色
     *
     * @return 返回角色id
     */
    List<String> findUserRoleByUserId(String userid);

    /**
     * 获取下级机构
     *
     * @param orgno
     * @return
     */
    List<SysOrg> getLowerOrgs(String orgno);

    /**
     * 根据用户id获取用户
     *
     * @param userids
     * @return
     */
    List<SysUser> findUserByIds(Collection<String> userids);

    /**
     * 根据角色id获取角色
     *
     * @param allRoleId
     * @return
     */
    List<SysRole> findRoleByIds(Collection<String> allRoleId);

    List<SysOrg> getOrg(String userid);

    List<SystemParam> getSysParam(String paraid);
}
