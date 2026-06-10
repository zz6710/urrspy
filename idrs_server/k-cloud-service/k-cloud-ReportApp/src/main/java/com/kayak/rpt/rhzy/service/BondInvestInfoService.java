package com.kayak.rpt.rhzy.service;

import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
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
import com.kayak.rpt.rhzy.dao.BondInvestInfoDao;
import com.kayak.rpt.rhzy.model.BondInvestInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "存量债券投资信息服务", model = BondInvestInfo.class)
public class BondInvestInfoService implements ExcelImportService<BondInvestInfo> {
	private static final Logger log = LoggerFactory.getLogger(BondInvestInfoService.class);

	@Autowired
	private BondInvestInfoDao bondInvestInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询存量债券投资信息信息", auth = APIAuth.YES)
	public SqlResult<BondInvestInfo> findBondInvestInfos(SqlParam<BondInvestInfo> params) throws Exception {
		return bondInvestInfoDao.findBondInvestInfos(params);
	}
	
	@API(desc = "修改存量债券投资信息", params = "id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,bond_balance,bond_balance_rmb,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept", auth = APIAuth.YES)
	public int updateBondInvestInfo(SqlParam<BondInvestInfo> params) throws Exception {
		return bondInvestInfoDao.updateBondInvestInfo(params).getEffect();
	}
	
	@API(desc = "删除存量债券投资信息", params = "id,org_code,report_date,inner_org_code,bond_code,bond_trustsp_org,bond_cate,bond_credit_grade,cur,bond_balance,bond_balance_rmb,debt_reg_date,value_date,redem_date,coupon_rate,issuer_id_code,issuer_region_code,issuer_industry,issuer_entp_scale,issuer_eco_sector,issuer_eco_dept", auth = APIAuth.YES)
	public int deleteBondInvestInfo(SqlParam<BondInvestInfo> params) throws Exception {
		return bondInvestInfoDao.deleteBondInvestInfo(params).getEffect();
	}

	public void deleteBondInvestInfoByDate(Object params) throws Exception{
		try {
			bondInvestInfoDao.deleteBondInvestInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void importFile(List<BondInvestInfo> bondInvestInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_bond_invest_info`(`report_date`, `org_code`, `inner_org_code`, `bond_code`, `bond_trustsp_org`, `bond_cate`, `bond_credit_grade`, `cur`, `bond_balance`, `bond_balance_rmb`, `debt_reg_date`, `value_date`, `redem_date`, `coupon_rate`, `issuer_id_code`, `issuer_region_code`, `issuer_industry`, `issuer_entp_scale`, `issuer_eco_sector`, `issuer_eco_dept`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (BondInvestInfo info : bondInvestInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode());
					ps.setString(4,info.getBondCode());
					ps.setString(5,info.getBondTrustspOrg());
					ps.setString(6,info.getBondCate());
					ps.setString(7,info.getBondCreditGrade());
					ps.setString(8,info.getCur());
					ps.setString(9,info.getBondBalance());
					ps.setString(10,info.getBondBalanceRmb());
					ps.setString(11,info.getDebtRegDate());
					ps.setString(12,info.getValueDate());
					ps.setString(13,info.getRedemDate());
					ps.setString(14,info.getCouponRate());
					ps.setString(15,info.getIssuerIdCode());
					ps.setString(16,info.getIssuerRegionCode());
					ps.setString(17,info.getIssuerIndustry());
					ps.setString(18,info.getIssuerEntpScale());
					ps.setString(19,info.getIssuerEcoSector());
					ps.setString(20,info.getIssuerEcoDept());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", bondInvestInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入资管产品存续期募集信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();
			}
		});
	}

	@API(desc = "根据已有文档类型获取模板子类型数据字典",auth = APIAuth.NO,operation = APIOperation.SELECT)
	public SqlResult<SqlRow> addclcSourceZonCdDict(SqlParam<BondInvestInfo> params) throws Exception {
		Map<String, Object> paramsDirect = params.getParamsDirect();
		List<SqlRow> tempTypeByDocType = bondInvestInfoDao.addclcSourceZonCdDict(paramsDirect);
		SqlResult<SqlRow> sqlRowSqlResult = new SqlResult<>();
		sqlRowSqlResult.setResults(tempTypeByDocType.size());
		sqlRowSqlResult.setRows(tempTypeByDocType);
		sqlRowSqlResult.setDesensitized(false);;
		return sqlRowSqlResult;
	}

}
