package com.kayak.pms.netValue.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.action.BaseController;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.ResponseResult;
import com.kayak.pms.disclosureControl.disclousreEnum.DisclosureTypeEnum;
import com.kayak.pms.disclosureControl.disclousreEnum.OperationTypeEnum;
import com.kayak.pms.disclosureControl.model.DisclosureOperation;
import com.kayak.pms.disclosureControl.service.DisclosureOperationService;
import com.kayak.utils.DateHelper;
import com.kayak.pms.netValue.model.T8ProdNetValueNotice;
import com.kayak.pms.netValue.service.T8ProdNetValueTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 净值报告下载Action
 * @author: zls
 * @create: 2021-08-03 18:41
 * @memo 备注信息
 */
@RestController
public class netValueAction extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(netValueAction.class);

    @Autowired
    private T8ProdNetValueTaskService t8ProdNetValueTaskService;

    @Autowired
    private DisclosureOperationService disclosureOperationService;

    /**
     * 下载净值文件
     *
     * @param response 响应体
     * @return out
     * @throws Exception
     */
    @RequestMapping(value = "/netValue/downloadNetValue.json",produces = { "application/json;charset=UTF-8"})
    public void downloadPrintTempVersion(HttpServletResponse response) {
        Map<String, Object> params = RequestSupport.getParameters();
        byte[] buffer = new byte[1024];
        FileInputStream fileInputStream = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream outputStream = null;
        String path = "";
        try{
            T8ProdNetValueNotice notice = new T8ProdNetValueNotice();
            String taskDate = params.get("taskDate").toString();
            notice.setDisclosureDate(taskDate);
            //将数据放入到文件中     文件形式待定
            path = t8ProdNetValueTaskService.generateNetValueNotice(notice);
            String fileName = taskDate+"净值公告"+".xls";
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" +
                    new String(fileName.getBytes("GB2312"), "ISO8859-1"));
            //下载
            File file = new File(path);
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
            logger.error(e.getMessage(), e);
        }finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        //return RequestSupport.updateReturnJson(true, "文件导出成功", null).toString();
    }

    @RequestMapping(value = "/completeOperation.json", produces = {"application/json;charset=UTF-8"})
    public ResponseResult completeOperation(@RequestBody Object o) throws Exception {
        String data = JSONObject.toJSONString(o);
        JSONObject json = JSONObject.parseObject(data);
        String str = JSONObject.toJSONString(json.get("latestSubmitParams"));
        String processInstance = JSONObject.toJSONString(json.get("processInstance"));
        Map map = JSONObject.parseObject(str, Map.class);
        Map map2 = JSONObject.parseObject(processInstance, Map.class);
        String processInstanceId = map2.get("processId").toString();
        String currentDate = DateHelper.getCurrentDate();
        String currentTime = DateHelper.getCurrentTime();
        DisclosureOperation disclosureOperation = new DisclosureOperation();
        disclosureOperation.setDealId(map.get("id").toString());
        disclosureOperation.setOperationType(OperationTypeEnum.SEVEN.getVal());
        disclosureOperation.setDisclosureType(DisclosureTypeEnum.NINE.getVal());
        disclosureOperation.setStatus("1");
        disclosureOperation.setEndDate(currentDate);
        disclosureOperation.setEndTime(currentTime);
        //执行保存操作
        disclosureOperationService.updateNetValOperation(disclosureOperation);
        ResponseResult responseResult = new ResponseResult<>("200");
        responseResult.setStatus("200");
        responseResult.setMessage("发起审批!");
        return responseResult;
    }
}
