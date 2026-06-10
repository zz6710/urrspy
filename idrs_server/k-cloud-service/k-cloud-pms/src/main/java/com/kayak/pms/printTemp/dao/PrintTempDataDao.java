package com.kayak.pms.printTemp.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.pms.printTemp.model.PrintTempData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: k-cloud
 * @description: 文档模板数据源Dao
 * @author: WangZhenXin
 * @create: 2020-12-29 09:05
 * @memo 备注信息
 */
@Repository
public class PrintTempDataDao extends ComnDao {
    private static final Logger logger = LoggerFactory.getLogger(PrintTempDataDao.class);

    public SqlResult<PrintTempData> getPrintTempDataList(SqlParam<PrintTempData> param) throws Exception {
        return super.findRows("select t.id, t.doc_type, t.temp_type, t.data_type, t.data_group_name, t.sql_info,xp_doc_type,is_xp_data, t.create_date,t.crt_time,t.update_date,t.upd_time " +
                "from t8_print_temp_data t ", param);
    }

    /**
     * 校验数据类型为map的数据源是否已存在
     *
     * @param printTempData 文档数据源对象
     * @return count
     */
    public Integer getPrintTempDataMapCount(PrintTempData printTempData) throws Exception {
        String sql = "select count(1) con from t8_print_temp_data t where t.data_type= $S{dataType}";
        if(printTempData.getIsXpData()!=null&&printTempData.getIsXpData()!=""){
            sql = sql + " and is_xp_data=$S{isXpData}";
        }
        if(printTempData.getXpDocType()!=null&&printTempData.getXpDocType()!=""){
            sql = sql + " and xp_doc_type=$S{xpDocType}";
        }
        SqlRow sqlRow = super.findRow( sql, printTempData);
        return sqlRow.getInteger("con");
    }

    public int addPrintTempData(PrintTempData printTempData) throws Exception {
        AtomicInteger effect = new AtomicInteger();
        doTrans(() -> {
            effect.set(super.update("insert into t8_print_temp_data(id, doc_type, temp_type, data_type, data_group_name, sql_info,xp_doc_type,is_xp_data, update_date, create_date,crt_time, upd_time, crt_user, upd_user) " +
                    "VALUES ($AUTOIDS{id}, $S{docType}, $S{tempType}, $S{dataType}, $S{dataGroupName}, $S{sqlInfo},$S{xpDocType},$S{isXpData}, $S{updateDate}, $S{createDate}, $S{crtTime}, $S{updTime}, $S{crtUser}, $S{updUser})", printTempData).getEffect());
        });
        return effect.get();
    }

    public int updatePrintTempData(PrintTempData printTempData) throws Exception {
        AtomicInteger effect = new AtomicInteger();
        doTrans(() -> {
            effect.set(super.update("update t8_print_temp_data t " +
                    "set t.data_group_name =$S{dataGroupName}, t.sql_info=$S{sqlInfo},t.update_date=$S{updateDate},t.xp_doc_type=$S{xpDocType},t.is_xp_data=$S{isXpData},t.upd_time=$S{updTime},t.upd_user=$S{updUser} " +
                    "where t.id=$S{id}", printTempData).getEffect());
        });
        return effect.get();
    }

    public List<PrintTempData> getPrintTempDataByVersionId(String versionId) throws Exception {
        List<SqlRow> sqlRowList = this.findRows("select t.id,  " +
                "       t.doc_type,  " +
                "       t.temp_type,  " +
                "       t.data_type,  " +
                "       t.data_group_name,  " +
                "       t.sql_info  " +
                "from t8_print_temp_data t  " +
                "where t.data_type = '1' AND t.is_xp_data='0'", versionId);
        ArrayList<PrintTempData> printTempDataArrayList = new ArrayList<>();
        for (SqlRow sqlRow : sqlRowList) {
            PrintTempData printTempData = new PrintTempData();
            printTempData.setId(sqlRow.getString("id"));
            printTempData.setDocType(sqlRow.getString("doc_type"));
            printTempData.setTempType(sqlRow.getString("temp_type"));
            printTempData.setDataType(sqlRow.getString("data_type"));
            printTempData.setDataGroupName(sqlRow.getString("data_group_name"));
            printTempData.setSqlInfo(sqlRow.getString("sql_info"));
            printTempDataArrayList.add(printTempData);
        }
        return printTempDataArrayList;
    }


    /**
     * 组装Map参数数据源
     * @param printTempDataList 数据源列表
     * @param params 查询所需参数集合
     * @return List<Map<String,Object>>
     * @throws Exception e
     */
    public List<Map<String,Object>> getPrintTempDataInfoToMap(List<PrintTempData> printTempDataList,Map<String,Object> params) throws Exception{
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (PrintTempData printTempData : printTempDataList) {
            //如果数据源类型是map
            if ("1".equals(printTempData.getDataType())){
                //如果配置了文档模板数据源
              if(StringUtils.isNotBlank(printTempData.getSqlInfo())) {
                  //获取sql传参后返回的产品数据
                  List<SqlRow> sql_info = this.findRows(printTempData.getSqlInfo(), params);
                  if(sql_info.size()>0) {
                      SqlRow row = sql_info.get(0);
                      Map<String,Object> map = new HashMap<>();
                      for (Map.Entry<String, Object> entry : row.entrySet()) {
                          map.put(entry.getKey(),entry.getValue());
                      }
                      list.add(map);
                  }
              }
            }
        }
        return list;
    }


    /**
     * 组装List参数数据源
     * @param printTempDataList 数据源列表
     * @param params 查询所需参数集合
     * @return  List<Map<String,List<Map<String,Object>>>>
     * @throws Exception e
     */
    public List<Map<String,List<Map<String,Object>>>> getPrintTempDataInfoToList(List<PrintTempData> printTempDataList,Map<String,Object> params) throws Exception{
        List<Map<String,List<Map<String,Object>>>> groupList = new ArrayList<Map<String,List<Map<String,Object>>>>();
        for (PrintTempData printTempData : printTempDataList) {
            if ("2".equals(printTempData.getDataType())){
                Map<String,List<Map<String,Object>>> groupMap = new HashMap<String,List<Map<String,Object>>>();
                List<SqlRow> sqlRows =  this.findRows(printTempData.getSqlInfo(),params);
                List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
                for(SqlRow sqlRow :sqlRows){
                    Map<String,Object> map = new HashMap<String, Object>();
                    for (Map.Entry<String, Object> entry : sqlRow.entrySet()) {
                        map.put(entry.getKey(),entry.getValue());
                    }
                    list.add(map);
                }
                groupMap.put(printTempData.getDataGroupName(),list);
                groupList.add(groupMap);
            }
        }
        return groupList;
    }
}
