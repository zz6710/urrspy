package com.kayak.web.workflow.service;

import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.WfAttachmentVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 附件上传Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
public interface IWfFileUploadService {

    String enableFileUpload(String procDefId);

    List<WfAttachmentVo> getFileUploadList(String procInsId);

    void uploadFiles(WfTaskBo taskBo);

    void previewPdf(String attachmentId, HttpServletResponse response);
}
