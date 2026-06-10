package com.kayak.dps.outLands.action;

import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.dao.AssBondInfoModelDao;
import com.kayak.dps.app.model.AssBondInfoModel;
import com.kayak.dps.app.utils.SftpUtils;
import com.kayak.dps.valtabimp.excel.ExcelParse;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BondInfoAction extends BaseController {


    @Autowired
    private AssBondInfoModelDao assBondInfoModelDao;

    /**
     * 债券补录信息导入
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/bondInfoUpload.json",produces = { "application/json;charset=UTF-8"})
    public String bondInfoUpload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, Object> params = new HashMap<>();
            for (int i = 0; i <2 ; i++) {
                //处理sheet页的数据
                List<AssBondInfoModel> assBondInfoModelList = ExcelParse.readExcelData(file.getInputStream(), i, 1, 1, AssBondInfoModel.class, true, null);
                // 移除第一标题行
                assBondInfoModelList.remove(0);
                assBondInfoModelDao.importAssBondInfo(assBondInfoModelList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return updateFailure("导入失败！ "+e.getMessage());
        }
        return updateSuccess("导入成功！");
    }

    private String reverseToString(String actualProdTerDate) {
        BigDecimal bd = new BigDecimal(actualProdTerDate);
        return bd.toPlainString();
    }



    /**
     * 债券信息下载模板
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/downloadBondInfoTemp.json", produces = {"application/json;charset=UTF-8"})
    public void downloadScheduleTemp(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        String fileName = "BondInfo.xlsx";
        //获取公共模板文件路径

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
                    SftpUtils.getFile(remoteIp, remoteUserName, remoteUserPassword, dealRemotePath, temPath, fileName);
                } catch (Exception e) {
                    log.error("获取远端服务器文件失败{}",e);
                }
//                File localPath = new File(temPath);
//                if (!localPath.exists() && !localPath.isDirectory()) {
//                    localPath.mkdirs();
//                }
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
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
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