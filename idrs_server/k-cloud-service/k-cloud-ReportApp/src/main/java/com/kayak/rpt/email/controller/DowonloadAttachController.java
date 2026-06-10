package com.kayak.rpt.email.controller;

import com.kayak.context.EmailThreadPoolProperties;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.report.util.DownloadUtil;
import com.kayak.rpt.email.service.EmailSendService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;
@Slf4j
@RestController
public class DowonloadAttachController extends BaseController {

    @Autowired
    EmailSendService emailSendService;
    @Autowired
    private EmailThreadPoolProperties emailThreadPoolProperties;
    /**
     * 单个文件下载   　下载文件最终需要从S3下载，本地存放的是S3下载的文件，页面下载完执行删除。
     * @param requestSupport
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/dowonloadAttach/attachDownloadSingle.json")
    public String attachDownloadSingle(RequestSupport requestSupport,HttpServletResponse response) throws Exception {
        Map<String, Object> params = requestSupport.getParameters();
        //参照的配置中可能包含文件名
        String osPath = "80000080008"; //系统表中配置的 mac系统中 本地路径
        String winPath = "80000080009"; //系统表中配置的 windows系统中 本地路径
//        String fileName = "Supcan-Setup113.8.exe";
        //页面附件列表的单个远程文件，下载实际从服务器本地取的文件
        String fileName = params.getOrDefault("fileName","").toString();
        //远程S3文件名
        String remoteFilePath = params.getOrDefault("filePath","").toString();
        //先从S3下载文件到本地
        String localFilePath = emailThreadPoolProperties.getRptEmailtempLocalFilePath() + fileName;
        try {
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remoteFilePath,localFilePath);
            log.info("从远程S3下载文件到本地 remoteFilePath:"+remoteFilePath+"; localFilePath"+localFilePath);
        } catch (Exception e) {
            log.error("组装邮件附件时，从S3下载附件失败。S3路径为【{}】",remoteFilePath+"; Exception:"+e.getMessage());
            return RequestSupport.updateReturnJson(false, "文件不存在", null).toString();
        }

        String rootPath = DownloadUtil.init(osPath, winPath);
        if(!new File(rootPath  + fileName).exists()){
            String ftpPath = SysUtil.getSystemParamsByParaid("70000010011");
            //从远程文件盘如S3 下载文件到本地盘的方法
//            FileTransfer transfer= FileTransferHelpler.getTransfer();
//            FileTransfer transfer = new FileTransferHelpler().getTransfer();
//            log.info("从远程S3下载文件到本地 remoteFilePath:"+remoteFilePath+"; localFilePath"+rootPath+fileName);
//            transfer.downloadFileAndDisconnect(remoteFilePath,rootPath+fileName);
        }
        //已有的  从系统本地盘，通过页面文件流传输 下载到用户本地盘的方法
        DownloadUtil.downloadFile(localFilePath, fileName ,response);
        //删除服务器本地文件
        emailSendService.deleteLocalFile(rootPath+fileName);
        return RequestSupport.updateReturnJson(true, "下载成功", null).toString();

    }



    /**
     * 批量文件下载    待完善
     * @param requestSupport
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/attachDownloadBatch.json")
    public void attachDownloadBatch(RequestSupport requestSupport,HttpServletResponse response) throws Exception {
        //　验证　params 是否可以获取到请求参数
        Map<String, Object> params = requestSupport.getParameters();
        // 　注意，参照的配置中可能包含文件名
        String osPath = "80000080008"; //系统表中配置的 mac系统中 本地路径
        String winPath = "80000080009"; //系统表中配置的 windows系统中 本地路径
//        String fileName = "Supcan-Setup113.8.exe";
        String rootPath = DownloadUtil.init(osPath, winPath);



        //获取文件名及文件路径列表   把邮件日志Id传入
        String  id = params.getOrDefault("id","").toString();
        // 查询获取附件列表

        // 循环，批量下载文件。
        String fileName = params.getOrDefault("fileName","").toString();
        //含远程文件名
        String remoteFilePath = params.getOrDefault("filePath","").toString();




        if(!new File(rootPath  + fileName).exists()){
            String ftpPath = SysUtil.getSystemParamsByParaid("70000010011");
            //从远程文件盘如S3 下载文件到本地盘的方法
//            FileTransfer transfer= FileTransferHelpler.getTransfer();
            FileTransfer transfer = new FileTransferHelpler().getTransfer();
            transfer.downloadFileAndDisconnect(remoteFilePath,rootPath+fileName);
        }
        //已有的  从系统本地盘，通过页面文件流传输 下载到用户本地盘的方法
        DownloadUtil.downloadFile(rootPath+ fileName, fileName ,response);
    }


}
