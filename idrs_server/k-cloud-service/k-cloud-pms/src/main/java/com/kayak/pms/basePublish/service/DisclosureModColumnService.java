package com.kayak.pms.basePublish.service;

import cn.hutool.core.collection.CollectionUtil;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.cache.impl.MemoryCache;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.SysUtil;
import com.kayak.pms.basePublish.dao.DisclosureModColumnDao;
import com.kayak.pms.basePublish.dao.DisclosureWordDateDao;
import com.kayak.pms.basePublish.enums.FunctypeEnum;
import com.kayak.pms.basePublish.model.DisclosureModColumn;
import com.kayak.pms.basePublish.model.DisclosureWordDate;
import com.kayak.utils.DateHelper;
import com.kayak.utils.CamelCaseMapUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
@APIDefine(desc = "信披模板源字段服务", model = DisclosureModColumn.class)
public class DisclosureModColumnService {
	private static final Logger log = LoggerFactory.getLogger(MemoryCache.class);
    @Autowired
    private DisclosureModColumnDao disclosureModColumnDao;

    @Autowired
    private DisclosureWordDateDao wordDateDao;

    @API(desc = "修改信披模板源字段", params = "id,disclosure_mod_version_id,t8_disclosure_source_id,column_label,column_key,column_value,isdisplay,roleids,is_sysvalue,sqls,crt_date,crt_time,crt_user_id,crt_user_name,upd_date,upd_time,upd_user_id,upd_user_name,remark", auth = APIAuth.NO)
    public String updateDisclosureModColumn(SqlParam<DisclosureWordDate> params) throws Exception {
        String jsonData = params.getModel().getJsonData();
        JSONObject _json = new JSONObject(jsonData);
        Map<String, Object> map = _json.toMap();
        if (map != null) {
            //保存补录数据
            DaoUtil.doTrans(() -> {
                String date = DateHelper.getCurrentDate();
                String time = DateHelper.getCurrentTime();
                String userId = SysUtil.getSysUserParamValue("sys_user_userid").toString();
				map.forEach((key, value) ->{
					DisclosureWordDate disclosureWordDate = new DisclosureWordDate();
					disclosureWordDate.setColumnKey(key);
					disclosureWordDate.setColumnValue((String)value);
					disclosureWordDate.setT8DisclosureNoticeId(params.getModel().getT8DisclosureNoticeId());
					disclosureWordDate.setCrtDate(date);
					disclosureWordDate.setCrtTime(time);
					disclosureWordDate.setCrtUser(userId);
					try {
						wordDateDao.insert(disclosureWordDate);
					} catch (Exception e) {
						e.printStackTrace();
                    }
                });
            });
        }
        return RequestSupport.updateReturnJson(true, "操作成功!!", null).toString();
    }

