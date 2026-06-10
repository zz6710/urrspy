package com.kayak.rpt.rhzg.dao;


import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.rpt.rhzg.model.ZG06;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ZG06Dao extends ComnDao {

    //TODO 查询条件需要控制，数据量大时可能会出现问题
    public SqlResult<ZG06> findZG06s(SqlParam<ZG06> params) throws Exception {
        String sql = "SELECT t.id,t.prod_cd,t.report_date,t.theory_report_start_date,t.theory_report_end_date,t.asset_debt_project,t.asset_income_code,t.base_asset_sale_org_nm,t.base_asset_sale_org_cd,t.base_asset_sale_org_typ,t.base_asset_sale_org_industry,t.base_asset_sale_org_reg_zone,t.base_asset_sale_org_econo_sector,t.base_asset_sale_org_scale,t.trans_dt,t.trans_scheduled_end_dt,t.trans_extension_dt,t.trans_real_end_dt,t.base_asset_typ,t.base_asset_ori_prot_ccy,t.base_asset_ori_prot_amt,t.base_asset_ori_prot_amt_cny,t.base_asset_trans_ccy,t.base_asset_trans_amt,t.base_asset_trans_amt_cny,t.trans_org_out_table_f,t.trans_org_buy_back_f,t.is_fixed_rate,t.rate_level,t.guarantee_mode,t.base_asset_trans_dep,t.base_asset_end_dt_ccy,t.base_asset_end_dt_balance,t.base_asset_end_dt_balance_cny,t1.zone_text,register_trading_place,register_trading_code,t.tech_flag,t.green_flag,t.spec_flag,t.aged_flag,t.num_core_flag,t.base_asset_inv_obj_idt,t.base_asset_inv_obj_scale FROM app_pbc_report_zg06 t left join (select '000000' as itemkey,'000000:全部地区' as zone_text from dual union all select itemkey,CONCAT(itemkey,':',itemval) as zone_text from sys_dict_item where dict  in ('pbc_city_area_det','pbc_country_code')) t1 on t.base_asset_sale_org_reg_zone = t1.itemkey  where sys_data_status ='1'  ";
        if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
            sql = sql + " and  t.prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
            sql = sql + " and  t.report_date like '" + params.getModel().getReportDate() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getId())) {
            sql = sql + " and  t.id = '" + params.getModel().getId() + "'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAssetIncomeCode())) {
            sql = sql + " and  t.asset_income_code like '%" + params.getModel().getAssetIncomeCode() + "%'";
        }
        if (StringUtils.isNotBlank(params.getModel().getAssetDebtProject())) {
            sql = sql + " and  t.asset_debt_project = '" + params.getModel().getAssetDebtProject() + "'";
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

    public UpdateResult updateZG06(SqlParam<ZG06> params) throws Exception {
        return super.update("UPDATE app_pbc_report_zg06 SET prod_cd = $S{prodCd},theory_report_start_date = $S{theoryReportStartDate},asset_debt_project = $S{assetDebtProject},asset_income_code = $S{assetIncomeCode},base_asset_sale_org_nm = $S{baseAssetSaleOrgNm},base_asset_sale_org_cd = $S{baseAssetSaleOrgCd},base_asset_sale_org_typ = $S{baseAssetSaleOrgTyp},base_asset_sale_org_industry = $S{baseAssetSaleOrgIndustry},base_asset_sale_org_reg_zone = $S{zoneText},base_asset_sale_org_econo_sector = $S{baseAssetSaleOrgEconoSector},base_asset_sale_org_scale = $S{baseAssetSaleOrgScale},trans_dt = $S{transDt},trans_scheduled_end_dt = $S{transScheduledEndDt},trans_extension_dt = $S{transExtensionDt},trans_real_end_dt = $S{transRealEndDt},base_asset_typ = $S{baseAssetTyp},base_asset_ori_prot_ccy = $S{baseAssetOriProtCcy},base_asset_ori_prot_amt = if($S{baseAssetOriProtAmt}='',null,$S{baseAssetOriProtAmt}),base_asset_ori_prot_amt_cny = if($S{baseAssetOriProtAmtCny}='',null,$S{baseAssetOriProtAmtCny}),base_asset_trans_ccy = $S{baseAssetTransCcy},base_asset_trans_amt = if($S{baseAssetTransAmt}='',null,$S{baseAssetTransAmt}),base_asset_trans_amt_cny = if($S{baseAssetTransAmtCny}='',null,$S{baseAssetTransAmtCny}),trans_org_out_table_f = $S{transOrgOutTableF},trans_org_buy_back_f = $S{transOrgBuyBackF},is_fixed_rate = $S{isFixedRate},rate_level = if($S{rateLevel}='',null,$S{rateLevel}),guarantee_mode = $S{guaranteeMode},base_asset_trans_dep = $S{baseAssetTransDep},base_asset_end_dt_ccy = $S{baseAssetEndDtCcy},base_asset_end_dt_balance = if($S{baseAssetEndDtBalance}='',null,$S{baseAssetEndDtBalance}),base_asset_end_dt_balance_cny = if($S{baseAssetEndDtBalanceCny}='',null,$S{baseAssetEndDtBalanceCny}),register_trading_place= $S{registerTradingPlace},register_trading_code= $S{registerTradingCode},tech_flag=$S{techFlag},green_flag=$S{greenFlag},spec_flag=$S{specFlag},aged_flag=$S{agedFlag},num_core_flag=$S{numCoreFlag},base_asset_inv_obj_idt=$S{baseAssetInvObjIdt},base_asset_inv_obj_scale=$S{baseAssetInvObjScale} WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZG06(SqlParam<ZG06> params) throws Exception {
        return super.update("DELETE FROM app_pbc_report_zg06 WHERE id = $S{id}",
                params.getModel());
    }

    public UpdateResult deleteZg06ByDate(Object params) throws Exception {

        return super.update("DELETE FROM app_pbc_report_zg06 where report_date = $S{beginDate} ", params);
    }


    public UpdateResult addZg06(Object params) throws Exception {
        return super.update("INSERT INTO app_pbc_report_zg06(prod_cd,theory_report_start_date,register_status,asset_debt_project,asset_income_code,base_asset_sale_org_nm,base_asset_sale_org_cd,base_asset_sale_org_typ,base_asset_sale_org_industry,base_asset_sale_org_reg_zone,base_asset_sale_org_econo_sector,base_asset_sale_org_scale,trans_dt,trans_scheduled_end_dt,trans_extension_dt,trans_real_end_dt,base_asset_typ,base_asset_ori_prot_ccy,base_asset_ori_prot_amt,base_asset_ori_prot_amt_cny,base_asset_trans_ccy,base_asset_trans_amt,base_asset_trans_amt_cny,trans_org_out_table_f,trans_org_buy_back_f,is_fixed_rate,rate_level,guarantee_mode,base_asset_trans_dep,base_asset_end_dt_ccy,base_asset_end_dt_balance,base_asset_end_dt_balance_cny) VALUES($S{prodCd},$S{theoryReportStartDate},$S{registerStatus},$S{assetDebtProject},$S{assetIncomeCode},$S{baseAssetSaleOrgNm},$S{baseAssetSaleOrgCd},$S{baseAssetSaleOrgTyp},$S{baseAssetSaleOrgIndustry},$S{baseAssetSaleOrgRegZone},$S{baseAssetSaleOrgEconoSector},$S{baseAssetSaleOrgScale},$S{transDt},$S{transScheduledEndDt},$S{transExtensionDt},$S{transRealEndDt}, $S{baseAssetTyp},$S{baseAssetOriProtCcy},if($S{baseAssetOriProtAmt}='',null,$S{baseAssetOriProtAmt}),if($S{baseAssetOriProtAmtCny}='',null,$S{baseAssetOriProtAmtCny}),$S{baseAssetTransCcy},if($S{baseAssetTransAmt}='',null,$S{baseAssetTransAmt}),if($S{baseAssetTransAmtCny}='',null,$S{baseAssetTransAmtCny}),$S{transOrgOutTableF},$S{transOrgBuyBackF},$S{isFixedRate},if($S{rateLevel}='',null,$S{rateLevel}),$S{guaranteeMode},$S{baseAssetTransDep},$S{baseAssetEndDtCcy},if($S{baseAssetEndDtBalance}='',null,$S{baseAssetEndDtBalance}),if($S{baseAssetEndDtBalanceCny}='',null,$S{baseAssetEndDtBalanceCny}))", params);
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
