package com.kayak.dps.app.action;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kayak.clear.utils.Tools;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.FileUtil;
import com.kayak.core.util.PublicUtils;
import com.kayak.dps.app.model.ImportTemplateManage;
import com.kayak.dps.app.model.ImportTemplateUploadModel;
import com.kayak.dps.app.service.ImportTemplateManageService;
import com.kayak.report.dao.BaseReportFileManageDao;
import com.kayak.report.model.BaseReportFileManage;
import com.kayak.rpt.datacompare.RptCmpService;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipOutputStream;

@RestController
public class ImportTemplateUploadController extends BaseController {

    @Autowired
    private ImportTemplateManageService importTemplateManageService;
    @Autowired
    private RptCmpService rptCmpService;    
    @Autowired
    private BaseReportFileManageDao baseReportFileManageDao;

    @Value("${oss.remote.path}")
    private String remotePath;

    @Value("${zg.query.reflect.upd}")
    private String reflectUpdSql;
    @Value("${zg.query.org_info.upd}")
    private String orgInfoUpdSql;
    @Value("${zg.query.asset_info.upd}")
    private String assetInfoUpdSql;
    @Value("${zg.query.memo.upd1}")
    private String detailsUpdSql1;
    @Value("${zg.query.memo.upd2}")
    private String detailsUpdSql2;
    /**
     * 报送数据导入-模板维护-模板上传
     * @param request
     * @param response
     * @param file
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/uploadFile.json", produces = {"application/json;charset=UTF-8"})
    public String uploadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> parameters = RequestSupport.getParameters();
        JSONObject params = JSONUtil.parseObj(parameters.get("params"));
        ImportTemplateUploadModel  uploadModel= initData();
        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        importTemplateManage.setSystemTableName(params.getStr("systemTableName"));
        importTemplateManage.setImportType(params.getStr("importType"));
        importTemplateManage.setTemplateName(params.getStr("templateName"));
        importTemplateManage.setRowStart(params.getStr("rowStart"));
        importTemplateManage.setColumnStart(params.getStr("columnStart"));
        importTemplateManage.setTemplateStatus("1");
        String version = "1.0";
        // 获取当前模板信息
        ImportTemplateManage oldTemplateInfo =  importTemplateManageService.getTemplateInfoByUpload(importTemplateManage);
        if(null !=oldTemplateInfo){
            version = new BigDecimal(oldTemplateInfo.getVersion()).add(new BigDecimal("0.1")).toString();
        }
        if (file != null) {
            //文件名
            String fileName = file.getOriginalFilename();
            //文件后缀
           String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!"xlsx".equalsIgnoreCase(extension) && !"xls".equalsIgnoreCase(extension) && !"et".equalsIgnoreCase(extension)) {
                return RequestSupport.updateReturnJson(false, "请上传Excel文件：" + fileName, null).toString();
            }
            String newFileName =   fileName.substring(0,fileName.lastIndexOf(".")) +"-"+version+"."+extension;
            String forUpload = uploadModel.getLocalPath() ;//临时路径，用于上传
            File localFile = new File(forUpload + newFileName);
            if(!localFile.getParentFile().exists()) {
                localFile.getParentFile().mkdirs();
            }
            //转存文件
            file.transferTo(localFile);
            // 存入oss
            String remoteFile;
            if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
                remoteFile = remotePath + "/" + newFileName;
            } else {
                remoteFile = remotePath + newFileName;
            }
            try {
                FileTransfer transfer =  FileTransferHelpler.getTransfer();
                transfer.uploadFileAndDisconnect(forUpload + newFileName, remoteFile);
            } catch (Exception e) {
                log.error("文件上传oss失败", e);
                return RequestSupport.updateReturnJson(false, "文件上传oss失败", null).toString();
            }
            importTemplateManage.setOssFilePath(remoteFile);
            importTemplateManage.setOssFilePath(remoteFile);
            importTemplateManage.setTemplateFilePath(forUpload);
            importTemplateManage.setTemplateFileName(newFileName);
            importTemplateManage.setVersion(version);
            importTemplateManageService.addTemplateInfoByUpload(importTemplateManage,oldTemplateInfo);
            // 自动配置字段信息
            if ("1".equals(SysUtil.getSystemParamsByParaid("is_auto_generate_field"))){
                // 横表
                if ("01".equals(importTemplateManage.getImportType())) {
                    importTemplateManageService.fieldAutoGenerate(importTemplateManage, localFile);
                }
            }
        }else {
            return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
        }
        return updateSuccess("导入成功！");
    }

     public ImportTemplateUploadModel initData() throws Exception {
         ImportTemplateUploadModel importTemplateUploadModel = new ImportTemplateUploadModel();
         String os = System.getProperty("os.name");
         if(os.toLowerCase().startsWith("win")){
             importTemplateUploadModel.setLocalPath("D:\\download\\");
         }else{
             importTemplateUploadModel.setLocalPath(SysUtil.getSystemParamsByParaid("70000010008"));
         }
         importTemplateUploadModel.setLineIp(SysUtil.getSystemParamsByParaid("70000010004"));
         importTemplateUploadModel.setRemotePath(SysUtil.getSystemParamsByParaid("70000010003"));
         importTemplateUploadModel.setSftpUserName(SysUtil.getSystemParamsByParaid("70000010005"));
         importTemplateUploadModel.setSftpUserPwd(SysUtil.getSystemParamsByParaid("70000010006"));
        return importTemplateUploadModel;
     }


    @RequestMapping(value = "/importTemplate/implData.json", produces = {"application/json;charset=UTF-8"})
    public String implData(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file)  {

        Map<String, Object> parameters = RequestSupport.getParameters();
        JSONObject params = JSONUtil.parseObj(parameters.get("params"));
        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        if(!Tools.strIsEmpty(params.getStr("tableName")) && !Tools.strIsEmpty(params.getStr("reportDate"))){
            params.putOnce("systemTableName", params.getStr("tableName"));
            params.putOnce("sysDataDate", params.getStr("reportDate"));
            importTemplateManage.setSysDataVersion("0");
        }
         importTemplateManage.setSysDataStatus("1");
        if (Tools.strIsEmpty(params.getStr("systemTableName"))) {
            return updateFailure("请选择系统表名");
        }
        if (Tools.strIsEmpty(params.getStr("sysDataDate"))) {
            return updateFailure("请选择基准日期");
        }
        importTemplateManage.setSystemTableName(params.getStr("systemTableName"));
        importTemplateManage.setSysDataDate(params.getStr("sysDataDate"));

        try {
            String fileName = file.getOriginalFilename();
            //文件后缀
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            File tempFile = File.createTempFile("prefix", "."+extension);
            file.transferTo(tempFile);
            //调用 Excel解析导入服务
            importTemplateManageService.implExcelData(1,importTemplateManage,tempFile,null);//importFlag=1每次新增
            // 删除临时文件
            tempFile.delete();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return updateFailure(e.getMessage());
        }

        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/importTemplate/implDataAndCompare.json", produces = {"application/json;charset=UTF-8"})
    public String implDataAndCompare(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile file)  {

        Map<String, Object> parameters = RequestSupport.getParameters();
        String return_msg = "";
        JSONObject params = JSONUtil.parseObj(parameters.get("params"));
        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        baseReportFileManageDao.checkDataDate(params);
        if(!Tools.strIsEmpty(params.getStr("tableName")) && !Tools.strIsEmpty(params.getStr("reportDate"))){
            params.putOnce("systemTableName", params.getStr("tableName"));
            params.putOnce("sysDataDate", params.getStr("reportDate"));//报告日期
            importTemplateManage.setSysDataVersion("0");
        }
        if(!Tools.strIsEmpty(params.getStr("dealDate"))){
            params.putOnce("dataDate", params.getStr("dealDate"));//数据日期
        }
            importTemplateManage.setSysDataStatus("0");
        if (Tools.strIsEmpty(params.getStr("systemTableName"))) {
            return updateFailure("请选择系统表名");
        }
        if (Tools.strIsEmpty(params.getStr("sysDataDate"))) {
            return updateFailure("请选择基准日期");
        }
        importTemplateManage.setSystemTableName(params.getStr("systemTableName"));
        importTemplateManage.setSysDataDate(params.getStr("sysDataDate"));
        String[] systables = new String[0];

        //判断交易信息登记是否要更新
        if (params.get("systemTableName").equals(baseReportFileManageDao.getTransTableName())) {
            try {
                updateTradeInfoReportData();
                log.info("更新交易信息登记及资产要素登记交易对手类型完成");
            } catch (Exception e) {
                log.info("更新交易信息登记及资产要素登记交易对手类型异常");
            }

        }

        // 查询是否为sheet页导入
        List<SqlRow> sqlRows = baseReportFileManageDao.findSystablesByMap(params);

        if(!sqlRows.isEmpty()){
            if(StringUtils.isNotEmpty(sqlRows.get(0).getString("export_table_id"))){
                systables = sqlRows.get(0).getString("export_table_id").split(",");
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            //文件后缀
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            File tempFile = File.createTempFile("prefix", "."+extension);
            file.transferTo(tempFile);
            if(systables.length > 0){
                for(int i = 0; i < systables.length; i++){
                    importTemplateManage.setSystemTableName(systables[i]);
                    String sheetName = baseReportFileManageDao.findSheetByTable(systables[i]);
                    if(StringUtils.isNotEmpty(sheetName)){
                        importTemplateManageService.implExcelData(2, importTemplateManage, tempFile, sheetName);////importFlag=2先删除后新增
                        //比较
                        rptCmpService.compare(params.getStr("reportDate"), systables[i]);
                    }
                }
                return_msg = "导入成功！";
            }else{
                importTemplateManageService.implExcelData(2, importTemplateManage, tempFile, null);////importFlag=2先删除后新增
                //调用 Excel解析导入服务
                new Thread(new Runnable() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        //比较
                        rptCmpService.compare(params.getStr("reportDate"), params.getStr("tableName"));
                    }
                }).start();
                return_msg = "导入成功！";
            }

            // 删除临时文件
            tempFile.delete();


        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return updateFailure(e.getMessage());
        }

        return updateSuccess(return_msg);
    }

    /**
     * 产品报告文件管理文件上传
     *
     * @param  request
     * @param  response
     * @param  multipartFile
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/importBaseReportFileMange.json", produces = {"application/json;charset=UTF-8"})
    public String importBaseReportFileMange(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile)  {
        File file = null;

        try {
            if (multipartFile == null) {
                return updateFailure("上传文件为空");
            }
            // 转换File
            File tmpFile = FileUtil.multipartFileToFile(multipartFile);
            file = new File(tmpFile.getAbsolutePath());
            // 存入文件列表
            importTemplateManageService.importBaseReportFileMange(file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        } finally {
            FileUtil.delFile(file);
        }

        return updateSuccess("导入成功！");
    }


    /**
     * 产品报告文件管理文件上传
     *
     * @param  request
     * @param  response
     * @param  multipartFile
     * @return String
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/importProdReportFileMange.json", produces = {"application/json;charset=UTF-8"})
    public String importProdReportFileMange(HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "file") MultipartFile multipartFile)  {
        Map<String, Object> parameters = RequestSupport.getParameters();
        String return_msg = "";
        JSONObject params = JSONUtil.parseObj(parameters.get("params"));
        String importmodel=params.getStr("importmodel");
        File file = null;
        String prod_msg="";
        try {
            if (multipartFile == null) {
                return updateFailure("上传文件为空");
            }
            // 转换File
            File tmpFile = FileUtil.multipartFileToFile(multipartFile);
            file = new File(tmpFile.getAbsolutePath());
            // 存入文件列表
            prod_msg=importTemplateManageService.importProdReportFileMange(file,importmodel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return updateFailure(e.getMessage());
        } finally {
            FileUtil.delFile(file);
        }
        if(prod_msg.length()>0){
            return updateFailure(prod_msg);
        }else{
            return updateSuccess("导入成功！");
        }

    }

    /**
     * 产品报告文件管理文件下载
     *
     * @param  response
     * @param  id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/downloadBaseReportFileMange.json",produces = {"application/json;charset=UTF-8"})
    public void downloadBaseReportFileMange(HttpServletResponse response, @RequestParam(value = "id") String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            log.error("下载文件异常，id参数id参数为空");
            return;
        }

        List<BaseReportFileManage> list = baseReportFileManageDao.findBaseReportFileManages(id, new BaseReportFileManage());
        if (CollectionUtils.isEmpty(list)) {
            log.error("下载文件异常，id参数查询结果为空");
            return;
        }
        // 单个文件不要压缩
        if (list.size() == 1) {
            String ossPath = list.get(0).getRemoteFile();
            String fileName = list.get(0).getFileName();

            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            File file = File.createTempFile("prefix", "."+extension);

            try {
                // 从oss下载文件
                if (com.kayak.core.util.Tools.isNotEmpty(ossPath)) {
                    FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
                    fileTransfer.downloadFileAndDisconnect(ossPath, file.getAbsolutePath());
                }
                // 下载文件
                FileUtil.downloadFile(file.getAbsolutePath(), fileName, response);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtil.delFile(file);
            }
        } else if (list.size() > 1) {
            // 多个文件压缩
            File zipFile = File.createTempFile("prefix", ".zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
                for (BaseReportFileManage baseReportFileManage : list) {
                    String ossPath = baseReportFileManage.getRemoteFile();
                    String fileName = baseReportFileManage.getFileName();

                    File tmpFile = File.createTempFile("prefix", fileName);
                    File file = new File(tmpFile.getParent() + File.separator + fileName);

                    try {
                        // 从oss下载文件
                        if (com.kayak.core.util.Tools.isNotEmpty(ossPath)) {
                            FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
                            fileTransfer.downloadFileAndDisconnect(ossPath, file.getAbsolutePath());
                        }
                        // 压缩文件
                        FileUtil.compress(file, zipOutputStream, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 删除临时文件
                        FileUtil.delFile(tmpFile);
                        FileUtil.delFile(file);
                    }
                }
                // 下载文件
                zipOutputStream.close();
                String fileName = "理财产品说明书_" + DateUtil.getNowDate() + ".zip";
                FileUtil.downloadFile(zipFile.getAbsolutePath(), fileName, response);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (zipFile != null) {
                    FileUtil.delFile(zipFile);
                }
            }
        }
    }

    /**
     * 产品报告文件管理文件下载报送文件
     *
     * @param  response
     * @param  id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/importTemplate/downloadBSBaseReportFileMange.json",produces = {"application/json;charset=UTF-8"})
    public void downloadBSBaseReportFileMange(HttpServletResponse response, @RequestParam(value = "id") String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            log.error("下载报送文件异常，id参数为空");
            return;
        }

        List<BaseReportFileManage> list = baseReportFileManageDao.findBaseReportFileManages(id, new BaseReportFileManage());
        if (CollectionUtils.isEmpty(list)) {
            log.error("下载报送文件异常，id参数查询结果为空");
            return;
        }
        // 单个文件直接压缩
        if (list.size() == 1) {
            File zipFile = File.createTempFile("prefix", ".zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
                BaseReportFileManage baseReportFileManage = list.get(0);
                String prodCd = baseReportFileManage.getProdCd();
                String ossPath = baseReportFileManage.getRemoteFile();
                String fileName = baseReportFileManage.getFileName();

                File tmpFile = File.createTempFile("prefix", fileName);
                File file = new File(tmpFile.getParent() + File.separator + fileName);

                try {
                    // 从oss下载文件
                    if (com.kayak.core.util.Tools.isNotEmpty(ossPath)) {
                        FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
                        fileTransfer.downloadFileAndDisconnect(ossPath, file.getAbsolutePath());
                    }
                    // 压缩文件
                    FileUtil.compressDir(zipOutputStream, "01" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "02" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "03" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "04" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "05" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "06" + File.separator);
                    FileUtil.compress(file, zipOutputStream, "07" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "08" + File.separator);
                    FileUtil.compressDir(zipOutputStream, "09" + File.separator);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    FileUtil.delFile(tmpFile);
                    FileUtil.delFile(file);
                }

                // 下载文件
                zipOutputStream.close();
                String zipName = prodCd + ".zip";
                FileUtil.downloadFile(zipFile.getAbsolutePath(), zipName, response);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtil.delFile(zipFile);
            }
        } else if (list.size() > 1) {
            // 多个文件先压缩单个，在汇总压缩
            File zipFile = File.createTempFile("prefix", ".zip");
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile))) {
                for (BaseReportFileManage baseReportFileManage : list) {
                    String prodCd = baseReportFileManage.getProdCd();
                    String ossPath = baseReportFileManage.getRemoteFile();
                    String fileName = baseReportFileManage.getFileName();

                    File tmpZip = File.createTempFile("prefix", ".zip");
                    File zip = new File(tmpZip.getParent() + File.separator + prodCd + ".zip");

                    try (ZipOutputStream zipOs = new ZipOutputStream(new FileOutputStream(zip))) {
                        File tmpFile = File.createTempFile("prefix", fileName);
                        File file = new File(tmpFile.getParent() + File.separator + fileName);

                        try {
                            // 从oss下载文件
                            if (com.kayak.core.util.Tools.isNotEmpty(ossPath)) {
                                FileTransfer fileTransfer = new FileTransferHelpler().getTransfer();
                                fileTransfer.downloadFileAndDisconnect(ossPath, file.getAbsolutePath());
                            }
                            // 压缩文件
                            FileUtil.compressDir(zipOs, "01" + File.separator);
                            FileUtil.compressDir(zipOs, "02" + File.separator);
                            FileUtil.compressDir(zipOs, "03" + File.separator);
                            FileUtil.compressDir(zipOs, "04" + File.separator);
                            FileUtil.compressDir(zipOs, "05" + File.separator);
                            FileUtil.compressDir(zipOs, "06" + File.separator);
                            FileUtil.compress(file, zipOs, "07" + File.separator);
                            FileUtil.compressDir(zipOs, "08" + File.separator);
                            FileUtil.compressDir(zipOs, "09" + File.separator);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            FileUtil.delFile(tmpFile);
                            FileUtil.delFile(file);
                        }
                        // 压缩文件
                        zipOs.close();
                        FileUtil.compress(zip, zipOutputStream, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        FileUtil.delFile(tmpZip);
                        FileUtil.delFile(zip);
                    }
                }
                // 下载文件
                zipOutputStream.close();
                String fileName = "产品报告文件管理_" + DateUtil.getNowDate() + ".zip";
                FileUtil.downloadFile(zipFile.getAbsolutePath(), fileName, response);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                FileUtil.delFile(zipFile);
            }
        }
    }

    /**
     * 下载 数据
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
//    @RequestMapping(value = "/print/downloadXPTempVersion.json",produces = { "application/json;charset=UTF-8"})
    @RequestMapping(value = "/importTemplate/downloadTempData.json",produces = { "application/json;charset=UTF-8"})
    public void downloadTempData(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        importTemplateManage.setId(parameters.get("id").toString());

        BufferedOutputStream outputStream = null;
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;

        String tempPathName= "";

        try{
            ImportTemplateManage templateInfo =  importTemplateManageService.getTemplateInfoById(importTemplateManage);
             tempPathName = templateInfo.getTemplateFilePath()+ DateUtil.getNowTime()+".xlsx";


            String pathFile = templateInfo.getTemplateFilePath() + templateInfo.getTemplateFileName();

            //读取excel模板
            File file = new File(pathFile);

            templateInfo.setSysDataDate(parameters.get("impDate").toString());
            templateInfo.setSysDataVersion(parameters.get("sysDataVersion").toString());


            XSSFWorkbook book  = importTemplateManageService.exportExcelData(templateInfo,file);


            FileOutputStream fos = new FileOutputStream(tempPathName);
            book.write(fos);
            fos.close();
            File bookFile = new File(tempPathName);

            response.addHeader("Content-Length",String.valueOf(bookFile.length()));
            fileInputStream = new FileInputStream(bookFile);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                deleteTemp(tempPathName);
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }

        }
    }

    public  void deleteTemp(String tempPathName){
        File bookFile = new File(tempPathName);
        // 检查文件是否存在
        if (bookFile.exists()) {
            // 删除文件并检查是否成功
            boolean success = bookFile.delete();
            if (success) {
                System.out.println("文件已成功删除。");
            } else {
                System.out.println("文件删除失败。");
            }
        } else {
            System.out.println("文件不存在。");
        }
    }

    /**
     * 下载产品模板对应版本
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
//    @RequestMapping(value = "/print/downloadXPTempVersion.json",produces = { "application/json;charset=UTF-8"})
    @RequestMapping(value = "/importTemplate/downloadFile.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersion(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        importTemplateManage.setId(parameters.get("id").toString());


        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;

        try{
            ImportTemplateManage templateInfo =  importTemplateManageService.getTemplateInfoById(importTemplateManage);
            if (templateInfo != null) {

//                response.addHeader("Access-Control-Expose-Headers","*");
//                response.setHeader("filename",templateInfo.getTemplateFileName());
//                response.setHeader("content-disposition", "attachment;filename=" + new String(templateInfo.getTemplateFileName().getBytes("GB2312"), "ISO8859-1"));

//
//                /**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
//                //远端服务器ip地址
//                String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
//                //远端服务器用户名
//                String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
//                //远端服务器密码
//                String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
//                //远端服务器信批文档模板sftp存储路径
//                String remotePath = SysUtil.getSystemParamsByParaid("70000010003");
//                String dealRemotePath = remotePath + disclosureModVersion.getId() + separate ;
//                //本机或本地服务器信批模板文档存储路径
//                String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
//                        ?SysUtil.getSystemParamsByParaid("70000010002")//本机存放根路径
//                        :SysUtil.getSystemParamsByParaid("70000010009");//服务器存放根路径
//                //文件名
//                String fileModName = disclosureModVersion.getDocName();
//                FileTransfer transfer=new FileTransferHelpler().getTransfer();
//                transfer.downloadFileAndDisconnect(dealRemotePath+fileModName,temPath+fileModName);

                //从本地路径输出前端head中响应IO
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
                response.addHeader("Content-Length",String.valueOf(file.length()));

                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                outputStream = new BufferedOutputStream(response.getOutputStream());
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.flush();
                    i = bufferedInputStream.read(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }

        }
    }



    /**
     * 下载产品模板对应版本
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
//    @RequestMapping(value = "/print/downloadXPTempVersion.json",produces = { "application/json;charset=UTF-8"})
    @RequestMapping(value = "/importTemplate/downloadFileByName.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersionByName(HttpServletResponse response) {

        Map<String, Object> parameters = RequestSupport.getParameters();

        ImportTemplateManage importTemplateManage = new ImportTemplateManage();
        importTemplateManage.setSystemTableName(parameters.get("systemTableName").toString());


        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;

        try{
            ImportTemplateManage templateInfo =  importTemplateManageService.getTemplateInfoByUpload(importTemplateManage);
            if (templateInfo != null) {

//                response.addHeader("Access-Control-Expose-Headers","*");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("filename",templateInfo.getTemplateFileName());
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(templateInfo.getTemplateFileName(), "UTF-8"));
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setCharacterEncoding("UTF-8");
//
//                /**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
//                //远端服务器ip地址
//                String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
//                //远端服务器用户名
//                String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
//                //远端服务器密码
//                String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
//                //远端服务器信批文档模板sftp存储路径
//                String remotePath = SysUtil.getSystemParamsByParaid("70000010003");
//                String dealRemotePath = remotePath + disclosureModVersion.getId() + separate ;
//                //本机或本地服务器信批模板文档存储路径
//                String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
//                        ?SysUtil.getSystemParamsByParaid("70000010002")//本机存放根路径
//                        :SysUtil.getSystemParamsByParaid("70000010009");//服务器存放根路径
//                //文件名
//                String fileModName = disclosureModVersion.getDocName();
//                FileTransfer transfer=new FileTransferHelpler().getTransfer();
//                transfer.downloadFileAndDisconnect(dealRemotePath+fileModName,temPath+fileModName);

                //从本地路径输出前端head中响应IO
                String pathFile = templateInfo.getTemplateFilePath() + templateInfo.getTemplateFileName();
                File file = new File(pathFile);

                response.addHeader("Content-Length",String.valueOf(file.length()));

                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                outputStream = new BufferedOutputStream(response.getOutputStream());
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, i);
                    outputStream.flush();
                    i = bufferedInputStream.read(buffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }

        }
    }

    /**
     * 更新交易登记报表
     * @throws Exception
     */
    public void updateTradeInfoReportData () throws Exception {
        String workday = PublicUtils.getSysWordDay();
        List<String> updSqlList = new ArrayList<>(Arrays.asList(reflectUpdSql,orgInfoUpdSql,assetInfoUpdSql,detailsUpdSql1,detailsUpdSql2));
        Map<String, Object> params = new HashMap<>();
        params.put("workday", workday);
        baseReportFileManageDao.executeUpdSql(updSqlList, params);
    }
//
//    @RequestMapping(value = "/uploadPPI.json", produces = {"application/json;charset=UTF-8"})
//    public String uploadPPI(HttpServletRequest request, HttpServletResponse response,
//                         @RequestParam(value = "file") MultipartFile file) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
//        List<ReportPPI> reportPPIS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPPI.class, true, null);
//        if (reportPPIS.size() < 4) {
//            return updateFailure("导入文件为空文件");
//        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品基本信息")) {
//            return updateFailure("导入模版格式错误！");
//        } else {
//            //移除第一第二标题行
//            reportPPIS.remove(0);
//            reportPPIService.importReportPPIData(reportPPIS, params);
//        }
//        return updateSuccess("导入成功！");
//    }
//
//    @RequestMapping(value = "/uploadPIE.json", produces = {"application/json;charset=UTF-8"})
//    public String uploadPIE(HttpServletRequest request, HttpServletResponse response,
//                         @RequestParam(value = "file") MultipartFile file) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
//        List<ReportPIE> reportPIES = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPIE.class, true, null);
//        if (reportPIES.size() < 2) {
//            return updateFailure("导入文件为空文件");
//        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品终止信息")) {
//            return updateFailure("导入模版格式错误！");
//        } else {
//            //移除第一第二标题行
//            reportPIES.remove(0);
//            reportPIEService.importReportPIEData(reportPIES, params);
//        }
//        return updateSuccess("导入成功！");
//    }

//    @RequestMapping(value = "/uploadPIB.json", produces = {"application/json;charset=UTF-8"})
//    public String uploadPIB(HttpServletRequest request, HttpServletResponse response,
//                         @RequestParam(value = "file") MultipartFile file) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
//        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
//        List<ReportPIB> reportPIBS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPIB.class, true, null);
//        if (reportPIBS.size() < 2) {
//            return updateFailure("导入文件为空文件");
//        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品起始募集信息")) {
//            return updateFailure("导入模版格式错误！");
//        } else {
//            //移除第一第二标题行
//            reportPIBS.remove(0);
//            reportPIBService.importReportPIBData(reportPIBS, params);
//        }
//        return updateSuccess("导入成功！");
//    }

//    @RequestMapping(value = "/download.json")
//    public void downloadPPISendFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        Map<String, Object> params = RequestSupport.getParameters();
//        String property = System.getProperty("os.name");
//        String root;
//        if (property.toLowerCase().startsWith("win")) {
//            root = CacheUtil.getSystemParam("80000080007");
//        } else {
//            root = CacheUtil.getSystemParam("80000080006");
//        }
//        root += "/";
//        params.put("root", root);
//        File file = new File(root);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        try {
//            String orgno = "C1086243000029";
//            String flagStr;
//            List<File> fileList = new ArrayList<>();
//            if ("mjDataSend".equals(params.get("sendType"))) {
//                fileGenerator.generatePCD1(params);
//                fileList.add(new File(root + "PCD1.dat"));
//                flagStr = "PCD";
//            } else if ("assetsPVDSend".equals(params.get("sendType"))) {
//                fileGenerator.generatePVD1(params);
//                fileGenerator.generatePVD3(params);
//                FileUtil.generateEmptyFile(root, "PVD2.dat");
//                flagStr = "PVD";
//                fileList.add(new File(root + "PVD1.dat"));
//                fileList.add(new File(root + "PVD2.dat"));
//                fileList.add(new File(root + "PVD3.dat"));
//            } else {
//                flagStr = "PPI";
//                FileUtil.generateEmptyFile(root, "PIB1.dat");
//                FileUtil.generateEmptyFile(root, "PIB2.dat");
//                FileUtil.generateEmptyFile(root, "PIE1.dat");
//                FileUtil.generateEmptyFile(root, "PPE1.dat");
//                FileUtil.generateEmptyFile(root, "PPB1.dat");
//                if ("prodSend".equals(params.get("sendType"))) {
//                    fileGenerator.generatePIB1(params);
//                    fileGenerator.generatePIB2(params);
//                } else if ("zjSend".equals(params.get("sendType"))) {
//                    fileGenerator.generatePPB1(params);
//                } else if ("prodStopSend".equals(params.get("sendType"))) {
//                    fileGenerator.generatePIE1(params);
//                } else if ("zjStopSend".equals(params.get("sendType"))) {
//                    fileGenerator.generatePPE1(params);
//                }
//                fileList.add(new File(root + "PIB1.dat"));
//                fileList.add(new File(root + "PIB2.dat"));
//                fileList.add(new File(root + "PIE1.dat"));
//                fileList.add(new File(root + "PPB1.dat"));
//                fileList.add(new File(root + "PPE1.dat"));
//            }
//
//            //生成压缩文件
//            File zipFile = new File(root + flagStr + orgno + ".zip");
//            MyZipCompressing.zipMutipleFiles(zipFile.getAbsolutePath(), fileList);
//            request.setAttribute("doc_name", zipFile.getName());
//            request.setAttribute("path", zipFile.getAbsolutePath());
//            FileUtil.download(request, response);
//        } catch (IOException e) {
//            log.error("下载人行报表报送数据异常: ", e);
//        }


//    }
}
