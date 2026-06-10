package com.kayak.pms.printTemp.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.pms.connect.file.KFileClient;
import com.kayak.pms.interceptor.MethodAnnotation;
import com.kayak.pms.printTemp.model.StaticTemp;
import com.kayak.pms.printTemp.model.StaticTempVersion;
import com.kayak.pms.printTemp.service.StaticTempService;
import com.kayak.pms.printTemp.service.StaticTempVersionService;
import com.kayak.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 静态文档上传Action
 * @author: WangZhenXin
 * @create: 2021-01-02 10:40
 * @memo 备注信息
 */
@RestController
public class StaticTempAction extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(StaticTempAction.class);

    @Autowired
    private KFileClient kFileClient;

    @Autowired
    private StaticTempService staticTempService;

    @Autowired
    private StaticTempVersionService staticTempVersionService;

    private static final String documentPath = "staticDocument";
    @MethodAnnotation(desc="静态文档管理-上传静态文档")
    @RequestMapping(value = "print/uploadStaticTemp.json",produces = { "application/json;charset=UTF-8"})
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws Exception {
        if (file == null) {
            return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
        }
        // 请限制上传文件的大小(M)
        long size = file.getSize() / 1024 / 1024;
        if (size > 1024) {
            return RequestSupport.updateReturnJson(false, "上传文件不能大于1G", null).toString();
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                || fileName.endsWith(".html")) {
            return RequestSupport.updateReturnJson(false, "禁止上传脚本", null).toString();
        }
        //获取参数
        Map<String, Object> params = RequestSupport.getParameters();
        //上传模板的默认版本号为V1.0
        String version = "V1.0";
        //文档类型
        String docType = (String) params.get("docType");
        //模板类型
        String tempType = (String) params.get("tempType");

        try {
            //保存产品文档信息
            StaticTemp staticTemp = new StaticTemp();
            staticTemp.setDocType(docType);
            staticTemp.setTempType(tempType);
            staticTemp.setRemark((String) params.get("remark"));
            staticTemp.setDistributorCode((String) params.get("distributorCode"));
            staticTemp.setT8TruteeInfoId((String) params.get("t8TruteeInfoId"));
            staticTemp.setTempName(fileName.substring(0, fileName.lastIndexOf(".")));
            //检查文档模板基础信息是否存在，不存在则新增
            Integer cont = staticTempService.checkStaticTemp(staticTemp);
            if (cont > 0) {
                return RequestSupport.updateReturnJson(false, "该产品文档已存在", null).toString();
            }

            String root = FileUtil.getFileStorePath();
            //拼接文件保存路径
            String staticDocumentPath = root + documentPath + "/" + docType + "/" +
                    tempType + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + "/" + version;
            //保存文件
            FileUtil.fileSaveToLocal(file, fileName, staticDocumentPath);
            //组装静态文档版本信息
            StaticTempVersion staticTempVersion = new StaticTempVersion();
            staticTempVersion.setVersion(version);
            staticTempVersion.setTempName(fileName);
            staticTempVersion.setFilePath(staticDocumentPath + "/" + fileName);
            staticTempVersion.setRemark((String) params.get("remark"));
            //保存文档模板信息
            staticTempService.saveStaticTempAndStaticTempVersion(staticTemp, staticTempVersion);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("上传静态文档失败：{}", e.getMessage());
            return RequestSupport.updateReturnJson(false, "上传静态文档失败", null).toString();
        }

        return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
    }


    @RequestMapping(value = "print/uploadStaticTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String uploadStaticTempVersion(@RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null) {
            return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
        }
        // 请限制上传文件的大小(M)
        long size = file.getSize()/1024/1024;
        if (size > 1024) {
            return RequestSupport.updateReturnJson(false, "上传文件不能大于1G", null).toString();
        }
        String fileName = file.getOriginalFilename().toLowerCase();
        if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                || fileName.endsWith(".html")) {
            return RequestSupport.updateReturnJson(false, "禁止上传脚本", null).toString();
        }
        //获取参数
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            StaticTempVersion staticTempVersion = new StaticTempVersion();
            String t8StaticTempId = (String) params.get("t8StaticTempId");
            //文档类型
            String docType = (String) params.get("docType");
            //模板类型
            String tempType = (String) params.get("tempType");

            //文档文件夹名称
            String tempName = (String) params.get("tempName");
            staticTempVersion.setT8StaticTempId(t8StaticTempId);
            //获取版本信息
            String tempVersion = staticTempVersionService.getNewestStaticTempVersion(t8StaticTempId);
            String root = FileUtil.getFileStorePath();
            String staticDocumentPath = root + documentPath + "/" + docType + "/" +
                    tempType + "/" + tempName + "/" + tempVersion;
            staticTempVersion.setVersion(tempVersion);
            staticTempVersion.setTempName(fileName);
            staticTempVersion.setFilePath(staticDocumentPath + "/" + fileName);
            staticTempVersion.setRemark((String) params.get("remark"));
            //保存文件
            FileUtil.fileSaveToLocal(file, fileName, staticDocumentPath);
            //保存之前先把之前版本作废
            staticTempVersionService.updateStaticTempVersionStatus(t8StaticTempId,staticTempVersion.getVersion());
            //保存新版本信息
            staticTempVersionService.saveStaticTempVersion(staticTempVersion);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("上传静态文档失败：{}", e.getMessage());
            return RequestSupport.updateReturnJson(false, "上传静态文档失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
    }

    /**
     * 下载产品模板对应版本
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/downloadStaticTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public void downloadStaticTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            //文档名称
            String fileName = (String) params.get("tempName");
            String filePath = (String) params.get("filePath");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //下载
            File file = new File(filePath);
            if (file.isDirectory() || !file.exists()) {
                throw new Exception("文件不存在!");
            }
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
