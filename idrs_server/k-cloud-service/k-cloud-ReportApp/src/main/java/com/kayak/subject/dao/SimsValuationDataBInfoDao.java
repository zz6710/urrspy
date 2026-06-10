package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.subject.model.SimsValuationDataBInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SimsValuationDataBInfoDao extends ComnDao {

	public SqlResult<SimsValuationDataBInfo> findSimsValuationDataBInfos(SqlParam<SimsValuationDataBInfo> params) throws Exception {
		String sql = " select I_CODE as icode ,A_TYPE as atype,M_TYPE as mtype,comcode,bottom_code,t2.itemval as ASSET_TYPE,AMOUNT,COST,CURRENCY,date_format(INPUT_DATE, '%Y-%m-%d') as INPUT_DATE,ITEM_ID,ITEM_NAME,IMPORT_DATE,ASSET_CODE,ORG_LEVEL,t1.DICTNAME as ZZ_REPORT_TYPE,t3.itemval as TRADE_PLACE,IS_PUBLIC,NET_VALUE,DATA_INSR_DT,DEAL_DATE   from ODS_SIMS_VALUATION_DATA t " +
				" left join base_ex_map t1 on t.ZZ_REPORT_TYPE = t1.OUT_VALUE and t1.dict ='zz_report_type' \n" +
				" left join sys_dict_item t2 on t.ASSET_TYPE = t2.itemkey and t2.dict ='sims_asset_type' \n" +
				" left join sys_dict_item t3 on t.TRADE_PLACE = t3.itemkey and t3.dict ='trade_market' where 1=1 \n";
		if (StringUtils.isNotBlank(params.getModel().getInputDate())) {
			sql = sql + " and  t.input_date = '" + params.getModel().getInputDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIcode())) {
			sql = sql + " and  t.i_code like '%" + params.getModel().getIcode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getComcode())) {
			sql = sql + " and  t.comcode like '%" + params.getModel().getComcode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getBottomCode())) {
			sql = sql + " and  t.bottom_code like '%" + params.getModel().getBottomCode() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getAssetType())) {
			sql = sql + " and  t.asset_type = '"+ params.getModel().getAssetType() + "'";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addImportSimsValuationDataBInfo(Object map) throws Exception {
		return super.update("insert into ods_sims_valuation_data (comcode,bottom_code,asset_type,amount,cost,currency,input_date,item_id,item_name,import_date,i_code,a_type,m_type,asset_code,org_level,zz_report_type,trade_place,is_public,net_value,data_insr_dt,deal_date) " +
						"VALUES ($S{comcode},$S{bottomCode},$S{assetType},$S{amount},$S{cost},$S{currency},$S{inputDate},$S{itemId},$S{itemName},$S{importDate},$S{icode},$S{atype},$S{mtype},$S{assetCode},$S{orgLevel},$S{zzReportType},$S{tradePlace},$S{isPublic},$S{netValue},$S{dataInsrDt},date_format(CURDATE(),'%Y%m%d'))" ,
				DataSourceProperty.PUB,map);
	}

	public UpdateResult deleteSimsValuationData(Object params) throws Exception {
		return super.update("DELETE FROM ods_sims_valuation_data where INPUT_DATE like  '%$U{inputDate}%' ", params);
	}

}
