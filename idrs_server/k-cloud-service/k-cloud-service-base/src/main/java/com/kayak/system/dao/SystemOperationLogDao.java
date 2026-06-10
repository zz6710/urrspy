package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.system.model.SystemOperationLog;
import org.springframework.stereotype.Repository;

@Repository
public class SystemOperationLogDao extends ComnDao {


    public SqlResult<SystemOperationLog> find(SqlParam<SystemOperationLog> params) throws Exception {
        return super.findRows("SELECT t.id, t.server_desc, t.method_desc, t.submit_old_data, t.submit_data, t.result, t.error_msg, t.operation_date, t.operation_time, t2.username FROM sys_operation_log t LEFT JOIN sys_user t2 ON t.userid = t2.userid where t.server not in('LogBackupService','ScheduleNavService','T8OdsSyncSetService','DisclosureNoticeChannelService') ORDER BY t.operation_date desc, t.operation_time desc", params);
    }
}
