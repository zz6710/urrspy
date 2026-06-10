package com.kayak.rpt.rhzg.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.rhzg.model.ZG06;
import com.kayak.rpt.rhzg.model.ZG12;
import com.kayak.subject.model.PubReq;
import com.kayak.subject.service.RptBusinessBaseTaskService;
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
@APIDefine(desc = "ZG06文件导入服务", model = ZG06.class)
public class ZG06ImportService extends RptBusinessBaseTaskService implements ExcelImportService<ZG06> {

    private static final Logger log = LoggerFactory.getLogger(ZG06ImportService.class);

    @Autowired
    private ComnDao comnDao;

    @API(desc = "删除文件导入ZG06的数据", params = "id", auth = APIAuth.YES)
    public int deleteZG06Buffer(String dealDate) throws Exception {
        String strSql = "truncate table import_zg06_buffer";
        return comnDao.update(strSql, dealDate).getEffect();
    }

    /**
     * 调用任务处理ZG06临时数据
     */
    public void callTaskDealImportZG06(String dealDate) throws Exception{
        PubReq request = new PubReq();
        request.setTaskId("M099");
        request.setTaskDate(dealDate);
        super.beforeClear(request);
        super.dataModeExConvert(request);
    }

    /**
     * ZG06文件导入到临时表
     * @param zg06s
     * @param map
     * @throws Exception
     */
    public void importFile(List<ZG06> zg06s, Map map) throws Exception {
        long startTime = System.currentTimeMillis();
        String batchSql = "INSERT INTO import_zg06_buffer(prod_cd,report_date,register_status,asset_debt_project,asset_income_code,base_asset_sale_org_nm,base_asset_sale_org_cd,base_asset_sale_org_typ,base_asset_sale_org_industry,base_asset_sale_org_reg_zone,base_asset_sale_org_econo_sector,base_asset_sale_org_scale,trans_dt,trans_scheduled_end_dt,trans_extension_dt,base_asset_typ,base_asset_ori_prot_ccy,base_asset_ori_prot_amt,base_asset_ori_prot_amt_cny,base_asset_trans_ccy,base_asset_trans_amt,base_asset_trans_amt_cny,trans_org_out_table_f,trans_org_buy_back_f,is_fixed_rate,rate_level,guarantee_mode,base_asset_trans_dep,base_asset_end_dt_ccy,base_asset_end_dt_balance,base_asset_end_dt_balance_cny,register_trading_place,register_trading_code,sys_data_status,sys_data_source,sys_data_version,register_serno,imp_date,theory_report_start_date,tech_flag,green_flag,spec_flag,aged_flag,num_core_flag,base_asset_inv_obj_idt,base_asset_inv_obj_scale) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                for (ZG06 info : zg06s){
                    ps.setString(1,info.getProdCd());
                    ps.setString(2,map.get("dealDate").toString());
                    ps.setString(3,"0");
                    ps.setString(4,info.getAssetDebtProject()==null ? null : info.getAssetDebtProject().split(":")[0]);
                    ps.setString(5,info.getAssetIncomeCode());
                    ps.setString(6,info.getBaseAssetSaleOrgNm());
                    ps.setString(7,info.getBaseAssetSaleOrgCd());
                    ps.setString(8,info.getBaseAssetSaleOrgTyp()==null ? null : formatValue(info.getBaseAssetSaleOrgTyp().split(":")[0]));
                    ps.setString(9,info.getBaseAssetSaleOrgIndustry()==null ? null : info.getBaseAssetSaleOrgIndustry().split(":")[0]);
                    ps.setString(10,info.getBaseAssetSaleOrgRegZone()==null ? null : formatValue(info.getBaseAssetSaleOrgRegZone().split(":")[0]));
                    ps.setString(11,info.getBaseAssetSaleOrgEconoSector()==null ? null : info.getBaseAssetSaleOrgEconoSector().split(":")[0]);
                    ps.setString(12,info.getBaseAssetSaleOrgScale()==null ? null : info.getBaseAssetSaleOrgScale().split(":")[0]);
                    ps.setString(13,formateDate(info.getTransDt()));
                    ps.setString(14,formateDate(info.getTransScheduledEndDt()));
                    ps.setString(15,formateDate(info.getTransExtensionDt()));
                    ps.setString(16,info.getBaseAssetTyp()==null ? null : formatValue(info.getBaseAssetTyp().split(":")[0]));
                    ps.setString(17,info.getBaseAssetOriProtCcy());
                    ps.setString(18,info.getBaseAssetOriProtAmt());
                    ps.setString(19,info.getBaseAssetOriProtAmtCny());
                    ps.setString(20,info.getBaseAssetTransCcy());
                    ps.setString(21,info.getBaseAssetTransAmt());
                    ps.setString(22,info.getBaseAssetTransAmtCny());
                    ps.setString(23,info.getTransOrgOutTableF()==null ? null : formatValue(info.getTransOrgOutTableF().split(":")[0]));
                    ps.setString(24,info.getTransOrgBuyBackF()==null ? null : formatValue(info.getTransOrgBuyBackF().split(":")[0]));
                    ps.setString(25,info.getIsFixedRate()==null ? null : info.getIsFixedRate().split(":")[0]);
                    ps.setString(26,info.getRateLevel());
                    ps.setString(27,info.getGuaranteeMode()==null ? null : info.getGuaranteeMode().split(":")[0]);
                    ps.setString(28,info.getBaseAssetTransDep()==null ? null : formatValue(info.getBaseAssetTransDep().split(":")[0]));
                    ps.setString(29,info.getBaseAssetEndDtCcy());
                    ps.setString(30,info.getBaseAssetEndDtBalance());
                    ps.setString(31,info.getBaseAssetEndDtBalanceCny());
                    ps.setString(32,info.getRegisterTradingPlace()==null ? null : info.getRegisterTradingPlace().split(":")[0]);
                    ps.setString(33,info.getRegisterTradingCode());
                    ps.setString(34,"1");
                    ps.setString(35, "2");//sys_data_source 2
                    ps.setString(36, "1.0");//sys_data_version
                    ps.setString(37, UUID.randomUUID().toString());//register_serno 2
                    ps.setString(38, DateUtil.getNowDate());// imp_date
                    ps.setString(39,map.get("dealDate").toString());
                    ps.setString(40,info.getTechFlag()==null ? null : formatValue(info.getTechFlag().split(":")[0]));
                    ps.setString(41,info.getGreenFlag()==null ? null : formatValue(info.getGreenFlag().split(":")[0]));
                    ps.setString(42,info.getSpecFlag()==null ? null : formatValue(info.getSpecFlag().split(":")[0]));
                    ps.setString(43,info.getAgedFlag()==null ? null : formatValue(info.getAgedFlag().split(":")[0]));
                    ps.setString(44,info.getNumCoreFlag()==null ? null : formatValue(info.getNumCoreFlag().split(":")[0]));
                    ps.setString(45,info.getBaseAssetInvObjIdt()==null ? null : info.getBaseAssetInvObjIdt().split(":")[0]);
                    ps.setString(46,info.getBaseAssetInvObjScale()==null ? null : info.getBaseAssetInvObjScale().split(":")[0]);
                    ps.addBatch();
                }
                ps.executeBatch();

                log.info(" ##### 批量入库import_zg06_buffer 数量：{}耗时: {} ms", zg06s.size(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入import_zg06_buffer临时表异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();
            }
        });

    }

    private String formateDate(String date){
        String result = "";
        if(date != null){
            if(date.length()>=10){
                result = date.substring(0,10);
            }else{
                result = date;
            }
        }
        return result;
    }

    private String formatValue(String value){
        String result = "";
        if(value != null && value.length() >0){
            int idx = value.indexOf(".");
            if(idx >0){
                result = value.substring(0,idx);
            }else{
                result = value;
            }
        }
        return result;
    }
}
