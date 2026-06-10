package com.kayak.rpt.nineAttachments.controller;

import com.alibaba.excel.EasyExcel;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.nineAttachments.service.NineAttachmentsLogService;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/nineAttachments")
public class NineAttachmentsController extends BaseController {
    @Autowired
    private NineAttachmentsLogService nineAttachmentsLogService;
    @RequestMapping(value = "/download.json")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        nineAttachmentsLogService.download(params,response);
    }



}
