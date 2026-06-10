package com.kayak.rpt.rhlc.dao;

import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import com.kayak.rpt.rhlc.model.AppAssetUnincorporatedEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

@Repository
public class AppAssetUnincorporatedEntityDao extends ComnDao {

	public SqlResult<AppAssetUnincorporatedEntity> findAppAssetUnincorporatedEntitys(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return super.findRows("SELECT id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version FROM app_asset_unincorporated_entity", params);
	}

	public List<SqlRow> findAppAssetUnincorporatedEntitys(Map<String, Object> params) throws Exception {
		String sql = "SELECT id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version FROM app_asset_unincorporated_entity where 1=1";
		if (StringUtils.isNotEmpty(String.valueOf(params.get("reportDate")))) {
			sql += " and report_date = $S{reportDate}";
		}
		if (StringUtils.isNotEmpty(String.valueOf(params.get("dtDt")))) {
			sql += " and dt_dt = $S{dtDt}";
		}
		if (StringUtils.isNotEmpty(String.valueOf(params.get("prdcNm")))) {
			sql += " and prdc_nm like '%$U{prdcNm}%'";
		}
		return super.findRows(sql, params);
	}

	public UpdateResult addAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return super.update("INSERT INTO app_asset_unincorporated_entity(id,report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,prdc_class,is_special_fund,asset,details,create_date,imp_date,theory_report_start_date,theory_report_end_date,register_status,register_date,sys_data_source,sys_data_status,sys_data_version) VALUES($AUTOIDI{id},$S{reportDate},$S{dtDt},$S{prdcNm},$S{prdcCd},$S{prdcType},$S{prdcClass},$S{isSpecialFund},$D{asset},$S{details},$S{createDate},$S{impDate},$S{theoryReportStartDate},$S{theoryReportEndDate},$S{registerStatus},$S{registerDate},$S{sysDataSource},$S{sysDataStatus},$S{sysDataVersion})",
				params.getModel());
	}

	public void impAppAssetUnincorporatedEntity(List<AppAssetUnincorporatedEntity> appAssetUnincorporatedEntities, Map<String, Object> params) throws Exception {
		//开启事务处理批处理
		doTrans( () ->{
			PreparedStatement preparedStatement = null;
			try {
				Connection connection = this.getConnection();
				preparedStatement = connection.prepareStatement("REPLACE INTO app_asset_unincorporated_entity(report_date,dt_dt,prdc_nm,prdc_cd,prdc_type,imp_date,sys_data_source,sys_data_status) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
				for (AppAssetUnincorporatedEntity appAssetUnincorporatedEntity : appAssetUnincorporatedEntities) {
					preparedStatement.setString(1, (String) params.get("reportDate"));
					preparedStatement.setString(2, appAssetUnincorporatedEntity.getDtDt());
					preparedStatement.setString(3, appAssetUnincorporatedEntity.getPrdcNm());
					preparedStatement.setString(4, appAssetUnincorporatedEntity.getPrdcCd());
					preparedStatement.setString(5, appAssetUnincorporatedEntity.getPrdcType());
					preparedStatement.setString(6, (String) params.get("reportDate"));
					preparedStatement.setString(7, "2");
					preparedStatement.setString(8, "1");
					preparedStatement.addBatch();
				}
				preparedStatement.executeBatch();
			} catch (Exception e) {
				throw e;
			} finally {
				preparedStatement.close();
			}
		});
	}

	public UpdateResult updateAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return super.update("UPDATE app_asset_unincorporated_entity SET report_date=$S{reportDate} ,dt_dt=$S{dtDt} ,prdc_nm=$S{prdcNm} ,prdc_cd=$S{prdcCd} ,prdc_type=$S{prdcType} ,prdc_class=$S{prdcClass} ,is_special_fund=$S{isSpecialFund} ,asset=$D{asset} ,details=$S{details} ,create_date=$S{createDate} ,imp_date=$S{impDate} ,theory_report_start_date=$S{theoryReportStartDate} ,theory_report_end_date=$S{theoryReportEndDate} ,register_status=$S{registerStatus} ,register_date=$S{registerDate} ,sys_data_source=$S{sysDataSource} ,sys_data_status=$S{sysDataStatus} ,sys_data_version=$S{sysDataVersion}  WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult deleteAppAssetUnincorporatedEntity(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return super.update("DELETE FROM app_asset_unincorporated_entity WHERE  id=$I{id} ",
				params.getModel());
	}

	public UpdateResult reloadData(SqlParam<AppAssetUnincorporatedEntity> params) throws Exception {
		return super.update(ExeQuery.queryExeId("M10009EQ001"),
				params.getModel());
	}

}
