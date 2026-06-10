package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.subject.model.DwsCounterPartyInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DwsCounterPartyInfoDao extends ComnDao {

	public SqlResult<DwsCounterPartyInfo> findDwsCounterPartyInfos(SqlParam<DwsCounterPartyInfo> params) throws Exception {
	    String sql = "SELECT id,prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,act_dt,crt_date,crt_time,upd_date,upd_time,status,exception" +
    				" FROM dws_counter_party_info" +
    				" where 1=1";
		if (Tools.isNotEmpty(params.getModel().getProdCd())) {
            sql += " and prod_cd like '%" + params.getModel().getProdCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getProdIntrCd())) {
            sql += " and prod_intr_cd like '%" + params.getModel().getProdIntrCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getBredCd())) {
            sql += " and bred_cd like '%" + params.getModel().getBredCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAssetCd())) {
            sql += " and asset_cd like '%" + params.getModel().getAssetCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCntrProdType())) {
            sql += " and cntr_prod_type like '%" + params.getModel().getCntrProdType() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCntrOrgCd())) {
            sql += " and cntr_org_cd like '%" + params.getModel().getCntrOrgCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCntrOrgNm())) {
            sql += " and cntr_org_nm like '%" + params.getModel().getCntrOrgNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCntrProdCd())) {
            sql += " and cntr_prod_cd like '%" + params.getModel().getCntrProdCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCntrProdNm())) {
            sql += " and cntr_prod_nm like '%" + params.getModel().getCntrProdNm() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCcyCd())) {
            sql += " and ccy_cd like '%" + params.getModel().getCcyCd() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAmtBal())) {
            sql += " and amt_bal like '%" + params.getModel().getAmtBal() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getAmtBalCny())) {
            sql += " and amt_bal_cny like '%" + params.getModel().getAmtBalCny() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getActDt())) {
            sql += " and act_dt like '" + params.getModel().getActDt() + "%'";
        }
		if (Tools.isNotEmpty(params.getModel().getCrtDate())) {
			sql += " and crt_date = '" + params.getModel().getCrtDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getCrtTime())) {
			sql += " and crt_time = '" + params.getModel().getCrtTime() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdDate())) {
			sql += " and upd_date = '" + params.getModel().getUpdDate() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getUpdTime())) {
			sql += " and upd_time = '" + params.getModel().getUpdTime() + "'";
		}
		if (Tools.isNotEmpty(params.getModel().getStatus())) {
			sql += " and status = '" + params.getModel().getStatus() + "'";
		}
		sql += " order by act_dt desc, crt_date desc";
        return super.findRows(sql, params);
	}

	public List<DwsCounterPartyInfo> findDwsCounterPartyInfoHistory(String assetCd, String lastMonthLastDay) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("assetCd", assetCd);
		params.put("lastMonthLastDay", lastMonthLastDay);
		String sql = "SELECT distinct asset_cd,cntr_prod_cd,act_dt" +
				" FROM dws_counter_party_info" +
				" where act_dt = $S{lastMonthLastDay}";
		if (Tools.isNotEmpty(assetCd)) {
			sql += " and asset_cd = $S{assetCd}";
		}
		return super.findRows(DwsCounterPartyInfo.class, sql, DataSourceProperty.PUB, params);
	}

	public UpdateResult addDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return super.update("INSERT INTO dws_counter_party_info(prod_cd,prod_intr_cd,bred_cd,asset_cd,cntr_prod_type,cntr_org_cd,cntr_org_nm,cntr_prod_cd,cntr_prod_nm,ccy_cd,amt_bal,amt_bal_cny,status,exception,act_dt,crt_date,crt_time,upd_date,upd_time) VALUES($S{prodCd},$S{prodIntrCd},$S{bredCd},$S{assetCd},$S{cntrProdType},$S{cntrOrgCd},$S{cntrOrgNm},$S{cntrProdCd},$S{cntrProdNm},$S{ccyCd},$D{amtBal},$D{amtBalCny},$S{status},$S{exception},$S{actDt},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}
	
	public UpdateResult updateDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return super.update("UPDATE dws_counter_party_info SET prod_cd=$S{prodCd} ,prod_intr_cd=$S{prodIntrCd} ,bred_cd=$S{bredCd} ,asset_cd=$S{assetCd} ,cntr_prod_type=$S{cntrProdType} ,cntr_org_cd=$S{cntrOrgCd} ,cntr_org_nm=$S{cntrOrgNm} ,cntr_prod_cd=$S{cntrProdCd} ,cntr_prod_nm=$S{cntrProdNm} ,ccy_cd=$S{ccyCd} ,amt_bal=$D{amtBal} ,amt_bal_cny=$D{amtBalCny} ,status=$S{status} ,exception=$S{exception} ,act_dt=$S{actDt} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime} WHERE id=$I{id}",
				params.getModel());
	}
	
	public UpdateResult deleteDwsCounterPartyInfo(SqlParam<DwsCounterPartyInfo> params) throws Exception {
		return super.update("DELETE FROM dws_counter_party_info WHERE id=$I{id}",
				params.getModel());
	}

	public UpdateResult deleteDwsCounterPartyInfo(DwsCounterPartyInfo params) throws Exception {
		return super.update("DELETE FROM dws_counter_party_info WHERE act_dt = $S{actDt}",
				params);
	}

}
