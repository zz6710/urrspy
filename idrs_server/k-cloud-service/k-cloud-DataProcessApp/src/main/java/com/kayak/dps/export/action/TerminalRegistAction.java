package com.kayak.dps.export.action;

import com.alibaba.druid.util.StringUtils;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.action.BaseController;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.export.dao.ExportExcelDao;
import com.kayak.dps.export.util.ExportExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

@RestController
public class TerminalRegistAction extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TerminalRegistAction.class);


	@Autowired
	private ExportExcelDao exportExcelDao;


	/**
	 * 描述：交易对手信息excel导出
	 *
	 */
	@RequestMapping(value = "/conterPartyExpoertExcel.json")
	public void expoertExcel(HttpServletResponse response) throws Exception {
		Map<String, Object> params = RequestSupport.getParameters();
		Map<String,String> pathParams = new HashMap<>();
		List<Map<String, String>> filePaths = new ArrayList<>();
		pathParams.put("win","70000010002");
		pathParams.put("os","70000010009");
		String rootPath = ExportExcelUtil.getRootPath(pathParams) + UUID.randomUUID().toString().replace("-","").toUpperCase() + "/";
		List<String> headList = Arrays.asList(String.valueOf(params.get("heads")).split(","));
		String fieldArrs[] = String.valueOf(params.get("fields")).split(",");
		String sql = "select " + String.valueOf(params.get("fields"))+" from mid_ass_counter_party a left join mid_ast_itts_org_bas_inf b on a.ORG_CD=b.ORG_NBR_EXT  where 1=1 ";
		if(params.get("counterPartyCd") != null){
			sql += " and a.counter_party_cd = '" + String.valueOf(params.get("counterPartyCd"))+"'";
		}
		if(params.get("counterPartyNm") != null){
			sql += " and a.counter_party_nm like '%"+String.valueOf(params.get("counterPartyNm"))+"%' ";
		}
		if(params.get("orgNm") != null){
			sql += " and a.ORG_NM like '%"+String.valueOf(params.get("orgNm"))+"%' ";
		}
		sql += "order by a.ORG_CD";
		List<SqlRow>  dataLt = exportExcelDao.getDatas(sql,String.valueOf(DataSourceProperty.PUB));
		List<List<String>> dataList = new ArrayList<>();
		for (SqlRow sqlRow:dataLt) {
			List<String> rowList = new ArrayList<>();
			for (int j = 0; j < fieldArrs.length; j++) {
				rowList.add(StringUtils.equals(String.valueOf(sqlRow.get(fieldArrs[j])),"null")?"":String.valueOf(sqlRow.get(fieldArrs[j])));
			}
			dataList.add(rowList);
		}
			ExportExcelUtil.setSheet("交易信息登记");
			ExportExcelUtil.createHead(headList);
			ExportExcelUtil.createContent(dataList);
			File file = new File(rootPath);
			if (!file.isDirectory()) file.mkdirs();
			String createFileName =  "交易对手信息.xls";
			ExportExcelUtil.writeToFile(rootPath + createFileName);
			Map<String, String> fileMap = new HashMap<>();
			fileMap.put("filePath",rootPath + createFileName);
			fileMap.put("fileName",createFileName);
			filePaths.add(fileMap);

		ExportExcelUtil.downloadFile(rootPath + createFileName ,createFileName ,response);
		ExportExcelUtil.deleteFolder(new File(rootPath));
	}

}
