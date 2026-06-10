package com.kayak.pms.disclosureControl.util;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.kayak.schedule.utils
 * user:rennannan
 * date:2021/5/28 17:35
 * function:数据同步工具类
 */
public class SynDataUtil {
    @Autowired
    private ComnDao comnDao;
    private static final Logger logger = LoggerFactory.getLogger(SynDataUtil.class);



    /**
     * 将object转换为String 避免空报错
     *
     * @param object
     * @return
     */
    public static String objToStr(Object object) {
        String result = "";
        if (object != null) {
            result = String.valueOf(object);
        }
        return result;
    }

    /**
     * 得到新的字符串
     *
     * @param lists        查询的数据结果集
     * @param tableCloumns 字段信息
     * @return values (x1,y1),(x2,y2)
     */
    public static void getNew(String updateTimeStamp,StringBuffer stringBuffer,List<SqlRow> lists, String tableCloumns) {
        String[] ss = tableCloumns.split(",");
        //StringBuffer stringBuffer = new StringBuffer();
        SqlRow sqlRow;
        String data;
        String s;
        for (int a = 0; a < lists.size(); a++) {
            sqlRow = lists.get(a);

            data = "(";
            for (int i = 0; i < ss.length; i++) {
                s = ss[i].trim();
                if (i == ss.length - 1) {
                    data = data + "'" + objToStr(sqlRow.get(s)) +"','"+updateTimeStamp+ "')";
                   
                } else {
                	
                    data = data + "'" + objToStr(sqlRow.get(s)) + "',";
                   
                }
            }
            if (a == lists.size() - 1) {
                stringBuffer.append(data);
            } else {
                stringBuffer.append(data + ",");
            }
        }

        //return stringBuffer;
    }

    /**
     * 查询条件转换判断 条件示例：$S{prod_code}='ABC',$S{date}>'20210331'
     *
     * @param condition
     * @param param
     * @return
     */
    public static boolean compareUtil(String condition, Map<String, Object> param) {
        boolean isOk = true;
        String booleans = "";
        if (condition.isEmpty()) {
            isOk = true;
        } else {
            if (condition.contains(",")) {
                String[] cc = condition.split(",");
                String[] cloumns = new String[cc.length], compares = new String[cc.length], results = new String[cc.length];
                for (int i = 0; i < cc.length; i++) {
                    String c = cc[i];
                    cloumns[i] = c.substring(c.indexOf("{") + 1, c.indexOf("}"));
                    compares[i] = c.substring(c.indexOf("}") + 1, c.indexOf("'"));
                    results[i] = c.substring(c.indexOf("'") + 1, c.lastIndexOf("'"));
                }
                for (int i = 0; i < cloumns.length; i++) {
                    String cloumn = cloumns[i], compare = compares[i], result = results[i];
                    booleans = booleans + checks(cloumn, compare, result, param);
                }
            } else {
                String cloumn = condition.substring(condition.indexOf("{") + 1, condition.indexOf("}"));
                String compare = condition.substring(condition.indexOf("}") + 1, condition.indexOf("'"));
                String result = condition.substring(condition.indexOf("'") + 1, condition.lastIndexOf("'"));
                booleans = checks(cloumn, compare, result, param);
            }
            if (booleans.contains("false")) {
                isOk = false;
            }
        }
        return isOk;
    }

    /**
     * 将sql中的$s转换为$S,$d转为$D,$u>>$U,$autoid>$AUTOID
     * @param sql
     * @return
     */
    public static String sToS(String sql){
        String regex0 = "$s{",regex1 = "$d{",regex2 = "$u{",regex3 = "$autoids{";
        String replace0 = "$S{",replace1 = "$D{",replace2 = "$U{",replace3 = "$AUTOIDS{";

        if(sql.contains(regex0)){
            sql = sql.replace(regex0,replace0);
        }
        if(sql.contains(regex1)){
            sql = sql.replace(regex1,replace1);
        }
        if(sql.contains(regex2)){
            sql = sql.replace(regex2,replace2);
        }
        if(sql.contains(regex3)){
            sql = sql.replace(regex3,replace3);
        }
        return sql;
    }
    /**
     * 检查字段匹配条件是否满足，拼接为字符串，包含false则不通过条件
     *
     * @param cloumn
     * @param compare
     * @param result
     * @param param
     * @return
     */
    public static String checks(String cloumn, String compare, String result, Map<String, Object> param) {

        String booleans = "";
        switch (compare) {
            case ">":
                if (!objToStr(param.get(cloumn)).isEmpty()) {
                    if (objToStr(param.get(cloumn)).compareTo(result) > 0) {
                        booleans = booleans + "true,";
                    } else {
                        booleans = booleans + "false,";
                    }
                } else {
                    booleans = booleans + "false,";
                }
                break;
            case "<":
                if (!objToStr(param.get(cloumn)).isEmpty()) {
                    if (objToStr(param.get(cloumn)).compareTo(result) < 0) {
                        booleans = booleans + "true,";
                    } else {
                        booleans = booleans + "false,";
                    }
                } else {
                    booleans = booleans + "false,";
                }
                break;
            case "=":
                if (!objToStr(param.get(cloumn)).isEmpty()) {
                    if (objToStr(param.get(cloumn)).compareTo(result) == 0) {
                        booleans = booleans + "true,";
                    } else {
                        booleans = booleans + "false,";
                    }
                } else {
                    booleans = booleans + "false,";
                }
                break;
            case ">=":
                if (!objToStr(param.get(cloumn)).isEmpty()) {
                    if (objToStr(param.get(cloumn)).compareTo(result) >= 0) {
                        booleans = booleans + "true,";
                    } else {
                        booleans = booleans + "false,";
                    }
                } else {
                    booleans = booleans + "false,";
                }
                break;
            case "<=":
                if (!objToStr(param.get(cloumn)).isEmpty()) {
                    if (objToStr(param.get(cloumn)).compareTo(result) <= 0) {
                        booleans = booleans + "true,";
                    } else {
                        booleans = booleans + "false,";
                    }
                } else {
                    booleans = booleans + "false,";
                }
                break;
            default:
                booleans = booleans + "true,";
                break;
        }
        return booleans;
    }

    /**
     * 将SqlRow，转换为结果集的Map
     *
     * @param sqlRow1
     * @param select_columns
     * @return
     */
    public static Map<String, Object> rowMap(SqlRow sqlRow1, String select_columns) {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        String[] cloumns = select_columns.split(",");
        for (String s : cloumns) {
            returnMap.put(s, sqlRow1.get(s));
        }
        return returnMap;
    }


}
