package com.kayak.report.action;

import com.kayak.utils.FileTransferHelpler;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.report.dao.ReportExportDao;
import com.kayak.report.util.DownloadUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Map;

@RestController
public class ReportExportAction {

    private static final Logger log = LoggerFactory.getLogger(ReportExportAction.class);

    @Autowired
    private ReportExportDao reportExportDao;

    private String rootPath;

    private void init() {
        String system = System.getProperty("os.name");
        try {
            if (system.toLowerCase().startsWith("win")) {
                rootPath = CacheUtil.getSystemParam("80000080005");
            } else {
                rootPath = CacheUtil.getSystemParam("80000080004");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/download.json")
    public void downloadXml(HttpServletResponse response) throws Exception {
        init();
        String system = System.getProperty("os.name");
        try {
            if (system.toLowerCase().startsWith("win")) {
                rootPath = CacheUtil.getSystemParam("80000080005");
            } else {
                rootPath = CacheUtil.getSystemParam("80000080004");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> params = RequestSupport.getParameters();
        String menuid = String.valueOf(params.get("menuid"));
        //报表涉及的数据库表的导出顺序数组
        String[] tableNames = {"sys_report_css", "sys_report_xml_css", "sys_report_xml", "sys_report_sql", "sys_report_xml_sql", "sys_report_condition"};
        String filename = reportExportDao.createApplicationConfigXML(tableNames, menuid, rootPath);
        params.put("doc_name", filename);
        this.download(params, response);
    }

    public void download(Map<String, Object> params, HttpServletResponse response) throws Exception {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //避免中文名字文件乱码
        String fileName = (String) params.get("doc_name");
        if ("".equals(fileName) || fileName == null) {

            fileName = (String) params.get("doc_name");
        }
        if (!fileName.contains(".")) {
            fileName += ".xls";
        }
        log.info("-----------------文件名称:" + fileName);
        try {
            String downLoadPath = rootPath + fileName;
            log.info("文件下载路径：" + downLoadPath);
            long fileLength = new File(downLoadPath).length();
            //下载文件固定步骤
            response.setContentType("application/x-octetstream;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
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
    /**
     * 描述：报表插件下载
     *
     * @author zhaojie
     */
    @RequestMapping(value = "/reportDownloadExe.json")
    public void reportDownloadExe(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String osPath = "80000080008";
        String winPath = "80000080009";
        String fileName = "Supcan-Setup113.8.exe";
        String rootPath = DownloadUtil.init(osPath, winPath);
        if(!new File(rootPath  + fileName).exists()){
            String ftpPath = SysUtil.getSystemParamsByParaid("70000010011");
                FileTransfer transfer=FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(ftpPath+fileName,rootPath+fileName);
        }
        DownloadUtil.downloadFile(rootPath+ fileName, fileName ,response);
    }
}
