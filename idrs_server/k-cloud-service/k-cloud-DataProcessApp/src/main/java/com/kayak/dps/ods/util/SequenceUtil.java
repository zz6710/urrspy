package com.kayak.dps.ods.util;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;

import java.util.List;

public class SequenceUtil {

    /**
     * 获取sys_sequence表中对应名字的序列
     * @param tableName
     * @param totalLength
     * @return
     * @throws Exception
     */
    public static synchronized String getSequence(String tableName, int totalLength) throws Exception{
        StringBuffer sb = new StringBuffer();
        ComnDao dao = SysBeans.getBean("comnDao");
        String selectSql = "select t.maxid from sys_sequence t where t.tablename='" + tableName + "'";
        List<SqlRow> rs = dao.findRows(selectSql, "dsSys");
        if(rs.size()>0){
            for (SqlRow sqlRow : rs) {
                String curValue = String.valueOf(sqlRow.getInteger("maxid"));
                sb.append(getSomeStr(curValue, totalLength, "0")).append(curValue);
                //判断当前长度是否已经是最大值
                String maxValue = getSomeStr("", totalLength, "9");
                String updateSql = "";
                if (maxValue.equals(curValue)) {
                    updateSql = "update sys_sequence t set t.maxid=1 where t.tablename='" + tableName + "'";
                } else {
                    updateSql = "update sys_sequence t set t.maxid=t.maxid+1 where t.tablename='" + tableName + "'";
                }
                dao.update(updateSql);
            }
        }else{
            sb.append(getSomeStr("1", totalLength, "0")).append("1");
            String insertSql = "insert into sys_sequence values ('" + tableName + "',2)";
            dao.update(insertSql);
        }
        return sb.toString();
    }

    /**
     * 获取相应个数的某个字符串
     * @param curValue
     * @param totalLength
     * @return
     */
    public static String getSomeStr(String curValue, int totalLength, String appendStr){
        StringBuffer sb = new StringBuffer();
        if(totalLength > 0) {
            int curLength = curValue.length();
            if(totalLength - curLength > 0) {
                for(int i = 0; i < (totalLength - curLength); i ++) {
                    sb.append(appendStr);
                }
            }
        }
        return sb.toString();
    }
}
