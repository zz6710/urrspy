package com.kayak.rpt.rhzy.component;

import com.alibaba.excel.EasyExcel;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.rpt.rhzy.model.*;
import com.kayak.rpt.rhzy.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class ZyExcelComponent {
    private static final Logger log = LoggerFactory.getLogger(ZyExcelComponent.class);

    @Autowired
    private InterbankDepositInfoService interbankDepositInfoService;
    @Autowired
    private InterbankDepositAmountInfoService interbankDepositAmountInfoService;
    @Autowired
    private BondInvestInfoService bondInvestInfoService;
    @Autowired
    private BondInvestAmountInfoService bondInvestAmountInfoService;
    @Autowired
    private SpvInvestAmountInfoService spvInvestAmountInfoService;
    @Autowired
    private SpvInvestInfoService spvInvestInfoService;

    public String importInterbankDepositInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入资管产品基本信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<InterbankDepositInfo> excelImportListener = new ExcelImportListener<InterbankDepositInfo>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return interbankDepositInfoService;
            }
        };

        interbankDepositInfoService.deleteInterbankDepositInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(InterbankDepositInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入资管产品基本信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importInterbankDepositAmountInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入资管产品基本信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<InterbankDepositAmountInfo> excelImportListener = new ExcelImportListener<InterbankDepositAmountInfo>(params) {
            @Override
            protected ExcelImportService getImportService() { return interbankDepositAmountInfoService; }
        };

        interbankDepositAmountInfoService.deleteInterbankDepositAmountInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(InterbankDepositAmountInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入资管产品基本信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importBondInvestInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入资管产品基本信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<BondInvestInfo> excelImportListener = new ExcelImportListener<BondInvestInfo>(params) {
            @Override
            protected ExcelImportService getImportService() { return bondInvestInfoService; }
        };

        bondInvestInfoService.deleteBondInvestInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(BondInvestInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入资管产品基本信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importBondInvestAmountInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入资管产品基本信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<BondInvestAmountInfo> excelImportListener = new ExcelImportListener<BondInvestAmountInfo>(params) {
            @Override
            protected ExcelImportService getImportService() { return bondInvestAmountInfoService; }
        };

        bondInvestAmountInfoService.deleteBondInvestAmountInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(BondInvestAmountInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入资管产品基本信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importSpvInvestAmountInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入特定目的载体投资发生额信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<SpvInvestAmountInfo> excelImportListener = new ExcelImportListener<SpvInvestAmountInfo>(params) {
            @Override
            protected ExcelImportService getImportService() { return spvInvestAmountInfoService; }
        };

        spvInvestAmountInfoService.deleteSpvInvestAmountInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(SpvInvestAmountInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入特定目的载体投资发生额信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importSpvInvestInfo(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入存量特定目的载体投资信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<SpvInvestInfo> excelImportListener = new ExcelImportListener<SpvInvestInfo>(params) {
            @Override
            protected ExcelImportService getImportService() { return spvInvestInfoService; }
        };

        spvInvestInfoService.deleteSpvInvestInfoByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(SpvInvestInfo.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入存量特定目的载体投资信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }
}
