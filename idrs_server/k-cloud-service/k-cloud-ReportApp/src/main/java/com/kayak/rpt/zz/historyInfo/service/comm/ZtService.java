package com.kayak.rpt.zz.historyInfo.service.comm;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.linuxense.javadbf.DBFException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RefreshScope
public class ZtService {

    @Autowired
    private ZtDao ztDao;
    @Autowired
    public ComnDao comnDao;

    @Value("${zt.api.holdJoinFlag}")
    private Boolean holdJoinFlag;//持仓数据拼接标志

    public String getInsertSQl(String port_code) throws Exception {
        // 解析入库字段，拼接SQL
        List<String> fieldList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        StringBuffer vars = new StringBuffer();

        Map<String,Object> params = new HashMap<>();
        params.put("port_code",port_code);
        List<SqlRow> rs = ztDao.queryFieldList(params);
        String  tableName=rs.get(0).getString("port_table");
        sb.append(" insert into ").append(tableName).append(" ( ");
        boolean flag = false;
        List<String> fieldType = new ArrayList<>();
        // 每个表表字段字数
        int size = 0;
        for (SqlRow sqlRow : rs) {
            // 拼接逗号
            if (flag) {
                sb.append(",");
                vars.append(",");
            } else {
                flag = true;
            }
            size++;
            // 字段
            sb.append(sqlRow.getString("field_code"));
            fieldType.add(sqlRow.getString("field_type"));
            // 占位变量
            vars.append("?");

            // 字段名
            fieldList.add(sqlRow.getString("field_code"));
        }
        sb.append(" ) values ( ").append(vars).append(" )");
        log.info(" 入库SQL："+sb);
        if(CollectionUtils.isEmpty(fieldList)){
            throw new DBFException("未配置字段信息");
        }
        return sb.toString();
    }

    /**
     * 中台投资者历史数据入库处理
     * @param port_code
     * @param pageParam
     * @param is_del
     * @param contentArray
     * @throws Exception
     */
    public void batchSave(String port_code,Map<String, Object> pageParam, boolean is_del, JSONArray contentArray) throws Exception {
        String batchSql =getInsertSQl(port_code);
        Map<String,Object> params = new HashMap<>();
        params.put("port_code",port_code);
        Boolean genFlag=false;//是否需要每日持仓
        if(("api_app_cust_vol_register_info".equalsIgnoreCase(port_code) || "api_app_cust_vol_register_sub_info".equals(port_code))
                && pageParam.get("custNo")!=null && StringUtils.isNotEmpty("custNo")) {//需要生成每日持仓数据
            genFlag=true;
        }
        List<SqlRow> rs = ztDao.queryFieldList(params);
        long startTime = System.currentTimeMillis();
        Boolean finalGenFlag = genFlag;
        comnDao.doTrans(() -> {
            Connection connection = comnDao.getConnection();
            PreparedStatement ps  = connection.prepareStatement(batchSql);
            try {
                String strt_dt="";
                String end_dt="";
                if (is_del) {//确认标识为true时删除
                    if(pageParam.get("order_id") != null){
                        ztDao.deleteTable(rs.get(0).getString("port_table"), String.valueOf(pageParam.get("order_id")));//清空当前历史表,目前仅用于中台接口查询投资者历史数据
                    } else {
                        ztDao.clearTable(rs.get(0).getString("port_table"));//清空当前历史表,目前仅用于中台接口查询投资者历史数据
                    }
                }

                for (int i=0;i<contentArray.length();i++){
                    JSONObject content =(JSONObject) contentArray.get(i);
                    Map<String, Object> jsonMap = Tools.json2map(content);
                    //中台接口下载固定参数
                    jsonMap.put("order_id(指令编号)", pageParam.get("order_id"));
                    jsonMap.put("mrg_typ(合并状态)", "9");
                    if(finalGenFlag && holdJoinFlag){//是否需要每日持仓
                        DateTimeFormatter informatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        DateTimeFormatter outformatter=DateTimeFormatter.ofPattern("yyyyMMdd");
                        strt_dt= String.valueOf(jsonMap.get("strt_dt(开始日期)"));
                        end_dt= String.valueOf(jsonMap.get("end_dt(结束日期)"));
                        LocalDate dt_strt_dt=LocalDate.parse(strt_dt,informatter);
                        LocalDate dt_end_dt=LocalDate.parse(end_dt,informatter);
                        if("9999-12-31".equals(end_dt)){
                            String sysDate = DateUtil.getNowDate();//系统工作日
                            dt_end_dt=LocalDate.parse(sysDate,outformatter);
                        }
                        while(dt_strt_dt.isBefore(dt_end_dt)){
                            for (int j=0;j<rs.size();j++) {
                                SqlRow sqlRow=rs.get(j);
                                String file_field_code = sqlRow.getString("file_field_code");
                                String field_code = sqlRow.getString("field_code");
                                Object value=jsonMap.get(file_field_code);
                                if("hold_date".equalsIgnoreCase(field_code) || "report_date".equalsIgnoreCase(field_code) ){
                                    value=dt_strt_dt.format(outformatter);
                                }
                                ps.setObject(j+1,value);
                            }
                            ps.addBatch();
                            dt_strt_dt=dt_strt_dt.plusDays(1);
                        }

                    }else{
                        for (int j=0;j<rs.size();j++) {
                            SqlRow sqlRow=rs.get(j);
                            String file_field_code = sqlRow.getString("file_field_code");
                            ps.setObject(j+1,jsonMap.get(file_field_code));
                        }
                        log.info(ps.toString());
                        ps.addBatch();
                    }

                }
                ps.executeBatch();

                log.info(" ##### 批量入库{}耗时: {} ms", contentArray.length(),System.currentTimeMillis() - startTime);
            }catch (Exception e) {
                log.error("导入投资者三期数据异常!", e);
                throw new Exception(e.getMessage());
            } finally{
                ps.close();

            }
        });
    }

}
