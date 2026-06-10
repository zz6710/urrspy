package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.subject.model.DwsCounterPartyInfo;
import com.kayak.subject.service.DwsCounterPartyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsCounterPartyInfoController extends BaseController {

    @Autowired
    private DwsCounterPartyInfoService dwsCounterPartyInfoService;

    @RequestMapping(value = "/uploadDwsCounterPartyInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsCounterPartyInfo(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String actDt = (String) params.get("dealDate");
        DwsCounterPartyInfo dwsCounterPartyInfo = new DwsCounterPartyInfo();
        dwsCounterPartyInfo.setActDt(DateUtil.getLastDayOfMonth(actDt));
        dwsCounterPartyInfoService.deleteDwsCounterPartyInfo(dwsCounterPartyInfo);
        log.info("删除 dws_counter_party_info 表数据，日期为：{}", actDt);
        try {
            message = dwsCounterPartyInfoService.importDwsCounterPartyInfo(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
