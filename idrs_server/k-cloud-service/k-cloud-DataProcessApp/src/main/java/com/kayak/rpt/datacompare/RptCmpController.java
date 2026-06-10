package com.kayak.rpt.datacompare;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class RptCmpController extends BaseController {
    @Autowired
    private RptCmpService rptCmpService;


    @RequestMapping(value = "/reportCompare")
    public @ResponseBody
    String reportCompare(@RequestBody Map params) {
        try {
            String report_date=params.get("report_date").toString();
            String table_name=params.get("table_name").toString();
            rptCmpService.compare(report_date,table_name);
            return RequestSupport.updateReturnJson(true,"报表比对完成！",null).toString();
        } catch (Exception e) {// 获取返回提示的错误
            return updateFailure(e.getMessage());
        }
    }

    @RequestMapping (value = "/investorReportCompare")
    public @ResponseBody
    String investorReportCompare(@RequestBody Map params) {
        try {
            String table_name=params.get("table_name").toString();
            String report_date=params.get("report_date").toString();
            rptCmpService.investorCompare(table_name,report_date);
            return RequestSupport.updateReturnJson(true,"报表比对完成！",null).toString();
        } catch (Exception e) {// 获取返回提示的错误
            return updateFailure(e.getMessage());
        }
    }

    @RequestMapping(value = "/exportCompareFile.json",produces = { "application/json;charset=UTF-8"})
    public void download(HttpServletResponse response) throws Exception{
        Map<String, Object> parameters = RequestSupport.getParameters();

        rptCmpService.download(parameters, response);
    }

}
