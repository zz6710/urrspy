package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.validate.EditGroup;
import com.kayak.scheduled.WorkFlowCallbackService;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.bo.WfBusiInfoBo;
import com.kayak.web.workflow.domain.vo.WfBusiInfoVo;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.service.IWfBusiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务审批Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
@Validated
@Api(value = "业务流程追踪", tags = {"业务流程追踪"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/busiInfo")
@ApiSupport(order = 9)
public class WfBusiInfoController extends BaseController {

    private final IWfBusiInfoService iWfBusiInfoService;

    private final WorkFlowCallbackService workFlowCallbackService;

    private final WfBusiInfoMapper wfBusiInfoMapper;

    /**
     * 查询业务审批列表
     */
    @ApiOperation("查询业务审批列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfBusiInfoVo> list(@MultiRequestBody WfBusiInfoBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfBusiInfoService.queryPageList(bo, pageQuery);
    }

    @ApiOperation("重新执行业务")
    @PostMapping("/execute.json")
    @ApiOperationSupport(includeParameters = {"bo.busiId"})
    public R<Void> execute(@Validated(EditGroup.class) @RequestBody WfBusiInfoBo bo) {
        WfBusiInfo wfBusiInfo = wfBusiInfoMapper.selectById(bo.getBusiId());
        if (wfBusiInfo == null) {
            return R.fail("业务数据不存在");
        }
        workFlowCallbackService.execute(wfBusiInfo);
        return R.ok();
    }

    @ApiOperation("将错误置为已处理")
    @PostMapping("/errorConfirm.json")
    @ApiOperationSupport(includeParameters = {"bo.busiId"})
    public R<Void> errorConfirm(@Validated(EditGroup.class) @RequestBody WfBusiInfoBo bo) {
        WfBusiInfo wfBusiInfo = wfBusiInfoMapper.selectById(bo.getBusiId());
        if (wfBusiInfo == null) {
            return R.fail("业务数据不存在");
        }
        return toAjax(iWfBusiInfoService.errorConfirm(wfBusiInfo) ? 1 : 0);
    }

}
