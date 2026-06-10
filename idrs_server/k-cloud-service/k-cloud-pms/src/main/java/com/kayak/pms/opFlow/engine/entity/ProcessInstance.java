package com.kayak.pms.opFlow.engine.entity;

import com.kayak.core.system.SysUtil;
import com.kayak.graphql.annotation.GraphQLField;
import com.kayak.graphql.annotation.GraphQLModel;
import com.kayak.helper.StringHelper;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceStatus;
import lombok.Data;

import java.io.Serializable;

/**
 * 流程实例
 * Created by daniel on 20/03/2017.
 */
@Data
@GraphQLModel(fetcher = "processInstanceService",table = "opf_process_instance")
public class ProcessInstance implements Serializable {
    private static final long serialVersionUID = -6266849684594338511L;

    /**
     * 主键ID
     */
    @GraphQLField
    private String processInstanceId;

    @GraphQLField(label = "流程名称", kkhtml = "KFieldText", sql = "t2.display_name like '%$U{processDisplayName}%'", field = "processDisplayName")
    private String processDisplayName;
    @GraphQLField(label = "流程状态", kkhtml = "KFieldSelect", kkhtmlExt = "{\"data-dict\":\"process_instance_status\"}", field = "current_status")
    private String currentStatus;
    /**
     * 流程定义ID
     */
    @GraphQLField
    private String processId;
    @GraphQLField
    private String processVersion;

    @GraphQLField
    private String submitParams;

    /**
     * 流程实例创建者ID
     */
    private String creator;
    /**
     * 流程实例创建时间
     */
    private String createDate;
    private String createTime;
    // 查询条件：发起时间范围
    @GraphQLField(sql = "create_date >= $S{createDateStart}", field = "createDateStart")
    private String createDateStart;
    @GraphQLField(sql = "create_date <= $S{createDateEnd}", field = "createDateEnd")
    private String createDateEnd;
    /**
     * 上次操作时间
     */
    private String updateDate;
    private String updateTime;

    /**
     * 流程实例完成时间
     */
    private String finishDate;
    private String finishTime;

    /**
     * 流程实例终止时间
     */
    private String terminalDate;
    private String terminalTime;

    /**
     * 流程实例为子流程时，该字段标识父流程实例ID
     */
    private String parentProcessId;

    /**
     * 流程实例为子流程时，该字段标识父流程哪个节点模型启动的子流程
     */
    private String parentNodeName;

    private String originalData;

    private String type;

    private String masterFlag;
    private String masterId;

    @GraphQLField
    private String currentNode;
    @GraphQLField
    private String nextNode;
    private String lastNode;
    /**
     * 最后操作人
     */
    private String lastOperator;

    private String prodCode;
    private String prodName;

    public static ProcessInstance copy(ProcessInstance processInstance) {
        ProcessInstance processInstanceCopy = new ProcessInstance();

        processInstanceCopy.setProcessId(processInstance.getProcessId());
        processInstanceCopy.setProcessInstanceId(StringHelper.getPrimaryKey());
        processInstanceCopy.setProcessVersion(processInstance.getProcessVersion());
        processInstanceCopy.setMasterFlag(processInstance.getMasterFlag());
        processInstanceCopy.setMasterId(processInstance.getMasterId());
        processInstanceCopy.setCurrentNode(processInstance.getCurrentNode());
        processInstanceCopy.setLastNode(processInstance.getLastNode());
        processInstanceCopy.setNextNode(processInstance.getNextNode());
        processInstanceCopy.setCurrentStatus(ProcessInstanceStatus.RUNNING);
        processInstanceCopy.setCreator(SysUtil.getLoginUserid());
        processInstanceCopy.setCreateDate(DateHelper.getCurrentDate());
        processInstanceCopy.setCreateTime(DateHelper.getCurrentTime());
        // 上级流程实例id
        processInstanceCopy.setParentProcessId(processInstance.getProcessInstanceId());
        // 从节点标识
        processInstanceCopy.setMasterFlag("1");
        // 主节点id
        processInstanceCopy.setMasterId(processInstance.getProcessInstanceId());

        return processInstanceCopy;
    }

    public static ProcessInstance copy(ProcessInstance processInstance, boolean fromParent) {
        ProcessInstance processInstanceCopy = copy(processInstance);
        if (fromParent) {
            // 上级流程实例id
            processInstanceCopy.setParentProcessId(processInstance.getProcessInstanceId());
            // 从节点标识
            processInstanceCopy.setMasterFlag("1");
            // 主节点id
            processInstanceCopy.setMasterId(processInstance.getProcessInstanceId());
        }
        return processInstanceCopy;
    }

    public static void copy(ProcessInstance from, ProcessInstance to) {
        to.setProcessInstanceId(from.getProcessInstanceId());
        to.setProcessId(from.getProcessId());
        to.setProcessVersion(from.getProcessVersion());
        to.setParentProcessId(from.getParentProcessId());
        to.setCreator(from.getCreator());
        to.setCreateTime(from.getCreateTime());
        to.setCreateDate(from.getCreateDate());
        to.setCurrentStatus(from.getCurrentStatus());
        to.setCurrentNode(from.getCurrentNode());
        to.setLastOperator(from.getLastOperator());
        to.setLastNode(from.getLastNode());
        to.setUpdateDate(from.getUpdateDate());
        to.setUpdateTime(from.getUpdateTime());
        to.setNextNode(from.getNextNode());
        to.setMasterFlag(from.getMasterFlag());
        to.setMasterId(from.getMasterId());
        to.setFinishTime(from.getFinishTime());
        to.setFinishDate(from.getFinishDate());
        to.setTerminalTime(from.getTerminalTime());
        to.setTerminalDate(from.getTerminalDate());
        to.setType(from.getType());
    }
}
