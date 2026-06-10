package com.kayak.rpt.rhzy.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.rpt.rhzg.service.ExcelImportService;
import com.kayak.rpt.rhzy.dao.SpvInvestAmountInfoDao;
import com.kayak.rpt.rhzy.model.SpvInvestAmountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "特定目的载体投资发生额信息服务", model = SpvInvestAmountInfo.class)
public class SpvInvestAmountInfoService implements ExcelImportService<SpvInvestAmountInfo> {
	private static final Logger log = LoggerFactory.getLogger(SpvInvestAmountInfoService.class);

	@Autowired
	private SpvInvestAmountInfoDao spvInvestAmountInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询特定目的载体投资发生额信息信息", auth = APIAuth.YES)
	public SqlResult<SpvInvestAmountInfo> findSpvInvestAmountInfos(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return spvInvestAmountInfoDao.findSpvInvestAmountInfos(params);
	}
	
	@API(desc = "修改特定目的载体投资发生额信息", params = "id,org_code,report_date,inner_org_code,specific_aim_type,product_code,specific_aim_code,issuer_code,issuer_arear_code,rnn_code,sub_date,end_date,trade_date,cur,trade_amount,trade_amount_rmb,trade_dire", auth = APIAuth.YES)
	public int updateSpvInvestAmountInfo(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return spvInvestAmountInfoDao.updateSpvInvestAmountInfo(params).getEffect();
	}
	
	@API(desc = "删除特定目的载体投资发生额信息", params = "id,org_code,report_date,inner_org_code,specific_aim_type,product_code,specific_aim_code,issuer_code,issuer_arear_code,rnn_code,sub_date,end_date,trade_date,cur,trade_amount,trade_amount_rmb,trade_dire", auth = APIAuth.YES)
	public int deleteSpvInvestAmountInfo(SqlParam<SpvInvestAmountInfo> params) throws Exception {
		return spvInvestAmountInfoDao.deleteSpvInvestAmountInfo(params).getEffect();
	}

	public void deleteSpvInvestAmountInfoByDate(Object params) throws Exception{
		try {
			spvInvestAmountInfoDao.deleteSpvInvestAmountInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void importFile(List<SpvInvestAmountInfo> spvInvestAmountInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_spv_invest_amount_Info`(`report_date`, `org_code`, `inner_org_code`, `specific_aim_type`, `product_code`, `specific_aim_code`, `issuer_code`, `issuer_arear_code`, `rnn_code`, `sub_date`, `end_date`, `trade_date`, `cur`, `trade_amount`, `trade_amount_rmb`, `trade_dire`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (SpvInvestAmountInfo info : spvInvestAmountInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode());
					ps.setString(4,info.getSpecificAimType());
					ps.setString(5,info.getProductCode());
					ps.setString(6,info.getSpecificAimCode());
					ps.setString(7,info.getIssuerCode());
					ps.setString(8,info.getIssuerArearCode());
					ps.setString(9,info.getRnnCode());
					ps.setString(10,info.getSubDate());
					ps.setString(11,info.getEndDate());
					ps.setString(12,info.getTradeDate());
					ps.setString(13,info.getCur());
					ps.setString(14,info.getTradeAmount());
					ps.setString(15,info.getTradeAmountRmb());
					ps.setString(16,info.getTradeDire());
					ps.addBatch();
				}
				ps.executeBatch();
				log.info(" ##### 批量入库{}耗时: {} ms", spvInvestAmountInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入特定目的载体投资发生额信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();
			}
		});
	}
}
