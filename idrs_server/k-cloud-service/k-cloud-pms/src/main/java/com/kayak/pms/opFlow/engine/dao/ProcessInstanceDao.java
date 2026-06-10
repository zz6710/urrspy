package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceStatus;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Created by daniel on 29/03/2017.
 */
@Repository("processInstanceDao")
public class ProcessInstanceDao extends ComnDao {

    public void createProcessInstance(ProcessInstance processInstance) throws Exception {
        String sql = "INSERT INTO opf_process_instance(process_instance_id,process_id,process_version, creator,create_date,create_time,update_date,update_time,last_operator,finish_date,finish_time,parent_process_Id,parent_node_name," +
                " current_status,master_flag,master_id,current_node,next_node,last_node)" +
                " VALUES ($S{processInstanceId},$S{processId},$S{processVersion},$S{creator}, $S{createDate}, $S{createTime}, $S{createDate}, $S{createTime},$S{creator}, $S{finishDate}, $S{finishTime}," +
                " $S{parentProcessId}, $S{parentNodeName}, $S{currentStatus}, $S{masterFlag}, $S{masterId},$S{currentNode},$S{nextNode},$S{lastNode})";
        super.update(sql, processInstance);
    }

    public void updateStatus(ProcessInstance processInstance) throws Exception {
        processInstance.setUpdateDate(DateHelper.getCurrentDate());
        processInstance.setUpdateTime(DateHelper.getCurrentTime());
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_status=$S{currentStatus},update_date=$S{updateDate},update_time=$S{updateTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public void terminal(ProcessInstance processInstance) throws Exception {
        processInstance.setCurrentStatus(ProcessInstanceStatus.STOP);
        processInstance.setTerminalDate(DateHelper.getCurrentDate());
        processInstance.setTerminalTime(DateHelper.getCurrentTime());
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_status=$S{currentStatus},terminal_date=$S{terminalDate},terminal_time=$S{terminalTime},update_date=$S{terminalDate},update_time=$S{terminalTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public void back(ProcessInstance processInstance) throws Exception {
        processInstance.setCurrentStatus(ProcessInstanceStatus.BACK);
        processInstance.setUpdateDate(DateHelper.getCurrentDate());
        processInstance.setUpdateTime(DateHelper.getCurrentTime());
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_status=$S{currentStatus},update_date=$S{terminalDate},update_time=$S{terminalTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public void refuse(ProcessInstance processInstance) throws Exception {
        processInstance.setCurrentStatus(ProcessInstanceStatus.REFUSE);
        processInstance.setUpdateDate(DateHelper.getCurrentDate());
        processInstance.setUpdateTime(DateHelper.getCurrentTime());
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_status=$S{currentStatus},update_date=$S{updateDate},update_time=$S{updateTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public void updateNode(ProcessInstance processInstance) throws Exception {
        processInstance.setUpdateDate(DateHelper.getCurrentDate());
        processInstance.setUpdateTime(DateHelper.getCurrentTime());
        // 设置最后操作人
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_node=$S{currentNode},last_node=$S{lastNode},next_node=$S{nextNode},update_date=$S{updateDate},update_time=$S{updateTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public void finish(ProcessInstance processInstance) throws Exception {
        processInstance.setCurrentStatus(ProcessInstanceStatus.FINISH);
        processInstance.setCurrentNode(null);
        processInstance.setNextNode(null);
        processInstance.setFinishDate(DateHelper.getCurrentDate());
        processInstance.setFinishTime(DateHelper.getCurrentTime());
        // 设置最后操作人
        processInstance.setLastOperator(SysUtil.getLoginUserid());
        String sql = "UPDATE opf_process_instance SET current_status=$S{currentStatus},current_node=$S{currentNode},last_node=$S{lastNode},next_node=$S{nextNode},finish_date=$S{finishDate},finish_time=$S{finishTime},update_date=$S{finishDate},update_time=$S{finishTime},last_operator=$S{lastOperator} WHERE process_instance_id=$S{processInstanceId}";
        super.update(sql, processInstance);
    }

    public int countByNotBack(ProcessInstance processInstance) throws Exception {
        String sql = "SELECT count(*) FROM opf_process_instance WHERE" +
                " parent_process_id=$S{parentProcessId}" +
                " AND process_instance_id!=$S{processInstanceId} AND current_status!='" + ProcessInstanceStatus.BACK + "'";
        return super.findRow(Integer.class, sql, 0, processInstance);
    }

    public int countByNotFinish(ProcessInstance processInstance) throws Exception {
        String sql = "SELECT count(*) FROM opf_process_instance WHERE" +
                " parent_process_id=$S{parentProcessId} AND process_instance_id!=$S{processInstanceId} AND current_status!='" + ProcessInstanceStatus.FINISH + "'";
        return super.findRow(Integer.class, sql, 0, processInstance);
    }

    public ProcessInstance getProcessInstanceById(String processInstanceId) throws Exception {
        String sql = "SELECT process_instance_id,process_id,process_version, creator,create_date,create_time,finish_date,finish_time,parent_process_Id,parent_node_name," +
                "current_status,master_flag,master_id,current_node,next_node,last_node FROM opf_process_instance" +
                " WHERE process_instance_id='" + processInstanceId + "'";
        return super.findRow(ProcessInstance.class, sql, 0, null);
    }

    public void complete(ProcessInstance processInstance) {}

    public ProcessInstance getParentProcessInstance(String processInstanceId) throws Exception {
        String sql = "SELECT parent_process_Id FROM opf_process_instance WHERE process_instance_id='" + processInstanceId + "'";
        sql = "SELECT process_instance_id,process_id,process_version, creator,create_date,create_time,finish_date,finish_time,parent_process_Id,parent_node_name," +
                "current_status,master_flag,master_id,current_node,next_node,last_node FROM opf_process_instance" +
                " WHERE process_instance_id=(" + sql + ")";
        return super.findRow(ProcessInstance.class, sql, 0, null);
    }

    public ProcessInstance getByParent(String processInstanceId, String name) throws Exception {
        String sql = "SELECT process_instance_id,process_id,process_version, creator,create_date,create_time,finish_date,finish_time,parent_process_Id,parent_node_name," +
                " current_status,master_flag,master_id,current_node,next_node,last_node FROM opf_process_instance FROM opf_process_instance" +
                " WHERE parent_process_id='" + processInstanceId + "' AND last_node='"+name+"'";
        return super.findRow(ProcessInstance.class, sql, 0, null);
    }

    public SqlResult<ProcessInstance> findByCreator(SqlParam<ProcessInstance> params) throws Exception {
        String loginUserid = SysUtil.getLoginUserid();
        String sql = "SELECT t2.display_name process_display_name,t1.process_instance_id,su.username creator,CONCAT(t1.create_date,t1.create_time) create_date,t1.last_node,su1.username last_operator," +
                "CONCAT(t1.update_date,t1.update_time) update_date,t1.current_node,t1.current_status,tpf.prod_code,tpi.prod_name" +
                " FROM opf_process_instance t1 LEFT JOIN opf_process t2 ON t1.process_id=t2.process_id AND t1.process_version=t2.version\n" +
                " LEFT JOIN sys_user su ON t1.creator = su.userid" +
                " LEFT JOIN sys_user su1 ON t1.last_operator = su1.userid" +
                " LEFT JOIN t8_prod_flow tpf ON tpf.op_process_id = t1.process_instance_id\n" +
                " LEFT JOIN t8_prod_info tpi ON tpf.prod_code=tpi.prod_code" +
                " WHERE master_flag='0' AND creator = '" + loginUserid + "'";
        if (StringUtils.isNotBlank(params.getModel().getCurrentStatus())){
            sql += " AND current_status ='" + params.getModel().getCurrentStatus() +"'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProcessDisplayName())) {
            sql += " AND display_name LIKE '%" +params.getModel().getProcessDisplayName() +"%'";
        }
        sql += " ORDER BY t1.create_date DESC,t1.create_time DESC";
        return super.findRows(sql, params);
    }

    public SqlResult<ProcessInstance> findByJoin(SqlParam<ProcessInstance> params) throws Exception {
        String loginUserid = SysUtil.getLoginUserid();
        String sql = "SELECT process_instance_id FROM opf_his_task WHERE process_instance_id=t4.process_instance_id AND submit_user='" + loginUserid + "'";
        sql = "SELECT DISTINCT if(length(t4.parent_process_id)>0,t4.parent_process_id,t4.process_instance_id) FROM opf_process_instance t4 WHERE EXISTS (" + sql + ")";
        sql = "SELECT t2.display_name process_display_name,t1.process_instance_id,su.username creator,CONCAT(t1.create_date,t1.create_time) create_date,t1.last_node," +
                "su1.username last_operator,CONCAT(t1.update_date,t1.update_time) update_date,t1.current_node,t1.current_status,tpf.prod_code,tpi.prod_name" +
                " FROM opf_process_instance t1 LEFT JOIN opf_process t2 ON t1.process_id=t2.process_id AND t1.process_version=t2.version\n" +
                " LEFT JOIN sys_user su ON t1.creator = su.userid" +
                " LEFT JOIN sys_user su1 ON t1.last_operator = su1.userid" +
                " LEFT JOIN t8_prod_flow tpf ON tpf.op_process_id = t1.process_instance_id\n" +
                " LEFT JOIN t8_prod_info tpi ON tpf.prod_code=tpi.prod_code" +
                " WHERE t1.process_instance_id in (" + sql + ") ";
        if (StringUtils.isNotBlank(params.getModel().getCurrentStatus())){
            sql += " AND current_status  in ('" + params.getModel().getCurrentStatus() +"')";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql += " and tpf.prod_code = '"+params.getModel().getProdCode()+"'";
        }
        if (StringUtils.isNotBlank(params.getModel().getProcessDisplayName())) {
            sql += " AND display_name LIKE '%" +params.getModel().getProcessDisplayName() +"%'";
        }
        sql += " ORDER BY t1.create_date DESC,t1.create_time DESC";
        return super.findRows(sql,params);
    }

}
