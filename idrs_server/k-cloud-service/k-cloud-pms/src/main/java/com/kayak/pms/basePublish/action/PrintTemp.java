package com.kayak.pms.basePublish.action;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aspose.words.*;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.graphql.model.FetcherData;
import com.kayak.pms.basePublish.dao.DisclosureModDao;
import com.kayak.pms.basePublish.dao.DisclosureModVersionDao;

import com.kayak.pms.basePublish.dao.DisclosureRuleDao;
import com.kayak.pms.basePublish.model.*;
import com.kayak.pms.basePublish.service.DisclosureModColumnService;
import com.kayak.pms.basePublish.service.DisclosureModService;
import com.kayak.pms.basePublish.service.DisclosureModVersionService;
import com.kayak.pms.basePublish.service.DisclosureSourceService;
import com.kayak.pms.connect.utils.FileUtil;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeProcessDao;
import com.kayak.pms.disclosureControl.dao.DisclosureNoticeVersionDao;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeDocService;
import com.kayak.pms.disclosureControl.service.DisclosureNoticeService;
import com.kayak.pms.global.constants.XpStatus;
import com.kayak.pms.interceptor.MethodAnnotation;
import com.kayak.pms.printTemp.utils.UploadUtils;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.OnlineUtils;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.docx4j.TraversalUtil;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.finders.RangeFinder;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.org.apache.poi.util.IOUtils;
import org.docx4j.wml.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class PrintTemp extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PrintTemp.class);
    //文件分隔符
    private static final String separate = "/";
    //分隔符
    private static final String separate2 = ":";
    //分隔符3
    private static final String separate3 = "//";

    //在线编辑引用的jquery名称
    private static final String jqueryName = "jquery.min.js";

    @Autowired
    private DisclosureModVersionService disclosureModVersionService;

    @Autowired
    private DisclosureSourceService disclosureSourceService;

    @Autowired
    private DisclosureModService disclosureModService;

    @Autowired
    private DisclosureModDao disclosureModDao;

    @Autowired
    private DisclosureModColumnService disclosureModColumnService;

    @Autowired
    private DisclosureNoticeVersionDao disclosureNoticeVersionDao;

    @Autowired
    private DisclosureNoticeProcessDao disclosureNoticeProcessDao;

    @Autowired
    private DisclosureRuleDao disclosureRuleDao;

    @Autowired
    private DisclosureNoticeService disclosureNoticeService;

    @Autowired
    private DisclosureNoticeDocService disclosureNoticeDocService;

    @Autowired
    private DisclosureModVersionDao disclosureModVersionDao;

    @Autowired
    private DisclosureNoticeDao disclosureNoticeDao;

    private String fileStorePath;

    private String ip;

    @Value("${path.word}")
    private  String winPath;


    @Autowired
    private WordToPdfUtil wordToPdfUtil;

    /**
     * 信披文档模板上传
     *
     * @param file
     * @param response
     * @return
     * @throws Exception
     */
    @MethodAnnotation(desc="信披文档模板上传")
    @RequestMapping(value = "/xpdoc/uploadTemp.json",produces = { "application/json;charset=UTF-8"})
    public String uploadPrintTemp(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        if (file != null) {
            //获取参数
            Map<String, Object> parameters = RequestSupport.getParameters();
            JSONObject params = JSONUtil.parseObj(parameters.get("params"));
            String onlineUrl = ((String) params.get("onlineUrl"));
            AtomicReference<String> disclosureModId = new AtomicReference<>("");
            AtomicReference<String> versionId = new AtomicReference<>("");
            try {
                String s = "";
                String os = System.getProperty("os.name");
                String localPath = "";
                String remotePath = "";
                String lineIp = "";
                String sftpUserName = "";
                String sftpUserPwd = "";

                /**指定模板上传文件服务器的ip、用户名、用户密码*/
                //远端服务器ip地址
                s="70000010004";
                lineIp = SysUtil.getSystemParamsByParaid(s);
                //远端服务器用户名
                s="70000010005";
                sftpUserName = SysUtil.getSystemParamsByParaid(s);
                //远端服务器密码
                s="70000010006";
                sftpUserPwd = SysUtil.getSystemParamsByParaid(s);

                if(os.toLowerCase().startsWith("win")){
                    //本地文件根目录
                    s="70000010007";
                    localPath = SysUtil.getSystemParamsByParaid(s);

                }else{
                    //本地文件根目录
                    s="70000010008";
                    localPath = SysUtil.getSystemParamsByParaid(s);
                }
                //远端sftp文件服务器根路径
                remotePath = SysUtil.getSystemParamsByParaid("70000010003");


                //前端页面浏览器地址栏ip+端口
                log.info("前端拦截链接："+onlineUrl);
                ip = onlineUrl ;

                String date = DateUtil.getSysWordDay();
                String time = DateUtil.getNowTime();
                String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
                String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称



                //设置在线编辑页面中jquery路径
                String jqueryPah = onlineUrl + separate  + jqueryName;
                String fileName = file.getOriginalFilename();    //文件名称
                if (StringUtils.isNotEmpty(fileName)) {
                    /* 对于文件名称空格格式化（包括半角，圆角） */
                    Pattern pattern = Pattern.compile("[\\t\\n\\x0B\\f\\r\\xA0]+");
                    Matcher matcher = pattern.matcher(fileName);
                    fileName = matcher.replaceAll("");
                    //文件后缀
                    String extension = null;
                    if (fileName.contains(".")) {
                        extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                    }
                    if (!"doc".equals(extension) && !"docx".equals(extension)) {
                        throw new PromptException("请上传Word文件" + fileName);
                    }
                    DisclosureMod mod = new DisclosureMod();
                    mod.setDisclosureType((String) params.get("disclosureType"));
                    mod.setDisclosureSonType((String) params.get("disclosureSonType"));
                    mod.setModName((String) params.get("modName"));
                    mod.setRemark((String) params.get("remark"));

                    mod.setCrtDate(date);
                    mod.setCrtTime(time);
                    mod.setCrtUserId(userid);// 用户编号
                    mod.setCrtUserName(username);// 姓名

                    //检查文档模板基础信息是否存在，不存在则新增
                    Integer cont = disclosureModService.checkXPPrintTemp(mod);
                    if (cont > 0) {
                        return RequestSupport.updateReturnJson(false, "上传文档模板失败,已存在该模板", null).toString();
                    }
                    String finalFileName = fileName;
                    String finalLineIp = lineIp;
                    String finalSftpUserName = sftpUserName;
                    String finalSftpUserPwd = sftpUserPwd;
                    String finalRemotePath = remotePath;
                    String finalLocalPath = localPath;
                    DaoUtil.doTrans(() -> {
                        /**
                         * 模板及版本文件数据入库
                         */
                        disclosureModId.set(disclosureModService.addDisclosureMod(mod));
                        //组装文档模板版本信息
                        DisclosureModVersion modVersion = new DisclosureModVersion();
                        modVersion.setDisclosureModId(disclosureModId.get());

                        modVersion.setCrtDate(date);
                        modVersion.setCrtTime(time);
                        modVersion.setCrtUserId(userid);
                        modVersion.setCrtUserName(username);

                        String version = (String) params.get("version");

                        modVersion.setVersion(version);
                        modVersion.setDocName(finalFileName);
                        modVersion.setRemark( params.get("remark").toString());
                        //设置文档状态为启用
                        modVersion.setStatus(XpStatus.start.getItemKey());
                        versionId.set(disclosureModVersionService.addDisclosureModVersion(modVersion));
                        modVersion.setDisclosureModVersionId(versionId.get());
                        //数据库始终保存远端服务器路径
                        String fileSavePath = finalRemotePath + versionId+separate;
                        modVersion.setUploadPath(fileSavePath);
                        disclosureModVersionDao.updDisclosureVersionPath(modVersion);

                        /**
                         * 模板文件传输前做文件转存处理
                         */
                        //传输远端前文件暂存本机
                        String forUpload = finalLocalPath + versionId +separate;//临时路径，用于上传
                        File localFile = new File(forUpload + finalFileName);
                        //文件夹不存在的话创建文件夹
                        if(!localFile.getParentFile().exists()){
                            localFile.mkdirs();
                        }
                        //清除该目录下的文件及子目录文件而不删除该目录文件夹
                        FileUtils.cleanDirectory(new File(forUpload));
                        //转存文件
                        file.transferTo(localFile);


                        /**
                         *  linux执行文件转存到指定文件服务器，本地服务器生成后再转存，如果文件不需要在本地服务器保留，转存后执行删除文件,
                         */
                        String dealRemotePath =fileSavePath ;
                        String dealLocalPath =forUpload ;
                        //SftpUtils.putFile(finalLineIp, finalSftpUserName, finalSftpUserPwd, dealRemotePath, dealLocalPath, finalFileName);
                        FileTransfer transfer=new FileTransferHelpler().getTransfer();
                        transfer.uploadFileAndDisconnect(dealLocalPath+finalFileName,dealRemotePath+finalFileName);

                        /**
                         *  版本文件字段占位符数据入库
                         */
                        //将文件转存html文件后解析html文件字段key
                        String json = UploadUtils.wordToHtml(forUpload + finalFileName, jqueryPah);
                        JSONObject jsonObject = JSONUtil.parseObj(json);
                        List<DisclosureModColumn> modColumnList = new ArrayList<DisclosureModColumn>();
                        //预览->路由跳转地址，暂存远端路径
                        String urlPath = ip + finalRemotePath  + versionId + separate + finalFileName.substring(0, finalFileName.lastIndexOf("."))+ ".html";
                        List<String> keys = new ArrayList<String>();
                        jsonObject.forEach((key, value) -> {
                            keys.add(key);
                            DisclosureModColumn modColumn = new DisclosureModColumn();
                            modColumn.setColumnKey(key);

                            modColumn.setCrtDate(date);
                            modColumn.setCrtTime(time);
                            modColumn.setCrtUserId(userid);// 用户编号
                            modColumn.setCrtUserName(username);// 姓名

                            modColumn.setT8DisclosureVersionId(versionId.get());
                            modColumn.setFileName(finalFileName);
                            modColumn.setIsdisplay("1");
                            modColumn.setViewUrl(urlPath);
//                            modColumn.setRoleids("9");
                            modColumn.setUploadPath(fileSavePath);
                            modColumn.setIsSysvalue("1");//默认自动取值
                            modColumnList.add(modColumn);
                        });
                        List<SqlRow> sources = disclosureSourceService.findDisclosureSourcesBykeys(keys.toString().replace("]", "").replace("[", ""));
                        modColumnList.forEach((column)->{
                            sources.forEach((source->{
                                if(source.getString("key").equals( column.getColumnKey())) {
                                    column.setColumnLabel(source.getString("column_label"));
                                    column.setColumnValue(source.getString("column_value"));
                                    column.setT8DisclosureSourceId(source.getString("id"));
                                }
                            }));
                        });
                        disclosureModColumnService.addModColumnList(modColumnList);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.info("上传文档模板失败：{}", e.getMessage());
                disclosureModDao.deleteDisclosureMod(disclosureModId.get());
                disclosureModDao.deleteDisclosureModVersion(versionId.get());
                disclosureModDao.deleteDisclosureModVersionCol(versionId.get());
                if(e.getMessage().contains("too long")){
                    return RequestSupport.updateReturnJson(false, "模板内容有误，请检查表格大小是否规范或字段占位符是否正确使用！", null).toString();
                }
                return RequestSupport.updateReturnJson(false, "上传文档模板失败", null).toString();
            }
        } else {
            return RequestSupport.updateReturnJson(false, "上传文件为空", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
    }

    /**
     * 上传文档模板子模版
     * @param file
     * @param response
     * @return
     * @throws Exception
     */
    @MethodAnnotation(desc="上传文档模板子模版")
    @RequestMapping(value = "/xpdoc/uploadTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String uploadPrintTempVersion(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        //定义返回参数集合
        Map<String, Object> returnMap = new HashMap<>();
        AtomicReference<String> versionId= new AtomicReference<>("");
        if (file != null) {
            //获取参数
            Map<String, Object> parameters = RequestSupport.getParameters();
            JSONObject params = JSONUtil.parseObj(parameters.get("params"));
            String onlineUrl = ((String) params.get("onlineUrl"));
            try {
                String s = "";
                String os = System.getProperty("os.name");
                String localPath = "";
                String remotePath = "";


                if(os.toLowerCase().startsWith("win")){
                    //本地文件根目录
                    s="70000010007";
                    localPath = SysUtil.getSystemParamsByParaid(s);
                }else{
                    //本地文件根目录
                    s="70000010008";
                    localPath = SysUtil.getSystemParamsByParaid(s);
                }
                //远端sftp文件服务器根路径
                remotePath = SysUtil.getSystemParamsByParaid("70000010003");


                //前端页面浏览器地址栏ip+端口
                log.info("前端拦截链接："+onlineUrl);
                ip = onlineUrl;

                String date = DateUtil.getSysWordDay();
                String time = DateUtil.getNowTime();
                //获取登录用户id
                String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));//用户编号
                String username = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_username"));//用户名称

                //设置在线编辑页面中jquery路径
                String jqueryPah = onlineUrl + separate  + jqueryName;
                String finalRemotePath = remotePath;
                String finalLocalPath = localPath;
                DaoUtil.doTrans(() -> {
                    String fileName = file.getOriginalFilename();    //文件名称
                    if (StringUtils.isNotEmpty(fileName)) {
                        /* 对于文件名称空格格式化（包括半角，圆角） */
                        Pattern pattern = Pattern.compile("[\\t\\n\\x0B\\f\\r\\xA0]+");
                        Matcher matcher = pattern.matcher(fileName);
                        fileName = matcher.replaceAll("");
                        //文件后缀
                        String extension = null;
                        if (fileName.contains(".")) {
                            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
                        }
                        if (!"doc".equals(extension) && !"docx".equals(extension)) {
                            throw new PromptException("请上传Word文件" + fileName);
                        }
                        //组装文档模板版本信息
                        DisclosureModVersion modVersion = new DisclosureModVersion();
                        String disclosureModId = (String) params.get("disclosureModId");
                        modVersion.setDisclosureModId(disclosureModId);
                        //取得最大的版本
                        String maxVersion = disclosureModVersionService.getNewestXPVersion(disclosureModId);
                        String newVersion= disclosureModService.getNextVersion(maxVersion);
                        modVersion.setVersion(newVersion);
                        modVersion.setDocName(fileName);
                        modVersion.setRemark((String) params.get("remark"));
                        modVersion.setStatus(XpStatus.start.getItemKey());//启用
                        String temp_html = "";
                        modVersion.setDocHtml(temp_html);

                        modVersion.setCrtDate(date);
                        modVersion.setCrtTime(time);
                        modVersion.setCrtUserId(userid);
                        modVersion.setCrtUserName(username);

                        versionId.set(disclosureModVersionService.updateDocSonVersion(modVersion));
                        modVersion.setDisclosureModVersionId(versionId.get());
                        String fileSavePath = finalRemotePath  + versionId.get()+separate;
                        modVersion.setUploadPath(fileSavePath);
                        disclosureModVersionDao.updDisclosureVersionPath(modVersion);


                        String forUpload = finalLocalPath + versionId +separate;//临时路径，用于上传
                        File localFile = new File(forUpload + fileName);
                        if(!localFile.getParentFile().exists()){
                            logger.info("创建文件夹目录:", forUpload);
                            localFile.mkdirs();
                        }
                        logger.info("文档模板上传路径:{}", localFile);
                        //清除该目录下的文件及子目录文件而不删除该目录文件夹
                        FileUtils.cleanDirectory(new File(forUpload));
                        file.transferTo(localFile);

                        //文件转存到指定文件目录
                        FileTransfer transfer=new FileTransferHelpler().getTransfer();
                        transfer.uploadFileAndDisconnect(forUpload + fileName,fileSavePath+fileName);

                        /**
                         * 文本字段扫描组装并处理基础数据
                         */
                        //将文件转存html文件后解析html文件字段key
                        String json = UploadUtils.wordToHtml(forUpload + fileName, jqueryPah);
                        JSONObject jsonObject = JSONUtil.parseObj(json);
                        String finalFileName = fileName;
                        List<DisclosureModColumn> modColumnList = new ArrayList<DisclosureModColumn>();
                        //预览->html文件，暂存远端路径
                        String urlPath = ip + finalRemotePath + versionId.get() + separate + fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
                        List<String> keys = new ArrayList<String>();
                        jsonObject.forEach((key, value) -> {
                            keys.add(key);
                            DisclosureModColumn modColumn = new DisclosureModColumn();
                            modColumn.setColumnKey(key);

                            modColumn.setCrtDate(date);
                            modColumn.setCrtTime(time);
                            modColumn.setCrtUserId(userid);// 用户编号
                            modColumn.setCrtUserName(username);// 姓名

                            modColumn.setT8DisclosureVersionId(versionId.get());
                            modColumn.setFileName(finalFileName);
                            modColumn.setViewUrl(urlPath);
                            modColumn.setUploadPath(fileSavePath);
                            modColumn.setIsSysvalue("1");
                            modColumn.setIsdisplay("1");
//                            modColumn.setRoleids("9");
                            modColumnList.add(modColumn);
                        });
                        List<SqlRow> sources = disclosureSourceService.findDisclosureSourcesBykeys(String.join(",",keys));
                        modColumnList.forEach((column)->{
                            sources.forEach((source->{
                                if(source.getString("column_key").equals( column.getColumnKey())) {
                                    column.setColumnLabel(source.getString("column_label"));
                                    column.setColumnValue(source.getString("column_value"));
                                    column.setT8DisclosureSourceId(source.getString("id"));
                                }
                            }));
                        });
                        disclosureModColumnService.addModColumnList(modColumnList);
                    }
                });
                returnMap.put("type", "2");
                return RequestSupport.updateReturnJson(true, "文件上传成功", returnMap).toString();
            } catch (Exception e) {
                log.error("上传文档模板失败：{}"+e.getMessage(),e);
                disclosureModDao.deleteDisclosureModVersion(versionId.get());
                disclosureModDao.deleteDisclosureModVersionCol(versionId.get());
                if(e.getMessage().contains("too long")){
                    return RequestSupport.updateReturnJson(false, "模板内容有误，请检查表格大小是否规范或字段占位符是否正确使用！", null).toString();
                }
                return RequestSupport.updateReturnJson(false, "上传文档模板失败", null).toString();
            }
        } else {
            throw new Exception("上传文档模板失败");
        }

    }

    /**
     * 下载产品模板对应版本
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/downloadXPTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
//        OutputStream ostream = null;
//        ByteArrayInputStream bais = null;
//        ByteArrayOutputStream bos = null;

        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;

        try{
            DisclosureModVersion disclosureModVersion = disclosureModVersionService.getPrintXPVersionById((String) params.get("id"));
            if (disclosureModVersion != null) {
                response.addHeader("Access-Control-Expose-Headers","*");
                response.setHeader("filename",disclosureModVersion.getDocName());
                response.setHeader("content-disposition", "attachment;filename=" +
                        new String(disclosureModVersion.getDocName().getBytes("GB2312"), "ISO8859-1"));


                /**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
                //远端服务器ip地址
                String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
                //远端服务器用户名
                String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
                //远端服务器密码
                String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
                //远端服务器信批文档模板sftp存储路径
                String remotePath = SysUtil.getSystemParamsByParaid("70000010003");
                String dealRemotePath = remotePath + disclosureModVersion.getId() + separate ;
                //本机或本地服务器信批模板文档存储路径
                String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
                        ?SysUtil.getSystemParamsByParaid("70000010002")//本机存放根路径
                        :SysUtil.getSystemParamsByParaid("70000010009");//服务器存放根路径
                //文件名
                String fileModName = disclosureModVersion.getDocName();
                FileTransfer transfer=new FileTransferHelpler().getTransfer();
                transfer.downloadFileAndDisconnect(dealRemotePath+fileModName,temPath+fileModName);

                //从本地路径输出前端head中响应IO
                String pathFile = temPath + fileModName;
                File file = new File(pathFile);
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
            logger.error(e.getMessage(), e);
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
    @RequestMapping(value = "/print/downloadXPGGTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public void downloadXPGGPrintTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
        OutputStream ostream = null;
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream bos = null;


        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        String processInstanceId = "";
        try{
            processInstanceId = (String) params.get("processInstanceId");
        }catch (Exception e){

        }
        try{
            DisclosureRule dis = disclosureRuleDao.findGGDisclosureRules((String) params.get("id"));
            DisclosureModVersion disclosureModVersion = disclosureModVersionService.getPrintXPVersionById((String) dis.getId());
            DisclosureNoticeVersion disclosureNoticeVersion = new DisclosureNoticeVersion();
            disclosureNoticeVersion.setT8DisclosureNoticeId((String)params.get("id"));
            Map<String,String> map5 = disclosureNoticeProcessDao.findMaxLatestVersions(disclosureNoticeVersion);
            Map<String, Object> map = new HashMap<>();
            map.put("prodCode",params.get("prodCode"));
            map.put("t8ProdInfoId",params.get("t8ProdInfoId"));
            map.put("prodBaseDate",params.get("prodBaseDate"));
            map.put("disclosureSonType",dis.getDisclosureSonType());
            map.put("t8DisclosureNoticeId",params.get("id"));
            map.put("version",map5.get("version").toString());
            map.put("isComplete",map5.get("isComplete").toString());
            SqlRow infoMap = disclosureNoticeService.findDataInfoByType(map);
            DisclosureNotice disclosureNotice = disclosureNoticeDao.queryNoticeById((String) params.get("id"));

            if (infoMap != null && infoMap.size()>0){
                fileStorePath = OnlineUtils.getOnlinepath(winPath);
                String newFilePath = fileStorePath + separate+"xpTemp" +separate+ params.get("id")+separate+map5.get("version");
                String filePath = fileStorePath + separate+"xpTemp" +separate+ disclosureModVersion.getId();
                //File file = new File(filePath);
                File word =null;
                if("1".equals(map5.get("isComplete"))){
                    //word = new File(filePath,PrintTempUtil.versionToFile(map5.get("fileName"), "V1.0"));
                    word = new File(filePath,disclosureModVersion.getDocName());
                }else{
                    word = new File(filePath,map5.get("fileName"));
                }
                fileInputStream = new FileInputStream(word);

                // 载入模板文件
                WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(fileInputStream);
                // 提取正文
                MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
                org.docx4j.wml.Document wmlDoc = (org.docx4j.wml.Document) mainDocumentPart.getJaxbElement();
                org.docx4j.wml.Body body = wmlDoc.getBody();
                // 提取正文中所有段落
                List<Object> paragraphs = body.getContent();
                // 提取书签并创建书签的游标
                RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
                new TraversalUtil(paragraphs, rt);
                //判断是否份额分类
                if("1".equals(disclosureNotice.getIsSendEmail())) {
                    //判断书签
                    for (CTBookmark bm : rt.getStarts()) {
                        if (bm.getName().equals("comparisonChart")) {
                            // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
                            String os = System.getProperty("os.name");
                            //根据noticeID查询t8_disclosure_share_image
                            List<SqlRow> shareImages = disclosureNoticeDao.queryShareImage(disclosureNotice.getId(),"0");
                            if(CollectionUtils.isEmpty(shareImages))
                                continue;

                            shareImages.forEach((image)->{
                                InputStream is = null;
                                try {

                                    String path = image.getString("share_image_path");
                                    String shareName = image.getString("share_name");
                                    log.info("imagePath---------->{}",path);
                                    if (Strings.isEmpty(path) || !(new File(path).exists()))
                                        return;
                                    is = new FileInputStream(path);
                                    byte[] bytes = IOUtils.toByteArray(is);
                                    // 穿件一个行内图片
                                    BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);

                                    Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
                                    P p = (P) (bm.getParent());
                                    ObjectFactory factory = new ObjectFactory();

                                    R run = factory.createR();
                                    run = factory.createR();
                                    Text text = factory.createText();
                                    text.setValue(shareName);
                                    run.getContent().add(text);
                                    Br br = factory.createBr();
                                    run.getContent().add(br);
                                    p.getContent().add(run);

                                    run = factory.createR();
                                    Drawing drawing = factory.createDrawing();
                                    drawing = factory.createDrawing();
                                    drawing.getAnchorOrInline().add(inline);
                                    run.getContent().add(drawing);
                                    br = factory.createBr();
                                    run.getContent().add(br);
                                    p.getContent().add(run);


                                } catch (Exception e) {
                                    log.error("ioException{}",e);
                                }finally {
                                    if(is!=null) {
                                        try {
                                            is.close();
                                        } catch (IOException e) {
                                            log.error("ioException{}",e);
                                        }
                                    }
                                }
                            });

                        }else if (bm.getName().equals("comparisonChart2")) {
                            // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
                            String os = System.getProperty("os.name");
                            //根据noticeID查询t8_disclosure_share_image
                            List<SqlRow> shareImages = disclosureNoticeDao.queryShareImage(disclosureNotice.getId(),"1");
                            if(CollectionUtils.isEmpty(shareImages))
                                continue;

                            shareImages.forEach((image)->{
                                InputStream is = null;
                                try {

                                    String path = image.getString("share_image_path");
                                    String shareName = image.getString("share_name");
                                    log.info("imagePath---------->{}",path);
                                    if (Strings.isEmpty(path) || !(new File(path).exists()))
                                        return;
                                    is = new FileInputStream(path);
                                    byte[] bytes = IOUtils.toByteArray(is);
                                    // 穿件一个行内图片
                                    BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);

                                    Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
                                    P p = (P) (bm.getParent());
                                    ObjectFactory factory = new ObjectFactory();

                                    R run = factory.createR();
                                    run = factory.createR();
                                    Text text = factory.createText();
                                    text.setValue(shareName);
                                    run.getContent().add(text);
                                    Br br = factory.createBr();
                                    run.getContent().add(br);
                                    p.getContent().add(run);

                                    run = factory.createR();
                                    Drawing drawing = factory.createDrawing();
                                    drawing = factory.createDrawing();
                                    drawing.getAnchorOrInline().add(inline);
                                    run.getContent().add(drawing);
                                    br = factory.createBr();
                                    run.getContent().add(br);
                                    p.getContent().add(run);


                                } catch (Exception e) {
                                    log.error("ioException{}",e);
                                }finally {
                                    if(is!=null) {
                                        try {
                                            is.close();
                                        } catch (IOException e) {
                                            log.error("ioException{}",e);
                                        }
                                    }
                                }
                            });

                        }
                    }
                }else {
                    InputStream is = null;
                    // 遍历书签
                    for (CTBookmark bm : rt.getStarts()) {
                        // 这儿可以对单个书签进行操作，也可以用一个map对所有的书签进行处理
                        if (bm.getName().equals("comparisonChart")) {
                            // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
                            String os = System.getProperty("os.name");
                            String path = "";
                            Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry<String, Object> entry = it.next();
                                //System.out.println("key:" + entry.getKey() + " key:" + entry.getValue());
                                if ("image_path".equals(entry.getKey())  &&entry.getValue()!=null&&Strings.isNotBlank(String.valueOf(entry.getValue()))) {
                                    path = entry.getValue().toString();
                                }

                            }
                            if (!os.toLowerCase().startsWith("win")) {

                                String prodCode = disclosureNotice.getProdCode();
                                String baseDate = disclosureNotice.getProdBaseDate();
                                if (Tools.isNotEmpty(path) && new File(path).exists()) {
                                    is = new FileInputStream(path);
                                }
                                //去除自动生成图片
                                 /*else {
                                 	if(realPath!=null && new File(realPath).exists()) {
                                 		is = new FileInputStream(realPath);
                                 	}
                                     
                                 }*/
                            }else{
                                String prodCode = disclosureNotice.getProdCode();
                                String baseDate = disclosureNotice.getProdBaseDate();
                                if (Tools.isNotEmpty(path) && new File(path).exists()) {
                                    is = new FileInputStream(path);
                                }
                                //去除自动生成图片
                                 /*else {
                                     if(realPath!=null && new File(realPath).exists()) {
                                         is = new FileInputStream(realPath);
                                     }

                                 }*/
                            }
                            if (is !=null){
                                byte[] bytes = IOUtils.toByteArray(is);
                                // 穿件一个行内图片
                                BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);
                                // createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
                                // 最有一个是限制图片的宽度，缩放的依据
                                Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
                                // 获取该书签的父级段落
                                P p = (P) (bm.getParent());
                                ObjectFactory factory = new ObjectFactory();
                                // R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
                                R run = factory.createR();
                                // drawing理解为画布？
                                Drawing drawing = factory.createDrawing();
                                drawing.getAnchorOrInline().add(inline);
                                run.getContent().add(drawing);
                                p.getContent().add(run);
                                //is.close();
                            }

                        }else
                        if (bm.getName().equals("comparisonChart2")) {
                            // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片
                            String os = System.getProperty("os.name");
                            String path = "";
                            Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
                            while(it.hasNext()){
                                Map.Entry<String, Object> entry = it.next();
                                //System.out.println("key:"+entry.getKey()+" key:"+entry.getValue());
                                if("image_path2".equals(entry.getKey()) && entry.getValue()!=null){
                                    path = entry.getValue().toString();
                                }else{

                                }

                            }
                            if(os.toLowerCase().startsWith("win")){
                                String prodCode = disclosureNotice.getProdCode();
                                String baseDate = disclosureNotice.getProdBaseDate();
                                String realPath = "";
                                String startDate = disclosureNotice.getReportStartDate();
                                //is = new FileInputStream("D:\\ChromDownloads\\净值表现与市场对比.png");
                                if(Tools.isNotEmpty(path) && new File(path).exists()){
                                    is = new FileInputStream(path);
                                }
                                //去除自动生成图片
                                 /*else{
                                     realPath = echartsAction.navMarketAreaCreateImg(prodCode,startDate,baseDate);
                                     if(Tools.isNotEmpty(realPath) && new File(realPath).exists()){
                                         is = new FileInputStream(realPath);
                                     }
                                 }*/

                            }else{

                                String prodCode = disclosureNotice.getProdCode();
                                String baseDate = disclosureNotice.getProdBaseDate();
                                String startDate = disclosureNotice.getReportStartDate();
                                String realPath = "";
                                //is = new FileInputStream("/home/ftpuser/test/echarts/EB1844/20210529/202105291622270542574.jpg");
                                if(Tools.isNotEmpty(path) && new File(path).exists()){
                                    is = new FileInputStream(path);
                                }
                                //去除自动生成图片
                                 /*else{
                                     realPath = echartsAction.navMarketAreaCreateImg(prodCode,startDate,baseDate);
                                     if(Tools.isNotEmpty(realPath) && new File(realPath).exists()){
                                         is = new FileInputStream(realPath);
                                     }
                                 }*/
                            }
                            if(is!=null){
                                byte[] bytes = IOUtils.toByteArray(is);
                                // 穿件一个行内图片
                                BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);
                                // createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
                                // 最有一个是限制图片的宽度，缩放的依据
                                Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 6000);
                                // 获取该书签的父级段落
                                P p = (P) (bm.getParent());
                                ObjectFactory factory = new ObjectFactory();
                                // R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
                                R run = factory.createR();
                                // drawing理解为画布？
                                Drawing drawing = factory.createDrawing();
                                drawing.getAnchorOrInline().add(inline);
                                run.getContent().add(drawing);
                                //setParagraphStyle(p);
                                p.getContent().add(run);
                                //is.close();
                            }
                        }
                    }
                    if(is!=null){
                        is.close();

                    }
                }

                File localPathFile = new File(newFilePath+separate+"temp");

                if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                    localPathFile.mkdirs();
                }
                wPackage.save(new FileOutputStream(newFilePath+separate+"temp"+separate+map5.get("fileName")));

                wordToPdfUtil.getLicense();
                //com.aspose.words.Document document = new com.aspose.words.Document(filePath+File.separator+disclosureModVersion.getDocName());
                com.aspose.words.Document document = new com.aspose.words.Document(newFilePath+separate+"temp"+separate+map5.get("fileName"));
                // 获得要替换的word模板
                Range range = document.getRange();// range获取word中的内容
                Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
                //年报或者半年报  不显示其他关联交易
                if ("2".equals(dis.getDisclosureSonType()) || "3".equals(dis.getDisclosureSonType())) {
                    infoMap.put("other_transaction1","");
                    infoMap.put("other_transaction2","");
                    infoMap.put("other_transaction3","");
                    infoMap.put("other_relation_transaction","");
                }
                //是否份额分类产品

                if ("1".equals(disclosureNotice.getIsSendEmail())) {
                    document = this.isShareSort(document,disclosureNotice,disclosureModVersion.getDocName(),infoMap);
                }

                while(it.hasNext()){
                    Map.Entry<String, Object> entry = it.next();
                    System.out.println("key:"+entry.getKey()+" key:"+entry.getValue());
                    String str = "";
                    String value = "";
                    if(entry.getValue()!=null){
                        value = entry.getValue().toString();
                    }
                    if("image_path".equals(entry.getKey()) && entry.getValue()!=null){
                        value="";
                    }
                    if("image_path2".equals(entry.getKey()) && entry.getValue()!=null){
                        value="";
                    }
//                     if("assets_desc".equals(entry.getKey())){
//                         String result = disclosureNoticeService.findDetail((String) params.get("id"));
//                         if("success".equals(result)){
//
//                         }else{
//                             value="";
//                         }
//                     }
                    if ("assets_desc".equals(entry.getKey())) {
                        Map<String,Object> paramMap = new HashMap<>();
                        paramMap.put("t8DisclosureNoticeId",disclosureNotice.getId());
                        SqlParam<DisclosureRegularAsset> params1=new FetcherData<>(paramMap,DisclosureRegularAsset.class);
                    }
                    if("establish_date".equals(entry.getKey())){
                        if(entry.getValue()!=null){
                            String establishDate = entry.getValue().toString();
                            if(entry.getValue()!=null&&entry.getValue()!=""){
                                if(establishDate.length()>5){
                                    String year = establishDate.substring(0,4);
                                    String month = establishDate.substring(4,6);
                                    String day = establishDate.substring(6,8);
                                    if("1".equals(month.substring(0,1))){
                                        month = month;
                                    }else{
                                        month = month.substring(1,2);
                                    }
                                    if("0".equals(day.substring(0,1))){

                                        day = day.substring(1,2);
                                    }else{
                                        day = day;
                                    }
                                    value = year+"年"+month+"月"+day+"日";
                                }
                            }
                        }

                    }
                    if("end_date".equals(entry.getKey())){
                        String endDate = entry.getValue().toString();
                        if(entry.getValue()!=null&&entry.getValue()!=""){
                            String netvalDate = entry.getValue().toString();
                            if(netvalDate.length()>5){
                                String year = endDate.substring(0,4);
                                String month = endDate.substring(4,6);
                                String day = endDate.substring(6,8);
                                if(!"1".equals(month.substring(0,1))){
                                    month = month.substring(1,2);
                                }
                                if("0".equals(day.substring(0,1))){

                                    day = day.substring(1,2);
                                }
                                value = year+"年"+month+"月"+day+"日";
                            }
                        }
                    }
                    if("report_date".equals(entry.getKey())){
                        if(entry.getValue()!=null){
                            String reportDate = entry.getValue().toString();
                            if(entry.getValue()!=null&&entry.getValue()!=""){
                                if(reportDate.length()>5){
                                    String year = reportDate.substring(0,4);
                                    String month = reportDate.substring(4,6);
                                    String day = reportDate.substring(6,8);
                                    if("1".equals(month.substring(0,1))){
                                        month = month;
                                    }else{
                                        month = month.substring(1,2);
                                    }
                                    if("0".equals(day.substring(0,1))){

                                        day = day.substring(1,2);
                                    }else{
                                        day = day;
                                    }
                                    value = year+"年"+month+"月"+day+"日";
                                }
                            }
                        }

                    }
                    if("netval_date".equals(entry.getKey())){
                        if (entry.getValue() != null && !("".equals(entry.getValue().toString().trim()))) {
                            String netvalDate = entry.getValue().toString();
                            if(netvalDate.length()>5){
                                String year = netvalDate.substring(0, 4);
                                String month = netvalDate.substring(4, 6);
                                String day = netvalDate.substring(6, 8);
                                if ("1".equals(month.substring(0, 1))) {
                                    month = month;
                                } else {
                                    month = month.substring(1, 2);
                                }
                                if ("0".equals(day.substring(0, 1))) {

                                    day = day.substring(1,2);
                                }else{
                                    day = day;
                                }
                                value = year+"年"+month+"月"+day+"日";
                            }
                        }
                    }
                    if(entry.getValue()!=null){
                        str = value;
                        if("brief,financier,term".contains(entry.getKey())) {
                            str = str.replaceAll("\n\r",ControlChar.LINE_BREAK);
                            str = str.replaceAll("\r\n",ControlChar.LINE_BREAK);
                            str = str.replaceAll("\n", ControlChar.LINE_BREAK);
                        }else {
                            if (str.contains("\n\r")) {
                                str = formatStr(str,"\n\r");
                            }
                            if (str.contains("\r\n")) {
                                str = formatStr(str,"\r\n");
                            }
                            if (str.contains("\n")) {
                                str = formatStr(str,"\n");
                            }
                        }

                    }
                    log.info("key--------{}-------val--------{}",entry.getKey(),str);
                    range.replace("${" + entry.getKey() + "}", str,true,false);
                }
                //定期报告资产配置明细
                Table table = (Table) document.getChild(NodeType.TABLE, 3, true);
                if(table==null){
                    Table table4 = (Table) document.getChild(NodeType.TABLE, 1, true);
                    if(table4!=null){


                        List<SqlRow> list = disclosureNoticeDao.findDetailListForNotice1(params.get("prodCode").toString(), params.get("id").toString(), params.get("prodBaseDate").toString());
                        Integer i = 0;
                        if (list != null && list.size() > 0) {
                            for (Map map2 : list) {
                                i = i + 1;
                                Node deepClone = table4.getLastRow().deepClone(true);
                                Range range2 = table4.getLastRow().getRange();
                                if (map2.get("list_id") != null &&  StringUtils.isNotBlank(map2.get("list_id").toString())) {
                                    if ("合计".equals(map2.get("list_id").toString())) {
                                        range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                    } else {
                                        if (Float.parseFloat(map2.get("list_id").toString()) > 6) {
                                            range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                        } else {
                                            if(map2.get("list_id").toString().contains(".")){
                                                range2.replace("${list_id}", map2.get("list_id").toString().substring(0, map2.get("list_id").toString().indexOf(".")), true, false);
                                            }else{
                                                range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                            }
                                        }
                                    }
                                } else {
                                    range2.replace("${list_id}", "", true, false);
                                }
                                if (map2.get("list_assets_type") != "" && map2.get("list_assets_type") != null) {
                                    if ("总资产".equals(map2.get("list_assets_type").toString())) {
                                        range2.replace("${list_assets_type}", "", true, false);
                                    } else {
                                        range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(), true, false);
                                    }
                                } else {
                                    range2.replace("${list_assets_type}", "", true, false);
                                }
                                if (map2.get("list_amount") != "" && map2.get("list_amount") != null) {
                                    range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
                                } else {
                                    range2.replace("${list_amount}", "", true, false);
                                }
                                if (map2.get("list_asset_ratio") != "" && map2.get("list_asset_ratio") != null) {
                                    range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true, false);
                                } else {
                                    range2.replace("${list_asset_ratio}", "", true, false);
                                }

                                table4.getRows().add(deepClone);
                            }
                            table4.getLastRow().remove();
                        } else {
                            Node deepClone = table.getLastRow().deepClone(true);
                            Range range2 = table.getLastRow().getRange();
                            range2.replace("${list_id}", "", true, false);
                            range2.replace("${list_assets_type}", "", true, false);
                            range2.replace("${list_amount}", "", true, false);
                            range2.replace("${list_asset_ratio}", "", true, false);
                            table.getRows().add(deepClone);
                            table.getLastRow().remove();
                        }
                    }
                }else{
                    int counts = table.getFirstRow().getCount();
                    if(counts >4) {
                        table= (Table) document.getChild(NodeType.TABLE, 2, true);
                    }
                    List<SqlRow> list = disclosureNoticeDao.findDetailListForNotice1(params.get("prodCode").toString(),params.get("id").toString(),params.get("prodBaseDate").toString());
                    Integer i=0;
                    if(list!=null && list.size()>0){
                        for(Map map2:list){
                            i=i+1;
                            Node deepClone = table.getLastRow().deepClone(true);
                            Range range2 = table.getLastRow().getRange();
                            if(map2.get("list_id") != null &&  StringUtils.isNotBlank(map2.get("list_id").toString())){
                                if("合计".equals(map2.get("list_id").toString())){
                                    range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                }else{
                                    if(Float.parseFloat(map2.get("list_id").toString())>6){
                                        range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                    }else{
                                        if(map2.get("list_id").toString().contains(".")){
                                            range2.replace("${list_id}", map2.get("list_id").toString().substring(0,map2.get("list_id").toString().indexOf(".")), true, false);
                                        }else{
                                            range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                        }
                                    }
                                }
                            }else{
                                range2.replace("${list_id}", "", true, false);
                            }
                            if(map2.get("list_assets_type")!=""&&map2.get("list_assets_type")!=null){
                                if("总资产".equals(map2.get("list_assets_type").toString())){
                                    range2.replace("${list_assets_type}", "", true, false);
                                }else{
                                    range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(), true, false);
                                }
                            }else{
                                range2.replace("${list_assets_type}", "", true, false);
                            }
                            if(map2.get("list_amount")!=""&&map2.get("list_amount")!=null){
                                range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
                            }else{
                                range2.replace("${list_amount}", "", true, false);
                            }
                            if(map2.get("list_asset_ratio")!=""&&map2.get("list_asset_ratio")!=null){
                                range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true, false);
                            }else{
                                range2.replace("${list_asset_ratio}", "", true, false);
                            }

                            table.getRows().add(deepClone);
                        }
                        table.getLastRow().remove();
                    }else{
                        Node deepClone = table.getLastRow().deepClone(true);
                        Range range2 = table.getLastRow().getRange();
                        range2.replace("${list_id}", "", true, false);
                        range2.replace("${list_assets_type}", "", true, false);
                        range2.replace("${list_amount}", "", true, false);
                        range2.replace("${list_asset_ratio}", "", true, false);
                        table.getRows().add(deepClone);
                        table.getLastRow().remove();
                    }
                }


                //定期报告十大资产明细
                Table table2 = (Table) document.getChild(NodeType.TABLE, 4, true);

                if(table2!=null){
                    int counts = table2.getFirstRow().getCount();
                    if(counts <5 || (disclosureModVersion.getDocName().contains("现金") && counts == 5)) { //兼容份额分类模板
                        table2= (Table) document.getChild(NodeType.TABLE, 3, true);
                    }
                    List<SqlRow> list2 = disclosureNoticeService.findTenDetailList(params.get("id").toString());
                    if(list2!=null && list2.size()>0){
                        for(Map map3:list2){
                            Node deepClone2 = table2.getLastRow().deepClone(true);
                            Range range2 = table2.getLastRow().getRange();
                            if(map3.get("ten_list_id")!=""&&map3.get("ten_list_id")!=null){
                                range2.replace("${ten_list_id}", map3.get("ten_list_id").toString(), true, false);
                            }else{
                                range2.replace("${ten_list_id}", "", true, false);
                            }
                            if(map3.get("ten_assets_code")!=""&&map3.get("ten_assets_code")!=null){
                                range2.replace("${ten_assets_code}", map3.get("ten_assets_code").toString(), true, false);
                            }else{
                                range2.replace("${ten_assets_code}", "", true, false);
                            }
                            if(map3.get("ten_assets_name")!=""&&map3.get("ten_assets_name")!=null){
                                range2.replace("${ten_assets_name}", map3.get("ten_assets_name").toString(), true, false);
                            }else{
                                range2.replace("${ten_assets_name}", "", true, false);
                            }
                            if(map3.get("ten_assets_scale")!=""&&map3.get("ten_assets_scale")!=null){
                                range2.replace("${ten_assets_scale}", map3.get("ten_assets_scale").toString(), true, false);
                            }else{
                                range2.replace("${ten_assets_scale}", "", true, false);
                            }
                            if(map3.get("ten_asset_ratio")!=""&&map3.get("ten_asset_ratio")!=null){
                                range2.replace("${ten_asset_ratio}", map3.get("ten_asset_ratio").toString(), true, false);
                            }else{
                                range2.replace("${ten_asset_ratio}", "", true, false);
                            }

                            table2.getRows().add(deepClone2);
                        }
                        table2.getLastRow().remove();
                    }else{
                        Node deepClone2 = table2.getLastRow().deepClone(true);
                        Range range2 = table2.getLastRow().getRange();
                        range2.replace("${ten_list_id}", "", true, false);
                        range2.replace("${ten_assets_code}", "", true, false);
                        range2.replace("${ten_assets_name}", "", true, false);
                        range2.replace("${ten_assets_scale}", "", true, false);
                        range2.replace("${ten_asset_ratio}", "", true, false);
                        table2.getRows().add(deepClone2);

                        table2.getLastRow().remove();
                    }
                }

                String isFormal = infoMap.getString("is_formal");
                String fileName = map5.get("fileName");
                //非标判断   ：1 是非标
                log.info("非标状态{},文件名称{}",isFormal,fileName);
                if(Strings.isNotEmpty(isFormal)&&"1".equals(isFormal)) {

                }else {
                    log.info("去除表格");
                    // 1 non_default_desc设置为空
                    range.replace("${non_default_desc}", "",true,false);
                    // 2 去除表格
                    if(fileName.contains("公募封闭固收")||fileName.contains("公募开放固收")||fileName.contains("私募开放固收")) {
                        Table rmTable = (Table) document.getChild(NodeType.TABLE, 5, true);
                        rmTable.getRows().removeAt(0);
                        rmTable.getRows().removeAt(0);
                    }

                    if(fileName.contains("私募封闭固收")) {
                        Table rmTable = (Table) document.getChild(NodeType.TABLE, 2, true);
                        rmTable.getRows().removeAt(0);
                        rmTable.getRows().removeAt(0);
                    }
                }

                String noticeTitle = params.get("noticeTitle") + ".docx";
                response.setContentType("application/msword;charset=UTF-8");//导出word格式
                response.addHeader("Content-Disposition", "attachment;filename=" +
                        new String(noticeTitle.getBytes("GB2312"), "ISO8859-1"));
                ostream = response.getOutputStream();
                document.save(ostream, SaveFormat.DOCX);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            if (ostream != null) {
                try {
                    ostream.close();
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

    //份额分类模板处理
    private com.aspose.words.Document isShareSort(com.aspose.words.Document document, DisclosureNotice disclosureNotice,String fileName,Map<String,Object> infoMap) throws Exception {
        //所有模板中产品概况循环添加下属份额分类
        List<SqlRow> sqlRows = disclosureNoticeDao.findDisclosureRegularShareSort(disclosureNotice.getProdCode(),disclosureNotice.getProdBaseDate(),disclosureNotice.getId());
        Table table1 = (Table) document.getChild(NodeType.TABLE, 0, true);
        Table table2 = null;
        Table table3 = null;
        Table table7 = null;

        //产品净值表现
        if (!fileName.contains("单一资产") && !fileName.contains("现金") && !fileName.contains("私募封闭")) {
            table2 = (Table) document.getChild(NodeType.TABLE, 1, true);
        }
        //财务指标
        if (fileName.contains("现金")) {
            table3 = (Table) document.getChild(NodeType.TABLE, 1, true);
        } else if (fileName.contains("公募") || fileName.contains("私募开放")) {
            table3 = (Table) document.getChild(NodeType.TABLE, 2, true);
        }
        //开放式份额信息
        if (fileName.contains("公募开放非固收") || fileName.contains("私募开放非固收")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 5, true);
        } else if (fileName.contains("公募开放固收") || fileName.contains("私募开放固收")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 6, true);
        } else if (fileName.contains("现金")) {
            table7 = (Table) document.getChild(NodeType.TABLE, 4, true);
        }

        if (sqlRows != null && sqlRows.size() > 0) {
            for (Map map1 : sqlRows) {
                Node deepClone1 = table1.getLastRow().deepClone(true);
                Range range1 = table1.getLastRow().getRange();

                if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
                    range1.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
                } else {
                    range1.replace("${list_sale_name}", "", true, false);
                }

                if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                    range1.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                } else {
                    range1.replace("${list_sale_code}", "", true, false);
                }

                if (map1.get("share_value") != "" && map1.get("share_value") != null) {
                    range1.replace("${netval}", map1.get("share_value").toString(), true, false);
                } else {
                    range1.replace("${netval}", "", true, false);
                }

                if (map1.get("share_total_value") != "" && map1.get("share_total_value") != null) {
                    range1.replace("${netval_total}", map1.get("share_total_value").toString(), true, false);
                } else {
                    range1.replace("${netval_total}", "", true, false);
                }

                if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
                    range1.replace("${netval_vol}", map1.get("end_asset_value").toString(), true, false);
                } else {
                    range1.replace("${netval_vol}", "", true, false);
                }

                table1.getRows().add(deepClone1);

                //产品净值表现
                Node deepClone2;
                Range range2;
                if (table2 != null) {
                    deepClone2 = table2.getLastRow().deepClone(true);
                    range2 = table2.getLastRow().getRange();

                    if (map1.get("sales_name") != "" && map1.get("sales_name") != null) {
                        range2.replace("${list_sale_name}", map1.get("sales_name").toString(), true, false);
                    } else {
                        range2.replace("${list_sale_name}", "", true, false);
                    }
                    if (map1.get("dur_net_growth") != "" && map1.get("dur_net_growth") != null) {
                        range2.replace("${list_report}", map1.get("dur_net_growth").toString()+"%", true, false);
                    } else {
                        range2.replace("${list_report}", "", true, false);
                    }
                    if (map1.get("sur_net_growth") != "" && map1.get("sur_net_growth") != null) {
                        range2.replace("${list_subsist}", map1.get("sur_net_growth").toString()+"%", true, false);
                    } else {
                        range2.replace("${list_subsist}", "", true, false);
                    }
                    table2.getRows().add(deepClone2);
                }

                //产品财务指标
                Node deepClone3;
                Range range3;
                if(table3 != null) {
                    deepClone3 = table3.getLastRow().deepClone(true);
                    range3 = table3.getLastRow().getRange();
                    if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                        range3.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                    } else {
                        range3.replace("${list_sale_code}", "", true, false);
                    }
                    if (map1.get("end_share_value") != "" && map1.get("end_share_value") != null) {
                        range3.replace("${list_netval_end}", map1.get("end_share_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_end}", "", true, false);
                    }
                    if (map1.get("end_total_value") != "" && map1.get("end_total_value") != null) {
                        range3.replace("${list_netval_total}", map1.get("end_total_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_total}", "", true, false);
                    }
                    if (map1.get("end_prod_share") != "" && map1.get("end_prod_share") != null) {
                        range3.replace("${list_end_vol}", map1.get("end_prod_share").toString(), true, false);
                    } else {
                        range3.replace("${list_end_vol}", "", true, false);
                    }
                    if (map1.get("end_asset_value") != "" && map1.get("end_asset_value") != null) {
                        range3.replace("${list_netval_vol}", map1.get("end_asset_value").toString(), true, false);
                    } else {
                        range3.replace("${list_netval_vol}", "", true, false);
                    }
                    if (infoMap.get("list_income") != "" && infoMap.get("list_income") != null) {
                        range3.replace("${list_income}", infoMap.get("list_income").toString(), true, false);
                    } else {
                        range3.replace("${list_income}", "", true, false);
                    }
                    if (infoMap.get("list_profit") != "" && infoMap.get("list_profit") != null) {
                        range3.replace("${list_profit}", infoMap.get("list_profit").toString(), true, false);
                    } else {
                        range3.replace("${list_profit}", "", true, false);
                    }
                    table3.getRows().add(deepClone3);
                }

                //开放式份额
                Node deepClone7;
                Range range7;
                if (table7 != null) {
                    deepClone7 = table7.getLastRow().deepClone(true);
                    range7 = table7.getLastRow().getRange();

                    if (map1.get("sales_code") != "" && map1.get("sales_code") != null) {
                        range7.replace("${list_sale_code}", map1.get("sales_code").toString(), true, false);
                    } else {
                        range7.replace("${list_sale_code}", "", true, false);
                    }
                    if (map1.get("begin_total_share") != "" && map1.get("begin_total_share") != null) {
                        range7.replace("${list_begin_vol}", map1.get("begin_total_share").toString(), true, false);
                    } else {
                        range7.replace("${list_begin_vol}", "", true, false);
                    }
                    if (map1.get("dur_purch_share") != "" && map1.get("dur_purch_share") != null) {
                        range7.replace("${list_sub_vol}", map1.get("dur_purch_share").toString(), true, false);
                    } else {
                        range7.replace("${list_sub_vol}", "", true, false);
                    }
                    if (map1.get("dur_redem_share") != "" && map1.get("dur_redem_share") != null) {
                        range7.replace("${list_sub_redeem}", map1.get("dur_redem_share").toString(), true, false);
                    } else {
                        range7.replace("${list_sub_redeem}", "", true, false);
                    }
                    if (map1.get("end_total_share") != "" && map1.get("end_total_share") != null) {
                        range7.replace("${list_end_vol}", map1.get("end_total_share").toString(), true, false);
                    } else {
                        range7.replace("${list_end_vol}", "", true, false);
                    }
                    table7.getRows().add(deepClone7);
                }

            }

            if (table1 != null) {
                table1.getLastRow().remove();
                //合并资产净值单元格
                //20220406 去除单元格合并
                /*if(fileName.contains("单一资产")) {
                    Cell cellStartRange = table1.getRows().get(15).getCells().get(5); //第15行第6列
                    Cell cellEndRange = table1.getRows().get(15+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                    mergeCells(cellStartRange, cellEndRange);
                }
                if (fileName.contains("私募封闭")) {
                    Cell cellStartRange = table1.getRows().get(14).getCells().get(5); //第14行第6列
                    Cell cellEndRange = table1.getRows().get(14+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                    mergeCells(cellStartRange, cellEndRange);
                }*/
            }
            if (table2 !=null)
                table2.getLastRow().remove();
            if (table3 != null) {
                table3.getLastRow().remove();
                //合并最后两列单元格
                Cell cellStartRange1 = table3.getRows().get(3).getCells().get(5); //第3行第6列
                Cell cellEndRange1 = table3.getRows().get(3+sqlRows.size() - 1).getCells().get(5); //第n行第6列
                mergeCells(cellStartRange1, cellEndRange1);

                Cell cellStartRange2 = table3.getRows().get(3).getCells().get(6); //第3行第7列
                Cell cellEndRange2 = table3.getRows().get(3+sqlRows.size() - 1).getCells().get(6); //第n行第7列
                mergeCells(cellStartRange2, cellEndRange2);

            }
            if (table7 != null) {
                table7.getLastRow().remove();
            }


        } else {
            if(table1 != null) {
                Node deepClone1 = table1.getLastRow().deepClone(true);
                Range range1 = table1.getLastRow().getRange();
                range1.replace("${list_sale_name}", "", true, false);
                range1.replace("${list_sale_code}", "", true, false);
                range1.replace("${netval}", "", true, false);
                range1.replace("${netval_total}", "", true, false);
                range1.replace("${netval_vol}", "", true, false);
                table1.getRows().add(deepClone1);
                table1.getLastRow().remove();
            }
            if (table2 != null) {
                Node deepClone2 = table2.getLastRow().deepClone(true);
                Range range2 = table2.getLastRow().getRange();
                range2.replace("${list_sale_name}", "", true, false);
                range2.replace("${list_report}", "", true, false);
                range2.replace("${list_subsist}", "", true, false);
                table2.getRows().add(deepClone2);
                table2.getLastRow().remove();
            }

            if (table3 != null) {
                Node deepClone3 = table3.getLastRow().deepClone(true);
                Range range3 = table3.getLastRow().getRange();
                range3.replace("${list_sale_code}", "", true, false);
                range3.replace("${list_netval_end}", "", true, false);
                range3.replace("${list_netval_total}", "", true, false);
                range3.replace("${list_end_vol}", "", true, false);
                range3.replace("${list_netval_vol}", "", true, false);
                range3.replace("${list_income}", "", true, false);
                range3.replace("${list_profit}", "", true, false);
                table3.getRows().add(deepClone3);
                table3.getLastRow().remove();
            }
            if (table7 != null) {
                Node deepClone7 = table7.getLastRow().deepClone(true);
                Range range7 = table7.getLastRow().getRange();
                range7.replace("${list_sale_code}", "", true, false);
                range7.replace("${list_begin_vol}", "", true, false);
                range7.replace("${list_sub_vol}", "", true, false);
                range7.replace("${list_sub_redeem}", "", true, false);
                range7.replace("${list_end_vol}", "", true, false);
                table7.getRows().add(deepClone7);
                table7.getLastRow().remove();
            }

        }
        return document;
    }

    /**
     * @Description 合并单元格
     * @Date 2021/11/30 16:08
     * @Param [startCell, endCell] 开始cell  结束cell
     * @Return void
     */
    private static void mergeCells(Cell startCell, Cell endCell) {
        Table parentTable = startCell.getParentRow().getParentTable();

        Point startCellPos = new Point(startCell.getParentRow().indexOf(startCell), parentTable.indexOf(startCell.getParentRow()));
        Point endCellPos = new Point(endCell.getParentRow().indexOf(endCell), parentTable.indexOf(endCell.getParentRow()));
        Rectangle mergeRange = new Rectangle(Math.min(startCellPos.x, endCellPos.x), Math.min(startCellPos.y, endCellPos.y), Math.abs(endCellPos.x - startCellPos.x) + 1,
                Math.abs(endCellPos.y - startCellPos.y) + 1);

        for (Row row : parentTable.getRows()) {
            for (Cell cell : row.getCells()) {
                Point currentPos = new Point(row.indexOf(cell), parentTable.indexOf(row));

                if (mergeRange.contains(currentPos)) {
                    if (currentPos.x == mergeRange.x)
                        cell.getCellFormat().setHorizontalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setHorizontalMerge(CellMerge.PREVIOUS);

                    if (currentPos.y == mergeRange.y)
                        cell.getCellFormat().setVerticalMerge(CellMerge.FIRST);
                    else
                        cell.getCellFormat().setVerticalMerge(CellMerge.PREVIOUS);
                }
            }
        }
    }

    /**
     * 批量下载信披公告文件
     * @param response
     */
    @RequestMapping(value = "/print/downloadXPGGPrintVersion.json",produces = { "application/json;charset=UTF-8"})
    public void downloadXPGGPrintVersion(HttpServletResponse response) {
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        Map<String, Object> params = RequestSupport.getParameters();
        String crtPath = (String)params.get("crtPath");
        if(!"null".equals(crtPath)&&!"".equals(crtPath)) {
            File file = new File (crtPath);
            log.info("文件路径【{}】",crtPath);
            try{
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                response.setContentType("application/msword;charset=UTF-8");
                response.addHeader("Content-Disposition", "attachment;filename=" +
                        new String((((String)params.get("fileName")).replaceAll("docx", "pdf")).getBytes("GB2312"), "ISO8859-1"));
                outputStream = new BufferedOutputStream(response.getOutputStream());
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.flush();
                    i = bufferedInputStream.read(buffer);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }finally {
                try {
                    if(fileInputStream!=null) {
                        fileInputStream.close();
                    }
                    if(bufferedInputStream!=null) {
                        bufferedInputStream.close();
                    }
                    if(outputStream!=null) {
                        outputStream.close();
                    }
                } catch (Exception e2) {
                    log.error("io关闭异常[{}]",e2);
                }
            }

        }
    }

    /**
     * 信披公告对应版本下载
     * 本地文件下载
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/downloadXPGGHandVersion.json", produces = {"application/json;charset=UTF-8"})
    public void downloadXPGGHandVersion(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();

        /**
         * 生成文件并替换文件内容
         */
        Map<String, Object> genParams = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(String.valueOf(params.get("t8DisclosureNoticeId")), String.valueOf(params.get("id")), params.get("noticeTitle").toString(), false);

        /**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
        String suffix = genParams.get("suffix").toString();
        String downloadPath = genParams.get("filePath").toString() + suffix;
        response.setHeader("filename", params.get("noticeTitle").toString() + suffix);
        FileUtil.downFileToBrowser(response, downloadPath);
    }

    /**
     * 信披公告预览
     * 本地文件生成替换及下载
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/previewXPGGByNoticeVersion.json", produces = {"application/json;charset=UTF-8"})
    public void previewXPGGByNoticeVersionId(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        Map<String, Object> genParams=new HashMap<>();
        if(params.get("fileName").toString().lastIndexOf(".")>0){
            String fileName = params.get("fileName").toString().substring(0,params.get("fileName").toString().lastIndexOf("."));
            String suffixName = params.get("fileName").toString().substring(params.get("fileName").toString().lastIndexOf("."));
            if(".pdf".equals(suffixName)){
                //手工公告不需要模板组装数据
                if("11".equals(params.get("disclosureType"))){
                    genParams.put("suffix",suffixName);
                    genParams.put("filePath",params.get("filePath")+"/"+fileName);

                }else{
                    genParams = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(String.valueOf(params.get("id")), String.valueOf(params.get("maxVersionId")), params.get("noticeTitle").toString(), false);
                }
                /**
                 * 生成文件并替换文件内容
                 */
                /**pdf后缀的文件直接返回*/
                String newPath = genParams.get("filePath").toString() + ".pdf";
                response.setHeader("filename", fileName + ".pdf");
                response.setContentType("application/x-www-form-urlencoded;charset=utf-8");
                FileUtil.downFileToBrowser(response, newPath);
            }else{
                //手工公告不需要模板组装数据
                if("11".equals(params.get("disclosureType"))){
                    genParams.put("suffix",".docx");
                    genParams.put("filePath",params.get("filePath")+separate+fileName);

                }else{
                    genParams = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(String.valueOf(params.get("id")), String.valueOf(params.get("maxVersionId")), params.get("noticeTitle").toString(), false);
                }
                /**
                 * 生成文件并替换文件内容
                 */
                /**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
                String suffix = genParams.get("suffix").toString();
                String downloadPath = genParams.get("filePath").toString() + suffix;//拼接公告路径、名称及原模板版本名称后缀
                String newPath = genParams.get("filePath").toString() + ".pdf";
                response.setHeader("filename", params.get("noticeTitle").toString() + ".pdf");
                response.setContentType("application/x-www-form-urlencoded;charset=utf-8");
                wordToPdfUtil.doc2pdf(downloadPath, newPath);
                FileUtil.downFileToBrowser(response, newPath);
            }
        }else{
            //手工公告不需要模板组装数据
            if("11".equals(params.get("disclosureType"))){
                genParams.put("suffix",".docx");
                genParams.put("filePath",params.get("filePath")+separate+params.get("fileName"));

            }else{
                genParams = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(String.valueOf(params.get("id")), String.valueOf(params.get("maxVersionId")), params.get("noticeTitle").toString(), false);
            }
            /**
             * 生成文件并替换文件内容
             */
            /**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
            String suffix = genParams.get("suffix").toString();
            String downloadPath = genParams.get("filePath").toString() + suffix;//拼接公告路径、名称及原模板版本名称后缀
            String newPath = genParams.get("filePath").toString() + ".pdf";
            response.setHeader("filename", params.get("noticeTitle").toString() + ".pdf");
            response.setContentType("application/x-www-form-urlencoded;charset=utf-8");
            wordToPdfUtil.doc2pdf(downloadPath, newPath);
            FileUtil.downFileToBrowser(response, newPath);
        }


    }

    /**
     * 信披公告对应版本下载
     * 本地文件生成替换及下载
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/downloadXPGGByNoticeVersion.json", produces = {"application/json;charset=UTF-8"})
    public void downloadXPGGByNoticeVersionId(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        /**
         * 生成文件并替换文件内容
         */
        Map<String, Object> genParams = disclosureNoticeDocService.judgeNoticeDocIsExistAndGenerateDoc(String.valueOf(params.get("id")), String.valueOf(params.get("maxVersionId")), params.get("noticeTitle").toString(), false);

        /**该文件后缀为公告生成后替换的公告文件后缀，应与模板一致，若无，则 .docx */
        String suffix = genParams.get("suffix").toString();
        String downloadPath = genParams.get("filePath").toString() + suffix;//拼接公告路径、名称及原模板版本名称后缀

        response.setHeader("filename", params.get("noticeTitle").toString() + suffix);
        FileUtil.downFileToBrowser(response, downloadPath);
    }

    @RequestMapping(value = "/print/saveXPGGTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public void saveXPGGPrintTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
        OutputStream ostream = null;
        InputStream is = null;
        FileInputStream fileInputStream = null;
        try{
            DisclosureRule dis = disclosureRuleDao.findGGDisclosureRules((String) params.get("id"));
            DisclosureModVersion disclosureModVersion = disclosureModVersionService.getPrintXPVersionById((String) dis.getId());
            Map<String, Object> map = new HashMap<>();
            map.put("prodCode",params.get("prodCode"));
            map.put("t8DisclosureNoticeId",params.get("id"));
            SqlRow infoMap = disclosureNoticeService.findDataInfoByType(map);
            if (infoMap != null && infoMap.size()>0){
                fileStorePath = OnlineUtils.getOnlinepath(winPath);
                String filePath = fileStorePath + "xpTemp" + "/" + disclosureModVersion.getId();
                //File file = new File(filePath);
                File word = new File(filePath,disclosureModVersion.getDocName());
                fileInputStream = new FileInputStream(word);

                // 载入模板文件
                WordprocessingMLPackage wPackage = WordprocessingMLPackage.load(fileInputStream);
                // 提取正文
                MainDocumentPart mainDocumentPart = wPackage.getMainDocumentPart();
                org.docx4j.wml.Document wmlDoc = (org.docx4j.wml.Document) mainDocumentPart.getJaxbElement();
                org.docx4j.wml.Body body = wmlDoc.getBody();
                // 提取正文中所有段落
                List<Object> paragraphs = body.getContent();
                // 提取书签并创建书签的游标
                RangeFinder rt = new RangeFinder("CTBookmark", "CTMarkupRange");
                new TraversalUtil(paragraphs, rt);
                // 遍历书签
                for (CTBookmark bm : rt.getStarts()) {
                    // 这儿可以对单个书签进行操作，也可以用一个map对所有的书签进行处理
                    if (bm.getName().equals("comparisonChart")) {
                        // 读入图片并转化为字节数组，因为docx4j只能字节数组的方式插入图片

                        String os = System.getProperty("os.name");
                        String path = "";
                        if(os.toLowerCase().startsWith("win")){
                            String prodCode = params.get("prodCode").toString();
                            String baseDate = params.get("prodBaseDate").toString();
                            is = new FileInputStream("D:\\ChromDownloads\\净值表现与市场对比.png");
                        }else{
                            String prodCode = params.get("prodCode").toString();
                            String baseDate = params.get("prodBaseDate").toString();
                            is = new FileInputStream("/home/ftpuser/test/echarts/EB1844/20210529/202105291622270542574.jpg");
                        }
                        byte[] bytes = IOUtils.toByteArray(is);
                        // 穿件一个行内图片
                        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wPackage, bytes);
                        // createImageInline函数的前四个参数我都没有找到具体啥意思，，，，
                        // 最有一个是限制图片的宽度，缩放的依据
                        Inline inline = imagePart.createImageInline(null, null, 0, 1, false, 5000);
                        // 获取该书签的父级段落
                        P p = (P) (bm.getParent());
                        ObjectFactory factory = new ObjectFactory();
                        // R对象是匿名的复杂类型，然而我并不知道具体啥意思，估计这个要好好去看看ooxml才知道
                        R run = factory.createR();
                        // drawing理解为画布？
                        Drawing drawing = factory.createDrawing();
                        drawing.getAnchorOrInline().add(inline);
                        run.getContent().add(drawing);
                        p.getContent().add(run);
                        is.close();
                    }
                }
                wPackage.save(new FileOutputStream(filePath+"/temp"+disclosureModVersion.getDocName()));

                wordToPdfUtil.getLicense();
                //com.aspose.words.Document document = new com.aspose.words.Document(filePath+File.separator+disclosureModVersion.getDocName());
                com.aspose.words.Document document = new com.aspose.words.Document(filePath+"/temp"+disclosureModVersion.getDocName());
                // 获得要替换的word模板
                Range range = document.getRange();// range获取word中的内容
                Iterator<Map.Entry<String, Object>> it = infoMap.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String, Object> entry = it.next();
                    //System.out.println("key:"+entry.getKey()+" key:"+entry.getValue());
                    String str = "";
                    if(entry.getValue()!=null){
                        str = entry.getValue().toString();
                        if("brief,financier,term".contains(entry.getKey())) {
                            str = str.replaceAll("\n\r",ControlChar.LINE_BREAK);
                            str = str.replaceAll("\r\n",ControlChar.LINE_BREAK);
                            str = str.replaceAll("\n", ControlChar.LINE_BREAK);
                        }else {
                            if (str.contains("\n\r")) {
                                str = formatStr(str,"\n\r");
                            }
                            if (str.contains("\r\n")) {
                                str = formatStr(str,"\r\n");
                            }
                            if (str.contains("\n")) {
                                str = formatStr(str,"\n");
                            }
                        }

                    }
                    logger.info("key------------------>{},value------------------->{}",entry.getKey(),str);
                    range.replace("${" + entry.getKey() + "}", str,true,false);
                }
                //定期报告资产配置明细
                Table table = (Table) document.getChild(NodeType.TABLE, 3, true);
                if(table==null){
                    Table table4 = (Table) document.getChild(NodeType.TABLE, 1, true);
                    List<SqlRow> list = disclosureNoticeService.findDetailList(params.get("prodCode").toString(),params.get("id").toString(),params.get("prodBaseDate").toString());
                    Integer i=0;
                    if(list!=null && list.size()>0){
                        for(Map map2:list){
                            i=i+1;
                            Node deepClone = table4.getLastRow().deepClone(true);
                            Range range2 = table4.getLastRow().getRange();
                            if(map2.get("list_id") != null &&  StringUtils.isNotBlank(map2.get("list_id").toString())){
                                if("合计".equals(map2.get("list_id").toString())){
                                    range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                }else{
                                    if(Float.parseFloat(map2.get("list_id").toString())>6){
                                        range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                    }else{
                                        range2.replace("${list_id}", map2.get("list_id").toString().substring(0,map2.get("list_id").toString().indexOf(".")), true, false);
                                    }
                                }
                            }else{
                                range2.replace("${list_id}", "", true, false);
                            }
                            if(map2.get("list_assets_type")!=""&&map2.get("list_assets_type")!=null){
                                if("总资产".equals(map2.get("list_assets_type").toString())){
                                    range2.replace("${list_assets_type}", "", true, false);
                                }else{
                                    range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(), true, false);
                                }
                            }else{
                                range2.replace("${list_assets_type}", "", true, false);
                            }
                            if(map2.get("list_amount")!=""&&map2.get("list_amount")!=null){
                                range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
                            }else{
                                range2.replace("${list_amount}", "", true, false);
                            }
                            if(map2.get("list_asset_ratio")!=""&&map2.get("list_asset_ratio")!=null){
                                range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true, false);
                            }else{
                                range2.replace("${list_asset_ratio}", "", true, false);
                            }

                            table4.getRows().add(deepClone);
                        }
                        table4.getLastRow().remove();
                    }else{
                        Node deepClone = table4.getLastRow().deepClone(true);
                        Range range2 = table4.getLastRow().getRange();
                        range2.replace("${list_id}", "", true, false);
                        range2.replace("${list_assets_type}", "", true, false);
                        range2.replace("${list_amount}", "", true, false);
                        range2.replace("${list_asset_ratio}", "", true, false);
                        table4.getRows().add(deepClone);
                        table4.getLastRow().remove();
                    }
                }else{
                    List<SqlRow> list = disclosureNoticeService.findDetailList(params.get("prodCode").toString(),params.get("id").toString(),params.get("prodBaseDate").toString());
                    Integer i=0;
                    if(list!=null && list.size()>0){
                        for(Map map2:list){
                            i=i+1;
                            Node deepClone = table.getLastRow().deepClone(true);
                            Range range2 = table.getLastRow().getRange();
                            if(map2.get("list_id") != null &&  StringUtils.isNotBlank(map2.get("list_id").toString())){
                                if("合计".equals(map2.get("list_id").toString())){
                                    range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                }else{
                                    if(Float.parseFloat(map2.get("list_id").toString())>6){
                                        range2.replace("${list_id}", map2.get("list_id").toString(), true, false);
                                    }else{
                                        range2.replace("${list_id}", map2.get("list_id").toString().substring(0,map2.get("list_id").toString().indexOf(".")), true, false);
                                    }
                                }
                            }else{
                                range2.replace("${list_id}", "", true, false);
                            }
                            if(map2.get("list_assets_type")!=""&&map2.get("list_assets_type")!=null){
                                if("总资产".equals(map2.get("list_assets_type").toString())){
                                    range2.replace("${list_assets_type}", "", true, false);
                                }else{
                                    range2.replace("${list_assets_type}", map2.get("list_assets_type").toString(), true, false);
                                }
                            }else{
                                range2.replace("${list_assets_type}", "", true, false);
                            }
                            if(map2.get("list_amount")!=""&&map2.get("list_amount")!=null){
                                range2.replace("${list_amount}", map2.get("list_amount").toString(), true, false);
                            }else{
                                range2.replace("${list_amount}", "", true, false);
                            }
                            if(map2.get("list_asset_ratio")!=""&&map2.get("list_asset_ratio")!=null){
                                range2.replace("${list_asset_ratio}", map2.get("list_asset_ratio").toString(), true, false);
                            }else{
                                range2.replace("${list_asset_ratio}", "", true, false);
                            }

                            table.getRows().add(deepClone);
                        }
                        table.getLastRow().remove();
                    }else{
                        Node deepClone = table.getLastRow().deepClone(true);
                        Range range2 = table.getLastRow().getRange();
                        range2.replace("${list_id}", "", true, false);
                        range2.replace("${list_assets_type}", "", true, false);
                        range2.replace("${list_amount}", "", true, false);
                        range2.replace("${list_asset_ratio}", "", true, false);
                        table.getRows().add(deepClone);
                        table.getLastRow().remove();
                    }
                }



                //定期报告十大资产明细
                Table table2 = (Table) document.getChild(NodeType.TABLE, 4, true);
                if(table2==null){

                }else{
                    List<SqlRow> list2 = disclosureNoticeService.findTenDetailList(params.get("id").toString());
                    if(list2!=null && list2.size()>0) {
                        for (Map map3 : list2) {
                            Node deepClone2 = table2.getLastRow().deepClone(true);
                            Range range2 = table2.getLastRow().getRange();
                            if (map3.get("ten_list_id") != "" && map3.get("ten_list_id") != null) {
                                range2.replace("${ten_list_id}", map3.get("ten_list_id").toString(), true, false);
                            } else {
                                range2.replace("${ten_list_id}", "", true, false);
                            }
                            if (map3.get("ten_assets_code") != "" && map3.get("ten_assets_code") != null) {
                                range2.replace("${ten_assets_code}", map3.get("ten_assets_code").toString(), true, false);
                            } else {
                                range2.replace("${ten_assets_code}", "", true, false);
                            }
                            if (map3.get("ten_assets_name") != "" && map3.get("ten_assets_name") != null) {
                                range2.replace("${ten_assets_name}", map3.get("ten_assets_name").toString(), true, false);
                            } else {
                                range2.replace("${ten_assets_name}", "", true, false);
                            }
                            if (map3.get("ten_assets_scale") != "" && map3.get("ten_assets_scale") != null) {
                                range2.replace("${ten_assets_scale}", map3.get("ten_assets_scale").toString(), true, false);
                            } else {
                                range2.replace("${ten_assets_scale}", "", true, false);
                            }
                            if (map3.get("ten_asset_ratio") != "" && map3.get("ten_asset_ratio") != null) {
                                range2.replace("${ten_asset_ratio}", map3.get("ten_asset_ratio").toString(), true, false);
                            } else {
                                range2.replace("${ten_asset_ratio}", "", true, false);
                            }

                            table2.getRows().add(deepClone2);
                        }
                        table2.getLastRow().remove();
                    }else{
                        Node deepClone2 = table2.getLastRow().deepClone(true);
                        Range range2 = table2.getLastRow().getRange();
                        range2.replace("${ten_list_id}", "", true, false);
                        range2.replace("${ten_assets_code}", "", true, false);
                        range2.replace("${ten_assets_name}", "", true, false);
                        range2.replace("${ten_assets_scale}", "", true, false);
                        range2.replace("${ten_asset_ratio}", "", true, false);
                        table2.getRows().add(deepClone2);

                        table2.getLastRow().remove();
                    }
                }

                document.save(filePath+"/temp2/"+disclosureModVersion.getDocName(), SaveFormat.DOCX);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }finally {
            if (ostream != null) {
                try {
                    ostream.flush();
                    ostream.close();
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
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }


        }
    }
    public String formatStr(String str,String type) {
        String arrays[] = null;
        String mStr=Strings.EMPTY;
        arrays = str.split(type);
        for(int i=0;i<arrays.length;i++) {
            if(i==0) {
                mStr=arrays[i];
            }else {
                if("".equals(arrays[i])||arrays[i].length()==0)
                    continue;
                mStr = mStr+ControlChar.LINE_BREAK+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+
                        ControlChar.NON_BREAKING_SPACE+arrays[i];
            }

        }

        if(arrays!=null)
            str = mStr;
        return str;
    }
}

