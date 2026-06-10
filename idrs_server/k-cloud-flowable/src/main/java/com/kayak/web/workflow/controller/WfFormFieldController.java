package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.exception.WorkflowException;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.bo.WfFormFieldBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.vo.WfFormFieldVo;
import com.kayak.web.workflow.service.IWfFormFieldService;
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
 * 单字段配置Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-02
 */
@Validated
@Api(value = "表单配置", tags = {"表单配置"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/formField")
@ApiSupport(order = 2)
public class WfFormFieldController extends BaseController {

    private final IWfFormFieldService iWfFormFieldService;

    /**
     * 查询单字段配置列表
     */
    @ApiOperation("查询单字段配置列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfFormFieldVo> list(@MultiRequestBody WfFormFieldBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfFormFieldService.queryPageList(bo, pageQuery);
    }

    /**
     * 新增单字段配置
     */
    @ApiOperation("新增单字段配置")
    @PostMapping("/add.json")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfFormFieldBo bo) {
        return toAjax(iWfFormFieldService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 获取单字段配置详细信息
     */
    @ApiOperation("获取单字段配置详细信息")
    @PostMapping("/get/{formFieldId}.json")
    public R<WfFormFieldVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("formFieldId") Long formFieldId) {
        return R.ok(iWfFormFieldService.queryById(formFieldId));
    }

    /**
     * 修改单字段配置
     */
    @ApiOperation("修改单字段配置")
    @PostMapping("/update.json")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfFormFieldBo bo) {
        return toAjax(iWfFormFieldService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除单字段配置
     */
    @ApiOperation("删除单字段配置")
    @PostMapping("/delete/{formFieldIds}.json")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] formFieldIds) {
        return toAjax(iWfFormFieldService.deleteWithValidByIds(Arrays.asList(formFieldIds), true) ? 1 : 0);
    }

    @ApiOperation(value = "获取可驳回的任务节点")
    @PostMapping("/listByXml.json")
    @ApiOperationSupport(includeParameters = {"bo.bpmnXml"})
    public TableDataInfo getRejectTaskList(@RequestBody WfRejectTaskBo bo) {
        if (StringUtils.isAnyEmpty(bo.getBpmnXml())) {
            throw new WorkflowException("流程xml不能为空");
        }
        return TableDataInfo.build(iWfFormFieldService.listByXml(bo));
    }
}
