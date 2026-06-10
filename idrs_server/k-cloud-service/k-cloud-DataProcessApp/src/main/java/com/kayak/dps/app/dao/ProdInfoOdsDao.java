package com.kayak.dps.app.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.dps.app.model.ProdInfoOds;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class ProdInfoOdsDao extends ComnDao {
    @Resource(name = "comnDao")
    private ComnDao comnDao;
    public SqlResult<ProdInfoOds> findProdSeries(SqlParam<ProdInfoOds> params) throws Exception {
        String sql = "select prod_series_cd, prod_series_name from ODS_PROD_SERIES ";
        return super.findRows(sql, params);
    }

    public SqlResult<ProdInfoOds> findProdInfoOds(SqlParam<ProdInfoOds> params) throws Exception {
        String sql = "SELECT t.id,t.PROD_BRAND,t.ACCOUNT_CODE,t.account_name,t.PROD_CODE,t.CHECK_INON,t.PROD_NAME,t.PROD_MOD,t.T8_INVEST_PROP_TYPE,t.RISK_LEV,t.INVESTOR_TREND,t.CASH_TYPE,t.CASH_TYPE_Z,t.INCOME_TYPE,t.PROD_STATUS,t.ISSU_CCY," +
                "t.INCOME_CCY,t.RETURN_CCY,t.SUBS_BDATE,t.SUBS_EDATE,t.ESTABLISH_DATE,t.END_DATE,t.REAL_END_DATE,t.IS_MIN_HOLD_TERM,t.MIN_HOLD_TERM,t.REDEEM_AFTER_HOLD,t.BLG_FIN_SAM_BUS_F,t.YJBJJZSM_PJ," +
                "t.PROD_NM_FU,t.PERFM_BENCHM_RATE,t.SUBSCR_SD_EARLIEST,t.SUBSCR_ED_LATEST,t.PLAN_FUND_AMOUNT,t.INVEST_MNG_FEE_RATE,t.PERFM_BENCHM_UPPER,t.PERFM_BENCHM_LOWER,t.MSG_TYPE,t.PROD_CHANGE_DATE," +
                "t.CUSTODY_ORG_MNG_DUTY,t.INCOME_TRANS_PROD_MARK,t.CROSS_BORDER_WEALTH,t.BASE_INFO_OPEN_MARK,t.CHANGE_REASON,t.PROD_RENEWAL_MARK,t.LIQUIDATE_MARK," +
                "t1.OPER_MOD,t1.COLL_MOD,t1.TERM_FLAG,t1.REDEEM_FLAG,t1.BORD_TRUSTI_CODE,t1.BORD_TRUSTI_CODE_P,t1.BORD_TRUSTI_NAME,t1.OVERS_TRUSTI_NAME,t1.OVERS_TRUSTI_NATION,t1.PROD_CREDIT_FLAG,t1.PROD_CREDIT_MOD,t1.PROD_CREDIT_ORG," +
                "t1.AUTHOR_NAME,t1.AUTHOR_IDENTIF,t1.DESIGN_NAME,t1.DESIGN_IDENTIF,t1.MANAGE_NAME,t1.MANAGE_IDENTIF,t1.SALEMAN_NAME,t1.SALEMAN_PHONENO,t1.SALEMAN_TELNO,t1.SALEMAN_EMAIL,t1.PROD_CYCLE,t1.SALE_PLACE," +
                "t1.SRV_MODE,t1.ASSET_MAPING,t1.MANAGE_MODE,t1.PRICING_TYPE,t1.COOPERATION_MODE,t1.RETURN_COST,t1.ADMIN_NAME,t1.PROD_TIMES,t1.RETURN_INCOME,t1.PROD_PRECENT,t1.IS_STRUCTPROD,t1.OPEN_MOD," +
                "t1.REGULAR_OPEN_CYCLE,t1.OTH_REGUL_OPEN_CYC,t1.RGLR_PRD_OPN_CYC,t1.IRREGULAR_OPEN_DESC,t1.FIRST_OPEN_DATE,t1.IS_OPEN_IN_HOLIDAY,t1.OPEN_TIMES,t1.OPEN_PERIOD_BUSINESS,t1.OPEN_PERI_BUSI_DESC," +
                "t1.COOPERATION_ORG_NAME,t1.NEW_OLD_PROD_F,t1.PROD_SAL_ZON,t1.PROD_ESP_PRPT,t1.CLSF_STO,t1.SPECI_COUNTRY_REGION,t1.INVEST_THRESH,cast(format(t1.SALE_COMMIS_RATE,5) as char) as SALE_COMMIS_RATE,cast(format(t1.CUSTODY_FEE_RATE,5) as char) as CUSTODY_FEE_RATE,t1.REDEEM_FLAG_PB," +
                "t2.PBC_REGCODE,(case when  t.CASH_TYPE = '01' then '1' else  t1.RGLR_PRD_OPN_CYC end) as MIN_PRD_OPN_CYC  " +
                "FROM ods_prod_base_info t " +
                "left join ODS_PROD_SPVS_INFO t1 on t.PROD_CODE = t1.PROD_CODE" +
                " left join ods_prod_core_info t2 on t.PROD_CODE = t2.PROD_CD" +
                " where 1=1 and (t.mother_fund_flag in ('0','1') or t.mother_fund_flag is null) ";

        ProdInfoOds p = params.getModel();
        if(StringUtils.isNotBlank(p.getProdCode())){
            sql = sql + " and (t.prod_code like '%" + params.getModel().getProdCode() + "%'";
            sql = sql + "  or t.prod_name like '%" + params.getModel().getProdCode() + "%')";
        }
        if(StringUtils.isNotBlank(p.getCheckInon())){
            sql += "and t.check_inon like '%$U{checkInon}%' ";
        }
        if(StringUtils.isNotBlank(p.getPbcRegcode())){
            sql += "and t2.PBC_REGCODE like '%$U{pbcRegcode}%' ";
        }
        if(StringUtils.isNotBlank(p.getProdStatus())){
            sql += "and t.prod_status in (" + SysUtil.inStr(p.getProdStatus()) + ")";
        }
        if(StringUtils.isNotBlank(p.getSubsBdate1()) && StringUtils.isNotBlank(p.getSubsBdate2())){
            sql += "and t.subs_bdate between $S{subsBdate1} and $S{subsBdate2} ";
        }
        if(StringUtils.isNotBlank(p.getEstablishDate1()) && StringUtils.isNotBlank(p.getEstablishDate2())){
            sql += "and t.establish_date between $S{establishDate1} and $S{establishDate2} ";
        }
        if(StringUtils.isNotBlank(p.getRealEndDate1()) && StringUtils.isNotBlank(p.getRealEndDate2())){
            sql += "and t.real_end_date between $S{realEndDate1} and $S{realEndDate2} ";
        }
        if(StringUtils.isNotBlank(p.getProdMod())){
            sql += "and t.prod_mod = $S{prodMod} ";
        }
        if(StringUtils.isNotBlank(p.getCashType())){
            sql += "and t.cash_type = $S{cashType} ";
        }
        return super.findRows(sql, params);
    }

    public SqlResult<ProdInfoOds> findSubProdInfoOds(SqlParam<ProdInfoOds> params) throws Exception {
        String sql = "SELECT t.id,t.PROD_BRAND,t.ACCOUNT_CODE,t.account_name,t.PROD_CODE,t.CHECK_INON,t.PROD_NAME,t.PROD_MOD,t.T8_INVEST_PROP_TYPE,t.RISK_LEV,t.INVESTOR_TREND,t.CASH_TYPE,t.CASH_TYPE_Z,t.INCOME_TYPE,t.PROD_STATUS,t.ISSU_CCY," +
                "t.INCOME_CCY,t.RETURN_CCY,t.SUBS_BDATE,t.SUBS_EDATE,t.ESTABLISH_DATE,t.END_DATE,t.REAL_END_DATE,t.IS_MIN_HOLD_TERM,t.MIN_HOLD_TERM,t.REDEEM_AFTER_HOLD,t.BLG_FIN_SAM_BUS_F,t.YJBJJZSM_PJ," +
                "t.PROD_NM_FU,t.PERFM_BENCHM_RATE,t.SUBSCR_SD_EARLIEST,t.SUBSCR_ED_LATEST,t.PLAN_FUND_AMOUNT,t.INVEST_MNG_FEE_RATE,t.PERFM_BENCHM_UPPER,t.PERFM_BENCHM_LOWER,t.MSG_TYPE,t.PROD_CHANGE_DATE," +
                "t.CUSTODY_ORG_MNG_DUTY,t.INCOME_TRANS_PROD_MARK,t.CROSS_BORDER_WEALTH,t.BASE_INFO_OPEN_MARK,t.CHANGE_REASON,t.PROD_RENEWAL_MARK,t.LIQUIDATE_MARK," +
                "t1.OPER_MOD,t1.COLL_MOD,t1.TERM_FLAG,t1.REDEEM_FLAG,t1.BORD_TRUSTI_CODE,t1.BORD_TRUSTI_CODE_P,t1.BORD_TRUSTI_NAME,t1.OVERS_TRUSTI_NAME,t1.OVERS_TRUSTI_NATION,t1.PROD_CREDIT_FLAG,t1.PROD_CREDIT_MOD,t1.PROD_CREDIT_ORG," +
                "t1.AUTHOR_NAME,t1.AUTHOR_IDENTIF,t1.DESIGN_NAME,t1.DESIGN_IDENTIF,t1.MANAGE_NAME,t1.MANAGE_IDENTIF,t1.SALEMAN_NAME,t1.SALEMAN_PHONENO,t1.SALEMAN_TELNO,t1.SALEMAN_EMAIL,t1.PROD_CYCLE,t1.SALE_PLACE," +
                "t1.SRV_MODE,t1.ASSET_MAPING,t1.MANAGE_MODE,t1.PRICING_TYPE,t1.COOPERATION_MODE,t1.RETURN_COST,t1.ADMIN_NAME,t1.PROD_TIMES,t1.RETURN_INCOME,t1.PROD_PRECENT,t1.IS_STRUCTPROD,t1.OPEN_MOD," +
                "t1.REGULAR_OPEN_CYCLE,t1.OTH_REGUL_OPEN_CYC,t1.RGLR_PRD_OPN_CYC,t1.IRREGULAR_OPEN_DESC,t1.FIRST_OPEN_DATE,t1.IS_OPEN_IN_HOLIDAY,t1.OPEN_TIMES,t1.OPEN_PERIOD_BUSINESS,t1.OPEN_PERI_BUSI_DESC," +
                "t1.COOPERATION_ORG_NAME,t1.NEW_OLD_PROD_F,t1.PROD_SAL_ZON,t1.PROD_ESP_PRPT,t1.CLSF_STO,t1.SPECI_COUNTRY_REGION,t1.INVEST_THRESH,cast(format(t1.SALE_COMMIS_RATE,5) as char) as SALE_COMMIS_RATE,cast(format(t1.CUSTODY_FEE_RATE,5) as char) as CUSTODY_FEE_RATE,t1.REDEEM_FLAG_PB," +
                "t2.PBC_REGCODE,(case when  t.CASH_TYPE = '01' then '1' else  t1.RGLR_PRD_OPN_CYC end) as MIN_PRD_OPN_CYC  " +
                "FROM ods_prod_base_info t " +
                "left join ODS_PROD_SPVS_INFO t1 on t.PROD_CODE = t1.PROD_CODE" +
                " left join ods_prod_core_info t2 on t.PROD_CODE = t2.PROD_CD" +
                " where 1=1 and (t.mother_fund_flag in ('0','2') or t.mother_fund_flag is null) ";

        ProdInfoOds p = params.getModel();
        if(StringUtils.isNotBlank(p.getProdCode())){
            sql = sql + " and (t.prod_code like '%" + params.getModel().getProdCode() + "%'";
            sql = sql + "  or t.prod_name like '%" + params.getModel().getProdCode() + "%')";
        }
        if(StringUtils.isNotBlank(p.getCheckInon())){
            sql += "and t.check_inon like '%$U{checkInon}%' ";
        }
        if(StringUtils.isNotBlank(p.getPbcRegcode())){
            sql += "and t2.PBC_REGCODE like '%$U{pbcRegcode}%' ";
        }
        if(StringUtils.isNotBlank(p.getProdStatus())){
            sql += "and t.prod_status in (" + SysUtil.inStr(p.getProdStatus()) + ")";
        }
        if(StringUtils.isNotBlank(p.getSubsBdate1()) && StringUtils.isNotBlank(p.getSubsBdate2())){
            sql += "and t.subs_bdate between $S{subsBdate1} and $S{subsBdate2} ";
        }
        if(StringUtils.isNotBlank(p.getEstablishDate1()) && StringUtils.isNotBlank(p.getEstablishDate2())){
            sql += "and t.establish_date between $S{establishDate1} and $S{establishDate2} ";
        }
        if(StringUtils.isNotBlank(p.getRealEndDate1()) && StringUtils.isNotBlank(p.getRealEndDate2())){
            sql += "and t.real_end_date between $S{realEndDate1} and $S{realEndDate2} ";
        }
        if(StringUtils.isNotBlank(p.getProdMod())){
            sql += "and t.prod_mod = $S{prodMod} ";
        }
        if(StringUtils.isNotBlank(p.getCashType())){
            sql += "and t.cash_type = $S{cashType} ";
        }
        return super.findRows(sql, params);
    }

    public UpdateResult updateProdInfoOds(ProdInfoOds params) throws Exception {
        String sql = "update ods_prod_base_info a \n" +
                "join ods_prod_base_info b on a.prod_code = b.prod_code \n" +
                "set a.PROD_BRAND = $S{prodBrand},\n" +
                "\ta.ACCOUNT_CODE = $S{accountCode},\n" +
                "\ta.account_name = $S{accountName},\n" +
                "\ta.CHECK_INON = $S{checkInon},\n" +
                "\ta.PROD_NAME = $S{prodName},\n" +
                "\ta.PROD_MOD = $S{prodMod},\n" +
                "\ta.T8_INVEST_PROP_TYPE = $S{t8InvestPropType},\n" +
                "\ta.RISK_LEV = $S{riskLev},\n" +
                "\ta.INVESTOR_TREND = $S{investorTrend},\n" +
                "\ta.CASH_TYPE = $S{cashType},\n" +
                "\ta.CASH_TYPE_Z = $S{cashTypeZ},\n" +
                "\ta.INCOME_TYPE = $S{incomeType},\n" +
                "\ta.PROD_STATUS = $S{prodStatus},\n" +
                "\ta.ISSU_CCY = $S{issuCcy},\n" +
                "\ta.INCOME_CCY = $S{incomeCcy},\n" +
                "\ta.RETURN_CCY = $S{returnCcy},\n" +
                "\ta.SUBS_BDATE = $S{subsBdate},\n" +
                "\ta.SUBS_EDATE = $S{subsEdate},\n" +
                "\ta.ESTABLISH_DATE = $S{establishDate},\n" +
                "\ta.END_DATE = $S{endDate},\n" +
                "\ta.REAL_END_DATE = $S{realEndDate},\n" +
                "\ta.IS_MIN_HOLD_TERM = $S{isMinHoldTerm},\n" +
                "\ta.MIN_HOLD_TERM = $D{minHoldTerm},\n" +
                "\ta.REDEEM_AFTER_HOLD = $S{redeemAfterHold},\n" +
                "\ta.BLG_FIN_SAM_BUS_F = $S{blgFinSamBusF},\n" +
                "\ta.YJBJJZSM_PJ = $S{yjbjjzsmPj},\n" +
                "\ta.PROD_NM_FU = $S{prodNmFu},\n" +
                "\ta.PERFM_BENCHM_RATE = $D{perfmBenchmRate},\n" +
                "\ta.SUBSCR_SD_EARLIEST = $S{subscrSdEarliest},\n" +
                "\ta.SUBSCR_ED_LATEST = $S{subscrEdLatest},\n" +
                "\ta.PLAN_FUND_AMOUNT = $D{planFundAmount},\n" +
                "\ta.INVEST_MNG_FEE_RATE = $D{investMngFeeRate},\n" +
                "\ta.PERFM_BENCHM_UPPER = $D{perfmBenchmUpper},\n" +
                "\ta.PERFM_BENCHM_LOWER = $D{perfmBenchmLower},\n" +
                "\ta.MSG_TYPE = $S{msgType},\n" +
                "\ta.PROD_CHANGE_DATE = $S{prodChangeDate},\n" +
                "\ta.CUSTODY_ORG_MNG_DUTY = $S{custodyOrgMngDuty},\n" +
                "\ta.INCOME_TRANS_PROD_MARK = $S{incomeTransProdMark},\n" +
                "\ta.CROSS_BORDER_WEALTH = $S{crossBorderWealth},\n" +
                "\ta.BASE_INFO_OPEN_MARK = $S{baseInfoOpenMark},\n" +
                "\ta.CHANGE_REASON = $S{changeReason},\n" +
                "\ta.PROD_RENEWAL_MARK = $S{prodRenewalMark},\n" +
                "\ta.LIQUIDATE_MARK = $S{liquidateMark},\n" +
                "\ta.PROD_STATUS = (case when b.PROD_STATUS > $S{prodStatus} then b.PROD_STATUS else $S{prodStatus} end),\n" +
                "\ta.UPD_DT = $S{updDt},\n" +
                "\ta.UPD_TIME = $S{updTime},\n" +
                "\ta.DATA_FLAG = $S{dataFlag} \n" +
                "where a.prod_code = $S{prodCode} ";
        return super.update(sql, params);
    }
    public UpdateResult updateProdSpvsInfo(ProdInfoOds params) throws Exception {
        String sql = "update ods_prod_spvs_info set " +
                "PBC_REGCODE = $S{pbcRegcode},"+
                "OPER_MOD = $S{operMod},"+
                "COLL_MOD = $S{collMod},"+
                "TERM_FLAG = $S{termFlag},"+
                "REDEEM_FLAG = $S{redeemFlag},"+
                "BORD_TRUSTI_CODE = $S{bordTrustiCode},"+
                "BORD_TRUSTI_CODE_P = $S{bordTrustiCodeP},"+
                "BORD_TRUSTI_NAME = $S{bordTrustiName},"+
                "OVERS_TRUSTI_NAME = $S{oversTrustiName},"+
                "OVERS_TRUSTI_NATION = $S{oversTrustiNation},"+
                "PROD_CREDIT_FLAG = $S{prodCreditFlag},"+
                "PROD_CREDIT_MOD = $S{prodCreditMod},"+
                "PROD_CREDIT_ORG = $S{prodCreditOrg},"+
                "AUTHOR_NAME = $S{authorName},"+
                "AUTHOR_IDENTIF = $S{authorIdentif},"+
                "DESIGN_NAME = $S{designName},"+
                "DESIGN_IDENTIF = $S{designIdentif},"+
                "MANAGE_NAME = $S{manageName},"+
                "MANAGE_IDENTIF = $S{manageIdentif},"+
                "SALEMAN_NAME = $S{salemanName},"+
                "SALEMAN_PHONENO = $S{salemanPhoneno},"+
                "SALEMAN_TELNO = $S{salemanTelno},"+
                "SALEMAN_EMAIL = $S{salemanEmail},"+
                "PROD_CYCLE = $S{prodCycle},"+
                "SALE_PLACE = $S{salePlace},"+
                "SRV_MODE = $S{srvMode},"+
                "ASSET_MAPING = $S{assetMaping},"+
                "MANAGE_MODE = $S{manageMode},"+
                "PRICING_TYPE = $S{pricingType},"+
                "COOPERATION_MODE = $S{cooperationMode},"+
                "RETURN_COST = $S{returnCost},"+
                "ADMIN_NAME = $S{adminName},"+
                "PROD_TIMES = $D{prodTimes},"+
                "RETURN_INCOME = $S{returnIncome},"+
                "PROD_PRECENT = $S{prodPrecent},"+
                "IS_STRUCTPROD = $S{isStructprod},"+
                "OPEN_MOD = $S{openMod},"+
                "REGULAR_OPEN_CYCLE = $S{regularOpenCycle},"+
                "OTH_REGUL_OPEN_CYC = $D{othRegulOpenCyc},"+
                "RGLR_PRD_OPN_CYC = $D{rglrPrdOpnCyc},"+
                "IRREGULAR_OPEN_DESC = $S{irregularOpenDesc},"+
                "FIRST_OPEN_DATE = $S{firstOpenDate},"+
                "IS_OPEN_IN_HOLIDAY = $S{isOpenInHoliday},"+
                "OPEN_TIMES = $D{openTimes},"+
                "OPEN_PERIOD_BUSINESS = $S{openPeriodBusiness},"+
                "OPEN_PERI_BUSI_DESC = $S{openPeriBusiDesc},"+
                "COOPERATION_ORG_NAME = $S{cooperationOrgName},"+
                "NEW_OLD_PROD_F = $S{newOldProdF},"+
                "PROD_SAL_ZON = $S{prodSalZon},"+
                "PROD_ESP_PRPT = $S{prodEspPrpt},"+
                "CLSF_STO = $D{clsfSto},"+
                "SPECI_COUNTRY_REGION = $S{speciCountryRegion},"+
                "INVEST_THRESH = $D{investThresh},"+
                "SALE_COMMIS_RATE = $D{saleCommisRate},"+
                "CUSTODY_FEE_RATE = $D{custodyFeeRate},"+
                "REDEEM_FLAG_PB = $S{redeemFlagPb}, "+
                "UPD_DT = $S{updDt},"+
                "UPD_TIME = $S{updTime}"+
                "where prod_code = $S{prodCode}";
        return super.update(sql, params);
    }

    /**
     * @methodName updateProdPbc1
     * @description 更新ods_prod_core_info表中的原始人行代码
     * @param params params
     * @return com.kayak.core.sql.UpdateResult
     */
    public UpdateResult updateProdCoreOldPbc(ProdInfoOds params) throws Exception {
        String sql = "update ods_prod_core_info set old_pbc_regcode = PBC_REGCODE where PROD_CD = $S{prodCode}";
        return super.update(sql, params);
    }

    /**
     * @methodName getPbcRegCode
     * @description 获取人行代码（原来的）
     * @param params 参数
     * @return java.lang.String
     */
    public String getPbcRegCode(ProdInfoOds params) throws Exception {
        String sql = "select PBC_REGCODE from ods_prod_core_info where PROD_CD = $S{prodCode}";
        List<SqlRow> rows = super.findRows(sql, params);
        if (!CollectionUtils.isEmpty(rows)) {
            String pbcRegcode = rows.get(0).getString("PBC_REGCODE");
            return Objects.nonNull(pbcRegcode) ? pbcRegcode : "";
        }
        return "";
    }

    public UpdateResult updateProdPbc(ProdInfoOds params,String oldPbcRegcode) throws Exception {
    String sql = "replace into ods_prod_core_info (PROD_CD,PBC_REGCODE,old_PBC_REGCODE) values ($S{prodCode},$S{pbcRegcode},'" + oldPbcRegcode + "')";
        return super.update(sql, params);
    }

    public UpdateResult updateProdPbcDly(ProdInfoOds params) throws Exception {
        String sql = "update dws_dly_inv_trd_dtl set PRDC_CD_PBC = $S{pbcRegcode} where PRDC_CD = $S{prodCode}";
        return super.update(sql, params);
    }

    public void updateProdInfo(ProdInfoOds params) throws Exception {
        updateProdInfoOds(params);
        updateProdSpvsInfo(params);
        String oldPbcRegcode = getPbcRegCode(params);
        log.info("人行登记编码必须为Z开头！！！");
        if(params.getPbcRegcode() != null && params.getPbcRegcode().toUpperCase().startsWith("Z")){
            updateProdPbc(params,oldPbcRegcode);
            updateProdPbcDly(params);
        }
    }

    public UpdateResult deleteIssuanceRegist (ProdInfoOds params) throws Exception {
        String sql = "delete from app_prod_issuance_regist_info  where PROD_IDENT_CODE = $S{prodCode} and REGISTER_STATUS != 3 ";
        return super.update(sql, params);
    }

    public String findIssuanceRegist(ProdInfoOds prodInfoOds) throws Exception {
        String cnt ="";
        String sql = "select count(1) cnt from app_prod_issuance_regist_info  where PROD_IDENT_CODE = '"+prodInfoOds.getProdCode()+"' and REGISTER_STATUS = 3 ";
        if(super.findRows(sql).size()>0){
            cnt=super.findRows(sql).get(0).getString("cnt");
        }
        return cnt;
    }

    public void updateProdInfoReport(ProdInfoOds params) throws Exception {
        doTrans(() -> {
        String sql = "update ods_prod_base_info set " +
                "prod_report_status = $S{prodReportStatus}," +
                "check_inon = $S{checkInon} " +
                "where prod_code = $S{prodCode}";
        super.update(sql, params);
        String sql1 = "update app_prod_regist_filing_info set " +
                "REGISTER_STATUS = $S{prodReportStatus} " +
                "where IDENT_CODE = $S{prodCode}";
        super.update(sql1, params);
        });
    }
    /**
     * 记录产品管理文件log
     * @param params
     * @throws Exception
     */
    public void createFileLog(Map<String, Object> params) throws Exception{
        comnDao.update("insert into base_port_file_log (\n" +
                " sequence,\n" +
                " port_code,\n" +
                " port_name,\n" +
                " port_type,\n" +
                " port_dir,\n" +
                " file_state,\n" +
                " exec_message,\n" +
                " deal_user_id,\n" +
                " deal_date,\n" +
                " crt_date,\n" +
                " crt_time ) \n" +
                " values (\n" +
                " $S{sequence},\n" +
                " $S{port_code},\n" +
                " '产品管理系统文件交互',\n" +
                " '7',\n" +
                " $S{port_dir},\n" +
                " $S{file_state},\n" +
                " '处理中',\n" +
                " $S{deal_user_id},\n" +
                " $S{deal_date},\n" +
                " DATE_FORMAT(NOW(), '%Y%m%d'),\n" +
                " DATE_FORMAT(NOW(), '%H%i%s')\n" +
                " )", params);
    }

    /**
     * 更新推送文件log
     * @param params
     * @throws Exception
     */
    public void updateFileLog(Map<String, Object> params) throws Exception{
        comnDao.update("update base_port_file_log\n" +
                " set file_state = $S{fileState},\n" +
                " exec_message = $S{message},\n" +
                " total_num = $S{totalNum},\n" +
                " deal_user_id = $S{deal_user_id},\n" +
                " upd_date = DATE_FORMAT(NOW(), '%Y%m%d'),\n" +
                " upd_time = DATE_FORMAT(NOW(), '%H%i%s')\n" +
                " where sequence = $S{sequence}", params);
    }

    /**
     * 更新文件推送状态
     * @param params
     * @throws Exception
     */
    public void updateProdStatus(Map<String, Object> params) throws Exception{
        comnDao.update("update ods_prod_base_info set file_status ='1' where id = $S{id}", params);
    }

    public List<ProdInfoOds> findProdList(ProdInfoOds ProdInfoOds) throws Exception {
        String sql = "select prod_code,check_inon,prod_report_status from ods_prod_base_info where file_status <>'1' and prod_report_status <>'0' and check_inon <>'' ";
        return super.findRows(ProdInfoOds.class, sql,
                DataSourceProperty.PUB, ProdInfoOds);
    }

    /**
     * 查询成立日期
     * @param  prodCode
     * @return
     * @throws Exception
     */
    public String findEstablishDate(String prodCode) throws Exception {
        String establish_date="";
        HashMap<String, Object> params =  new HashMap<>();
        params.put("prodCode", prodCode);
        String sql = "select establish_date from ods_prod_base_info where CHECK_INON = $S{prodCode} and MOTHER_FUND_FLAG=1";
        if(super.findRows(sql,params).size()>0){
            establish_date=super.findRows(sql,params).get(0).getString("establish_date");
        }
        return establish_date;
    }

    /**
     * 获取提交审批的数据
     * @param  prodCode
     * @return
     * @throws Exception
     */
      public String findWorkflowValues(String prodCode,String serverName) throws Exception {
          String submitData ="";
          String sql = "select submit_data from flow_busi_info t left join flow_busi_config t1 on t.process_key = t1.process_key where `values` = '"+prodCode+"' and process_status = '2' and bus_status ='1' and t1.server like '%"+serverName+"%' order by t.create_time desc limit 1 ";
          if(super.findRows(sql).size()>0){
              submitData=super.findRows(sql).get(0).getString("submit_data");
          }
          return submitData;
      }

      /**
       * @methodName updateReport
       * @description 修改人行对应报表的
       * @param list  参数信息
       * @return void
       */
    public void updateReport(List<HashMap<String, Object>> list) throws Exception {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String settle_date = DateUtil.getPreviousMonthLastDay(date, 0);
        String additional_cond = " 1=1 ";

        String sql = "update $U{table_name} tt " +
                "join ods_prod_core_info t1 on tt.$U{product_code_col} = t1.PROD_CD " +
                " join ods_prod_base_info t2 on t1.PROD_CD = t2.PROD_CODE" +
                " set tt.$U{register_code_col} = t1.PBC_REGCODE where t2.UPD_DT = '" + date + "'";

        String sql2 = "update $U{table_name} tt " +
                "join ods_prod_core_info t1 on tt.$U{product_code_col} = t1.old_pbc_regcode " +
                " join ods_prod_base_info t2 on t1.PROD_CD = t2.PROD_CODE" +
                " set tt.$U{register_code_col} = t1.PBC_REGCODE where " + additional_cond + " and t2.UPD_DT = '" + date + "'";

        for (HashMap<String, Object> params : list) {
            UpdateResult update1 = comnDao.update(sql, params);
            int effect = update1.getEffect();
            log.info("update1条数{}",effect);

            if (Objects.equals(params.get("product_code_col"),params.get("register_code_col"))) {
                if(String.valueOf(params.get("table_name")).toUpperCase().contains("APP_PBC_REPORT_ZG04")) {
                    sql2 = sql2.replace("1=1", "tt.report_date = $S{report_date}");
                }
                params.put("report_date", settle_date);
                UpdateResult update2 = comnDao.update(sql2, params);
                int effect1 = update2.getEffect();
                log.info("effect2条数{}",effect1);
            }
        }
    }

    public SqlResult<ProdInfoOds> prodInfoCheck(SqlParam<ProdInfoOds> params) throws Exception {
        return super.findRows(ExeQuery.queryExeId("CPCHECK001"), params);
    }

}
