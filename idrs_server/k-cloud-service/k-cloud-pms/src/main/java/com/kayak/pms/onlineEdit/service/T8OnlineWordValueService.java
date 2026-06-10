package com.kayak.pms.onlineEdit.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.T81.dao.T8ProdDocInfoDao;
import com.kayak.pms.T81.dao.T8ProdInfoDao;

import com.kayak.pms.T81.model.T8ProdInfo;

import com.kayak.pms.onlineEdit.dao.T8OnlineWordTableColumnsDao;
import com.kayak.pms.onlineEdit.dao.T8OnlineWordValueDao;
import com.kayak.pms.onlineEdit.model.T8OnlineWordValue;
import com.kayak.pms.printTemp.utils.UploadUtils;
import com.kayak.pms.printTemp.utils.WordToPdfUtil;
import com.kayak.utils.OnlineUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: k-cloud
 * @description: 文档在线编辑数据Service
 * @author: WangZhenXin
 * @create: 2021-01-29 19:04
 * @memo 备注信息
 */
@Service
@APIDefine(desc = "文档在线编辑数据Service", model = T8OnlineWordValue.class)
public class T8OnlineWordValueService {
	private static final Logger logger = LoggerFactory.getLogger(T8OnlineWordValueService.class);

	@Autowired
	private T8OnlineWordValueDao t8OnlineWordValueDao;


	@Autowired
	private T8ProdInfoDao t8ProdInfoDao;

	
	@Autowired
	private T8ProdDocInfoDao t8ProdDocInfoDao;

	@Value("${path.word}")
	private String winPath;
	
	@Autowired
	private T8OnlineWordTableColumnsDao t8OnlineWordTableColumnsDao;

	@Autowired
	private WordToPdfUtil wordToPdfUtil;

	public int initT8OnlineWordValue(List<T8OnlineWordValue> t8OnlineWordValue) throws Exception {
		int flag = 0;
		for (T8OnlineWordValue onlineWordValue : t8OnlineWordValue) {
			flag += t8OnlineWordValueDao.initT8OnlineWordValue(onlineWordValue);
		}
		return flag;
	}

