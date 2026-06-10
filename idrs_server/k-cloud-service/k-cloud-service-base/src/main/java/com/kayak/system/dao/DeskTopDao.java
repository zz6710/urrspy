package com.kayak.system.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.system.model.DeskTopModel;
import com.kayak.system.model.OtherDeskTopModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class DeskTopDao extends ComnDao {
    /**
     * 查询接口调用日志信息
     */
    public SqlResult<DeskTopModel> findPortLogInfo(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "select f.id,f.port_name, f.port_type,f.deal_date, f.file_state, f.total_num "+
                     "  from base_port_file_log f " +
                     " where 1 = 1 ";
        String sql1 = "union all select f.id, f.port_name,f.port_type,'"+params.getModel().getDealDate()+"' deal_date,'00' file_state,'' total_num from base_port_manage f  where not exists (" +
                                "select 1 from base_port_file_log t where f.port_code  = t.port_code and t.deal_date='"+params.getModel().getDealDate()+"')";
        String sql2 = "select f.id, f.port_name,f.port_type,'"+params.getModel().getDealDate()+"' deal_date,'00' file_state from base_port_manage f  where not exists (" +
                "select 1 from base_port_file_log t where f.port_code  = t.port_code and t.deal_date='"+params.getModel().getDealDate()+"')";
//        if (StringUtils.isNotBlank(params.getModel().getDealDate())) {
//            sql = sql + " and f.deal_date = '" + params.getModel().getDealDate() + "'";
//        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate()) && StringUtils.isNotBlank(params.getModel().getTheoryReportEndDate())) {
            sql += " and f.deal_date between "+"'"+params.getModel().getTheoryReportStartDate()+"'"+" and "+"'"+params.getModel().getTheoryReportEndDate()+"'";
        }
        if (StringUtils.isNotBlank(params.getModel().getFileState())) {
            //首页里的不限删选
            if(params.getModel().getFileState().equals("-1")){
                sql = sql +sql1;
            }else if(params.getModel().getFileState().equals("00")){
                //首页里的待处理
                return super.findRows(sql2, DataSourceProperty.PUB, params);
            }else{
                sql = sql + " and f.file_state like '%" + params.getModel().getFileState() + "%'";
                sql += " order by f.id desc ";
            }
        }
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 查询接口接入情况（失败）
     */
    public SqlResult<DeskTopModel> findPortErrLogInfo(SqlParam<DeskTopModel> params) throws Exception {
//        String sql = "select f.id,f.port_name, f.port_type,f.deal_date, f.file_state, f.total_num from base_port_file_log f  where 1 = 1  and f.deal_date = '"+params.getModel().getDealDate()+"' and f.file_state like '%02%' order by f.id desc";
        String sql = "select f.id,f.port_name, f.port_type,f.deal_date, f.file_state, f.total_num from base_port_file_log f  where 1 = 1  and f.file_state like '%02%' order by f.id desc";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }
    /**
     * 查询信息披露情况
     */
    public SqlResult<DeskTopModel>  findDisclosureNDetails(SqlParam<DeskTopModel> params,String nowPlanDate,String nextPlanDate) throws Exception {
        // 0 今日 1 延期  2 明日
        String disclosureFlag=params.getModel().getDisclosureFlag();
        StringBuilder sql = new StringBuilder("");
        if("1".equals(disclosureFlag)){
            sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date <'").append(nowPlanDate).append("'");
        }else if("2".equals(disclosureFlag)){
            sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date ='").append(nextPlanDate).append("' ");
        }else{
            sql.append("select d.disclosure_type,d.plan_fb_date,d.disclosure_son_type,d.disclosure_status,count(*) disclosure_count from idb_disclosure_notice d where 1=1 and d.plan_fb_date ='").append(nowPlanDate).append("' ");
        }
        sql.append("group by d.disclosure_status,d.disclosure_type,d.disclosure_son_type,d.plan_fb_date");
        return super.findRows(sql.toString(), DataSourceProperty.IDB, params);
    }

    public SqlResult<DeskTopModel> findReportResultInfo(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "SELECT distinct res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res";
        sql += " inner join base_report_info bri on res.report_table = bri.report_table";
        sql += " inner join sys_role_menu srm on bri.menuid = srm.menuid";
        sql += " inner join sys_user_role sur on srm.roleid = sur.roleid";
        sql += " where res.status='2'";
        if(!"admin".equals(SysUtil.getLoginUserid())){
            sql += "   and sur.userid ='" + SysUtil.getLoginUserid() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
            sql = sql + " and res.theory_report_start_date ='" + params.getModel().getTheoryReportStartDate() + "'";
            //sql = sql + " and res.theory_report_end_date >='" + params.getModel().getTheoryReportStartDate() + "'";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<DeskTopModel> findNextReportResultInfo(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "SELECT  distinct res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res";
        sql += " inner join base_report_info bri on res.report_table = bri.report_table";
        sql += " inner join sys_role_menu srm on bri.menuid = srm.menuid";
        sql += " inner join sys_user_role sur on srm.roleid = sur.roleid";
        sql += " where res.status='2'";
        if(!"admin".equals(SysUtil.getLoginUserid())){
            sql += "   and sur.userid ='" + SysUtil.getLoginUserid() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
            sql = sql + " and res.theory_report_end_date <'" + params.getModel().getTheoryReportStartDate() + "'";
        }
        return super.findRows(sql, params);
    }
    public SqlResult<DeskTopModel> findNextReportResultInfoNum(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "SELECT  distinct res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res";
        sql += " inner join base_report_info bri on res.report_table = bri.report_table";
        sql += " inner join sys_role_menu srm on bri.menuid = srm.menuid";
        sql += " inner join sys_user_role sur on srm.roleid = sur.roleid";
        sql += " where res.status='2'";
        if(!"admin".equals(SysUtil.getLoginUserid())){
            sql += "   and sur.userid ='" + SysUtil.getLoginUserid() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
            sql = sql + " and res.theory_report_start_date ='" + params.getModel().getTheoryReportStartDate() + "'";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<DeskTopModel> findTodayReportResultInfo(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "SELECT distinct res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res";
        sql += " inner join base_report_info bri on res.report_table = bri.report_table";
        sql += " inner join sys_role_menu srm on bri.menuid = srm.menuid";
        sql += " inner join sys_user_role sur on srm.roleid = sur.roleid";
        sql += " where res.status='1'";
        if(!"admin".equals(SysUtil.getLoginUserid())){
            sql += "   and sur.userid ='" + SysUtil.getLoginUserid() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
            sql = sql + " and res.register_date ='" + params.getModel().getTheoryReportStartDate() + "'";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<DeskTopModel> findExpiryReportResultInfo(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "SELECT distinct res.report_type,res.report_table,res.report_table_name,res.theory_report_end_date,res.theory_report_start_date,total,res.report_success_number,res.status,res.register_status,(total-report_success_number) need_total FROM base_report_result res";
        sql += " inner join base_report_info bri on res.report_table = bri.report_table";
        sql += " inner join sys_role_menu srm on bri.menuid = srm.menuid";
        sql += " inner join sys_user_role sur on srm.roleid = sur.roleid";
        sql += " where res.status='2'";
        if(!"admin".equals(SysUtil.getLoginUserid())){
            sql += "   and sur.userid ='" + SysUtil.getLoginUserid() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getTheoryReportStartDate())) {
            sql = sql + " and res.theory_report_end_date ='" + params.getModel().getTheoryReportStartDate() + "'";
        }
        return super.findRows(sql, params);
    }

    //基础数据补录查询
    public SqlResult<DeskTopModel> findSubmitRemindNum(SqlParam<DeskTopModel> params) throws Exception {
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
                "left join ods_zcccmx k1 on k.SCR_CD=k1.asset_code and k.TRX_MKT=k1.trx_mkt " +
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
    public SqlResult<DeskTopModel> findIndicatorCheckRemind(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "select re.table_name,bd.validate_table,bd.validate_result,bd.create_date,bd.deal_date,re.report_catgory,count(*) as data_num " +
                " from base_data_validation bd left join base_report_info re on re.report_table =bd.validate_table " +
                " where 1=1";
        if (StringUtils.isNotBlank(params.getModel().getReportCatgory()))
            sql += " and re.report_catgory = $S{reportCatgory}";
        if (StringUtils.isNotBlank(params.getModel().getValidateResult())) {
            sql += " and bd.validate_result = $S{validateResult}";
        }
        if (StringUtils.isNotBlank(params.getModel().getCreateDate())) {
            sql += " and bd.create_date = $S{createDate}";
        }
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

    //获取当期日期的下一个工作日
    public SqlRow getNextWorkday(String currentDay, String pgmno) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("pgmno",pgmno);
        params.put("currentDay",currentDay);
        String sql = "SELECT min(workday) workday FROM sys_workday_set WHERE pgmno = $S{pgmno} and workday > $S{currentDay} order by workday";
        return super.findRow(sql, params);
    }




    /**
     * 查询 代码转换 未办结事项
     */
    public SqlResult<DeskTopModel> findOtherTopInfoUnCode(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "select t.*,'请理财中心修改公开市场代码' as msg, '' as end_date , 'base_asset_code_management' as source_table   from base_asset_code_management t where t.id not in ( select keyword from index_reminder_record where source_table = 'base_asset_code_management')";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }


    /**
     * 查询 代码转换 已办结事项
     */
    public SqlResult<DeskTopModel> findOtherTopInfoCode(SqlParam<DeskTopModel> params) throws Exception {
        String sql = "select t.*, '已办结' as msg, s.crt_date as end_date from base_asset_code_management t inner join index_reminder_record  s   on   t.id   =  s.keyword  where  s.source_table = 'base_asset_code_management' ";
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 查询
     */
    public SqlResult<OtherDeskTopModel> findOtherTopInfo(SqlParam<OtherDeskTopModel> params) throws Exception {
        String sql = "SELECT keyword, source_table, crt_date, crt_time, remark, old_data, new_data, remind_status, remind_msg, deal_date from index_reminder_record  where  source_table =   '" + params.getModel().getSourceTable()+"'  and  remind_status = '" +params.getModel().getRemindStatus()+"'" ;
        if(params.getModel().getStartDate() !=null &&  !params.getModel().getStartDate().equals("")){
            sql =  sql +  " and   deal_date   >=  '"+params.getModel().getStartDate()+"'  ";
        }
        if(params.getModel().getEndDate() !=null &&  !params.getModel().getEndDate().equals("")){
            sql =  sql +  " and   deal_date   <=  '"+params.getModel().getEndDate()+"'  ";
        }
        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

    /**
     * 查询  无条件
     */
    public SqlResult<OtherDeskTopModel> findOtherTopInfoAll(SqlParam<OtherDeskTopModel> params) throws Exception {
        String sql = "SELECT keyword, source_table, crt_date, crt_time, remark, old_data, new_data, remind_status, remind_msg, deal_date from index_reminder_record  where  remind_status = '0' " ;

        return super.findRows(sql, DataSourceProperty.PUB, params);
    }

//
//    /**
//     * 查询 代码转换 已办结事项
//     */
//    public void saveOtherTopInfoCode(SqlParam<DeskTopModel> params) throws Exception {
//        String sql = "INSERT INTO index_reminder_record (keyword, source_table, crt_date, crt_time)VALUES($S{keyword}, $S{sourceTable}, $S{crtDate}, $S{crtTime}) ";
//         super.update(sql, DataSourceProperty.PUB, params.getModel());
//    }
//


    /**
     * 查询 代码转换 已办结事项
     */
    public void updateOtherTopInfoStatus(SqlParam<OtherDeskTopModel> params) throws Exception {
        String sql = " UPDATE index_reminder_record " +
                "SET crt_date='"+params.getModel().getCrtDate()+"' , crt_time='"+params.getModel().getCrtTime()+"',  remind_status= '1' " +
                "WHERE keyword='"+params.getModel().getKeyword()+"' AND source_table='"+params.getModel().getSourceTable()+"' and deal_date =  '"+params.getModel().getDealDate()+"' ";
        super.update(sql, DataSourceProperty.PUB, params.getModel());
    }






}
