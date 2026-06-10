package com.kayak.web.workflow.controller;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import com.kayak.common.annotation.MultiRequestBody;
import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.common.validate.AddGroup;
import com.kayak.common.validate.EditGroup;
import com.kayak.web.workflow.domain.bo.WfBusConfigBo;
import com.kayak.web.workflow.domain.vo.WfBusConfigVo;
import com.kayak.web.workflow.service.IWfBusConfigService;
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
 * 业务流程Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@Validated
@Api(value = "业务配置", tags = {"业务配置"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/busConfig")
@ApiSupport(order = 5)
public class WfBusConfigController extends BaseController {

    private final IWfBusConfigService iWfBusConfigService;

    /**
     * 查询业务流程列表
     */
    @ApiOperation("查询业务流程列表")
    @PostMapping("/list.json")
    public TableDataInfo<WfBusConfigVo> list(@MultiRequestBody WfBusConfigBo bo, @MultiRequestBody PageQuery pageQuery) {
        return iWfBusConfigService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取业务流程详细信息
     */
    @ApiOperation("获取业务流程详细信息")
    @PostMapping("/get/{server}.json")
    public R<WfBusConfigVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("server") String server) {
        return R.ok(iWfBusConfigService.queryById(server));
    }

    /**
     * 新增业务流程
     */
    @ApiOperation("新增业务流程")
    @PostMapping("/add.json")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody WfBusConfigBo bo) {
        return toAjax(iWfBusConfigService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改业务流程
     */
    @ApiOperation("修改业务流程")
    @PostMapping("/update.json")
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody WfBusConfigBo bo) {
        return toAjax(iWfBusConfigService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除业务流程
     */
    @ApiOperation("删除业务流程")
    @PostMapping("/delete/{servers}.json")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable String[] servers) {
        return toAjax(iWfBusConfigService.deleteWithValidByIds(Arrays.asList(servers), true) ? 1 : 0);
    }
}
