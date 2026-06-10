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
import com.kayak.rpt.rhzy.dao.InterbankDepositAmountInfoDao;
import com.kayak.rpt.rhzy.model.InterbankDepositAmountInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "同业存款发生额信息服务", model = InterbankDepositAmountInfo.class)
public class InterbankDepositAmountInfoService implements ExcelImportService<InterbankDepositAmountInfo> {
	private static final Logger log = LoggerFactory.getLogger(InterbankDepositAmountInfoService.class);

	@Autowired
	private InterbankDepositAmountInfoDao interbankDepositAmountInfoDao;
	@Autowired
	private ComnDao comnDao;

	@API(desc = "查询同业存款发生额信息信息", auth = APIAuth.YES)
	public SqlResult<InterbankDepositAmountInfo> findInterbankDepositAmountInfos(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return interbankDepositAmountInfoDao.findInterbankDepositAmountInfos(params);
	}
	
	@API(desc = "修改同业存款发生额信息", params = "id,org_code,report_date,inner_org_code,busi_type,cntr_id_type,cntr_code,deposit_acco_code,deposit_protocol_code,protocol_start_date,protocol_end_date,cur,trade_amount,trade_amount_rmb,trade_date,trade_ser_no,rate_level,trade_acco_no,trade_acco_bank_no,cntr_acco_no,trade_dire", auth = APIAuth.YES)
	public int updateInterbankDepositAmountInfo(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return interbankDepositAmountInfoDao.updateInterbankDepositAmountInfo(params).getEffect();
	}
	
	@API(desc = "删除同业存款发生额信息", params = "id,org_code,report_date,inner_org_code,busi_type,cntr_id_type,cntr_code,deposit_acco_code,deposit_protocol_code,protocol_start_date,protocol_end_date,cur,trade_amount,trade_amount_rmb,trade_date,trade_ser_no,rate_level,trade_acco_no,trade_acco_bank_no,cntr_acco_no,trade_dire", auth = APIAuth.YES)
	public int deleteInterbankDepositAmountInfo(SqlParam<InterbankDepositAmountInfo> params) throws Exception {
		return interbankDepositAmountInfoDao.deleteInterbankDepositAmountInfo(params).getEffect();
	}

	public void deleteInterbankDepositAmountInfoByDate(Object params) throws Exception{
		try {
			interbankDepositAmountInfoDao.deleteInterbankDepositAmountInfoByDate(params);
		} catch (Exception e) {
			throw e;
		}
	}

	public void importFile(List<InterbankDepositAmountInfo> interbankDepositAmountInfos, Map map) throws Exception {
		long startTime = System.currentTimeMillis();
		String batchSql = "INSERT INTO `app_interbank_deposit_amount_info`(`report_date`, `org_code`, `inner_org_code`, `busi_type`, `cntr_id_type`, `cntr_code`, `deposit_acco_code`, `deposit_protocol_code`, `protocol_start_date`, `protocol_end_date`, `cur`, `trade_amount`, `trade_amount_rmb`, `trade_date`, `trade_ser_no`, `rate_level`, `trade_acco_no`, `trade_acco_bank_no`, `cntr_acco_no`, `trade_dire`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		comnDao.doTrans(() -> {
			Connection connection = comnDao.getConnection();
			PreparedStatement ps  = connection.prepareStatement(batchSql);
			try {
				for (InterbankDepositAmountInfo info : interbankDepositAmountInfos){
					ps.setString(1,map.get("reportDate").toString());
					ps.setString(2,info.getOrgCode());
					ps.setString(3,info.getInnerOrgCode() == null ? null : info.getInnerOrgCode());
					ps.setString(4,info.getBusiType());
					ps.setString(5,info.getCntrIdType());
					ps.setString(6,info.getCntrCode());
					ps.setString(7,info.getDepositAccoCode());
					ps.setString(8,info.getDepositProtocolCode());
					ps.setString(9,info.getProtocolStartDate());
					ps.setString(10,info.getProtocolEndDate());
					ps.setString(11,info.getCur());
					ps.setString(12,info.getTradeAmount());
					ps.setString(13,info.getTradeAmountRmb());
					ps.setString(14,info.getTradeDate());
					ps.setString(15,info.getTradeSerNo());
					ps.setString(16,info.getRateLevel());
					ps.setString(17,info.getTradeAccoNo());
					ps.setString(18,info.getTradeAccoBankNo());
					ps.setString(19,info.getCntrAccoNo());
					ps.setString(20,info.getTradeDire());
					ps.addBatch();

				}
				ps.executeBatch();

				log.info(" ##### 批量入库{}耗时: {} ms", interbankDepositAmountInfos.size(),System.currentTimeMillis() - startTime);
			}catch (Exception e) {
				log.error("导入资管产品存续期募集信息异常!", e);
				throw new Exception(e.getMessage());
			} finally{
				ps.close();

			}
		});
	}

}
