package com.kayak.web.workflow.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.kayak.common.constant.ProcessConstants;
import com.kayak.common.enums.AttachmentTypeEnum;
import com.kayak.common.exception.WorkflowException;
import com.kayak.converter.pdf.PdfConverter;
import com.kayak.utils.AuthObjectUtil;
import com.kayak.utils.FieldUtil;
import com.kayak.utils.SysUtil;
import com.kayak.utils.flow.ModelUtils;
import com.kayak.web.system.domain.SysUser;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.bo.WfUploadFile;
import com.kayak.web.workflow.domain.vo.WfAttachmentVo;
import com.kayak.web.workflow.service.IWfFileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 附件上传Service业务层处理
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfFileUploadServiceImpl implements IWfFileUploadService {
    private final RepositoryService repositoryService;

    private final TaskService taskService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    protected IdentityService identityService;

    @Override
    public String enableFileUpload(String procDefId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);
        StartEvent startEvent = ModelUtils.getStartEvent(bpmnModel);
        String enableFileUpload = startEvent.getAttributeValue(ProcessConstants.NAMASPASE, ProcessConstants.PROCESS_ENABLE_FILE_UPLOAD);
        return enableFileUpload;
    }

    @Override
    public List<WfAttachmentVo> getFileUploadList(String procInsId) {
        List<WfAttachmentVo> wfAttachmentVos = new ArrayList<>();
        List<Attachment> processInstanceAttachments = taskService.getProcessInstanceAttachments(procInsId);
        for (Attachment attachment : processInstanceAttachments) {
            WfAttachmentVo wfAttachmentVo = new WfAttachmentVo();
            wfAttachmentVo.setId(attachment.getId());
            wfAttachmentVo.setName(attachment.getName());
            wfAttachmentVo.setUrl(attachment.getUrl());
            wfAttachmentVo.setType(attachment.getType());
            wfAttachmentVo.setTime(attachment.getTime());
            wfAttachmentVo.setDescription(attachment.getDescription());
            wfAttachmentVo.setUploadUserId(attachment.getUserId());
            wfAttachmentVos.add(wfAttachmentVo);
        }
        AuthObjectUtil.complementUserInfo(wfAttachmentVos, MapUtil.builder(new HashMap<String, String>()).put(FieldUtil.noPrefix(WfAttachmentVo::getUploadUserId), FieldUtil.noPrefix(WfAttachmentVo::getUploadUserName)).build(), FieldUtil.noPrefix(SysUser::getUsername));
        return wfAttachmentVos;
    }

    @Override
    public void uploadFiles(WfTaskBo taskBo) {
        identityService.setAuthenticatedUserId(SysUtil.getCurrentUserId());
        List<WfUploadFile> uploadFiles = taskBo.getUploadFiles();
        if (CollectionUtil.isEmpty(uploadFiles)) {
            return;
        }
        for (WfUploadFile uploadFile : uploadFiles) {
            taskService.createAttachment(AttachmentTypeEnum.PROCESS.getType(), taskBo.getTaskId(), taskBo.getProcInsId(), uploadFile.getUploadName(), null, uploadFile.getUploadPath());
        }
    }

    @Override
    public void previewPdf(String attachmentId, HttpServletResponse response) {
        Attachment attachment = taskService.getAttachment(attachmentId);
        if (attachment == null) {
            throw new WorkflowException("文件不存在");
        }
        response.setContentType("application/pdf;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + attachment.getName() + "");
        try (
                ServletOutputStream out = response.getOutputStream();
                InputStream in = new FileInputStream(handleFile(attachment, response));
        ) {
            IOUtils.copy(in, out);
        } catch (Exception e) {
            log.error("文件{}预览失败", attachment.getName(), e);
            throw new WorkflowException(e.getMessage());
        }
    }

    private File handleFile(Attachment attachment, HttpServletResponse response) throws IOException {
        String url = attachment.getUrl();
        String fileExtension = url.substring(url.lastIndexOf("."));
        String inFileName = url;
        String outFileName = url.substring(0, url.lastIndexOf(".")) + ".pdf";
        if (".pdf".equals(fileExtension)) {
            return new File(uploadPath, inFileName);
        } else {
            //非pdf文件，如果已转换为pdf,直接返回转换后的文件
            File file = new File(uploadPath, outFileName);
            if (!file.exists()) {
                boolean converter = PdfConverter.converter(inFileName, outFileName, uploadPath);
                if (!converter) {
                    throw new WorkflowException("文件格式不支持预览");
                }
            }
            return file;
        }
    }
}
