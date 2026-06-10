package com.kayak.rpt.zz.manage.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.rpt.zz.manage.model.ProdRegistFilingInfo;
import com.kayak.rpt.zz.manage.model.TrCustRegisterInfo;
import com.kayak.rpt.zz.manage.model.TrCustTransInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class TrCustRegisterInfoDao extends ComnDao {

	public SqlResult<TrCustRegisterInfo> findTrCustRegisterInfos(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String sql = " SELECT T1.id,T1.report_date,bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type, " +
					" personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark, " +
					" register_serno,imp_date,register_date,register_status,register_acct,ta_id,register_cust_no,create_date,theory_report_start_date,theory_report_end_date, " +
					" moble as moble_display,id_code as id_code_display,cust_name as cust_name_display,tel_phone as tel_phone_display,email as email_display,ifnull(ARS.audit_status,0) audit_status " +
					" FROM app_cust_register_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.report_date=ARS.report_date and ARS.table_id = 'app_cust_register_info' " +
					" where t1.sys_data_status ='1' ";

		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and T1.report_date = '" + params.getModel().getReportDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
			sql = sql + " and T1.report_date >= '" + params.getModel().getQueryStartDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryEndDate())) {
			sql = sql + " and T1.report_date <= '" + params.getModel().getQueryEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getPersonalIdType())) {
			sql = sql + " and T1.personal_id_type in (" + SysUtil.inStr(params.getModel().getPersonalIdType()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getOrganizationIdType())) {
			sql = sql + " and T1.organization_id_type in (" + SysUtil.inStr(params.getModel().getOrganizationIdType()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getDataType())) {
			sql = sql + " and T1.data_type in (" + SysUtil.inStr(params.getModel().getDataType()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
			sql = sql + " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTaId())) {
			sql = sql + " and T1.ta_id = '" + params.getModel().getTaId() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustType())) {
			sql = sql + " and T1.cust_type in (" + SysUtil.inStr(params.getModel().getCustType()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status in (" + SysUtil.inStr(params.getModel().getRegisterStatus()) + ")";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdCode())) {
			sql = sql + " and T1.id_code = '" + params.getModel().getIdCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql = sql + " and T1.id = '" + params.getModel().getId() + "'";
		}
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}
	public int findTrCustRegisterInfosCount(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String sql = " SELECT count(1)  FROM app_cust_register_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.report_date=ARS.report_date and ARS.table_id = 'app_cust_register_info' " +
				" where t1.sys_data_status ='1' ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and T1.report_date = '" + params.getModel().getReportDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
			sql = sql + " and T1.report_date >= '" + params.getModel().getQueryStartDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryEndDate())) {
			sql = sql + " and T1.report_date <= '" + params.getModel().getQueryEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getPersonalIdType())) {
			sql = sql + " and T1.personal_id_type = '" + params.getModel().getPersonalIdType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getOrganizationIdType())) {
			sql = sql + " and T1.organization_id_type = '" + params.getModel().getOrganizationIdType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getDataType())) {
			sql = sql + " and T1.data_type = '" + params.getModel().getDataType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
			sql = sql + " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTaId())) {
			sql = sql + " and T1.ta_id = '" + params.getModel().getTaId() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustType())) {
			sql = sql + " and T1.cust_type = '" + params.getModel().getCustType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdCode())) {
			sql = sql + " and T1.id_code = '" + params.getModel().getIdCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql = sql + " and T1.id = '" + params.getModel().getId() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}
	public int findTrCustRegisterInfosFailStatus(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String sql = " SELECT count(1)  FROM app_cust_register_info T1 LEFT JOIN base_report_data_audit_results ARS ON T1.report_date=ARS.report_date and ARS.table_id = 'app_cust_register_info' " +
				" where T1.sys_data_status ='1'  and T1.register_status in (0,1) ";
		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
			sql = sql + " and T1.report_date = '" + params.getModel().getReportDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryStartDate())) {
			sql = sql + " and T1.report_date >= '" + params.getModel().getQueryStartDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getQueryEndDate())) {
			sql = sql + " and T1.report_date <= '" + params.getModel().getQueryEndDate() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getPersonalIdType())) {
			sql = sql + " and T1.personal_id_type = '" + params.getModel().getPersonalIdType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getOrganizationIdType())) {
			sql = sql + " and T1.organization_id_type = '" + params.getModel().getOrganizationIdType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getDataType())) {
			sql = sql + " and T1.data_type = '" + params.getModel().getDataType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
			sql = sql + " and T1.cust_no = '" + params.getModel().getCustNo() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getTaId())) {
			sql = sql + " and T1.ta_id = '" + params.getModel().getTaId() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getCustType())) {
			sql = sql + " and T1.cust_type = '" + params.getModel().getCustType() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql = sql + " and T1.register_status = '" + params.getModel().getRegisterStatus() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getIdCode())) {
			sql = sql + " and T1.id_code = '" + params.getModel().getIdCode() + "'";
		}
		if (StringUtils.isNotBlank(params.getModel().getId())) {
			sql = sql + " and T1.id = '" + params.getModel().getId() + "'";
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}
	/**
	 * 查询全量身份信息，用于数据比较
	 * @param custNos
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<TrCustRegisterInfo> findAllCustByCustNo(String custNos, SqlParam<TrCustRegisterInfo> params) throws Exception {
		params.setStart(0);
		params.setLimit(1000);
		String sql = "select bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type," +
				" organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark," +
				" ta_id,channel_code,cust_mark " +
				" from (select bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,a.cust_no,cust_type,personal_id_type," +
				" organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark," +
				" ta_id,channel_code,cust_mark,a.deal_date,b.deal_date as deal_date1  " +
				" from ods_cust_base_inf a  " ;
		if(io.micrometer.core.instrument.util.StringUtils.isNotBlank(custNos)){
			sql = sql + " inner join (select cust_no , max(deal_date) as deal_date from ods_cust_base_inf where cust_no in (" + custNos + ") " +
					"  and deal_date < "+ params.getModel().getReportDate() +" group by cust_no  )  b on a.cust_no = b.cust_no ";
			sql = sql + " where a.cust_no in (" + custNos + ") ) c where c.deal_date = c.deal_date1";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<TrCustRegisterInfo> findTrCustRegisterInfosAndIsError(SqlParam<TrCustRegisterInfo> params) throws Exception {
		return super.findRows("SELECT report_date,ta_id,bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no,is_error " +
			"FROM ( SELECT T1.*, ( CASE WHEN t2.register_serno IS NULL THEN '0' ELSE '1' END ) AS is_error " +
			"FROM app_cust_register_info T1 " +
			"LEFT JOIN app_cust_register_info_erdesc T2 ON T2.register_serno = T1.register_serno ) AA",DataSourceProperty.PUB, params);
	}

	public UpdateResult addTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		return super.update("INSERT INTO app_cust_register_info(report_date,ta_id,bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no,create_date,theory_report_start_date, sys_data_status, in_cust_no) VALUES($S{reportDate},$S{taId},$S{bankCode},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{dataType},$S{oriCustNo},$S{custNo},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{remark},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{registerAcct},$S{registerCustNo},date_format(CURDATE(),'%Y%m%d'),$S{reportDate}, '1', if($S{custType} in ('01', '02', '03'), concat('11', $S{personalIdType}, $S{idCode}), concat('22', $S{organizationIdType}, $S{idCode})))",
				DataSourceProperty.PUB,params.getModel());
	}

	public void addTrCustRegisterInfoBatch(List<Map<String, Object>> mapList) throws Exception {
		Date date = new Date();
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(date);
		String  sql  = "INSERT INTO app_cust_register_info(report_date,ta_id,bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no,create_date) VALUES($S{reportDate},$S{taId},$S{bankCode},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{dataType},$S{oriCustNo},$S{custNo},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{remark},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),$S{impDate},$S{registerDate},'0',$S{registerAcct},$S{registerCustNo},date_format(CURDATE(),'%Y%m%d'))";
		for (Map<String, Object> map : mapList) {
			map.put("impDate",dateStr);
			super.update(sql,DataSourceProperty.PUB,map);
		}
	}

	public SqlResult<TrCustRegisterInfo> findValidateInfos(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String sql = "select index_code,reason from base_data_validation where validate_table = $S{validateTable} and deal_date = $S{reportDate} and data_id = $S{dataId}" ;
		return super.findRows(sql, DataSourceProperty.PUB, params);
	}
	/**
	 * 更新投资者身份信息登记报送数据
	 * 同步更新全量投资者基础信息(相同字段全覆盖)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String updSql1 = "UPDATE app_cust_register_info \n" +
				         "   SET theory_report_end_date=$S{theoryReportEndDate} ,report_date=$S{reportDate} ,ta_id=$S{taId} , \n" +
				         "       bank_code=$S{bankCode} ,is_belong=$S{isBelong} ,iss_bank_name=$S{issBankName} , \n" +
				         "       iss_bank_code=$S{issBankCode} ,in_out_sign=$S{inOutSign} ,iss_country=$S{issCountry} , \n" +
				         "       data_type=$S{dataType} ,ori_cust_no=$S{oriCustNo} ,cust_no=$S{custNo} , \n" +
				         "       cust_type=$S{custType} ,personal_id_type=$S{personalIdType} ,organization_id_type=$S{organizationIdType} , \n" +
				         "       other_id_name=$S{otherIdName} ,id_code=$S{idCodeDisplay} ,spv_open_bank=$S{spvOpenBank} , \n" +
				         "       other_open_bank=$S{otherOpenBank} ,cust_name=$S{custNameDisplay} ,sex=$S{sex} , \n" +
				         "       risk_level=$S{riskLevel} ,moble=$S{mobleDisplay} ,tel_phone=$S{telPhoneDisplay} , \n" +
				         "       email=$S{emailDisplay} ,remark=$S{remark}  ,in_cust_no=if($S{custType} in ('01', '02', '03'), concat('11', $S{personalIdType}, $S{idCodeDisplay}), concat('22', $S{organizationIdType}, $S{idCodeDisplay})) ,imp_date=$S{impDate} , \n" +
				         "       register_date=$S{registerDate} ,register_acct=$S{registerAcct} , \n" +
				         "       register_cust_no=$S{registerCustNo}  \n" +
				         " WHERE id = $S{id} ";

		return super.update(updSql1, DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult updateTrCustInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		String UpdSql2 = "update ods_cust_base_inf b \n" +
				"   set b.bank_code = $S{bankCode}, b.cust_no = $S{custNo}, \n" +
				"       b.ori_cust_no = $S{oriCustNo}, b.is_belong = $S{isBelong}, b.iss_bank_name = $S{issBankName}, \n" +
				"       b.iss_bank_code = $S{issBankCode}, b.in_out_sign = $S{inOutSign}, b.iss_country = $S{issCountry}, \n" +
				"       b.cust_type = $S{custType}, b.personal_id_type = $S{personalIdType}, b.organization_id_type = $S{organizationIdType}, \n" +
				"       b.other_id_name = $S{otherIdName}, b.id_code = $S{idCodeDisplay}, b.spv_open_bank = $S{spvOpenBank}, \n" +
				"       b.other_open_bank = $S{otherOpenBank}, b.cust_name = $S{custNameDisplay}, b.sex = $S{sex}, \n" +
				"       b.risk_level = $S{riskLevel}, b.moble = $S{mobleDisplay}, b.tel_phone = $S{telPhoneDisplay}, \n" +
				"       b.email = $S{emailDisplay}, b.ta_id = $S{taId}, \n" +
				"       b.remark = $S{remark}, inner_cust_no=if($S{custType} in ('01', '02', '03'), concat('11', $S{personalIdType}, $S{idCodeDisplay}), concat('22', $S{organizationIdType}, $S{idCodeDisplay})) ,upd_dt=$S{impDate} ,b.upd_user = '" + SysUtil.getSysUserParams().get("userid") + "', \n" +
				"       b.upd_dt = date_format(sysdate(), '%Y%m%d') \n" +
				" where b.cust_no = $S{custNo} ";

		String UpdSql3 = "insert into ods_cust_base_inf\n" +
				"(bank_code,cust_no,inner_cust_no,ori_cust_no,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,cust_type,personal_id_type\n" +
				",organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,ta_id\n" +
				",channel_code,cust_mark,remark,crt_user,upd_user,crt_dt,upd_dt,data_type,deal_date,strt_dt,end_dt)\n" +
				"select\n" +
				" bank_code                     as bank_code\n" +
				",cust_no                       as cust_no\n" +
				",in_cust_no                    as inner_cust_no\n" +
				",ori_cust_no                   as ori_cust_no\n" +
				",is_belong                     as is_belong\n" +
				",iss_bank_name                 as iss_bank_name\n" +
				",iss_bank_code                 as iss_bank_code\n" +
				",in_out_sign                   as in_out_sign\n" +
				",iss_country                   as iss_country\n" +
				",cust_type                     as cust_type\n" +
				",personal_id_type              as personal_id_type\n" +
				",organization_id_type          as organization_id_type\n" +
				",other_id_name                 as other_id_name\n" +
				",id_code                       as id_code\n" +
				",spv_open_bank                 as spv_open_bank\n" +
				",other_open_bank               as other_open_bank\n" +
				",cust_name                     as cust_name\n" +
				",sex                           as sex\n" +
				",risk_level                    as risk_level\n" +
				",moble                         as moble\n" +
				",tel_phone                     as tel_phone\n" +
				",email                         as email\n" +
				",ta_id                         as ta_id\n" +
				",channel_code                  as channel_code\n" +
				",cust_mark                     as cust_mark\n" +
				",remark                        as remark\n" +
				",'" + SysUtil.getSysUserParams().get("userid") + "' as crt_user\n" +
				",'" + SysUtil.getSysUserParams().get("userid") + "' as upd_user\n" +
				",date_format(now(), '%h%i%s')  as crt_dt\n" +
				",date_format(now(), '%h%i%s')  as upd_dt\n" +
				",'01'                          as data_type\n" +
				",report_date                   as deal_date\n" +
				",report_date                   as strt_dt\n" +
				",'20991231'                    as end_dt\n" +
				" from app_cust_register_info a\n" +
				"where a.cust_no = $S{custNo}\n" +
				"  and a.data_type = '01'\n" +
				"  and not exists(select 1 from ods_cust_base_inf b\n" +
				"                  where b.cust_no = $S{custNo}) ";

		// 投资者持有、投资者持有（子产品）、投资者明细表
		// 1、先更新本表数据已报送
		String UpdSql4 = "update " + DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate()) + " set cust_no = $S{custNo}, register_status = '2' where ta_id = $S{taId}";
		String UpdSql5 = "update " + DateUtil.getInvSubTable("app_cust_vol_register_sub_info", params.getModel().getReportDate()) + " set cust_no = $S{custNo}, register_status = '2' where report_date = $S{reportDate} and ta_id = $S{taId}";
		String UpdSql6 = "update " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " set cust_no = $S{custNo}, register_status = '2' where ta_id = $S{taId}";
		// 2、在更新中台历史数据，通过modify表去更新，晚批会推送到中台
		String IntSql7 = "replace into app_cust_vol_register_info_modify (bank_code, prod_code, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, register_serno, create_date, theory_report_start_date, theory_report_end_date, report_date, PROD_CODE_M, TA_ID, MRG_TYP, sys_data_status, sys_data_source, sys_data_version) " +
				"         select bank_code, prod_code, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, register_serno, date_format(CURDATE(), '%Y%m%d'), theory_report_start_date, theory_report_end_date, report_date, PROD_CODE_M, TA_ID, MRG_TYP, sys_data_status, sys_data_source, sys_data_version " +
				"           from " + DateUtil.getInvTable("app_cust_vol_register_info", params.getModel().getReportDate()) + " where ta_id = $S{taId}";
		String IntSql8 = "replace into app_cust_vol_register_sub_info_modify (bank_code, prod_code, prod_code_m, prod_code_s, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, register_serno, create_date, theory_report_start_date, theory_report_end_date, report_date, mrg_typ, ta_id, channel_flag, cust_type, personal_id_type, organization_id_type, other_id_name, id_code) " +
				"         select bank_code, prod_code, prod_code_m, prod_code_s, cust_no, hold_date, cur, hold_vol, hold_amt, convert_rmb, imp_date, register_date, register_status, register_serno, date_format(CURDATE(), '%y%m%d'), theory_report_start_date, theory_report_end_date, report_date, mrg_typ, ta_id, channel_flag, cust_type, personal_id_type, organization_id_type, other_id_name, id_code " +
				"           from " + DateUtil.getInvSubTable("app_cust_vol_register_sub_info", params.getModel().getReportDate()) + " where report_date = $S{reportDate} and ta_id = $S{taId}";
		String IntSql9 = "replace into app_cust_trans_info_modify (bank_code, trans_serno, contract_no, fnc_trans_acct_no, host_cust_no, cust_no, cust_name, deal_no, acct_no, acct_bank_no, acct_bank_name, acct_loc_code, is_agent, agent_bank_code, agent_bank_name, agent_regu_code, prod_code, busi_code, busi_regu_code, ack_date, ack_time, cur, ack_amt, convert_rmb, nav, ack_vol, fee_amt, channel_flag, inputuser, remark, register_serno, imp_date, register_date, register_status, create_date, theory_report_start_date, theory_report_end_date, report_date, ta_id, prod_code_m, sys_data_status, sys_data_source, sys_data_version, cust_name_ori, son_share_code, spe_channel_flag) " +
				"         select bank_code, trans_serno, contract_no, fnc_trans_acct_no, host_cust_no, cust_no, cust_name, deal_no, acct_no, acct_bank_no, acct_bank_name, acct_loc_code, is_agent, agent_bank_code, agent_bank_name, agent_regu_code, prod_code, busi_code, busi_regu_code, ack_date, ack_time, cur, ack_amt, convert_rmb, nav, ack_vol, fee_amt, channel_flag, inputuser, remark, register_serno, imp_date, register_date, register_status, date_format(CURDATE(), '%Y%m%d'), theory_report_start_date, theory_report_end_date, report_date, ta_id, prod_code_m, sys_data_status, sys_data_source, sys_data_version, cust_name_ori, son_share_code, spe_channel_flag " +
				"           from " + DateUtil.getInvTable("app_cust_trans_info", params.getModel().getReportDate()) + " where ta_id = $S{taId}";

		// 更新全量投资者身份
		super.update(UpdSql2, DataSourceProperty.PUB,params.getModel());
		UpdateResult updateResult = super.update(UpdSql3, DataSourceProperty.PUB,params.getModel());
		// 如果是新增类型，才需要更新
		try {
			if ("01".equals(params.getModel().getDataType())) {
				super.update(UpdSql4, DataSourceProperty.PUB,params.getModel());
				super.update(UpdSql5, DataSourceProperty.PUB,params.getModel());
				super.update(UpdSql6, DataSourceProperty.PUB,params.getModel());

				super.update(IntSql7, DataSourceProperty.PUB,params.getModel());
				super.update(IntSql8, DataSourceProperty.PUB,params.getModel());
				super.update(IntSql9, DataSourceProperty.PUB,params.getModel());
			}
		} catch (Exception e) {
			log.error("同步修改投资者持有、子产品、明细信息失败！"+e.getMessage(), e);
			throw e;
		}
		return updateResult;
	}

	public List<SqlRow> findTrCustRegisterStatus(SqlParam<TrCustRegisterInfo> params) throws Exception {
		return super.findRows("select register_status from app_cust_register_info where id = "+params.getModel().getId());
	}

	public UpdateResult deleteTrCustRegisterInfo(SqlParam<TrCustRegisterInfo> params) throws Exception {
		return super.update("DELETE FROM app_cust_register_info WHERE id = $S{id} ",
				DataSourceProperty.PUB,params.getModel());
	}

	public UpdateResult addImportTrCustRegisterInfo(Object param) throws Exception {
		return super.update("INSERT INTO app_cust_register_info(report_date,ta_id,bank_code,is_belong,iss_bank_name,iss_bank_code,in_out_sign,iss_country,data_type,ori_cust_no,cust_no,cust_type,personal_id_type,organization_id_type,other_id_name,id_code,spv_open_bank,other_open_bank,cust_name,sex,risk_level,moble,tel_phone,email,remark,register_serno,imp_date,register_date,register_status,register_acct,register_cust_no,create_date,theory_report_start_date,sys_data_status) " +
						                                  "VALUES($S{reportDate},$S{taId},$S{bankCode},$S{isBelong},$S{issBankName},$S{issBankCode},$S{inOutSign},$S{issCountry},$S{dataType},$S{oriCustNo},$S{custNo},$S{custType},$S{personalIdType},$S{organizationIdType},$S{otherIdName},$S{idCode},$S{spvOpenBank},$S{otherOpenBank},$S{custName},$S{sex},$S{riskLevel},$S{moble},$S{telPhone},$S{email},$S{remark},(select concat(DATE_FORMAT(NOW(), '%y%m%d%H%i%s'),UUID_SHORT()) from dual),date_format(CURDATE(),'%Y%m%d'),$S{registerDate},'0',$S{registerAcct},$S{registerCustNo},date_format(CURDATE(),'%Y%m%d'),$S{theoryReportStartDate},'1')",
				DataSourceProperty.PUB,param);
	}

	/**
	 * 根据识别标识对投资者身份信息数据进行更新操作
	 * @param param 参数
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateRegisterInfoByCustNo(Object param) throws Exception {
		return super.update("update app_cust_register_info set ta_id = $S{taId}, bank_code = $S{bankCode}, is_belong = $S{isBelong}, iss_bank_name = $S{issBankName}, iss_bank_code = $S{issBankCode}, in_out_sign = $S{inOutSign}, iss_country = $S{issCountry}, data_type =$S{dataType}, ori_cust_no = $S{oriCustNo}, cust_type = $S{custType}, personal_id_type = $S{personalIdType}, organization_id_type = $S{organizationIdType}, other_id_name = $S{otherIdName}, id_code = $S{idCode}, spv_open_bank = $S{spvOpenBank}, other_open_bank = $S{otherOpenBank}, cust_name = $S{custName}, sex = $S{sex}, risk_level = $S{riskLevel}, moble = $S{moble}, tel_phone = $S{telPhone}, email = $S{email}, remark = $S{remark}, in_cust_no = if($S{custType} in ('01', '02', '03'), concat('11', $S{personalIdType}, $S{idCode}), concat('22', $S{organizationIdType}, $S{idCode})), register_date = $S{registerDate}, sys_data_status = '1' where cust_no = $S{custNo} and report_date = $S{reportDate}", DataSourceProperty.PUB, param);
	}

	/** 手动确认成功
	 * 更新指定日期的数据为报送成功
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public UpdateResult updateRegisterInfoRegisterStatusSuccess(SqlParam<TrCustRegisterInfo> params) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE app_cust_register_info T1 SET T1.register_status='3' WHERE T1.sys_data_status ='1' and");
		sql.append(" T1.report_date >= '" + params.getModel().getQueryStartDate() + "'");
		sql.append(" and T1.report_date <= '" + params.getModel().getQueryEndDate() + "'");
//		StringBuilder sql = new StringBuilder("UPDATE app_cust_register_info T1 SET T1.register_status='3' WHERE 1=1 ");
//		if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
//			sql.append(" and T1.report_date ='" + params.getModel().getReportDate()+ "'");
//		}
		if (StringUtils.isNotBlank(params.getModel().getPersonalIdType())) {
			sql.append(" and T1.personal_id_type ='" + params.getModel().getPersonalIdType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getOrganizationIdType())) {
			sql.append(" and T1.organization_id_type ='" + params.getModel().getOrganizationIdType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getDataType())) {
			sql.append(" and T1.data_type ='" + params.getModel().getDataType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getCustNo())) {
			sql.append(" and T1.cust_no ='" + params.getModel().getCustNo() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getTaId())) {
			sql.append(" and T1.ta_id ='" + params.getModel().getTaId() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getCustType())) {
			sql.append(" and T1.cust_type ='" + params.getModel().getCustType() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getIdCode())) {
			sql.append(" and T1.id_code ='" + params.getModel().getIdCode() + "'");
		}
		if (StringUtils.isNotBlank(params.getModel().getRegisterStatus())) {
			sql.append(" and T1.register_status ='" + params.getModel().getRegisterStatus() + "'");
		}
		return super.update(sql.toString(), DataSourceProperty.PUB, params);
	}
	public UpdateResult deleteImportSubseqSubscrRegistInfo(Map<String, Object> params) throws Exception {
		return super.update("DELETE FROM app_cust_register_info where report_date between $S{beginDate} and $S{queryDate} ", params);
	}

    public List<SqlRow> getPersonalIdTypeDict(Map<String, Object> params)  throws Exception {
		String sql = "SELECT itemkey VALUE,itemval TEXT FROM sys_dict_item where dict='tr_personal_id_type'  ";
		if(StringUtils.equals(String.valueOf(params.get("inOutSign")),"01")){
			sql += " and itemorder not in ('21','22','23','24','99') ";
		}else if(StringUtils.equals(String.valueOf(params.get("inOutSign")),"02")){
			sql += " and itemorder in ('21','22','23','24','99')  ";
		}
		sql += "  order by itemorder+0  ";
		return super.findRows(sql,DataSourceProperty.PUB,params);
    }

	//查看客户号 下是否有持有记录
	public int findProdTrCustVolEffective(TrCustRegisterInfo params) throws Exception {
//		String workdate = DateUtil.getSysWordDay();
		StringBuilder sql = new StringBuilder("");
		sql.append("select count(1) from app_cust_vol_register_info where sys_data_status ='1' ");
//		if (Strings.isNotBlank(params.getModel().getReportDate())) {
//			sql.append(" and report_date = ").append(params.getModel().getReportDate());
//		}
		if (StringUtils.isNotBlank(params.getCustNo())) {
			sql.append(" and cust_no = '").append(params.getCustNo()).append("'");
		}
		return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql.toString(),DataSourceProperty.PUB, params)));
	}

	//查看客户号 下是否有投资者明细记录
	public int findProdTrCustTransEffective(TrCustRegisterInfo params) throws Exception {
			String sql = "select count(1) from app_cust_trans_info  where sys_data_status ='1' ";

//			if (StringUtils.isNotBlank(params.getModel().getReportDate())) {
//				sql += " and report_date = " + params.getModel().getReportDate();
//			}
			if (StringUtils.isNotBlank(params.getCustNo())) {
				sql += " and cust_no = '" + params.getCustNo() + "'";
			}
			return Integer.parseInt(String.valueOf(super.findRow(Integer.class,sql,DataSourceProperty.PUB, params)));
	}



}
