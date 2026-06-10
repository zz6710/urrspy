package com.kayak.pms.disclosureControl.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import com.kayak.pms.disclosureControl.model.DisclosureNoticeValue;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DisclosureNoticeDocDao extends ComnDao {

	/**
	 * 根据公告版本号获取信批文档需要替换的字段
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<DisclosureNoticeValue> getNoticeVersionValue(Map<String, Object> params) throws Exception {
		String sqlStr = "select * from idb_disclosure_notice_value where t8_disclosure_version_id = $S{disclosureNoticeVersionId}";

		return super.findRows(DisclosureNoticeValue.class, sqlStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 根据公告id获取公告标题(上传文件名称)
	 * @param disclosureNoticeId
	 * @return
	 */
	public String getNoticeNameByNoticeId(String disclosureNoticeId) {
		String notice_name = "";
		SqlRow noticeRes = null;

		try {
			//获取信批公告标题名称
			noticeRes = super.findRow(" SELECT notice_title FROM idb_disclosure_notice WHERE id = " + disclosureNoticeId, DataSourceProperty.IDB, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (noticeRes != null){
			notice_name = noticeRes.getString("notice_title");
		}
		return notice_name;
	}

	/**
	 * 根据公告id获取其最大版本id
	 * @param disclosureNoticeId
	 * @return
	 * @throws Exception
	 */
	public String getNoticeLatestVersionId (String disclosureNoticeId) throws Exception {
		String verSql = "SELECT MAX(CONVERT(nv.id,SIGNED)) AS maxVerId  " +
				"  FROM idb_disclosure_notice dn " +
				"  LEFT JOIN idb_disclosure_notice_version nv ON nv.t8_disclosure_notice_id = dn.id " +
				" WHERE dn.id = " + disclosureNoticeId ;
		SqlRow maxVersionRes = super.findRow(verSql ,DataSourceProperty.IDB, null);
		if(maxVersionRes != null){
			return maxVersionRes.getString("maxVerId");
		} else {
			throw new Exception("数据异常,该公告不存在版本号信息！");
		}
	}

	/**
	 * 查询信批公告模板相关信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getNoticeModInfo (Map<String, Object> params) throws Exception {
		String noticeModStr = "SELECT dn.disclosure_type, dn.disclosure_son_type, dm.mod_name, dmv.doc_name, dn.report_end_date,dnv.disclosure_mod_version_id,dnv.notice_version" +
				"  FROM idb_disclosure_notice_version dnv " +
				"  JOIN idb_disclosure_notice dn ON dn.id = dnv.t8_disclosure_notice_id  " +
				"  JOIN idb_disclosure_mod_version dmv ON dmv.id = dnv.disclosure_mod_version_id " +
				"  JOIN idb_disclosure_mod dm ON dm.id = dmv.disclosure_mod_id " +
				" WHERE dnv.id = $S{disclosureNoticeVersionId} AND dnv.t8_disclosure_notice_id = $S{disclosureNoticeId}";
		SqlRow modResult = super.findRow(noticeModStr, DataSourceProperty.IDB, params);
		if (modResult != null) {
			params.put("disclosure_type", modResult.getString("disclosure_type"));
			params.put("disclosure_son_type", modResult.getString("disclosure_son_type"));
			params.put("mod_doc_name", modResult.getString("doc_name"));//模板文件名称.docx
			params.put("mod_name", modResult.getString("mod_name"));//模板名称
			params.put("versionNum", modResult.getString("notice_version"));//模板版本号
			params.put("disclosure_mod_version_id", modResult.getString("disclosure_mod_version_id"));//模板版本id
		}
		return params;
	}

	/**
	 * 根据渠道id及版本id确定需要发布的文件格式和文件发布名称模板
	 * @param notice_version_id
	 * @return
	 */
	public SqlRow getNoticeFilePubType (String notice_version_id, String channel_id) throws Exception{
		String sqlStr = "SELECT dcr.upload_file_type, dcr.upload_file_name_type  " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  JOIN idb_disclosure_notice dn ON dnv.t8_disclosure_notice_id = dn.id  " +
				"  LEFT JOIN idb_disclosure_channel_rule dcr ON dcr.disclosure_type = dn.disclosure_type " +
				" AND dcr.disclosure_son_type = dn.disclosure_son_type " +
				" AND (dcr.PROD_CLC_MTH=dn.PROD_CLC_MTH OR dcr.PROD_CLC_MTH IS NULL OR dcr.PROD_CLC_MTH ='') " +
				" AND (dcr.PROD_FORM =dn.PROD_FORM OR dcr.PROD_FORM IS NULL OR dcr.PROD_FORM ='') " +
				" AND (dcr.PROD_OBJ=dn.PROD_OBJ OR dcr.PROD_OBJ IS NULL OR dcr.PROD_OBJ ='') " +
				" AND (dcr.PROD_SER_CD=dn.PROD_SER_CD OR dcr.PROD_SER_CD IS NULL OR dcr.PROD_SER_CD ='') " +
				" AND (dcr.prod_inv_typ=dn.prod_inv_typ OR dcr.prod_inv_typ IS NULL OR dcr.prod_inv_typ ='')  " +
				" AND (dcr.INV_PRD_DIME=dn.INV_PRD_DIME OR dcr.INV_PRD_DIME IS NULL OR dcr.INV_PRD_DIME ='')  " +
				" AND (dcr.INV_PRD_LEN=dn.INV_PRD_LEN OR dcr.INV_PRD_LEN IS NULL OR dcr.INV_PRD_LEN ='') " +
				" WHERE dcr.channel_ids = '" + channel_id + "'"+
				"   AND dnv.id = '" + notice_version_id + "'" ;

		String sqlStrNew = "SELECT" +
				" distinct(dcr.upload_file_type) as upload_file_type, dcr.suffix_file_name," +
				" dcr.upload_file_name_type " +
				" FROM" +
				" idb_disclosure_notice_version dnv" +
				" JOIN idb_disclosure_notice dn ON dnv.t8_disclosure_notice_id = dn.id" +
				" LEFT JOIN idb_disclosure_channel_rule dcr ON dcr.disclosure_type = dn.disclosure_type " +
				" AND dcr.disclosure_son_type = dn.disclosure_son_type " +
				" WHERE dcr.channel_ids = '"+ channel_id + "'"+"   AND dnv.id = '" + notice_version_id + "'" ;

		return super.findRow(sqlStrNew, DataSourceProperty.IDB, null);
	}

	/**
	 * 根据渠道版本id确定需要发布的文件格式
	 * @param notice_version_id
	 * @return
	 */
	public SqlRow queryFileInfByNoticeId (String notice_version_id) throws Exception {
		String sqlStr = "SELECT dn.prod_code, dn.prod_name, dn.disclosure_type, dn.disclosure_son_type, dn.notice_title " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  JOIN idb_disclosure_notice dn ON dn.id = dnv.t8_disclosure_notice_id " +
				" WHERE dnv.id = " + notice_version_id;
		return super.findRow(sqlStr, DataSourceProperty.IDB, null);
	}

	/**
	 * 通过key字段匹配配置表获取字段值
	 * 若返回值会空,则该值通过key去取方法中params参数
	 * @param column_key
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> queryAndRepalceColumnValue (String column_key, Map<String, Object> params) throws Exception {
		Map<String, String> param = new HashMap<>();//仅存一组key-value键值对
		param.put(column_key, "");//默认参数值为空

		String str = "SELECT ds.column_key, ds.column_label, ds.value_sql, ds.data_source " +
				"  FROM idb_disclosure_source ds " +
				" WHERE ds.column_key = '" + column_key + "'";
		SqlRow keyRes = super.findRow(str, DataSourceProperty.IDB, null);
		if(str != null && !"".equals(keyRes.getString("value_sql"))){
			SqlRow valueRes = super.findRow(keyRes.getString("value_sql"), keyRes.getInteger("data_source"), params);
			if(valueRes != null){
				param.put(column_key, valueRes.getString("column_value"));//默认参数值为空
			}
		}
		return param;
	}

	/**
	 * 更新信批公告状态
	 */
	public void updateNoticeStatusById(Map<String, Object> params) throws Exception {
		String updStr = "update idb_disclosure_notice " +
				"           set disclosure_status = $S{pub_status}, " +
				"               real_fb_date = $S{pub_date} " +
				"         where id = $S{notice_id}";
		super.update(updStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 更新信批公告版本状态
	 */
	public void updateNoticeVersionStatus(Map<String, Object> params) throws Exception {
		String updStr = "UPDATE idb_disclosure_notice_version dnv " +
				"   SET dnv.is_notice_pub = $S{pub_status}, dnv.pub_file_name = $S{file_name} " +
				" WHERE id = $S{notice_version_id}";
		super.update(updStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 更新信公告渠道信息状态
	 */
	public void updateNoticeChannelStatus(Map<String, Object> params) throws Exception {
		String updStr = "UPDATE idb_disclosure_notice_channel dnc SET dnc.notice_channel_public_status = $S{pub_status} ,dnc.update_date =  $S{pub_date} ,dnc.update_time =  $S{pub_time} " +
				" WHERE disclosure_notice_version_id = $S{notice_version_id} AND disclosure_notice_channel_id = $S{channel_id}";
		super.update(updStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 发布记录入表
	 */
	public void insertNoticeRecord(Map<String, Object> params) throws Exception {
		String updStr = " insert into idb_notice_channel_pub_record (notice_version_id, channel_id, pub_status, user_id, user_name, pub_date, pub_time, remark) " +
				" values ($S{notice_version_id}, $S{channel_id}, $S{pub_status}, $S{user_id}, " +
				"         $S{user_name}, $S{pub_date}, DATE_FORMAT(NOW(), '%H%i%s'), $S{remark})";
		super.update(updStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 根据信批公告版本判断该替换模板类型: 1-直接替换文字/2-需替换表格
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String getDisclosureTypeInfo (Map<String, Object> params) throws Exception {
		SqlRow noticeRes = super.findRow("SELECT dn.disclosure_type, dn.disclosure_son_type FROM idb_disclosure_notice dn WHERE dn.id = $S{disclosureNoticeId}", DataSourceProperty.IDB, params);
		return noticeRes.getString("disclosure_son_type");//信批公告子类型
	}

	/**
	 * 根据公告版本查询信批模板中指定表格
	 * 需要替换的表头信息
	 * @param noticeVersionId 公告版本id
	 * @return
	 * @throws Exception
	 */
	public List<String[]> getDocGridReplaceContent (String noticeVersionId, Map<String, Object> configParams) throws Exception {
		List<String[]> returnList = new ArrayList<>();
		String sqlStr = "SELECT ngc.disclosure_mod_version_id, ngc.replace_str, ngc.column_name, ngc.row_order, ngc.column_order, ngc.value_column_code, ngc.exeid " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  LEFT JOIN idb_notice_grid_config ngc ON ngc.disclosure_mod_version_id = dnv.disclosure_mod_version_id " +
				" WHERE dnv.id = '" + noticeVersionId + "' AND ngc.replace_str = $S{replace_str}" +
				" ORDER BY ngc.row_order, ngc.column_order ";
		List<SqlRow> configResList = super.findRows(SqlRow.class, sqlStr, DataSourceProperty.IDB, configParams);

		int maxColumnCount = Integer.parseInt(String.valueOf(configParams.get("max_column")));
		//解析表格行列名称参数
		for (int i=0; i<Integer.parseInt(String.valueOf(configParams.get("max_row"))); i++) {//按行读取
			String[] columnStr = new String[maxColumnCount];
			for (int j=0; j<maxColumnCount; j++) {
				columnStr[j] = configResList.get(i*maxColumnCount + j).getString("column_name");
			}
			returnList.add(columnStr);
		}

		return  returnList;
	}

	/**
	 * 获取列表配置表中最大行数列数
	 * @param noticeVersionId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMaxRowColumn (String noticeVersionId) throws Exception {
		List<Map<String, Object>> paramsList = new ArrayList<>();
		String sqlStr = "SELECT MAX(ngc.row_order) as max_row, MAX(ngc.column_order) as max_column, ngc.replace_str, ngc.exeid,ngc.remark " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  LEFT JOIN idb_notice_grid_config ngc ON ngc.disclosure_mod_version_id = dnv.disclosure_mod_version_id " +
				" WHERE dnv.id = '" + noticeVersionId +
				"' GROUP BY ngc.replace_str ";
		List<SqlRow> countResList = super.findRows(sqlStr, DataSourceProperty.IDB);
		for(SqlRow countRes : countResList){
			Map<String, Object> params = new HashMap<>();
			params.put("max_row", countRes.getInteger("max_row"));
			params.put("max_column", countRes.getInteger("max_column"));
			params.put("replace_str", countRes.getString("replace_str"));
			params.put("exe_id", countRes.getString("exeid"));
			params.put("remark", countRes.getString("remark"));
			paramsList.add(params);
		}

		return paramsList;
	}

	/**
	 * 获取水平向需要合并的单元格查询结果
	 * @param configParams
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> getHorizonCellsList (Map<String, Object> configParams) throws Exception {
		String sqlStr = "SELECT ngc.disclosure_mod_version_id, ngc.replace_str, ngc.column_name, ngc.row_order, ngc.column_order, ngc.value_column_code, ngc.exeid, ngc.merge_row_num, ngc.merge_column_num " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  LEFT JOIN idb_notice_grid_config ngc ON ngc.disclosure_mod_version_id = dnv.disclosure_mod_version_id " +
				" WHERE ngc.replace_str = $S{replace_str}  AND merge_column_num >0 " +
				" GROUP BY ngc.value_column_code,ngc.row_order " +
				"HAVING COUNT(1) > 1  " +
				" ORDER BY ngc.column_order, ngc.row_order ";
		return super.findRows(SqlRow.class, sqlStr, DataSourceProperty.IDB, configParams);
	}

	/**
	 * 获取垂直向需要合并的单元格查询结果
	 * @param configParams
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> getVerticalCellsList (Map<String, Object> configParams) throws Exception {
		String sqlStr = "SELECT ngc.disclosure_mod_version_id, ngc.replace_str, ngc.column_name, max(ngc.row_order) as row_order, ngc.column_order, ngc.value_column_code, ngc.exeid, ngc.merge_row_num, ngc.merge_column_num " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  LEFT JOIN idb_notice_grid_config ngc ON ngc.disclosure_mod_version_id = dnv.disclosure_mod_version_id " +
				" WHERE ngc.replace_str = $S{replace_str} AND merge_row_num >0 " +
				" GROUP BY ngc.value_column_code, ngc.column_order " +
				"HAVING COUNT(1) > 1  " +
				" ORDER BY ngc.column_order ";
		return super.findRows(SqlRow.class, sqlStr, DataSourceProperty.IDB, configParams);
	}

	/**
	 * 根据查询语句查询填充数据
	 * @param exeId
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> getFillingValueDataByExeId (String exeId, Map<String, Object> params) throws Exception {
		String sqlStr = ExeQuery.queryExeId(exeId);

		return super.findRows(SqlRow.class, sqlStr, DataSourceProperty.IDB, params);
	}

	/**
	 * 查询推送理财公告文件内容数据行信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public SqlRow queryPubTAIntegrateMessInfo (Map<String, Object> params) throws Exception {
		String str = "SELECT dn.prod_code, (CASE WHEN dn.disclosure_type = '12' THEN '01'/*销售文本*/ WHEN dn.disclosure_type = '2' THEN '03'/*发行成立公告*/ " +
				"                           WHEN dn.disclosure_type = '13' THEN '04'/*申购赎回公告*/ WHEN dn.disclosure_type = '9' THEN '05'/*净值公告*/ " +
				"                           WHEN dn.disclosure_son_type = '0501' THEN '06'/*季度报告*/ WHEN dn.disclosure_son_type = '0502' THEN '07'/*半年报告*/ " +
				"                           WHEN dn.disclosure_type = '3' THEN '08'/*产品到期公告*/ END) AS disclosure_type, dn.prod_name, dn.notice_title " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  LEFT JOIN idb_disclosure_notice dn ON dn.id = dnv.t8_disclosure_notice_id " +
				" WHERE dnv.id = " + params.get("notice_version_id");
		return super.findRow(str, DataSourceProperty.IDB, params);
	}

	/**
	 * 查询推送理财公告文件内容数据行信息
	 * @param pub_date 发布日期
	 * @param channel_id 渠道id
	 * @return
	 * @throws Exception
	 */
	public List<SqlRow> getPubFileList(String pub_date, String channel_id) throws Exception {
		String querySql = "select distinct disclosure_notice_version_id,id,pub_path,batch_no from idb_pubfile_channel_record " +
				" where pub_date = '"+pub_date+"' and disclosure_channel_id = '"+channel_id+"' ";
		return super.findRows(querySql, DataSourceProperty.IDB);
	}

	/**
	 * 插入推送渠道文件列表信息
	 * @param params
	 * @throws Exception
	 */
	public void saveNoticePubFileInfo (Map<String, Object> params) throws Exception {
		String insertSql = "insert into idb_pubfile_channel_record (disclosure_channel_id, pub_path, file_name, disclosure_notice_version_id, batch_no, pub_date, crt_date, crt_time) " +
				           "values ($S{disclosure_channel_id}, $S{pub_path}, $S{file_name}, $S{disclosure_notice_version_id}, " +
				           "$S{batch_no}, $S{pub_date}, date_format(SYSDATE(),'%Y%m%d'),date_format(SYSDATE(),'%H%i%s')) ";
		super.update(insertSql, DataSourceProperty.IDB, params);
	}

	/**
	 * 获取信批公告中表格字段顺序数组
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String[] getFillingGridColumnOrder (Map<String, Object> params) throws Exception {
		int strLength = 0;
		try {
			strLength = Integer.parseInt(String.valueOf(params.get("max_column")));//此处容易抛异常
		} catch (Exception e) {
			log.error("获取信批公告中表格字段顺序数组异常" + e);
		}

		String[] column_order = new String[strLength];
		String sqlStr = "SELECT GROUP_CONCAT(gc.value_column_code) AS value_column_code " +
				"  FROM idb_disclosure_notice_version dnv " +
				"  JOIN idb_notice_grid_config gc ON gc.disclosure_mod_version_id = dnv.disclosure_mod_version_id " +
				" WHERE dnv.id = $S{noticeVersionId} " +
				"   AND gc.replace_str = $S{replace_str} " +
				" ORDER BY gc.column_order ";
		SqlRow sqlRes = super.findRow(sqlStr, DataSourceProperty.IDB, params);
		if(sqlRes != null){
			column_order = sqlRes.getString("value_column_code").split(",");
		}
		return column_order;
	}

	/**
	 * 删除当前模板表单配置
	 * @throws Exception
	 */
	public void delNoticeGridConfig (Map<String, Object> params) throws Exception{
		super.update("delete from idb_notice_grid_config where disclosure_mod_version_id = $S{disclosure_mod_version_id}", DataSourceProperty.IDB, params);
	}

	/**
	 * 重新根据基准之间配置表单配置信息
	 * @throws Exception
	 */
	public void replaceNoticeGridConfig (Map<String, Object> params) throws Exception{
		String sql = "insert into idb_notice_grid_config (disclosure_mod_version_id,replace_str,column_name,row_order,column_order,value_table_name," +
				     "       value_column_code,exeid,merge_row_num,merge_column_num) " +
				     "select dmv.id as disclosure_mod_version_id, gc.replace_str, gc.column_name, gc.row_order, gc.column_order, gc.value_table_name, " +
				     "       gc.value_column_code, gc.exeid, gc.merge_row_num, gc.merge_column_num " +
				     "  from idb_disclosure_notice_version dnv " +
				     "  join idb_disclosure_notice dn on dnv.t8_disclosure_notice_id = dn.id " +
				     "  join idb_disclosure_mod_version dmv on dmv.id = dnv.disclosure_mod_version_id " +
				     "  join idb_disclosure_mod dm on dm.id = dmv.disclosure_mod_id " +
				     "  left join idb_notice_grid_config_source gc on gc.disclosure_type = dm.disclosure_type  and gc.disclosure_son_type = dm.disclosure_son_type " +
				     " where 1=1 and dn.prod_base_date >= gc.effect_date and dn.prod_base_date < gc.expiry_date " +
				     "   and dmv.id not in (select distinct ng.disclosure_mod_version_id from idb_notice_grid_config ng)/*已经存在的配置信息保存无需插入*/ " +
				     "   and dnv.id = $S{disclosureNoticeVersionId} " +
				     " order by gc.replace_str , gc.row_order ,gc.column_order";
		super.update(sql, DataSourceProperty.IDB, params);
	}

}