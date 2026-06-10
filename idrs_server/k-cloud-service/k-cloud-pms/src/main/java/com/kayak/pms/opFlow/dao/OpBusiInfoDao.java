package com.kayak.pms.opFlow.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.opFlow.model.OpBusiInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OpBusiInfoDao extends ComnDao {

    public SqlResult<OpBusiInfo> find(SqlParam<OpBusiInfo> params) throws Exception {
        params.setMakeSql(true);
        String sql = "SELECT busi_id, busi_name, upper_id, icon_class FROM op_busi_info";
        return super.findRows(sql, params);
    }

    public void add(SqlParam<OpBusiInfo> params) throws Exception {
        String sql = "INSERT INTO op_busi_info(busi_id, busi_name, upper_id, icon_class, create_user)" +
                " VALUES ($S{busiId}, $S{busiName}, $S{upperId}, $S{iconClass}, '"+SysUtil.getLoginUserid()+"')";
        super.update(sql, params.getModel());
    }

    public boolean existByName(SqlParam<OpBusiInfo> params, boolean withBusiId) throws Exception {
        String sql;
        if (withBusiId) {
            sql = "SELECT 1 FROM op_busi_info WHERE busi_name=$S{busiName} AND busi_id!=$S{busiId} LIMIT 1";
        } else {
            sql = "SELECT 1 FROM op_busi_info WHERE busi_name=$S{busiName} LIMIT 1";
        }
        SqlRow row = super.findRow(sql, params.getModel());
        return row != null;
    }

    public UpdateResult updateOpBusiInfo(SqlParam<OpBusiInfo> params) throws Exception {
        String sql = "UPDATE op_busi_info SET busi_name=$S{busiName}, upper_id=$S{upperId}, icon_class=$S{iconClass}, update_user='"+ SysUtil.getLoginUserid() +"'" +
                " WHERE busi_id=$S{busiId}";
        return super.update(sql, params.getModel());
    }

    public void delete(SqlParam<OpBusiInfo> params) throws Exception {
        super.doTrans(() -> {
            // 删除功能信息表
            super.update("DELETE FROM op_busi_info WHERE busi_id=$S{busiId}", params.getModel());
            // 删除功能表单表
            super.update("DELETE FROM op_busi_form WHERE busi_id=$S{busiId}", params.getModel());
        });
    }

    public SqlResult<OpBusiInfo> findAll(SqlParam<OpBusiInfo> params) throws Exception {
        String sql = "SELECT busi_id, busi_name FROM op_busi_info";
        // 选上级功能时，排除本身
        if (StringUtils.isNotEmpty(params.getModel().getBusiId())) {
            sql += " WHERE busi_id!=$S{busiId}";
        }
        return super.findRows(sql, params);
    }
}
