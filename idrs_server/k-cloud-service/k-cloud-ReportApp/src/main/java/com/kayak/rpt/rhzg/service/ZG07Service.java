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
import com.kayak.rpt.rhzg.dao.ZG07Dao;
import com.kayak.rpt.rhzg.model.ZG07;
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
@APIDefine(desc = "除回购和拆借外贷款明细信息服务", model = ZG07.class)
public class ZG07Service implements ExcelImportService<ZG07> {

    private static final Logger log = LoggerFactory.getLogger(ZG07Service.class);

    @Autowired
    private ZG07Dao zG07Dao;

    @Autowired
    private ComnDao comnDao;


    @API(desc = "查询除回购和拆借外贷款明细信息", auth = APIAuth.YES)
    public SqlResult<ZG07> findZG07s(SqlParam<ZG07> params) throws Exception {
       // params.setMakeSql(true);
        return zG07Dao.findZG07s(params);
    }


    @API(desc = "修改除回购和拆借外贷款明细信息",params = "id,prod_code,bi_type,zone,cus_type,chu_mount,zhe_mount,fen_mount" , auth = APIAuth.YES)
    public int updateZG07(SqlParam<ZG07> params) throws Exception {
        params.getModel().setZoneText(params.getModel().zoneText==null ? null : params.getModel().zoneText.split(":")[0]);
        return zG07Dao.updateZG07(params).getEffect();
    }


    @API(desc = "删除除回购和拆借外贷款明细信息", params = "id", auth = APIAuth.YES)
    public int deleteZG07(SqlParam<ZG07> params) throws Exception {
        return zG07Dao.deleteZG07(params).getEffect();
    }


