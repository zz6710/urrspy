package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsDailyPrdDtl;
import com.kayak.subject.service.DwsDailyPrdDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsDailyPrdDtlController extends BaseController {

    @Autowired
    private DwsDailyPrdDtlService dwsDailyPrdDtlService;

    @RequestMapping(value = "/uploadDwsDailyPrdDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsDailyPrdDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String dealDate = (String) params.get("dealDate");
        DwsDailyPrdDtl dwsDailyPrdDtl = new DwsDailyPrdDtl();
        dwsDailyPrdDtl.setDealDate(dealDate);
        dwsDailyPrdDtlService.deleteDwsDailyPrdDtl(dwsDailyPrdDtl);
        try {
            message = dwsDailyPrdDtlService.importDwsDailyPrdDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
