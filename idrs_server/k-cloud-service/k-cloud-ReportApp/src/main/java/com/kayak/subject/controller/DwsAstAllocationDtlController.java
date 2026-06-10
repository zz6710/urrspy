package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.service.DwsAstAllocationDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DwsAstAllocationDtlController extends BaseController {

    @Autowired
    private DwsAstAllocationDtlService service;

    /**
     * 资产配置情况明细表excel文件导入
     * @param file 文件对象
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadDwsAstAllocationDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstAllocationDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String dealDate = (String) params.get("dealDate");
        String message = "";
        service.deleteDwsAstAllocationDtl(dealDate); //删除数据
        log.info("删除 dws_ast_allocation_dtl 表数据，日期为：{}", dealDate);
        try {
            message = service.importDwsAstAllocationDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }


}
