package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.T82.model.T82001;
import com.kayak.pms.prod.model.DistributorQuota;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2020/12/18 14:36
 */
@Repository
public class DistributorQuotaDao extends ComnDao {
    /**
     * 查询额度列表信息
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<SqlRow> findProdInfo(SqlParam<DistributorQuota> params) throws Exception {
        String sql = "select distinct a.prod_name as prodName,a.prod_code as prodCode from t8_prod_info a " +
                "left join t8_distributor_quota_manage b on a.PROD_CODE=b.PROD_CODE " +
                "where 1=1  ";
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql = sql + " and a.prod_code=$S{prodCode}";
        }
        if (StringUtils.isNotBlank(params.getModel().getDistributorCode())) {
            sql = sql + " and b.distributor_code=$S{distributorCode}";
        }
        //获取总记录数量
        List<SqlRow> rowsCount = super.findRows(sql, params.getModel());

        String limit = "limit " + params.getStart() + "," + params.getLimit();
        sql = sql + limit;
        List<SqlRow> rows = super.findRows(sql, params.getModel());
        SqlResult<SqlRow> sqlRowSqlResult= new SqlResult<>();
        sqlRowSqlResult.setRows(rows);
        sqlRowSqlResult.setResults(rowsCount.size());
        return sqlRowSqlResult;
    }


    public SqlResult<SqlRow> findDistributorQuotaInfo(SqlParam<DistributorQuota> params) throws Exception {
        String count = "select count(*) count FROM t8_distributor_quota_manage a join t8_distributor_info b on a.DISTRIBUTOR_CODE = b.DISTRIBUTOR_CODE where a.prod_code=$S{prodCode}";
        //获取总记录数量
        List<SqlRow> rowsCount = super.findRows(count, params.getModel());
        String sql = "SELECT a.id, a.distributor_code distributorCode,a.quota,a.prod_code prodCode,a.quota_type quotaType,a.remark,a.quota_state,b.dept quotaState,b.dept,b.distributor_type distributorType,b.distributor_name distributorName\n" +
                "FROM t8_distributor_quota_manage a join t8_distributor_info b on a.DISTRIBUTOR_CODE = b.DISTRIBUTOR_CODE join t8_prod_info c on a.PROD_CODE = c.PROD_CODE where a.prod_code=$S{prodCode} ";
        String limit = "limit " + params.getStart() + "," + params.getLimit();
        sql = sql + limit;
        List<SqlRow> rows = super.findRows(sql, params.getModel());
        SqlResult<SqlRow> sqlRowSqlResult= new SqlResult<>();
        sqlRowSqlResult.setRows(rows);
        sqlRowSqlResult.setResults(rowsCount.get(0).getInteger("count"));
        return sqlRowSqlResult;
    }

    public SqlResult<SqlRow> findDistributorQuotaInfoOnApprove(SqlParam<DistributorQuota> params) throws Exception {
        String count = "SELECT count(*) count " +
                "FROM t8_distributor_quota_manage a join t8_distributor_info b on a.DISTRIBUTOR_CODE = b.DISTRIBUTOR_CODE join t8_prod_info c on a.PROD_CODE = c.PROD_CODE where a.prod_code=$S{prodCode}";
        //获取总记录数量
        List<SqlRow> rowsCount = super.findRows(count, params.getModel());
        String sql = "SELECT a.id, a.distributor_code distributorCode,a.quota,a.prod_code prodCode,a.quota_type quotaType,a.remark,a.quota_state,b.dept quotaState,b.dept,b.distributor_type distributorType,b.distributor_name distributorName\n" +
                "FROM t8_distributor_quota_manage a join t8_distributor_info b on a.DISTRIBUTOR_CODE = b.DISTRIBUTOR_CODE join t8_prod_info c on a.PROD_CODE = c.PROD_CODE where a.prod_code=$S{prodCode} ";
        String limit = "limit " + params.getStart() + "," + params.getLimit();
        sql = sql + limit;
        //查询需要展示的数据
        List<SqlRow> rows = super.findRows(sql, params.getModel());
        SqlResult<SqlRow> sqlRowSqlResult= new SqlResult<>();
        sqlRowSqlResult.setRows(rows);
        sqlRowSqlResult.setResults(rowsCount.get(0).getInteger("count"));
        return sqlRowSqlResult;
    }

    public UpdateResult addDistributorQuota(SqlParam<DistributorQuota> params) throws Exception {
        return super.update("INSERT INTO t8_distributor_quota_manage (distributor_code, quota, remark, prod_code,quota_state) VALUES($S{distributorCode},$S{quota},$S{remark},$S{prodCode},'1')",
                params.getModel());
    }

    public UpdateResult updateDistributorQuota(SqlParam<DistributorQuota> params) throws Exception {
        return super.update("update t8_distributor_quota_manage set distributor_code=$S{distributorCode},quota=$S{quota},remark=$S{remark},prod_code=$S{prodCode} where id=$S{id}",
                params.getModel());
    }

    public void deleteDistributorQuota(SqlParam<DistributorQuota> params) throws Exception {
         super.update("delete from t8_distributor_quota_manage where id=$S{id}",
                params.getModel());
    }

    public void updateQuotaState(SqlParam<DistributorQuota> params) throws Exception {
        super.update("update t8_distributor_quota_manage set quota_state='2' where quota_state='1' and prod_code=$S{prodCode} ",
                params.getModel());
    }



    public SqlResult<SqlRow> findQuotaByProdCode(SqlParam<DistributorQuota> params) throws Exception {
        String count = "select count(*) count from t8_distributor_quota_manage a left join t8_distributor_info b on a.distributor_code = b.distributor_code where INSTR($S{prodCode}, a.prod_code) > 0";
        //获取总记录数量
        List<SqlRow> rowsCount = super.findRows(count, params.getModel());
        String sql = "select a.quota,a.distributor_code distributorCode,b.distributor_name distributorName from t8_distributor_quota_manage a left join t8_distributor_info b on a.distributor_code = b.distributor_code where INSTR($S{prodCode}, a.prod_code) > 0 ";
        String limit = "limit " + params.getStart() + "," + params.getLimit();
        sql = sql + limit;
        List<SqlRow> rows = super.findRows(sql, params.getModel());
        SqlResult<SqlRow> sqlRowSqlResult= new SqlResult<>();
        sqlRowSqlResult.setRows(rows);
        sqlRowSqlResult.setResults(rowsCount.get(0).getInteger("count"));
        return sqlRowSqlResult;
    }

}
