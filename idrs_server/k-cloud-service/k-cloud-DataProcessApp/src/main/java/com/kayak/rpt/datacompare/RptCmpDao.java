package com.kayak.rpt.datacompare;

import com.kayak.base.dao.ComnDao;
import com.kayak.base.dao.DataSourceProperty;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.sql.UpdateResult;
import com.kayak.core.util.ExeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RptCmpDao {
    @Autowired
    public ComnDao comnDao;
    //获取比对配置
    public SqlRow getCfg(String table_name) throws Exception {
        String sql = "SELECT c.table_name, c.pk_field ,c.pk_name, c.cmp_field,c.cpm_name ,c.left_condition,c.right_condition,c.sqlstr,c.rt_cmp_field,c.coordinate_type FROM base_rpt_cpm_cfg c\n" +
                "left join app_table_info ati  on c.table_name =ati.system_table_name \n" +
                "WHERE (table_name=$S{table_name} or ati.id=$S{table_name}) and status='1'";

        HashMap query_params=  new HashMap();
        query_params.put("table_name",table_name);
        List<SqlRow> list=comnDao.findRows(sql, DataSourceProperty.PUB, query_params);
        if (list.size()==1){
            return  list.get(0);
        }else{
            throw new PromptException("请补充报表比对配置");
        }
    }
    public SqlResult<RptCmp> find(SqlParam<RptCmp> params) throws Exception {
        String  sqlStr= ExeQuery.queryExeId("CPMEQ04");
        params.setMakeSql(true);
        return comnDao.findRows(sqlStr, params);
    }

    /**
     * 获取需要导入的报送列表
     * @param params
     * @return
     * @throws Exception
     */
    public SqlResult<RptCmp> findTableName(SqlParam<RptCmp> params) throws Exception {
        String sql = "select info.id, info.system_table_name,CONCAT(info.system_table_name_cn,'-',info.system_table_name) as table_name from app_table_info info inner join base_rpt_cpm_cfg cfg on info.system_table_name = cfg.table_name ";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);
    }

    public UpdateResult createCmpLog (HashMap params) throws Exception {
        String inSql = "insert into base_rpt_cmp_log (table_name,upload_count,system_count,pk_match_count,all_match_count,not_match_count,report_date,create_date) \n" +
                "values ($S{table_name}, $U{upload_count},$U{system_count},$U{pk_match_count},$U{all_match_count},$U{not_match_count},$S{report_date}, date_format(sysdate(), '%Y%m%d'))";
        return comnDao.update(inSql, params);
    }

    public SqlResult<RptCmp> findCmpLog(SqlParam<RptCmp> params) throws Exception {
        String sql = "select upload_count,system_count,pk_match_count,all_match_count,not_match_count,report_date from base_rpt_cmp_log where id in\n" +
                "           ( select max(id) id from  base_rpt_cmp_log  where report_date = $S{reportDate} AND table_name = $S{tableName} )";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);
    }
    public String getUploadCount(HashMap params) throws Exception {
        String sql = "select count(1) as upload_count from $U{table_name} where sys_data_version='0' and report_date = $S{report_date}" ;
        return comnDao.findRows(sql, DataSourceProperty.PUB, params).get(0).getString("upload_count");
    }
    public String getUploadCount1(HashMap params) throws Exception {
        String sql = "select count(1) as upload_count from $U{table_name} " ;
        return comnDao.findRows(sql, DataSourceProperty.PUB, params).get(0).getString("upload_count");
    }
    /**
     * @methodName getUploadCount2
     * @description 查询身份信息数量
     * @param params  参数
     * @return java.lang.String
     */
    public String getUploadCount2(HashMap params) throws Exception {
        String sql = "select count(1) as upload_count from $U{table_name} where deal_date = $S{deal_date} and data_status != '-2' and data_status != '-7'";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params).get(0).getString("upload_count");
    }

    public String getMaxVersion(HashMap params) throws Exception {
        String sql = "select max(sys_data_version) as version from $U{table_name} where sys_data_status ='1' and report_date = $S{report_date}" ;
        return comnDao.findRows(sql, DataSourceProperty.PUB, params).get(0).getString("version");
    }

    /**
     * 根据报送日期查询数据日期
     * @param dParams
     * @return
     * @throws Exception
     */
    public String checkDataDate (Map<String, Object> dParams) throws Exception {
        String report_date = (String)dParams.get("dealDate");
        String checkSql = ExeQuery.queryExeId("checkeu002");
        SqlRow row = comnDao.findRow(checkSql, dParams);
        if(row != null) report_date = row.getString("report_date");
        return report_date;
    }

    public List<SqlRow> findByMap(String sql, Map params) throws Exception {
        sql += " where a.table_name = $S{tableName} and a.report_date = $S{reportDate}";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);
    }

    public List<SqlRow> findPkNameByMap(Map params) throws Exception {
        String sql = "select pk_name from base_rpt_cpm_cfg where table_name =$S{tableName}";
        return comnDao.findRows(sql, DataSourceProperty.PUB, params);
    }


}
