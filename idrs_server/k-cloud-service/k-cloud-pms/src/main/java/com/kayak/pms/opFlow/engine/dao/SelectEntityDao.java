package com.kayak.pms.opFlow.engine.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import org.springframework.stereotype.Repository;

@Repository
public class SelectEntityDao extends ComnDao {

    public SqlResult<SelectEntity> listActors(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT userid AS value, username AS label FROM sys_user", param);
    }

    public SqlResult<SelectEntity> listRoles(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT roleid AS value, rolename AS label FROM sys_role", param);
    }

    public SqlResult<SelectEntity> listDicts(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT dict AS value, dictname AS label FROM sys_dict", param);
    }

    public SqlResult<SelectEntity> listUsers(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT userid AS value, username AS label FROM sys_user", param);
    }

    public SqlResult<SelectEntity> listButtons(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT buttonid AS value, buttonid AS label FROM sys_button_access", param);
    }

    public SqlResult<SelectEntity> listProcess(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT process_id AS value, display_name AS label FROM opf_process", param);
    }

    public SqlResult<SelectEntity> listProcessConfig(SqlParam<SelectEntity> param) throws Exception {
        return super.findRows("SELECT wbc.server AS value,wbc.process_name AS label FROM wf_busi_confignew wbc WHERE wbc.status = '1'",param);
    }
}
