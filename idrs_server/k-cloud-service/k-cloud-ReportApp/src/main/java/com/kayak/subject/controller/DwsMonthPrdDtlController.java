package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsMonthPrdDtl;
import com.kayak.subject.service.DwsMonthPrdDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsMonthPrdDtlController extends BaseController {

    @Autowired
    private DwsMonthPrdDtlService dwsMonthPrdDtlService;

    @RequestMapping(value = "/uploadDwsMonthPrdDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsMonthPrdDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String dealDate = (String) params.get("dealDate");
        DwsMonthPrdDtl dwsMonthPrdDtl = new DwsMonthPrdDtl();
        dwsMonthPrdDtl.setDealDate(dealDate);
        dwsMonthPrdDtlService.deleteDwsMonthPrdDtl(dwsMonthPrdDtl);
        log.info("删除 dws_month_prd_dtl 表数据，日期为：{}", dealDate);
        try {
            message = dwsMonthPrdDtlService.importDwsMonthPrdDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
