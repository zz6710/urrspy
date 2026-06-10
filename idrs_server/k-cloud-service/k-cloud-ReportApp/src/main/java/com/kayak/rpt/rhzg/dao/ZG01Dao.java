package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.rhzg.model.ZG01;
import com.kayak.rpt.zz.manage.model.InitialSubRegistInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ZG01Dao extends ComnDao {




    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG01> findZG01s(SqlParam<ZG01> params) throws Exception {
        String sql = "SELECT id,msg_typ,sys_data_version,prod_cd,prod_nm,isu_org_cd,isu_org_nm," +
                "            prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt," +
                "            clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr," +
                "            ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f," +
                "            trust_prod_type,base_open_info_f,change_reason,back1,back2,back3,back4,back5,REGISTER_STATUS,create_date,report_date,theory_report_start_date,theory_report_end_date" +
                "       FROM app_pbc_report_zg01 " +
                "      where sys_data_status ='1' " ;
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and prod_cd = '" + params.getModel().getProdCd() + "'";
        }
        //report_date 为报送日期
        if (StringUtils.isNotBlank(params.getModel().getBeginDate())) {
            sql = sql + " and create_date >= '" + params.getModel().getBeginDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getQueryDate())) {
            sql = sql + " and create_date <= '" + params.getModel().getQueryDate() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and id = '" + params.getModel().getId() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportBeginDate())) {
            sql = sql + " and  report_date >= '" + params.getModel().getReportBeginDate()+ "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportEndDate())) {
            sql = sql + " and  report_date <= '" + params.getModel().getReportEndDate() + "'";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<ZG01> findZG01sByProd(String prods, SqlParam<ZG01> params) throws Exception {
        params.setStart(0);
        params.setLimit(2000);
        String sql = "SELECT id,msg_typ,sys_data_version,prod_cd,prod_nm,isu_org_cd,isu_org_nm," +
                "            prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt," +
                "            clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr," +
                "            ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f," +
                "            trust_prod_type,base_open_info_f,change_reason,back1,back2,back3,back4,back5,REGISTER_STATUS,create_date,report_date,theory_report_start_date,theory_report_end_date" +
                "       FROM app_pbc_report_zg01 " +
                "      where sys_data_status ='1' " ;
        if(StringUtils.isNotBlank(prods)){
            sql = sql + " and  prod_cd in (" + prods + ")";
        }
        return super.findRows(sql, params);
    }


    public UpdateResult updateZG01(SqlParam<ZG01> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg01 SET msg_typ=$S{msgTyp},theory_report_start_date=$S{theoryReportStartDate},prod_cd=$S{prodCd},prod_nm=$S{prodNm},isu_org_cd=$S{isuOrgCd},isu_org_nm=$S{isuOrgNm},prod_cate=$S{prodCate},prod_inv_typ=$S{prodInvTyp},prod_brnd=$S{prodBrnd},prod_tms=$S{prodTms},isu_org_prod_cd=$S{isuOrgProdCd},clc_ccy=$S{clcCcy},call_prcp_ccy=$S{callPrcpCcy},call_ern_ccy=$S{callErnCcy},prod_clc_mth=$S{prodClcMth},mng_mth=$S{mngMth},prod_mod=$S{prodMod},clc_bgn_dt=$S{clcBgnDt},clc_end_dt=$S{clcEndDt},isu_org_early_term_f=$S{isuOrgEarlyTermF},cust_redemption_f=$S{custRedemptionF},prod_inc_crd_f=$S{prodIncCrdF},prod_inc_crd_org_typ=$S{prodIncCrdOrgTyp},prod_inc_crd_form=$S{prodIncCrdForm},dms_trst_org_cd=$S{dmsTrstOrgCd},ovs_trst_org_cnr=$S{ovsTrstOrgCnr},ovs_trst_org_nm=$S{ovsTrstOrgNm},found_dt=$S{foundDt},change_dt=$S{changeDt},prod_scheduled_end_dt=$S{prodScheduledEndDt},entrusted_duty=$S{entrustedDuty},clsf_prod_f=$S{clsfProdF},usufruct_change_prod_f=$S{usufructChangeProdF},cash_mng_prod_f=$S{cashMngProdF},cb_w_mng_f=$S{cbWMngF},trust_prod_type = $S{trustProdType},base_open_info_f=$S{baseOpenInfoF},change_reason=$S{changeReason}," +
                        "back1 = $S{back1},back2 = $S{back2},back3 = $S{back3},back4 = $S{back4},back5 = $S{back5}  WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG01(SqlParam<ZG01> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg01 WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZg01ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg01 where  create_date = $S{createDate}", params);
    }

    public UpdateResult addZg01(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg01(msg_typ,theory_report_start_date,register_status,prod_cd,prod_nm,isu_org_cd,isu_org_nm,prod_cate,prod_inv_typ,prod_brnd,prod_tms,isu_org_prod_cd,clc_ccy,call_prcp_ccy,call_ern_ccy,prod_clc_mth,mng_mth,prod_mod,clc_bgn_dt,clc_end_dt,isu_org_early_term_f,cust_redemption_f,prod_inc_crd_f,prod_inc_crd_org_typ,prod_inc_crd_form,dms_trst_org_cd,ovs_trst_org_cnr,ovs_trst_org_nm,found_dt,change_dt,prod_scheduled_end_dt,entrusted_duty,clsf_prod_f,usufruct_change_prod_f,cash_mng_prod_f,cb_w_mng_f,trust_prod_type,base_open_info_f,change_reason,back1,back2,back3,back4,back5 ) VALUES($S{msgTyp},$S{theoryReportStartDate},$S{registerStatus},$S{prodCd},$S{prodNm},$S{isuOrgCd},$S{isuOrgNm},$S{prodCate},$S{prodInvTyp},$S{prodBrnd},$S{prodTms},$S{isuOrgProdCd},$S{clcCcy},$S{callPrcpCcy},$S{callErnCcy},$S{prodClcMth},$S{mngMth},$S{prodMod},$S{clcBgnDt},$S{clcEndDt},$S{isuOrgEarlyTermF},$S{custRedemptionF},$S{prodIncCrdF},$S{prodIncCrdOrgTyp},$S{prodIncCrdForm},$S{dmsTrstOrgCd},$S{ovsTrstOrgCnr},$S{ovsTrstOrgNm},$S{foundDt},$S{changeDt},$S{prodScheduledEndDt},$S{entrustedDuty},$S{clsfProdF},$S{usufructChangeProdF},$S{cashMngProdF},$S{cbWMngF},$S{trusProdType},$S{baseOpenInfoF},$S{changeReason},$S{back1},$S{back2},$S{back3},$S{back4},$S{back5})", params);
    }
}
