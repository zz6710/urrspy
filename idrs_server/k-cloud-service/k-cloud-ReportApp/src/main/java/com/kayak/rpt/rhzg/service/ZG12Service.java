package com.kayak.rpt.rhzg.service;


import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.dao.ZG12Dao;
import com.kayak.rpt.rhzg.model.ZG12;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@APIDefine(desc = "除资产收益权外其他债权明细信息服务", model = ZG12.class)
public class ZG12Service implements ExcelImportService<ZG12> {

    private static final Logger log = LoggerFactory.getLogger(ZG12Service.class);

    @Autowired
    private ZG12Dao zG12Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询除资产收益权外其他债权明细信息", auth = APIAuth.YES)
    public SqlResult<ZG12> findZG12s(SqlParam<ZG12> params) throws Exception {
        return zG12Dao.findZG12s(params);
    }


    @API(desc = "修改除资产收益权外其他债权明细信息",params = "id,prod_cd,report_date,borrower_typ,zone,borrower_cd,industry_msg,enter_sponsor_eco_sector,enter_scale,borrower_asset_cd,borrower_real_trans,borrower_issue_dt,borrower_end_dt,borrower_extension_dt,is_fixed_rate,rate_level,guarantee_mode,ori_contract_ccy,ori_contract_amt,ori_contract_amt_cny,borrower_balance_ccy,borrower_balance,borrower_balance_cny,type_of_debt,register_trading_place,register_trading_code,register_status,theory_report_start_date,theory_report_end_date" , auth = APIAuth.YES)
    public int updateZG12(SqlParam<ZG12> params) throws Exception {
        params.getModel().setZoneText(params.getModel().zoneText==null ? null : params.getModel().zoneText.split(":")[0]);
        return zG12Dao.updateZG12(params).getEffect();
    }


    @API(desc = "删除除资产收益权外其他债权明细信息", params = "id", auth = APIAuth.YES)
    public int deleteZG12(SqlParam<ZG12> params) throws Exception {
        return zG12Dao.deleteZG12(params).getEffect();
    }


    public void importFile(List<ZG12> zg12s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg12(prod_cd,borrower_typ,zone,borrower_cd,industry_msg,enter_sponsor_eco_sector,enter_scale,borrower_asset_cd,borrower_real_trans,borrower_issue_dt,borrower_end_dt,borrower_extension_dt,is_fixed_rate,rate_level,guarantee_mode,ori_contract_ccy,ori_contract_amt,ori_contract_amt_cny,borrower_balance_ccy,borrower_balance,borrower_balance_cny,type_of_debt,register_trading_place,register_trading_code,register_status,report_date,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,tech_flag,green_flag,spec_flag,aged_flag,num_core_flag) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG12 info : zg12s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,info.getBorrowerTyp()==null ? null : info.getBorrowerTyp().split(":")[0]);
                    ps.setString(3,info.getZone()==null ? null : info.getZone().split(":")[0]);
                    ps.setString(4,info.getBorrowerCd());
                    ps.setString(5,info.getIndustryMsg()==null ? null : info.getIndustryMsg().split(":")[0]);
                    ps.setString(6,info.getEnterSponsorEcoSector()==null ? null : info.getEnterSponsorEcoSector().split(":")[0]);
                    ps.setString(7,info.getEnterScale()==null ? null : info.getEnterScale().split(":")[0]);
                    ps.setString(8,info.getBorrowerAssetCd());
                    ps.setString(9,info.getBorrowerRealTrans()==null ? null : info.getBorrowerRealTrans().split(":")[0]);
                    ps.setString(10,info.getBorrowerIssueDt().substring(0,10));
                    ps.setString(11,info.getBorrowerEndDt().substring(0,10));
                    ps.setString(12,info.getBorrowerExtensionDt());
                    ps.setString(13,info.getIsFixedRate()==null ? null : info.getIsFixedRate().split(":")[0]);
                    ps.setString(14,info.getRateLevel());
                    ps.setString(15,info.getGuaranteeMode()==null ? null : info.getGuaranteeMode().split(":")[0]);
                    ps.setString(16,info.getOriContractCcy());
                    ps.setString(17,info.getOriContractAmt());
                    ps.setString(18,info.getOriContractAmtCny());
                    ps.setString(19,info.getBorrowerBalanceCcy());
                    ps.setString(20,info.getBorrowerBalance());
                    ps.setString(21,info.getBorrowerBalanceCny());
                    ps.setString(22,info.getTypeOfDebt()==null ? null : info.getTypeOfDebt().split(":")[0]);
                    ps.setString(23,info.getRegisterTradingPlace()==null ? null : info.getRegisterTradingPlace().split(":")[0]);
                    ps.setString(24,info.getRegisterTradingCode()==null ? null : info.getRegisterTradingCode().split(":")[0]);
                    ps.setString(25,"0");
                    ps.setString(26,map.get("beginDate").toString());
                    ps.setString(27,"1");
                    ps.setString(28, "2");//sys_data_source 2
                    ps.setString(29, "1.0");//sys_data_version
                    ps.setString(30, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(31, DateUtil.getNowDate());// imp_date
                    ps.setString(32,map.get("beginDate").toString());
                    ps.setString(33,info.getTechFlag()==null ? null : info.getTechFlag().split(":")[0]);
                    ps.setString(34,info.getGreenFlag()==null ? null : info.getGreenFlag().split(":")[0]);
                    ps.setString(35,info.getSpecFlag()==null ? null : info.getSpecFlag().split(":")[0]);
                    ps.setString(36,info.getAgedFlag()==null ? null : info.getAgedFlag().split(":")[0]);
                    ps.setString(37,info.getNumCoreFlag()==null ? null : info.getNumCoreFlag().split(":")[0]);
                    ps.addBatch();

                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg12s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入除资产收益权外其他债权明细信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg12ByDate(Object params) throws Exception{
        try {
            zG12Dao.deleteZg12ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    private void solveZG12(ZG12 info) {
//        info.setRegisterStatus("0");
//        info.setLoanTyp(info.getLoanTyp()==null ? null : info.getLoanTyp().split(":")[0]);
//        info.setBorrowerTyp(info.getBorrowerTyp()==null ? null : info.getBorrowerTyp().split(":")[0]);
//        info.setIndustryMsg(info.getIndustryMsg()==null ? null : info.getIndustryMsg().split(":")[0]);
//        info.setEnterSponsorEcoSector(info.getEnterSponsorEcoSector()==null ? null : info.getEnterSponsorEcoSector().split(":")[0]);
//        info.setEnterScale(info.getEnterScale()==null ? null : info.getEnterScale().split(":")[0]);
//        info.setLoanProdTyp(info.getLoanProdTyp()==null ? null : info.getLoanProdTyp().split(":")[0]);
//        info.setIsFixedRate(info.getIsFixedRate()==null ? null : info.getIsFixedRate().split(":")[0]);
//        info.setGuaranteeMode(info.getGuaranteeMode()==null ? null : info.getGuaranteeMode().split(":")[0]);
//        info.setLoanQuality(info.getLoanQuality()==null ? null : info.getLoanQuality().split(":")[0]);
//        info.setLoanStatus(info.getLoanStatus()==null ? null : info.getLoanStatus().split(":")[0]);
//        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));
    }

    @API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> addclcSourceZonCdDict(SqlParam<ZG12> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        List<SqlRow> tempTypeByDocType = zG12Dao.addclcSourceZonCdDict(paramsDirect);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }


}
