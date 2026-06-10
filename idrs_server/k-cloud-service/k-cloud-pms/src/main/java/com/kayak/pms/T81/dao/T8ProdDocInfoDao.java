package com.kayak.pms.T81.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.model.T8ProdDocInfo;
import com.kayak.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class T8ProdDocInfoDao extends ComnDao {

	public SqlResult<T8ProdDocInfo> findT8ProdDocInfos(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.findRows("SELECT DISTINCT t1.is_currency_template,t1.id,t2.prod_mode,t2.prod_code,t2.prod_name,t2.raise_type,t3.meet_name,t4.temp_name,\n" +
				"t5.itemval,t1.t8_prod_info_id,t1.doc_type,t1.distributor_code,t1.t8_trutee_info_id, \n" +
				"t1.t8_meet_create_id,t1.t8_print_temp_version_id,t1.doc_version,t1.doc_desc,t1.correlation_time   \n" +
				"FROM t8_prod_doc_info t1 left join t8_prod_info t2 on t1.t8_prod_info_id = t2.id  \n" +
				"left join t8_meet_create t3 on t1.t8_meet_create_id = t3.id  \n" +
				"left join (select tv.id ,tv.temp_name,tp.temp_type from t8_print_temp_version tv left join t8_print_temp tp on tv.t8_print_temp_id = tp.id\n" +
				"           union all\n" +
				"\t\t\t\t\tselect tv.id ,tv.temp_name,tp.temp_type from t8_static_temp_version tv left join t8_static_temp tp  on tv.t8_static_temp_id = tp.id) t4\n" +
				"on t4.id = t1.t8_print_temp_version_id  and temp_type = t1.doc_type\n" +
				"left join sys_dict_item t5 on t5.itemkey = t1.doc_type  \n" +
				"WHERE t5.dict in ('t8_temp_type_fb_gm','t8_temp_type_tt_gm','t8_temp_type_zq_gm','t8_temp_type_hb', \n" +
				"'t8_temp_type_zq_sm','t8_temp_type_tt_sm','t8_temp_type_fb_sm','t8_temp_type_qt','t8_temp_type_ty')", params);
	}

	public List<T8ProdDocInfo> findT8ProdDocInfos(String t8ProdInfoId) throws Exception {
		return super.findRows(T8ProdDocInfo.class,"SELECT * FROM t8_prod_doc_info  where t8_prod_info_id=$S{t8ProdInfoId} ", 0,t8ProdInfoId);
	}

	public SqlResult<T8ProdDocInfo> getMeetName(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.findRows("SELECT itemval from sys_dict_item " +
				"WHERE dict in ('t8_temp_type_fb_gm','t8_temp_type_tt_gm','t8_temp_type_zq_gm','t8_temp_type_hb'," +
				"'t8_temp_type_zq_sm','t8_temp_type_tt_sm','t8_temp_type_fb_sm','t8_temp_type_qt','t8_temp_type_ty') and itemkey = $S{docType}", params);
	}

	public List<SqlRow> getT8ProdDocInfo(String  prodCode,String docType) throws Exception {
		return super.findRows("select tpdi.* from t8_prod_info tpi LEFT JOIN t8_prod_doc_info tpdi on tpi.id = tpdi.t8_prod_info_id where tpi.prod_code='"+prodCode+"' and doc_type='"+docType+"' limit 0,1");
	}

	public UpdateResult addT8ProdDocInfo(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.update("INSERT INTO t8_prod_doc_info(id,t8_prod_info_id,doc_type,distributor_code,t8_trutee_info_id,t8_meet_create_id,t8_print_temp_version_id,doc_version,doc_desc,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user) " +
						" VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{docType},$S{distributorCode},$S{t8TruteeInfoId},$S{t8MeetCreateId},$S{t8PrintTempVersionId},$S{docVersion},$S{docDesc},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser})",
				params.getModel());
	}

	public UpdateResult updateT8ProdDocInfo(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.update("UPDATE t8_prod_doc_info SET t8_prod_info_id=$S{t8ProdInfoId} ,doc_type=$S{docType} ,distributor_code=$S{distributorCode} ," +
						"t8_trutee_info_id=$S{t8TruteeInfoId} ,t8_meet_create_id=$S{t8MeetCreateId} ,t8_print_temp_version_id=$S{t8PrintTempVersionId} ," +
						"doc_version=$S{docVersion} ,doc_desc=$S{docDesc},upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user=$S{updUser}   WHERE  id=$S{id} ",
				params.getModel());
	}

	public UpdateResult deleteT8ProdDocInfo(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.update("DELETE FROM t8_prod_doc_info WHERE  id=$S{id} ",
				params.getModel());
	}

	public T8ProdDocInfo getProdManualT8ProdDocInfoByProdCode(T8ProdDocInfo t8ProdDocInfo) throws Exception {
		String sql = "select t.id, " +
				"       t.t8_prod_info_id, " +
				"       t.doc_type, " +
				"       t.distributor_code, " +
				"       t.t8_trutee_info_id, " +
				"       t.t8_meet_create_id, " +
				"       t.t8_print_temp_version_id, " +
				"       t.doc_version, " +
				"       t.doc_desc " +
				"from t8_prod_doc_info t " +
				"         left join t8_prod_info t1 on t.t8_prod_info_id = t1.id " +
				"where t1.prod_code = $S{prodCode} " +
				"  and t.doc_type = $S{docType}";
		if(StringUtils.isNotEmpty(t8ProdDocInfo.getDistributorCode())){
			sql += " and   t.distributor_code = $S{distributorCode} ";
		}
		if (StringUtils.isNotEmpty(t8ProdDocInfo.getT8TruteeInfoId())){
			sql += " and   t.t8_trutee_info_id = $S{t8TruteeInfoId} ";
		}
		return super.findRow(T8ProdDocInfo.class, sql, 0, t8ProdDocInfo);
	}

	public SqlResult<Map<String, Object>> findProdEscrowAgreement(Map<String, Object> parameters) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT*FROM ( "
	            + "SELECT IF (count(t.version)> 0,'1','0') AS is_have,temp.*FROM (select * from ( "
	            + "SELECT bank.id,prod.id t8_prod_info_id,prod.is_recycle_code,IFNULL(doc.doc_type,'10102') AS document_type," +
					"IFNULL(doc.doc_type,0) has_template,prod.prod_code,prod.prod_name,gd_trutee trutee_num,prod.prod_status,trutee.trutee_code," +
					"trutee.trutee_name,trutee.is_outside,trutee.id t8_trutee_info_id,gd_out_trutee,gd_trutee,trutee_custody_data,out_trutee_custody_data," +
					"out_trutee_custody_data trutee_date FROM t8_prod_info prod LEFT JOIN t8_prod_trutee_bank bank ON prod.id=bank.t8_prod_info_id " +
					"LEFT JOIN t8_prod_doc_info doc ON prod.id=doc.t8_prod_info_id AND doc.doc_type IN ('10002','20002','30002','40002','50002','60002','70002','10102') " +
					"AND doc.t8_trutee_info_id=bank.t8_trutee_info_id LEFT JOIN t8_trutee_info trutee ON bank.t8_trutee_info_id=trutee.id WHERE trutee.is_outside='0' " +
					"UNION ALL "
	            + "SELECT bank.id,prod.id t8_prod_info_id,prod.is_recycle_code,IFNULL(doc.doc_type,'10102') AS document_type,IFNULL(doc.doc_type,0) has_template,prod.prod_code,prod.prod_name," +
					"gd_out_trutee trutee_num,prod.prod_status,trutee.trutee_code,trutee.trutee_name,trutee.is_outside,trutee.id t8_trutee_info_id,gd_out_trutee,gd_trutee," +
					"trutee_custody_data,out_trutee_custody_data,out_trutee_custody_data trutee_date FROM t8_prod_info prod " +
					"LEFT JOIN t8_prod_trutee_bank bank ON prod.id=bank.t8_prod_info_id LEFT JOIN t8_prod_doc_info doc ON prod.id=doc.t8_prod_info_id " +
					"AND doc.doc_type IN ('10002','20002','30002','40002','50002','60002','70002','10102') AND doc.t8_trutee_info_id=bank.t8_out_trutee_info_id " +
					"LEFT JOIN t8_trutee_info trutee ON bank.t8_out_trutee_info_id=trutee.id WHERE trutee.is_outside='1') tt where 1=1 ") ;
		if (StringUtils.isNotBlank((String)parameters.get("t8ProdInfoId"))) {
			sql.append(" and tt.t8_prod_info_id=$S{t8ProdInfoId}");
		}
		if (StringUtils.isNotBlank((String)parameters.get("prodStatus"))) {
			sql.append("and tt.prod_status=$S{prodStatus}");
		}
		if (StringUtils.isNotBlank((String)parameters.get("prodCode"))) {
			sql.append("and tt.prod_code=$S{prodCode}");
		}
		if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
		            sql.append(" and tt.prod_name like '%" + parameters.get("prodName") + "%' ") ;
		        }
		sql.append(")temp LEFT JOIN t8_prod_document_version t ON t.prod_code=temp.prod_code AND temp.document_type=t.document_type LEFT JOIN sys_dict_item t1 " +
				"ON t1.dict='t8_temp_type' AND t1.itemkey=t.document_type LEFT JOIN t8_distributor_info t2 ON t2.distributor_code=t.distributor_code " +
				"LEFT JOIN t8_trutee_info t3 ON t3.id=t.t8_trutee_info_id WHERE 1=1 GROUP BY temp.prod_code) temp6 where 1=1 ");
		if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
			if("0".equals(parameters.get("isRecycleCode"))){
				sql.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null ) ");
			}else{
				sql.append(" and temp6.is_recycle_code ='"+parameters.get("isRecycleCode")+"' ");
			}
		}else{
			sql.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null ) ");
		}
		if (StringUtils.isNotBlank((String)parameters.get("isHave"))) {
			if("0".equals(parameters.get("isHave"))){
				sql.append("and is_have='0'");
			}else{
				sql.append("and is_have='1'");
			}
		}

		sql.append(" order by temp6.t8_prod_info_id + 0 desc");
		return SqlUtils.sqlPackage(sql.toString(), DataSourceProperty.PUB, parameters, this);
	}


	public SqlResult<Map<String, Object>> findProdPublishAgreement(Map<String, Object> parameters) throws Exception {
		StringBuilder sql = new StringBuilder(
	            "SELECT*FROM (SELECT IF (count(t.version)> 0,'1','0') AS is_have,temp.*FROM ( "
	                + "SELECT b.id t8_prod_info_id,b.is_recycle_code,ifnull(a.doc_type,'10106') document_type,a.doc_type,b.prod_code,b.prod_name,(CASE (\n"
	                + "SELECT count(1) FROM t8_prod_document_attachment d WHERE d.prod_code=b.prod_code AND d.attachment_type='12') " +
						"WHEN 0 THEN '0' ELSE '1' END) show_create_btn,(CASE (\n"
	                + "SELECT count(1) FROM t8_prod_document_version d WHERE d.prod_code=b.prod_code AND d.document_type " +
						"IN ('10006','20006','30006','40006','50006','60006','70006','10106')) WHEN 0 THEN '0' ELSE '1' END) show_upload_btn,b.prod_status," +
						"ifnull(a.id,'-1') t8_spare_column_one FROM t8_prod_info b LEFT JOIN t8_prod_doc_info a ON a.t8_prod_info_id=b.id " +
						"AND a.doc_type IN ('10006','20006','30006','40006','50006','60006','70006','10106') WHERE 1=1 ");
		if (StringUtils.isNotBlank((String)parameters.get("prodCode"))) {
			sql.append(" and b.prod_code = $S{prodCode}");
		}
		if (StringUtils.isNotBlank((String)parameters.get("prodName"))) {
		    sql.append(" and b.prod_name like '%" + parameters.get("prodName") + "%'");
		}
		if (StringUtils.isNotBlank((String)parameters.get("prodStatus"))) {
			sql.append(" and b.prod_status = $S{prodStatus}");
		}
		sql.append(") temp LEFT JOIN t8_prod_document_version t ON temp.document_type=t.document_type AND temp.prod_code=t.prod_code GROUP BY temp.prod_code) temp6 WHERE 1=1 ");
		if (StringUtils.isNotBlank((String)parameters.get("isHave"))) {
			if("0".equals(parameters.get("isHave"))){
				sql.append(" and temp6.is_have='0'");
			} else {
				sql.append(" and temp6.is_have='1'");
			}
		}
		if (parameters.get("isRecycleCode") != null && parameters.get("isRecycleCode") != "") {
			if("0".equals(parameters.get("isRecycleCode"))){
				sql.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null ) ");
			}else{
				sql.append(" and temp6.is_recycle_code ='"+parameters.get("isRecycleCode")+"' ");
			}
		}else{
			sql.append(" and (temp6.is_recycle_code != '1' or temp6.is_recycle_code is null ) ");
		}
		return SqlUtils.sqlPackage(sql.toString(), DataSourceProperty.PUB, parameters, this);
	}


	public void deleteT8ProdDocInfo(String params){
		try {
			super.update("DELETE FROM t8_prod_doc_info WHERE  t8_prod_info_id=$S{params} ",
				params);
		}catch (Exception e) {

		}
	}

	public void updateT8ProdDocInfo(T8ProdDocInfo params){
		try {
			String userid = Tools.obj2Str(SysUtil.getSysUserParamValue("sys_user_userid"));
			Date now = new Date();
			String uptDate = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
			String uptTime = DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT);
			params.setUpdDate(uptDate);
			params.setUpdTime(uptTime);
			params.setUpdUser(userid);
			super.update("INSERT INTO t8_prod_doc_info(id,t8_prod_info_id,doc_type,distributor_code,t8_trutee_info_id,t8_meet_create_id,t8_print_temp_version_id,doc_version,doc_desc,correlation_time,crt_date,crt_time,crt_user,upd_date,upd_time,upd_user,is_currency_template) " +
						" VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{docType},$S{distributorCode},$S{t8TruteeInfoId},$S{t8MeetCreateId},$S{t8PrintTempVersionId},$S{docVersion},$S{docDesc},$S{correlationTime},$S{crtDate},$S{crtTime},$S{crtUser},$S{updDate},$S{updTime},$S{updUser},$S{isCurrencyTemplate})",
				params);
		}catch (Exception e) {

		}
	}






















	public SqlResult<T8ProdDocInfo> findT8ProdDocInfoHiss(SqlParam<T8ProdDocInfo> params) throws Exception {
		return super.findRows("SELECT id,t8_prod_info_id,doc_type,distributor_code,t8_trutee_info_id,t8_meet_create_id,t8_print_temp_version_id,doc_version,doc_desc,crt_date,crt_time,crt_user FROM t8_prod_doc_info_his", params);
	}

	public void addT8ProdDocInfoHis(T8ProdDocInfo params) {
		try {
			super.update("INSERT INTO t8_prod_doc_info_his(id,t8_prod_info_id,doc_type,distributor_code,t8_trutee_info_id,t8_meet_create_id,t8_print_temp_version_id,doc_version,doc_desc,crt_date,crt_time,crt_user) VALUES($AUTOIDS{id},$S{t8ProdInfoId},$S{docType},$S{distributorCode},$S{t8TruteeInfoId},$S{t8MeetCreateId},$S{t8PrintTempVersionId},$S{docVersion},$S{docDesc},$S{crtDate},$S{crtTime},$S{crtUser})",
					params);
		}catch ( Exception e){

	    }

	}

	public List<SqlRow> findDocumentByCodeType(String prodinfoId,String docType)throws Exception  {
		String sql =" SELECT file_path filePath from t8_static_temp_version v where v.id = ((SELECT d.t8_print_temp_version_id from t8_prod_doc_info d where d.t8_prod_info_id = '"+prodinfoId+"' and d.doc_type = '"+docType+"' ORDER BY d.doc_version desc))";
		
		return super.findRows(sql);
	}
	
	public List<SqlRow> findDocByCodeType(String prodinfoId)throws Exception  {
		String sql =" SELECT temp_name,id from t8_print_temp_version where id =(SELECT t8_print_temp_version_id from t8_prod_doc_info where t8_prod_info_id ="+prodinfoId+" and doc_type in ('10011','20011','30011','40011','50011','60011','70011','10111'))" ;
		
		return super.findRows(sql);
	}

	//根据销售商代码查询数据
	public List<SqlRow> findByDistributorCode(String distributorCode) throws Exception {

		Map<String, Object> params = new HashMap<>(1);
		params.put("distributorCode", distributorCode);
		return super.findRows("select t.id,t.distributor_code from t8_prod_doc_info t where t.distributor_code=$S{distributorCode}", params);
	}

	public Integer updateDistributorCode(T8ProdDocInfo t8ProdDocInfo) throws Exception {

		String sql = " UPDATE t8_prod_doc_info SET distributor_code = $S{distributorCode} WHERE id = $S{id}";
		return super.update(sql, t8ProdDocInfo).getEffect();
	}
	
	
	public SqlRow getRiskNum(Map<String,Object> param) throws Exception {
		
		return super.findRow("SELECT risk_num,temp_name from t8_print_temp_version where id =(SELECT max(d.t8_print_temp_version_id)  from t8_prod_info p LEFT JOIN t8_prod_doc_info d on p.id=d.t8_prod_info_id where p.prod_code =$S{prodCode} and d.doc_type=$S{docType}" + 
				")", param);
	}
}
