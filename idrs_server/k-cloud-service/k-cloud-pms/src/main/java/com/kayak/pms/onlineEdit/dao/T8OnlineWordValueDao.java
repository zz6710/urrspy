package com.kayak.pms.onlineEdit.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.onlineEdit.model.T8OnlineWordValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: k-cloud
 * @description: 文档在线编辑数据Dao
 * @author: WangZhenXin
 * @create: 2021-01-29 19:06
 * @memo 备注信息
 */
@Repository
public class T8OnlineWordValueDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(T8OnlineWordValueDao.class);

    public int initT8OnlineWordValue(T8OnlineWordValue t8OnlineWordValue) throws Exception {
        return super.update("insert into t8_online_word_value(id, word_key, word_value, word_comment, dict,t8_prod_document_version_id, " +
                "                                 t8_print_temp_version_id, " +
                "                                 is_disabled, file_name, view_url, upload_path) " +
                "values ($AUTOIDS{t8_online_word_value}, $S{wordKey}, $S{wordValue}, " +
                "        (select (IF(max(id) is null, $S{wordKey} , column_comment)) column_comment " +
                "         from t8_online_word_table_columns " +
                "         where column_name = $S{wordKey})," +
                "   (select max(dict) dict from t8_online_word_table_columns where column_name = $S{wordKey}) ," +
                "       $S{t8ProdDocumentVersionId}, " +
                "        $S{t8PrintTempVersionId}, " +
                "        (select (IF(max(id) is null, 1, is_disabled)) is_disabled " +
                "         from t8_online_word_table_columns " +
                "         where column_name = $S{wordKey}), $S{fileName}, $S{viewUrl}, $S{uploadPath})", t8OnlineWordValue).getEffect();
    }

    public List<T8OnlineWordValue> getT8OnlineWordValueListByT8ProdDocumentVersionId(String t8ProdDocumentVersionId) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       t.word_value,  " +
                "       t.word_comment, t.dict," +
                "       t.t8_prod_document_version_id, " +
                "       t.t8_print_temp_version_id, " +
                "       t.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path " +
                "from t8_online_word_value t " +
                "where t.t8_prod_document_version_id = $S{t8ProdDocumentVersionId}";
        return super.findRows(T8OnlineWordValue.class, sql, 0, t8ProdDocumentVersionId);
    }

    public List<T8OnlineWordValue> getT8OnlineWordValueListByT8PrintTempVersionId(String t8PrintTempVersionId) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       IF(t.word_value = '',t1.default_value,t.word_value) word_value,  " +
                "       t1.column_comment word_comment, t1.dict," +
                "       t.t8_prod_document_version_id, " +
                "       t.t8_print_temp_version_id, " +
                "       t1.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path, " +
                "       t1.default_value, " +
                "       t1.empty_default_val, " +
                "       t1.data_digits, " +
                "       t1.data_type, " +
                "       t1.sql_info, " +
                "       t1.column_name " +
                "from t8_online_word_value t left join t8_online_word_table_columns t1 on t.word_key = t1.doc_column " +
                "where t.t8_print_temp_version_id = $S{t8PrintTempVersionId} and t.t8_prod_document_version_id = ''";
        return super.findRows(T8OnlineWordValue.class, sql, 0, t8PrintTempVersionId);
    }

    public SqlResult<T8OnlineWordValue> getT8OnlineWordValueList(SqlParam<T8OnlineWordValue> param) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       \tIF(((select c.DEFAULT_VALUE from t8_online_word_table_columns c where c.doc_column=t.word_key) != '' and t.word_value = '') or t.word_value = 'null','  ',t.word_value)  word_value, " +
                "       t.word_comment, t.dict," +
                "       t.t8_prod_document_version_id, " +
                "       t.t8_print_temp_version_id, " +
                "       t.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path " +
                "from t8_online_word_value t " +
                "where t.t8_prod_document_version_id = $S{t8ProdDocumentVersionId}" +
                " or  t.t8_print_temp_version_id = $S{t8PrintTempVersionId}";
        return super.findRows(sql, param);
    }



    public int addT8OnlineWordValue(T8OnlineWordValue t8OnlineWordValue) throws Exception {
        return super.update("insert into t8_online_word_value(id, word_key, word_value, word_comment,dict, t8_print_temp_version_id, " +
                "                                 t8_prod_document_version_id, is_disabled, " +
                "                                 file_name, view_url, upload_path) " +
                "VALUES ($AUTOIDS{t8_online_word_value}, $S{wordKey}, $S{wordValue}, " +
                "        $S{wordComment},$S{dict},$S{t8PrintTempVersionId}, " +
                "        $S{t8ProdDocumentVersionId}, " +
                "        $S{isDisabled}, $S{fileName}, $S{viewUrl}, $S{uploadPath})", t8OnlineWordValue).getEffect();
    }

    public T8OnlineWordValue getMaxT8OnlineWordValueByT8PrintTempVersionId(String t8PrintTempVersionId) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       IF(t.word_value = '',(select c.DEFAULT_VALUE from t8_online_word_table_columns c where c.doc_column=t.word_key),t.word_value) word_value,  " +
                "       t.word_comment, t.dict," +
                "       t.t8_print_temp_version_id, " +
                "       t.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path, " +
                "       t.t8_prod_document_version_id " +
                "from t8_online_word_value t " +
                "where t.id = (select max(t.id) " +
                "              from t8_online_word_value t " +
                "              where t.t8_print_temp_version_id = $S{t8PrintTempVersionId})";
        return super.findRow(T8OnlineWordValue.class, sql, 0, t8PrintTempVersionId);
    }

    public T8OnlineWordValue getMaxT8OnlineWordValueByProcessInstanceId(String processInstanceId) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       IF(t.word_value = '',(select c.DEFAULT_VALUE from t8_online_word_table_columns c where c.doc_column=t.word_key),t.word_value) word_value,  " +
                "       t.word_comment, t.dict," +
                "       t.t8_print_temp_version_id, " +
                "       t.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path, " +
                "       t.t8_prod_document_version_id " +
                "from t8_online_word_value t " +
                "left join t8_print_temp_version t1 on t.t8_print_temp_version_id = t1.id " +
                "where t1.process_instance_id = $S{processInstanceId} ";
        return super.findRow(T8OnlineWordValue.class, sql, 0, processInstanceId);
    }


    public List<SqlRow> getMaxT8OnlineWordValues(String documentVersionId) throws Exception {
        String sql = "select t.id, " +
                "       t.word_key, " +
                "       t.word_value, " +
                "       t.word_comment, t.dict," +
                "       t.t8_print_temp_version_id, " +
                "       t.is_disabled, " +
                "       t.file_name, " +
                "       t.view_url, " +
                "       t.upload_path, " +
                "       t.t8_prod_document_version_id " +
                " from t8_online_word_value t " +

                " where  t.t8_prod_document_version_id = '"+documentVersionId+"'";
        return super.findRows(sql);
    }

	public SqlRow getDistributorInfo(String prodCode)throws Exception  {
		// TODO Auto-generated method stub
		return super.findRow("	  SELECT (select GROUP_CONCAT(distributor_name order by distributor_code asc separator '@') type from t8_distributor_info where FIND_IN_SET(distributor_code,t4.distributor_code)) distributor/*产品销售商*/,\r\n" + 
				"    (select GROUP_CONCAT(official_website order by distributor_code  asc separator '@') type from t8_distributor_info where FIND_IN_SET(distributor_code,t4.distributor_code)) official_website/*产品销售商官网*/,\r\n" + 
				"    (select GROUP_CONCAT(main_duty order by distributor_code asc separator '@') type from t8_distributor_info where FIND_IN_SET(distributor_code,t4.distributor_code)) main_duty/*产品销售商职责*/,\r\n" + 
				"    (select GROUP_CONCAT(customer_service_hotline order by distributor_code asc separator '@') type from t8_distributor_info where FIND_IN_SET(distributor_code,t4.distributor_code)) customer_service_hotline/*产品销售客服热线*/,\r\n" + 
				"    (select GROUP_CONCAT(ADDRESS order by distributor_code asc separator '@') type from t8_distributor_info where FIND_IN_SET(distributor_code,t4.distributor_code)) distributor_address/*产品销售商住所*/ from t8_prod_info t\r\n" + 
				" left join t8_prod_sale t4 on t4.t8_prod_info_id = t.id where t.prod_code = $S{prodCode} ", prodCode);
	}

	public SqlRow findDataInfo(T8OnlineWordValue T8OnlineWordValue) throws Exception{
		String sql = "SELECT * FROM t8_print_temp_data tptd WHERE is_xp_data='0' ";
		SqlRow sqlRow = super.findRow(sql, T8OnlineWordValue);
		SqlRow mapSqlResult = null;
		if (sqlRow != null) {
			String sqlInfo = sqlRow.getString("sql_info");
			mapSqlResult = super.findRow(sqlInfo, T8OnlineWordValue);
		}
		return mapSqlResult;

	}
	
	public List<SqlRow> findColumnData() throws Exception{
		String sql = "SELECT * FROM t8_online_word_table_columns ";
		List<SqlRow> sqlRows = super.findRows(sql, 0);		
		return sqlRows;

	}
	
	
	public SqlRow findDictVal(String dict,String key) throws Exception{
		Map<String,Object> param = new HashMap();
		param.put("dict", dict);
		param.put("key",key);
		String sql = "SELECT itemval from sys_dict_item where dict = $S{dict}  and itemkey=$S{key}";
		SqlRow sqlRow = super.findRow(sql, param);
		return sqlRow;

	}
	
	
	public List<SqlRow> execSql(String Sql,T8OnlineWordValue T8OnlineWordValue) throws Exception{
		
		return super.findRows(Sql, T8OnlineWordValue);

	}

}
