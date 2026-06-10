package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.model.DwsAstPrdItmBalSmr;
import com.kayak.subject.service.DwsAstPrdItmBalSmrNewService;
import com.kayak.subject.service.DwsAstPrdItmBalSmrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DwsAstPrdItmBalSmrController extends BaseController {

    @Autowired
    private DwsAstPrdItmBalSmrService dwsAstPrdItmBalSmrService;
    @Autowired
    private DwsAstPrdItmBalSmrNewService dwsAstPrdItmBalSmrNewService;

    @RequestMapping(value = "/uploadDwsAstPrdItmBalSmr.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstPrdItmBalSmr(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        String actDt = (String) params.get("dealDate");
        DwsAstPrdItmBalSmr dwsAstPrdItmBalSmr = new DwsAstPrdItmBalSmr();
        dwsAstPrdItmBalSmr.setActDt(actDt);
        dwsAstPrdItmBalSmrService.deleteDwsAstPrdItmBalSmr(dwsAstPrdItmBalSmr);
        log.info("删除 dws_ast_prd_itm_bal_smr 表数据，日期为：{}", actDt);
        try {
            message = dwsAstPrdItmBalSmrService.importDwsAstPrdItmBalSmr(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

    // 增量导入
    @RequestMapping(value = "/uploadDwsAstPrdItmBalSmrNew.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstPrdItmBalSmrNew(@RequestParam(value = "file") MultipartFile file) {
//        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params = RequestSupport.getParameters();
        String message;
        try {
            message = dwsAstPrdItmBalSmrNewService.importDwsAstPrdItmBalSmr(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }

}
