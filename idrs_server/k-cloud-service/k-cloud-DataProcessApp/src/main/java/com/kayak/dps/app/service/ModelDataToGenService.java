package com.kayak.dps.app.service;

import com.kayak.clear.exception.ModelDataHandleException;
import com.kayak.dps.app.utils.ReportDataGenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ModelDataToGenService {

    /**
     * DWD层及DWS层报送业务表数据加工处理通用方法(一维及二维表数据加工入库)
     * @param task_id 清算任务id
     * @param base_date 数据生成截止日期
     */
    public void DwdToAppDataProcessGenMethod(String task_id, String base_date) {
        Map<String, Object> params = new HashMap<>();
        try {
            log.info("---------- 任务: " + task_id +" 数据加工开始 Start -----------");
            ReportDataGenUtils.reportDataHandlerProcess(task_id,base_date);
            log.info("---------- 任务: " + task_id +" 数据加工结束 End-----------");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new ModelDataHandleException("---------- 任务: " +task_id + " 数据生成异常:" + e);
        }
    }

    /**
     * DWD层及DWS层报送业务表数据加工处理通用方法(一维及二维表数据加工入库)
     * 日期不是通过报送时点计算出出来的，而是传入base_date跑数
     * @param task_id 清算任务id
     * @param base_date 数据生成截止日期
     */
    public void DwdToAppDataProcessRepMethod(String task_id, String base_date) {
        Map<String, Object> params = new HashMap<>();
        try {
            log.info("---------- 任务: " + task_id +" 数据加工开始 Start -----------");
            ReportDataGenUtils.baseDataHandlerProcess(task_id,base_date);
            log.info("---------- 任务: " + task_id +" 数据加工结束 End-----------");
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            throw new ModelDataHandleException("---------- 任务: " +task_id + " 数据生成异常:" + e);
        }
    }

}
