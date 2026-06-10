package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.Tools;
import com.kayak.pms.disclosureControl.model.DisclosureNotice;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeVersion;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DisclosureNoticeVersionDao extends ComnDao {

	public SqlResult<DisclosureNoticeVersion> findDisclosureNoticeVersions(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		String sql = "SELECT tdnv.*,tdn.prod_base_date,tdpr.disclosure_son_type FROM idb_disclosure_notice_version tdnv LEFT JOIN idb_disclosure_notice tdn ON tdnv.t8_disclosure_notice_id=tdn.id LEFT JOIN idb_disclosure_prod_rule tdpr ON tdpr.id=tdn.t8_disclosure_rule_id WHERE 1=1";
		if(Tools.isNotEmpty(params.getModel().getT8DisclosureNoticeId())){
			sql = sql + " and tdn.id=$S{t8DisclosureNoticeId}";
		}
		if(Tools.isNotEmpty(params.getModel().getProdCode())){
			sql = sql + " and tdn.prod_code=$S{prodCode}";
		}
		return super.findRows(sql, params);
	}

	public SqlResult<DisclosureNoticeVersion> findDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		String sql = "SELECT \n" +
				"  tdn.disclosure_type,\n" +
				"  tdn.disclosure_son_type,\n" +
				"  tdm.mod_name,\n" +
				"  tdmv.version AS mod_version,\n" +
				"  tdnv.file_name,\n" +
				"  tdnv.file_path,\n" +
				"  tdnv.notice_version,\n" +
				"  tdn.prod_code,\n" +
				"  pb.prod_nm prod_name,\n" +
				"  tdnv.crt_date,\n" +
				"  tdnv.crt_user_name,\n" +
				"  tdnv.id,\n" +
				"  tdnv.t8_disclosure_notice_id,\n" +
				"  tdnv.crt_user_id,\n" +
				"  tdnv.crt_time,\n" +
				"  tdnv.upd_date,\n" +
				"  tdnv.upd_time,\n" +
				"  tdnv.upd_user_id,\n" +
				"  tdnv.upd_user_name,\n" +
				"  tdnv.remark,\n" +
				"  SUBSTR(tdmv.doc_name,LOCATE('.',tdmv.doc_name)) suffix,pb.dms_trst_org_nm trustee_name," +
				"  tdn.notice_title \n" +
				"FROM\n" +
				"  idb_disclosure_notice_version tdnv \n" +
				"  LEFT JOIN idb_disclosure_notice tdn \n" +
				"    ON tdnv.t8_disclosure_notice_id = tdn.id \n" +
				"  LEFT JOIN idb_disclosure_mod_version tdmv \n" +
				"    ON tdmv.id = tdnv.disclosure_mod_version_id \n" +
				"  LEFT JOIN idb_disclosure_mod tdm \n" +
				"    ON tdm.id = tdmv.disclosure_mod_id \n" +
				"  LEFT JOIN APP_PRD_BAS_INF pb \n" +
				"    ON pb.prod_cd = tdn.prod_code \n" +
				"WHERE 1 = 1 \n" +
				"  AND tdn.effect_status = '1' ";
		if (StringUtils.isNotBlank(params.getModel().getDisclosureType())) {
		    sql = sql + " and tdn.disclosure_type = " + params.getModel().getDisclosureType() + " ";
		}
		if(Tools.isNotEmpty(params.getModel().getDisclosureSonType())){
			sql = sql + " and tdn.disclosure_son_type = " + params.getModel().getDisclosureSonType() + " ";
		}
		if (StringUtils.isNotEmpty(params.getModel().getProdCode())) {
			sql = sql + " and tdn.prod_code like '%" + params.getModel().getProdCode() + "%'";
		}
		if(Tools.isNotEmpty(params.getModel().getProdName())){
			sql = sql + " and tdn.prod_name like '%" + params.getModel().getProdName() + "%'";
		}
		if(Tools.isNotEmpty(params.getModel().getCrtDate())){
			sql = sql + " and tdnv.crt_date = " + params.getModel().getCrtDate();
		}
		if (StringUtils.isNotBlank(params.getModel().getTrusteeName())) {
			sql = sql + " and pb.dms_trst_org_nm = '" + params.getModel().getTrusteeName() + "'";
		}
		sql= sql+" ORDER BY prod_code, tdnv.id desc ";
		return super.findRows(sql, DataSourceProperty.IDB, params);
	}


	public String addT8DisclosureNoticeVersion(DisclosureNoticeVersion params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_version\n" +
						"(id,prod_code,t8_disclosure_notice_id,disclosure_mod_version_id,notice_version,mod_version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,file_path,file_name,remark,crt_path,is_notice_pub) VALUES\n" +
						"($AUTOIDS{id},$S{prodCode},$S{t8DisclosureNoticeId},$S{disclosureModVersionId},$S{noticeVersion},$S{modVersion},$S{docType},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{filePath},$S{fileName},$S{remark},$S{crtPath},$S{isNoticePub})",
				DataSourceProperty.IDB,params).getAutoId();
	}

	public UpdateResult updT8DisclosureNoticeVersion(DisclosureNoticeVersion params) throws Exception {
		return super.update("update idb_disclosure_notice_version set file_path=$S{filePath},crt_path=$S{crtPath} where id = $S{id}",
				DataSourceProperty.IDB,params);
	}
	public UpdateResult updNoticeVersionStatus(DisclosureNoticeVersion params) throws Exception {
		return super.update("update idb_disclosure_notice_version set is_notice_pub=$S{isNoticePub} where id = $S{id}",
				DataSourceProperty.IDB,params);
	}

	public UpdateResult addDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_version(id,prod_code,t8_disclosure_notice_id,version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark) VALUES($AUTOIDS{id},,$S{prodCode},$S{t8DisclosureNoticeId},$S{version},$S{docType},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{filePath},$S{fileName},$S{remark})",
				DataSourceProperty.IDB, params.getModel());
	}

	public UpdateResult addDisclosureNoticeVersionByHand(DisclosureNoticeVersion params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_version(id,prod_code,t8_disclosure_notice_id,version,doc_type,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,file_path,file_name,remark) VALUES($AUTOIDS{id},$S{prodCode},$S{t8DisclosureNoticeId},$S{version},$S{docType},$S{crtDate},$S{crtTime},$S{crtUserId},$S{crtUserName},$S{updDate},$S{updTime},$S{updUserId},$S{updUserName},$S{filePath},$S{fileName},$S{remark})",
				DataSourceProperty.IDB, params);
	}

	public UpdateResult updateDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_version SET prod_code=$S{prodCode} ,t8_disclosure_notice_id=$S{t8DisclosureNoticeId} ,version=$S{version} ,doc_type=$S{docType} ,crt_date=$S{crtDate} ,crt_time=$S{crtTime} ,crt_user_id=$S{crtUserId} ,crt_user_name=$S{crtUserName} ,upd_date=$S{updDate} ,upd_time=$S{updTime} ,upd_user_id=$S{updUserId} ,upd_user_name=$S{updUserName} ,file_path=$S{filePath} ,file_name=$S{fileName} ,remark=$S{remark}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}
	
	public UpdateResult deleteDisclosureNoticeVersion(SqlParam<DisclosureNoticeVersion> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice_version WHERE  id=$S{id} ", DataSourceProperty.IDB,
				params.getModel());
	}

	public String findMaxVersions(DisclosureNoticeVersion params) throws Exception {
		List<SqlRow> rows = super.findRows("select IFNULL(max(version),'') version from idb_disclosure_notice_version where t8_disclosure_notice_id=$S{t8DisclosureNoticeId}", DataSourceProperty.IDB, params);
		String version = "";
		if (rows != null || rows.size() >0) {
			 String v = rows.get(0).getString("version");
			 if(v!=null && v!=""){
			 	String[] data = v.split("v");

			 }
		}else{
			version="v1.0";
		}
		return version;
	}

	public List<SqlRow> findVersionsInfo(DisclosureNoticeVersion params) throws Exception {
		List<SqlRow> rows =  super.findRows("SELECT tdn.prod_code,tdmc.file_name,tdmc.upload_path FROM idb_disclosure_notice tdn LEFT JOIN idb_disclosure_prod_rule tdpr ON tdn.t8_disclosure_rule_id=tdpr.id LEFT JOIN idb_disclosure_mod_version tdmv ON tdmv.id=tdpr.t8_disclosure_version_id LEFT JOIN idb_disclosure_mod_column tdmc ON tdmc.t8_disclosure_version_id=tdmv.id WHERE tdn.id='9' LIMIT 1", DataSourceProperty.IDB, params);
		if(rows!=null && rows.size()>0){
			return rows;
		}else{
			return null;
		}
	}

		public DisclosureNoticeVersion findDisclosureNewNotice(String noticeId) throws Exception {
		
		return super.findRow(DisclosureNoticeVersion.class, "SELECT id,prod_code,t8_disclosure_notice_id,version,doc_type,file_path,file_name,crt_path from idb_disclosure_notice_version where t8_disclosure_notice_id ='"+noticeId+"' ORDER BY id+0 desc  LIMIT 1", DataSourceProperty.IDB,null);
	}

		public int updateNoticeVersion(DisclosureNoticeVersion noticeVersion) throws Exception {
			//SqlRow sqlRow = super.findRow("SELECT id from idb_disclosure_notice_version where t8_disclosure_notice_id = '"+noticeVersion.getT8DisclosureNoticeId()+"' ORDER BY id desc  LIMIT 1", null);
			return super.update("update idb_disclosure_notice_version set crt_path= $S{crtPath} where id =$S{id} ", DataSourceProperty.IDB, noticeVersion).getEffect();
		}
		
		
		public DisclosureNoticeVersion queryNoticeVersionByNoticeId(DisclosureNotice notice)  throws Exception {
			
			return super.findRow(DisclosureNoticeVersion.class, "select id,prod_code,version,t8_disclosure_notice_id,crt_path from idb_disclosure_notice_version where t8_disclosure_notice_id = $S{id} ORDER BY id+0 desc limit 1;", DataSourceProperty.IDB, notice);
		}

		public int updateEmailStatus(DisclosureNotice notice) throws Exception {
			SqlRow sqlRow = super.findRow("SELECT id from idb_disclosure_notice_version where t8_disclosure_notice_id = '"+notice.getId()+"' ORDER BY id+0 desc  LIMIT 1", null);
			if(sqlRow !=null) {
				return super.update("update idb_disclosure_notice_version set is_send_email = '"+notice.getIsSendEmail()+"' where id ='"+sqlRow.getString("id")+"' ", DataSourceProperty.IDB, null).getEffect();
			}
			return 0;
		}
		
		
		public SqlRow getNewVersion(String noticeId) throws Exception {
			SqlRow sqlRow = super.findRow("SELECT id,crt_path,version from idb_disclosure_notice_version where t8_disclosure_notice_id = '"+noticeId+"' ORDER BY id+0 desc  LIMIT 1", DataSourceProperty.IDB, null);
			
			return sqlRow;
		}

		public SqlResult<DisclosureNoticeVersion> findManualVersionsInfoById(SqlParam<DisclosureNoticeVersion> params) throws Exception {
			return super.findRows("SELECT tdnv.file_name,tdnv.file_path,'0' file_type FROM idb_disclosure_notice_version tdnv  WHERE tdnv.t8_disclosure_notice_id='"+params.getModel().getT8DisclosureNoticeId()+"' LIMIT 1", DataSourceProperty.IDB, params);

		}

}
