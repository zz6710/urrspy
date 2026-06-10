package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.service.DwsAstDebPbnkDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DwsAstDebPbnkDtlController extends BaseController {

    @Autowired
    private DwsAstDebPbnkDtlService service;
    /**
     * 导入资产负债剩余期限明细表
     * @param file 文件对象
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadDwsAstDebPbnkDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstDebPbnkDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String dealDate = (String) params.get("dealDate");
        String message = "";
        service.deleteDwsAstDebPbnkDtl(dealDate); //删除数据
        log.info("删除 dws_ast_deb_pbnk_dtl 表数据，日期为：{}", dealDate);
        try {
            message = service.importDwsAstDebPbnkDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }
}
