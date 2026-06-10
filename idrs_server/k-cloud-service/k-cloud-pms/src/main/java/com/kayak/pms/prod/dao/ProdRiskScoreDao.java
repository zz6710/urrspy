package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.pms.prod.model.T8ProdRiskScore;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/1/30 10:58
 */
@Repository
public class ProdRiskScoreDao extends ComnDao {
    public SqlResult<Map<String, Object>> findProdRiskScore(SqlParam<T8ProdRiskScore> params) throws Exception {
        String sql = "select a.id t8_prod_info_id,a.prod_code,a.prod_name,b.prod_risk_level,b.risk_score,IFNULL(b.risk_score_status, '1') risk_score_status, " +
                " b.is_confirm,b.crt_date,b.crt_time,b.upd_date,b.upd_time from t8_prod_info a join t8_prod_risk_score b on a.id = b.t8_prod_info_id where 1=1 and (b.risk_score_status='3' or b.risk_score_status='4') ";
        if (params.getModel().getIsRecycleCode() != null && params.getModel().getIsRecycleCode() != "") {
            if("0".equals(params.getModel().getIsRecycleCode())){
                sql = sql + "and (a.is_recycle_code != '1' or a.is_recycle_code is null )";
            }else{
                sql = sql + " and a.is_recycle_code ='"+params.getModel().getIsRecycleCode()+"'";
            }
        }else{
            sql = sql + "and (a.is_recycle_code != '1' or a.is_recycle_code is null )";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
                  sql += " and a.prod_name like '%" + params.getModel().getProdName() + "%'";
              }
        if (StringUtils.isNotBlank(params.getModel().getT8ProdInfoId())) {
            sql = sql + "and a.id = $S{t8ProdInfoId}";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdCode())) {
            sql = sql + "and a.prod_code = $S{prodCode}";
        }
        if (StringUtils.isNotBlank(params.getModel().getRiskScoreStatus())) {
            sql = sql + "and b.risk_score_status = $S{riskScoreStatus}";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdRiskLevel())) {
            sql = sql + "and b.prod_risk_level = $S{prodRiskLevel}";
        }
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }


    public SqlResult<Map<String, Object>> findProdRiskScoreStart(SqlParam<T8ProdRiskScore> params) throws Exception {
        String sql = "select b.approval_status,a.id,t8_prod_info_id,a.prod_code,a.prod_name,b.prod_risk_level,b.risk_score,IFNULL(b.risk_score_status, '1') risk_score_status, " +
                " b.is_confirm,b.crt_date,b.crt_time,b.upd_date,b.upd_time from t8_prod_info a join t8_prod_risk_score b on a.id = b.t8_prod_info_id where 1=1 and b.risk_score_status!='1'";
        if (params.getModel().getIsRecycleCode() != null && params.getModel().getIsRecycleCode() != "") {
            if("0".equals(params.getModel().getIsRecycleCode())){
                sql = sql + "and (a.is_recycle_code != '1' or a.is_recycle_code is null )";
            }else{
                sql = sql + " and a.is_recycle_code ='"+params.getModel().getIsRecycleCode()+"'";
            }
        }else{
            sql = sql + "and (a.is_recycle_code != '1' or a.is_recycle_code is null )";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdName())) {
                  sql += " and a.prod_name like '%" + params.getModel().getProdName() + "%'";
              }
        if (StringUtils.isNotBlank(params.getModel().getT8ProdInfoId())) {
            sql = sql + "and a.id = $S{t8ProdInfoId}";
        }
        if (StringUtils.isNotBlank(params.getModel().getRiskScoreStatus())) {
            sql = sql + "and b.risk_score_status = $S{riskScoreStatus}";
        }
        if (StringUtils.isNotBlank(params.getModel().getProdRiskLevel())) {
            sql = sql + "and b.prod_risk_level = $S{prodRiskLevel}";
        }
        if (StringUtils.isNotBlank(params.getModel().getRiskLevel())) {
            sql = sql + "and b.prod_risk_level = $S{riskLevel}";
        }
        return SqlUtils.sqlPackage(sql, DataSourceProperty.PUB, params, this);
    }

    public SqlResult<T8ProdRiskScore> findProdRiskScoreByCode(SqlParam<T8ProdRiskScore> param) throws Exception {
        String sql = "select t8_risk_template_version_id as id from t8_prod_risk_rat trr  left join t8_prod_risk_rat tprr on trr.t8_prod_info_id = tprr.t8_prod_info_id left join t8_risk_project trp on tprr.t8_risk_project_id = trp.id left join t8_risk_template_version  trtv on trp.t8_risk_template_version_id = trtv.id  where tprr.t8_prod_info_id=$S{t8ProdInfoId} limit 1,1";
        return super.findRows(sql, param);
    }

    public int addOrUpdateProdRiskScore(T8ProdRiskScore prodRiskScore) throws Exception {
        //存在则更新，不存在则修改
        UpdateResult update = super.update("INSERT INTO t8_prod_risk_score (id, t8_prod_info_id, risk_score, risk_score_status, prod_risk_level,inputuser, " +
                "crt_date, crt_time, upd_date, upd_time,process_instance_id,approval_status) " +
                "VALUES ($AUTOIDS{id}, $S{t8ProdInfoId}, $S{riskScore}, $S{riskScoreStatus},$S{prodRiskLevel},$S{inputuser}," +
                " $S{crtDate}, $S{crtTime},$S{updDate},$S{updTime},$S{processInstanceId},$S{approvalStatus}) on duplicate key update risk_score=$S{riskScore},risk_score_status=$S{riskScoreStatus}, " +
                " prod_risk_level=$S{prodRiskLevel},updateuser=$S{updateuser},upd_date=$S{updDate},upd_time=$S{updTime},process_instance_id=$S{processInstanceId},approval_status=$S{approvalStatus}", prodRiskScore);

        return update.getEffect();
    }

    public int updateriskScoreStatus(SqlParam<T8ProdRiskScore> param) throws Exception {
        return super.update("update t8_prod_risk_score set risk_score_status = $S{riskScoreStatus} where t8_prod_info_id = $S{t8ProdInfoId}", param.getModel()).getEffect();
    }

    public int updateConfirmStatus(SqlParam<T8ProdRiskScore> param) throws Exception {
        //return  super.update("update t8_prod_risk_score set is_confirm = '2',risk_score_status='4' where t8_prod_info_id = $S{t8ProdInfoId}", param.getModel()).getEffect();
        return updateConfirmStatus(param.getModel());
    }

    public int updateConfirmStatus(Object param) throws Exception {
        return super.update("update t8_prod_risk_score set is_confirm = '2',risk_score_status='4' where t8_prod_info_id = $S{t8ProdInfoId}", param).getEffect();
    }

    public int updateprodRiskLevel(SqlParam<T8ProdRiskScore> param) throws Exception {
        //return  super.update("update t8_prod_risk_score set prod_risk_level = $S{prodRiskLevel} where t8_prod_info_id = $S{t8ProdInfoId}", param.getModel()).getEffect();
        return updateprodRiskLevel(param.getModel());
    }

    public int updateprodRiskLevel(Object param) throws Exception {
        return super.update("update t8_prod_risk_score set prod_risk_level = $S{prodRiskLevel} where t8_prod_info_id = $S{t8ProdInfoId}", param).getEffect();
    }

    /**
     * 审批拒绝修改状态
     * @param t8ProdRiskScore
     */
    public void updateApprovalStatus(T8ProdRiskScore t8ProdRiskScore) throws Exception {
        String sql="UPDATE `t8_prod_risk_score` SET approval_status=3 where t8_prod_info_id='"+t8ProdRiskScore.getT8ProdInfoId()+"'";
        super.update(sql);
    }


    /**
     * 审批通过修改状态
     * @param t8ProdRiskScore
     */
    public void riskScoreAgree(T8ProdRiskScore t8ProdRiskScore) throws Exception {
        String sql="UPDATE `t8_prod_risk_score` SET approval_status=2 where t8_prod_info_id='"+t8ProdRiskScore.getT8ProdInfoId()+"'";
        super.update(sql);
    }

