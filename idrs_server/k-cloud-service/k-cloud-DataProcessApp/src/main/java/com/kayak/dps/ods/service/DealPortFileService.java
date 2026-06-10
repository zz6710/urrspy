package com.kayak.dps.ods.service;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.util.DBFFileUtil;
import com.kayak.dps.ods.util.TxtFileUtil;
import com.kayak.dps.pub.WriteCallback;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
public class DealPortFileService {

    @Resource(name = "dealValuePortService")
    private DealValuePortService dealValuePortService;

    @Autowired
    private JCConfigService jcConfigService;

    /**
     * 处理接口信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public void dealAllPortInfo(Map<String, Object> params) throws Exception {
        List<Map<String, Object>> portList = null;
        String port_dir = params.get("portDir").toString();
        //按天处理
        if(StringUtils.equals("1",params.get("dealType").toString())){
            if(StringUtils.isEmpty(params.get("dealDate").toString()))
                throw new RuntimeException("数据日期输入不正确！");
            portList = dealValuePortService.getAllPortInfo(params);
            dealAllPort(portList, port_dir);
        }else{//按日期区间处理
            if(StringUtils.isEmpty(params.get("dealDates[0]").toString()) || StringUtils.isEmpty(params.get("dealDates[1]").toString()))
                throw new RuntimeException("数据区间输入不正确！");
            String startDate = params.get("dealDates[0]").toString();
            String endDate = params.get("dealDates[1]").toString();
            List<SqlRow> dealDateList = dealValuePortService.getDealDateList(startDate, endDate);
            for (SqlRow sqlRow :dealDateList) {
                try {
                    params.put("dealDate",sqlRow.getString("dealDate").replace("-",""));
                    portList = dealValuePortService.getAllPortInfo(params);
                    dealAllPort(portList, port_dir);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    /**
     * 处理接口信息 按天
     *
     * @param
     * @return
     * @throws Exception
     */
    private void dealAllPort(List<Map<String, Object>> portList, String port_dir) throws Exception{
        for (Map<String, Object> portInfo : portList) {
            if("1".equals(port_dir)){
                dealSendFile(portInfo);
            }else{//信披目前只有接收解析文件
                dealResvFile(portInfo);
            }
        }
    }

