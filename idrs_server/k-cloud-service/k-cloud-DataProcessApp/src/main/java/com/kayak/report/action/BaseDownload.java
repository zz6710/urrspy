package com.kayak.report.action;

import com.kayak.clear.utils.Tools;
import com.kayak.dps.app.model.ImportTemplateManage;
import com.kayak.dps.app.service.ImportTemplateManageService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

public class BaseDownload {

    protected static final Logger log = LoggerFactory.getLogger(TemplateFileDownloadAction.class);

    @Autowired
    protected ImportTemplateManageService importTemplateManageService;

    /**
     * 从OSS下载文件
     * @param templateInfo
     */
    public void downFileFromOss(ImportTemplateManage templateInfo) throws Exception{
        String pathFile = templateInfo.getTemplateFilePath() + templateInfo.getTemplateFileName();
        File file = new File(pathFile);
        String remoteFile = templateInfo.getOssFilePath();
        if (!file.exists() && Tools.strIsNotEmpty(remoteFile)) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remoteFile, pathFile);
        }
    }

    /**
     * 下载文件
     * @param templateInfo 导入模板对象
     * @param response 响应流
     * @throws Exception
     */
    public void downFile(ImportTemplateManage templateInfo, HttpServletResponse response) throws Exception{
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try{
            String downLoadPath = templateInfo.getTemplateFilePath() + templateInfo.getTemplateFileName();
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
        }finally {
            if (bis != null){
                bis.close();
            }

            if (bos != null){
                bos.close();
            }

        }
    }
}