/*    *//**
     * 根据流程id查询产品
     * @param processInstanceId
     * @return
     *//*
    public T8ProdRiskScore getRiskScoreReject(String processInstanceId) throws Exception {

               String sql = "SELECT\n" +
                "\tt8_prod_risk_score.ID,\n" +
                "\tt8_prod_risk_score.t8_prod_info_id,\n" +
                "\tt8_prod_risk_score.risk_score,\n" +
                "\tt8_prod_risk_score.prod_risk_level,\n" +
                "\tt8_prod_risk_score.risk_score_status,\n" +
                "\tt8_prod_risk_score.is_confirm,\n" +
                "\tt8_prod_risk_score.inputuser,\n" +
                "\tt8_prod_risk_score.updateuser,\n" +
                "\tt8_prod_risk_score.crt_date,\n" +
                "\tt8_prod_risk_score.crt_time,\n" +
                "\tt8_prod_risk_score.upd_date,\n" +
                "\tt8_prod_risk_score.upd_time,\n" +
                "\tt8_prod_risk_score.process_instance_id,\n" +
                "\tt8_prod_risk_score.approval_status \n" +
                "FROM\n" +
                "\tt8_prod_risk_score \n" +
                "WHERE\n" +
                "\tprocess_instance_id = '" + processInstanceId + "'";
               return super.findRow(T8ProdRiskScore.class,sql,0,null);
    }*/
}
