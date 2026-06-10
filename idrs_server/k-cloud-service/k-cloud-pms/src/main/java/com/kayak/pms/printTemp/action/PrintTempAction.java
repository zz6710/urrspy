package com.kayak.pms.printTemp.action;

import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.interceptor.MethodAnnotation;
import com.kayak.pms.printTemp.model.PrintTemp;
import com.kayak.pms.printTemp.model.PrintTempVersion;
import com.kayak.pms.printTemp.service.PrintTempService;
import com.kayak.pms.printTemp.service.PrintTempVersionService;
import com.kayak.pms.printTemp.utils.NetWorkUtils;
import com.kayak.pms.printTemp.utils.PrintTempUtil;
import com.kayak.pms.printTemp.utils.WordCompareUtil;
import com.kayak.pms.printTemp.utils.WordToHtmlUtil;
import com.kayak.utils.OnlineUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: k-cloud
 * @description: 文档上传Action
 * @author: WangZhenXin
 * @create: 2020-12-26 18:41
 * @memo 备注信息
 */
@RestController
public class PrintTempAction extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempAction.class);

    @Autowired
    private PrintTempService printTempService;

    @Autowired
    private PrintTempVersionService printTempVersionService;

    //文件分隔符
    private static final String separate = "/";

    private String fileStorePath;

    @Value("${path.word}")
    private String wordPath;

    @MethodAnnotation(desc="文档模板管理-文档上传")
    @RequestMapping(value = "/print/uploadPrintTemp.json",produces = { "application/json;charset=UTF-8"})
    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        if (file != null) {
            //获取参数
            Map<String, Object> params = RequestSupport.getParameters();
            try {
                DaoUtil.doTrans(()->{
                    //遍历文件
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
//                            return RequestSupport.updateReturnJson(false, "请上传Word文件" + fileName, null).toString();
                        }
                        PrintTemp printTemp = new PrintTemp();
                        printTemp.setDocType((String) params.get("docType"));
                        printTemp.setTempType((String) params.get("tempType"));
                        printTemp.setRemark((String) params.get("remark"));
                        printTemp.setDistributorCode((String) params.get("distributorCode"));
                        printTemp.setT8TruteeInfoId((String) params.get("t8TruteeInfoId"));
                        printTemp.setT8MeetCreateId((String) params.get("t8MeetCreateId"));
                        printTemp.setTempName(fileName.substring(0, fileName.lastIndexOf(".")));
                        //获取word 转 html 文件信息
                        String temp_html = WordToHtmlUtil.wordToHtml(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".")));
                        //减产文档模板基础信息是否存在，不存在则新增
                        Integer cont = printTempService.checkPrintTemp(printTemp);
                        if (cont > 0) {
                            throw new PromptException("该产品文档已存在" + fileName);
//                            return RequestSupport.updateReturnJson(false, "该产品文档已存在", null).toString();
                        }
                        String printTempId = printTempService.savePrintTempInfo(printTemp);
                        //组装文档模板版本信息
                        PrintTempVersion printTempVersion = new PrintTempVersion();
                        printTempVersion.setT8PrintTempId(printTempId);
                        String version = (String) params.get("version");
                        printTempVersion.setVersion(version);
                        printTempVersion.setTempName(PrintTempUtil.versionToFile(fileName, version));
                        printTempVersion.setTempHtml(temp_html);
                        printTempVersion.setRemark((String) params.get("remark"));
                        printTempVersionService.savePrintTempVersion(printTempVersion);
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
        return RequestSupport.updateReturnJson(true, "文件上传成功", null).toString();
    }

    @MethodAnnotation(desc="文档模板管理-文档版本上传")
    @RequestMapping(value = "/print/uploadPrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String uploadPrintTempVersion(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        //定义返回参数集合
        Map<String,Object> returnMap = new HashMap<>();
        if (file != null) {
            //获取参数
            Map<String, Object> params = RequestSupport.getParameters();
            try {
                DaoUtil.doTrans(()->{
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
//                            return RequestSupport.updateReturnJson(false, "请上传Word文件" + fileName, null).toString();
                        }
                        //获取word 转 html 文件信息
                        String temp_html = WordToHtmlUtil.wordToHtml(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".")));
                        //组装文档模板版本信息
                        PrintTempVersion printTempVersion = new PrintTempVersion();
                        String t8PrintTempId = (String) params.get("t8PrintTempId");
                        printTempVersion.setT8PrintTempId(t8PrintTempId);
                        String newVersion = printTempVersionService.getNewestPrintTempVersion(t8PrintTempId);
                        String[] split = newVersion.split("\\.");
                        //最新版本等于最大版本+1
                        int i = Integer.parseInt(split[1]) + 1;
                        newVersion = split[0] + "." + i;
                        printTempVersion.setVersion(newVersion);
                        printTempVersion.setTempName(PrintTempUtil.versionToFile(fileName, newVersion));
                        printTempVersion.setTempHtml(temp_html);
                        printTempVersion.setRemark((String) params.get("remark"));
                        printTempVersionService.savePrintTempVersion(printTempVersion);
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
        returnMap.put("type","2");
        return RequestSupport.updateReturnJson(true, "文件上传成功", returnMap).toString();
    }

    /**
     * 下载产品模板对应版本
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/print/downloadPrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersion(HttpServletResponse response) {
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
            PrintTempVersion printTempVersion = printTempVersionService.getPrintTempVersionById((String) params.get("id"),processInstanceId);
            if (printTempVersion != null) {
                //获取路径
                fileStorePath = OnlineUtils.getOnlinepath(wordPath);
                String filePath = fileStorePath + "printTemp" + "/" + printTempVersion.getId() + "/" + printTempVersion.getTempName();
                File file = new File(filePath);
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                //输出文件
                response.setContentType("application/msword;charset=UTF-8");//导出word格式
                response.addHeader("Content-Disposition", "attachment;filename=" +
                        new String(printTempVersion.getTempName().getBytes("GB2312"), "ISO8859-1"));
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
        }
    }

    /**
     * 预览产品模板对应版本
     *
     * @return out
     * @throws Exception Exception
     */
    @RequestMapping(value = "/print/previewPrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String previewPrintTempVersion() throws PromptException {
        Map<String, Object> params = RequestSupport.getParameters();
        try{
            PrintTempVersion printTempVersion = printTempVersionService.getPrintTempVersionById((String) params.get("id"),"");
            String rootPath = PrintTempUtil.getRootPath();
            String path2 = rootPath+"/static/printTemp.html";
            //清空指定打印文件内容，重新写入
            WordToHtmlUtil.clearInfoForFile(path2);
            if (printTempVersion != null) {
                String temp_html = printTempVersion.getTempHtml();
                Document document = Jsoup.parse(temp_html);
                temp_html = document.html();
                //在线打印
                //填充打印文件内容
                WordToHtmlUtil.writeFile(temp_html, path2);
                String url = NetWorkUtils.generateUrl("printTemp.html");
                Map<String,Object> returnMap = new HashMap<>();
                returnMap.put("url",url);
                return RequestSupport.updateReturnJson(true,"生成成功",returnMap).toString();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        throw new PromptException("预览信息生成失败");
    }


    /**
     * 文档比对
     *
     * @return out
     * @throws Exception Exception
     */
    @MethodAnnotation(desc="文档模板管理-文档对比")
    @RequestMapping(value = "/print/comparePrintTempVersion.json",produces = { "application/json;charset=UTF-8"})
    public String comparePrintTempVersion(@RequestParam(value = "file", required = false) MultipartFile file) throws PromptException {
        Map<String, Object> params = RequestSupport.getParameters();
        InputStream bais = null;
        InputStream bais1 = null;

        try{
            PrintTempVersion printTempVersion = printTempVersionService.getPrintTempVersionByPrintTempVersion(params);
            String temp_html = printTempVersion.getTempHtml();
            Document document = Jsoup.parse(temp_html);
            //去掉meto标签
            Elements elements = document.select("meta");
            for (Element el : elements) {
                el.remove();
            }
            elements = document.select("html");
            elements.attr("xmlns:v", "urn:schemas-microsoft-com:vml");
            elements.attr("xmlns:o", "urn:schemas-microsoft-com:office:office");
            elements.attr("xmlns:w", "urn:schemas-microsoft-com:office:word");
            elements.attr("xmlns:m", "http://schemas.microsoft.com/office/2004/12/omml");
            elements.attr("xmlns", "http://www.w3.org/TR/REC-html40");
            //doc展示居中
            elements = document.select("head");
            //在head标签里面增加以下word文档属性：其中<w:View>Print</w:View>是页面视图;<w:View>Web</w:View>的话就是web模式 ;<w:View>MasterPages</w:View> 是大纲模式
            elements.prepend("<!--[if gte mso 9]><xml><w:WordDocument><w:View>Print</w:View><w:TrackMoves>false</w:TrackMoves><w:TrackFormatting/><w:ValidateAgainstSchemas/><w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid><w:IgnoreMixedContent>false</w:IgnoreMixedContent><w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText><w:DoNotPromoteQF/><w:LidThemeOther>EN-US</w:LidThemeOther><w:LidThemeAsian>ZH-CN</w:LidThemeAsian><w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript><w:Compatibility><w:BreakWrappedTables/><w:SnapToGridInCell/><w:WrapTextWithPunct/><w:UseAsianBreakRules/><w:DontGrowAutofit/><w:SplitPgBreakAndParaMark/><w:DontVertAlignCellWithSp/><w:DontBreakConstrainedForcedTables/><w:DontVertAlignInTxbx/><w:Word11KerningPairs/><w:CachedColBalance/><w:UseFELayout/></w:Compatibility><w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel><m:mathPr><m:mathFont m:val=\"Cambria Math\"/><m:brkBin m:val=\"before\"/><m:brkBinSub m:val=\"--\"/><m:smallFrac m:val=\"off\"/><m:dispDef/><m:lMargin m:val=\"0\"/> <m:rMargin m:val=\"0\"/><m:defJc m:val=\"centerGroup\"/><m:wrapIndent m:val=\"1440\"/><m:intLim m:val=\"subSup\"/><m:naryLim m:val=\"undOvr\"/></m:mathPr></w:WordDocument></xml><![endif]-->");
            temp_html = document.html();
            //下载doc
            byte b[] = temp_html.getBytes("utf-8");  //这里是必须要设置编码的，不然导出中文就会乱码。
            bais = new ByteArrayInputStream(b);

            String fileName = file.getOriginalFilename();
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
                return RequestSupport.updateReturnJson(false, "请上传Word文件" + fileName, null).toString();
            }
            String tempHtml1 = WordToHtmlUtil.wordToHtml(file.getInputStream(), fileName.substring(fileName.lastIndexOf(".")));
            Document document1 = Jsoup.parse(tempHtml1);
            //去掉meto标签
            Elements elements1 = document1.select("meta");
            for (Element el : elements1) {
                el.remove();
            }
            elements1 = document1.select("html");
            elements1.attr("xmlns:v", "urn:schemas-microsoft-com:vml");
            elements1.attr("xmlns:o", "urn:schemas-microsoft-com:office:office");
            elements1.attr("xmlns:w", "urn:schemas-microsoft-com:office:word");
            elements1.attr("xmlns:m", "http://schemas.microsoft.com/office/2004/12/omml");
            elements1.attr("xmlns", "http://www.w3.org/TR/REC-html40");
            //doc展示居中
            elements1 = document1.select("head");
            //在head标签里面增加以下word文档属性：其中<w:View>Print</w:View>是页面视图;<w:View>Web</w:View>的话就是web模式 ;<w:View>MasterPages</w:View> 是大纲模式
            elements1.prepend("<!--[if gte mso 9]><xml><w:WordDocument><w:View>Print</w:View><w:TrackMoves>false</w:TrackMoves><w:TrackFormatting/><w:ValidateAgainstSchemas/><w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid><w:IgnoreMixedContent>false</w:IgnoreMixedContent><w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText><w:DoNotPromoteQF/><w:LidThemeOther>EN-US</w:LidThemeOther><w:LidThemeAsian>ZH-CN</w:LidThemeAsian><w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript><w:Compatibility><w:BreakWrappedTables/><w:SnapToGridInCell/><w:WrapTextWithPunct/><w:UseAsianBreakRules/><w:DontGrowAutofit/><w:SplitPgBreakAndParaMark/><w:DontVertAlignCellWithSp/><w:DontBreakConstrainedForcedTables/><w:DontVertAlignInTxbx/><w:Word11KerningPairs/><w:CachedColBalance/><w:UseFELayout/></w:Compatibility><w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel><m:mathPr><m:mathFont m:val=\"Cambria Math\"/><m:brkBin m:val=\"before\"/><m:brkBinSub m:val=\"--\"/><m:smallFrac m:val=\"off\"/><m:dispDef/><m:lMargin m:val=\"0\"/> <m:rMargin m:val=\"0\"/><m:defJc m:val=\"centerGroup\"/><m:wrapIndent m:val=\"1440\"/><m:intLim m:val=\"subSup\"/><m:naryLim m:val=\"undOvr\"/></m:mathPr></w:WordDocument></xml><![endif]-->");
            temp_html = document1.html();
            //下载doc
            byte b1[] = temp_html.getBytes("utf-8");  //这里是必须要设置编码的，不然导出中文就会乱码。
            bais1 = new ByteArrayInputStream(b1);
            //获取路径
            fileStorePath = OnlineUtils.getOnlinepath(wordPath);
            WordCompareUtil.compareWordByInputStream(bais, bais1, fileStorePath);
            String url = NetWorkUtils.generateUrl("printCompare.html");
            Map<String,Object> returnMap = new HashMap<>();
            returnMap.put("type","1");
            returnMap.put("url",url);
            return RequestSupport.updateReturnJson(true,"生成比对文件成功",returnMap).toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        throw new PromptException("文档比对信息生成失败");
    }




}
