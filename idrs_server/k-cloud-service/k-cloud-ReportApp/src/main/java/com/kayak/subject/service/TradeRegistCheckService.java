package com.kayak.subject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.subject.dao.TradeRegistCheckDao;
import com.kayak.subject.model.TradeRegistCheck;

@Service
@APIDefine(desc = "交易登记校验表服务", model = TradeRegistCheck.class)
public class TradeRegistCheckService {

	@Autowired
	private TradeRegistCheckDao tradeRegistCheckDao;

	@API(desc = "查询交易登记校验表信息", auth = APIAuth.YES)
	public SqlResult<TradeRegistCheck> findTradeRegistChecks(SqlParam<TradeRegistCheck> params) throws Exception {
		params.setMakeSql(true);
		return tradeRegistCheckDao.findTradeRegistChecks(params);
	}

	@API(desc = "添加交易登记校验表", params = "id,prod_code,prod_reg_enc,invest_type,bottom_code,trade_invamount,fa_amount,pl_amount,pl_rate,at_amount,at_pl_amount,at_pl_rate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int addTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return tradeRegistCheckDao.addTradeRegistCheck(params).getEffect();
	}
	
	@API(desc = "修改交易登记校验表", params = "id,prod_code,prod_reg_enc,invest_type,bottom_code,trade_invamount,fa_amount,pl_amount,pl_rate,at_amount,at_pl_amount,at_pl_rate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int updateTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return tradeRegistCheckDao.updateTradeRegistCheck(params).getEffect();
	}
	
	@API(desc = "删除交易登记校验表", params = "id,prod_code,prod_reg_enc,invest_type,bottom_code,trade_invamount,fa_amount,pl_amount,pl_rate,at_amount,at_pl_amount,at_pl_rate,create_date,theory_report_start_date,theory_report_end_date,imp_date,register_status,sys_data_status,sys_data_version,sys_data_source,report_date", auth = APIAuth.NO)
	public int deleteTradeRegistCheck(SqlParam<TradeRegistCheck> params) throws Exception {
		return tradeRegistCheckDao.deleteTradeRegistCheck(params).getEffect();
	}

}
