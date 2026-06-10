package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.DateFormatEnum;
import com.kayak.core.util.DateUtil;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeChannel;
import com.kayak.utils.DateHelper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureNoticeChannelDao extends ComnDao {

	private static String userId;
	private static String userName;

	public static void setMessage() {
		userId = (String)SysUtil.getSysUserParamValue("sys_user_userid");
		userName = (String)SysUtil.getSysUserParamValue("sys_user_username");
	}

	public SqlResult<DisclosureNoticeChannel> findDisclosureNoticeChannels(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.findRows("SELECT id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name FROM idb_disclosure_notice_channel", DataSourceProperty.IDB, params);
	}

	/**
	 * 查询最新信息公告版本号信息
	 * @param disclosureNoticeId
	 * @return
	 * @throws Exception
	 */
	public SqlRow findDisclosureNoticeVersions(String disclosureNoticeId) throws Exception {
		String sql = "SELECT file_path,file_name,version,doc_type " +
				"FROM idb_disclosure_notice_version " +
				"WHERE t8_disclosure_notice_id='"+disclosureNoticeId+"' " +
				"AND version=(SELECT MAX(version) version FROM idb_disclosure_notice_version WHERE t8_disclosure_notice_id='"+disclosureNoticeId+"')";
		return super.findRow(sql, DataSourceProperty.IDB, null);
	}

	public SqlRow findDisclosureNotice(String params) throws Exception {
		String sql = "SELECT t1.*,t3.doc_name   FROM idb_disclosure_notice  t1 LEFT JOIN idb_disclosure_prod_rule t2 ON t1.t8_disclosure_rule_id=t2.id  LEFT JOIN idb_disclosure_mod_version t3 ON t2.t8_disclosure_version_id=t3.id WHERE t1.id='"+params+"'";
		return super.findRow(sql, DataSourceProperty.IDB, null);
	}

	/**
	 * 查询公告详情中需要披露的渠道信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlResult<DisclosureNoticeChannel> findDisclosureNoticeChannelsInfo(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.findRows("SELECT tdnc.id, tdnc.disclosure_notice_id, tdnc.disclosure_notice_channel_id, tdc.channel_name, tdc.is_docking, " +
				"tdc.docking_way, tdc.host_ip, tdc.protocol, tdc.port_code, tdc.user_name, tdc.password, tdc.file_path, tdc.status, tdc.remark, " +
				"tdnc.channel_public_date, tdnc.notice_channel_public_status, tdnc.create_date, tdnc.create_user_id ,tdnc.update_date,tdnc.update_time " +
				"FROM idb_disclosure_notice_channel/*公告详情渠道信息*/ tdnc " +
				"JOIN idb_disclosure_channel/*渠道管理*/ tdc ON tdc.id = tdnc.disclosure_notice_channel_id AND tdc.status = '1' " +
				"WHERE 1=1  and tdnc.disclosure_notice_id = $S{disclosureNoticeId} AND tdnc.disclosure_notice_version_id = $S{noticeVersionId}",
//						"WHERE 1=1 AND tdc.is_docking = '1'/*是否对接*/ and tdnc.disclosure_notice_id = $S{disclosureNoticeId} AND tdnc.disclosure_notice_version_id = $S{noticeVersionId}",
				DataSourceProperty.IDB, params);
	}

	public SqlResult<DisclosureNoticeChannel> findDisclosureManualNoticeChannelsInfo(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.findRows("SELECT tdnc.id, tdnc.disclosure_notice_id, tdnc.disclosure_notice_channel_id, tdc.channel_name, tdc.is_docking, " +
						"tdc.docking_way, tdc.host_ip, tdc.protocol, tdc.port_code, tdc.user_name, tdc.password, tdc.file_path, tdc.status, tdc.remark, " +
						"tdnc.channel_public_date, tdnc.notice_channel_public_status, tdnc.create_date, tdnc.create_user_id ,tdnc.update_date,tdnc.update_time " +
						"FROM idb_disclosure_notice_channel/*公告详情渠道信息*/ tdnc " +
						"JOIN idb_disclosure_channel/*渠道管理*/ tdc ON tdc.id = tdnc.disclosure_notice_channel_id AND tdc.status = '1' " +
						"WHERE 1=1  and tdnc.disclosure_notice_id = $S{disclosureNoticeId} ",
//						"WHERE 1=1 AND tdc.is_docking = '1'/*是否对接*/ and tdnc.disclosure_notice_id = $S{disclosureNoticeId} AND tdnc.disclosure_notice_version_id = $S{noticeVersionId}",
				DataSourceProperty.IDB, params);
	}

	public List<SqlRow> findDisclosureNoticeChannelsBy(String disclosureNoticeId) throws Exception {
		return super.findRows("SELECT tdnc.id,tdnc.disclosure_notice_id,tdci.channel_code,tdci.channel_name,tdci.emails," +
				"tdnc.channel_public_date,sdi.itemval notice_channel_public_status,tdn.stage FROM idb_disclosure_notice_channel tdnc " +
				"LEFT JOIN idb_disclosure_channel_info tdci ON tdnc.disclosure_notice_channel_id=tdci.id " +
				"left join sys_dict_item sdi on tdnc.notice_channel_public_status=sdi.itemkey and sdi.dict='xp_release_status' " +
				"right join idb_disclosure_notice tdn on tdn.id = tdnc.disclosure_notice_id  WHERE disclosure_notice_id = '" + disclosureNoticeId + "' ", DataSourceProperty.IDB);
	}

	public List<SqlRow> findDisclosureNoticeChannelsByDate() throws Exception {
		String date = DateHelper.getCurrentDate();
		return super.findRows("SELECT t1.*,t2.disclosure_type,t3.doc_name   FROM idb_disclosure_notice  t1 LEFT JOIN idb_disclosure_prod_rule t2 ON t1.t8_disclosure_rule_id=t2.id  LEFT JOIN idb_disclosure_mod_version t3 ON t2.t8_disclosure_version_id=t3.id WHERE t1.plan_fb_date='"+date+"' AND t1.stage = '4' and t1.current_stage_status in('0','3') and t1.publish_status!='2'", DataSourceProperty.IDB);
	}

	public List<SqlRow> findDisclosureNoticeChannelsEmails(String noticeId) throws Exception {
		return super.findRows("SELECT tdci.id,tdnc.id channel_id,tdnc.disclosure_notice_id,tdci.channel_code,tdci.channel_name,tdci.emails,tdnc.channel_public_date FROM idb_disclosure_notice_channel tdnc LEFT JOIN idb_disclosure_channel_info tdci ON tdnc.disclosure_notice_channel_id=tdci.id WHERE tdnc.disclosure_notice_id='"+noticeId+"' and tdnc.notice_channel_public_status !='2' ", DataSourceProperty.IDB);
	}

	public List<SqlRow> findAllDisclosureNoticeChannels() throws Exception {
		return super.findRows(" SELECT\n" +
				"\ttdci.id,\n" +
				"\ttdnc.id channel_id,\n" +
				"\ttdci.channel_code,\n" +
				"\ttdci.channel_name,\n" +
				"\ttdci.emails,\n" +
				"\ttdnc.channel_public_date,\n" +
				"\tgroup_concat( tdnc.disclosure_notice_id ) disclosure_notice_id,\n" +
				"\tdisclosure_notice_channel_id \n" +
				"FROM\n" +
				"\tidb_disclosure_notice_channel tdnc\n" +
				"\tLEFT JOIN idb_disclosure_channel_info tdci ON tdnc.disclosure_notice_channel_id = tdci.id \n" +
				"WHERE\n" +
				"\ttdnc.notice_channel_public_status != '2' \n" +
				"GROUP BY\n" +
				"\tdisclosure_notice_channel_id ", DataSourceProperty.IDB);
	}

	/**
	 * 根据公告id查询公告相关信息
	 * @param notice_version_id
	 * @return
	 * @throws Exception
	 */
	public SqlRow findDisclosureNoticeById(String notice_version_id) throws Exception {
		String queryStr = "SELECT dn.id, dm.disclosure_type, dm.disclosure_son_type, dm.mod_name, dmv.doc_name, dn.prod_base_date, dn.report_end_date, " +
				          "       dn.notice_title, dn.prod_code,(SELECT itemval FROM sys_dict_item WHERE dict = 'xp_report_type' AND itemkey = dn.report_type) report_type " +
				          "  FROM idb_disclosure_notice_version dnv" +
				          "  JOIN idb_disclosure_notice dn ON dn.id = dnv.t8_disclosure_notice_id " +
				          "  JOIN idb_disclosure_mod_version dmv ON dmv.id = dnv.disclosure_mod_version_id " +
				          "  JOIN idb_disclosure_mod dm ON dm.id = dmv. disclosure_mod_id  " +
				          " WHERE dnv.id = " + notice_version_id;
		return super.findRow(queryStr, DataSourceProperty.IDB, null);
	}
	
	public List<SqlRow> findNoticeById(String id) throws Exception {
		return super.findRows(" SELECT t1.id,t1.prod_base_date,t1.prod_code,t1.t8_prod_info_id,t1.notice_title,t1.report_end_date," +
				" (select itemval from sys_dict_item where dict = 'xp_report_type' and itemkey = t1.report_type  ) report_type," +
				" t2.disclosure_type,t3.doc_name   FROM idb_disclosure_notice t1 " +
				" LEFT JOIN idb_disclosure_prod_rule t2 ON t1.t8_disclosure_rule_id=t2.id " +
				" LEFT JOIN idb_disclosure_mod_version t3 ON t2.t8_disclosure_version_id=t3.id " +
				" WHERE t1.id = '"+id+"'", DataSourceProperty.IDB);
	}

	public UpdateResult addDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.update("INSERT INTO idb_disclosure_notice_channel(id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name,disclosure_notice_version_id) VALUES($AUTOIDS{id},$S{disclosureNoticeId},$S{disclosureNoticeChannelId},$S{noticeChannelPublicStatus},$S{channelPublicDate},$S{createDate},$S{createTime},$S{updateDate},$S{updateTime},$S{createUserId},$S{updateUserId},$S{createUserName},$S{noticeVersionId})",
				DataSourceProperty.IDB, params.getModel());
	}

	public void addDisclosureNoticeChannel(DisclosureNoticeChannel disclosureNoticeChannel) throws Exception {
		super.update("INSERT INTO idb_disclosure_notice_channel(id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name) VALUES($AUTOIDS{id},$S{disclosureNoticeId},$S{disclosureNoticeChannelId},$S{noticeChannelPublicStatus},$S{channelPublicDate},$S{createDate},$S{createTime},$S{updateDate},$S{updateTime},$S{createUserId},$S{updateUserId},$S{createUserName})",
				DataSourceProperty.IDB, disclosureNoticeChannel);
	}

	public UpdateResult updateDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.update("UPDATE idb_disclosure_notice_channel SET disclosure_notice_id=$S{disclosureNoticeId} ,disclosure_notice_channel_id=$S{disclosureNoticeChannelId} ,notice_channel_public_status=$S{noticeChannelPublicStatus} ,channel_public_date=$S{channelPublicDate} ,create_date=$S{createDate} ,create_time=$S{createTime} ,update_date=$S{updateDate} ,update_time=$S{updateTime} ,create_user_id=$S{createUserId} ,update_user_id=$S{updateUserId} ,create_user_name=$S{createUserName}  WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}

	public UpdateResult deleteDisclosureNoticeChannel(SqlParam<DisclosureNoticeChannel> params) throws Exception {
		return super.update("DELETE FROM idb_disclosure_notice_channel WHERE  id=$S{id} ",
				DataSourceProperty.IDB, params.getModel());
	}

	public boolean cheackHCommonId(String disclosureNoticeId, String disclosureNoticeChannelId) throws Exception {
		String sql="SELECT count(id) as id FROM idb_disclosure_notice_channel WHERE disclosure_notice_id='"+disclosureNoticeId+"' AND disclosure_notice_channel_id='"+disclosureNoticeChannelId+"'";
		SqlRow row = super.findRow(sql, DataSourceProperty.IDB,null);
		if(Integer.parseInt(row.get("id").toString())>0){
			return true;
		}
		return false;
	}

	public int updateNoticeStatus(String noticeId) throws Exception {
		String updUserId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
        String updUserName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		return super.update("UPDATE idb_disclosure_notice SET current_stage_status='13',publish_status='2' ,lcd_status = '1',upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+updUserId+"', upd_user_name = '"+updUserName+"'  WHERE  id=$S{noticeId} ",
				DataSourceProperty.IDB, noticeId).getEffect();
	}
	public int updateNoticeStatus1(String noticeId) throws Exception {
		String date = DateUtil.getNowDate();
		String time = DateUtil.getNowTime();
		return super.update("UPDATE idb_disclosure_notice SET current_stage_status='13',publish_status='2' ,upd_date = '"+date+"', upd_time = '"+time+"', upd_user_id = '"+userId+"', upd_user_name = '"+userName+"'  WHERE  id=$S{noticeId} ",
				DataSourceProperty.IDB, noticeId).getEffect();
	}

	public int updateNoticeStatusForFail(String noticeId) throws Exception {
		return super.update("UPDATE idb_disclosure_notice SET publish_status='3' WHERE  id=$S{noticeId} ",
				DataSourceProperty.IDB, noticeId).getEffect();
	}

	/**
	 * 功能：根据公告渠道id修改渠道发布信息（发布状态、发布时间）
	 * 作者：rennannan
	 * 日期：20211117
	 */
	public void updateChannelStatusById(DisclosureNoticeChannel channel) throws Exception {
		super.update("update idb_disclosure_notice_channel " +
				" set notice_channel_public_status=$S{noticeChannelPublicStatus}," +
				"  channel_public_date=$S{channelPublicDate}," +
				"  version=$S{version}" +
				" where id=$S{id}", DataSourceProperty.IDB, channel);
	}

	public UpdateResult updateChannelStatus(String params,String params2,String version) throws Exception {
		Date now = new Date();
		String userName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String userId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updDate = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
		String updTime = DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT);
			return super.update("UPDATE idb_disclosure_notice_channel SET notice_channel_public_status='2' ,version = '"+version+"',channel_public_date='" + updDate + "' ,update_date='" + updDate + "' ,update_time='" + updTime + "' ,update_user_id='" + userId + "' WHERE   disclosure_notice_id=" + params2 + " and disclosure_notice_channel_id='"+params+"';", DataSourceProperty.IDB);
	}

	public UpdateResult updateChannelStatusForFail(String params,String params2) throws Exception {
		Date now = new Date();
		String userName = (String) SysUtil.getSysUserParamValue("sys_user_username");
		String userId = (String) SysUtil.getSysUserParamValue("sys_user_userid");
		String updDate = DateUtil.formatDate(now, DateFormatEnum.DATE_FORMAT);
		String updTime = DateUtil.formatDate(now, DateFormatEnum.TIME_FORMAT);
		return super.update("UPDATE idb_disclosure_notice_channel SET notice_channel_public_status='3' ,channel_public_date='"+updDate+"' ,update_date='"+updDate+"' ,update_time='"+updTime+"' ,update_user_id='"+userId+"' WHERE  disclosure_notice_id=" + params2 + " and disclosure_notice_channel_id='"+params+"';",
				DataSourceProperty.IDB, params);
	}

	public List<SqlRow> findReportType(String params) throws Exception {
		String sql = "select * from sys_dict_item where dict='xp_report_type'";
		return super.findRows(sql, DataSourceProperty.IDB);
	}

	public List<SqlRow> findDisclosureNoticeChannelsById(String id) throws Exception {
		return super.findRows("SELECT id,disclosure_notice_id,disclosure_notice_channel_id,notice_channel_public_status,channel_public_date,create_date,create_time,update_date,update_time,create_user_id,update_user_id,create_user_name FROM idb_disclosure_notice_channel where disclosure_notice_id='"+id+"' and notice_channel_public_status!='2'", DataSourceProperty.IDB, id);
	}

	//查询这笔公告是否需要补录
	public SqlRow getIfClearing(Map<String,Object> params) throws Exception {
		return super.findRow("SELECT\n" +
				" prodRule.prod_code,\n" +
				" prodRule.disclosure_type,\n" +
				" prodRule.disclosure_son_type,\n" +
				" (CASE IFNULL( dmct.count, '0' ) WHEN '0' THEN '0' ELSE '1'  END) if_clearing\n" +
				"FROM\n" +
				" idb_disclosure_prod_rule prodRule\n" +
				"LEFT JOIN idb_disclosure_mod_version tdmv ON prodRule.disclosure_mod_version_id = tdmv.id\n" +
				"LEFT JOIN ( SELECT DISTINCT disclosure_mod_version_id, COUNT(*) `count` FROM idb_disclosure_mod_column WHERE is_sysvalue = '2' GROUP BY           disclosure_mod_version_id ) dmct \n" +
				"ON tdmv.id = dmct.disclosure_mod_version_id \n" +
				"WHERE prodRule.prod_code = $S{prodCode} \n" +
				"and prodRule.disclosure_type = $S{disclosureType} \n" +
				"and prodRule.disclosure_son_type = $S{disclosureSonType}", DataSourceProperty.IDB, params);

	}
}
