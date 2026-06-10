package com.kayak.dps.app.action;


import com.kayak.core.action.BaseController;
import com.kayak.dps.app.service.UnderFundInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class UnderFundInfoController extends BaseController {

    @Autowired
    private UnderFundInfoService underFundInfoService;
    //从业人员登记信息管理
    @RequestMapping(value = "/UnderFundInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileTrPractyRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "UnderFundInfo.xlsx";
        underFundInfoService.exportFile(response, fileName);
    }

}