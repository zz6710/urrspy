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
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.app.dao.UnderFundInfoDao;
import com.kayak.dps.app.model.UnderFundInfo;
import com.kayak.dps.app.utils.SftpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

@Service
@APIDefine(model = UnderFundInfo.class)
public class UnderFundInfoService {
    protected static final Logger log = LoggerFactory.getLogger(UnderFundInfoService.class);

    @Autowired
    private UnderFundInfoDao underFundInfoDao;

    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<UnderFundInfo> findFundInfos(SqlParam<UnderFundInfo> params) throws Exception {
        params.setMakeSql(false);
        return underFundInfoDao.findFundInfos(params);
    }

    @API(desc = "添加", auth = APIAuth.YES, operation = APIOperation.INSTER)
    public String addFundInfo(SqlParam<UnderFundInfo> params) throws Exception {

        UnderFundInfo m = params.getModel();
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        try {
            m.setCrtDate(date);
            m.setCrtTime(time);
            m.setCrtUser(userid);
            underFundInfoDao.addFundInfo(m);
            return RequestSupport.updateReturnJson(true,"新增成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"新增失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "修改", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String updateFundInfo(SqlParam<UnderFundInfo> params) throws Exception {
        UnderFundInfo m = params.getModel();
        String date = DateUtil.getNowDate();
        String time = DateUtil.getNowTime();
        String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
        try {
            m.setUpdDate(date);
            m.setUpdTime(time);
            m.setUpdUser(userid);
            underFundInfoDao.updateFundInfo(m);
            return RequestSupport.updateReturnJson(true,"信息补录成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"信息补录失败！" + e.getMessage() ,null).toString();
        }
    }

    @API(desc = "删除", auth = APIAuth.YES, operation = APIOperation.DELETE)
    public String deleteFundInfo(SqlParam<UnderFundInfo> params) throws Exception {
        try {
            underFundInfoDao.deleteFundInfo(params.getModel());
            return RequestSupport.updateReturnJson(true,"删除成功",null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false,"删除失败！" + e.getMessage() ,null).toString();
        }
    }
    @API(desc = "查询", auth = APIAuth.NO)
    public SqlResult<UnderFundInfo> findassetManagerCode(SqlParam<UnderFundInfo> params) throws Exception {
        params.setMakeSql(false);
        return underFundInfoDao.findassetManagerCode(params);
    }
    @API(desc = "模板下载", auth = APIAuth.NO)
    public void exportFile(HttpServletResponse response, String fileName) {
        //  Map<String, Object> params = RequestSupport.getParameters();
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        try{
            if (fileName != null) {
                response.setContentType("application/x-octetstream;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                response.addHeader("Access-Control-Expose-Headers","*");
                response.setHeader("filename",fileName);
                response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//                response.addHeader("Access-Control-Expose-Headers","*");
//                response.setHeader("filename",fileName);
//                response.setHeader("content-disposition", "attachment;filename=" +
//                        new String(fileName.getBytes("GB2312"), "ISO8859-1"));


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

//                try {
//                    SftpUtils.getFile(remoteIp, remoteUserName, remoteUserPassword, dealRemotePath, temPath, fileName);
//                } catch (Exception e) {
//                    log.error("获取远端服务器文件失败{}",e);
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
