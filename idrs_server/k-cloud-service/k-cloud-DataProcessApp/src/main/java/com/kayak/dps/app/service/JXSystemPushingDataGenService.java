package com.kayak.dps.app.service;

import com.kayak.clear.exception.ModelDataHandleException;
import com.kayak.dps.app.dao.JXSystemPushingDataGenDao;
import com.kayak.dps.ods.constants.Constants;
import com.kayak.dps.ods.util.zz.SFtpHelper;
import com.kayak.dps.valtabimp.action.FileUploadBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JXSystemPushingDataGenService {

    @Resource
    private FileUploadBiz fileUploadBiz;

    @Resource
    private JXSystemPushingDataGenDao jxSystemPushingDataGenDao;

    /**
     * 给绩效推送委外估值数据生成文件推送至指定sftp目录
     * @param task_id 清算任务id
     * @param change_date 数据获取日期
     */
    public void pushingExternalValueDataToJXSystem(String task_id, String change_date) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        params.put("change_date", change_date);
        String file_name = "";
        try {
            //TODO 此为给绩效风控推送的委外估值解析数据文件，暂不做配置跟清算任务绑定写死(包括文件类型为dat)，后续优化
            if("P080".equals(task_id)){//组合持仓信息
                file_name = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "HOLDING_FILENAME").replace("[deal_date]", change_date).trim();
            } else if("P081".equals(task_id)) {//组合每日统计指标融合表
                file_name = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "DAILY_FILENAME").replace("[deal_date]", change_date).trim();
            }

            String local_path = "";//本地路径
            try {
                if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                    local_path = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "LOCAL_PATH_WIN");
                } else {
                    local_path = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "LOCAL_PATH_LINUX");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("未获取到本地文件:" + e.getMessage());
            }

            local_path = local_path.replace("[deal_date]", change_date).trim();
            String sftp_ip = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "SFTP_IP");
            String username = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "USERNAME");
            String password = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "PASSWORD");
            String remote_path = fileUploadBiz.getExternalGzFileUploadParams(Constants.CONFIG_TYPE_JXHC, "REMOTE_PATH").replace("[deal_date]", change_date).trim();//上传路径

            //根据task_id找到推送文件内容的查询语句
            List<String> dataList = jxSystemPushingDataGenDao.getPushDatalistByTaskId(params);
            this.generateExternalFileData(file_name, local_path, dataList);

            //远程服务器上传文件至本地目录
            try{
                SFtpHelper.putFile(sftp_ip, username, password, remote_path, local_path, file_name + ".dat","1");
                SFtpHelper.putFile(sftp_ip, username, password, remote_path, local_path, file_name + ".dat.ok","1");
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("绩效风控委外估值数据文件上传失败:" + e.getMessage());
            }
            log.info("---------- 委外估值数据生成:任务 Start -----------");
        } catch (Exception e) {
            throw new ModelDataHandleException("委外估值数据生成异常:" + e);
        }
    }

    /**
     * 根据数据集生成txt文件并写入数据
     * @return
     * @throws Exception
     */
    private void generateExternalFileData (String file_name, String local_path, List<String> dataList)  throws Exception  {
        BufferedWriter bufferedWriter = null;
        String okFileName = file_name + ".dat.ok";
        try{
            //创建路径
            File filePath = new File(local_path );
            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            File txt_file = new File(local_path + file_name + ".dat");
            if (txt_file.exists()) {//若文件已存在，则先删除后新建
                txt_file.delete();
                log.info("删除并重新生成推送文件:" + local_path + file_name + ".dat");
            }
                txt_file.createNewFile();

            bufferedWriter = new BufferedWriter(new FileWriter(txt_file));//内容写入缓存
            //生成txt文件内容,每行的内容为推送文件名+换行符
            for (String txt_content : dataList) {
                bufferedWriter.write(txt_content + "\r\n");
            }
            bufferedWriter.flush();//缓存写入文件

            //在写入ok文件
            File ok_file = new File(local_path + okFileName);
            if (ok_file.exists()) {//若文件已存在，则先删除后新建
                ok_file.delete();
                log.info("删除并重新生成推送ok文件:" + local_path + ok_file);
            }
              ok_file.createNewFile();

        }catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new Exception("写入绩效风控txt文件异常:" + e.getMessage());
        }finally {
            bufferedWriter.close();//关闭流
        }
    }

}