	@API(desc = "在线编辑", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordValue> getT8OnlineWordValueList(SqlParam<T8OnlineWordValue> param) throws Exception {
		Map<String, Object> parameters = RequestSupport.getParameters();
		String prodCode = (String) parameters.get("prodCode");
		String documentType = (String) parameters.get("documentType");
		SqlResult<T8OnlineWordValue> t8OnlineWordValueList = t8OnlineWordValueDao.getT8OnlineWordValueList(param);
		/* 对是份额分类的产品进行处理（目前主要是针对产品说明书和创设方案） */
		if (StringUtils.isNotBlank(prodCode) && StringUtils.isNotBlank(documentType)
				&& (documentType.endsWith("01") || documentType.endsWith("07"))) {
			// 通过产品代码查询产品信息
			T8ProdInfo prodInfo = t8ProdInfoDao.getProdInfoByProdCode(prodCode);
			// 如果是份额分类
			if ("1".equals(prodInfo.getIsShareSort())) {
				String fileSeparator = "/";
				// 获取文档路径信息
				List<T8OnlineWordValue> rows = t8OnlineWordValueList.getRows();
				if (!CollectionUtils.isEmpty(rows)) {
					String basePath = "";
					String os = System.getProperty("os.name");
					String s = "";
					// 根据操作系统配置临时路径
					if (os.toLowerCase().startsWith("win")) {
						basePath = winPath;
					} else {
						s = "80000080003";
						basePath = SysUtil.getSystemParamsByParaid(s);
					}

					/************************************ 2.处理word文档动态表格 *************************/
					// 得到文档路径
					String viewUrl = rows.get(0).getViewUrl();
					// 获取文件名
					String fileName = rows.get(0).getFileName();
					viewUrl = basePath + fileSeparator + viewUrl.substring(viewUrl.lastIndexOf("printTemp"));
					String wordPath = viewUrl.substring(0, viewUrl.lastIndexOf(".")) + ".docx";
					logger.info("读取到文档路径:{}", wordPath);
					wordToPdfUtil.getLicense();
					com.aspose.words.Document document = new com.aspose.words.Document(wordPath);
					// 对不同的文档进行不同的处理

					/************************************ 3.将处理过后的文档保存到一个临时路基 ***************/
					String temPath = basePath + fileSeparator + "printTemp" + fileSeparator
							+ rows.get(0).getT8PrintTempVersionId() + fileSeparator + "tempFile" + fileSeparator;
					// 先删除目录下的文件
					FileUtils.deleteDirectory(new File(temPath));
					document.save(temPath + fileName);
					logger.info("临时文件保存成功,保存路径{}", temPath + fileName);
					UploadUtils.wordToHtml(temPath + fileName, null);
					// 设置返回的在线编辑html路径
					// String returnPath = temPath + fileName.substring(0,
					// fileName.lastIndexOf(".")) + ".html";
					String ipPath = rows.get(0).getViewUrl();
					ipPath = ipPath.substring(0, ipPath.lastIndexOf("printTemp"));
					String returnPath = ipPath + "printTemp" + fileSeparator + rows.get(0).getT8PrintTempVersionId()
							+ fileSeparator + "tempFile" + fileSeparator
							+ fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
					logger.info("返回路径:{}", returnPath);
					rows.forEach(item -> {
						item.setViewUrl(returnPath);
					});
				}
			}
		}
		return t8OnlineWordValueList;
	}

	
	@API(desc = "申请发行说明预览", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordValue> preview(SqlParam<T8OnlineWordValue> param) throws Exception {
		Map<String, Object> parameters = RequestSupport.getParameters();
		String prodCode = (String) parameters.get("prodCode");
		SqlResult<T8OnlineWordValue> t8OnlineWordValueList = t8OnlineWordValueDao.getT8OnlineWordValueList(param);
		// 通过产品代码查询产品信息
	   T8ProdInfo prodInfo = t8ProdInfoDao.getProdInfoByProdCode(prodCode);	
		//（份额分类，开放式产品），“产品期限”不应该显示
	   List<T8OnlineWordValue> wordValues= t8OnlineWordValueList.getRows();
		if("1".equals(prodInfo.getIsShareSort())&&!"1".equals(prodInfo.getProdMode())) {
			wordValues.parallelStream().forEach((wordValue)->{
				if("product_term_desc".equals(wordValue.getWordKey())) {
					wordValue.setWordValue("");
				}
				
			});
		}
		//（非份额分类，封闭式产品），运作模式显示错误
		if(!"1".equals(prodInfo.getIsShareSort())&&"1".equals(prodInfo.getProdMode())) {
			 wordValues.parallelStream().forEach((wordValue)->{
				if("cycle_open_term".equals(wordValue.getWordKey())||"cycle_open_type".equals(wordValue.getWordKey())) {
					wordValue.setWordValue("");
				}
				
			});
		}
		t8OnlineWordValueList.setRows(wordValues);
		return t8OnlineWordValueList;
	}

	@API(desc = "根据模板Id获取最大Id数据", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordValue> getMaxT8OnlineWordValueByT8PrintTempVersionId1(
			SqlParam<T8OnlineWordValue> t8OnlineWordValueSqlParam) throws Exception {
		T8OnlineWordValue t8OnlineWordValue = t8OnlineWordValueDao.getMaxT8OnlineWordValueByT8PrintTempVersionId(
				t8OnlineWordValueSqlParam.getModel().getT8PrintTempVersionId());
		SqlResult<T8OnlineWordValue> sqlResult = new SqlResult<>();
		ArrayList<T8OnlineWordValue> list = new ArrayList<>();
		list.add(t8OnlineWordValue);
		sqlResult.setRows(list);
		sqlResult.setResults(list.size());
		sqlResult.setDesensitized(false);
		return sqlResult;
	}

	@API(desc = "根据模板Id获取最大Id数据", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult<T8OnlineWordValue> getMaxT8OnlineWordValueByProcessInstanceId(
			SqlParam<T8OnlineWordValue> t8OnlineWordValueSqlParam) throws Exception {
		T8OnlineWordValue t8OnlineWordValue = t8OnlineWordValueDao.getMaxT8OnlineWordValueByProcessInstanceId(
				t8OnlineWordValueSqlParam.getModel().getProcessInstanceId());
		SqlResult<T8OnlineWordValue> sqlResult = new SqlResult<>();
		ArrayList<T8OnlineWordValue> list = new ArrayList<>();
		list.add(t8OnlineWordValue);
		sqlResult.setRows(list);
		sqlResult.setResults(list.size());
		sqlResult.setDesensitized(false);
		return sqlResult;
	}

	public T8OnlineWordValue getMaxT8OnlineWordValueByT8PrintTempVersionId(String t8PrintTempVersionId)
			throws Exception {
		return t8OnlineWordValueDao.getMaxT8OnlineWordValueByT8PrintTempVersionId(t8PrintTempVersionId);
	}

	@API(desc = "根据模板Id获取Id数据", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public List<T8OnlineWordValue> getT8OnlineWordValueListByT8ProdDocumentVersionId(String t8ProdDocumentVersionId)
			throws Exception {
		return t8OnlineWordValueDao.getT8OnlineWordValueListByT8ProdDocumentVersionId(t8ProdDocumentVersionId);
	}

	@API(desc = "根据模板Id获取Id数据", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public List<T8OnlineWordValue> getT8OnlineWordValueListByT8PrintTempVersionId(String t8PrintTempVersionId)
			throws Exception {
		return t8OnlineWordValueDao.getT8OnlineWordValueListByT8PrintTempVersionId(t8PrintTempVersionId);
	}

	public SqlRow getDistributorInfo(String prodCode) throws Exception {
		return t8OnlineWordValueDao.getDistributorInfo(prodCode);
	}

	public String valadationRiskNum(SqlParam<T8OnlineWordValue> t8OnlineWordValueSqlParam) throws Exception {
		 Map<String,Object> param = RequestSupport.getParameters();
		SqlRow riskRow = t8ProdDocInfoDao.getRiskNum(param);
		if(riskRow!=null) {
			return RequestSupport.updateReturnJson(true, "", null).toString();
		}else {
			return RequestSupport.updateReturnJson(false, "请先保存文档再预览！", null).toString();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@API(desc = "获取数据源", auth = APIAuth.NO, operation = APIOperation.SELECT)
	public SqlResult findDataInfo(SqlParam<T8OnlineWordValue> t8OnlineWordValueSqlParam) throws Exception {

		final List  docNames = Arrays.asList(new String[] {"公募定开产品合同模板-不含份额分类.docx","公募定开产品说明书模板-含份额分类.docx",
				"公募定开产品合同模板-不含份额分类+封闭期投资日.docx","公募定开产品说明书模板-含份额分类+封闭期投资日.docx",
				"公募封闭产品合同模板-不含份额分类.docx","公募封闭产品合同模板-含份额分类.docx",
				"公募最低持有期产品合同模板（定开净值）-不含份额分类+含产品开放日.docx","公募最低持有期产品合同模板（天天净值）-不含份额分类+含产品开放日.docx"});
	   SqlResult<T8OnlineWordValue> sqlResult = new SqlResult<>();
		//文档数据源
		SqlRow sqlRow = t8OnlineWordValueDao.findDataInfo(t8OnlineWordValueSqlParam.getModel());
        Map<String,Object> param = RequestSupport.getParameters();
		Set<String> keys = sqlRow.keySet();
		List<T8OnlineWordValue> wordValues = new ArrayList();
		//文档数据映射查询
		List<SqlRow> columDatas = t8OnlineWordValueDao.findColumnData();
		T8OnlineWordValue t8OnlineWordValue = t8OnlineWordValueDao.getMaxT8OnlineWordValueByT8PrintTempVersionId(
				t8OnlineWordValueSqlParam.getModel().getT8PrintTempVersionId());
		//文档模板风险数目（产品说明书，创设方案）
		SqlRow riskRow = t8ProdDocInfoDao.getRiskNum(param);
		int riskNum = StringUtils.isEmpty(riskRow==null?null:riskRow.getString("risk_num")) == true ? 0: Integer.parseInt(riskRow.getString("risk_num"));
		param.put("riskNum", riskNum);
		keys.forEach((t) -> {
			T8OnlineWordValue wordValue = new T8OnlineWordValue();
			wordValue.setWordKey(t);
			wordValue.setWordValue(String.valueOf(sqlRow.get(t)));
			wordValue.setViewUrl(t8OnlineWordValue.getViewUrl());
			if("excess_status".equals(t)) {
				wordValues.add(wordValue);
			}
			try {

				for (SqlRow columData : columDatas) {
					String val = null;
					if ((StringUtils.isEmpty(columData.getString("column_name"))
							|| !t.equals(columData.getString("column_name"))))
						continue;
					T8OnlineWordValue tempVal = new T8OnlineWordValue();
					if (StringUtils.isNotEmpty(columData.getString("default_value"))) {
						// 替换默认值
						val = changeDefaultVal(columDatas,columData, sqlRow);
						wordValue.setWordValue(val);
					}
					if (StringUtils.isNotEmpty(columData.getString("dict"))) {
						// 替换数据字典
						val = changeDictVal(columData, String.valueOf(sqlRow.get(t)));
						wordValue.setWordValue(val);
					}

					if (StringUtils.isNotEmpty(columData.getString("sql_info"))) {
						// 替换sql
						t8OnlineWordValueSqlParam.getModel().setWordKey(columData.getString("column_name"));
						val = changeSqlVal(columData, t8OnlineWordValueSqlParam.getModel(),param);
						wordValue.setWordValue(val);

					}
					wordValue.setWordKey(columData.getString("doc_column"));
					BeanUtil.copyProperties(wordValue, tempVal, true);
					wordValues.add(tempVal);
				}
				

			} catch (Exception e) {
				logger.error("数据库查询异常：【{}】", e);
			}

		});
		int has_other_risk_num = (int)param.get("riskNum")+1;
		boolean flag = false;
		boolean excessStatus = false;
		for(T8OnlineWordValue wordValue:wordValues) {
			if("has_other_risk".equals(wordValue.getWordKey())) {
				wordValue.setWordValue(String.valueOf(has_other_risk_num));
			}
			
			if("redemption_fee".equals(wordValue.getWordKey())&&StringUtils.isNotEmpty(wordValue.getWordValue())) {
				flag=true;
			}
			 if("excess_status".equals(wordValue.getWordKey())&&StringUtils.isNotEmpty(wordValue.getWordValue())&&"true".equals(wordValue.getWordValue())) {         	
	            	excessStatus=true;	
	            }
			 if((wordValue.getWordKey().endsWith("date")&&StringUtils.isNotEmpty(wordValue.getWordValue())&&wordValue.getWordValue().length()>=8)) {         	
				 wordValue.setWordValue(wordValue.getWordValue().substring(0,4)+"年"+wordValue.getWordValue().substring(4,6)+"月"+wordValue.getWordValue().substring(6,8)+"日");	
	            }
		}
		for(T8OnlineWordValue wordValue:wordValues) {
			if(flag==true&&"redemption_fee_num".equals(wordValue.getWordKey()))
				wordValue.setWordValue("10.2.6");	
			if(excessStatus==false&&wordValue.getWordKey().contains("excess_performance_reward")) {
				if(docNames.contains(t8OnlineWordValue.getFileName().replace(" ",""))) {
					wordValue.setWordValue("本产品不收取超额业绩报酬。");
				}else {
					wordValue.setWordValue("本产品暂不收取超额业绩报酬。");
				}
				
			}
				
			if(excessStatus==false&&"excess_perf_explain".equals(wordValue.getWordKey())) {
				if(docNames.contains(t8OnlineWordValue.getFileName().replace(" ",""))) {
					wordValue.setWordValue("本产品不收取超额业绩报酬。");
				}else {
					wordValue.setWordValue("本产品暂不收取超额业绩报酬。");
				}
			}
				
		}
		Map<String, Object> parameters = RequestSupport.getParameters();
		String prodCode = (String) parameters.get("prodCode");
		String documentType = (String) parameters.get("docType");
		
		/* 对是份额分类的产品进行处理（目前主要是针对产品说明书和创设方案） */
		if (StringUtils.isNotBlank(prodCode) && StringUtils.isNotBlank(documentType)
				&& (documentType.endsWith("01") || documentType.endsWith("07"))) {
			// 通过产品代码查询产品信息
			T8ProdInfo prodInfo = t8ProdInfoDao.getProdInfoByProdCode(prodCode);
			// 如果是份额分类
			if ("1".equals(prodInfo.getIsShareSort())) {
				
				// 获取文档路径信息
				
				if (!CollectionUtils.isEmpty(wordValues)) {
					String basePath = "";
					String os = System.getProperty("os.name");
					String s = "";
					// 根据操作系统配置临时路径
					if (os.toLowerCase().startsWith("win")) {
						basePath = winPath;
					} else {
						s = "80000080003";
						basePath = SysUtil.getSystemParamsByParaid(s);
					}
					/********************* 1.组装份额分类信息 *********************/

					/************************************ 2.处理word文档动态表格 *************************/
					// 得到文档路径
					String viewUrl = wordValues.get(0).getViewUrl();
					// 获取文件名
					String fileName = t8OnlineWordValue.getFileName();
					viewUrl = basePath + File.separator + viewUrl.substring(viewUrl.lastIndexOf("printTemp"));
					String wordPath = viewUrl.substring(0, viewUrl.lastIndexOf(".")) + ".docx";
					logger.info("读取到文档路径:{}", wordPath);
					wordToPdfUtil.getLicense();
					com.aspose.words.Document document = new com.aspose.words.Document(wordPath);


					/************************************ 3.将处理过后的文档保存到一个临时路基 ***************/
					String temPath = basePath + File.separator + "printTemp" + File.separator
							+ t8OnlineWordValueSqlParam.getModel().getT8PrintTempVersionId() + File.separator + "tempFile" + File.separator;
					// 先删除目录下的文件
					FileUtils.deleteDirectory(new File(temPath));
					document.save(temPath + fileName);
					logger.info("临时文件保存成功,保存路径{}", temPath + fileName);
					UploadUtils.wordToHtml(temPath + fileName, null);
					// 设置返回的在线编辑html路径
					// String returnPath = temPath + fileName.substring(0,
					// fileName.lastIndexOf(".")) + ".html";
					String ipPath = wordValues.get(0).getViewUrl();
					ipPath = ipPath.substring(0, ipPath.lastIndexOf("printTemp"));
					String returnPath = ipPath + "printTemp" + File.separator + t8OnlineWordValueSqlParam.getModel().getT8PrintTempVersionId()
							+ File.separator + "tempFile" + File.separator
							+ fileName.substring(0, fileName.lastIndexOf(".")) + ".html";
					logger.info("返回路径:{}", returnPath);
					wordValues.forEach(item -> {
						item.setViewUrl(returnPath);
					});
				}
			}
		}
		List<SqlRow> onlineWordTableColums = t8OnlineWordTableColumnsDao.findT8OnlineWordTableColumns();
		for(T8OnlineWordValue wordValue :wordValues) {
			for(SqlRow tableColumn :onlineWordTableColums) {
				if(wordValue.getWordKey().equals(tableColumn.getString("column_name"))) {
					String tV = OnlineUtils.dataTypeHandler(wordValue, tableColumn.getString("data_type"), tableColumn.getString("data_digits"), wordValue.getWordValue());
					wordValue.setWordValue(tV);
				}
			}
		}
		sqlResult.setRows(wordValues);
		return sqlResult;
	}

	public String changeDefaultVal(List<SqlRow> columDatas,SqlRow columData, SqlRow sqlRow) throws Exception {
		List<String> keys = (List<String>) sqlRow.keySet().stream().collect(Collectors.toList());
		String dfV = columData.getString("default_value");
		for (String key : keys) {
			if (dfV.contains(key)) {
				String val = sqlRow.getString(key);
				if(StringUtils.isEmpty(val))
					return  getEmptyDefaultVal(columData, val);
				//判断默认值里字段是否有数据字典
				if(columDatas.contains(columData)&&StringUtils.isNotEmpty(columData.getString("dict"))) {
					val = changeDictVal(columData, String.valueOf(sqlRow.get(key)));
				}
				dfV = dfV.replace(String.valueOf("${" + key + "}"),val );

			}
		}
		return dfV;
	}

	//// val为空时，  判断文档映射字段empty_default_val 
	public String getEmptyDefaultVal(SqlRow columData,String val) {
		if(StringUtils.isNotEmpty(columData.getString("empty_default_val"))) {
			return columData.getString("empty_default_val");
		}
		return val;
	}
	public String changeDictVal(SqlRow columData, String dictKey) throws Exception {
		String val = "";
		if (MapUtil.isEmpty(columData)) {
			return getEmptyDefaultVal(columData, null);
		}
		if(Strings.isNotEmpty(dictKey)&&dictKey.contains(",")) {
			String []arry = dictKey.split(",");
			
			for(String itemkey : arry) {
				 String temp = CacheUtil.getDictItem(columData.getString("dict"), itemkey);
				if(temp==null){
					continue;
				}else if(arry.length>1){
					if(Strings.isBlank(val)) {
						val=temp;
					}else {
						val=val+"、"+temp;
					}
					
				}else {
					val=temp;
				}
				
			}
			
		}else if(Strings.isNotEmpty(dictKey)){
			 val = CacheUtil.getDictItem(columData.getString("dict"), dictKey);
		}
		return val;
	}

	public String changeSqlVal(SqlRow columData, T8OnlineWordValue wordValue,Map<String,Object> param) throws Exception {
		String sql = columData.getString("sql_info");
		List<SqlRow> sqlRows = t8OnlineWordValueDao.execSql(sql, wordValue);
		if (CollectionUtils.isEmpty(sqlRows)) {
			return getEmptyDefaultVal(columData, null);
		}
			
		int riskNum = (int)param.get("riskNum");
		StringBuffer sbf = new StringBuffer();
		for (SqlRow row : sqlRows) {
			
			if ("other_risk".equals(wordValue.getWordKey())) {
				riskNum++;
				sbf.append("\n" + "\u3000" + "\u3000" + riskNum + "." + row.getString("val"));
			} else {
				if (sqlRows.size() > 1) {
					sbf.append("\n" + "\u3000" + "\u3000" + row.getString("val"));
				} else {
					sbf.append(row.getString("val"));
				}
			}

		}
		param.put("riskNum", riskNum);
		return sbf.toString();
	}
	
}