    public int addModColumnList(List<DisclosureModColumn> list) throws Exception {
        int flag = 0;
        for (DisclosureModColumn disclosureModColumn : list) {
			if(disclosureModColumn.getColumnKey().contains("span>")){
				String str = disclosureModColumn.getColumnKey();
				log.info("模板占位符=:>>"+str);
				//int start = str.indexOf("s3\">");
				//int end = str.lastIndexOf("</span>");
				//str=str.substring(start+4,end);
				str = str.replace("</span>","");
				str = str.replace("<span>","");
				str = str.replace("<span class=\"s3\">","");
				str = str.replace("</a>","");
				str = str.replace("<a name=\"_GoBack\">","");
				disclosureModColumn.setColumnKey(str);
			}
            flag += disclosureModColumnDao.addDisclosureModColumn(disclosureModColumn);
        }
	        return flag;
	}
	/**
	* @功能描述:模板版本预览查询字段数据
	* @params:[param]
	* @return:com.kayak.core.sql.SqlResult<com.kayak.pms.basePublish.model.DisclosureModColumn>
	* @Athor:ouyifan
	* @date:2022/6/20
	*/
	 @API(desc = "根据模板Id获取最大Id数据",auth = APIAuth.NO,operation = APIOperation.SELECT)
	    public SqlResult<DisclosureModColumn> getMaxXPVersionId(SqlParam<DisclosureModColumn> param) throws Exception {
		    DisclosureModColumn disclosureModColumn = disclosureModColumnDao.getMaxXPVersionId(param.getModel().getT8DisclosureVersionId());
	        SqlResult<DisclosureModColumn> sqlResult = new SqlResult<>();
	        ArrayList<DisclosureModColumn> list = new ArrayList<>();
	        list.add(disclosureModColumn);
	        sqlResult.setRows(list);
	        sqlResult.setResults(list.size());
	        sqlResult.setDesensitized(false);
	        return sqlResult;
	    }
	 @API(desc = "根据模板Id获取column数据",auth = APIAuth.NO,operation = APIOperation.SELECT)
	    public SqlResult<DisclosureModColumn> geXPbyModId(SqlParam<DisclosureModColumn> param) throws Exception {
		    List<DisclosureModColumn> listModColums = disclosureModColumnDao.geXPbyModId(param.getModel().getDisclosureModVersionId());
	        SqlResult<DisclosureModColumn> sqlResult = new SqlResult<>();
	        sqlResult.setRows(listModColums);
	        sqlResult.setResults(listModColums.size());
	        sqlResult.setDesensitized(false);
	        return sqlResult;
	    }

