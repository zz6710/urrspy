package com.kayak.rpt.rhzj.controller;

import com.kayak.core.action.BaseController;
import com.kayak.rpt.rhzj.model.ReportPCD;
import com.kayak.rpt.rhzj.service.ReportPCDService;
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
@RequestMapping(value = "/reportPCD")
public class ReportPCDController extends BaseController {

    @Autowired
    private ReportPCDService reportPCDService;

    @RequestMapping(value = "/uploadPCD.json", produces = {"application/json;charset=UTF-8"})
    private String  uploadPCD(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value = "file") MultipartFile file) throws Exception {
        List<ReportPCD> reportPCDS = ExcelParse.readExcelData(file.getInputStream(), 0, 0, 0, ReportPCD.class, true, null);
        if (reportPCDS.size() < 2) {
            return updateFailure("导入文件为空文件");
        } else if (!Objects.requireNonNull(file.getOriginalFilename()).startsWith("产品资金募集信息")) {
            return updateFailure("导入模版格式错误！");
        } else {
            //移除第一标题行
            reportPCDS.remove(0);
            reportPCDService.importReportPCDData(reportPCDS);
        }
        return updateSuccess("导入成功！");
    }
}
