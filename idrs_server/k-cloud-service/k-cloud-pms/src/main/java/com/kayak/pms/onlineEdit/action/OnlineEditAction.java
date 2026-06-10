package com.kayak.pms.onlineEdit.action;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.connect.utils.FileUtil;
import com.kayak.pms.interceptor.MethodAnnotation;
import com.kayak.pms.onlineEdit.model.T8OnlineWordValue;
import com.kayak.pms.onlineEdit.service.T8OnlineWordValueService;
import com.kayak.pms.printTemp.model.PrintTemp;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import com.kayak.pms.printTemp.service.PrintTempService;
import com.kayak.pms.printTemp.service.PrintTempVersionService;
import com.kayak.pms.printTemp.utils.PrintTempUtil;
import com.kayak.pms.printTemp.utils.UploadUtils;
import com.kayak.pms.printTemp.utils.WordCompareUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: k-cloud
 * @description: 文档在线编辑Action
 * @author: WangZhenXin
 * @create: 2021-01-29 17:36
 * @memo 备注信息
 */
@RestController
public class OnlineEditAction extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(OnlineEditAction.class);
    //文件分隔符
    private static final String separate = "/";

    //在线编辑引用的jquery名称
    private static final String jqueryName = "jquery.min.js";

    @Autowired
    private PrintTempService printTempService;

    @Autowired
    private PrintTempVersionService printTempVersionService;

    @Autowired
    private T8OnlineWordValueService t8OnlineWordValueService;

    private String fileStorePath;

    private String ip;

    @Value("${path.word}")
    private  String winPath;


    /**
     * 文档模板上传
     * @param file
     * @param response
     * @return
     * @throws Exception
     */
    @MethodAnnotation(desc="文档模板管理-上传文档模板")
    @RequestMapping(value = "/onlineEdit/uploadPrintTemp.json",produces = { "application/json;charset=UTF-8"})
    public String uploadPrintTemp(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
       response.setContentType("text/html;chartset=UTF-8");
        if (file != null) {
            //获取参数
//            Map<String, Object> params = RequestSupport.getParameters();
            Map<String, Object> parameters = RequestSupport.getParameters();
            Map<String, Object> params = JSONUtil.parseObj(parameters.get("params"));
            String onlineUrl = ((String) params.get("onlineUrl"));
            try {
                String s = "";
                String os = System.getProperty("os.name");
                String path = "";
                if(os.toLowerCase().startsWith("win")){
                    ip = onlineUrl;
                    fileStorePath = winPath;
                }else{
                    s="80000080003";
                    path = SysUtil.getSystemParamsByParaid(s);
                    ip = onlineUrl+path;
                    fileStorePath = path + separate;
                }
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
                        PrintTemp printTemp = new PrintTemp();
                        printTemp.setDocType((String) params.get("docType"));
                        printTemp.setTempType((String) params.get("tempType"));
                        printTemp.setRemark((String) params.get("remark"));
                        printTemp.setDistributorCode((String) params.get("distributorCode"));
                        printTemp.setT8TruteeInfoId((String) params.get("t8TruteeInfoId"));
                        printTemp.setT8MeetCreateId((String) params.get("t8MeetCreateId"));
                        printTemp.setProcessInstanceId(Tools.toString(params.get("processInstanceId")));
                        printTemp.setTempName(fileName.substring(0, fileName.lastIndexOf(".")));
                        //检查文档模板基础信息是否存在，不存在则新增
                        /*Integer cont = printTempService.checkPrintTemp(printTemp);
                        if (cont > 0) {
                            return RequestSupport.updateReturnJson(false, "上传文档模板失败,已存在该模板", null).toString();
                        }*/
                        String finalFileName1 = fileName;
                        DaoUtil.doTrans(() -> {
                        String printTempId = printTempService.savePrintTempInfo(printTemp);
                        //组装文档模板版本信息
                        PrintTempVersion printTempVersion = new PrintTempVersion();
                        printTempVersion.setT8PrintTempId(printTempId);
                        String version = (String) params.get("version");
                        printTempVersion.setVersion(version);
                        //获取word 转 html 文件信息
                       // String temp_html = WordToHtmlUtil.wordToHtml(file.getInputStream(), finalFileName1.substring(finalFileName1.lastIndexOf(".")));
                      //  printTempVersion.setTempHtml(temp_html);
                        printTempVersion.setTempName(PrintTempUtil.versionToFile(finalFileName1, version));
                        printTempVersion.setTempName(finalFileName1);
                        printTempVersion.setProcessInstanceId(Tools.toString(params.get("processInstanceId"),null));;
                        printTempVersion.setRiskNum(String.valueOf(params.get("riskNum")));
                        printTempVersion.setRemark((String) params.get("remark"));
                        String printTempVersionId = printTempVersionService.savePrintTempVersion(printTempVersion);
                        //拼接上传服务器路径 文件服务器默认linux所以路径拼接用/
                        String fileSavePath = fileStorePath + "printTemp" + separate + printTempVersionId;
                        File localPathFile = new File(fileSavePath);
                        //文件夹不存在的话创建文件夹
                        if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                            localPathFile.mkdirs();
                        }
                        File localFile = new File(fileSavePath + separate + finalFileName1);
                        //清除该目录下的文件及子目录文件而不删除该目录文件夹
                        FileUtils.cleanDirectory(new File(fileSavePath));
                        //转存文件
                        file.transferTo(localFile);
                        String json = UploadUtils.wordToHtml(fileSavePath + separate + finalFileName1, jqueryPah);
                        JSONObject jsonObject = JSONUtil.parseObj(json);
                        List<T8OnlineWordValue> t8OnlineWordValueList = new ArrayList<>();
                        String finalFileName = finalFileName1;
                        String urlPath = ip + "/printTemp" + separate + printTempVersionId + separate + finalFileName1.substring(0, finalFileName1.lastIndexOf(".")) + ".html";
                        jsonObject.forEach((key, value) -> {
                            T8OnlineWordValue t8OnlineWordValue = new T8OnlineWordValue();
                            t8OnlineWordValue.setT8PrintTempVersionId(printTempVersionId);
                            t8OnlineWordValue.setWordKey(key);
                            t8OnlineWordValue.setFileName(finalFileName);
                            t8OnlineWordValue.setViewUrl(urlPath);
                            t8OnlineWordValue.setUploadPath(fileSavePath);
                            t8OnlineWordValueList.add(t8OnlineWordValue);
                        });
                        t8OnlineWordValueService.initT8OnlineWordValue(t8OnlineWordValueList);
                });
                    }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("上传文档模板失败：{}", e.getMessage());
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
    @RequestMapping(value = "/onlineEdit/uploadPrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String uploadPrintTempVersion(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        //定义返回参数集合
        Map<String, Object> returnMap = new HashMap<>();
        if (file != null) {
            //获取参数
            //Map<String, Object> params = RequestSupport.getParameters();
            Map<String, Object> parameters = RequestSupport.getParameters();
            JSONObject params = JSONUtil.parseObj(parameters.get("params"));
            try {
                String s = "";
                String os = System.getProperty("os.name");
                String onlineUrl = ((String) params.get("onlineUrl"));
                String path = "";
                if(os.toLowerCase().startsWith("win")){
                    ip = onlineUrl;
                    fileStorePath = winPath;
                }else{
                    s="80000080003";
                    path = SysUtil.getSystemParamsByParaid(s);
                    ip = onlineUrl+path;
                    fileStorePath  = path + separate;
                }
                //设置在线编辑页面中jquery路径
                String jqueryPah = onlineUrl + separate  + jqueryName;
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
                        PrintTempVersion printTempVersion = new PrintTempVersion();
                        String t8PrintTempId = (String) params.get("t8PrintTempId");
                        printTempVersion.setT8PrintTempId(t8PrintTempId);
                        //取得最大的版本
                        String newVersion = printTempVersionService.getNewestPrintTempVersion(t8PrintTempId);
                        String[] split = newVersion.split("\\.");
                        //最新版本等于最大版本+1
                        int i = Integer.parseInt(split[1]) + 1;
                        newVersion = split[0] + "." + i;
                        printTempVersion.setVersion(newVersion);
                        //printTempVersion.setTempName(PrintTempUtil.versionToFile(fileName, newVersion));
                        printTempVersion.setTempName(fileName);
                        printTempVersion.setRiskNum(params.get("riskNum")==null?null:String.valueOf(params.get("riskNum")));
                        printTempVersion.setRemark((String) params.get("remark"));
                        //获取word 转 html 文件信息
                        /*modify by zhangchangsi 20210307 docx转换失败且目前不需要将html保存到表*/
                        //String temp_html = WordToHtmlUtil.wordToHtml(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".")));
                        String temp_html = "";
                        printTempVersion.setTempHtml(temp_html);
                        printTempVersion.setProcessInstanceId((String) params.get("processInstanceId"));
                        String printTempVersionId = printTempVersionService.savePrintTempVersion(printTempVersion);
                        //上传服务器根目录
                        //拼接上传服务器路径 文件服务器默认linux所以路径拼接用/
                        String fileSavePath = fileStorePath + "printTemp" + separate + printTempVersionId;
                        File localPathFile = new File(fileSavePath);
                        //文件夹不存在的话创建文件夹
                        if (!localPathFile.exists() && !localPathFile.isDirectory()) {
                            localPathFile.mkdirs();
                        }
                        File localFile = new File(localPathFile, fileName);
                        logger.info("文档模板上传路径:{}", localFile);
                        //转存文件
                        file.transferTo(localFile);
                        String json = UploadUtils.wordToHtml(fileSavePath + separate + fileName, jqueryPah);
                        JSONObject jsonObject = JSONUtil.parseObj(json);
                        String finalFileName = fileName;
                        String urlPath = ip +  separate + "printTemp" + separate + printTempVersionId + separate + fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
                        List<T8OnlineWordValue> t8OnlineWordValueList = new ArrayList<>();
                        jsonObject.forEach((key, value) -> {
                            T8OnlineWordValue t8OnlineWordValue = new T8OnlineWordValue();
                            t8OnlineWordValue.setT8PrintTempVersionId(printTempVersionId);
                            t8OnlineWordValue.setWordKey(key);
                            t8OnlineWordValue.setFileName(finalFileName);
                            t8OnlineWordValue.setViewUrl(urlPath);
                            t8OnlineWordValue.setUploadPath(fileSavePath);
                            t8OnlineWordValueList.add(t8OnlineWordValue);
                        });
                        t8OnlineWordValueService.initT8OnlineWordValue(t8OnlineWordValueList);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                log.info("上传文档模板失败：{}", e.getMessage());
                return RequestSupport.updateReturnJson(false, "上传文档模板失败", null).toString();
            }
        } else {
            throw new Exception("上传文档模板失败");
        }
        returnMap.put("type", "2");
        return RequestSupport.updateReturnJson(true, "文件上传成功", returnMap).toString();
    }

    @MethodAnnotation(desc="文档模板管理-上传文档对比")
    @RequestMapping(value = "/onlineEdit/comparePrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String comparePrintTempVersion(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        if (file != null) {
            //获取参数
            Map<String, Object> params = RequestSupport.getParameters();
            Map<String, Object> returnMap = new HashMap<>();
            try {
                String s = "";
                String os = System.getProperty("os.name");
                String onlineUrl = ((String) params.get("onlineUrl"));
                String path = "";
                if(os.toLowerCase().startsWith("win")){
                    ip = onlineUrl;
                    fileStorePath = winPath;
                }else{
                    s="80000080003";
                    path = SysUtil.getSystemParamsByParaid(s);
                    ip = onlineUrl+path;
                    fileStorePath  = path + separate;
                }
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
                    //获取对比版本文件信息
                    PrintTempVersion printTempVersion = printTempVersionService.getPrintTempVersionByPrintTempVersionForCompare(params);
                    String fileSavePath = fileStorePath + "printTemp" + File.separator + printTempVersion.getId();
                    //对比文件一
                    String fileVersionName = fileSavePath + File.separator + printTempVersion.getTempName();
                    //对比文件二
                    String localFilePath = FileUtil.fileSaveToLocal(file);
                    logger.info("对比文件一路径:{}", fileVersionName);
                    logger.info("对比文件二路径:{}", localFilePath);
                    WordCompareUtil.compareWordByPath(fileVersionName, localFilePath, fileStorePath);
                    /*拼接差异文件访问路径*/
                    String url = ip + "/staticFile/printCompare.html";
                    returnMap.put("type", "1");
                    returnMap.put("url", url);
                }
            } catch (Exception e) {
            	e.printStackTrace();
                log.error("文档比对失败：错误信息{}",e.toString() );
                return RequestSupport.updateReturnJson(false, "文档比对失败", null).toString();
            }
            return RequestSupport.updateReturnJson(true, "文档对比成功", returnMap).toString();
        } else {
            throw new Exception("文档比对失败");
        }
    }
}
