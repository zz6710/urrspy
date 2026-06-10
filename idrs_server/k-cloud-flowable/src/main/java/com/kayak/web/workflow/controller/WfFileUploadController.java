package com.kayak.web.workflow.controller;

import com.kayak.common.controller.BaseController;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.common.entity.result.R;
import com.kayak.web.workflow.domain.vo.WfAttachmentVo;
import com.kayak.web.workflow.service.IWfFileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.task.Attachment;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;

/**
 * 业务流程Controller
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@Validated
@Api(value = "附件上传", tags = {"附件上传"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/fileUpload")
public class WfFileUploadController extends BaseController {

    private final IWfFileUploadService iWfFileUploadService;

    @ApiOperation("是否开启附件上传")
    @PostMapping("/enableFileUpload/{procDefId}.json")
    public R<Object> enableFileUpload(@ApiParam(value = "流程定义Id") @NotEmpty(message = "流程定义Id不能为空") @PathVariable String procDefId) {
        return R.ok("获取成功", iWfFileUploadService.enableFileUpload(procDefId));
    }

    @ApiOperation("附件列表")
    @PostMapping("/list/{procInsId}.json")
    public TableDataInfo<WfAttachmentVo> list(@ApiParam(value = "流程实例Id") @NotEmpty(message = "流程实例不能为空") @PathVariable String procInsId) {
        return TableDataInfo.build(iWfFileUploadService.getFileUploadList(procInsId));
    }

    @ApiOperation("预览pdf")
    @PostMapping("/preview/pdf/{attachmentId}.json")
    public void previewPdf(@ApiParam(value = "附件id") @NotEmpty(message = "附件id不能为空") @PathVariable String attachmentId, HttpServletResponse response) {
        iWfFileUploadService.previewPdf(attachmentId, response);
    }

}
