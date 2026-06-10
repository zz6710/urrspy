package com.kayak.dps.direct.controller;

import com.kayak.dps.direct.service.DirectAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RestController
@RequestMapping(value = "/direct")
public class DirectAnalysisController {

    @Autowired
    private DirectAnalysisService directAnalysisService;
    @RequestMapping(value = "/analysisResultInfo.json")
    public void analysisWpResult(@RequestBody Map<String, Object> params) throws Exception {
        //个人养老金反馈报文处理
        //directAnalysisService.analysisWpResult(params);
        //直连报送反馈报文处理
        directAnalysisService.analysisDirectResult(params);

    }
}
