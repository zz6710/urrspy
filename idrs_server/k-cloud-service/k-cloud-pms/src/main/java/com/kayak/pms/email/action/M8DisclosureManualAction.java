package com.kayak.pms.email.action;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.connect.utils.FileUtil;
import com.kayak.pms.email.dao.M8DisclosureManualDao;
import com.kayak.pms.email.model.M8DisclosureManual;
import com.kayak.pms.interceptor.MethodAnnotation;
import com.kayak.pms.email.service.M8DisclosureManualService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：手工公告_任务新建
 * @author zhaojie
 * @date 2021-05-19
*/
@RestController
public class M8DisclosureManualAction  extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(M8DisclosureManualAction.class);

    @Autowired
    private M8DisclosureManualService m8DisclosureManualService;
    @MethodAnnotation(desc="手工公告配置任务上传文件")
    @RequestMapping(value = "/M8DisclosureManual/fileUpload.json", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String fileUpload(HttpServletRequest request,@RequestParam(value = "file") MultipartFile file) throws Exception {
        try{
            long size = file.getSize() / 1024 / 1024;
            if (size > 1024) {
                return RequestSupport.updateReturnJson(false, "上传文件不能大于1G", null).toString();
            }
            /*Map<String, Object> parameters = RequestSupport.getParameters();
            String fileName = file.getOriginalFilename().toLowerCase();
            String fileSuffix = StringUtils.lowerCase(fileName.substring(fileName.lastIndexOf(".")));
            if (fileName.endsWith(".sh") || fileName.endsWith(".class") || fileName.endsWith(".jsp")
                    || fileName.endsWith(".html") || fileName.endsWith(".exe") || fileName.endsWith(".sql")) {
                return RequestSupport.updateReturnJson(false, "禁止上传脚本", null).toString();
            }
            Date now = new Date();
            String updDate = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
            String updTime = DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT);
            String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
            String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
            M8DisclosureManual m8DisclosureManual = new M8DisclosureManual();
            m8DisclosureManual.setProdCode((String) parameters.get("prodCode"));
            m8DisclosureManual.setCrtUser((String) parameters.get("crtUser"));
            m8DisclosureManual.setStartDstablishdate((String) parameters.get("startEstablishDate"));
            m8DisclosureManual.setNote((String) parameters.get("note"));
            m8DisclosureManual.setSendmail((String) parameters.get("sendEmail"));
            m8DisclosureManual.setCrtDate((String) parameters.get("crtDate"));
            m8DisclosureManual.setChannel((String) parameters.get("channel"));
            m8DisclosureManual.setTitle((String) parameters.get("title"));
            m8DisclosureManual.setType((String) parameters.get("type"));
            m8DisclosureManual.setSonType((String) parameters.get("sonType"));
            m8DisclosureManual.setFileName(fileName);
            String path = FileUtil.getFileStorePath();
            String fileServerPath = path + "/notice/" + m8DisclosureManual.getType() +"/"+m8DisclosureManual.getProdCode()+ "/" + m8DisclosureManual.getStartDstablishdate();
            FileUtil.fileSaveToLocal(file, fileName,fileServerPath);
            m8DisclosureManual.setFilePath(fileServerPath);
            m8DisclosureManualService.insertManualInfo(m8DisclosureManual);*/
            Map<String, Object> param = RequestSupport.getParameters();
            Map<String,Object> result = new HashMap<String,Object>();
            String fileName = file.getOriginalFilename();
            String path = FileUtil.getFileStorePath()+ File.separator+"xpTemp"+File.separator+DateUtil.getTimestamp14();
            FileUtil.fileSaveToLocal(file, fileName, path);

            result.putAll(param);
            result.put("fileName", fileName);
            result.put("filePath", path+File.separator+fileName);
            log.info("文件上传成功，路径为【{}】",path+File.separator+fileName);
            return RequestSupport.updateReturnJson(true, "成功", result).toString();
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return RequestSupport.updateReturnJson(false, "上传失败", null).toString();
        }
    }

    @RequestMapping(value = "/M8DisclosureManual/download.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;

        try{

            String fileName = params.get("fileName").toString();
            String filePath = params.get("filePath").toString();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //下载
            File file = new File(filePath);
            if (file.isDirectory() || !file.exists()) {
                throw new Exception("文件不存在!");
            }
            fileInputStream = new FileInputStream(file);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            outputStream = new BufferedOutputStream(response.getOutputStream());
            int i = bufferedInputStream.read(buffer);
            while (i != -1) {
                outputStream.write(buffer, 0, buffer.length);
                outputStream.flush();
                i = bufferedInputStream.read(buffer);
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }
}
