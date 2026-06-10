package com.kayak.rpt.rhzg.component;

import com.alibaba.excel.EasyExcel;
import com.kayak.rpt.rhzg.listener.ExcelImportListener;
import com.kayak.rpt.rhzg.model.*;
import com.kayak.rpt.rhzg.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Component
public class ExcelComponent {

    private static final Logger log = LoggerFactory.getLogger(ExcelComponent.class);

    @Autowired
    private ZG01Service zg01Service;
    @Autowired
    private ZG02Service zg02Service;
    @Autowired
    private ZG03Service zg03Service;
    @Autowired
    private ZG04Service zg04Service;
    @Autowired
    private ZG05Service zg05Service;
    @Autowired
    private ZG06Service zg06Service;
    @Autowired
    private ZG07Service zg07Service;
    @Autowired
    private ZG08Service zg08Service;
    @Autowired
    private ZG09Service zg09Service;
    @Autowired
    private ZG10Service zg10Service;
    @Autowired
    private ZG11Service zg11Service;
    @Autowired
    private ZG12Service zg12Service;
    @Autowired
    private ZG13Service zg13Service;




    public String importZg01(MultipartFile file, Map<String, Object> params) throws Exception{

        long startTime = System.currentTimeMillis();
        log.info("导入资管产品基本信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG01> excelImportListener = new ExcelImportListener<ZG01>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg01Service;
            }
        };

        zg01Service.deleteZg01ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG01.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }

        log.info("导入资管产品基本信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg02(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资管产品初始募集信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG02> excelImportListener = new ExcelImportListener<ZG02>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg02Service;
            }
        };

        zg02Service.deleteZg02ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG02.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资管产品初始募集信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg03(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资管产品终止信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG03> excelImportListener = new ExcelImportListener<ZG03>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg03Service;
            }
        };

        zg03Service.deleteZg03ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG03.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资管产品终止信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg04(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资管产品存续期募集信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG04> excelImportListener = new ExcelImportListener<ZG04>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg04Service;
            }
        };

        zg04Service.deleteZg04ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG04.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资管产品存续期募集信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg05(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资管产品资产负债信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG05> excelImportListener = new ExcelImportListener<ZG05>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg05Service;
            }
        };

        zg05Service.deleteZg05ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG05.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资管产品资产负债信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg06(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资产收益权明细信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG06> excelImportListener = new ExcelImportListener<ZG06>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg06Service;
            }
        };

        zg06Service.deleteZg06ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG06.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资产收益权明细信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg07(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入回购和拆借外贷款明细信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG07> excelImportListener = new ExcelImportListener<ZG07>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg07Service;
            }
        };

        zg07Service.deleteZg07ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG07.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入回购和拆借外贷款明细信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg08(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入特定目的载体交易对手明细信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG08> excelImportListener = new ExcelImportListener<ZG08>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg08Service;
            }
        };

        zg08Service.deleteZg08ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG08.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入特定目的载体交易对手明细信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg09(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入资产负债剩余期限信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG09> excelImportListener = new ExcelImportListener<ZG09>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg09Service;
            }
        };

        zg09Service.deleteZg09ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG09.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入资产负债剩余期限信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg10(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入债券等资产配置情况信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG10> excelImportListener = new ExcelImportListener<ZG10>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg10Service;
            }
        };

        zg10Service.deleteZg10ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG10.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入债券等资产配置情况信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZg11(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("导入企业债券分行业和企业规模情况信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG11> excelImportListener = new ExcelImportListener<ZG11>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg11Service;
            }
        };

        zg11Service.deleteZg11ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG11.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("导入企业债券分行业和企业规模情况信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZG12(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("除资产收益权外其他债权明细信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG12> excelImportListener = new ExcelImportListener<ZG12>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg12Service;
            }
        };

        zg12Service.deleteZg12ByDate(params);

        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG12.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("除资产收益权外其他债权明细信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }

    public String importZG13(MultipartFile file, Map<String, Object> params) throws Exception{
        long startTime = System.currentTimeMillis();
        log.info("其他股权明细信息【{}】开始",file.getOriginalFilename());

        ExcelImportListener<ZG13> excelImportListener = new ExcelImportListener<ZG13>(params) {
            @Override
            protected ExcelImportService getImportService() {
                return zg13Service;
            }
        };
        zg13Service.deleteZg13ByDate(params);
        try {
            EasyExcel.read(file.getInputStream())
                    .head(ZG13.class)
                    .registerReadListener(excelImportListener)
                    .sheet()
                    .doRead();
        } catch (Exception e) {
            throw new Exception(excelImportListener.getStopMsg());
        }
        log.info("其他股权明细信息【{}】结束，耗时：{} ms",file.getOriginalFilename(),System.currentTimeMillis() - startTime);
        return excelImportListener.getStopMsg();
    }
}
