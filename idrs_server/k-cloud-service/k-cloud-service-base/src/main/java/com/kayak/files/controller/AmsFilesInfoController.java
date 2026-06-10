package com.kayak.files.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.files.model.AmsFilesInfo;
import com.kayak.files.service.AmsFilesInfoService;
import com.kayak.graphql.model.FetcherData;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RestController
public class AmsFilesInfoController extends BaseController {

    @Autowired
    AmsFilesInfoService amsFilesInfoService;

    @RequestMapping(value = "/fileManage/uploadFile.json", produces = {"application/json;charset=UTF-8"})
    public String uploadFile( @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        String fileNameParam = (String) parameters.get("fileName");
        if (file == null) {
            return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
        }
        //文件名
        String fileName = file.getOriginalFilename();
        log.info("getOriginalFilename:"+new String(file.getOriginalFilename().getBytes("ISO-8859-1"),"utf-8"));
        if (Tools.isEmpty(fileName)) {
            return RequestSupport.updateReturnJson(false, "文件名为空", null).toString();
        }
        //文件后缀
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = DateUtil.getTimestamp14() + "." + extension;
        //文件管理上传服务器根路径(linux)
        String rootPath = SysUtil.getSystemParamsByParaid("90000040001");
        String remoteRootPath = SysUtil.getSystemParamsByParaid("90000040002");
        if (Tools.isEmpty(rootPath) || Tools.isEmpty(remoteRootPath)) {
            return RequestSupport.updateReturnJson(false, "参数配置为空", null).toString();
        }
        String serverPath = rootPath + newFileName;
        File localFile = new File(serverPath);
        if(!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
        }
        //转存文件
        try {
            file.transferTo(localFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return RequestSupport.updateReturnJson(false, "上传文件失败：" + localFile, null).toString();
        }
        String ossPath = remoteRootPath + newFileName;
        try {
            FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
            fileTransfer.uploadFileAndDisconnect(serverPath, ossPath);
        } catch (Exception e) {
            log.error("文件上传oss失败", e);
            return RequestSupport.updateReturnJson(false, "文件上传oss失败", null).toString();
        }
        AmsFilesInfo amsFilesInfo = new AmsFilesInfo();
        amsFilesInfo.setFileName(fileNameParam);
        amsFilesInfo.setFileType(extension.toUpperCase(Locale.ROOT));
        amsFilesInfo.setServerPath(serverPath);
        amsFilesInfo.setOssPath(ossPath);
        amsFilesInfo.setUploadTime(DateUtil.getTimestamp19());
        FetcherData<AmsFilesInfo> params = new FetcherData<>(BeanUtil.beanToMap(amsFilesInfo), AmsFilesInfo.class);
        try {
            amsFilesInfoService.addAmsFilesInfo(params);
        } catch (Exception e) {
            return RequestSupport.updateReturnJson(false, "新增文件失败", null).toString();
        }
        return updateSuccess("操作成功！");
    }

    @RequestMapping(value = "/fileManage/download.json")
    public void download(HttpServletResponse response, @RequestParam(value = "id") String id) throws Exception {
        if (Tools.isEmpty(id)) {
            log.error("下载文件异常，缺少id参数.");
            return;
        }
        if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
            log.error("检测到sql注入异常");
            return;
        }
        List<AmsFilesInfo> list = amsFilesInfoService.findAmsFilesInfoById(id);
        if (list.isEmpty()) {
            return;
        }
        AmsFilesInfo amsFilesInfo = list.get(0);
        String servePath = amsFilesInfo.getServerPath();
        String ossPath = amsFilesInfo.getOssPath();
        File file = new File(servePath);
        // 从oss下载文件
        if (!file.exists() && Tools.isNotEmpty(ossPath)) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
            fileTransfer.downloadFileAndDisconnect(ossPath, servePath);
        }
        // 下载文件
        if (file.exists()) {
            try(FileInputStream input = new FileInputStream(file);
                OutputStream outputStream = response.getOutputStream()) {
                response.setContentType("application/octet-stream");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(amsFilesInfo.getFileName(),"UTF-8"));
                response.setContentLength((int) file.length());
                IOUtils.copy(input, outputStream);
            } catch (Exception e) {
                log.error("文件下载失败", e);
            }
        } else {
            throw new RuntimeException("file not exits.");
        }
    }

}
