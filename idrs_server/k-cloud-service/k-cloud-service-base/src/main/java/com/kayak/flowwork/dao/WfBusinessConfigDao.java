package com.kayak.flowwork.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.flowwork.model.WfBusinessConfig;
import org.springframework.stereotype.Repository;

@Repository
public class WfBusinessConfigDao extends ComnDao {

    public SqlResult<WfBusinessConfig> find(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.findRows("SELECT DISTINCT bc.status,bc.app_display,bc.server,bc.process_name,bc.bus_keys,bc.table_name," +
                " p.display_name AS process_display_name,sm.name AS server_name" +
                " FROM wf_busi_config bc" +
                " LEFT JOIN wf_process p ON p.name = bc.process_name" +
                " LEFT JOIN sys_server_method sm ON sm.server = bc.server", params);
    }

    public int add(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.update("INSERT INTO wf_busi_config (server, process_name, bus_keys, table_name, app_display)" +
                " VALUES" +
                " ($S{server}, $S{processName}, $S{busKeys}, $S{tableName}, $S{appDisplay})", params.getModel()).getEffect();
    }

    public int edit(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.update("UPDATE wf_busi_config SET " +
                " process_name = $S{processName}, " +
                " bus_keys = $S{busKeys}, " +
                " app_display = $S{appDisplay}, " +
                " table_name = $S{tableName} " +
                " WHERE server = $S{server}", params.getModel()).getEffect();
    }

    public int delete(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.update("DELETE FROM wf_busi_config WHERE server = $S{server}", params.getModel()).getEffect();
    }
    public int turnDown(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.update("update wf_busi_config set status ='0' WHERE server = $S{server}", params.getModel()).getEffect();
    }
    public int turnOn(SqlParam<WfBusinessConfig> params) throws Exception {
        return super.update("update wf_busi_config set status ='1' WHERE server = $S{server}", params.getModel()).getEffect();
    }

}
