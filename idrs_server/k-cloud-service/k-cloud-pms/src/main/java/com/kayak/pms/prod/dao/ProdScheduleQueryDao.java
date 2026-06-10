package com.kayak.pms.prod.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.pms.prod.model.ProdScheduleQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * com.kayak.pms.prod.dao
 * user:rennannan
 * date:2021/3/16 17:30
 * function:产品进度查询dao
 */
@Repository
public class ProdScheduleQueryDao extends ComnDao {
    public SqlResult<ProdScheduleQuery> findProdScheduleList(SqlParam<ProdScheduleQuery> schedule) throws Exception {
        StringBuffer sql= new StringBuffer("\t select prodInfo.prod_code,status.itemval prod_status,prodInfo.prod_name,\n" +
                "\t        prodInfo.apply_regist_date apply_confirm,prodInfo.issue_regist_date issue_confirm,\n" +
                "\t        (select group_concat(sysUser.username) from  t8_prod_user users join sys_user sysUser on users.userid_a = sysUser.userid where users.t8_prod_info_id=prodInfo.id and users.role_id='3') prod_manage_name,scheMeet.business_date meet_date,meetParam.business_date meet_param_confirm_Date,\n" +
                "\t\t\t\t\tapplyRegistParam.business_date apply_param_confirm_date,\n" +
                "\t\t\t\t\tmanualLaw.business_date manual_law_examine_date, " +
                "          manualFinalize.business_date manual_finalize_date," +
                "\t\t\t\t\tissueParam.business_date issue_param_confirm_date," +
                "          registDocLaw.business_date apply_regist_doc_law," +
                "          paramFinalize.business_date param_finalize \t\n" +
                "     from\n" +
                "\t\t\t\t(\n" +
                "\t\t\t\tselect prod.prod_code,prod.is_recycle_code,prod.prod_status,prod.id,prod.prod_name,prod.apply_regist_date,prod.issue_regist_date\n" +
                "\t\t\t\t\tfrom t8_prod_info prod \n" +
                "\t\t\t\t where 1=1 \n");
        if (StringUtils.isNotBlank(schedule.getModel().getProdName())) {
                    sql.append(" and prod.prod_name like '%" + schedule.getModel().getProdName() + "%'");
                }
        if (StringUtils.isNotEmpty(schedule.getModel().getProdCode())) {
            sql.append(" and prod.prod_code = $S{prodCode}");
        }
        //产品经理id
        if (StringUtils.isNotEmpty(schedule.getModel().getProdManageId())) {
            sql.append(" and exists( select 1 from t8_prod_user prodUser where prodUser.t8_prod_info_id = prod.id and prodUser.role_id='3' and userid_a=$S{prodManageId})");
        }
        //当前阶段
        if (StringUtils.isNotEmpty(schedule.getModel().getProdStatus())) {
            sql.append(" and prod.prod_status=$S{prodStatus}");
        }
        //一次报备开始时间
        if (StringUtils.isNotEmpty(schedule.getModel().getFirstStartDate())) {
            sql.append(" and prod.apply_regist_date >=$S{firstStartDate}");
        }
        //一次报备结束时间
        if (StringUtils.isNotEmpty(schedule.getModel().getFirstEndDate())) {
            sql.append(" and prod.apply_regist_date <=$S{firstEndDate}");
        }
        //二次报备开始时间
        if (StringUtils.isNotEmpty(schedule.getModel().getSecondStartDate())) {
            sql.append(" and prod.issue_regist_date >=$S{secondStartDate}");
        }
        //二次报备结束时间
        if (StringUtils.isNotEmpty(schedule.getModel().getSecondEndDate())) {
            sql.append(" and prod.issue_regist_date <=$S{secondEndDate}");
        }
        String sql2 =
                "\t\t\t\t ) prodInfo\n" +
                        "left join t8_prod_schedule_info scheMeet\n" +
                        "       on prodInfo.id = scheMeet.t8_prod_info_id\n" +
                        "\t\t\tand scheMeet.node_id='01'\n" +
                        "left join t8_prod_schedule_info meetParam\t\t\n" +
                        "       on prodInfo.id = meetParam.t8_prod_info_id\n" +
                        "\t\t  and meetParam.node_id='02'\n" +
                        "left join t8_prod_schedule_info applyRegistParam\n" +
                        "       on prodInfo.id= applyRegistParam.t8_prod_info_id\n" +
                "\t\t  and applyRegistParam.node_id='03'\n" +
                "left join t8_prod_schedule_info manualLaw\n" +
                "       on prodInfo.prod_code = manualLaw.prod_code\n" +
                "\t\t  and manualLaw.node_id = '04'\n" +
                        "left join t8_prod_schedule_info manualFinalize\n" +
                        "       on prodInfo.prod_code = manualFinalize.prod_code\n" +
                        "\t\t  and manualFinalize.node_id = '05'\n" +
                        "left join t8_prod_schedule_info registDocLaw\n" +
                        "       on prodInfo.id= registDocLaw.t8_prod_info_id\n" +
                        "\t\t  and registDocLaw.node_id='06' \n" +
                        "left join t8_prod_schedule_info issueParam\t\t\n" +
                        "       on prodInfo.id = issueParam.t8_prod_info_id\n" +
                        "\t\t  and issueParam.node_id='08'\n" +
                        "left join t8_prod_schedule_info paramFinalize\n" +
                        "       on prodInfo.prod_code = paramFinalize.prod_code\n" +
                        "\t\t  and paramFinalize.node_id='10'" +
                        "left join sys_dict_item status" +
                        "       on prodInfo.prod_status = status.itemkey" +
                        "      and status.dict = 't8_prod_status' where 1=1";
                sql.append(sql2);
        if (schedule.getModel().getIsRecycleCode() != null && schedule.getModel().getIsRecycleCode() != "") {
            if("0".equals(schedule.getModel().getIsRecycleCode())){
                sql.append(" and (prodInfo.is_recycle_code != '1' or prodInfo.is_recycle_code is null ) ");
            }else{
                sql.append(" and prodInfo.is_recycle_code ='"+schedule.getModel().getIsRecycleCode()+"' ");
            }
        }else{
            sql.append(" and (prodInfo.is_recycle_code != '1' or prodInfo.is_recycle_code is null ) ");
        }
        return super.findRows(sql.toString(),schedule);
    }
}
