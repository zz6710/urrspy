package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.opFlow.model.OpBusiForm;
import org.springframework.stereotype.Repository;

@Repository
public class OpBusiFormDao extends ComnDao {

    public void save(SqlParam<OpBusiForm> params) throws Exception {
        super.doTrans(() -> {
            // 先删除
            super.update("DELETE FROM op_busi_form WHERE busi_id=$S{busiId}", params.getModel());

            // 再插入
            String sql = "INSERT INTO op_busi_form(busi_id, form_id, order_no)" +
                    " VALUES ($S{busiId}, $S{formId}, $I{orderNo})";
            for (OpBusiForm opBusiForm : params.getModel().getList()) {
                super.update(sql, opBusiForm);
            }
        });
    }

    public SqlResult<OpBusiForm> find(SqlParam<OpBusiForm> params) throws Exception {
        String sql = "SELECT busi_id, form_id, order_no FROM op_busi_form WHERE busi_id=$S{busiId} ORDER BY order_no";
        return super.findRows(sql, params);
    }
}
