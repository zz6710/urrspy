package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.result.R;
import com.kayak.utils.StringUtils;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.service.ICalculateProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回调返回参数校验配置Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Validated
@Api(value = "流程预测相关接口", tags = {"流程预测相关接口"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/calculateProcess")
public class CalculateProcessController extends BaseController {
    @Autowired
    private ICalculateProcessService iCalculateProcessService;

    @ApiOperation(value = "获取预测的任务")
    @PostMapping(value = "/getCalculateTask.json")
    @ApiOperationSupport(includeParameters = {"bo.procKey", "bo.procInsId", "bo.variables"})
    public R getCalculateTask(@RequestBody WfTaskBo bo) {
        if (StringUtils.isEmpty(bo.getProcKey())) {
            return R.fail("procKey不能为空");
        }
        return R.ok(iCalculateProcessService.getCalculateTask(bo));
    }

    @ApiOperation(value = "获取预测申请节点的抄送人")
    @PostMapping(value = "/getCalculateCopy.json")
    @ApiOperationSupport(includeParameters = {"bo.procKey"})
    public R getCalculateCopy(@RequestBody WfTaskBo bo) {
        if (StringUtils.isEmpty(bo.getProcKey())) {
            return R.fail("procKey不能为空");
        }
        return R.ok(iCalculateProcessService.getCalculateCopy(bo));
    }

    @ApiOperation(value = "获取流程图走向")
    @PostMapping(value = "/getFlowViewRun.json")
    @ApiOperationSupport(includeParameters = {"bo.procKey", "bo.procInsId", "bo.variables"})
    public R getFlowViewRun(@RequestBody WfTaskBo bo) {
        if (StringUtils.isEmpty(bo.getProcKey())) {
            return R.fail("procKey不能为空");
        }
        return R.ok(iCalculateProcessService.getFlowViewRun(bo));
    }

    @ApiOperation(value = "获取流程图")
    @PostMapping(value = "/getFlowView.json")
    @ApiOperationSupport(includeParameters = {"bo.procKey"})
    public R getFlowView(@RequestBody WfTaskBo bo) {
        if (StringUtils.isEmpty(bo.getProcKey())) {
            return R.fail("procKey不能为空");
        }
        return R.ok(null, iCalculateProcessService.getFlowView(bo));
    }
}
