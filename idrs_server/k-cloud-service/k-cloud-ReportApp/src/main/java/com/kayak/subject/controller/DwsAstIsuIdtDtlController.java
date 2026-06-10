package com.kayak.subject.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.subject.service.DwsAstIsuIdtDtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
public class DwsAstIsuIdtDtlController extends BaseController {

    @Autowired
    private DwsAstIsuIdtDtlService service;

    /**
     * 导入企业债按行业、企业规模统计明细表
     * @param file 文件对象
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/uploadDwsAstIsuIdtDtl.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstIsuIdtDtl(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String dealDate = (String) params.get("dealDate");
        String message = "";
        service.deleteDwsAstIsuIdtDtlByDealDate(dealDate); //删除数据
        log.info("删除 dws_ast_isu_idt_dtl 表数据，日期为：{}", dealDate);
        try {
            message = service.importDwsAstIsuIdtDtl(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }
}
