package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.subject.model.DwsAstMngPlanInfo;
import com.kayak.subject.service.DwsAstMngPlanInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsAstMngPlanInfoController extends BaseController {

    @Autowired
    private DwsAstMngPlanInfoService dwsAstMngPlanInfoService;

    @RequestMapping(value = "/uploadDwsAstMngPlanInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstMngPlanInfo(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        String message;
        // 清空表
        dwsAstMngPlanInfoService.truncateDwsAstMngPlanInfo(new DwsAstMngPlanInfo());
        String suffix = file.getOriginalFilename().split("\\.")[1];
        try {
            if ("xls".equals(suffix) || "xlsx".equals(suffix)) {
                message = dwsAstMngPlanInfoService.importDwsAstMngPlanInfo(file, params);
            } else {
                message = dwsAstMngPlanInfoService.importDwsAstMngPlanInfoCsv(file, params);
            }
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