    public void importFile(List<ZG07> zg07s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();

        String batchSql = "INSERT INTO app_pbc_report_zg07(prod_cd,loan_typ,loan_trans_org_cd,loan_contract_ori_cd,loan_contract_ori_zone,borrower_typ,zone,borrower_cd,industry_msg,enter_sponsor_eco_sector,enter_scale,loan_receipt_cd,loan_prod_typ,loan_real_trans,loan_issue_dt,loan_end_dt,loan_extension_dt,is_fixed_rate,rate_level,guarantee_mode,loan_quality,loan_status,loan_trans_discount_rate,ori_contract_ccy,ori_contract_amt,ori_contract_amt_cny,loan_balance_ccy,loan_balance,loan_balance_cny,register_status,report_date,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,tech_flag,green_flag,spec_flag,aged_flag,num_core_flag) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {

                for (ZG07 info : zg07s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,info.getLoanTyp()==null ? null : info.getLoanTyp().split(":")[0]);
                    ps.setString(3,info.getLoanTransOrgCd());
                    ps.setString(4,info.getLoanContractOriCd());
                    ps.setString(5,info.getLoanContractOriZone());
                    ps.setString(6,info.getBorrowerTyp()==null ? null : info.getBorrowerTyp().split(":")[0]);
                    ps.setString(7,info.getZone()==null ? null : info.getZone().split(":")[0]);
                    ps.setString(8,info.getBorrowerCd());
                    ps.setString(9,info.getIndustryMsg()==null ? null : info.getIndustryMsg().split(":")[0]);
                    ps.setString(10,info.getEnterSponsorEcoSector()==null ? null : info.getEnterSponsorEcoSector().split(":")[0]);
                    ps.setString(11,info.getEnterScale()==null ? null : info.getEnterScale().split(":")[0]);
                    ps.setString(12,info.getLoanReceiptCd());
                    ps.setString(13,info.getLoanProdTyp()==null ? null : info.getLoanProdTyp().split(":")[0]);
                    ps.setString(14,info.getLoanRealTrans()==null ? null : info.getLoanRealTrans().split(":")[0]);
                    ps.setString(15,info.getLoanIssueDt());
                    ps.setString(16,info.getLoanEndDt());
                    ps.setString(17,info.getLoanExtensionDt());
                    ps.setString(18,info.getIsFixedRate()==null ? null : info.getIsFixedRate().split(":")[0]);
                    ps.setString(19,info.getRateLevel());
                    ps.setString(20,info.getGuaranteeMode()==null ? null : info.getGuaranteeMode().split(":")[0]);
                    ps.setString(21,info.getLoanQuality()==null ? null : info.getLoanQuality().split(":")[0]);
                    ps.setString(22,info.getLoanStatus()==null ? null : info.getLoanStatus().split(":")[0]);
                    ps.setString(23,info.getLoanTransDiscountRate());
                    ps.setString(24,info.getOriContractCcy());
                    ps.setString(25,info.getOriContractAmt());
                    ps.setString(26,info.getOriContractAmtCny());
                    ps.setString(27,info.getLoanBalanceCcy());
                    ps.setString(28,info.getLoanBalance());
                    ps.setString(29,info.getLoanBalanceCny());
                    ps.setString(30,"0");
                    ps.setString(31,map.get("beginDate").toString());
                    ps.setString(32,"1");
                    ps.setString(33, "2");//sys_data_source 2
                    ps.setString(34, "1.0");//sys_data_version
                    ps.setString(35, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(36, DateUtil.getNowDate());// imp_date
                    ps.setString(37,map.get("beginDate").toString());
                    ps.setString(38,info.getTechFlag()==null ? null : info.getTechFlag().split(":")[0]);
                    ps.setString(39,info.getGreenFlag()==null ? null : info.getGreenFlag().split(":")[0]);
                    ps.setString(40,info.getSpecFlag()==null ? null : info.getSpecFlag().split(":")[0]);
                    ps.setString(41,info.getAgedFlag()==null ? null : info.getAgedFlag().split(":")[0]);
                    ps.setString(42,info.getNumCoreFlag()==null ? null : info.getNumCoreFlag().split(":")[0]);
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", zg07s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入除回购和拆借外贷款明细信息异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });

    }

    public void deleteZg07ByDate(Object params) throws Exception{
        try {
            zG07Dao.deleteZg07ByDate(params);
        } catch (Exception e) {
            throw e;
        }
    }

    private void solveZG07(ZG07 info) {
        info.setRegisterStatus("0");
        info.setLoanTyp(info.getLoanTyp()==null ? null : info.getLoanTyp().split(":")[0]);
        info.setBorrowerTyp(info.getBorrowerTyp()==null ? null : info.getBorrowerTyp().split(":")[0]);
        info.setIndustryMsg(info.getIndustryMsg()==null ? null : info.getIndustryMsg().split(":")[0]);
        info.setEnterSponsorEcoSector(info.getEnterSponsorEcoSector()==null ? null : info.getEnterSponsorEcoSector().split(":")[0]);
        info.setEnterScale(info.getEnterScale()==null ? null : info.getEnterScale().split(":")[0]);
        info.setLoanProdTyp(info.getLoanProdTyp()==null ? null : info.getLoanProdTyp().split(":")[0]);
        info.setIsFixedRate(info.getIsFixedRate()==null ? null : info.getIsFixedRate().split(":")[0]);
        info.setGuaranteeMode(info.getGuaranteeMode()==null ? null : info.getGuaranteeMode().split(":")[0]);
        info.setLoanQuality(info.getLoanQuality()==null ? null : info.getLoanQuality().split(":")[0]);
        info.setLoanStatus(info.getLoanStatus()==null ? null : info.getLoanStatus().split(":")[0]);
        info.setTheoryReportStartDate(info.getTheoryReportStartDate()==null ? null : info.getTheoryReportStartDate().replace("-",""));
    }

    @API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
    public SqlResult<SqlRow> addclcSourceZonCdDict(SqlParam<ZG07> params) throws Exception {
        Map<String, Object> paramsDirect = params.getParamsDirect();
        List<SqlRow> tempTypeByDocType = zG07Dao.addclcSourceZonCdDict(paramsDirect);
        SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
        sqlRowSqlResult.setResults(tempTypeByDocType.size());
        sqlRowSqlResult.setRows(tempTypeByDocType);
        sqlRowSqlResult.setDesensitized(false);;
        return sqlRowSqlResult;
    }


}
