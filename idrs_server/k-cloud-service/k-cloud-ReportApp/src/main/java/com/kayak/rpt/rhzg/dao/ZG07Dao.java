package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.model.ZG07;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ZG07Dao extends ComnDao {

    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG07> findZG07s(SqlParam<ZG07> params) throws Exception {
        String sql = "select  t.id,t.report_date,t.theory_report_start_date,t.prod_cd,t.loan_typ,t.loan_trans_org_cd,t.loan_contract_ori_cd,t.loan_contract_ori_zone,t.borrower_typ,t.zone,t.borrower_cd,t.industry_msg,t.enter_sponsor_eco_sector,t.enter_scale,t.loan_receipt_cd,t.loan_prod_typ,t.loan_real_trans,t.loan_issue_dt,t.loan_end_dt,t.loan_extension_dt,t.is_fixed_rate,t.rate_level,t.guarantee_mode,t.loan_quality,t.loan_status,t.loan_trans_discount_rate,t.ori_contract_ccy,t.ori_contract_amt,t.ori_contract_amt_cny,t.loan_balance_ccy,t.loan_balance,t.loan_balance_cny,t1.zone_text,t.tech_flag,t.green_flag,t.spec_flag,t.aged_flag,t.num_core_flag  from  app_pbc_report_zg07 t left join (select '000000' as itemkey,'000000:全部地区' as zone_text from dual union all select itemkey,CONCAT(itemkey,':',itemval) as zone_text from sys_dict_item where dict  in ('pbc_city_area_det','pbc_country_code')) t1 on t.zone = t1.itemkey  where sys_data_status ='1' ";
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  t.prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  t.report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  t.id = '" + params.getModel().getId() + "'";
        }
        if (Tools.isNotEmpty(params.getModel().getTechFlag())) {
            sql += " and t.tech_flag = '" + params.getModel().getTechFlag() + "'";
        }
        if (Tools.isNotEmpty(params.getModel().getGreenFlag())) {
            sql += " and t.green_flag = '" + params.getModel().getGreenFlag() + "'";
        }
        if (Tools.isNotEmpty(params.getModel().getSpecFlag())) {
            sql += " and t.spec_flag = '" + params.getModel().getSpecFlag() + "'";
        }
        if (Tools.isNotEmpty(params.getModel().getAgedFlag())) {
            sql += " and t.aged_flag = '" + params.getModel().getAgedFlag() + "'";
        }
        if (Tools.isNotEmpty(params.getModel().getNumCoreFlag())) {
            sql += " and t.num_core_flag = '" + params.getModel().getNumCoreFlag() + "'";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult updateZG07(SqlParam<ZG07> params) throws Exception {
        return super.update("update  app_pbc_report_zg07 set prod_cd=$S{prodCd},loan_typ=$S{loanTyp},loan_trans_org_cd=$S{loanTransOrgCd},loan_contract_ori_cd=$S{loanContractOriCd},loan_contract_ori_zone=$S{loanContractOriZone},borrower_typ=$S{borrowerTyp},zone=$S{zoneText},borrower_cd=$S{borrowerCd},industry_msg=$S{industryMsg},enter_sponsor_eco_sector=$S{enterSponsorEcoSector},enter_scale=$S{enterScale},loan_receipt_cd=$S{loanReceiptCd},loan_prod_typ=$S{loanProdTyp},loan_real_trans=$S{loanRealTrans},loan_issue_dt=$S{loanIssueDt},loan_end_dt=$S{loanEndDt},loan_extension_dt=$S{loanExtensionDt},is_fixed_rate=$S{isFixedRate},rate_level=$S{rateLevel},guarantee_mode=$S{guaranteeMode},loan_quality=$S{loanQuality},loan_status=$S{loanStatus},loan_trans_discount_rate=if($S{loanTransDiscountRate}='',null,$S{loanTransDiscountRate}),ori_contract_ccy=$S{oriContractCcy},ori_contract_amt=$S{oriContractAmt},ori_contract_amt_cny=$S{oriContractAmtCny},loan_balance_ccy=$S{loanBalanceCcy},loan_balance=$S{loanBalance},loan_balance_cny =$S{loanBalanceCny},theory_report_start_date=$S{theoryReportStartDate},tech_flag=$S{techFlag},green_flag=$S{greenFlag},spec_flag=$S{specFlag},aged_flag=$S{agedFlag},num_core_flag=$S{numCoreFlag}  where id=$S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG07(SqlParam<ZG07> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg07 WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZg07ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg07 where report_date = $S{beginDate} ", params);
    }

    public UpdateResult addZg07(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg07(prod_cd,loan_typ,loan_trans_org_cd,loan_contract_ori_cd,loan_contract_ori_zone,borrower_typ,zone,borrower_cd,industry_msg,enter_sponsor_eco_sector,enter_scale,loan_receipt_cd,loan_prod_typ,loan_real_trans,loan_issue_dt,loan_end_dt,loan_extension_dt,is_fixed_rate,rate_level,guarantee_mode,loan_quality,loan_status,loan_trans_discount_rate,ori_contract_ccy,ori_contract_amt,ori_contract_amt_cny,loan_balance_ccy,loan_balance,loan_balance_cny,register_status,theory_report_start_date) VALUES($S{prodCd},$S{loanTyp},$S{loanTransOrgCd},$S{loanContractOriCd},$S{loanContractOriZone},$S{borrowerTyp},$S{zone},$S{borrowerCd},$S{industryMsg},$S{enterSponsorEcoSector},$S{enterScale},$S{loanReceiptCd},$S{loanProdTyp},$S{loanRealTrans},$S{loanIssueDt},$S{loanEndDt},$S{loanExtensionDt},$S{isFixedRate},if($S{rateLevel}='',null,$S{rateLevel}),$S{guaranteeMode},$S{loanQuality},$S{loanStatus},if($S{loanTransDiscountRate}='',null,$S{loanTransDiscountRate}),$S{oriContractCcy},if($S{oriContractAmt}='',null,$S{oriContractAmt}),if($S{oriContractAmtCny}='',null,$S{oriContractAmtCny}),$S{loanbalanceccy},if($S{loanBalance}='',null,$S{loanBalance}),if($S{loanBalanceCny}='',null,$S{loanBalanceCny}),$S{registerStatus},$S{theoryReportStartDate})", params);
    }

    public List<SqlRow> addclcSourceZonCdDict(Map<String, Object> params) throws Exception {
        String sql = "select '000000' as VALUE,'全部地区' as TEXT from dual union all SELECT itemkey VALUE, itemval TEXT  FROM sys_dict_item where 1 =1 ";
        if (StringUtils.isNotBlank(params.get("clcSourceCustTyp").toString())) {
            if(params.get("clcSourceCustTyp").toString().equals("1") || params.get("clcSourceCustTyp").toString().equals("2") || params.get("clcSourceCustTyp").toString().equals("3") || params.get("clcSourceCustTyp").toString().equals("4") || params.get("clcSourceCustTyp").toString().equals("5") ){
                sql = sql + " and dict  = 'pbc_city_area_det' ";
            }
            if(params.get("clcSourceCustTyp").toString().equals("6") ){
                sql = sql + " and dict  = 'pbc_country_code' ";
            }
        }else {
            sql = sql + " and dict  in ('pbc_city_area_det','pbc_country_code')  ";
        }
        if(params.containsKey("TEXT") && params.get("TEXT") != null ){
            if(StringUtils.isNotBlank(params.get("TEXT").toString())) {
                sql = sql + " and (itemkey like '%$U{TEXT}%' or itemval like '%$U{TEXT}%')  ";
            }
        }
        if (params.containsKey("limit") && params.get("limit") != null) {
            sql = sql + " limit " + params.get("limit").toString();
        }

        List<SqlRow> s = super.findRows(sql, DataSourceProperty.PUB,params);
        return s;
    }
}
