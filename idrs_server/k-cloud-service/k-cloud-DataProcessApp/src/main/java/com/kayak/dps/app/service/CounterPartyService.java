package com.kayak.dps.app.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.app.dao.CounterPartyModelDao;
import com.kayak.dps.app.model.CounterPartyModel;
import com.kayak.dps.app.utils.SftpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

@Service
@APIDefine(desc = "债券发行人服务", model = CounterPartyModel.class)
public class CounterPartyService extends BaseController {

    @Autowired
    private CounterPartyModelDao counterPartyModelDao;

    @API(desc = "添加债券发行人信息", auth = APIAuth.NO)
    public String addCounterPartyModel(SqlParam<CounterPartyModel> params)  {
        try {
            counterPartyModelDao.addCounterPartyModel(params);
            return RequestSupport.updateReturnJson(true,  "新增成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "查询债券发行人信息", auth = APIAuth.YES)
    public SqlResult<CounterPartyModel> findCounterPartyModel(SqlParam<CounterPartyModel> params) throws Exception {
        return counterPartyModelDao.findCounterPartyModel(params);
    }

    @API(desc = "删除债券发行人信息", auth = APIAuth.NO)
    public String deleteCounterPartyModel(SqlParam<CounterPartyModel> params){
        try {
            counterPartyModelDao.deleteCounterPartyModel(params);
            return RequestSupport.updateReturnJson(true,  "删除成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }

    @API(desc = "补录债券发行人信息", auth = APIAuth.YES)
    public String updateCounterPartyModel(SqlParam<CounterPartyModel> params){
        try {
            counterPartyModelDao.updateCounterPartyModel(params);
            return RequestSupport.updateReturnJson(true,  "修改成功！", null).toString();
        } catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,  e.getMessage(), null).toString();
        }
    }


    @API(desc = "查询机构代码",operation = APIOperation.SELECT, auth = APIAuth.NO)
    public SqlResult<CounterPartyModel> findOrgCdAndNm(SqlParam<CounterPartyModel> params) throws Exception {
        return counterPartyModelDao.findOrgCdAndNm(params);
    }

    @API(desc = "债券发行人信息导入", auth = APIAuth.YES)
    public void CounterPartyImport() throws Exception {}

    @API(desc = "债券发行人信息导出", auth = APIAuth.YES)
    public void CounterPartyExport() throws Exception {}

    @API(desc = "模板下载", auth = APIAuth.NO)
    public void exportFile(HttpServletResponse response, String fileName) {
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
