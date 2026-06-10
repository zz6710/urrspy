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
import com.kayak.rpt.rhzy.dao.SpvInvestInfoDao;
import com.kayak.rpt.rhzy.model.SpvInvestInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "存量特定目的载体投资信息服务", model = SpvInvestInfo.class)
public class SpvInvestInfoService implements ExcelImportService<SpvInvestInfo> {
	private static final Logger log = LoggerFactory.getLogger(BondInvestInfoService.class);

	@Autowired
	private SpvInvestInfoDao spvInvestInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询存量特定目的载体投资信息信息", auth = APIAuth.YES)
	public SqlResult<SpvInvestInfo> findSpvInvestInfos(SqlParam<SpvInvestInfo> params) throws Exception {
		return spvInvestInfoDao.findSpvInvestInfos(params);
	}
	
	@API(desc = "修改存量特定目的载体投资信息", params = "id,org_code,report_date,inner_org_code,spv_type,amps_code,spv_code,issuer_code,issuer_region_code,run_mode,subscrip_date,expire_date,cur,invest_balance,invest_balance_rmb", auth = APIAuth.YES)
	public int updateSpvInvestInfo(SqlParam<SpvInvestInfo> params) throws Exception {
		return spvInvestInfoDao.updateSpvInvestInfo(params).getEffect();
	}
	
	@API(desc = "删除存量特定目的载体投资信息", params = "id,org_code,report_date,inner_org_code,spv_type,amps_code,spv_code,issuer_code,issuer_region_code,run_mode,subscrip_date,expire_date,cur,invest_balance,invest_balance_rmb", auth = APIAuth.YES)
	public int deleteSpvInvestInfo(SqlParam<SpvInvestInfo> params) throws Exception {
		return spvInvestInfoDao.deleteSpvInvestInfo(params).getEffect();
	}

	public void deleteSpvInvestInfoByDate(Object params) throws Exception{
		try {
			spvInvestInfoDao.deleteSpvInvestInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void importFile(List<SpvInvestInfo> spvInvestInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_spv_invest_info`(`report_date`, `org_code`, `inner_org_code`, `spv_type`, `amps_code`, `spv_code`, `issuer_code`, `issuer_region_code`, `run_mode`, `subscrip_date`, `expire_date`, `cur`, `invest_balance`, `invest_balance_rmb`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (SpvInvestInfo info : spvInvestInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode());
					ps.setString(4,info.getSpvType());
					ps.setString(5,info.getAmpsCode());
					ps.setString(6,info.getSpvCode());
					ps.setString(7,info.getIssuerCode());
					ps.setString(8,info.getIssuerRegionCode());
					ps.setString(9,info.getRunMode());
					ps.setString(10,info.getSubscripDate());
					ps.setString(11,info.getExpireDate());
					ps.setString(12,info.getCur());
					ps.setString(13,info.getInvestBalance());
					ps.setString(14,info.getInvestBalanceRmb());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", spvInvestInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入资管产品存续期募集信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();
			}
		});
	}


}
