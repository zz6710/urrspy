package com.kayak.web.workflow.controller;

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
import com.kayak.web.workflow.domain.bo.ProcessConfigBo;
import com.kayak.web.workflow.domain.bo.WfModelBo;
import com.kayak.web.workflow.domain.bo.WfRejectTaskBo;
import com.kayak.web.workflow.domain.vo.WfModelVo;
import com.kayak.web.workflow.service.IWfModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * @author yuanjinqiao
 * @createTime 2022/6/21 9:09
 */
@Slf4j
@Api(tags = "流程模型")
@RequiredArgsConstructor
@RestController
@RequestMapping("/model")
@ApiSupport(order = 3)
public class WfModelController extends BaseController {

    private final IWfModelService modelService;

    @ApiOperation(value = "查询流程模型列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfModelVo> list(@MultiRequestBody WfModelBo modelBo, @MultiRequestBody PageQuery pageQuery) {
        return modelService.list(modelBo, pageQuery);
    }

    @ApiOperation(value = "查询模型历史列表")
    @PostMapping("/historyList.json")
    public TableDataInfo<WfModelVo> historyList(@MultiRequestBody WfModelBo modelBo, @MultiRequestBody PageQuery pageQuery) {
        return modelService.historyList(modelBo, pageQuery);
    }

    /**
     * 获取流程模型详细信息
     */
    @ApiOperation(value = "查询流程模型详情信息")
    @PostMapping(value = "/get/{modelId}.json")
    public R<WfModelVo> getInfo(@ApiParam("主键") @NotNull(message = "主键不能为空") @PathVariable("modelId") String modelId) {
        return R.ok(modelService.getModel(modelId));
    }

    /**
     * 根据模型id查询模型xml
     */
    @ApiOperation("根据模型id查询模型xml")
    @PostMapping(value = "/bpmnXml/get/{modelId}.json")
    public R<String> getBpmnXml(@ApiParam("主键") @NotNull(message = "主键不能为空") @PathVariable("modelId") String modelId) {
        return R.ok("操作成功", modelService.queryBpmnXmlById(modelId));
    }

    /**
     * 新增流程模型
     */
    @ApiOperation("新增流程模型")
    @PostMapping("/add.json")
    public R<WfModelBo> add(@Validated(AddGroup.class) @RequestBody WfModelBo modelBo) {
        WfModelBo model = modelService.insertModel(modelBo);
        return R.ok(model);
    }

    /**
     * 修改流程模型
     */
    @ApiOperation("修改流程模型")
    @PostMapping("/update.json")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfModelBo modelBo) {
        modelService.updateModel(modelBo);
        return R.ok();
    }

    /**
     * 保存流程模型
     */
    @ApiOperation("保存流程模型")
    @PostMapping("/save.json")
    public R<String> save(@RequestBody WfModelBo modelBo) {
        modelService.saveModel(modelBo);
        return R.ok();
    }

    @ApiOperation("设为最新流程模型")
    @PostMapping("/latest/{modelId}.json")
    public R<?> latest(@NotEmpty(message = "主键不能为空") @PathVariable String modelId) {
        modelService.latestModel(modelId);
        return R.ok();
    }

    /**
     * 删除流程模型
     */
    @ApiOperation("删除流程模型")
    @PostMapping("/delete/{modelIds}.json")
    public R<String> remove(@ApiParam("主键串") @NotEmpty(message = "主键不能为空") @PathVariable String[] modelIds) {
        modelService.deleteByIds(Arrays.asList(modelIds));
        return R.ok();
    }

    @ApiOperation("部署流程模型")
    @PostMapping("/deploy/{modelId}.json")
    public R<Void> deployModel(@NotEmpty(message = "主键不能为空") @PathVariable String modelId) {
        return toAjax(modelService.deployModel(modelId));
    }

    @ApiOperation(value = "获取可驳回的任务节点")
    @PostMapping("/rejectTaskList.json")
    public TableDataInfo getRejectTaskList(@RequestBody WfRejectTaskBo bo) {
        if (StringUtils.isAnyEmpty(bo.getBpmnXml(), bo.getTaskDefKey())) {
            throw new WorkflowException("流程xml,任务定义不能为空");
        }
        String str = bo.getBpmnXml().replace( new String( Character.toChars(0) ),"");
        bo.setBpmnXml(str);
        if (StringUtils.isAnyEmpty(bo.getBpmnXml(), bo.getTaskDefKey())) {
            throw new WorkflowException("流程xml,任务定义不能为空");
        }
        return TableDataInfo.build(modelService.getRejectTaskList(bo));
    }

    @ApiOperation(value = "导入流程参数与表单")
    @PostMapping("/importConfig.json")
    public R importConfig(@RequestBody ProcessConfigBo bo) {
        modelService.importConfig(bo);
        return R.ok();
    }
}
