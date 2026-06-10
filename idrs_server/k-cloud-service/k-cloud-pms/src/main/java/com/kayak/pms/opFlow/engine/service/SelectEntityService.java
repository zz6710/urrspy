package com.kayak.pms.opFlow.engine.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.engine.dao.SelectEntityDao;
import com.kayak.pms.opFlow.engine.entity.SelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "下拉数据源", model = SelectEntity.class)
public class SelectEntityService {
    @Autowired
    SelectEntityDao selectEntityDao;

    @API(desc = "参与者", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listActors(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listActors(param);
    }

    @API(desc = "角色", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listRoles(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listRoles(param);
    }

    @API(desc = "数据字典", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listDicts(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listDicts(param);
    }

    @API(desc = "用户", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listUsers(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listUsers(param);
    }

    @API(desc = "按钮", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listButtons(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listButtons(param);
    }

    @API(desc = "流程", auth = APIAuth.NO)
    public SqlResult<SelectEntity> listProcess(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listProcess(param);
    }

    @API(desc = "审批流配置",auth = APIAuth.NO)
    public SqlResult<SelectEntity> listProcessConfig(SqlParam<SelectEntity> param) throws Exception {
        return selectEntityDao.listProcessConfig(param);
    }
}
