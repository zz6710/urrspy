package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.dao.OpfLogDao;
import com.kayak.pms.opFlow.engine.constant.OperationTypeConstant;
import com.kayak.pms.opFlow.model.OpfLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "流程操作日志", model = OpfLog.class)
public class OpfLogService {
    @Autowired
    private OpfLogDao opfLogDao;

    @API(desc = "根据流程实例查询操作日志",auth = APIAuth.NO)
    public SqlResult<OpfLog> findLogs(SqlParam<OpfLog> param) throws Exception {
        param.getModel().setProcessInstanceId(param.getModel().getProcessInstanceId().substring(0,32));
        List<OpfLog> logs = opfLogDao.findLogs(param.getModel());
        // 非终止的，后续展示才有待操作和完成
        if (!OperationTypeConstant.END.equals(logs.get(logs.size() - 1).getOperationType())) {
            // 获取下一个节点，作为待办任务展示
            String nextNode = logs.get(logs.size() - 1).getNextNode();
            if (StringHelper.isNotEmpty(nextNode)) {
                for (String node : nextNode.split("\\|")) {
                    OpfLog opfLog = new OpfLog();
                    opfLog.setDisplayName(node);
                    opfLog.setOperationType(OperationTypeConstant.TODO);
                    //获取流程实例id
                    String processInstanceId = param.getModel().getProcessInstanceId();
                    SqlRow row = opfLogDao.findRow("SELECT id,display_name,task_type FROM opf_task WHERE process_instance_id = '" + processInstanceId + "'", processInstanceId);
                    if (row!=null) {
                        String taskType = row.getString("task_type");
                        String displayName = row.getString("display_name");
                        if (node.equals(displayName)) {
                            //功能节点
                            if ("0".equals(taskType)) {
                                String id = row.getString("id");
                                //根据指定人员进行查询
                                SqlRow taskActorRow = opfLogDao.findRow("SELECT GROUP_CONCAT(actor_id) actor_id FROM opf_task_actor WHERE actor_type = '2' AND task_id = '" + id + "'", id);
                                if (taskActorRow!=null) {
                                    String actorId = taskActorRow.getString("actor_id");
                                    if (StringUtils.isNotEmpty(actorId)) {
                                        SqlRow userRow = opfLogDao.findRow("SELECT GROUP_CONCAT(username) username FROM sys_user WHERE FIND_IN_SET(userid,'" + actorId + "')", actorId);
                                        if (userRow != null) {
                                            String username = userRow.getString("username");
                                            opfLog.setUsername(username);
                                        }
                                    } else {
                                        //通过角色进行查询
                                        taskActorRow = opfLogDao.findRow("SELECT GROUP_CONCAT(actor_id) actor_id FROM opf_task_actor WHERE actor_type = '1' AND task_id = '" + id + "'", id);
                                        if (taskActorRow!=null) {
                                            actorId = taskActorRow.getString("actor_id");
                                            if (StringUtils.isNotEmpty(actorId)) {
                                                SqlRow userRow = opfLogDao.findRow("SELECT GROUP_CONCAT(username) username FROM sys_user \n" +
                                                        "WHERE userid IN (SELECT userid FROM sys_user_role WHERE FIND_IN_SET(roleid,'"+actorId+"'))", actorId);
                                                if (userRow != null) {
                                                    String username = userRow.getString("username");
                                                    opfLog.setUsername(username);
                                                }
                                            }
                                        }
                                    }
                                }else {
                                    //通过角色进行查询
                                    taskActorRow = opfLogDao.findRow("SELECT GROUP_CONCAT(actor_id) actor_id FROM opf_task_actor WHERE actor_type = '1' AND task_id = '" + id + "'", id);
                                    if (taskActorRow!=null) {
                                        String actorId = taskActorRow.getString("actor_id");
                                        if (StringUtils.isNotEmpty(actorId)) {
                                            SqlRow userRow = opfLogDao.findRow("SELECT GROUP_CONCAT(username) username FROM sys_user \n" +
                                                    "WHERE userid IN (SELECT userid FROM sys_user_role WHERE FIND_IN_SET(roleid,'"+actorId+"'))", actorId);
                                            if (userRow != null) {
                                                String username = userRow.getString("username");
                                                opfLog.setUsername(username);
                                            }
                                        }
                                    }
                                }
                            } else if("1".equals(taskType)) {//审批节点
                                String prodCode = param.getModel().getProdCode();
                                if (StringUtils.isNotEmpty(prodCode)) {
                                    //获取审批流-流程实例id
                                    SqlRow businessRow = opfLogDao.findRow("SELECT id FROM wf_business_process WHERE prod_code = '"+prodCode+"' " +
                                            "AND PROCESS_TYPE ='2' AND PROCESS_STATUS ='1' AND (server ='ProdFlowService-prodManagerInputApproval' " +
                                            "OR server='ProdFlowService-businessManagerInputApproval') OR server='ProdFlowService-businessManagerInputApproval' " +
                                            " ORDER BY CREATE_TIME DESC LIMIT 1", prodCode);
                                    if (businessRow!=null) {
                                        String id = businessRow.getString("id");
                                        SqlRow approvalTaskRow = opfLogDao.findRow("SELECT GROUP_CONCAT(operator) operator FROM wf_approval_task WHERE process_id = '" + id + "' AND RESULT ='2'", id);
                                        if (approvalTaskRow!=null) {
                                            String operator = approvalTaskRow.getString("operator");
                                            SqlRow userRow = opfLogDao.findRow("SELECT GROUP_CONCAT(username) username FROM sys_user WHERE FIND_IN_SET(userid,'" + operator + "')", operator);
                                            if (userRow != null) {
                                                String username = userRow.getString("username");
                                                opfLog.setUsername(username);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                    logs.add(opfLog);
                    break;
                }
            } else {
                OpfLog opfLog = new OpfLog();
                opfLog.setOperationType(OperationTypeConstant.FINISH);
                logs.add(opfLog);
            }
        }

        return SqlResult.build(logs);
    }
}
