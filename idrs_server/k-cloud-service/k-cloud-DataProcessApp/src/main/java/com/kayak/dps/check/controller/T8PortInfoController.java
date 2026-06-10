package com.kayak.dps.check.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.FileUtil;
import com.kayak.dps.check.service.T8PortInfoService;
import com.kayak.dps.ods.util.zz.SFtpHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class T8PortInfoController extends BaseController {

    private static Logger logger = LogManager.getLogger(T8PortInfoController.class);

    @Autowired
    private T8PortInfoService t8PortInfoService;

    /**
     * 新增接口管理信息
     * @return
     */
   /* @RequestMapping(value="/addPortInfo.action")
    public String portManageInfoAdd() {
        logger.info("****************开始处理:新增接口管理信息*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            params.put("inputUser", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            t8PortInfoService.addPortInfo(params);
            return RequestSupport.updateReturnJson(true,"新增接口管理信息完成！",null).toString();
        } catch (Exception e) {
            logger.error("新增接口管理信息失败: ", e);
            logger.info("****************新增接口管理信息 处理结束*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }*/

    /**
     * 修改接口管理信息
     * @return
     */
    /*@RequestMapping(value="/updPortInfo.action")
    public String portManageInfoUpd() {
        logger.info("****************开始处理:修改接口管理信息*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            params.put("inputUser", Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid")));
            t8PortInfoService.updPortInfo(params);
            return RequestSupport.updateReturnJson(true,"修改接口管理信息完成！",null).toString();
        } catch (Exception e) {
            logger.error("修改接口管理信息失败: ", e);
            logger.info("****************修改接口管理信息 处理结束*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }*/

    @RequestMapping(value = "/downloadPortFiles.json")
    public String downloadPortFiles(HttpServletResponse response) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        //获取指定接口文件名称
        String deal_date = String.valueOf(params.get("dealDate"));//处理文件日期

        //指定接口类型，查询对应接口所有文件
        List<SqlRow> sqlRes = t8PortInfoService.queryPortManageInfoByPortType(params);
        String remote_path = "";
        String local_path = "";

        for (SqlRow sqlRow : sqlRes) {
            String file_name = sqlRow.getString("port_address").replace("[deal_date]", deal_date);
            remote_path = String.valueOf(params.get("portAddress")).replace("[deal_date]", deal_date);
            local_path = String.valueOf(params.get("localPath")).replace("[deal_date]", deal_date);
            File file = new File(local_path);
            if (!file.isDirectory()) file.mkdirs();
            try {
                //远程服务器下载文件至本地目录
                SFtpHelper.getFile(String.valueOf(params.get("sftpIp")), String.valueOf(params.get("username")), String.valueOf(params.get("password")),
                        remote_path, local_path, file_name,"0","2");
            } catch (Exception e) {
                return RequestSupport.updateReturnJson(false,  "文件下载异常！"+e.getMessage(), null).toString();
            }
        }
        String zipname = deal_date + ".zip";
        String zip_file_path = local_path  + deal_date + ".zip";
        //对本地目标文件夹进行压缩
        File zip_file = new File(zip_file_path);
        if(!zip_file.getParentFile().exists()){
            zip_file.getParentFile().mkdirs();
        }
        if (!zip_file.exists()) {
            zip_file.createNewFile();
        }

        byte[] buffer = new byte[1024];
        File localDir = new File(local_path);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zip_file));
        FileUtil.compressDirectory(localDir, zipOut, deal_date + "/");
        zipOut.close();
        FileUtil.downloadFile(zip_file_path ,zipname ,response);
        FileUtil.deleteFolder(new File(local_path));
        return RequestSupport.updateReturnJson(true,  "下载成功！", null).toString();
    }
}
