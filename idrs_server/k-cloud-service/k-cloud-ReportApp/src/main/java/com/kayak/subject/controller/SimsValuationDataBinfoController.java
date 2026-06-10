package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.service.SimsValuationDataBInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class SimsValuationDataBinfoController extends BaseController {

    @Autowired
    private SimsValuationDataBInfoService simsValuationDataBInfoService;

    @RequestMapping(value = "/simsValuationDataBInfoImport.json", produces = {"application/json;charset=UTF-8"})
    public String simsValuationDataBInfoImport(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        try {
            // 插入excl数据
            message = simsValuationDataBInfoService.importSimsValuationDataBInfo(file, params);
            // 记录插入日志
            simsValuationDataBInfoService.createImportLog((String) params.get("inputDate"));
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }
}
