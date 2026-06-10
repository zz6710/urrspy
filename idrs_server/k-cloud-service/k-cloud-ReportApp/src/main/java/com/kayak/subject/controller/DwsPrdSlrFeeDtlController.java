package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsPrdSlrFeeDtl;
import com.kayak.subject.service.DwsPrdSlrFeeDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsPrdSlrFeeDtlController extends BaseController {

    @Autowired
    private DwsPrdSlrFeeDtlService dwsPrdSlrFeeDtlService;

    @RequestMapping(value = "/uploadDwsPrdSlrFeeDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsPrdSlrFeeDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String dealDate = (String) params.get("dealDate");
        DwsPrdSlrFeeDtl dwsPrdSlrFeeDtl = new DwsPrdSlrFeeDtl();
        dwsPrdSlrFeeDtl.setDealDate(dealDate);
        dwsPrdSlrFeeDtlService.deleteDwsPrdSlrFeeDtl(dwsPrdSlrFeeDtl);
        try {
            message = dwsPrdSlrFeeDtlService.importDwsPrdSlrFeeDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
