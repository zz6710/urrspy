package com.kayak.web.workflow.controller;

import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.WfValidateConfig;
import com.kayak.web.workflow.service.IWfValidateConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回调返回参数校验配置Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Validated
@Api(value = "回调返回参数校验配置", tags = {"回调返回参数校验配置"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/validateConfig")
public class WfValidateConfigController extends BaseController {

    private final IWfValidateConfigService iWfValidateConfigService;

    /**
     * 查询回调返回参数校验配置列表
     */
    @ApiOperation("查询回调返回参数校验配置列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfValidateConfig> list(@MultiRequestBody WfValidateConfig bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfValidateConfigService.queryPageList(bo, pageQuery);
    }
}
