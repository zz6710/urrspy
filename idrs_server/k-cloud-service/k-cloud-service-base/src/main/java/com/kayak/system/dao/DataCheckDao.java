package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.system.model.DataCheckModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;


@Repository
public class DataCheckDao extends ComnDao {
    //基础数据补录查询
    public SqlResult<DataCheckModel> findSubmitRemind(SqlParam<DataCheckModel> params) throws Exception {
        String sql = "select '1' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_sht_nm as scr_nm from ods_bond_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code and k.trx_mkt=k1.trx_mkt " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='2' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.pbnk_industry_issuer is null or k.isu_org_typ_siz is null and k.isu_mth is null) " +
                "union all " +
                "select '2' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_fnd_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code and k.trx_mkt=(case when k1.trx_mkt='3' then '6' else k1.trx_mkt end) " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='4' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.fnd_inv_ast is null) " +
                "union all " +
                "select '3' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_asharede_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code and k.trx_mkt=k1.trx_mkt " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='1' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null ) " +
                "union all " +
                "select '4' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_nstd_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.pbnk_industry_issuer is null or k.isu_org_typ_siz is null\n" +
                "   or  k.buy_back_f is null or k.lvrg_prj is null or k.ast_in_rat is null or k.ast_out_rat is null or k.lvrg_tot_fee is null) " +
                "union all " +
                "select '4' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_nstd_bas_inf k " +
                "left join ods_zcccmx k1 on left(k.scr_cd,length(k.scr_cd)-2)=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} " +
                "  and k.scr_cd like '%DC' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.pbnk_industry_issuer is null or k.isu_org_typ_siz is null " +
                "   or  k.buy_back_f is null or k.lvrg_prj is null or k.ast_in_rat is null or k.ast_out_rat is null or k.lvrg_tot_fee is null) " +
                "union all " +
                "select '5' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_mng_plan_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='11' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.amt is null or k.fnd_crry_mth is null or k.ast_mng_plan_prpt is null " +
                "   or k.mng_fee_tat is null or k.trst_fee_tat is null or k.trx_rel_smr_fee_rat is null or k.oth_smr_fee_rat is null or k.med_agn_srv_org_smr_fee_rat is null) " +
                "union all " +
                "select '6' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_direct_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='11' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.publisher_trade_pb is null or k.publisher_scale_pb is null and k.iss_mod is null) " +
                "union all " +
                "select '7' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_amng_fund_ntpinfo k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='11' " +
                "  and (k.cbnd_scd_ctg is null or k.g06_scd_ctg is null or k.pbnk_trd_ctg is null or k.inv_asset is null) " +
                "union all " +
                "select '8' page, k1.valuation_date as holding_date,k.ass_nbr_ext as scr_cd,k2.org_sht_nm as scr_nm from ods_ass_right_bas_inf k " +
                "left join ods_zcccmx k1 on k.ass_nbr_ext=k1.asset_code " +
                "left join ods_org_info k2 on k.org_nbr_ext=k2.org_nbr_ext  " +
                "where k1.valuation_date=$S{holdingDate} and k1.asset_type='11' " +
                " and (k.cbc_sub_type is null or k.gg_cbc_sub_type is null or k.investment_type is null or k.sharehold is null) " +
                "union all " +
                "select '9' page, k1.valuation_date as holding_date,k.scr_cd,k.scr_nm from ods_trm_bas_inf k " +
                "left join ods_zcccmx k1 on k.scr_cd=k1.asset_code " +
                "where k1.valuation_date=$S{holdingDate} " +
                "  and k.dps_typ='06' " +
                "  and (k.cbnd_scd_ctg is null or k.gg_cbc_sub_type is null or k.pbnk_trd_ctg is null or k.lnk_sbj_mat_typ is null or k.lnk_sbj_mat is null)";
        return super.findRows(sql,params);
    }

    //指标校验提醒查询
    public SqlResult<DataCheckModel> findIndicatorCheckRemind(SqlParam<DataCheckModel> params) throws Exception {
        String userid=SysUtil.getLoginUserid();
        String sql = "select b.validate_type, b.validate_result, b.reason, r.table_name as validate_table, b.index_name as column_code, " +
                "       b.create_date, b.create_time, b.index_code, b.index_name,r.report_table, b.deal_date " +
                "  from base_data_validation b " +
                "  join base_report_info r on b.validate_table = r.report_table " +
                " where b.deal_date='"+params.getModel().getDealDate()+"'";
        if(!"admin".equals(userid)){
            sql = sql +" and r.menuid in(select k2.menuid from sys_user k inner join sys_user_role k1 on k.userid=k1.userid inner join sys_role_menu k2 on k1.roleid=k2.roleid where k.userid="+userid+" )";
        }
        return super.findRows(sql,params);
    }
    //指标校验提醒查询
    public SqlResult<DataCheckModel> findIndicatorCheck(SqlParam<DataCheckModel> params) throws Exception {
        String sql = "select re.table_name,bd.validate_table,bd.validate_result,bd.create_date,bd.deal_date,re.report_catgory,count(*) as data_num " +
                " from base_data_validation bd left join base_report_info re on re.report_table =bd.validate_table " +
                " where 1=1 and bd.validate_result in ('-1','2') ";
//        if (StringUtils.isNotBlank(params.getModel().getCreateDate())) {
//            sql += " and bd.create_date = $S{createDate}";
//        }
        sql += " group by re.table_name,bd.validate_table,bd.validate_result,bd.create_date,bd.deal_date,re.report_catgory order by bd.validate_result";
        return super.findRows(sql,params);
    }
    public String getSysWordDay () throws Exception {
        String sysWordDay="";
        String systemParamsByParaid = SysUtil.getSystemParamsByParaid("10006");
        if ("0".equals(systemParamsByParaid)) {
            sysWordDay = DateUtil.getNowDate();
        } else {
            sysWordDay = SysUtil.getSystemParamsByParaid("10004");
        }
        return sysWordDay;
    }

}
