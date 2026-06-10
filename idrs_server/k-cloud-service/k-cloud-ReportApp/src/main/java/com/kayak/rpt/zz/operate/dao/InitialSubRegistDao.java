package com.kayak.rpt.zz.operate.dao;

import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.UpdateResult;
import com.kayak.rpt.zz.operate.model.InitialSubRegist;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

@Repository
public class InitialSubRegistDao extends ComnDao {

	public SqlResult<InitialSubRegist> findInitialSubRegists(SqlParam<InitialSubRegist> params) throws Exception {
		String sql = "SELECT id, bank_code, prod_code, number_indiv_invest, number_corpor_invest, number_ucor_invest, other_distribut_agents, details, register_serno, imp_date, register_date, register_status, actual_subscribed_amt, subscribed_vol, amt_other_db_agents, summit_user, create_date, create_time, op_type, FND_TRST_ACT_NBR, fnd_trst_act, zon_clc_amt FROM app_initial_sub_regist_remark where 1=1 ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and DATE(create_date) >= DATE($S{startDate}) and DATE(create_date) <= DATE($S{endDate})";
		}
		sql += " order by create_date desc,create_time desc ";
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addInitialSubRegist(InitialSubRegist initialSubRegist) throws Exception {
		return super.update("INSERT INTO app_initial_sub_regist_remark(fnd_trst_act_nbr,fnd_trst_act,bank_code,prod_code,number_indiv_invest,number_corpor_invest,number_ucor_invest,other_distribut_agents,details,register_serno,imp_date,register_date,register_status,actual_subscribed_amt,subscribed_vol,amt_other_db_agents,summit_user,create_date,create_time,op_type,report_date) VALUES($S{fndTrstActNbr},$S{fndTrstAct},$S{bankCode},$S{prodCode},$D{numberIndivInvest},$D{numberCorporInvest},$D{numberUcorInvest},$S{otherDistributAgents},$S{details},$S{registerSerno},date_format(CURDATE(),'%Y%m%d'),$S{registerDate},$S{registerStatus},$D{actualSubscribedAmt},$D{subscribedVol},$D{amtOtherDbAgents},$S{summitUser},date_format(CURDATE(),'%Y%m%d'),date_format(CURTIME(),'%H%i%s'),$S{opType},$S{reportDate})",
				DataSourceProperty.PUB, initialSubRegist);
	}
	
	public UpdateResult updateInitialSubRegist(SqlParam<InitialSubRegist> params) throws Exception {
		return super.update("UPDATE app_initial_sub_regist_remark SET bank_code=$S{bankCode} ,prod_code=$S{prodCode} ,number_indiv_invest=$D{numberIndivInvest} ,number_corpor_invest=$D{numberCorporInvest} ,number_ucor_invest=$D{numberUcorInvest} ,other_distribut_agents=$S{otherDistributAgents} ,details=$S{details} ,register_serno=$S{registerSerno} ,imp_date=$S{impDate} ,register_date=$S{registerDate} ,register_status=$S{registerStatus} ,actual_subscribed_amt=$D{actualSubscribedAmt} ,subscribed_vol=$D{subscribedVol} ,amt_other_db_agents=$D{amtOtherDbAgents} ,summit_user=$S{summitUser} ,create_date=$S{createDate} ,create_time=$S{createTime} ,op_type=$S{opType},zon_clc_amt=S{zonClcAmt}  WHERE ",
				DataSourceProperty.PUB, params.getModel());
	}
	
	public UpdateResult deleteInitialSubRegist(SqlParam<InitialSubRegist> params) throws Exception {
		return super.update("DELETE FROM app_initial_sub_regist_remark WHERE ",
				DataSourceProperty.PUB, params.getModel());
	}

}
