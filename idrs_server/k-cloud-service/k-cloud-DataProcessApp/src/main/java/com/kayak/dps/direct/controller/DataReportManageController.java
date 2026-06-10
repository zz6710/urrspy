package com.kayak.dps.direct.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.service.DirectAnalysisService;
import com.kayak.graphql.model.FetcherData;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@RestController
public class DataReportManageController extends BaseController {

    @Autowired
    private DirectAnalysisService directAnalysisService;

    @RequestMapping(value = "/dataReportManage/send_download.json")
    public void send_download(HttpServletResponse response, @RequestParam(value = "origfilename") String origfilename,@RequestParam(value = "workdate") String workdate) throws Exception {
        if (Tools.isEmpty(origfilename)) {
            log.error("下载文件异常，缺少origfilename参数.");
            return;
        }
        if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
            log.error("检测到sql注入异常");
            return;
        }
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
        String localPath = SysUtil.getSystemParamsByParaid("app_localpath")+"/zzfile/sendComplete/"+workdate+"/"+origfilename;
        //String localPath = remotePath+"/"+origfilename;;
        remotePath = remotePath+"/zzfile/sendComplete/"+workdate+"/"+origfilename;
        File file = new File(localPath);
        // 本地不存在，从远程下载
        // 本地不存在，从远程下载
        if (!file.exists() && Tools.isNotEmpty(remotePath)) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
           FileTransfer transfer = FileTransferHelpler.getTransfer();
           transfer.downloadFileAndDisconnect(remotePath, localPath);
        }
        file = new File(localPath);
        if (file.exists()) {
            try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
                OutputStream outputStream = response.getOutputStream()) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                response.setContentLength((int) file.length());
                IOUtils.copy(input, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("file not exits.");
        }
    }

    @RequestMapping(value = "/dataReportManage/receive_download.json")
    public void receive_download(HttpServletResponse response, @RequestParam(value = "origfilename") String origfilename,@RequestParam(value = "workdate") String workdate) throws Exception {
        if (Tools.isEmpty(origfilename)) {
            log.error("下载文件异常，缺少origfilename参数.");
            return;
        }
        if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
            log.error("检测到sql注入异常");
            return;
        }
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
        String localPath = SysUtil.getSystemParamsByParaid("app_localpath")+"/zzfile/receive/"+workdate+"/"+origfilename;
        //String localPath = remotePath+"/"+origfilename;;
        remotePath = remotePath+"/zzfile/receive/"+workdate+"/"+origfilename;
        File file = new File(localPath);
        // 本地不存在，从远程下载
        // 本地不存在，从远程下载
        if (!file.exists() && Tools.isNotEmpty(remotePath)) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            transfer.downloadFileAndDisconnect(remotePath, localPath);
        }
        file = new File(localPath);
        if (file.exists()) {
            try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
                OutputStream outputStream = response.getOutputStream()) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/octet-stream;charset=utf-8");
                response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
                response.setContentLength((int) file.length());
                IOUtils.copy(input, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new RuntimeException("file not exits.");
        }
    }

    @RequestMapping(value = "/dataReportManage/directReportInfo.json")
    public String directReportInfo(HttpServletResponse response, @RequestParam(value = "origfilename") String origfilename,@RequestParam(value = "workdate") String workdate) throws Exception {
        if (Tools.isEmpty(origfilename)) {
            log.error("下载文件异常，缺少origfilename参数.");
            return updateFailure("下载文件异常，缺少origfilename参数.");
        }
        if (!RequestSupport.getCanCodeMore(RequestSupport.getParameters())) {
            log.error("检测到sql注入异常");
            return updateFailure("检测到sql注入异常");
        }
        Map<String,Object> params = new HashMap<>();
        origfilename =origfilename.replace(".zip",".txt");
        String remotePath = SysUtil.getSystemParamsByParaid("90000051312");
        String localPath = SysUtil.getSystemParamsByParaid("app_localpath")+"/zzfile/receiveInfo/"+workdate+"/"+origfilename;
        remotePath = remotePath+"/zzfile/receiveInfo/"+workdate+"/"+origfilename;
        File file = new File(localPath);
        if (Tools.isNotEmpty(remotePath)) {
            if(!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileTransfer transfer = FileTransferHelpler.getTransfer();
            try{
                transfer.downloadFileAndDisconnect(remotePath, localPath);
            }catch (Exception e){
                return updateFailure("反馈报文不存在！");
            }
        }
        file = new File(localPath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Files.newInputStream(Paths.get(localPath)), "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(line.startsWith("{") && line.endsWith("}")){
                        String content=line.substring(1,line.length()-1);
                        if(content.length()>0){
                            params.put(content.split("=")[0],content.split("=")[1]);
                            directAnalysisService.analysisDirectResult(params);
                        }
                        log.info("反馈报文解析完成！",content);
                    }
                }
            } catch (Exception e) {
                log.error("反馈报文文件不存在或解析报文异常", localPath);
                return updateFailure("反馈报文文件不存在或解析报文异常！");
            }
        } else {
            return updateFailure("文件不存在！");
        }
        return updateSuccess("处理完成");
    }
}
