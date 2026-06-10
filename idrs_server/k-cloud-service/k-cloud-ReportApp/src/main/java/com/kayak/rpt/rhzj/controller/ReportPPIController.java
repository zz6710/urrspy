package com.kayak.rpt.rhzj.controller;

import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.rhzj.biz.FileGenerator;
import com.kayak.rpt.rhzj.model.ReportPIB;
import com.kayak.rpt.rhzj.model.ReportPIE;
import com.kayak.rpt.rhzj.model.ReportPPI;
import com.kayak.rpt.rhzj.service.ReportPIBService;
import com.kayak.rpt.rhzj.service.ReportPIEService;
import com.kayak.rpt.rhzj.service.ReportPPIService;
import com.kayak.rpt.rhzj.util.ExcelParse;
import com.kayak.rpt.rhzj.util.FileUtil;
import com.kayak.rpt.rhzj.util.MyZipCompressing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "/reportPPI")
public class ReportPPIController extends BaseController {


    @Autowired
    private ReportPPIService reportPPIService;
    @Autowired
    private ReportPIEService reportPIEService;
    @Autowired
    private ReportPIBService reportPIBService;
    @Autowired
    private FileGenerator fileGenerator;


    @RequestMapping(value = "/uploadPPI.json", produces = {"application/json;charset=UTF-8"})
    public String uploadPPI(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
        List<ReportPPI> reportPPIS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPPI.class, true, null);
        if (reportPPIS.size() < 4) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品基本信息")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一第二标题行
            reportPPIS.remove(0);
            reportPPIService.importReportPPIData(reportPPIS, params);
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/uploadPIE.json", produces = {"application/json;charset=UTF-8"})
    public String uploadPIE(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
        List<ReportPIE> reportPIES = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPIE.class, true, null);
        if (reportPIES.size() < 2) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品终止信息")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一第二标题行
            reportPIES.remove(0);
            reportPIEService.importReportPIEData(reportPIES, params);
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/uploadPIB.json", produces = {"application/json;charset=UTF-8"})
    public String uploadPIB(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("beginDate", request.getParameter("beginDate").replace("-", ""));
        params.put("queryDate", request.getParameter("queryDate").replace("-", ""));
        List<ReportPIB> reportPIBS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPIB.class, true, null);
        if (reportPIBS.size() < 2) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品起始募集信息")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一第二标题行
            reportPIBS.remove(0);
            reportPIBService.importReportPIBData(reportPIBS, params);
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/download.json")
    public void downloadPPISendFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String property = System.getProperty("os.name");
        String root;
        if (property.toLowerCase().startsWith("win")) {
            root = CacheUtil.getSystemParam("80000080007");
        } else {
            root = CacheUtil.getSystemParam("80000080006");
        }
        root += "/";
        params.put("root", root);
        File file = new File(root);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            String orgno = "C1086243000029";
            String flagStr;
            List<File> fileList = new ArrayList<>();
            if ("mjDataSend".equals(params.get("sendType"))) {
                fileGenerator.generatePCD1(params);
                fileList.add(new File(root + "PCD1.dat"));
                flagStr = "PCD";
            } else if ("assetsPVDSend".equals(params.get("sendType"))) {
                fileGenerator.generatePVD1(params);
                fileGenerator.generatePVD3(params);
                FileUtil.generateEmptyFile(root, "PVD2.dat");
                flagStr = "PVD";
                fileList.add(new File(root + "PVD1.dat"));
                fileList.add(new File(root + "PVD2.dat"));
                fileList.add(new File(root + "PVD3.dat"));
            } else {
                flagStr = "PPI";
                FileUtil.generateEmptyFile(root, "PIB1.dat");
                FileUtil.generateEmptyFile(root, "PIB2.dat");
                FileUtil.generateEmptyFile(root, "PIE1.dat");
                FileUtil.generateEmptyFile(root, "PPE1.dat");
                FileUtil.generateEmptyFile(root, "PPB1.dat");
                if ("prodSend".equals(params.get("sendType"))) {
                    fileGenerator.generatePIB1(params);
                    fileGenerator.generatePIB2(params);
                } else if ("zjSend".equals(params.get("sendType"))) {
                    fileGenerator.generatePPB1(params);
                } else if ("prodStopSend".equals(params.get("sendType"))) {
                    fileGenerator.generatePIE1(params);
                } else if ("zjStopSend".equals(params.get("sendType"))) {
                    fileGenerator.generatePPE1(params);
                }
                fileList.add(new File(root + "PIB1.dat"));
                fileList.add(new File(root + "PIB2.dat"));
                fileList.add(new File(root + "PIE1.dat"));
                fileList.add(new File(root + "PPB1.dat"));
                fileList.add(new File(root + "PPE1.dat"));
            }

            //生成压缩文件
            File zipFile = new File(root + flagStr + orgno + ".zip");
            MyZipCompressing.zipMutipleFiles(zipFile.getAbsolutePath(), fileList);
            request.setAttribute("doc_name", zipFile.getName());
            request.setAttribute("path", zipFile.getAbsolutePath());
            FileUtil.download(request, response);
        } catch (IOException e) {
            log.error("下载人行报表报送数据异常: ", e);
        }


    }
}
