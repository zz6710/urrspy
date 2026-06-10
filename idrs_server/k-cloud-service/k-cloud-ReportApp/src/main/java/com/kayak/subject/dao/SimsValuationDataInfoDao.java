package com.kayak.subject.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.subject.model.SimsValuationDataInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class SimsValuationDataInfoDao extends ComnDao {

	public SqlResult<SimsValuationDataInfo> findSimsValuationDataInfos(SqlParam<SimsValuationDataInfo> params) throws Exception {
		String sql = " select I_CODE as icode ,A_TYPE as atype,M_TYPE as mtype,comcode,bottom_code,t2.itemval as ASSET_TYPE,AMOUNT,COST,CURRENCY,date_format(INPUT_DATE, '%Y-%m-%d') as INPUT_DATE,ITEM_ID,ITEM_NAME,IMPORT_DATE,ASSET_CODE,ORG_LEVEL,t1.DICTNAME as ZZ_REPORT_TYPE,t3.itemval as TRADE_PLACE,IS_PUBLIC,NET_VALUE,DATA_INSR_DT,DEAL_DATE   from dwd_s03_ttrd_sims_valuation_data t " +
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

	public void addBatchSimsValuationDataInfos(String deal_date, List<SimsValuationDataInfo> simsValuationDataInfoList) throws Exception {
		PreparedStatement ps = null;
		// 选择主数据源
		try (AutoCloseable ac = daoService.selectDataSource(0)) {
			// 1、先删除该表数据
			daoService.update("truncate stg_wwfa_ttrd_sims_valuation_data");
			// 2、批量插入委外估值表数据
			Connection connection = daoService.getConnection();
			ps = connection.prepareStatement(" insert into stg_wwfa_ttrd_sims_valuation_data " +
					" (comcode, bottom_code, asset_type, amount, cost, currency, input_date, item_id, item_name, import_date, trade_place, is_public, net_value, deal_date)" +
					" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");

			for (SimsValuationDataInfo simsValuationDataInfo : simsValuationDataInfoList) {
				String inputDate = simsValuationDataInfo.getInputDate();
				inputDate = StringUtils.isNotEmpty(inputDate) ? inputDate.replaceAll("-", "") : inputDate;

				ps.setString(1, simsValuationDataInfo.getComcode());
				ps.setString(2, simsValuationDataInfo.getBottomCode());
				ps.setString(3, simsValuationDataInfo.getAssetType());
				ps.setString(4, simsValuationDataInfo.getAmount());
				ps.setString(5, simsValuationDataInfo.getCost());
				ps.setString(6, simsValuationDataInfo.getCurrency());
				ps.setString(7, inputDate);
				ps.setString(8, simsValuationDataInfo.getItemId());
				ps.setString(9, simsValuationDataInfo.getItemName());
				ps.setString(10, deal_date);
				ps.setString(11, simsValuationDataInfo.getTradePlace());
				ps.setString(12, simsValuationDataInfo.getIsPublic());
				ps.setString(13, simsValuationDataInfo.getNetValue());
				ps.setString(14, deal_date);
				ps.addBatch();
			}
			ps.executeBatch();
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}

	public List<SqlRow> findWwfaFilesInputDate(String deal_date) throws Exception {
		return super.findRows("select distinct input_date from stg_wwfa_ttrd_sims_valuation_data where import_date = '"+deal_date+"' order by input_date");
	}
}
