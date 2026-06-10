package com.kayak.rpt.rhzj.controller;

import com.kayak.core.action.BaseController;
import com.kayak.rpt.rhzj.model.ReportPVD;
import com.kayak.rpt.rhzj.model.ReportPVD3;
import com.kayak.rpt.rhzj.service.ReportPVD3Service;
import com.kayak.rpt.rhzj.service.ReportPVDService;
import com.kayak.rpt.rhzj.util.ExcelParse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/reportPVD")
public class ReportPVDController extends BaseController {

    @Autowired
    private ReportPVDService reportPVDService;

    @Autowired
    private ReportPVD3Service reportPVD3Service;

    @RequestMapping(value = "/uploadPVD.json", produces = {"application/json;charset=UTF-8"})
    private String  uploadPVD(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "file") MultipartFile file) throws Exception {
        List<ReportPVD> reportPVDS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPVD.class, true, null);
        if (reportPVDS.size() < 2) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("资产负债信息")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一标题行
            reportPVDS.remove(0);
            reportPVDService.importReportPVDData(reportPVDS);
        }
        return updateSuccess("导入成功！");
    }

    @RequestMapping(value = "/uploadPVD3.json", produces = {"application/json;charset=UTF-8"})
    private String  uploadPVD3(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "file") MultipartFile file) throws Exception {
        List<ReportPVD3> reportPVD3s = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPVD3.class, true, null);
        if (reportPVD3s.size() < 2) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("股票及其他股权资产")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一标题行
            reportPVD3s.remove(0);
            reportPVD3Service.importReportPVD3Data(reportPVD3s);
        }
        return updateSuccess("导入成功！");
    }
}
