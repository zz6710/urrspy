package com.kayak.web.workflow.controller;

import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.web.workflow.domain.bo.WfCopyBo;
import com.kayak.web.workflow.domain.bo.WfCopyTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfCopyVo;
import com.kayak.web.workflow.domain.vo.WfFormConfVO;
import com.kayak.web.workflow.service.IWfCopyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 抄送Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Validated
@Api(value = "抄送", tags = {"抄送"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/copy")
public class WfCopyController extends BaseController {

    private final IWfCopyService copyService;

    /**
     * 查询抄送列表
     */
    @ApiOperation("查询抄送列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfCopyVo> list(@MultiRequestBody WfCopyTaskQueryBo bo, @MultiRequestBody PageQuery pageQuery) {
        return copyService.queryPageList(bo, pageQuery);
    }

    @ApiOperation(value = "已阅")
    @PostMapping(value = "/read.json")
    @ApiOperationSupport(includeParameters = {"bo.copyId", "bo.read"})
    public R read(@RequestBody WfCopyBo bo) {
        if (ObjectUtil.hasEmpty(bo.getCopyId(), bo.getRead())) {
            return R.fail("参数错误！");
        }
        copyService.read(bo);
        return R.ok();
    }

    @ApiOperation(value = "抄送表单配置")
    @PostMapping(value = "/formConf/{procDefId}/{taskDefKey}.json")
    public R<WfFormConfVO> getFormConf(@ApiParam("流程定义id")
                                       @NotNull(message = "流程定义id不能为空")
                                       @PathVariable("procDefId") String procDefId,
                                       @ApiParam("任务定义key")
                                       @NotNull(message = "任务定义key不能为空")
                                       @PathVariable("taskDefKey") String taskDefKey) {
        return R.ok(copyService.getFormConf(procDefId, taskDefKey));
    }
}
