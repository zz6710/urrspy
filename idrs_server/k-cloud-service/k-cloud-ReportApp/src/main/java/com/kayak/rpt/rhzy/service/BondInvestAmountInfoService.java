package com.kayak.rpt.rhzy.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.rpt.rhzy.dao.BondInvestAmountInfoDao;
import com.kayak.rpt.rhzy.model.BondInvestAmountInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "债券投资发生额信息服务", model = BondInvestAmountInfo.class)
public class BondInvestAmountInfoService implements ExcelImportService<BondInvestAmountInfo> {
	private static final Logger log = LoggerFactory.getLogger(BondInvestInfoService.class);

	@Autowired
	private BondInvestAmountInfoDao bondInvestAmountInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询债券投资发生额信息信息", auth = APIAuth.YES)
	public SqlResult<BondInvestAmountInfo> findBondInvestAmountInfos(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return bondInvestAmountInfoDao.findBondInvestAmountInfos(params);
	}
	
	@API(desc = "修改债券投资发生额信息", params = "id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept,trade_date,trade_ser_no,trade_amount,trade_amount_rmb,trade_flag", auth = APIAuth.YES)
	public int updateBondInvestAmountInfo(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return bondInvestAmountInfoDao.updateBondInvestAmountInfo(params).getEffect();
	}
	
	@API(desc = "删除债券投资发生额信息", params = "id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept,trade_date,trade_ser_no,trade_amount,trade_amount_rmb,trade_flag", auth = APIAuth.YES)
	public int deleteBondInvestAmountInfo(SqlParam<BondInvestAmountInfo> params) throws Exception {
		return bondInvestAmountInfoDao.deleteBondInvestAmountInfo(params).getEffect();
	}

	public void deleteBondInvestAmountInfoByDate(Object params) throws Exception{
		try {
			bondInvestAmountInfoDao.deleteBondInvestAmountInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void importFile(List<BondInvestAmountInfo> bondInvestAmountInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_bond_invest_amount_info`(`report_date`, `org_code`, `inner_org_code`, `bond_code`, `bond_trustsp_org`, `bond_cate`, `bond_credit_grade`, `cur`, `debt_reg_date`, `value_date`, `redem_date`, `coupon_rate`, `issuer_id_code`, `issuer_region_code`, `issuer_industry`, `issuer_entp_scale`, `issuer_eco_sector`, `issuer_eco_dept`, `trade_date`, `trade_ser_no`, `trade_amount`, `trade_amount_rmb`, `trade_flag`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (BondInvestAmountInfo info : bondInvestAmountInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode());
					ps.setString(4,info.getBondCode());
					ps.setString(5,info.getBondTrustspOrg());
					ps.setString(6,info.getBondCate());
					ps.setString(7,info.getBondCreditGrade());
					ps.setString(8,info.getCur());
					ps.setString(9,info.getDebtRegDate());
					ps.setString(10,info.getValueDate());
					ps.setString(11,info.getRedemDate());
					ps.setString(12,info.getCouponRate());
					ps.setString(13,info.getIssuerIdCode());
					ps.setString(14,info.getIssuerRegionCode());
					ps.setString(15,info.getIssuerIndustry());
					ps.setString(16,info.getIssuerEntpScale());
					ps.setString(17,info.getIssuerEcoSector());
					ps.setString(18,info.getIssuerEcoDept());
					ps.setString(19,info.getTradeDate());
					ps.setString(20,info.getTradeSerNo());
					ps.setString(21,info.getTradeAmount());
					ps.setString(22,info.getTradeAmountRmb());
					ps.setString(23,info.getTradeFlag());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", bondInvestAmountInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入资管产品存续期募集信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();
			}
		});
	}

}
