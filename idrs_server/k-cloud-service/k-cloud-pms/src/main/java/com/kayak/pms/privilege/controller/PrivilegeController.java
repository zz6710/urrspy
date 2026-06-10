package com.kayak.pms.privilege.controller;

import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.privilege.dao.PrivilegeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


@RestController
public class PrivilegeController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(PrivilegeController.class);
	@Autowired
	private PrivilegeDao privilegeDao;

	@RequestMapping(value = "/privilege/getResult.json",produces = { "application/json;charset=UTF-8"})
	public HashMap<String, Object> getResult(HttpServletRequest request) {
		String sqltext = request.getParameter("sqls");
		HashMap<String, Object> mapData = new HashMap<String, Object>();
		List<String> columns = new ArrayList<>();
		List<SqlRow> rows =new ArrayList<>();
		StringBuilder build = new StringBuilder();
		mapData.put("size","0");
		mapData.put("columns","");
		mapData.put("rows","");
		mapData.put("msg","");
		int updatenumber = 0;
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

				for (String str:sqlStrings) {
					if(str.toLowerCase().startsWith("select") || str.toLowerCase().startsWith("with")) {
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
}
