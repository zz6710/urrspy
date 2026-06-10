package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.utils.SysUtil;
import com.kayak.web.workflow.domain.bo.WfCopyBo;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfCopyVo;
import com.kayak.web.workflow.domain.vo.WfFormConfVO;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.service.ICalculateProcessService;
import com.kayak.web.workflow.service.IWfCopyService;
import com.kayak.web.workflow.service.IWfProcessService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 工作流流程管理
 *
 * @author yuanjinqiao
 * @createTime 2022/3/24 18:54
 */
@Slf4j
@Api(tags = "流程管理")
@RequiredArgsConstructor
@RestController
@RequestMapping("/process")
@ApiSupport(order = 6)
public class WfProcessController extends BaseController {

    private final IWfProcessService processService;

    @ApiOperation(value = "根据流程key启动流程实例")
    @PostMapping("/start.json")
    public R<String> start(
            @ApiParam(value = "变量集合,json对象") @RequestBody Map<String, Object> variables) {
        return processService.startProcess(variables);

    }

    @ApiOperation(value = "获取待办列表", response = WfTaskVo.class)
    @PostMapping(value = "/todoList.json")
    public TableDataInfo<WfTaskVo> todoProcess(@MultiRequestBody WfTaskQueryBo bo, @MultiRequestBody PageQuery pageQuery) {
        return processService.queryPageTodoProcessList(bo, pageQuery);
    }

    @ApiOperation(value = "获取已办列表", response = WfTaskVo.class)
    @PostMapping(value = "/finishedList.json")
    public TableDataInfo<WfTaskVo> finishedProcess(@MultiRequestBody WfTaskQueryBo bo, @MultiRequestBody PageQuery pageQuery) {
        return processService.queryPageFinishedProcessList(bo, pageQuery);
    }

    @ApiOperation(value = "发起流程追踪", response = WfTaskVo.class)
    @PostMapping(value = "/ownList.json")
    public TableDataInfo<WfTaskVo> ownProcess(@MultiRequestBody WfTaskQueryBo bo, @MultiRequestBody PageQuery pageQuery) {
        return processService.queryPageOwnProcessList(bo, pageQuery);
    }

    @ApiOperation(value = "历史任务详情", response = WfTaskVo.class)
    @PostMapping(value = "/hisTaskDetail/{procInsId}.json")
    public TableDataInfo<WfTaskVo> hisTaskDetail(@ApiParam("流程实例id") @NotNull(message = "流程实例id不能为空") @PathVariable("procInsId") String procInsId) {
        List<WfTaskVo> wfTaskVos = processService.historyTaskList(procInsId);
        return TableDataInfo.build(wfTaskVos);
    }

    @ApiOperation(value = "表单配置")
    @PostMapping(value = "/formConf/{procDefId}/{taskDefKey}.json")
    public R<WfFormConfVO> formConf(@ApiParam("流程定义id")
                                    @NotNull(message = "流程定义id不能为空")
                                    @PathVariable("procDefId") String procDefId,
                                    @ApiParam("任务定义key")
                                    @NotNull(message = "任务定义key不能为空")
                                    @PathVariable("taskDefKey") String taskDefKey) {
        return R.ok(processService.getFormConf(procDefId, taskDefKey));
    }

    @ApiOperation(value = "审批表单数据")
    @PostMapping(value = "/formData/{procInsId}.json")
    public R<Map<String, Object>> formData(@ApiParam("流程实例id")
                                           @NotNull(message = "流程实例id不能为空")
                                           @PathVariable("procInsId") String procInsId) {
        return R.ok(processService.getFormData(procInsId));
    }

    @ApiOperation(value = "表单字段显示json", response = WfTaskVo.class)
    @PostMapping(value = "/formLabelInfo/{procInsId}.json")
    public R<Map<String, Object>> formLabelInfo(@ApiParam("流程实例id")
                                                @NotNull(message = "流程实例id不能为空")
                                                @PathVariable("procInsId") String procInsId) {
        return R.ok(processService.getFormLabelInfo(procInsId));
    }
}
