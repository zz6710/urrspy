package com.kayak.rpt.zz.manage.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.zz.manage.service.ExcelToMapService;
import com.kayak.rpt.zz.manage.service.TrPractyRegistInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class TrPractyRegistInfoAction extends BaseController {

    @Autowired
    private TrPractyRegistInfoService trPractyRegistInfoService;

    @Autowired
    private ExcelToMapService excelToMapService;


    // MethodAnnotation(desc="从业人员登记信息-批量上传")
    @RequestMapping(value = "/chinaBondSubmit/TrPractyRegistInfo/comn-upload.json",produces = { "application/json;charset=UTF-8"})
    public String upload(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "file") MultipartFile file) throws Exception {
        response.setContentType("text/html;chartset=UTF-8");
        Map<String, Object> returnData = new HashMap<>();
        boolean res = false;
        String result = "";
        JSONObject jsonObject1 = null;
        String fileName = file.getOriginalFilename();
        if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
            //return updateFailure("上传文件不为Excel");
            return RequestSupport.updateReturnJson(false, "请上传EXCEL!", null).toString();
        }
        try {
            result = trPractyRegistInfoService.batchImport(fileName, file);
            //jsonObject1 =JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
/**
    @RequestMapping(value = "/chinaBondSubmit/TrPractyRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFile(String fileName, HttpServletResponse response) {
        // fileName = "ImportModel.xlsx";
        fileName = "从业人员登记信息导入模板.xlsx";
        excelToMapService.exportFile(response, fileName);
    }
    */
}
