package com.kayak.rpt.zz.manage.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.graphql.model.FetcherData;
import com.kayak.rpt.rhzj.util.ExcelParse;
import com.kayak.rpt.zz.manage.dao.ImportTemplateManageFieldFstDao;
import com.kayak.rpt.zz.manage.model.AppNavInfoReg;
import com.kayak.rpt.zz.manage.model.ImportTemplateManageFieldFst;
import com.kayak.rpt.zz.manage.model.ImportTemplateManageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@APIDefine(desc = "EXCEL表头对比服务", model = AppNavInfoReg.class)
public class CompareHeaderService {

	@Autowired
	ImportTemplateManageFieldFstDao importTemplateManageFieldFstDao;

	/**
	 *
	 * @param file
	 * @param systemTableName 对应表名
	 * @param headerNumber 要忽略的头信息行数，实际是读取的excel的headerNumber +1 行（行数计算从下标0起始）
	 * @return
	 * @throws Exception
	 */
	public String compare(MultipartFile file, String systemTableName,int headerNumber) throws Exception {
		String isCheck = "0";
		//headerNumber 不传默认0，起始值0代表第一行
		List<String> lists = ExcelParse.readExcelHears(file.getInputStream(), 0, headerNumber, true, null);
		Map<String, Object> params00 = new HashMap<>();
		params00.put("systemTableName", systemTableName);
		//若无模板，importFiles为0条。
		List<ImportTemplateManageFieldFst> importFiles = importTemplateManageFieldFstDao.findTemplateFieldList(new FetcherData<ImportTemplateManageFieldFst>(params00, ImportTemplateManageFieldFst.class)).getRows();
		List<String> columnList = new ArrayList<>();
		for (ImportTemplateManageFieldFst field : importFiles) {
			String database_column_name = field.getDatabaseColumnName();
			String template_column_serial = field.getTemplateColumnSerial();
			columnList.add(database_column_name);
		}

		for (int i = 0; i < columnList.size(); i++) {
			String column_code = columnList.get(i);
			if (!column_code.equals(lists.get(i))) {
				isCheck = "第" + (i + 1) + "列字段匹配错误，应该为：" + column_code;
				break;
			}
		}
		return isCheck;
	}


	/**
	 * 获取模板中表头所在行位置  主要受模板头信息影响
	 * 模板未启用及未设定表头起始起始值的时候，默认表头按一行处理
	 *
	 * @param systemTableName
	 * @return default 1    else  rowStart
	 * @throws Exception
	 */
	public int getTempalteDataStartRow(String systemTableName) throws Exception {
		Map<String, Object> paramsReq = new HashMap<>();
		paramsReq.put("systemTableName", systemTableName);
		SqlParam<ImportTemplateManageVo> params =new FetcherData<ImportTemplateManageVo>(paramsReq, ImportTemplateManageVo.class);

		int rowStart = 0;
		SqlResult<ImportTemplateManageVo> result = importTemplateManageFieldFstDao.findTemplateHeadStartRow(params);
		if(result==null || result.getRows()==null || result.getRows().size()==0|| StringUtils.isEmpty(result.getRows().get(0).getRowStart())){
			//模板未启用，或未设定表头起始值的情况下 默认表头按一行处理
			rowStart = 1;
		}else{
			//模板启用时
			rowStart = Integer.parseInt(result.getRows().get(0).getRowStart());
			//设定表头起始值为0的情况下 默认表头按一行处理
			if(rowStart==0){
				rowStart = 1;
			}
		}
		return rowStart;
	}
}