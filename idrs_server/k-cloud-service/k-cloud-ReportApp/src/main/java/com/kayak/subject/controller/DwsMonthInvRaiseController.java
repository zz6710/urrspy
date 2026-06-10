package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsMonthInvRaise;
import com.kayak.subject.service.DwsMonthInvRaiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsMonthInvRaiseController extends BaseController {

    @Autowired
    private DwsMonthInvRaiseService dwsMonthInvRaiseService;

    @RequestMapping(value = "/uploadDwsMonthInvRaise.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsMonthInvRaise(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String dealDate = (String) params.get("dealDate");
        DwsMonthInvRaise dwsMonthInvRaise = new DwsMonthInvRaise();
        dwsMonthInvRaise.setDealDate(dealDate);
        dwsMonthInvRaiseService.deleteDwsMonthInvRaise(dwsMonthInvRaise);
        log.info("删除dws_month_inv_raise表数据，日期为：{}", dealDate);
        try {
            message = dwsMonthInvRaiseService.importDwsMonthInvRaise(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
