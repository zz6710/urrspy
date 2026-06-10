package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.model.OpFormParam;
import com.kayak.pms.opFlow.model.OpFormParamRelation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OpFormParamDao extends ComnDao {

    /**
     * 保存表单参数
     * @param params
     * @return
     * @throws Exception
     */
    public void save(SqlParam<OpFormParam> params) throws Exception {
        List<OpFormParam> list = params.getModel().getParams();
        // 开启事务插入，先删除再插入
        super.doTrans(() -> {
            super.update("DELETE FROM op_form_param WHERE form_id=$S{formId}", params.getModel());
            super.update("DELETE FROM op_form_param_relation WHERE form_id=$S{formId}", params.getModel());
            for (OpFormParam opFormParam : list) {
                super.update("INSERT INTO op_form_param(form_id, param_code, param_name, func_type, field_length, field_precision, show_flag, blank_flag, edit_flag, max_value, min_value, default_value, data_way, dict, placeholder, order_no)" +
                        " VALUES($S{formId}, $S{paramCode}, $S{paramName}, $S{funcType}, $S{fieldLength}, $S{fieldPrecision}, $S{showFlag}, $S{blankFlag}, $S{editFlag}, $S{maxValue}, $S{minValue}, $S{defaultValue}, $S{dataWay}, $S{dict}, $S{placeholder}, $S{orderNo})",
                        opFormParam);
                if (opFormParam.getRelations() != null) {
                    for (OpFormParamRelation relation : opFormParam.getRelations()) {
                        super.update("INSERT INTO op_form_param_relation(form_id, param_code, param_value, link_param_code, show_flag, blank_flag, edit_flag, dict, default_value)" +
                                        " VALUES($S{formId}, $S{paramCode}, $S{paramValue}, $S{linkParamCode}, $S{showFlag}, $S{blankFlag}, $S{editFlag}, $S{dict}, $S{defaultValue})",
                                relation);
                    }

                }
            }
        });
    }

    public SqlResult<OpFormParam> findOpFormParamById(SqlParam<OpFormParam> params) throws Exception {
        return super.findRows("SELECT form_id, param_code, param_name, func_type, field_length, field_precision, show_flag, blank_flag, edit_flag, max_value, min_value, default_value, data_way, dict, placeholder, order_no FROM op_form_param WHERE form_id=$S{formId}", params);
    }

    public SqlResult<OpFormParam> findOpFormDataById(SqlParam<OpFormParam> params) throws Exception {
        return super.findRows("SELECT ofp.form_id,ofp.param_code,ofp.func_type,ofd.FIELD_VALUE,ofp.param_name,ofp.dict FROM opf_form_data ofd LEFT JOIN op_form_param ofp ON ofp.param_code = ofd.FIELD_NAME AND ofd.form_id = ofp.form_id\n" +
                "WHERE ofd.PROCESS_INSTANCE_ID = $S{processInstanceId}",params);
    }
}
