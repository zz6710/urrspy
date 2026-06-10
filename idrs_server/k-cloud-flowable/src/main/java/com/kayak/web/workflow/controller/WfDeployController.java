package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.web.workflow.domain.bo.WfProcessBo;
import com.kayak.web.workflow.domain.bo.WfProcessStateBo;
import com.kayak.web.workflow.domain.vo.WfDeployVo;
import com.kayak.web.workflow.service.IWfDeployService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * @author yuanjinqiao
 * @createTime 2022/3/24 20:57
 */
@Slf4j
@Api(tags = "流程部署")
@RequiredArgsConstructor
@RestController
@RequestMapping("/deploy")
@ApiSupport(order = 4)
public class WfDeployController extends BaseController {

    private final IWfDeployService deployService;

    /**
     * 查询流程部署列表
     */
    @PostMapping("/list.json")
    @ApiOperation(value = "查询已部署流程列表")
    public TableDataInfo<WfDeployVo> list(@MultiRequestBody WfProcessBo processBo, @MultiRequestBody PageQuery pageQuery) {
        return deployService.queryPageList(processBo, pageQuery);
    }

    /**
     * 查询流程部署版本列表
     */
    @PostMapping("/publishList.json")
    @ApiOperation(value = "查询流程部署版本列表")
    public TableDataInfo<WfDeployVo> publishList(@MultiRequestBody WfProcessBo processBo,
                                                 @MultiRequestBody PageQuery pageQuery) {
        return deployService.queryPublishList(processBo.getProcessKey(), pageQuery);
    }

    @ApiOperation(value = "激活或挂起流程")
    @PostMapping(value = "/changeState.json")
    public R<Void> changeState(@RequestBody WfProcessStateBo wfProcessStateBo) {
        deployService.updateState(wfProcessStateBo);
        return R.ok();
    }

    @ApiOperation(value = "读取xml文件")
    @PostMapping("/bpmnXml/get/{definitionId}.json")
    public R<String> getBpmnXml(@ApiParam(value = "流程定义ID") @PathVariable(value = "definitionId") String definitionId) {
        return R.ok(null, deployService.queryBpmnXmlById(definitionId));
    }

    /**
     * 删除流程模型
     */
    @ApiOperation("删除流程部署")
    @PostMapping("/delete/{deployIds}.json")
    public R<String> remove(@ApiParam(value = "流程部署ids") @NotEmpty(message = "主键不能为空") @PathVariable String[] deployIds) {
        deployService.deleteByIds(Arrays.asList(deployIds));
        return R.ok();
    }

    /**
     * 覆盖流程模型
     */
    @ApiOperation("覆盖流程模型")
    @PostMapping("/override/{fromProcessDefId}/{toProcessDefId}.json")
    public R<String> override(@ApiParam(value = "原流程定义id") @NotEmpty(message = "原流程定义id不能为空") @PathVariable String fromProcessDefId,
                              @ApiParam(value = "迁移目标流程定义ID") @NotEmpty(message = "迁移目标流程定义不能为空") @PathVariable String toProcessDefId) {
        deployService.override(fromProcessDefId, toProcessDefId);
        return R.ok();
    }

}
