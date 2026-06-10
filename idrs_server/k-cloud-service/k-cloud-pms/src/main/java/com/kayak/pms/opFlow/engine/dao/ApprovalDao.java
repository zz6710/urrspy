package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.pms.opFlow.engine.entity.Approval;
import com.kayak.pms.opFlow.engine.entity.vo.ApprovalDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by daniel on 13/04/2017.
 */
@Repository
public class ApprovalDao extends ComnDao {
    public void saveApproval(Approval approval) throws Exception {
        String sql = "INSERT INTO opf_approval(id, result, opinion, process_instance_id, task_id, operator, create_date, create_time, type, process_id, modified_data, result_text, submit_params_id)" +
                " VALUES ($S{id}, $S{result}, $S{opinion}, $S{processInstanceId}, $S{taskId}, $S{operator}, $S{createDate}, $S{createTime}, $S{type}, $S{processId}, $S{modifiedData}, $S{resultText}, $S{submitParamsId})";
        super.update(sql, approval);
    }

    public ApprovalDetail get(SqlParam<Approval> param) throws Exception {
        String sql = "SELECT " +
                "          wf.display_name AS processDisplayName," +
                "          wht.display_name AS taskDisplayName," +
                "          wa.opinion," +
                "          wa.result," +
                "          wa.result_text," +
                "          wa.create_date AS approvalDate," +
                "          wa.create_time AS approvalTime" +
                "      FROM opf_approval wa" +
                "      LEFT JOIN opf_his_task wht ON  wht.id = wa.task_id" +
                "      LEFT JOIN opf_process_instance wpi ON  wpi.id = wa.process_instance_id" +
                "      LEFT JOIN opf_process wf ON  wf.id = wpi.process_id" +
                "      WHERE wa.id = $S{id}";
        return super.findRow(ApprovalDetail.class, sql, 0, param.getModel());
    }

    public List<Approval> getDynamicModifiedDetail(SqlParam<Approval> param) throws Exception {
        String sql = "SELECT" +
                "        su.username AS operator," +
                "        modified_data," +
                "        wht.display_name AS taskDisplayName," +
                "        wht.name AS taskName" +
                "        FROM wf_approval wa" +
                "        LEFT JOIN sys_user su ON su.userid = wa.operator" +
                "        LEFT JOIN wf_his_task wht ON  wht.id = wa.task_id" +
                "        WHERE wa.process_instance_id = $S{processInstanceId}" +
                "        AND modified_data IS NOT NULL" +
                "        ORDER BY wa.create_date ASC , wa.create_time ASC";
        return super.findRows(Approval.class, sql, 0, param.getModel());
    }
}
