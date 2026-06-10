package com.kayak.report.dao;

import cn.hutool.json.JSONObject;
import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.ExeQuery;
import com.kayak.report.model.BaseReportFileManage;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BaseReportFileManageDao extends ComnDao {

	public SqlResult<BaseReportFileManage> findBaseReportFileManages(SqlParam<BaseReportFileManage> params) throws Exception {
		String sql = "SELECT a.id,a.file_name,a.file_type,a.prod_cd,a.prod_nm_fu,a.zipfilename,a.remote_file,a.operaterno,a.operatername,a.crt_date,a.crt_time,a.upd_date,a.upd_time " +
				"       FROM base_report_file_manage a " +
//				"       LEFT JOIN dwd_prd_prd_bas_inf b on a.prod_cd = b.prod_cd " +
				"      WHERE 1=1 ";//(a.prod_cd = '' or a.prod_cd is null or b.prod_status <> '03')
		if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
			sql = sql + " and a.prod_cd like '%" + params.getModel().getProdCd() + "%'";
		}
		if ("1".equals(params.getModel().getProdNmFu())) {
			sql = sql + " and (a.prod_nm_fu = '' or a.prod_nm_fu is null)";
		}
		if (StringUtils.isNotBlank(params.getModel().getCrtDate())) {
			sql = sql + " and a.crt_date = '" + params.getModel().getCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getFileName())) {
			sql = sql + " and a.file_name like '%" + params.getModel().getFileName() + "%'";
		}
		if (StringUtils.isNotBlank(params.getModel().getZipfilename())) {
			sql = sql + " and a.zipfilename = '" + params.getModel().getZipfilename() + "'";
		}

		sql = sql + " order by a.crt_date desc, a.prod_cd, a.crt_time desc ";
		return super.findRows(sql, params);
	}

	public List<SqlRow>  findFileManagesByBody(BaseReportFileManage param) throws Exception {
		String sql = "SELECT a.id,a.file_name,a.file_type,a.prod_cd,a.prod_nm_fu,a.zipfilename,a.remote_file,a.operaterno,a.operatername,a.crt_date,a.crt_time,a.upd_date,a.upd_time " +
				"       FROM base_report_file_manage a  WHERE 1=1 ";
		if (StringUtils.isNotBlank(param.getProdCd())) {
			sql = sql + " and a.prod_cd like '%" + param.getProdCd() + "%'";
		}
		if ("1".equals(param.getProdNmFu())) {
			sql = sql + " and (a.prod_nm_fu = '' or a.prod_nm_fu is null)";
		}
		if (StringUtils.isNotBlank(param.getCrtDate())) {
			sql = sql + " and a.crt_date = '" + param.getCrtDate() + "'";
		}
		if (StringUtils.isNotBlank(param.getFileName())) {
			sql = sql + " and a.file_name like '%" + param.getFileName() + "%'";
		}
		if (StringUtils.isNotBlank(param.getZipfilename())) {
			sql = sql + " and a.zipfilename = '" + param.getZipfilename() + "'";
		}

		sql = sql + " order by a.crt_date desc, a.prod_cd, a.crt_time desc ";
		return super.findRows(sql, DataSourceProperty.PUB,param);
	}
	public SqlResult<BaseReportFileManage> findDwdPrdPrdBasInfs(SqlParam<BaseReportFileManage> params) throws Exception {
		String sql = "SELECT prod_cd,prod_nm_fu FROM dwd_prd_prd_bas_inf WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
			sql = sql + " and (prod_cd like '%" + params.getModel().getProdCd() + "%'";
			sql = sql + "  or prod_nm_fu like '%" + params.getModel().getProdCd() + "%')";
		}
		return super.findRows(sql, params);
	}

	public List<BaseReportFileManage> findBaseReportFileManages(String id, BaseReportFileManage baseReportFileManage) throws Exception {
		String sql = "SELECT id,file_name,prod_cd,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time FROM base_report_file_manage WHERE 1=1 ";
		if (StringUtils.isNotBlank(id)) {
			sql = sql + " and id in (" + id + ")";
		}
		return super.findRows(BaseReportFileManage.class, sql, 0, baseReportFileManage);
	}

	public List<BaseReportFileManage> findDwdPrdPrdBasInfs(BaseReportFileManage baseReportFileManage) throws Exception {
		String sql = "SELECT prod_cd,prod_nm_fu FROM dwd_prd_prd_bas_inf WHERE prod_status <> '03' ";
		if (StringUtils.isNotBlank(baseReportFileManage.getProdNmFu())) {
			sql = sql + " and prod_nm_fu = '" + baseReportFileManage.getProdNmFu() + "'";
		} else {
			return null;
		}
		return super.findRows(BaseReportFileManage.class, sql, 0, new BaseReportFileManage());
	}

	public SqlResult<BaseReportFileManage> findOdsPrdPrdBasInfs(SqlParam<BaseReportFileManage> params) throws Exception {
		String sql = "SELECT t.PROD_CODE as prod_cd , t.PROD_NAME as prod_nm_fu FROM ods_prod_base_info t WHERE 1=1 ";
		if (StringUtils.isNotBlank(params.getModel().getProdCd())) {
			sql = sql + " and (t.PROD_CODE like '%" + params.getModel().getProdCd() + "%'";
			sql = sql + "  or t.PROD_NAME like '%" + params.getModel().getProdCd() + "%')";
		}
		return super.findRows(sql, params);
	}

	/**
	 * 增加查产品运作模式字段是否需要上传流动性风险文件
	 * @param baseReportFileManage
	 * @return
	 * @throws Exception
	 */
	public List<BaseReportFileManage> findOdsPrdPrdBasInfsBy(BaseReportFileManage baseReportFileManage) throws Exception {
		String sql = "SELECT t.PROD_CODE as prod_cd , t.PROD_NAME as prod_nm_fu, t1.operation_mode " +
				" from ods_prod_base_info t " +
				" left join app_prod_regist_filing_info t1 on t.prod_code = t1.IDENT_CODE " +
				" where 1=1 and (t.mother_fund_flag in ('0','1') or t.mother_fund_flag is null or t.mother_fund_flag = '' ) ";
		if (StringUtils.isNotBlank(baseReportFileManage.getProdCd())) {
			sql = sql + " and  t.prod_code= '" + baseReportFileManage.getProdCd() + "'";
		} else {
			return null;
		}
		return super.findRows(BaseReportFileManage.class, sql, 0, new BaseReportFileManage());
	}

	public List<BaseReportFileManage> findDwdPrdPrdBasInfsBy(BaseReportFileManage baseReportFileManage) throws Exception {
		String sql = "SELECT prod_cd,prod_nm_fu FROM dwd_prd_prd_bas_inf WHERE prod_status <> '03' ";
		if (StringUtils.isNotBlank(baseReportFileManage.getProdCd())) {
			sql = sql + " and prod_cd = '" + baseReportFileManage.getProdCd() + "'";
		} else {
			return null;
		}
		return super.findRows(BaseReportFileManage.class, sql, 0, new BaseReportFileManage());
	}

	public UpdateResult addBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		return super.update("INSERT INTO base_report_file_manage(id,file_name,file_type,prod_cd,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$S{fileName},$S{fileType},$S{prodCd},$S{prodNmFu},$S{zipfilename},$S{remoteFile},$S{operaterno},$S{operatername},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				params.getModel());
	}

	public String addBaseReportFileManage(BaseReportFileManage model) throws Exception {
		return super.update("INSERT INTO base_report_file_manage(id,file_name,file_type,prod_cd,prod_nm_fu,zipfilename,remote_file,operaterno,operatername,crt_date,crt_time,upd_date,upd_time) VALUES($AUTOIDI{id},$S{fileName},$S{fileType},$S{prodCd},$S{prodNmFu},$S{zipfilename},$S{remoteFile},$S{operaterno},$S{operatername},$S{crtDate},$S{crtTime},$S{updDate},$S{updTime})",
				model).getAutoId();
	}
	
	public UpdateResult updateBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		params.getModel().setUpdDate(DateUtil.getNowDate());
		params.getModel().setUpdTime(DateUtil.getNowTime());
		return super.update("UPDATE base_report_file_manage SET file_name=$S{fileName} ,prod_cd=$S{prodCd} ,prod_nm_fu=$S{prodNmFu} ,zipfilename=$S{zipfilename} ,remote_file=$S{remoteFile} ,operaterno=$S{operaterno} ,operatername=$S{operatername} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,upd_date=$S{updDate} ,upd_time=$S{updTime}  WHERE  id=$I{id} ",
				params.getModel());
	}
	
	public UpdateResult deleteBaseReportFileManage(SqlParam<BaseReportFileManage> params) throws Exception {
		return super.update("DELETE FROM base_report_file_manage WHERE  id=$I{id} ",
				params.getModel());
	}

	/**
	 * 根据报送日期查询数据日期
	 * @param dParams
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkDataDate (JSONObject dParams) {
		try {
			String checkSql = ExeQuery.queryExeId("CHECKEU001");
			String report_date = (String)dParams.get("dealDate");
			SqlRow row = super.findRow(checkSql, dParams);
			if(row != null) report_date = row.getString("report_date");
			dParams.put("reportDate", report_date);

			return dParams;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return dParams;
		}
	}

	public List<SqlRow> findSystablesByMap(Map params) {
		try {
			String sql = "select export_table_id from app_table_info where id =$S{systemTableName}";
			return super.findRows(sql, DataSourceProperty.PUB, params);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}
	}

	public String findSheetByTable(String systemTableName) {
		try {
			String sheetName = "";
			Map params = new HashMap();
			params.put("systemTableName",systemTableName);
			String sql = "select b.template_name from import_template_manage b where " +
					"b.system_table_name =$S{systemTableName} " +
					"and b.id in ( select max(a.id) from import_template_manage a group by a.system_table_name)";
			List<SqlRow> sqlRows = super.findRows(sql, DataSourceProperty.PUB, params);
			if(!sqlRows.isEmpty()){
				sheetName = sqlRows.get(0).getString("template_name");
			}
			return sheetName;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return null;
		}
	}

	/**
	 * 执行交易信息登记更新数据
	 * @param sqlIdList
	 */
	public void executeUpdSql(List<String> sqlIdList, Map<String, Object> params) throws Exception {
		for (String exeId : sqlIdList) {
			super.update(ExeQuery.queryExeId(exeId), DataSourceProperty.PUB, params);
		}
	}

	public String getTransTableName ()  {
		String sqlStr = "select id from app_table_info ati where system_table_name = 'app_prod_trans_regist_info'";//交易信息登记表id查询
		try {
			SqlRow row = super.findRow(sqlStr, null);
			if (row.isEmpty()){
				log.info("未找到交易信息登记报表-app_prod_trans_regist_info 对应id");
				return "null";
			}
			log.info("交易信息登记报表对应id为:"+ row.getString("id"));
			return row.getString("id");
		} catch (Exception e) {
			log.info("交易信息登记报表对应id异常:" + e.getMessage());
			return "null";
		}


	}

}
