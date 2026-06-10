package com.kayak.web.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.exception.WorkflowException;
import com.kayak.utils.SysUtil;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.service.IWfTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 工作流任务管理
 *
 * @author yuanjinqiao
 * @createTime 2022/3/10 00:12
 */
@Slf4j
@Api(tags = "任务管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/task")
@ApiSupport(order = 7)
public class WfTaskController {

    private final IWfTaskService flowTaskService;

    @ApiOperation(value = "撤回申请", response = WfTaskVo.class)
    @PostMapping(value = "/revokeProcess.json")
    @ApiOperationSupport(includeParameters = {"bo.procInsId"})
    public R revokeProcess(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getProcInsId())) {
            return R.fail("参数错误！");
        }
        flowTaskService.revokeProcess(bo);
        return R.ok();
    }

    @ApiOperation(value = "审批任务")
    @PostMapping(value = "/complete.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.uploadFiles"})
    public R complete(@RequestBody WfTaskBo bo) throws Exception {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId())) {
            return R.fail("参数错误！");
        }
        flowTaskService.complete(bo);
        return R.ok();
    }

    @ApiOperation(value = "退回任务(退回到上一步)")
    @PostMapping(value = "/return.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.uploadFiles"})
    public R taskReturn(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId())) {
            return R.fail("参数错误！");
        }
        flowTaskService.taskReturn(bo);
        return R.ok();
    }

    @ApiOperation(value = "驳回任务")
    @PostMapping(value = "/reject.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.targetKey", "bo.uploadFiles"})
    public R taskReject(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId(), bo.getTargetKey())) {
            return R.fail("参数错误！");
        }
        flowTaskService.taskReject(bo);
        return R.ok();
    }

    @ApiOperation(value = "拒绝任务")
    @PostMapping(value = "/refuse.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.uploadFiles"})
    public R taskRefuse(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId())) {
            return R.fail("参数错误！");
        }
        flowTaskService.taskRefuse(bo);
        return R.ok();
    }

    @ApiOperation(value = "获取所有可驳回的节点")
    @PostMapping(value = "/rejectList.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId"})
    public TableDataInfo<WfTaskVo> findRejectTaskList(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId())) {
            throw new WorkflowException("参数错误！");
        }
        return TableDataInfo.build(flowTaskService.findRejectTaskList(bo));
    }

    @ApiOperation(value = "委派任务")
    @PostMapping(value = "/delegate.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.userId", "bo.uploadFiles"})
    public R taskDelegate(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId(), bo.getUserId())) {
            return R.fail("参数错误！");
        }
        if (StrUtil.equalsAny(SysUtil.getCurrentUserId(), bo.getUserId())) {
            return R.fail("不能委派给自己！");
        }
        flowTaskService.taskDelegate(bo);
        return R.ok();
    }

    @ApiOperation(value = "转办任务")
    @PostMapping(value = "/transfer.json")
    @ApiOperationSupport(includeParameters = {"bo.taskId", "bo.procInsId", "bo.comment", "bo.userId", "bo.uploadFiles"})
    public R taskTransfer(@RequestBody WfTaskBo bo) {
        if (ObjectUtil.hasEmpty(bo.getTaskId(), bo.getProcInsId(), bo.getUserId())) {
            return R.fail("参数错误！");
        }
        if (StrUtil.equalsAny(SysUtil.getCurrentUserId(), bo.getUserId())) {
            return R.fail("不能转办给自己！");
        }
        flowTaskService.taskTransfer(bo);
        return R.ok();
    }

    @ApiOperation(value = "获取流程执行过程")
    @PostMapping("/flowViewer/{procInsId}.json")
    public R getFlowViewer(@ApiParam("流程实例id") @NotNull(message = "流程实例id不能为空") @PathVariable("procInsId") String procInsId) {
        return R.ok(flowTaskService.getFlowViewer(procInsId));
    }

    @ApiOperation(value = "获取按钮配置")
    @PostMapping("/btns/{procDefId}/{taskDefKey}.json")
    public TableDataInfo getBtns(
            @ApiParam("流程定义id") @NotNull(message = "流程定义id不能为空") @PathVariable("procDefId") String procDefId,
            @ApiParam("任务定义key") @NotNull(message = "任务定义key不能为空") @PathVariable("taskDefKey") String taskDefKey) {
        return TableDataInfo.build(flowTaskService.getBtns(procDefId, taskDefKey));
    }

    @ApiOperation(value = "触发接收任务")
    @PostMapping("/triggerReceiveTask/{procInsId}/{receiveTaskDefKey}.json")
    public R triggerReceiveTask(
            @ApiParam("流程实例id") @NotNull(message = "流程实例id不能为空") @PathVariable("procInsId") String procInsId,
            @ApiParam("接收任务定义id") @NotNull(message = "接收任务定义id不能为空") @PathVariable("receiveTaskDefKey") String receiveTaskDefKey) {
        flowTaskService.triggerReceiveTask(procInsId, receiveTaskDefKey);
        return R.ok();
    }

}
