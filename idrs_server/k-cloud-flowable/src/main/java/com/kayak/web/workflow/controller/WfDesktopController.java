package com.kayak.web.workflow.controller;

import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.service.IWfDesktopService;
import com.kayak.web.workflow.service.IWfProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ty
 * @since 2023-05-15 15:41:26
 */
@Slf4j
@Api(tags = "主菜单流程管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/desktop")
public class WfDesktopController extends BaseController {

    private final IWfDesktopService desktopService;


    /**
     * 首页流程情况一览（已发起）
     * @param bo
     * @param pageQuery
     * @return
     */
    @ApiOperation(value = "获取待办列表", response = WfTaskVo.class)
    @PostMapping(value = "/ownList.json")
    public TableDataInfo<WfTaskVo> ownProcess(@MultiRequestBody WfTaskQueryBo bo, @MultiRequestBody PageQuery pageQuery) {
        return desktopService.queryPageOwnProcessList(bo, pageQuery);
    }
}
