package com.kayak.report.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.report.service.ReportSqlResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.util.Map;
import java.util.Set;

@RestController
public class ReportSqlResultAction extends BaseController {

    @Autowired
    private ReportSqlResultService reportSqlResultService;

    @ResponseBody
    @RequestMapping(value = "/getResultData.json")
    public String getResultData() {
        log.info("访问地址：/getResultData.json");
        try {
            log.info("收到请求/getResultData.json");
            Map<String, Object> params = RequestSupport.getParameters();
            Set<String> keys = params.keySet();
            for (String k : keys) {
                params.put(k, URLDecoder.decode(URLDecoder.decode(params.get(k) == null ? "" : params.get(k).toString(), "UTF-8"), "UTF-8"));
            }
            JSONObject json = reportSqlResultService.getRes(params);
            return json.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return updateFailure("服务器异常，请稍后尝试");
        }
    }
}
