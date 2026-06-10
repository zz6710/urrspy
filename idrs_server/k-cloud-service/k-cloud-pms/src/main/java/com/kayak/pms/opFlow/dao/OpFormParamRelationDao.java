package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.model.OpFormParamRelation;
import org.springframework.stereotype.Repository;

@Repository
public class OpFormParamRelationDao extends ComnDao {

    /**
     * 查询表单参数联动关系
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<OpFormParamRelation> findOpFormParamRelations(SqlParam<OpFormParamRelation> params) throws Exception {
        return super.findRows("SELECT form_id, param_code,param_value,link_param_code,show_flag,blank_flag,edit_flag,dict,default_value FROM op_form_param_relation WHERE form_id=$S{formId}", params);
    }

    public SqlResult<OpFormParamRelation> findRelationsByFormId(SqlParam<OpFormParamRelation> params) throws Exception {
        String formIds = String.join("','", params.getModel().getFormId().split(","));
        return super.findRows("SELECT form_id, param_code,param_value,link_param_code,show_flag,blank_flag,edit_flag,dict,default_value FROM op_form_param_relation WHERE form_id in ('"+formIds+"')", params);
    }
}
