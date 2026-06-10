package com.kayak.dps.app.service;

import com.kayak.core.exception.SqlException;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.model.FundInfoModel;
import com.kayak.dps.ods.dao.FundInfoDao;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.dps.app.dao.FundNavInfoModelDao;
import com.kayak.dps.app.model.FundNavInfoModel;

import java.util.List;

@Service
@APIDefine(desc = "基金净值信息服务", model = FundNavInfoModel.class)
public class FundNavInfoModelService {

	@Autowired
	private FundNavInfoModelDao fundNavInfoModelDao;
	@Autowired
	private FundInfoDao fundInfoDao;

	public SqlResult<FundNavInfoModel> findFundNavInfoModels(SqlParam<FundNavInfoModel> params) throws Exception {
		params.setMakeSql(true);
		SqlResult<FundNavInfoModel> result = fundNavInfoModelDao.findFundNavInfoModels(params);
		List<FundNavInfoModel> rows = result.getRows();
		rows.forEach(a ->{
			try {
				a.setScrNm(fundInfoDao.findFundTypeAndNmById(a.getScrId()).get(0).get("SCR_NM").toString());
				a.setFundType(fundInfoDao.findFundTypeAndNmById(a.getScrId()).get(0).get("WD_FRS_CTG").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		result.setRows(rows);
		return result;
	}

	@API(desc = "查询基金净值信息信息", auth = APIAuth.YES)
	public SqlResult<FundNavInfoModel> findFundNavInfoModelsByScrCd(SqlParam<FundNavInfoModel> params) throws Exception {
		SqlResult<FundNavInfoModel> result = fundNavInfoModelDao.findFundNavInfoModelsByScrCd(params);
		List<FundNavInfoModel> rows = result.getRows();
		rows.forEach(a ->{
			try {
				a.setScrNm(fundInfoDao.findFundTypeAndNmById(a.getScrId()).get(0).get("SCR_NM").toString());
				a.setFundType(fundInfoDao.findFundTypeAndNmById(a.getScrId()).get(0).get("WD_FRS_CTG").toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		result.setRows(rows);
		return result;
	}

	@API(desc = "添加基金净值信息", auth = APIAuth.YES)
	public String addFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		try {
			// 如果交易市场是上交所或者深交所
			if ("1".equals(params.getModel().getTrxMkt()) || "2".equals(params.getModel().getTrxMkt())) {
				fundNavInfoModelDao.addToPublicFund(params);
			} else {
				// 如果是货币型基金
				if ("04".equals(params.getModel().getFundType())) {
					// 增加至货币市场基金收益信息
					fundNavInfoModelDao.addToMoneyMarketFund(params);
				}
				// 场外基金
				fundNavInfoModelDao.addFundNavInfoModel(params);
			}
			return RequestSupport.updateReturnJson(true, "新增成功", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "存在有相同的基金净值信息，新增失败", null).toString();
		}
	}



	@API(desc = "修改基金净值信息", params = "scr_id,scr_cd,trx_mkt,ntc_dt,stop_dt,unt_nav,acm_nav,acm_dvd,adj_fct,ccy_cd,crt_dt,upd_dt", auth = APIAuth.YES)
	public String updateFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		try {
			// 如果交易市场是上交所或者深交所
			if ("1".equals(params.getModel().getTrxMkt()) || "2".equals(params.getModel().getTrxMkt())) {
				fundNavInfoModelDao.updateToPublicFund(params);
			} else {
				if ("04".equals(params.getModel().getFundType())) {
					// 货币市场基金收益信息
					fundNavInfoModelDao.updateToMoneyMarketFund(params);
				} else {
					// 场外基金
					fundNavInfoModelDao.updateFundNavInfoModel(params);
				}
			}
			return RequestSupport.updateReturnJson(true, "添加成功", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "存在有相同的基金净值信息，添加失败", null).toString();
		}
	}



	@API(desc = "删除基金净值信息", params = "scr_id,scr_cd,trx_mkt,ntc_dt,stop_dt,unt_nav,acm_nav,acm_dvd,adj_fct,ccy_cd,crt_dt,upd_dt", auth = APIAuth.YES)
	public String deleteFundNavInfoModel(SqlParam<FundNavInfoModel> params) throws Exception {
		try {
			// 如果交易市场是上交所或者深交所
			if ("1".equals(params.getModel().getTrxMkt()) || "2".equals(params.getModel().getTrxMkt())) {
				fundNavInfoModelDao.deleteToPublicFund(params);
			} else{
				// 如果是货币型基金
				if ("04".equals(params.getModel().getFundType())) {
					// 货币市场基金收益信息
					fundNavInfoModelDao.deleteToMoneyMarketFund(params).getEffect();
				} else {
					fundNavInfoModelDao.deleteFundNavInfoModel(params).getEffect();
				}
			}
			return RequestSupport.updateReturnJson(true, "删除成功", null).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return RequestSupport.updateReturnJson(false, "删除失败", null).toString();
		}
	}


	// 查询是否能增加
	private boolean checkExist(SqlParam<FundNavInfoModel> params) throws Exception {
		int con;
		// 如果交易市场是上交所或者深交所
		if ("1".equals(params.getModel().getTrxMkt()) || "2".equals(params.getModel().getTrxMkt())) {
			con = Integer.parseInt(fundNavInfoModelDao.checkToPublicFund(params).get("con").toString());
		}else {
			if ("04".equals(params.getModel().getFundType())) {
				// 增加至货币市场基金收益信息
				con = Integer.parseInt(fundNavInfoModelDao.checkToMoneyMarketFund(params).get("con").toString());
			} else {
				// 场外基金
				con = Integer.parseInt(fundNavInfoModelDao.checkFundNavInfoModel(params).get("con").toString());
			}
		}
		// 当con为0时 返回false
		return con != 0;
	}

	// 查询是否能修改
	private boolean checkItUpdate(SqlParam<FundNavInfoModel> params) throws Exception {
		int con;
		// 如果交易市场是上交所或者深交所
		if ("1".equals(params.getModel().getTrxMkt()) || "2".equals(params.getModel().getTrxMkt())) {
			con = Integer.parseInt(fundNavInfoModelDao.checkToPublicFund(params).get("con").toString());
		} else {
			if ("04".equals(params.getModel().getFundType())) {
				// 增加至货币市场基金收益信息
				con = Integer.parseInt(fundNavInfoModelDao.checkToMoneyMarketFund(params).get("con").toString());
			} else {
				// 场外基金
				con = Integer.parseInt(fundNavInfoModelDao.checkFundNavInfoModel(params).get("con").toString());
			}
		}
		// 当con为1时 返回false
		return con != 1;
	}

}
