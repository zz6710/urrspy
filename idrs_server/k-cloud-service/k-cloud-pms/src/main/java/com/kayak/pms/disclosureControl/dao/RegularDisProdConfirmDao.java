package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.disclosureControl.model.RegularDisProdConfirm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * com.kayak.pms.disclosureControl.dao
 * user:rennannan
 * date:2021/5/20 17:12
 * function:
 */
@Repository
public class RegularDisProdConfirmDao extends ComnDao {

    /**
     * 功能：查询定期报告产品披露列表
     * 作者：rennannan
     * 日期：20210520
     *
     * @param param
     * @return
     * @throws Exception
     */
    public SqlResult<RegularDisProdConfirm> findRegularDisProdConfirm(SqlParam<RegularDisProdConfirm> param) throws Exception {
        StringBuffer sql = new StringBuffer("select * from (SELECT \n" +
                "notice.prod_code,\n" +
                "prod.prod_nm prod_name,\n" +
                "notice.id notice_id,\n" +
                "notVer.disclosure_mod_version_id,\n" +
                "notVer.notice_version,\n" +
                "modVer.doc_name mod_doc_name,\n" +
                "modVer.version mod_version_number,\n" +
                "notice.disclosure_type,\n" +
                "notice.disclosure_son_type,\n" +
                "notice.notice_title,\n" +
                "notice.prod_base_date,\n" +
                "notice.prod_base_date report_date,\n" +
                "prod.FOUND_DT establish_date,\n" +
                "prod.MTU_DT end_date,\n" +
                "LEFT(notice.prod_base_date,6) task_month," +
                "(select  count(*) from sys_workday_set sws  where workday between prod.FOUND_DT and notice.prod_base_date) to_establish_date_days,"+
                "(select  count(*) from sys_workday_set sws  where workday between notice.prod_base_date and MTU_DT) to_end_date_days,"+
//                "DATEDIFF(STR_TO_DATE(notice.prod_base_date,'%Y%m%d'),STR_TO_DATE(prod.FOUND_DT,'%Y%m%d')) to_establish_date_days,\n" +
//                "DATEDIFF(STR_TO_DATE(prod.MTU_DT,'%Y%m%d'),STR_TO_DATE(notice.prod_base_date,'%Y%m%d')) to_end_date_days,\n" +
                "prod.prod_inv_typ,prod.PROD_CLC_MTH,prod.PROD_FORM,prod.INV_PRD_DIME,prod.PROD_OBJ,prod.PROD_SER_CD\n" +
                "FROM \n" +
                "(SELECT MAX(CONVERT(t.id,SIGNED)) id \n" +
                "FROM \n" +
                "idb_disclosure_notice_version t \n" +
                "GROUP BY t.t8_disclosure_notice_id ) t\n" +
                "LEFT JOIN idb_disclosure_notice_version notVer \n" +
                "ON t.id = notVer.id\n" +
                "LEFT JOIN idb_disclosure_mod_version modVer\n" +
                "ON modVer.id = notVer.disclosure_mod_version_id\n" +
                "LEFT JOIN idb_disclosure_notice notice \n" +
                "ON notice.id = notVer.t8_disclosure_notice_id \n" +
                "LEFT JOIN app_prd_bas_inf prod \n" +
                "ON prod.prod_cd = notice.prod_code\n" +
                "WHERE notice.disclosure_type = '5' ) report where 1=1 \n");
        if (StringUtils.isNotEmpty(param.getModel().getStartMonth())) {
            sql.append(" and report.task_month >= $S{startMonth}\n");
        }
        if (StringUtils.isNotEmpty(param.getModel().getEndMonth())) {
            sql.append(" and report.task_month <= $S{endMonth}\n");
        }
        if (StringUtils.isNotEmpty(param.getModel().getProdCode())) {
            sql.append(" and report.prod_code=$S{prodCode}\n");
        }
        if (StringUtils.isNotEmpty(param.getModel().getProdName())) {
            sql.append(" and report.prod_name like '%$U{prodName}%'\n");
        }

        if (StringUtils.isNotBlank(param.getModel().getProdClcMth())) {
            sql.append(" and report.PROD_CLC_MTH=$S{prodClcMth}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getInvPrdDime())) {
            sql.append(" and report.INV_PRD_DIME=$S{invPrdDime}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getInvPrdLen())) {
            sql.append(" and report.INV_PRD_LEN=$S{invPrdLen}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getProdInvTyp())) {
            sql.append(" and report.prod_inv_typ=$S{prodInvTyp}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getProdObj())) {
            sql.append(" and report.PROD_OBJ=$S{prodObj}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getProdSerCd())) {
            sql.append(" and report.PROD_SER_CD=$S{prodSerCd}\n");
        }
        if (StringUtils.isNotBlank(param.getModel().getProdForm())) {
            sql.append(" and report.PROD_FORM=$S{prodForm}\n");
        }
//        if (StringUtils.isNotEmpty(param.getModel().getStatus())) {
//            sql.append(" and task.status=$S{status}");
//        }
        //信披子类型
        if (StringUtils.isNotEmpty(param.getModel().getDisclosureSonType())) {
            sql.append(" and report.disclosure_son_type=$S{disclosureSonType}\n");
        }
        return super.findRows(sql.toString(),
                DataSourceProperty.IDB, param);
    }

    /**
     * 功能：查询产品对应的投资经理
     * 作者：rennannan
     * 日期：20210615
     *
     * @param t8ProdInfoId
     * @return
     */
    public SqlRow findInvestMan(String t8ProdInfoId) throws Exception {
        String sql = "SELECT GROUP_CONCAT(cust_name) invest_man from ods_amng_cust_info where FIND_IN_SET(jobno,(select GROUP_CONCAT(new_invest_id) from t8_prod_invest_manager where t8_prod_info_id=$S{t8ProdInfoId} and status ='0'))";
        return super.findRow(sql, t8ProdInfoId);
    }

    /**
     * 功能：查询产品对应的估值核算
     * 作者：xiayi
     * 日期：20211011
     *
     * @param t8ProdInfoId
     * @return
     */
    public SqlRow findvaluationAccountingId(String t8ProdInfoId) throws Exception {
        String sql = "select GROUP_CONCAT(username) valuation_accounting_id from  sys_user su " +
                "where FIND_IN_SET(userid,(select GROUP_CONCAT(userid_a) from t8_prod_user where t8_prod_info_id = '"+t8ProdInfoId+"' and role_id ='9'))";
        return super.findRow(sql, t8ProdInfoId);
    }

    /**
     * 功能：修改信披任务信息
     * 作者：rennannan
     * 日期：20210522
     *
     * @param task
     * @return
     * @throws Exception
     */
    public int updateRegularDisProdConfirm(RegularDisProdConfirm task) throws Exception {
        String sql = "update idb_disclosure_prod_task " +
                "      set invest_approval_result = $S{investApprovalResult}," +
                "  invest_approval_date=$S{investApprovalDate}," +
                "  invest_approval_time=$S{investApprovalTime}," +
                "  invest_approval_user_id=$S{investApprovalUserId}," +
                "  invest_approval_user_name=$S{investApprovalUserName}," +
                "  disclosure_approval_result=$S{disclosureApprovalResult}," +
                "  disclosure_approval_date=$S{disclosureApprovalDate}," +
                "  disclosure_approval_time=$S{disclosureApprovalTime}," +
                "  disclosure_user_id=$S{disclosureUserId}," +
                "  disclosure_user_name=$S{disclosureUserName}" +
                "    where id=$S{disProdTaskId}";
        return super.update(sql, task).getEffect();
    }
}
