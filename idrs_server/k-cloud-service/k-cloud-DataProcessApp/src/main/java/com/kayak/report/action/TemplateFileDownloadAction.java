package com.kayak.report.action;

import com.kayak.dps.app.model.ImportTemplateManage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/templateDownload")
public class TemplateFileDownloadAction extends BaseDownload{

    /**
     * 模板下载
     * @param request
     * @param response
     */
    @RequestMapping(value = "/downFileByReportId.json",produces = { "application/json;charset=UTF-8"})
    public void downLoad( HttpServletRequest request, HttpServletResponse response) throws Exception{
        String reportId = request.getParameter("reportId"); //报表ID
        ImportTemplateManage importTemplateManage = importTemplateManageService.getTemplateInfoByTableName(reportId);
        if(importTemplateManage != null){
            downFileFromOss(importTemplateManage); //从先OSS下载模板文件
            downFile(importTemplateManage, response); //将模板文件写入到输出流中
        }
    }
}
