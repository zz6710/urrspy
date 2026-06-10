package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsMonthNavInf;
import com.kayak.subject.service.DwsMonthNavInfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsMonthNavInfController extends BaseController {

    @Autowired
    private DwsMonthNavInfService dwsMonthNavInfService;

    @RequestMapping(value = "/uploadDwsMonthNavInf.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsMonthNavInf(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String dealDate = (String) params.get("dealDate");
        DwsMonthNavInf dwsMonthNavInf = new DwsMonthNavInf();
        dwsMonthNavInf.setDealDate(dealDate);
        dwsMonthNavInfService.deleteDwsMonthNavInf(dwsMonthNavInf);
        log.info("删除 dws_month_nav_inf 表数据，日期为：{}", dealDate);
        try {
            message = dwsMonthNavInfService.importDwsMonthNavInf(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
