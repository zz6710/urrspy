package com.kayak.pms.T82.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.Sql;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.T82.model.T82001;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class T8DistributorInfoDao extends ComnDao {


	//校验销售商与产品是否可以关联
	public  void   disprodformyActionbefor(Map<String,Object> params) throws Exception {
		String sqlAll = "select decode (instr((select case" +
				"                       when (REDEEM_CFM_M = '0' OR APPLY_CFM_M = '0' OR" +
				"                            SUBS_CFM_N = '0') then" +
				"                        '0'" +
				"                       else" +
				"                        '01'" +
				"                     end" +
				"                from t8_prod_open" +
				"               where data_status = 'E'" +
				"                 and prod_code = $S{prodCode}),(select batch_no" +
				"               from t8_distributor_info" +
				"              where distributor_code = $S{distributorCode})),null,0,instr((select case" +
				"                       when (REDEEM_CFM_M = '0' OR APPLY_CFM_M = '0' OR" +
				"                            SUBS_CFM_N = '0') then" +
				"                        '0'" +
				"                       else" +
				"                        '01'" +
				"                     end" +
				"                from t8_prod_open" +
				"               where data_status = 'E'" +
				"                 and prod_code = $S{prodCode}),(select batch_no" +
				"               from t8_distributor_info" +
				"              where distributor_code = $S{distributorCode})) )result "+
				"  from DUAL";
		String sqlDb2 = "select decode (instr((select case" +
				"                       when (REDEEM_CFM_M = '0' OR APPLY_CFM_M = '0' OR" +
				"                            SUBS_CFM_N = '0') then" +
				"                        '0'" +
				"                       else" +
				"                        '01'" +
				"                     end" +
				"                from t8_prod_open" +
				"               where data_status = 'E'" +
				"                 and prod_code = $S{prodCode}),(select batch_no" +
				"               from t8_distributor_info" +
				"              where distributor_code = $S{distributorCode})),null,0,instr((select case" +
				"                       when (REDEEM_CFM_M = '0' OR APPLY_CFM_M = '0' OR" +
				"                            SUBS_CFM_N = '0') then" +
				"                        '0'" +
				"                       else" +
				"                        '01'" +
				"                     end" +
				"                from t8_prod_open" +
				"               where data_status = 'E'" +
				"                 and prod_code = $S{prodCode}),(select batch_no" +
				"               from t8_distributor_info" +
				"              where distributor_code = $S{distributorCode})) )result "+
				"  from sysibm.sysdummy1";
		Sql sql = Sql.build().mysqlSql(sqlAll).db2Sql(sqlDb2);
		List<SqlRow> datas=super.findRows(SqlRow.class, sql, 0, params);

			if(datas.get(0).getInteger("result") < 1){
				throw new PromptException(" 产品 ["+params.get("prodCode")+"] 不可与该销售商关联,请关闭当前页再试");
			}
	}
	

	public void disprodformyAction(List<Map<String,Object>> params) throws Exception {

		doTrans(() ->{
			for (Map<String,Object> param : params) {
				String sqlAll = "INSERT INTO t8_prod_distributor (" +
						" distributor_code,  prod_code," +
						" status," +
						" crt_time," +
						" upd_time" +
						" )" +
						" VALUES(" +
						" $S{distributorCode}, $S{prodCode}," +
						" '1'," +
						" current_timestamp," +
						" current_timestamp" +
						")";
				String sqlDb2 = "INSERT INTO t8_prod_distributor (" +
						" distributor_code,  prod_code," +
						" status," +
						" crt_time," +
						" upd_time" +
						" )" +
						" VALUES(" +
						" $S{distributorCode}, $S{prodCode}," +
						" '1'," +
						" current timestamp," +
						" current timestamp" +
						")";
				Sql sql = Sql.build().mysqlSql(sqlAll).db2Sql(sqlDb2);
				super.update(sql, param);
			}
		});
	}

	public int findTaDistributorInfoCounts(Map<String,Object> params) throws Exception {

		List<SqlRow> datas=super.findRows("SELECT COUNT(1) count FROM t8_distributor_info WHERE distributor_code = $S{distributorCode}", params);
		return datas.get(0).getInteger("count");
	}

	public SqlResult<T82001> findTaDistributorInfoCounts(SqlParam<T82001> params) throws Exception {
		params.setMakeSql(false);
		String sql = "SELECT DISTRIBUTOR_CODE FROM t8_distributor_info WHERE 1 = 1 ";
		if(StringUtils.isNotBlank(params.getModel().getDistributorCode())){
			sql = sql + " and  distributor_code = '"+params.getModel().getDistributorCode()+"' ";
		}
		if(StringUtils.isNotBlank(params.getModel().getId())){
			sql = sql + " and id <> '"+params.getModel().getId()+"' ";
		}
		return super.findRows(sql, params);
	}

	//调整销售商信息表,添加主键id
	public int findTaDistributorInfoCountsById(Map<String,Object> params) throws Exception {

		List<SqlRow> datas=super.findRows("SELECT COUNT(1) count FROM t8_distributor_info WHERE distributor_code = $S{distributorCode} and id !=$S{id}", params);
		return datas.get(0).getInteger("count");
	}

	//查询销售商修改前的代码
	public List<SqlRow> findTaDistributorCodeCountsById(Map<String,Object> params) throws Exception {

		List<SqlRow> datas=super.findRows("SELECT distributor_code  FROM t8_distributor_info WHERE  id =$S{id}", params);
		return datas;
	}

	public SqlResult<T82001> findTaDistributorInfos(SqlParam<T82001> params) throws Exception {

		String sql = "SELECT  "
				+ "	dis.id,dis.official_website,dis.customer_service_hotline,dis.main_duty,dis.distributor_code,dis.distributor_name,dis.dept,dis.distributor_simplify_name,dis.distributor_type,dis.manager_dept,dis.status,dis.n_legal_code,"
				+ "	dis.n_legal_type,dis.n_legal_id_code,dis.tech_connector,dis.tech_connector_mobile,dis.busi_connector,dis.busi_connector_mobile,dis.address,"
				+ "	dis.email,dis.postcode,dis.fax,dis.interface_type,dis.interface_version,dis.is_export_c1c5_file,dis.is_export_c6_26_file,dis.is_export_sale_fee_file,"
				+ "	dis.allow_break_redeem,dis.is_trans_much_acct,dis.is_single_trust,dis.convert_ack_method,dis.is_vol_list,dis.check_type,"
				+ "	dis.is_predistribution_acct,dis.present_confirm_num,dis.crt_time,dis.crt_user,dis.upd_time,dis.upd_user,"
				+ "	dis.remark,dis.file_imp_flag,dis.is_holidays_send,dis.fundday_file_path,dis.cfm_file_path,dis.req_file_path,"
				+ "	dis.freez_file_type, dis.pgmno,dis.process_status,dis.org_manage_dept,dis.inter_manage_dept " +
				" FROM T8_DISTRIBUTOR_INFO dis ORDER BY dis.crt_time desc";

		return (SqlResult<T82001>) super.findRows(sql, params);
	}

	public int addTaDistributorInfo(SqlParam<T82001> params) throws Exception {
		String sql = "INSERT INTO t8_distributor_info (id, " +
				"     distributor_code,  distributor_name," +
				"     manager_dept," +
				"     distributor_type,  status," +
				"     n_legal_code,      n_legal_type," +
				"     n_legal_id_code,   tech_connector," +
				"     tech_connector_mobile,  busi_connector,  busi_connector_mobile," +
				"     address,           email," +
				"     fax,               postcode," +
				"     interface_type,    interface_version," +
				"     is_export_c1c5_file, is_export_c6_26_file, is_export_sale_fee_file," +
				"     allow_break_redeem,   is_trans_much_acct," +
				"     is_single_trust,    convert_ack_method," +
				"     is_vol_list," +
				"     check_type," +
				"     remark,req_file_path,cfm_file_path," +
				"     fundday_file_path,is_holidays_send, crt_time,crt_user," +
				"     upd_time,upd_user,pgmno,imp_task_group,exp_task_group,data_status,org_manage_dept,inter_manage_dept,official_website,customer_service_hotline,main_duty)" +
				"     VALUES($AUTOIDS{id}, " +
				"     $S{distributorCode}, $S{distributorName}," +
				"     $S{managerDept}, " +
				"     $S{distributorType}, $S{status}," +
				"     $S{nLegalCode},       $S{nLegalType}," +
				"     $S{nLegalIdCode}," +
				"     $S{techConnector},   $S{techConnectorMobile}," +
				"     $S{busiConnector},   $S{busiConnectorMobile}," +
				"     $S{address},          $S{email}," +
				"     $S{fax},              $S{postcode}," +
				"     $S{interfaceType},   $S{interfaceVersion}," +
				"     $S{isExportC1c5File},$S{isExportC626File},$S{isExportSaleFeeFile}," +
				"     $S{allowBreakRedeem},  $S{isTransMuchAcct}," +
				"     $S{isSingleTrust},     $S{convertAckMethod}," +
				"     $S{isVolList}," +
				"     $S{checkType}," +
				"     $S{remark}," +
				"     $S{reqFilePath},$S{cfmFilePath},$S{funddayFilePath}," +
				"     $S{isHolidaysSend}," +
				"     current_timestamp,$S{crtUser}," +
				"     current_timestamp,$S{updUser}," +
				"     $S{pgmno},$S{impTaskGroup},$S{expTaskGroup},'A',$S{orgManageDept},$S{interManageDept},$S{officialWebsite},$S{customerServiceHotline},$S{mainDuty}" +
				"                     )";
		return super.update(sql, params.getModel()).getEffect();
	}

	
	public void updateTaDistributorInfo(SqlParam<T82001> params) throws Exception {

		doTrans(() -> {
			String sql = " UPDATE t8_distributor_info " +
					"               SET distributor_code = $S{distributorCode}," +
					"                   distributor_name = $S{distributorName}," +
					"                   distributor_type = $S{distributorType}," +
					"                   manager_dept = $S{managerDept}," +
					"                   status           = $S{status}," +
					"                   n_legal_code     = $S{nLegalCode}," +
					"                   n_legal_type     = $S{nLegalType}," +
					"                   n_legal_id_code  = $S{nLegalIdCode}," +
					"                   tech_connector   = $S{techConnector}," +
					"                   tech_connector_mobile = $S{techConnectorMobile}," +
					"                   busi_connector        = $S{busiConnector}," +
					"                   busi_connector_mobile = $S{busiConnectorMobile}," +
					"                   address            = $S{address}," +
					"                   email              = $S{email}," +
					"                   fax                = $S{fax}," +
					"                   postcode           = $S{postcode}," +
					"                   interface_type     = $S{interfaceType}," +
					"                   interface_version  = $S{interfaceVersion}," +
					"                   is_export_c1c5_file   = $S{isExportC1c5File}," +
					"                   is_export_c6_26_file   = $S{isExportC626File}," +
					"                   is_export_sale_fee_file   = $S{isExportSaleFeeFile}," +
					"                   allow_break_redeem = $S{allowBreakRedeem}," +
					"                   is_trans_much_acct = $S{isTransMuchAcct}," +
					"                   is_single_trust    = $S{isSingleTrust}," +
					"                   convert_ack_method = $S{convertAckMethod}," +
					"                   is_vol_list        = $S{isVolList}," +
					"                   check_type         = $S{checkType}," +
					"                   remark             = $S{remark}," +
					"                   req_file_path      = $S{reqFilePath}," +
					"                   cfm_file_path      = $S{cfmFilePath}," +
					"                   fundday_file_path  = $S{funddayFilePath}," +
					"                   is_holidays_send   = $S{isHolidaysSend}," +
					"                   pgmno              =$S{pgmno}," +
					"                   upd_user           = $S{crtUser}," +
					"                   imp_task_group           = $S{impTaskGroup}," +
					"                   exp_task_group           = $S{expTaskGroup}," +
					"                   upd_time           = current_timestamp," +
					"                   org_manage_dept = $S{orgManageDept}," +
					"                   inter_manage_dept = $S{interManageDept}," +
					"                   official_website = $S{officialWebsite}," +
					"                   customer_service_hotline = $S{customerServiceHotline}," +
					"                   main_duty = $S{mainDuty}" +

					"  WHERE id = $S{id}";
			super.update(sql, params.getModel());
			super.update(
					"INSERT INTO t8_distributor_info_his("
							+" distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,oper_user,oper_date,oper_flag,data_status)"
							+" SELECT"
							+" 	distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" 	tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" 	is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" 	is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,$S{crtUser},NOW(),'1','E'"
							+" FROM t8_distributor_info WHERE distributor_code = $S{distributorCode}", params.getModel());
		});
	}
	
	public int deleteTaDistributorInfo(SqlParam<T82001> params) throws Exception {
		return super.update("DELETE FROM T8_DISTRIBUTOR_INFO WHERE distributor_code = $S{distributorCode}",
				params.getModel()).getEffect();
	}


	public void startTaDistributorInfo(SqlParam<T82001> params) throws Exception {

		doTrans(() -> {
			super.update(
					" UPDATE t8_distributor_info" +
							"   SET status      = '1'" +
							" WHERE distributor_code = $S{distributorCode} ", params.getModel());
			super.update(
					"INSERT INTO t8_distributor_info_his("
							+" distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,oper_user,oper_date,oper_flag,data_status)"
							+" SELECT"
							+" 	distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" 	tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" 	is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" 	is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,$S{crtUser},NOW(0),'1','E'"
							+" FROM t8_distributor_info WHERE distributor_code = $S{distributorCode}", params.getModel());
		});
	}


	


	public int stopTaDistributorInfoBefor(Map<String,Object> params) throws Exception {

		List<SqlRow> datas=super.findRows("SELECT" +
				" count(*) as count" +
				" FROM T8_CUST_VOL" +
				" WHERE distributor_code = $S{distributorCode}" +
				" AND total_vol > 0", params);
		return datas.get(0).getInteger("count");
	}


	public void stopTaDistributorInfo(SqlParam<T82001> params) throws Exception {

		doTrans(() -> {
			super.update(
					" UPDATE t8_distributor_info" +
							"   SET status      = '2'" +
							" WHERE distributor_code = $S{distributorCode} ", params.getModel()).getEffect();
			super.update(
					"INSERT INTO t8_distributor_info_his("
							+" distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,oper_user,oper_date,oper_flag,data_status)"
							+" SELECT"
							+" 	distributor_code,distributor_name,distributor_type,status,n_legal_code,n_legal_type,n_legal_id_code,tech_connector,"
							+" 	tech_connector_mobile,busi_connector,busi_connector_mobile,address,email,fax,postcode,interface_type,interface_version,"
							+" 	is_export_c1c5_file,is_export_c6_26_file,is_export_sale_fee_file,allow_break_redeem,is_trans_much_acct,is_single_trust,convert_ack_method,"
							+" 	is_vol_list,check_type,remark,req_file_path,cfm_file_path,fundday_file_path,is_holidays_send,crt_time,crt_user,upd_time,upd_user,$S{crtUser},NOW(),'1','E'"
							+" FROM t8_distributor_info WHERE distributor_code = $S{distributorCode}", params.getModel()).getEffect();
		});
	}

	public SqlResult<T82001> findVersionByType(SqlParam<T82001> params) throws Exception{
		return super.findRows("SELECT distinct interface_version from T8_DISTRIBUTOR_INFO where interface_type = $S{interfaceType}", params);

	}

    public SqlResult<T82001> findDistributorDict(SqlParam<T82001> params) throws Exception {
		return super.findRows("select distributor_code,distributor_name ,distributor_type,dept from t8_distributor_info", params);
    }

    public SqlResult<T82001> findDistributorDept(SqlParam<T82001> params) throws Exception {
		return super.findRows("select dept,distributor_type from t8_distributor_info where distributor_code = $S{distributorCode}", params);
    }
    
    
    public SqlResult<T82001> findDistributorByProdCode(SqlParam<T82001> params) throws Exception {
		return super.findRows("SELECT d.distributor_code,d.distributor_name from t8_distributor_info  d " +
				"LEFT JOIN t8_prod_sale s on FIND_IN_SET(d.DISTRIBUTOR_CODE,s.distributor_code) " +
				"where s.prod_code = $S{prodCode}", params);
    }

	/**
	 * 功能：根据产品代码与部门查询创设时选择的销售商作为字典
	 * 作者：rennannan
	 * 日期：20210305
	 * @param params
	 * @return
	 * @throws Exception
	 */
    public SqlResult<T82001> findDistributorByCode(SqlParam<T82001> params) throws Exception {
		String sql = "select distributor_code,DISTRIBUTOR_NAME\n" +
                "  from t8_distributor_info \n" +
                " where FIND_IN_SET(DISTRIBUTOR_CODE,\n" +
                " (select sale.distributor_code \n" +
                "   from t8_prod_sale  sale \n" +
                "  where sale.prod_code = $S{prodCode}" +
                "  )\n" +
                " )";
		return super.findRows(sql,params);
	}
}
