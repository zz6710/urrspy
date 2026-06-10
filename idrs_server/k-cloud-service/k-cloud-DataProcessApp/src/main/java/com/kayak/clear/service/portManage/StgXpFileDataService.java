package com.kayak.clear.service.portManage;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.clear.resp.PubResp;
import com.kayak.clear.service.pub.CreateTaskService;
import com.kayak.core.system.SysUtil;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.dps.ods.service.JCConfigService;
import com.kayak.utils.EsbFileUtil;
import com.kayak.utils.FileTransferHelpler;
import com.kayak.utils.fileTransfer.config.FileTransferConfig;
import com.kayak.utils.fileTransfer.interfaces.FileTransfer;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import com.kayakwise.kcloud.batch.model.bo.RegistClearTaskPojo;
import com.kayakwise.kcloud.batch.model.req.TaskRegeditReq;
import com.kayakwise.kcloud.batch.service.BaseTaskService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 批处理-数据中台STG层源数据入库
 * @author zhangz
 * @date 2025/05/29
 */
@Component
@Scope("prototype")
public class StgXpFileDataService extends BaseTaskService<PubReq, PubResp> {

    private static Logger log = LoggerFactory.getLogger(StgXpFileDataService.class);

    @Autowired
    public CreateTaskService createTaskService;

    @Autowired
    private DealPortFileService dealPortFileService;

    @Autowired
    private JCConfigService jcConfigService;

    int workRedo = 0;

    int waitTime = 0;

    String workDate = "";

    @Override
    protected void doCheckParams(PubReq request) throws Exception {
        beforeClear(request);
        log.info(" ###### 参数校验 ");
    }

    @Override
    protected void doCheckBusiness(PubReq request) throws Exception {
        log.info(" ###### 业务校验 ");
    }

    @Override
    protected RegistClearTaskPojo taskRegist(TaskRegeditReq taskRegeditReq) throws Exception {
        log.info(" ###### 任务注册");
        return createTaskService.createSystemTask(taskRegeditReq);
    }

    @StepNo(stepNo = 1)
    protected void process(PubReq request) throws Exception {
        log.info("---------- 数据中台STG层数据处理 Start -----------");
        log.info(request.getTaskId() + " Request: {}", request);

        String taskId = request.getTaskId();
        String taskDate = request.getTaskDate();

        try {
            Map<String, Object> configMap = jcConfigService.getConfigInfo("ESB_XP");
            String serverPath = (String) configMap.get("SERVERPATH");
            String clientPath = (String) configMap.get("CLIENTPATH");
            String fileName = (String) configMap.get("CLIENTFILENAME");
            String tradeCode = (String) configMap.get("TRADECODE");
            String hostNo = (String) configMap.get("HOSTNO");

            if (StringUtils.isNotEmpty(serverPath)) {
                serverPath = serverPath.replace("[deal_date]", taskDate);
            }
            if (StringUtils.isNotEmpty(clientPath)) {
                clientPath = clientPath.replace("[deal_date]", taskDate);
            }
            // 下载信评债券持仓明细文件
            boolean flag = EsbFileUtil.esbDownload(serverPath, clientPath, tradeCode, hostNo);

            if (flag) {
                // 上传文件S3或者华为OBS存储，支持双线存储
                Map<String, Object> configMap1 = jcConfigService.getConfigInfo(Constants.CONFIG_TYPE_SJZT);
                Object sendObj = configMap1.get("SEND");
                if (ObjectUtil.isEmpty(sendObj)) {
                    uploadFileToObs(configMap1, "S3", taskDate, clientPath, fileName);
                } else {
                    String sendType = String.valueOf(sendObj);
                    String[] sends = sendType.split(",");
                    for (String send : sends) {
                        uploadFileToObs(configMap1, send, taskDate, clientPath, fileName);
                    }
                }
            } else {
                if (workRedo-- > 0) {
                    log.info("开始等待{}秒,文件名:" + fileName, waitTime);
                    Thread.sleep(waitTime*1000);
                    process(request);
                }
            }

        }catch (Exception e){
            log.error("[URRS-ERROR-H]{}日任务{}执行失败：{}",taskDate,taskId,e.getMessage());
            throw e;
        }
        log.info("---------- 数据中台STG层数据处理 End -----------");
    }

    /**
     * 清算流程启动之前 调用
     * 验证清算检查前 的 准备工作是否完成
     * 包含数据初始化工作 （axin）
     * @throws Exception
     */
    public void beforeClear(PubReq request) throws Exception{

        //参数初始化
        workDate=request.getTaskDate();
        workRedo=Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051213"));
        waitTime=Integer.parseInt(SysUtil.getSystemParamsByParaid("90000051214"));

        if("".equals(workDate)||workDate==null){
            throw new Exception("报送工作日不能为空。");
        }

    }

    /**
     * 文件上传至S3或者华为OBS存储
     *
     * @param configMap
     * @param send
     * @param localFile
     * @return
     */
    public void uploadFileToObs(Map<String, Object> configMap, String send, String taskDate, String localFile, String fileName) throws Exception {
        // 根据存储系统判断获取配置
        String value = StringUtils.equals("S3", send) ? "" : "_" + send;

        FileTransferConfig config = new FileTransferConfig();
        config.setProtocol(send);
        config.setAmazonEndpointUrl((String) configMap.get("ENDPOINT_URL" + value));
        config.setAmazonAwsAccessKey((String) configMap.get("ACCESS_KEY" + value));
        config.setAmazonAwsSecretKey((String) configMap.get("SECRET_KEY" + value));
        config.setAmazonAwsBucketName((String) configMap.get("BUCKET_NAME" + value));
        FileTransfer transfer = FileTransferHelpler.getTransfer(config);

        String remotePath = (String) configMap.get(Constants.REMOTE_PATH + value);
        String remoteFile;
        if (!remotePath.endsWith("\\") && !remotePath.endsWith("/")) {
            remoteFile = remotePath + "/" + fileName;
        } else {
            remoteFile = remotePath + fileName;
        }

        remoteFile = remoteFile.replace("[deal_date]", taskDate);
        transfer.uploadFileAndDisconnect(localFile, remoteFile);
    }

}
