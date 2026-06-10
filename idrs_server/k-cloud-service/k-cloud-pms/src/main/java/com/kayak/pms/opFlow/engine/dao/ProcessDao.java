package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.entity.Process;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by daniel on 23/03/2017.
 */
@Repository("processDao")
public class ProcessDao extends ComnDao {

    public int countProcess(SqlParam<Process> params) throws Exception {
        String sql = "SELECT COUNT(*) num FROM opf_process WHERE (name=$S{name} OR display_name = $S{displayName})";
        if (StringUtils.isNotEmpty(params.getModel().getProcessId())) {
            sql += " AND process_id != $S{processId}";
        }
        return super.findRow(Integer.class, sql, 0, params.getModel());
    }

    public void saveProcess(SqlParam<Process> params) throws Exception {
        this.insertNew(params.getModel());
    }

    public SqlResult<Process> listAllProcess(SqlParam<Process> params) throws Exception {
        String sql = "SELECT t1.process_id,t1.name,t1.display_name,(SELECT t.json FROM opf_process t WHERE t.process_id=t1.process_id ORDER BY t.version DESC LIMIT 1) json,t1.version,t2.max_version=t1.version deploy_status FROM opf_process t1" +
                " LEFT JOIN (SELECT process_id, MAX(version) max_version FROM opf_process GROUP BY process_id) t2 ON t1.process_id=t2.process_id" +
                " WHERE 1=1 AND t1.status='1'";
        return super.findRows(sql, params);
    }

    public SqlResult<Process> listEffectiveProcess(SqlParam<Process> params) throws Exception {
        String sql = "SELECT process_id,name,display_name, json,version,status FROM opf_process WHERE status='1'";
        return super.findRows(sql, params);
    }

    public Process getByMaxVersion(SqlParam<Process> params) throws Exception {
        String sql = "SELECT process_id,name,display_name,json,version,status FROM opf_process WHERE process_id=$S{processId} ORDER BY version DESC LIMIT 1";
        return super.findRow(Process.class, sql, 0, params.getModel());
    }

    public void insertNew(Process Process) throws Exception {
        String sql = "INSERT INTO opf_process(process_id, name, display_name, version, create_user, json, status) " +
                " VALUES ($S{processId}, $S{name}, $S{displayName}, $I{version}, $S{createUser}, $S{json}, $S{status})";
        super.update(sql, Process);
    }

    public void removeProcess(SqlParam<Process> params) throws Exception {
        String sql = "UPDATE opf_process SET status='0' WHERE process_id = $S{processId} AND version=$I{version}";
        super.update(sql, params.getModel());
    }

    public void updateProcess(SqlParam<Process> params) throws Exception {
        String sql = "update opf_process SET display_name=$S{displayName}, update_user=$S{updateUser}, json=$S{json}" +
                " WHERE process_id=$S{processId} AND version=$I{version}";
        super.update(sql, params.getModel());
    }

    public List<SqlRow> findWfConfigNewDict() throws Exception {
        String sql = "SELECT server itemkey, process_name itemval FROM wf_busi_confignew";
        return super.findRows(sql);
    }

    public List<Process> listAllProcess(Map<String, Object> queryCriteria) throws Exception {
        String sql = "SELECT process_id,name,display_name,json,MAX(version) version,status FROM opf_process WHERE 1=1";
        if (StringHelper.isNotEmptyObj(queryCriteria.get("name"))) {
            sql += " AND name like '%" + queryCriteria.get("name") + "%'";
        }
        if (StringHelper.isNotEmptyObj(queryCriteria.get("displayName"))) {
            sql += " AND display_name like '%" + queryCriteria.get("displayName") + "%'";
        }
        sql += " GROUP BY process_id";
        return super.findRows(Process.class, sql, 0, null);
    }

    public Process getProcessByMaxVersion(String processId) throws Exception {
        String sql = "SELECT process_id,name,display_name,json,version,status FROM opf_process WHERE process_id='" + processId + "' AND status='1'";
        return super.findRow(Process.class, sql, 0, null);
    }

    public Process getProcessById(String processId) throws Exception {
        String sql = "SELECT process_id,name,display_name,json,version,status FROM opf_process WHERE process_id='" + processId + "'";
        return super.findRow(Process.class, sql, 0, null);
    }

    public Process getProcessByVersion(String processId, String version) throws Exception {
        String sql = "SELECT process_id,name,display_name,json,version,status FROM opf_process WHERE process_id='" + processId + "' AND version='"+version+"'";
        return super.findRow(Process.class, sql, 0, null);
    }

    public void deployProcess(Process process) throws Exception {
        super.doTrans(() -> {
            String sql = "UPDATE opf_process SET status='0' WHERE process_id=$S{processId}";
            // 所有流程置为失效
            super.update(sql, process);
            // 最新流程置为生效
            sql = "UPDATE opf_process SET status='1' WHERE process_id=$S{processId} AND version=$I{version}";
            super.update(sql, process);
        });
    }
}
