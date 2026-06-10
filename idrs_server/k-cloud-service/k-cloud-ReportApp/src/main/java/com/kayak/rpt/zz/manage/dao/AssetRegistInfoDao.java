package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.rpt.zz.manage.model.AssetRegistInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Repository
public class AssetRegistInfoDao extends ComnDao {

	public SqlResult<AssetRegistInfo> findAssetRegistInfos(SqlParam<AssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String sql = "SELECT T1.BANK_CODE,T1.PROD_REG_ENC,T1.HOLDING_TYPE,T1.ASSET_CODE,T1.INVESTED_ASSET,T1.mezzanine_number,T1.MEZZANINE_ASSET_CODE,T1.ACCOUNT_CODE,T1.INVESTED_AMOUNT,T1.INVESTED_AMOUNT_CNY,T1.FAIR_VALUE,T1.FAIR_VALUE_CNY,T1.NET_VALUATION,T1.FL_VALUATION,T1.QUANTITY,T1.CNY,T1.HOLDING_DATE,T1.DETAILS,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.create_date,T1.theory_report_start_date,T1.theory_report_end_date,T1.report_date,T1.bank_name,T1.prod_name,ifnull(ARS.audit_status,0) audit_status FROM";
//		String holdDate = startDate;
		sql += " app_asset_regist_info";
		sql +=  " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_asset_regist_info' where t1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.holding_date = '" + startDate + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAssetCode())) {
			sql += " and T1.asset_code like '%" + params.getModel().getAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getMezzanineAssetCode())) {
			sql += " and T1.mezzanine_asset_code like '%" + params.getModel().getMezzanineAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getHoldingType())) {
			sql += " and T1.holding_type = '" + params.getModel().getHoldingType() + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public int findAssetRegistInfosCount(SqlParam<AssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String sql = "SELECT count(1) FROM";
//		String holdDate = startDate;
		sql += " app_asset_regist_info";
		sql +=  " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_asset_regist_info' where t1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.holding_date = '" + startDate + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAssetCode())) {
			sql += " and T1.asset_code like '%" + params.getModel().getAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getMezzanineAssetCode())) {
			sql += " and T1.mezzanine_asset_code like '%" + params.getModel().getMezzanineAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getHoldingType())) {
			sql += " and T1.holding_type = '" + params.getModel().getHoldingType() + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public int findAssetRegistInfosFailStatus(SqlParam<AssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String sql = "SELECT count(1) FROM app_asset_regist_info T1 ";
		sql += " LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_asset_regist_info' where t1.sys_data_status ='1' and t1.register_status in (0,1) ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.holding_date = '" + startDate + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  T1.register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and T1.prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAssetCode())) {
			sql += " and T1.asset_code like '%" + params.getModel().getAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getMezzanineAssetCode())) {
			sql += " and T1.mezzanine_asset_code like '%" + params.getModel().getMezzanineAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getHoldingType())) {
			sql += " and T1.holding_type = '" + params.getModel().getHoldingType() + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}

	public SqlResult<AssetRegistInfo> findAssetRegistInfos_day(SqlParam<AssetRegistInfo> params) throws Exception {
		String startDate = params.getModel().getStartDate();
		String lastCycleDay = DateUtil.getLastCycleDay(startDate,0, 1);
		String sql = "SELECT T1.BANK_CODE,T1.PROD_REG_ENC,T1.HOLDING_TYPE,T1.ASSET_CODE,T1.INVESTED_ASSET,T1.mezzanine_number,T1.MEZZANINE_ASSET_CODE,T1.ACCOUNT_CODE,T1.INVESTED_AMOUNT,T1.INVESTED_AMOUNT_CNY,T1.FAIR_VALUE,T1.FAIR_VALUE_CNY,T1.NET_VALUATION,T1.FL_VALUATION,T1.QUANTITY,T1.CNY,T1.HOLDING_DATE,T1.DETAILS,T1.register_serno,T1.imp_date,T1.register_date,T1.register_status,T1.create_date,T1.theory_report_start_date,T1.theory_report_end_date,T1.report_date,T1.bank_name,T1.prod_name,ifnull(ARS.audit_status,0) audit_status FROM";
		String holdDate = startDate;
		sql += " app_asset_regist_info_day";
		sql +=  " T1 LEFT JOIN base_report_data_audit_results ARS ON T1.theory_report_start_date = ARS.report_date AND ARS.table_id = 'app_asset_regist_info' where t1.sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and T1.holding_date like '" + holdDate + "%' ";
		}
