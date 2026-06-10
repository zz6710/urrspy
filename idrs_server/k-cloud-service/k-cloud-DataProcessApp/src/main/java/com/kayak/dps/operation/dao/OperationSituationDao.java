package com.kayak.dps.operation.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.operation.model.OperationSituation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class OperationSituationDao extends ComnDao {

    public SqlResult<OperationSituation> findProdInfo(SqlParam<OperationSituation> params) throws Exception {
        return super.findRows("select t.prod_cd prod_code, t.prod_nm prod_name, t.prod_cd value, t.prod_cd label " +
                "from app_prd_bas_inf t ", params);
    }

    //获取当前日期之前的最大日期
    public String getAssetPositionsTradeDate(String sysDate) throws Exception {

        List<SqlRow> l = super.findRows("select max(trade_date) trade_date from app_operation_asset_positions " +
                "where trade_date < $S{sysDate} ", sysDate);

        if (l.size() <= 0)
            return sysDate;

        return l.get(0).getString("trade_date");
    }

    //获取当日总额
    public BigDecimal getAssetPositionsTotal(OperationSituation o) throws Exception {

        String sql = "select ifnull(sum(t.position_amt),0) sum from app_operation_asset_positions t " +
                "where t.trade_date = $S{tradeDate} ";

        if (StringUtils.isNotBlank(o.getProdCode())){
            sql += " and t.prod_code = $S{prodCode} ";
        }

        List<SqlRow> l = super.findRows(sql, o);

        return new BigDecimal(l.get(0).getString("sum"));
    }

    public SqlResult<OperationSituation> findAssetPositions(SqlParam<OperationSituation> params) throws Exception {

        OperationSituation o = params.getModel();

        StringBuffer sql = new StringBuffer("select t.asset_code,t.asset_name,t.trade_date," +
                "sum(t.position_vol) position_vol ,sum(t.position_amt) position_amt, " +
                "t.asset_code name, format(sum(t.position_vol)/100000000 , 2) amount " +
                "from app_operation_asset_positions t where 1 = 1 ");
        if (StringUtils.isNotBlank(o.getProdCode())){
            sql.append(" and t.prod_code = $S{prodCode} ");
        }
        if (StringUtils.isNotBlank(o.getTradeDate())){
            sql.append(" and t.trade_date = $S{tradeDate} ");
        }
        sql.append(" group by t.asset_code,t.asset_name,t.trade_date order by t.asset_code ");
        return super.findRows(sql.toString(), params);
    }





    /**   产品规模   **/
    //获取查询日期
    public String getProdScaleTradeDate(String sysDate) throws Exception {

        List<SqlRow> l = super.findRows("select max(trade_date) trade_date from app_operation_prod_scale " +
                "where trade_date <= $S{sysDate} ", sysDate);

        if (l.size() <= 0)
            return sysDate;

        return l.get(0).getString("trade_date");
    }
    //查询总规模
    public SqlResult<OperationSituation> findProdScaleTotal(SqlParam<OperationSituation> params) throws Exception {

        String sql = "select ifnull(sum(t.existing_scale),0) existing_scale , ifnull(sum(t.history_scale),0) history_scale from app_operation_prod_scale t " +
                "where t.trade_date = $S{tradeDate} ";

        return super.findRows(sql, params);
    }

    //查询规模详情
    public SqlResult<OperationSituation> findProdScaleDetails(SqlParam<OperationSituation> params) throws Exception {
        OperationSituation o = params.getModel();

        String sql = "select t.id, t.prod_code, t.prod_name, t.prod_code_sub, t.trade_date, t.existing_scale, t.history_scale, t.crt_date " +
                "from app_operation_prod_scale t where 1 = 1 ";

        if (StringUtils.isNotBlank(o.getProdCode())){
            sql += " and t.prod_code = $S{prodCode} ";
        }

        return super.findRows(sql, params);
    }

    /**   机构持有量   **/
    public String getOrgHoldTradeDate(String sysDate) throws Exception {

        List<SqlRow> l = super.findRows("select max(trade_date) trade_date from app_operation_org_hold " +
                "where trade_date <= $S{sysDate} ", sysDate);

        if (l.size() <= 0)
            return sysDate;

        return l.get(0).getString("trade_date");
    }

    //机构下拉框
    public SqlResult<OperationSituation> findOrgDict(SqlParam<OperationSituation> params) throws Exception {
        String sql = "select distinct t.org_name, t.org_code " +
                "from app_operation_org_hold t " +
                "where t.trade_date = $S{tradeDate} " +
                "order by t.org_name";

        return super.findRows(sql, params);
    }


    //查询机构持有量
    public SqlResult<OperationSituation> findOrgHold(SqlParam<OperationSituation> params) throws Exception {

        String sql = "select t.org_name, t.org_code, t.trade_date, sum(t.hold_scale) hold_scale " +
                "from app_operation_org_hold t " +
                "where t.trade_date = $S{tradeDate} " +
                "group by t.org_name, t.org_code, t.trade_date " +
                "order by t.org_name";

        return super.findRows(sql, params);
    }
    public SqlResult<OperationSituation> findOrgHoldDetails(SqlParam<OperationSituation> params) throws Exception {
        OperationSituation o = params.getModel();

        String sql = "select t.org_name, t.prod_code, t.org_code, t.trade_date, t.hold_scale " +
                "from app_operation_org_hold t " +
                "where t.trade_date = $S{tradeDate} " ;

        if (StringUtils.isNotBlank(o.getProdCode())){
            sql += " and t.prod_code = $S{prodCode} ";
        }
        if (StringUtils.isNotBlank(o.getOrgCode())){
            sql += " and t.org_code = $S{orgCode} ";
        }
        sql += "order by t.org_name";
        return super.findRows(sql, params);
    }

    //行业下拉框
    public SqlResult<OperationSituation> findIndustryDict(SqlParam<OperationSituation> params) throws Exception {
        String sql = "select distinct t.industry from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "order by t.industry";

        return super.findRows(sql, params);
    }
    //评级下拉框
    public SqlResult<OperationSituation> findGradeDict(SqlParam<OperationSituation> params) throws Exception {
        String sql = "select distinct t.grade from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "order by t.grade";

        return super.findRows(sql, params);
    }
    //资产类型下拉框
    public SqlResult<OperationSituation> findTypeDict(SqlParam<OperationSituation> params) throws Exception {
        String sql = "select distinct t.asset_type from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "order by t.asset_type";

        return super.findRows(sql, params);
    }

    //查询债券分布情况-行业
    public SqlResult<OperationSituation> findAssetIndustry(SqlParam<OperationSituation> params) throws Exception {

        String sql = "select t.industry label, sum(t.pierce_before) pierce_before, sum(t.pierce_later) pierce_later " +
                "from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "group by t.industry " +
                "order by t.industry";

        return super.findRows(sql, params);
    }
    public String getAssetGradeTradeDate(String sysDate) throws Exception {

        List<SqlRow> l = super.findRows("select max(trade_date) trade_date from app_operation_asset_distribute " +
                "where trade_date <= $S{sysDate} ", sysDate);

        if (l.size() <= 0)
            return sysDate;

        return l.get(0).getString("trade_date");
    }
    //查询债券分布情况-评级
    public SqlResult<OperationSituation> findAssetGrade(SqlParam<OperationSituation> params) throws Exception {

        String sql = "select t.grade label, sum(t.pierce_before) pierce_before, sum(t.pierce_later) pierce_later " +
                "from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "group by t.grade " +
                "order by t.grade";

        return super.findRows(sql, params);
    }
    //查询债券分布情况-类型
    public SqlResult<OperationSituation> findAssetType(SqlParam<OperationSituation> params) throws Exception {

        String sql = "select t.asset_type label, sum(t.pierce_before) pierce_before, sum(t.pierce_later) pierce_later " +
                "from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " +
                "group by t.asset_type " +
                "order by t.asset_type";

        return super.findRows(sql, params);
    }

    //查询债券分布情况-详情
    public SqlResult<OperationSituation> findAssetDetails(SqlParam<OperationSituation> params) throws Exception {
        OperationSituation o = params.getModel();

        String sql = "select t.asset_code, t.grade, t.industry, t.asset_type, t.pierce_before, t.pierce_later, t.trade_date " +
                "from app_operation_asset_distribute t " +
                "where t.trade_date = $S{tradeDate} " ;

        if (StringUtils.isNotBlank(o.getAssetCode())){
            sql += " and t.asset_code = $S{assetCode} ";
        }
        if (StringUtils.isNotBlank(o.getGrade())){
            sql += " and t.grade = $S{grade} ";
        }
        if (StringUtils.isNotBlank(o.getIndustry())){
            sql += " and t.industry = $S{industry} ";
        }
        if (StringUtils.isNotBlank(o.getAssetType())){
            sql += " and t.asset_type = $S{assetType} ";
        }
        sql += "order by t.asset_code";

        return super.findRows(sql, params);
    }

}
