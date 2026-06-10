package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.opFlow.model.OpFormInfo;
import com.kayak.pms.opFlow.model.OpFormParam;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OpFormInfoDao extends ComnDao {

    public SqlResult<OpFormInfo> find(SqlParam<OpFormInfo> params) throws Exception {
        String sql = "SELECT form_id, form_name, form_type, comp_path FROM op_form_info";
        return super.findRows(sql, params);
    }

    public void add(SqlParam<OpFormInfo> params) throws Exception {
        String sql = "INSERT INTO op_form_info(form_id, form_name, form_type, comp_path, create_user)" +
                " VALUES ($S{formId}, $S{formName}, $S{formType}, $S{compPath}, '"+SysUtil.getLoginUserid()+"')";
        super.update(sql, params.getModel());
    }

    public boolean existByName(SqlParam<OpFormInfo> params, boolean withFormId) throws Exception {
        String sql;
        if (withFormId) {
            sql = "SELECT 1 FROM op_form_info WHERE form_name=$S{formName} AND form_id!=$S{formId} LIMIT 1";
        } else {
            sql = "SELECT 1 FROM op_form_info WHERE form_name=$S{formName} LIMIT 1";
        }
        SqlRow row = super.findRow(sql, params.getModel());
        return row != null;
    }

    public UpdateResult updateOpFormInfo(SqlParam<OpFormInfo> params) throws Exception {
        String sql = "UPDATE op_form_info SET form_name=$S{formName}, form_type=$S{formType}, comp_path=$S{compPath}, update_user='"+ SysUtil.getLoginUserid() +"'" +
                " WHERE form_id=$S{formId}";
        return super.update(sql, params.getModel());
    }

    public UpdateResult delete(SqlParam<OpFormInfo> params) throws Exception {
        return super.update("DELETE FROM op_form_info WHERE form_id=$S{formId}", params.getModel());
    }

    public SqlResult<OpFormInfo> findOpFormParam(SqlParam<OpFormInfo> params) throws Exception {
        List<String> formIdList = params.getModel().getFormIdList();
        String formIdStr = String.join("','", formIdList);
        // 先查出表单信息
        String sql = "SELECT form_id,form_name,form_type,comp_path FROM op_form_info WHERE form_id IN ('"+formIdStr+"')";
        SqlResult<OpFormInfo> result = super.findRows(sql, params);
        // 再查出表单参数
        sql = "SELECT form_id, param_code, param_name, func_type, field_length, field_precision, show_flag, blank_flag, edit_flag, max_value, min_value, default_value, data_way, dict, placeholder, order_no FROM op_form_param WHERE form_id in ('" + formIdStr + "')\n" +
                "ORDER BY order_no";
        List<OpFormParam> paramsResult = super.findRows(OpFormParam.class, sql, 0, params.getModel());
        // 根据表单id对表单参数分组
        Map<String, List<OpFormParam>> paramsResultMap = paramsResult.stream().collect(Collectors.groupingBy(OpFormParam::getFormId));
        // 分组后，塞入对应的表单信息，并返回
        result.getRows().forEach(row -> row.setParams(paramsResultMap.get(row.getFormId())));
        return result;
    }
}