//		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
//			sql += " and T1.prod_reg_enc = '" + params.getModel().getProdRegEnc() + "'";
//		}
//		if (Strings.isNotBlank(params.getModel().getAssetCode())) {
//			sql += " and T1.asset_code = '" + params.getModel().getAssetCode() + "' ";
//		}
//		if (Strings.isNotBlank(params.getModel().getMezzanineAssetCode())) {
//			sql += " and T1.mezzanine_asset_code = '" + params.getModel().getMezzanineAssetCode() + "' ";
//		}
//		if (Strings.isNotBlank(params.getModel().getHoldingType())) {
//			sql += " and T1.holding_type = '" + params.getModel().getHoldingType() + "' ";
//		}
//		if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
//			sql = sql + " and  T1.register_serno = '" + params.getModel().getRegisterSerno() + "'";
//		}
		return super.findRows(sql,DataSourceProperty.PUB, params);
	}

	public UpdateResult addAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		return super.update("INSERT INTO app_asset_regist_info(account_code,asset_code,bank_code,cny,create_date,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,prod_reg_enc,quantity,register_date,register_serno,register_status,theory_report_start_date) VALUES($S{accountCode},$S{assetCode},$S{bankCode},$S{cny},date_format(CURDATE(),'%Y%m%d'),$S{details},$D{fairValue},$D{fairValueCny},$D{flValuation},$S{holdingDate},$S{holdingType},$S{impDate},$D{investedAmount},$D{investedAmountCny},$S{investedAsset},$S{mezzanineAssetCode},$S{mezzanineNumber},$D{netValuation},$S{prodRegEnc},$D{quantity},$S{registerDate},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),'0',$S{theoryReportStartDate})",
				DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult updateAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		String holdingDate = params.getModel().getHoldingDate();
		String lastCycleDay = DateUtil.getLastCycleDay(holdingDate,0, 1);

		String updateSql = "UPDATE ";
		if (StringUtils.isNotEmpty(holdingDate) && holdingDate.equals(lastCycleDay)) {
			updateSql += " app_asset_regist_info";
		} else {
			updateSql += " app_asset_regist_info_day";
		}
		updateSql += " SET account_code=$S{accountCode} ,asset_code=$S{assetCode},bank_code=$S{bankCode},cny=$S{cny},details=$S{details},fair_value=$D{fairValue},fair_value_cny=$D{fairValueCny},fl_valuation=$D{flValuation},holding_date=$S{holdingDate},holding_type=$S{holdingType},invested_amount=$D{investedAmount},invested_amount_cny=$D{investedAmountCny},invested_asset=$S{investedAsset},mezzanine_asset_code=$S{mezzanineAssetCode},mezzanine_number=$S{mezzanineNumber},net_valuation=$D{netValuation},prod_reg_enc=$S{prodRegEnc},quantity=$D{quantity}  WHERE register_serno=$S{registerSerno}";
		return super.update(updateSql, DataSourceProperty.PUB,params.getModel());
	}
	
	public UpdateResult deleteAssetRegistInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		String holdingDate = params.getModel().getHoldingDate();
		String lastCycleDay = DateUtil.getLastCycleDay(holdingDate,0, 1);

		String deleteSql = "DELETE FROM  ";
		if (StringUtils.isNotEmpty(holdingDate) && holdingDate.equals(lastCycleDay)) {
			deleteSql += " app_asset_regist_info";
		} else {
			deleteSql += " app_asset_regist_info_day";
		}
		deleteSql += " WHERE register_serno=$S{registerSerno}  ";
		return super.update(deleteSql, DataSourceProperty.PUB,params.getModel());
	}
    //手动更新选中条件的数据为报送成功
	public UpdateResult updateAssetRegistInfoStatus(SqlParam<AssetRegistInfo> params) throws Exception {
		String holdDate=params.getModel().getStartDate();
		String sql = "update app_asset_regist_info SET register_status='3' WHERE sys_data_status ='1' ";
		if (Strings.isNotBlank(params.getModel().getStartDate())) {
			sql += " and holding_date = '" + holdDate + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterSerno())) {
			sql = sql + " and  register_serno = '" + params.getModel().getRegisterSerno() + "'";
		}
		if (Strings.isNotBlank(params.getModel().getProdRegEnc())) {
			sql += " and prod_reg_enc like '%" + params.getModel().getProdRegEnc() + "%'";
		}
		if (Strings.isNotBlank(params.getModel().getAssetCode())) {
			sql += " and asset_code like '%" + params.getModel().getAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getMezzanineAssetCode())) {
			sql += " and mezzanine_asset_code like '%" + params.getModel().getMezzanineAssetCode() + "%' ";
		}
		if (Strings.isNotBlank(params.getModel().getHoldingType())) {
			sql += " and holding_type = '" + params.getModel().getHoldingType() + "' ";
		}
		if (Strings.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and  register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		return super.update(sql, DataSourceProperty.PUB,params.getModel());
	}

	public void updateBaseReportResultInfo(SqlParam<AssetRegistInfo> params) throws Exception {
		String holdDate=params.getModel().getStartDate();
		String sql="update base_report_result set register_date = theory_report_start_date,report_success_number=total,status= '1',register_status= '1',update_date=date_format(now(),'%Y%m%d'),update_time=date_format(now(),'%H%i%s') where report_table = 'app_asset_regist_info' and theory_report_start_date in (select theory_report_start_date from app_asset_regist_info where report_date='"+holdDate+"' and sys_data_status='1') ";
		super.update(sql, DataSourceProperty.PUB, params.getModel());
	}

	public UpdateResult addImportAssetRegistInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_asset_regist_info(account_code,asset_code,bank_code,cny,create_date,details,fair_value,fair_value_cny,fl_valuation,holding_date,holding_type,imp_date,invested_amount,invested_amount_cny,invested_asset,mezzanine_asset_code,mezzanine_number,net_valuation,prod_reg_enc,quantity,register_date,register_serno,register_status,theory_report_start_date,report_date,sys_data_status) VALUES($S{accountCode},$S{assetCode},$S{bankCode},$S{cny},date_format(CURDATE(),'%Y%m%d'),$S{details},$D{fairValue},$D{fairValueCny},$D{flValuation},$S{holdingDate},$S{holdingType},date_format(CURDATE(),'%Y%m%d'),$D{investedAmount},$D{investedAmountCny},$S{investedAsset},$S{mezzanineAssetCode},$S{mezzanineNumber},$D{netValuation},$S{prodRegEnc},$D{quantity},$S{registerDate},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),'0',$S{theoryReportStartDate},$S{reportDate},'1')",
				DataSourceProperty.PUB,param);
	}

	public UpdateResult deleteImportAssetRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_asset_regist_info where theory_report_start_date = $S{theoryReportStartDate} ", params);
	}
	//根据唯一条件查询是否已存在记录，存在则更新
	//【持仓类别】对于“持仓类别”选择选择“02 登记系统资产”或“03 登记系统负债”的记录，“产品登记编码”、“行内资产/负债编码”、“中间层行内资产/负债编码”、“持仓日期”和“币种”这5个要素不允许同时重复。
	//【持仓类别】对于“持仓类别”选择“01 现金及活期存款”“04 其他资产”或“05 其他负债”的记录，“产品登记编码”、“会计科目名称”“中间层行内资产/负债编码”、“持仓日期”和“币种”这5个要素不允许同时重复。
	public String countAssetRegistInfo(AssetRegistInfo params) throws Exception {
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT id FROM app_asset_regist_info T1 where T1.sys_data_status ='1' ");
		if(params.getHoldingType() != null && Strings.isNotBlank(params.getHoldingType())){
			if("01".equals(params.getHoldingType()) || "04".equals(params.getHoldingType()) || "05".equals(params.getHoldingType())){
				if (Strings.isNotBlank(params.getProdRegEnc()) &&
						Strings.isNotBlank(params.getAccountCode()) &&
						//Strings.isNotBlank(params.getMezzanineAssetCode()) &&
						Strings.isNotBlank(params.getHoldingDate()) &&
						Strings.isNotBlank(params.getCny())
				) {
					sql.append(" and T1.prod_reg_enc = '" + params.getProdRegEnc() + "' ");
					sql.append(" and T1.account_code = '" + params.getAccountCode() + "' ");
					if(Strings.isNotBlank(params.getMezzanineAssetCode())){
						sql.append(" and T1.mezzanine_asset_code = '" + params.getMezzanineAssetCode() + "' ");
					}else{
						sql.append(" and (T1.mezzanine_asset_code is null or T1.mezzanine_asset_code = '') ");
					}
					sql.append(" and T1.holding_date = '" + params.getHoldingDate() + "' ");
					sql.append(" and T1.cny = '" + params.getCny() + "' limit 1");
				}

			}else if("02".equals(params.getHoldingType()) || "03".equals(params.getHoldingType())){
				if (Strings.isNotBlank(params.getProdRegEnc()) &&
						Strings.isNotBlank(params.getAssetCode()) &&
						Strings.isNotBlank(params.getMezzanineAssetCode()) &&
						Strings.isNotBlank(params.getHoldingDate()) &&
						Strings.isNotBlank(params.getCny())
				) {
					sql.append(" and T1.prod_reg_enc = '" + params.getProdRegEnc() + "' ");
					sql.append(" and T1.asset_code = '" + params.getAssetCode() + "' ");
					sql.append(" and T1.mezzanine_asset_code = '" + params.getMezzanineAssetCode() + "' ");
					sql.append(" and T1.holding_date = '" + params.getHoldingDate() + "' ");
					sql.append(" and T1.cny = '" + params.getCny() + "'  limit 1");
				}
			}else{
				return "";
			}
		}else{
			//持仓类别为空，直接返回0.  后续会去走新增。
			return "";
		}
//		if (Strings.isNotBlank(params.getRegisterStatus())) {
//			sql = sql + " and  T1.register_status = '" + params.getRegisterStatus() + "'";
//		}
		return String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params));
	}

	/**
	 * 有重复性数据时，导入覆盖更新
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateRepeatAssetRegistInfo(AssetRegistInfo params) throws Exception {
		String holdingDate = params.getHoldingDate();
//		String lastCycleDay = DateUtil.getLastCycleDay(holdingDate,0, 1);
		String updateSql = "UPDATE  app_asset_regist_info ";
		//可不更新的重复性字段
//		updateSql +="SET holding_type=$S{holdingType},prod_reg_enc=$S{prodRegEnc},mezzanine_asset_code=$S{mezzanineAssetCode},holding_date=$S{holdingDate},cny=$S{cny}";
		updateSql += " SET account_code=$S{accountCode} ,asset_code=$S{assetCode},bank_code=$S{bankCode},details=$S{details},fair_value=$D{fairValue},fair_value_cny=$D{fairValueCny},fl_valuation=$D{flValuation},invested_amount=$D{investedAmount},invested_amount_cny=$D{investedAmountCny},invested_asset=$S{investedAsset},mezzanine_number=$S{mezzanineNumber},net_valuation=$D{netValuation},quantity=$D{quantity}  WHERE id=$S{id}";
		return super.update(updateSql, DataSourceProperty.PUB,params);
	}
}
