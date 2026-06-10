package com.kayak.rpt.rhzg.controller;


import cn.hutool.core.util.ObjectUtil;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.rpt.rhzg.biz.RHZGTableStrEnum;
import com.kayak.rpt.rhzg.biz.ZGFileOperator;
import com.kayak.rpt.rhzj.util.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping(value = "/rhzg")
public class RHZGDownloadController extends BaseController {


    @Autowired
    private ZGFileOperator zgFileOperator;

    @RequestMapping(value = "/download.json")
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        //表名对应字符串
        String tableStr = Objects.toString(params.get("tableStr"));
        //接口代码，对应不同的文件
        String portCode = RHZGTableStrEnum.getProtCodeByTableStr(tableStr);
        String property = System.getProperty("os.name");
        // reportDate（月报）的最后一天
        String lastDayString = "";
        if (StringUtils.isNotEmpty((String) params.get("reportDate"))) {
            lastDayString = params.get("reportDate").toString().replace("-","");
            if(lastDayString.length()<8){
                String reportDate = lastDayString + "01";
                LocalDate yyyyMM = LocalDate.parse(reportDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDate lastDayOfMonth = yyyyMM.with(TemporalAdjusters.lastDayOfMonth());
                lastDayString = lastDayOfMonth.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            }

        }
        System.out.println("lastDay===============================================================" + lastDayString);
        //实际报送日期  适配按月报送，和按产品相关周期
        String workday = Objects.toString(params.get("queryDate") != null ? params.get("queryDate"): lastDayString);
        //金融机构法人标准代码_
        String orgno = CacheUtil.getSystemParam("80000073");
        String path;
        if (property.toLowerCase().startsWith("win")) {
            path = CacheUtil.getSystemParam("80000080012");
        } else {
            path = CacheUtil.getSystemParam("80000080011");
        }
        path += "/";

        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {

            //完整文件名
            String fileName = path + orgno + "_" + tableStr + "_" + workday + ".dat";
            zgFileOperator.creatFile(params,fileName,orgno + "_" + tableStr + "_" + workday + ".dat", portCode);

            //生成压缩文件
            zgFileOperator.creatZipFile(fileName);
            request.setAttribute("doc_name",orgno + "_" + tableStr + "_" + workday + ".zip");
            request.setAttribute("path",fileName.replace(".dat",".zip"));
            FileUtil.download(request, response);
        } catch (IOException e) {
            log.error("下载资管产品数据异常: ", e);

        }
    }




}
