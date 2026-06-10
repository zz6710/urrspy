package com.kayak.web.workflow.controller;

import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import com.kayak.web.workflow.domain.bo.WfEnvBo;
import com.kayak.web.workflow.domain.vo.WfEnvVo;
import com.kayak.web.workflow.service.IWfEvnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 流程参数配置Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-08
 */
@Validated
@Api(value = "流程参数配置控制器", tags = {"流程参数配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/env")
public class WfEnvController extends BaseController {

    private final IWfEvnService iWfEvnService;

    /**
     * 查询流程参数配置列表
     */
    @ApiOperation("查询流程参数配置列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfEnvVo> list(@MultiRequestBody WfEnvBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfEvnService.queryPageList(bo, pageQuery);
    }


    /**
     * 获取流程参数配置详细信息
     */
    @ApiOperation("获取流程参数配置详细信息")
    @PostMapping("/get/{envId}.json")
    public R<WfEnvVo> getInfo(@ApiParam("主键")
                                     @NotNull(message = "主键不能为空")
                                     @PathVariable("envId") Long envId) {
        return R.ok(iWfEvnService.queryById(envId));
    }

    /**
     * 新增流程参数配置
     */
    @ApiOperation("新增流程参数配置")
    @PostMapping("/add.json")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfEnvBo bo) {
        return toAjax(iWfEvnService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改流程参数配置
     */
    @ApiOperation("修改流程参数配置")
    @PostMapping("/update.json")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfEnvBo bo) {
        return toAjax(iWfEvnService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除流程参数配置
     */
    @ApiOperation("删除流程参数配置")
    @PostMapping("/delete/{envIds}.json")
    public R<Void> remove(@ApiParam("主键串")
                                       @NotEmpty(message = "主键不能为空")
                                       @PathVariable Long[] envIds) {
        return toAjax(iWfEvnService.deleteWithValidByIds(Arrays.asList(envIds), true) ? 1 : 0);
    }
}
