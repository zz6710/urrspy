package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.subject.service.BaseSubjectMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BaseSubjectMapController extends BaseController {

    @Autowired
    private BaseSubjectMapService baseSubjectMapService;

    @RequestMapping(value = "/uploadBaseSubjectMap.json", produces = {"application/json;charset=UTF-8"})
    public String uploadBaseSubjectMap(@RequestParam(value = "file") MultipartFile file) {
        Map<String, Object> params = new HashMap<>();
        String message;
        try {
            message = baseSubjectMapService.importBaseSubjectMap(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
