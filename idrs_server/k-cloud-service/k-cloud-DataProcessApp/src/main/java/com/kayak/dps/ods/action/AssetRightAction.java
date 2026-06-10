package com.kayak.dps.ods.action;


import com.kayak.core.action.BaseController;
import com.kayak.dps.ods.service.AssetRightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
public class AssetRightAction extends BaseController {

    @Autowired
    private AssetRightService assetRightService;
    //从业人员登记信息管理
    @RequestMapping(value = "/AssetRight/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileTrPractyRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "AssetRight.xlsx";
        assetRightService.exportFile(response, fileName);
    }

}