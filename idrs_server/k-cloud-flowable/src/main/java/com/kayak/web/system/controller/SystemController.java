package com.kayak.web.system.controller;

import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.factory.SystemServiceFactory;
import com.kayak.web.system.domain.SysRole;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.system.service.ISystemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

@Validated
@Api(value = "系统信息控制器", tags = {"系统信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/system")
public class SystemController extends BaseController {

    private final SystemServiceFactory systemServiceFactory;

    @ApiOperation("获取用户列表")
    @PostMapping(value = "/user/list.json")
    public TableDataInfo<SysUser> selectUserList(@RequestBody(required = false) SysUser sysUser) {
        ISystemService service = systemServiceFactory.createService();
        return TableDataInfo.build(service.selectUserList(sysUser));
    }

    @ApiOperation("获取角色列表")
    @PostMapping(value = "/role/list.json")
    public TableDataInfo<SysRole> selectRoleList() {
        ISystemService service = systemServiceFactory.createService();
        return TableDataInfo.build(service.selectRoleList());
    }

    @ApiOperation("根据用户id获取用户")
    @PostMapping(value = "/user/{userids}.json")
    public TableDataInfo<SysUser> selectRoleList(@ApiParam("主键串")
                                                 @NotEmpty(message = "主键不能为空")
                                                 @PathVariable String[] userids) {
        ISystemService service = systemServiceFactory.createService();
        return TableDataInfo.build(service.findUserByIds(Arrays.asList(userids)));
    }
}
