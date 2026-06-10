package com.kayak.rpt.rhzy.controller;

import com.kayak.core.action.BaseController;
import com.kayak.rpt.rhzy.component.ZyExcelComponent;
import com.kayak.rpt.rhzy.service.InterbankDepositAmountInfoService;
import com.kayak.rpt.rhzy.service.InterbankDepositInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/rhzy")
public class RHZYImportFileController extends BaseController {
    @Autowired
    private ZyExcelComponent zyExcelComponent;

    @Autowired
    private InterbankDepositInfoService interbankDepositInfoService;

    @Autowired
    private InterbankDepositAmountInfoService interbankDepositAmountInfoService;

    @RequestMapping(value = "/uploadInterbankDepositInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadInterbankDepositInfo(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importInterbankDepositInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadInterbankDepositAmountInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadInterbankDepositAmountInfo(HttpServletRequest request, HttpServletResponse response,
                                             @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importInterbankDepositAmountInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadBondInvestInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadBondInvestInfo(HttpServletRequest request, HttpServletResponse response,
                                                   @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importBondInvestInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadBondInvestAmountInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadBondInvestAmountInfo(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importBondInvestAmountInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadSpvInvestAmountInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadSpvInvestAmountInfo(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importSpvInvestAmountInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }

    @RequestMapping(value = "/uploadSpvInvestInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadSpvInvestInfo(HttpServletRequest request, HttpServletResponse response,
                                       @RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("reportDate", request.getParameter("reportDate").replace("-", ""));

        String errMsg = "";

        try {
            errMsg = zyExcelComponent.importSpvInvestInfo(file,params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }

        return errMsg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(errMsg);
    }
}
