package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import com.kayak.web.workflow.domain.bo.WfSurrogateBo;
import com.kayak.web.workflow.domain.vo.WfSurrogateVo;
import com.kayak.web.workflow.service.IWfSurrogateService;
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
 * 任务代理Controller
 *
 * @author yuanjinqiao
 * @date 2022-10-03
 */
@Validated
@Api(value = "转审批", tags = {"转审批"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/surrogate")
@ApiSupport(order = 8)
public class WfSurrogateController extends BaseController {

    private final IWfSurrogateService iWfSurrogateService;

    /**
     * 查询任务代理列表
     */
    @ApiOperation("查询任务代理列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfSurrogateVo> list(@MultiRequestBody WfSurrogateBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfSurrogateService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取任务代理详细信息
     */
    @ApiOperation("获取任务代理详细信息")
    @PostMapping("/{id}.json")
    public R<WfSurrogateVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("id") Long id) {
        return R.ok(iWfSurrogateService.queryById(id));
    }

    /**
     * 新增任务代理
     */
    @ApiOperation("新增任务代理")
    @PostMapping("/add.json")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfSurrogateBo bo) {
        return toAjax(iWfSurrogateService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改任务代理
     */
    @ApiOperation("修改任务代理")
    @PostMapping("/update.json")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfSurrogateBo bo) {
        return toAjax(iWfSurrogateService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除任务代理
     */
    @ApiOperation("删除任务代理")
    @PostMapping("/delete/{ids}.json")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWfSurrogateService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
