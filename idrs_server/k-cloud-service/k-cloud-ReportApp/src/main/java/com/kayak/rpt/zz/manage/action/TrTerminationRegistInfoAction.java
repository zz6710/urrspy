package com.kayak.rpt.zz.manage.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.SysUtil;
import com.kayak.rpt.excel.SftpUtils;
import com.kayak.rpt.zz.manage.service.ExcelToMapService;
import com.kayak.rpt.zz.manage.service.TrTerminationRegistInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

@RestController
public class TrTerminationRegistInfoAction extends BaseController {

    @Autowired
    private TrTerminationRegistInfoService trTerminationRegistInfoService;

    @Autowired
    private ExcelToMapService excelToMapService;



    //从业人员登记信息管理
    @RequestMapping(value = "/chinaBondSubmit/TrPractyRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileTrPractyRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "TrPractyRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //产品申报登记信息管理
    @RequestMapping(value = "/chinaBondSubmit/ProdRegistFilingInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileProdRegistFilingInfo(String fileName, HttpServletResponse response) {
        fileName = "ProdRegistFilingInfo.xlsx";
        exportFile(response, fileName);
    }

    //产品发行登记信息管理
    @RequestMapping(value = "/chinaBondSubmit/ProdIssuanceRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileProdIssuanceRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "ProdIssuanceRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //募集总量登记管理
    @RequestMapping(value = "/chinaBondSubmit/InitialSubRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileInitialSubRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "InitialSubRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //产品存续期登记管理
    @RequestMapping(value = "/chinaBondSubmit/SubseqSubscrRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileSubseqSubscrRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "SubseqSubscrRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //资产负债要素登记管理
    @RequestMapping(value = "/chinaBondSubmit/AssetDebtRegisterInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileAssetDebtRegisterInfo(String fileName, HttpServletResponse response) {
        fileName = "AssetDebtRegisterInfo.xlsx";
        exportFile(response, fileName);
    }

    //交易信息登记管理
    @RequestMapping(value = "/chinaBondSubmit/ProdTransRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileProdTransRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "ProdTransRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //估值信息登记管理
    @RequestMapping(value = "/chinaBondSubmit/AppraiseRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileAppraiseRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "AppraiseRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //资产持仓登记管理
    @RequestMapping(value = "/chinaBondSubmit/AssetRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileAssetRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "AssetRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //产品状态管理
    @RequestMapping(value = "/chinaBondSubmit/ProdStateRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileProdStateRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "ProdStateRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //净值信息登记管理
    @RequestMapping(value = "/chinaBondSubmit/SubPrdNav/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileSubPrdNavInfo(String fileName, HttpServletResponse response) {
        fileName = "SubPrdNavInfo.xlsx";
        exportFile(response, fileName);
    }

    //底层资产持仓管理
    @RequestMapping(value = "/chinaBondSubmit/UnderAssetRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFileUnderAssetRegistInfo(String fileName, HttpServletResponse response) {
        fileName = "UnderAssetRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    //产品终止登记管理
    @RequestMapping(value = "/chinaBondSubmit/TrTerminationRegistInfo/comn-download.json",produces = { "application/json;charset=UTF-8"})
    public void exportFile(String fileName, HttpServletResponse response) {
        fileName = "TrTerminationRegistInfo.xlsx";
        exportFile(response, fileName);
    }

    private void exportFile(HttpServletResponse response, String fileName) {
        //  Map<String, Object> params = RequestSupport.getParameters();
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try{
            if (fileName != null) {
                response.addHeader("Access-Control-Expose-Headers","*");
                response.setHeader("filename",fileName);
                response.setHeader("content-disposition", "attachment;filename=" +
                        new String(fileName.getBytes("GB2312"), "ISO8859-1"));


                /**指定下载模板文件的文件服务器的ip、用户名、用户密码等*/
                //远端服务器ip地址
                String remoteIp = SysUtil.getSystemParamsByParaid("70000010004");
                //远端服务器用户名
                String remoteUserName = SysUtil.getSystemParamsByParaid("70000010005");
                //远端服务器密码
                String remoteUserPassword = SysUtil.getSystemParamsByParaid("70000010006");
                //远端服务器文档模板sftp存储路径
                String remotePath = SysUtil.getSystemParamsByParaid("90000010000");
                String dealRemotePath = remotePath;
                //本机或本地服务器模板文档存储路径
                //本机或本地服务器模板文档存储路径
                String temPath =  System.getProperty("os.name").toLowerCase().startsWith("win")
                        ?SysUtil.getSystemParamsByParaid("90000010001")//本机存放根路径
                        :SysUtil.getSystemParamsByParaid("90000010002");//服务器存放根路径
                File dir = new File(temPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                //文件名
                /**操作远端下载文件存储本地路径*/

                try {
                    //SftpUtils.getFile(remoteIp, remoteUserName, remoteUserPassword, dealRemotePath, temPath, fileName);
                } catch (Exception e) {
                    log.error("获取远端服务器文件失败{}",e);
                }
                //从本地路径输出前端head中响应IO
                String pathFile = temPath + fileName;
                File file = new File(pathFile);
                fileInputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream);
                outputStream = new BufferedOutputStream(response.getOutputStream());
                int i = bufferedInputStream.read(buffer);
                while (i != -1) {
                    outputStream.write(buffer, 0, buffer.length);
                    outputStream.flush();
                    i = bufferedInputStream.read(buffer);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e2) {
                log.error("io关闭异常[{}]", e2);
            }
        }
    }
}
