package com.kayak.subject.controller;

import com.alibaba.excel.EasyExcel;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.model.ZG06;
import com.kayak.rpt.rhzg.model.ZG12;
import com.kayak.rpt.rhzg.model.ZG13;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.rpt.rhzg.service.ZG06ImportService;
import com.kayak.rpt.rhzg.service.ZG12ImportService;
import com.kayak.rpt.rhzg.service.ZG13ImportService;
import com.kayak.subject.model.DwsAstEquInfo;
import com.kayak.subject.service.DwsAstEquInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@RestController
public class DwsAstEquInfoController extends BaseController {

    @Autowired
    private DwsAstEquInfoService dwsAstEquInfoService;

    @Autowired
    private ZG12ImportService zg12ImportService;

    @Autowired
    private ZG13ImportService zg13ImportService;

    @Autowired
    private ZG06ImportService zg06ImportService;

    @RequestMapping(value = "/uploadDwsAstEquInfo.json", produces = {"application/json;charset=UTF-8"})
    public String uploadDwsAstEquInfo(@RequestParam(value = "file") MultipartFile file) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String dealDate = (String) params.get("dealDate");
        String fileType = (String) params.get("fileType");
        if(fileType.equals("ZG12")){
            return dealZG12Type(file, params, dealDate);
        }else if (fileType.equals("ZG13")){
            return dealZG13Type(file, params, dealDate);
        }else if (fileType.equals("YDDK")){
            return dealDefaultType(file, params, dealDate);
        }else if (fileType.equals("ZG06")){
            return dealZG06Type(file,params,dealDate); //处理ZG06
        }else{
            throw new Exception("无法处理文件类型：" + fileType);
        }
    }

    private String dealZG06Type(MultipartFile file, Map<String, Object> params,String dealDate) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入import_zg06_buffer【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG06> excelImportListener = new ExcelImportListener<ZG06>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg06ImportService;
            }
        };

        zg06ImportService.deleteZG06Buffer(dealDate); //清空zg06临时表
        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG06.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入import_zg06_buffer【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        String msg = excelImportListener.getStopMsg();
        if(msg.isEmpty()){
            zg06ImportService.callTaskDealImportZG06(dealDate); //调用任务处理数据
        }
        return msg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(msg);
    }

    /**
     *  处理ZG12文件导入
     * @param file
     * @param params
     * @param dealDate
     * @return
     * @throws Exception
     */
    private String dealZG12Type(MultipartFile file, Map<String, Object> params,String dealDate) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入import_zg12_buffer【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG12> excelImportListener = new ExcelImportListener<ZG12>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg12ImportService;
            }
        };

        zg12ImportService.deleteZG12Buffer(dealDate); //清空zg12临时表
        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG12.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入import_zg12_buffer【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        String msg = excelImportListener.getStopMsg();
        if(msg.isEmpty()){
            zg12ImportService.callTaskDealImportZG12(dealDate); //调用任务处理数据
        }
        return msg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(msg);
    }

    /**
     * 处理ZG13导入
     * @param file
     * @param params
     * @param dealDate
     * @return
     * @throws Exception
     */
    private String dealZG13Type(MultipartFile file, Map<String, Object> params,String dealDate) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入import_zg13_buffer【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG13> excelImportListener = new ExcelImportListener<ZG13>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg13ImportService;
            }
        };

        zg13ImportService.deleteZG13Buffer(dealDate); //清空zg13临时表
        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG13.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入import_zg13_buffer【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        String msg = excelImportListener.getStopMsg();
        if(msg.isEmpty()){
            zg13ImportService.callTaskDealImportZG13(dealDate); //调用任务处理数据
        }
        return msg.isEmpty() ? updateSuccess("导入成功！") : updateFailure(msg);
    }

    /**
     * 月度贷款明细和收益权明细信息 文件导入处理
     * @param file
     * @param params
     * @param dealDate
     * @return
     * @throws Exception
     */
    private String dealDefaultType(MultipartFile file, Map<String, Object> params,String dealDate) throws Exception{
        DwsAstEquInfo dwsAstEquInfo = new DwsAstEquInfo();
        dwsAstEquInfo.setActDt(DateUtil.getLastDayOfMonth(dealDate));
        dwsAstEquInfoService.deleteDwsAstEquInfo(dwsAstEquInfo);
        String message;
        try {
            message = dwsAstEquInfoService.importDwsAstEquInfo(file, params);
        } catch (Exception e) {
            return updateFailure(e.getMessage());
        }
        return message.isEmpty() ? updateSuccess("导入成功！") : updateFailure(message);
    }
}
