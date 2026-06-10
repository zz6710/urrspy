package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * com.kayak.pms.disclosureControl.dao
 * user:rennannan
 * date:2021/6/7 19:22
 * function:
 */
@Repository
public class SaveLogDao extends ComnDao {
    /**
     * 保存日志
     *
     * @param logParam [datatype,logs,start_date,start_time,end_date,end_time,remark]
     * @throws Exception
     */
    public void saveLog(Map<String, Object> logParam) throws Exception {
        try {
            String insLog = "insert into t8_ods_log (id,datatype,logs,start_date,start_time,end_date,end_time,remark) " +
                    "values($AUTOIDS{t8_ods_log},$S{datatype},$S{logs},$S{start_date},$S{start_time},$S{end_date},$S{end_time},$S{remark})";

            super.update(insLog, 0, logParam);//插入日志
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String saveTaskLog(Map<String, Object> logParam) throws Exception {
        String id = "";
        try {
            String insLog = "insert into t8_ods_task_log (id,table_name,select_condition,exec_start_time,exec_end_time,cost_time,task_status,update_date,update_time,remark) " +
                    "values($AUTOIDS{t8_ods_task_log},$S{table_name},$S{select_condition},$S{exec_start_time},$S{exec_end_time},$S{cost_time},$S{task_status},$S{update_date},$S{update_time},$S{remark})";

            id = super.update(insLog, 0, logParam).getAutoId();//插入日志
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void updateTaskLog(Map<String, Object> logParam) throws Exception {
        try {
            String insLog = "update t8_ods_task_log set exec_end_time=$S{exec_end_time},cost_time=$S{cost_time},task_status=$S{task_status},update_date=$S{update_date},update_time=$S{update_time},remark=$S{remark}  where id=$S{id}";

            super.update(insLog, 0, logParam);//插入日志
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
