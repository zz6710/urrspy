package com.kayak.pms.declare.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;

import com.kayak.pms.declare.model.MaterialTemplate;
import com.kayak.pms.declare.service.MaterialDocumentService;
import com.kayak.pms.declare.service.MaterialTemplateService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.FileUtil;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: k-cloud
 * @description: 信披-申报模板
 * @author: axin
 * @create: 2023-05-12 15:20
 * @memo 备注信息
 */
@RestController
@RequestMapping(value = "/materialController")
public class MaterialAction extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialAction.class);

    @Autowired
    MaterialTemplateService materialTemplateService;

    @Autowired
    MaterialDocumentService materialDocumentService;

    //模板上传
    @PostMapping(value = "/MaterialUploadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String materialUploadAction(@RequestParam(value = "file", required = false) MultipartFile[] files, HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            for (MultipartFile file : files) {

                //文件验证
                checkFile(file);

                //存储逻辑
                materialTemplateService.saveMaterialTemplate(file , parameters);

            }
        } catch (Exception e) {
            logger.info("上传文件失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "上传文件失败！" + e.getMessage(), null).toString();
        }

        return RequestSupport.updateReturnJson(true, "上传成功", null).toString();
    }


    @PostMapping(value = "/MaterialDownloadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String materialDownloadAction(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            String remotePath = "";
            //远端sftp文件服务器根路径
            remotePath = SysUtil.getSystemParamsByParaid("70000010012");
            parameters = materialTemplateService.getLocalPath(parameters);
            String fileServerPath = (String)parameters.get("versionPath");
            FileTransfer transfer=new FileTransferHelpler().getTransfer();
            transfer.downloadFileAndDisconnect(remotePath+File.separator+parameters.get("templateId")+File.separator+parameters.get("versionName"),fileServerPath);

            FileUtil.downFile(response, fileServerPath);
        }catch (Exception e){
            logger.info("上传下载失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件下载失败！" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件下载成功", null).toString();
    }

    //文档上传
    @PostMapping(value = "/DocumentUploadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String documentUploadAction(@RequestParam(value = "file", required = false) MultipartFile[] files, HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();
        try {
            for (MultipartFile file : files) {

                //文件验证
                checkFile(file);

                //存储逻辑
                materialDocumentService.uploadDocument(file , parameters);

            }
        } catch (Exception e) {
            logger.info("上传文件失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "上传文件失败！" + e.getMessage(), null).toString();
        }

        return RequestSupport.updateReturnJson(true, "上传成功", null).toString();
    }

    //下载单个文件
    @PostMapping(value = "/DocumentDownloadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String documentDownloadAction(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            String remotePath = "";
            //远端sftp文件服务器根路径
            remotePath = SysUtil.getSystemParamsByParaid("70000010013");
            String fileServerPath = (String)parameters.get("documentPath");
            FileTransfer transfer=new FileTransferHelpler().getTransfer();
            transfer.downloadFileAndDisconnect(remotePath+File.separator+parameters.get("prodCode")+ File.separator  + parameters.get("templateType")+ File.separator + parameters.get("documentName"),fileServerPath);

            FileUtil.downFile(response, fileServerPath);
        }catch (Exception e){
            logger.info("上传下载失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件下载失败！" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件下载成功", null).toString();
    }

    //下载所有子文件文件
    @PostMapping(value = "/SalesDownloadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String salesDownloadAction(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
            String prodCode = (String)parameters.get("prodCode");
            //保存文件根目录
            String root = FileUtil.getFileStorePath();
            //文件待打包路径：文件根目录 + “declare/document/” + 产品代码
            String filePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH + File.separator
                    + prodCode + File.separator  + parameters.get("templateType");
            //zip缓存路劲（随用随删）：文件根目录 + “declare/cachePath/” + 用户ID + 产品代码
            String zipFilePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.CACHEPATH + File.separator
                    + userid + File.separator  + prodCode;
            String fileName = prodCode + "_" + parameters.get("templateType") + "产品销售文件.zip";
            //打包文件
            FileUtil.fileToZip(filePath,zipFilePath,fileName,"GBK");
            FileUtil.downFile(response, zipFilePath + File.separator +fileName );
        }catch (Exception e){
            logger.info("上传下载失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件下载失败！" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件下载成功", null).toString();
    }

    //下载所有
    @PostMapping(value = "/DocumentDownloadActionAll.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String documentDownloadActionAll(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
            String prodCode = (String)parameters.get("prodCode");
            //保存文件根目录
            String root = FileUtil.getFileStorePath();
            //文件待打包路径：文件根目录 + “declare/document/” + 产品代码
            String filePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH + File.separator
                    + prodCode;

            String remotePath = "";
            //远端sftp文件服务器根路径
            remotePath = SysUtil.getSystemParamsByParaid("70000010013");
            //获取最新模板到本地
            FileTransfer transfer=new FileTransferHelpler().getTransfer();
            transfer.downDir(new File(remotePath+File.separator+prodCode),root + FileUtil.DECLAREPATH + File.separator + FileUtil.DOCUMENTPATH + File.separator);



            //zip缓存路劲（随用随删）：文件根目录 + “declare/cachePath/” + 用户ID + 产品代码
            String zipFilePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.CACHEPATH + File.separator
                    + userid + File.separator  + prodCode;
            String fileName = parameters.get("prodName") + prodCode + "_申报材料.zip";
            //打包文件
            FileUtil.fileToZip(filePath,zipFilePath,fileName,"GBK");
            FileUtil.downFile(response, zipFilePath + File.separator +fileName );
        }catch (Exception e){
            logger.info("上传下载失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件下载失败！" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件下载成功", null).toString();
    }


    //下载所有历史文件
    @PostMapping(value = "/HisDownloadAction.action",produces = { "application/json;charset=UTF-8"})
    @ResponseBody
    public String hisDownloadAction(HttpServletResponse response) {
        Map<String, Object> parameters = RequestSupport.getParameters();

        try {
            String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
            String prodCode = (String)parameters.get("prodCode");
            String templateType = (String)parameters.get("templateType");

            //保存文件根目录
            String root = FileUtil.getFileStorePath();

            //文件待打包路径：文件根目录 + “declare/history/” + 产品代码 + 模板类型
            String filePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.HISTORYPATH
                    + File.separator + prodCode + File.separator + templateType ;

            //zip缓存路劲（随用随删）：文件根目录 + “declare/cachePath/” + 用户ID + 产品代码
            String zipFilePath = root + FileUtil.DECLAREPATH + File.separator + FileUtil.CACHEPATH + File.separator
                    + userid + File.separator  + prodCode;
            String fileName = parameters.get("prodName") + prodCode + "_" + templateType +"_历史文件.zip";
            //打包文件
            FileUtil.fileToZip(filePath,zipFilePath,fileName,"GBK");
            FileUtil.downFile(response, zipFilePath + File.separator +fileName );
        }catch (Exception e){
            logger.info("上传下载失败：参数信息{},错误信息{}", parameters, e.getMessage());
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "文件下载失败！" + e.getMessage(), null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件下载成功", null).toString();
    }





    public void checkFile (MultipartFile file) throws Exception {

        //空文件验证
        if (file == null) {
            throw new Exception("上传文件为空");
        }

        // 请限制上传文件的大小(M)
        long size = file.getSize() / 1024 / 1024;
        if (size > 1024) {
            throw new Exception("上传文件不能大于1G");
        }
        //文件类型验证
        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        if (!fileName.endsWith(".docx") && !fileName.endsWith(".doc") && !fileName.endsWith(".pdf")) {
            throw new Exception("只能上传word文档和pdf");
        }
    }


}
