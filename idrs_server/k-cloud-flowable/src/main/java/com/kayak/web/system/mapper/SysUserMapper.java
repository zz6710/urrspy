package com.kayak.web.system.mapper;

import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.system.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统用户Mapper接口
 *
 * @author yuanjinqiao
 * @date 2022-01-15
 */
public interface SysUserMapper extends BaseMapperPlus<SysUserMapper, SysUser, SysUser> {

    /**
     * 根据角色Id获取用户
     *
     * @param roleIds
     * @return
     */
    List<SysUser> getUserByRoleIds(@Param("roleIds") List<String> roleIds);
}
