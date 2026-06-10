package com.kayak.report.action;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.system.RequestSupport;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/downloadStaticFile")
public class ReportTemplateExportAction {

    private static final Logger log = LoggerFactory.getLogger(ReportExportAction.class);

    @RequestMapping(value = "/downFileByName.json")
    public void downloadXml(HttpServletResponse response) throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        String fileName = parameters.get("fileName").toString(); //文件名
        download(fileName,response);
    }

    public void download(String templateFileNme, HttpServletResponse response) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        log.info("-----------------模板名称:" + templateFileNme);
        try {
            ClassLoader classLoader = ReportTemplateExportAction.class.getClassLoader();
            URL resourceUrl = classLoader.getResource("");
            String resourcePath = resourceUrl.getPath();
            String downLoadPath = resourcePath + templateFileNme;
            log.info("文件下载路径：" + downLoadPath);
            File localFile = new File(downLoadPath);
            long fileLength = localFile.length();
            //下载文件固定步骤
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(localFile.getName(), "utf-8"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            //response输出流
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024 * 100];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}