    /**
     * 信批贴源层数据处理方法入口
     * @return
     */
    public void dealResvFile(Map<String, Object> params) throws Exception {
        try {
            // 参数校验
            if (!StringUtils.equals("2", (String) params.get("port_dir"))) {
                throw new RuntimeException("文件接口方向不对");
            }
            if (StringUtils.isBlank((String) params.get("port_address"))) {
                throw new RuntimeException("文件路径不能为空");
            }
            Map<String, Object> configMap = null;
            // 文件名称、路径处理，替换占位符
            // 获取参数信息
            configMap = jcConfigService.getConfigInfo(this.transferPortTypeDict((String) params.get("port_type")));

            String resv = "S3";
            String value = "";
            Object resvObj = configMap.get("RESV");
            if (ObjectUtil.isNotEmpty(resvObj)) {
                resv = String.valueOf(resvObj);
                value = "S3".equals(resv) ? "" : "_" + resv;
            }

            // 获取参数
            String fileName = this.fileNamePretreatment((String) params.get(Constants.PORT_FILE_NAME), params);
            String localPath = this.fileNamePretreatment((String) configMap.get(Constants.LOCAL_PATH + value), params);
            //String skipNoFile = this.fileNamePretreatment((String) params.get(Constants.PORT_SKIP_FILE), params);
            //String userName = AESUtils.AESDecrypted((String)configMap.get(Constants.USERNAMES));
            //String passWord = AESUtils.AESDecrypted((String)configMap.get(Constants.PASSWORD));
            int lastDotIdex = fileName.lastIndexOf(".");
            //String okFileName = fileName.substring(0,lastDotIdex) + ".ok";

            File file = new File(localPath);
            file.mkdirs();

            // 记录文件流水
            dealValuePortService.createFileLog(params, localPath + fileName);

            // 判断是否远程下载文件
            if (StringUtils.equals(Constants.Y, (String) configMap.get(Constants.IS_DOWNLOAD + value))) {
                /**20240925特殊处理CISP产品为前一个或后一个自然日时，将远程获取文件路径也同等替换*/
                String remotePathDir = (String) configMap.get(Constants.REMOTE_PATH + value);
                if(((String) params.get(Constants.PORT_FILE_NAME)).contains("[last_date]")) {
                    remotePathDir = remotePathDir.replace("[deal_date]", "[last_date]");
                } else if (((String) params.get(Constants.PORT_FILE_NAME)).contains("[next_date]")) {
                    remotePathDir = remotePathDir.replace("[deal_date]", "[next_date]");
                }

                String remotePath = this.fileNamePretreatment(remotePathDir, params);
                /*if (StringUtils.equals(Constants.PORT_TYPE_3, (String) params.get("port_type"))
                        || StringUtils.equals(Constants.PORT_TYPE_4, (String) params.get("port_type"))
                        || StringUtils.equals(Constants.PORT_TYPE_5, (String) params.get("port_type"))) {//检查是否有.ok文件，估值系统和投资交易除外
                    SFtpHelper.getFile((String)configMap.get(Constants.SFTP_IP), userName,
                            passWord, remotePath, localPath, okFileName, skipNoFile, (String) params.get("port_dir"));
                }*/
                if(StringUtils.equals(Constants.Y, (String) configMap.get(Constants.IS_FULL_MATCHING + value))){

                    FileTransferConfig config = new FileTransferConfig();
                    config.setProtocol(resv);
                    config.setAmazonEndpointUrl((String) configMap.get("ENDPOINT_URL" + value));
                    config.setAmazonAwsAccessKey((String) configMap.get("ACCESS_KEY" + value));
                    config.setAmazonAwsSecretKey((String) configMap.get("SECRET_KEY" + value));
                    config.setAmazonAwsBucketName((String) configMap.get("BUCKET_NAME" + value));

                    FileTransfer transfer = FileTransferHelpler.getTransfer(config);
                    //判断文件是否需要等待，文档不存在就等待一段时间再检测再检测
                    if("1".equals(params.get("wait_flag"))){
                        for(int i=0;i<Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051213"));i++){//超过最大尝试次数就退出
                            if(false==transfer.isFileExists(remotePath + fileName)){//没找到文件就继续等待
                                int wait_time=Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051214"));
                                log.info("开始等待{}秒,文件名:" + fileName, wait_time);
                                Thread.sleep(wait_time*1000);
                            }else{
                                break;
                            }
                        }
                    }

                    transfer.downloadFileAndDisconnect(remotePath + fileName,localPath + fileName);
                }
            }

            params.put("fileName", localPath + fileName);

            // 文件明细批量入库
            dealValuePortService.batchSave(params);

            /*删除下载文件*/
            File delete_File = new File(localPath + fileName);
            delete_File.delete();
            log.info("**************** 回传文件解析成功 *********************");

        } catch (Exception e) {
            log.error(" 文件解析入库失败: ", e);
            /** 更新文件流水-失败 */
            try {
                dealValuePortService.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_02, "处理失败:" + e.getMessage(), 0);
            } catch (Exception e1) {
                log.error(" 文件流水更新失败: ", e);
            }
            log.info("**************** 回传文件解析失败 *********************");
            throw e;
        }
    }

    /**
     * 文件生成与发送接口（对接其他系统）
     *
     * @return
     */
    public void dealSendFile(Map<String, Object> params) throws Exception {
        log.info("****************文件生成与发送接口 Start*********************");
        try {
            // 参数校验
            if (!StringUtils.equals("1", (String) params.get("port_dir"))) {
                throw new RuntimeException("文件接口方向不对");
            }
            if (StringUtils.isBlank((String) params.get("port_address"))) {
                throw new RuntimeException("文件路径不能为空");
            }

            // 根据接口类型获取参数信息
            String configType;
            if (StringUtils.equals(Constants.PORT_TYPE_1, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_GZ;
            } else if (StringUtils.equals(Constants.PORT_TYPE_2, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_JY;
            } else if (StringUtils.equals(Constants.PORT_TYPE_3, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_TA;
            } else if (StringUtils.equals(Constants.PORT_TYPE_4, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_WIND;
            } else if (StringUtils.equals(Constants.PORT_TYPE_5, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_ZG;
            } else if (StringUtils.equals(Constants.PORT_TYPE_7, (String) params.get("port_type"))){
                configType = Constants.CONFIG_TYPE_CPTS;//产品管理系统推送
            } else if (StringUtils.equals(Constants.PORT_TYPE_9, (String) params.get("port_type"))) {
                configType = Constants.CONFIG_TYPE_SJZT_SEND;
            } else if (StringUtils.equals(Constants.PORT_TYPE_10, (String) params.get("port_type"))) {
                configType = Constants.CONFIG_TYPE_CISP_SEND;
                params.put("deal_time", DateUtil.getNowDate() + DateUtil.getNowTime());
                String lastMonthEndDay = DateUtils.getLastDayOfLastMonth(DateUtil.getNowDate());
                params.put("report_date",lastMonthEndDay);//数据日期
            }else if (StringUtils.equals(Constants.PORT_TYPE_11, (String) params.get("port_type"))) {
                configType = Constants.CONFIG_TYPE_WWGZ;
            }else {
                throw new RuntimeException("不支持的接口类型");
            }
            // 获取配置信息
            Map<String, Object> configMap = jcConfigService.getConfigInfo(configType);

            // 上传文件S3或者华为OBS存储，支持双线存储
            Object sendObj = configMap.get("SEND");
            if (ObjectUtil.isEmpty(sendObj)) {
                uploadFileToObs(params, configMap, "S3");
            } else {
                String sendType = String.valueOf(sendObj);
                String[] sends = sendType.split(",");
                for (String send : sends) {
                    Map<String, Object> params1 = new HashMap<>();
                    params1.putAll(params);
                    uploadFileToObs(params1, configMap, send);
                }
            }

            log.info("****************接口发送 end*********************");
        } catch (Exception e) {
            log.error(" 文件生成失败: ", e);
            /** 更新文件流水-失败 */
            try {
                dealValuePortService.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_02, "处理失败:" + e.getMessage(), 0);
            } catch (Exception e1) {
                log.error(" 文件流水更新失败: ", e);
                throw e;
            }
            log.info("****************接口发送 end*********************");
        }
    }

    /**
     * 文件上传至S3或者华为OBS存储
     *
     * @param params
     * @param configMap
     * @return
     */
    public void uploadFileToObs(Map<String, Object> params, Map<String, Object> configMap, String send) throws Exception {
        // 根据存储系统判断获取配置
        String value = StringUtils.equals("S3", send) ? "" : "_" + send;

        // 文件名称、路径处理，替换占位符
        String fileName = this.fileNamePretreatment((String) params.get("port_address"), params);
        String localPath = this.fileNamePretreatment((String) configMap.get(Constants.LOCAL_PATH + value), params);

        // 本地路径生成
        File file = new File(localPath);
        file.mkdirs();

        // 记录文件流水
        dealValuePortService.createFileLog(params, localPath + fileName);
        Integer count=0;
        // 文件生成，判断是DBF文件还是TXT文件
        if (StringUtils.equals(Constants.FILE_TYPE_DBF, (String) params.get("file_type"))) {
            DBFFileUtil.write(dealValuePortService.getPortField(params), dealValuePortService.queryPortContent(params), localPath + fileName);
        } else if (StringUtils.equals(Constants.FILE_TYPE_TXT, (String) params.get("file_type"))) {
            params.put("last_id", 0);
            count = dealValuePortService.queryPortContentCount(params);
            WriteCallback<Object[]> writeCallback = obj -> {
                // 每次返回1000条
                return dealValuePortService.queryPortContent(params);
            };
            TxtFileUtil.writeMore(dealValuePortService.queryFieldList(params), writeCallback, localPath + fileName, params, count);
        } else {
            throw new RuntimeException("不支持的文件类型");
        }
        // FTP文件上传S3
        FileTransferConfig config = new FileTransferConfig();
        config.setProtocol(send);
        config.setAmazonEndpointUrl((String) configMap.get("ENDPOINT_URL" + value));
        config.setAmazonAwsAccessKey((String) configMap.get("ACCESS_KEY" + value));
        config.setAmazonAwsSecretKey((String) configMap.get("SECRET_KEY" + value));
        config.setAmazonAwsBucketName((String) configMap.get("BUCKET_NAME" + value) );
        FileTransfer transfer = FileTransferHelpler.getTransfer(config);
        // 存入oss
        String remotePath = (String) configMap.get(Constants.REMOTE_PATH + value);
        remotePath = this.fileNamePretreatment(remotePath, params);
        String remoteFile;
        if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
            remoteFile = remotePath + "/" + fileName;
        } else {
            remoteFile = remotePath + fileName;
        }
        transfer.uploadFileAndDisconnect(localPath + fileName, remoteFile);
        // 删除临时文件
        File files = new File(localPath + fileName);
        files.delete();
        // ok文件存入oss
        if (StringUtils.equals(Constants.PORT_TYPE_10, (String) params.get("port_type"))) {
            params.put("deal_time", DateUtil.getNowDate());
            String fileOkName = this.fileNamePretreatment((String) params.get("port_address"), params);
            fileOkName = StringUtils.isNotEmpty(fileOkName) ? fileOkName.split("\\.")[0] + ".ok" : fileOkName;
            TxtFileUtil.writeJsonFile(fileName,localPath + fileOkName);

            String remoteOkFile;
            if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
                remoteOkFile = remotePath + "/" + fileOkName;
            } else {
                remoteOkFile = remotePath + fileOkName;
            }
            transfer.uploadFileAndDisconnect(localPath + fileOkName, remoteOkFile);

            // 删除临时文件
            File fileOk = new File(localPath + fileOkName);
            fileOk.delete();
        }
        // 更新文件流水-成功
        dealValuePortService.updateFileLog((String) params.get("sequence"), Constants.FILE_STATE_03, "上传"+send+"成功", count);
        log.info("文件发送成功:sources={}, target={}, 存储系统={}", localPath + fileName, remoteFile, send);
    }

    /**
     * 文件名称变量处理
     *
     * @param fileName
     * @param params
     * @return
     */
    public String fileNamePretreatment(String fileName, Map<String, Object> params) {
        String regex = "\\[(.*?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileName);

        // 获取文件名变量
        List<String> variables = new ArrayList<>();
        while (m.find()) {
            variables.add(m.group(1));
        }

        if (CollectionUtils.isNotEmpty(variables)) {
            for (String variable : variables) {
                if (params.get(variable) == null) {
                    throw new RuntimeException("文件名变量 " + variable + " 未赋值，请检查参数");
                }
                fileName = fileName.replace("[" + variable + "]", (String) params.get(variable));
            }
        }
        return fileName;
    }

    /**
     * 接口信息数据字典转换
     * @param port_type
     * @return
     */
    public String transferPortTypeDict (String port_type) {
        String portType = "";

        if (StringUtils.equals(Constants.PORT_TYPE_1, port_type)) {
            portType = Constants.CONFIG_TYPE_GZ;//1--->GZ
        }else if(StringUtils.equals(Constants.PORT_TYPE_2,port_type)){
            portType = Constants.CONFIG_TYPE_JYHC;//2--->JYHC
        }else if(StringUtils.equals(Constants.PORT_TYPE_3, port_type)){
            portType = Constants.CONFIG_TYPE_TAHC;//3--->TAHC
        }else if(StringUtils.equals(Constants.PORT_TYPE_4, port_type)){
            portType = Constants.CONFIG_TYPE_WINDHC;//4--->WINDHC
        }else if(StringUtils.equals(Constants.PORT_TYPE_5, port_type)){
            portType = Constants.CONFIG_TYPE_ZGHC;//5--->ZGHC
        }else if(StringUtils.equals(Constants.PORT_TYPE_7, port_type)){
            portType = Constants.CONFIG_TYPE_CPTS;//7--->CPTS
        }else if(StringUtils.equals(Constants.PORT_TYPE_8, port_type)){
            portType = Constants.CONFIG_TYPE_CHJR;//8--->CHJR
        }else if(StringUtils.equals(Constants.PORT_TYPE_9, port_type)){
            portType = Constants.CONFIG_TYPE_SJZT;//9--->SJZT
        }else if(StringUtils.equals(Constants.PORT_TYPE_10, port_type)){
            portType = Constants.CONFIG_TYPE_CISP;//10--->CISP
        }

        return portType;
    }

    /**
     * 历史表增量数据更新异常
     * @param request
     * @param workDate
     * @throws Exception
     */
    public void historyIncrementDataHandler (PubReq request, String workDate) throws Exception {
        try {
            dealValuePortService.handleIncrementData(request, workDate);
        } catch (Exception e) {
            log.info("**************** 任务ID:" + request.getTaskId() + " 接口增量文件更新失败 *********************");
            throw new Exception("任务ID:" + request.getTaskId() + " 接口增量文件更新失败");
        }

    }

}
