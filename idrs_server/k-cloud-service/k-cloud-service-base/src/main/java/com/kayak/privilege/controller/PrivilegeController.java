package com.kayak.privilege.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import com.kayak.privilege.dao.PrivilegeDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
public class PrivilegeController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
	@Autowired
	private PrivilegeDao privilegeDao;
	@Value("${database.schemas}")
	private String schemas;

	@RequestMapping(value = "/privilege/getResult.json",produces = { "application/json;charset=UTF-8"})
	public HashMap<String, Object> getResult(HttpServletRequest request) {
		String sqltext0 = request.getParameter("zhixingyuju");
		String sqltext = decrypt(sqltext0);
		HashMap<String, Object> mapData = new HashMap<String, Object>();
		List<String> columns = new ArrayList<>();
		HashMap<String, Object> columnComment = new HashMap<String, Object>();
		List<SqlRow> rows =new ArrayList<>();
		List<SqlRow> rows1 =new ArrayList<>();
		StringBuilder build = new StringBuilder();
		mapData.put("size","0");
		mapData.put("columns","");
		mapData.put("columnComment","");
		mapData.put("rows","");
		mapData.put("msg","");
		int updatenumber = 0;
		String []schemaGroup =schemas.split(",");
		String newschemas= StringUtils.join(schemaGroup,"','");
		try {
			if(sqltext != null){
				String sql =  sqltext;
				String[] sqlStrings = sql.split(";");
				int htmlString = 0;
				if(sqlStrings!=null){
					for(String str:sqlStrings){
						if(!str.trim().equals(""))
							build.append(str).append(";");
					}
				}
				String new_version_flag = SysUtil.getSystemParamsByParaid("90000051401");//是否启用最新版本 1为是
				for (String str:sqlStrings) {
					if("1".equals(new_version_flag) && (str.toLowerCase().startsWith("select") || str.toLowerCase().startsWith("with")) ) {//启用新版本 查询字段注释
						String startsql = "select * from (";
						String endsql = ") as pi limit 0,200";

						if (str.endsWith("all") || str.endsWith("all;")) {
							startsql = str.replaceAll("all[;]?$", "");
						} else {
							startsql = startsql + str + endsql;
						}

						String regStr = "from\\s+(?<g1>\\S+)";
						Pattern pattern = Pattern.compile(regStr);
						Matcher matcher = pattern.matcher(str);
						String tableName = "";
						while (matcher.find()) {
							tableName = matcher.group(1);
						}
						rows =  privilegeDao.getRows(startsql);
						rows1 = privilegeDao.getRows("select * from information_schema.`COLUMNS` c where c.TABLE_SCHEMA in ('"+newschemas+"')" + " and c.TABLE_NAME = '" + tableName + "'");


						if(rows.size()>0){
							Map<String, Object> col = rows.get(0);
							Iterator iterator = col.keySet().iterator();
							while (iterator.hasNext())
							{
								String key = (String) iterator.next();
								columns.add(key);
								for (int i = 0; i < rows1.size(); i++) {
									if(rows1.get(i).get("COLUMN_NAME").equals(key)) {
										columnComment.put(key, rows1.get(i).get("COLUMN_COMMENT"));
									}
								}

							}
						}
						updatenumber = -1;
						break;
					}else if(str.toLowerCase().startsWith("select") || str.toLowerCase().startsWith("with") ) {
						//走老本版 不查询字段注释
						String startsql = "select * from (";
						String endsql = ") as pi limit 0,200";

						if (str.endsWith("all") || str.endsWith("all;")) {
							startsql = str.replaceAll("all[;]?$", "");
						} else {
							startsql = startsql + str + endsql;
						}

						rows =  privilegeDao.getRows(startsql);

						if(rows.size()>0){
							Map<String, Object> col = rows.get(0);
							Iterator iterator = col.keySet().iterator();
							while (iterator.hasNext())
							{
								columns.add((String) iterator.next());
							}
						}
						updatenumber = -1;
						break;
					}else if(!"".equals(str)){
						updatenumber += privilegeDao.runUpdate(str);
					}else{
						updatenumber = -1;
					}
				}

				if(columns.size()==0){
					mapData.put("success", true);
					mapData.put("size","0");
					mapData.put("columns","");
					mapData.put("columnComment","");
					mapData.put("rows","");
					if(updatenumber>0){
						mapData.put("msg",updatenumber+"条执行成功");
					}else{
						mapData.put("msg","查询结果为空");
					}
				}else{
					mapData.put("size",rows.size());
					mapData.put("columns",columns);
					mapData.put("rows",rows);
					mapData.put("columnComment",columnComment);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(updatenumber == 0){
				mapData.put("msg",e.getMessage());
			}
		}
		logger.info(columns.toString());
		logger.info(rows.toString());
		return mapData;
	}

	private String decrypt(String encryptedData) {
		try {
			// 使用Base64解码
			byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
			// 将字节数组转换为字符串
			return new String(decodedBytes, "UTF-8");
		} catch (Exception e) {
			log.info("传入语句解码失败！！！");
			e.printStackTrace();
			return null;
		}
	}

}
