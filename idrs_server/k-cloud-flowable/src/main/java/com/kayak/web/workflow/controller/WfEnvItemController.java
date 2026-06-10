package com.kayak.web.workflow.controller;

import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.ValidList;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.validate.AddGroup;
import com.kayak.web.workflow.domain.bo.WfEnvItemBo;
import com.kayak.web.workflow.domain.vo.WfEnvItemVo;
import com.kayak.web.workflow.service.IWfEvnItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程参数项配置Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Validated
@Api(value = "流程参数项配置控制器", tags = {"流程参数项配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/envItem")
public class WfEnvItemController extends BaseController {

    private final IWfEvnItemService iWfEvnItemService;

    /**
     * 查询流程参数项配置列表
     */
    @ApiOperation("查询流程参数项配置列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfEnvItemVo> list(@MultiRequestBody WfEnvItemBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfEvnItemService.queryPageList(bo, pageQuery);
    }

    /**
     * 保存所有流程参数项
     */
    @ApiOperation("保存所有流程参数项")
    @PostMapping("/saveAll.json")
    public R<Void> saveAll(@Validated(AddGroup.class) @RequestBody ValidList<WfEnvItemBo> boList) {
        return toAjax(iWfEvnItemService.saveAll(boList, true) ? 1 : 0);
    }
}
