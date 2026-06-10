package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsProdTTRDBef;
import com.kayak.subject.service.DwsProdTTRDBefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DwsProdTTRDBefController extends BaseController {

    @Autowired
    private DwsProdTTRDBefService dwsProdTTRDBefService;

    @RequestMapping(value = "/uploadDwsProdTTRDBef.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsProdTTRDBef(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        try {
            message = dwsProdTTRDBefService.importDwsProdTTRDBef(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }
}
