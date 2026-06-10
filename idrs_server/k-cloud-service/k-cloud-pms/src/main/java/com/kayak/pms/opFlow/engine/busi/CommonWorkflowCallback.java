package com.kayak.pms.opFlow.engine.busi;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.spring.SpringContextHolder;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysBeans;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceConstant;
import com.kayak.pms.opFlow.engine.entity.FormData;
import com.kayak.pms.opFlow.engine.model.Execution;
import com.kayak.pms.opFlow.engine.service.BusiInfoService;
import com.kayak.pms.opFlow.engine.service.FormDataService;
import com.kayak.pms.opFlow.engine.utils.BusinessDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工作流通用回调
 *
 * @author  xiamh
 * @date    2020-2-1
 */
public class CommonWorkflowCallback {

    private static final Logger logger = LoggerFactory.getLogger(CommonWorkflowCallback.class);

    /**
     * 流程开始回调, 只修改表对应的状态
     *
     * @param formData
     * @throws KSystemException
     * @throws SQLException
     * @throws KPromptException
     * @throws KSqlException
     */
//    public static void updateCustomStartProcessCallback(Map<String, Object> formData) throws KSystemException, SQLException, KPromptException, KSqlException {
//        ComnDao comnDao = SysBeans.getComnDao();
//        String busiTableName = (String) formData.get(ProcessInstanceConstant.BUSI_TABLE_NAME);
//        String busiTablePrimaryKey = (String) formData.get(ProcessInstanceConstant.BUSI_TABLE_PrimaryKey);
//        String busiTablePrimaryKeyVal = (String) formData.get(busiTablePrimaryKey);
//        String busiProjectid = (String) formData.get("project_id");
//        Map<String, Object> updateParams = new HashMap<>();
//        updateParams.put("process_status", ProcessInstanceConstant.RUNNING);
//        updateParams.put("busiTableName", busiTableName);
//        updateParams.put("busiTablePrimaryKey", busiTablePrimaryKey);
//        updateParams.put("busiProjectid", busiProjectid);
//
//        if (StringHelper.isEmpty(busiTablePrimaryKey) || StringHelper.isEmpty(busiTablePrimaryKeyVal) || StringHelper.isEmpty(busiTableName)) {
//            logger.info("开启流程失败, 业务表{}的主键{}的值{}不正确", busiTableName, busiTablePrimaryKey, busiTablePrimaryKeyVal);
//            throw new WorkflowException("开启失败");
//        }
//        String sql = "UPDATE " + busiTableName + " SET process_instance_id=$S{process_instance_id}, process_status=$S{process_status} WHERE " + busiTablePrimaryKey + "='" + busiTablePrimaryKeyVal + "'";
//        if("project_labourhours_detail".equals(busiTableName) && !StringHelper.isEmpty(busiProjectid)){ //工时填报;项目分类特殊处理
//            sql = sql + " AND project_id ='"+busiProjectid+"'";
//        }
//        comnDao.doUpdateBySql(sql, formData);
//    }



    /**
     * 更新流程业务表状态
     * @param processInstanceId
     * @param processStatus
     */
    public static void updateProcessStatus(String processInstanceId, String processStatus) {
        BusiInfoService busiInfoService = SysBeans.getBean("busiInfoServiceImpl");
        busiInfoService.updateProcessStatusByInstanceId(processInstanceId, processStatus);
    }


    /**
     * 更新业务表process_instance_id和process_status字段
     * @param params
     * @param processInstanceId
     * @throws SQLException
     */
    @Deprecated
    public static void updateByProcessInstanceId(Map<String, Object> params, String processInstanceId) throws Exception {
        BusinessDataUtil businessDataUtil = SysBeans.getBean("businessDataUtil");
        businessDataUtil.updateBusinessData(processInstanceId, params);
    }

    /**
     * 流程撤销更新业务表状态
     * @param processInstanceId
     * @param processStatus
     * @throws SQLException
     */
    public static void updateAggressiveTypeProcessStatus(String processInstanceId, String processStatus) throws Exception {
        ComnDao comnDao = SysBeans.getBean("comnDao");
        FormDataService formDataService = SpringContextHolder.getBean("formDataService");
        List<FormData> formData = formDataService.listLatestFormData(processInstanceId);
        Map<String, Object> params = formData.stream().collect(Collectors.toMap(FormData::getFieldName, FormData::getFieldValue));
        String busiTableName = (String) params.get(ProcessInstanceConstant.BUSI_TABLE_NAME);

        String busiTablePrimaryKey = (String) params.get(ProcessInstanceConstant.BUSI_TABLE_PrimaryKey);
        String[] busiTablePrimaryKeys = busiTablePrimaryKey.split(",");

        StringBuilder sql = new StringBuilder("UPDATE " + busiTableName + " SET process_status='"+processStatus+"' WHERE 1 = 1 ");
        for (String tablePrimaryKey : busiTablePrimaryKeys) {
            sql.append(" AND ");
            sql.append(tablePrimaryKey);
            sql.append(" = '");
            sql.append(params.get(tablePrimaryKey));
            sql.append("' ");
        }
        comnDao.update(sql.toString(), params);
    }

    /**
     * 结束回调
     *
     * @param
     */
    public void busiEndProcessCallback() {
        Map<String, Object> updateParams = getUpdateParams();
        updateProcessStatus((String) updateParams.get("process_instance_id"), ProcessInstanceConstant.FINISH);
    }

    /**
     * 驳回到申请回调
     *
     * @param
     */
    public void busiRejectToApplyProcessCallback() {
        Map<String, Object> updateParams = getUpdateParams();
        updateProcessStatus((String) updateParams.get("process_instance_id"), ProcessInstanceConstant.REJECT_TO_APPLY);
    }

    /**
     * 拒绝回调
     *
     * @param
     */
    public void busiRefuseProcessCallback() {
        Map<String, Object> updateParams = getUpdateParams();
        updateProcessStatus((String) updateParams.get("process_instance_id"), ProcessInstanceConstant.REFUSE);
    }

    /**
     * 获取审批数据
     * @return
     */
    private Map<String, Object> getUpdateParams() {
        Map<String, Object> params = RequestSupport.getParameters();
        Execution execution = (Execution) params.get("execution");
//        String busiTableName = (String) execution.getLatestSubmitParams().get(ProcessInstanceConstant.BUSI_TABLE_NAME);
        // 可以用类似于上面的方法, 做特殊的数据处理或者重新开启工作流
        Map<String, Object> updateParams = new HashMap<>();
//        updateParams.put("table_name", busiTableName);
        updateParams.put("process_instance_id", execution.getProcessInstance().getProcessInstanceId());
        return updateParams;
    }

}