	@API(desc = "查询通用补录界面", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>> findSupplementaryRecord(SqlParam<DisclosureModColumn> params) throws Exception {
		SqlResult<Map<String, Object>> sqlResult = null;
		SqlResult<SqlRow> supplementaryRecord = disclosureModColumnDao.findSupplementaryRecord(params);
		if (supplementaryRecord.getRows() != null && supplementaryRecord.getRows().size() > 0) {
			List<SqlRow> rows = supplementaryRecord.getRows();
			rows.forEach(sqlRow -> {
				String functype = sqlRow.getString("functype");
				FunctypeEnum functypeEnum = null;
				//将数据字段类型转换为具体的组件类型
				switch (functype) {
					case "varchar":
						functypeEnum = FunctypeEnum.FUNCTYPE_VARCHAR;
						break;
					case "int":
						functypeEnum = FunctypeEnum.FUNCTYPE_INT;
						break;
					case "number":
						functypeEnum = FunctypeEnum.FUNCTYPE_NUMBER;
						break;
					case "select":
						functypeEnum = FunctypeEnum.FUNCTYPE_SELECT;
						break;
					case "mselect":
						functypeEnum = FunctypeEnum.FUNCTYPE_MSELECT;
						break;
					case "date":
						functypeEnum = FunctypeEnum.FUNCTYPE_DATE;
						break;
					case "time":
						functypeEnum = FunctypeEnum.FUNCTYPE_TIIME;
						break;
					default:
						functypeEnum = FunctypeEnum.FUNCTYPE_VARCHAR;
						break;
				}
				sqlRow.put("functype", functypeEnum.getComponentName());
			});
		}
		//将查询sql结果转换为驼峰命名
		if (supplementaryRecord.getRows() != null) {
			sqlResult = CamelCaseMapUtils.CamelCaseSqlRow(supplementaryRecord.getRows());
		}
		return sqlResult;
	}

	/**
	 * 查询公告详情对应模板维护字段(自动及手工维护)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@API(desc = "查询数据补录字段", auth = APIAuth.NO,operation = APIOperation.UPDATE)
	public SqlResult<Map<String, Object>> findSupplementaryRecordForDisclosureInfo(SqlParam<DisclosureModColumn> params) throws Exception {
		SqlResult<Map<String, Object>> sqlResult = null;
		SqlResult<SqlRow> supplementaryRecord = disclosureModColumnDao.findSupplementaryRecordForDisclosureInfo(params);
		if (supplementaryRecord != null && supplementaryRecord.getRows() != null && supplementaryRecord.getRows().size() > 0) {
			List<SqlRow> rows = supplementaryRecord.getRows();
			//20220125 将格式化小数位数
			formatDigits(rows);
			rows.forEach(sqlRow -> {
				String functype = sqlRow.getString("functype");
				FunctypeEnum functypeEnum = null;
				//将数据字段类型转换为具体的组件类型
				switch (functype) {
					case "varchar":
						functypeEnum = FunctypeEnum.FUNCTYPE_VARCHAR;
						break;
					case "int":
						functypeEnum = FunctypeEnum.FUNCTYPE_INT;
						break;
					case "number":
						functypeEnum = FunctypeEnum.FUNCTYPE_NUMBER;
						break;
					case "select":
						functypeEnum = FunctypeEnum.FUNCTYPE_SELECT;
						break;
					case "mselect":
						functypeEnum = FunctypeEnum.FUNCTYPE_MSELECT;
						break;
					case "date":
						functypeEnum = FunctypeEnum.FUNCTYPE_DATE;
						break;
					case "time":
						functypeEnum = FunctypeEnum.FUNCTYPE_TIIME;
						break;
					default:
						functypeEnum = FunctypeEnum.FUNCTYPE_VARCHAR;
						break;
				}
                sqlRow.put("functype", functypeEnum.getComponentName());
            });
        }
        //将查询sql结果转换为驼峰命名
        if (supplementaryRecord != null && supplementaryRecord.getRows() != null && supplementaryRecord.getRows().size() > 0) {
            sqlResult = CamelCaseMapUtils.CamelCaseSqlRow(supplementaryRecord.getRows());
        }else{
			List<SqlRow> objects = new ArrayList<>();
			sqlResult = CamelCaseMapUtils.CamelCaseSqlRow(objects);
		}
        return sqlResult;
    }

	private void formatDigits(List<SqlRow> rows) {
		if(CollectionUtil.isNotEmpty(rows)){
			DecimalFormat df = new DecimalFormat("#0.00");
			for (SqlRow row : rows) {
				//份额净值 或者份额累计净值 为4位  其余number为2位 只需要处理处理2位逻辑
				Object columnKey = row.get("column_key");
				Object columnValue = row.get("column_value");
				Object dataType = row.get("data_type");
				if(columnKey!=null&& StringUtils.isNotEmpty(columnKey.toString())&&columnValue!=null&&StringUtils.isNotEmpty(columnValue.toString())&&dataType!=null&&"number".equals(dataType.toString())){
					if(("netval".equals(columnKey)||"netval_total".equals(columnKey)||"netval_end".equals(columnKey)||"netval_end".equals(columnKey)||"netval_end".equals(columnKey))){
						//这里格式化为四位
					}else {
						//这里格式化为两位
						BigDecimal bigDecimal = new BigDecimal(columnValue.toString());
						row.put("column_value",df.format(bigDecimal));
					}

				}
			}
		}
	}

	@API(desc = "查询非标准投资情况", auth = APIAuth.NO)
	public SqlResult<Map<String, Object>> findNonStandardDesc(SqlParam<DisclosureModColumn> params) throws Exception {
		SqlResult<Map<String, Object>> sqlResult = null;
		SqlResult<SqlRow> supplementaryRecord = disclosureModColumnDao.findNonStandardDesc(params);
		
        //将查询sql结果转换为驼峰命名
        if (supplementaryRecord.getRows() != null) {
            sqlResult = CamelCaseMapUtils.CamelCaseSqlRow(supplementaryRecord.getRows());
        }
        return sqlResult;
    }

	@API(desc = "根据角色查询是否有编辑权限", auth = APIAuth.NO)
	public SqlResult<DisclosureModColumn> isEditByRoleId(SqlParam<DisclosureModColumn> params) throws Exception {
		return disclosureModColumnDao.isEdit(params);
	}
}
