package com.kayak.report.action;

import com.kayak.dps.app.model.ImportTemplateManage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 销售月报下载
 */
@RestController
@RequestMapping(value = "/saleMonthDownload")
public class SaleListDownloadAction extends BaseDownload{

    /**
     * 销售月表三张导出生成在一个excel分三个sheet
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/saleMonthListDownload.json",produces = { "application/json;charset=UTF-8"})
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String reportId = request.getParameter("reportId"); //报表ID
        String reportDate = request.getParameter("reportDate"); //报表日期
        ImportTemplateManage importTemplateManage = importTemplateManageService.getTemplateInfoByTableName(reportId);
        if(importTemplateManage != null){
            String fileName = importTemplateManage.getTemplateFileName(); //模板文件名
            importTemplateManage.setTemplateFileName("数据日期"+reportDate + fileName); //新文件名
            downFileFromOss(importTemplateManage); //从先OSS下载模板文件
            writeData(reportDate);
            downFile(importTemplateManage, response); //将模板文件写入到输出流中
        }
    }

    /**
     * 写入数据
     * @param reportDate 数据日期
     */
    private void writeData(String reportDate){

    }
}
