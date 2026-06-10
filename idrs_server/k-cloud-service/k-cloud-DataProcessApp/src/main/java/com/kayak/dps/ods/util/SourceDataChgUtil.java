package com.kayak.dps.ods.util;

import com.kayak.core.sql.SqlRow;
import com.kayak.dps.ods.dao.DealValuePortDao;
import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SourceDataChgUtil {

    private static Logger logger = LoggerFactory.getLogger(SourceDataChgUtil.class);

    private static Map<String,Object> tableMapping = new HashMap<>();

    /**
     * 初始化源数据变化信息
     * **/
    public static Map<String, Object> initData(Map<String, Object> params, DealValuePortDao dealValuePortDao) throws Exception {


       //  特殊处理  拼接对比规则的数据
        List<SqlRow> list =  dealValuePortDao.findComType(params);
        if( null!=list && list.size()>0 && !StringUtil.isBlank(list.get(0).getString("comparison_rules")) && list.get(0).getString("comparison_rules").equals("02")){
            //  未处理的数据  置为失效
            dealValuePortDao.updateComOldData(params);
        }


        List<String> natural_keys = new ArrayList<>();
        List<String> remind_fields = new ArrayList<>();
        List<String> field_type = new ArrayList<>();
        List<String> out_dict = new ArrayList<>();
        List<String> related_repos = new ArrayList<>();
        List<String> special_fields = new ArrayList<>();
        boolean stop = true;
        //查询当前系统内是否有未确认
        //查找源数据业务主键配置
        List<SqlRow> selectCondition = dealValuePortDao.findSelectCondition(params);
        params.put("addcolumsflag","0");
        if(selectCondition.size()>0){
            params.put("addcolumsflag","1");
            for (SqlRow sqlRow : selectCondition) {
                params.put("natural_keys_forchg",sqlRow.getString("natural_key"));
                params.put("remind_type_forchg",sqlRow.getString("remind_type"));
                natural_keys  = Arrays.asList(sqlRow.getString("natural_key").split(","));
                remind_fields.add(sqlRow.getString("remind_field"));
                field_type.add(sqlRow.getString("field_type"));
                out_dict.add(sqlRow.getString("out_dict"));
                related_repos  = Arrays.asList(sqlRow.getString("related_report").split(","));
                if(stop&&"rms_stg_pms_cpdm".equals(params.get("port_table").toString())){
                    special_fields.add("cpzt");
                    special_fields.add("dqrq");
                    stop = false;
                }
            }
            params.put("natural_keys",natural_keys);
            params.put("remind_fields",remind_fields);
            params.put("field_type",field_type);
            params.put("out_dict",out_dict);
            params.put("related_repos",related_repos);
            params.put("special_fields",special_fields);
        }
        tableMapping.put("app_prod_regist_filing_info","ident_code");
        tableMapping.put("app_prod_issuance_regist_info","prod_code");
        return params;
    }


    public static void dealSourceDataChgConcat(Map<String, Object> params, DealValuePortDao dealValuePortDao) throws Exception {

            logger.info("================拼接对比逻辑================");
            List<Map<String,Object>> oldDataListForNK = new ArrayList<>();
            Map<String, Object> dataSourcechg = new HashMap<>();
            //根据接口类型进行资产处理
            dataSourcechg.put("port_type",params.get("port_type").toString());
            List<String> natural_keys = (List<String>) params.get("natural_keys");
            List<String> remind_fields = (List<String>) params.get("remind_fields");


            // 特殊逻辑  对比字段拼接
            String remind_field_concat = "";
            for(String str : remind_fields){
                remind_field_concat= remind_field_concat+","+str;
            }
            remind_field_concat = remind_field_concat.substring(1,remind_field_concat.length());


            List<String> field_type = (List<String>) params.get("field_type");
            //查询旧数据与待确认数据
            List<SqlRow> oldData = dealValuePortDao.findOldDataByConcat(params);
            String remind_type_forchg = (String) params.get("remind_type_forchg");
            //状态变更信息
            Map<String, Object> is_effectives = new HashMap<>();
            is_effectives.put("port_table",params.get("port_table").toString());
            String oldidflag ="";
            //终结标志
            boolean endfalsg = false;
            //差异标志
            boolean diffflag = false;
            //同oldid下差异标志
            boolean samediffflag = false;
            //删除未确认的旧变化数据
            dealValuePortDao.deleteSourceChgInfo(params);
            if(oldData.size()>0){
                for(SqlRow row : oldData){
                    if("".equals(row.getString("oldid"))||row.getString("oldid")==null){
                        //当没有旧数据时为新增
                        is_effectives.put("newid",row.getString("newid"));
                        oldChangeNewEffective3Concat(is_effectives,dealValuePortDao);
                        continue;
                    }else{
                        if(!oldidflag.equals(row.getString("oldid"))){
                            //不存在重复oldid
                            oldidflag = row.getString("oldid");
                            endfalsg = false;
                        }else{
                            samediffflag = diffflag;
                            endfalsg = true;
                        }
                        diffflag = false;
                        //主键信息存储
                        oldDataListForNK = saveNaturalKeys(natural_keys,oldDataListForNK,row);
                            if(StringUtil.isBlank(row.getString("oldid"))){
                                //没有旧数据则跳出
                                diffflag = false;
                                continue;
                            }
                            //比较信息
                            Map<String,Object> flagMap = diffSourceDate(row.get("olddata"),row.get("newdata"),"VARCHAR");
                            if(flagMap.get("flag").equals(false)){
                                diffflag = true;
                                if(!endfalsg){
                                    //有差异进行数据存储
                                    String naturalkeys = "";
                                    for (int j = 0; j < oldDataListForNK.size(); j++) {
                                        Map<String, Object> oldDataMap = oldDataListForNK.get(j);
                                        Set<String> keyset = oldDataMap.keySet();
                                        for (String key : keyset){
                                            if(j==oldDataListForNK.size()-1){
                                                naturalkeys += key+":"+oldDataMap.get(key).toString();
                                            }else{
                                                naturalkeys += key+":"+oldDataMap.get(key).toString()+",";
                                            }
                                        }
                                    }
                                    dataSourcechg.put("natural_keys",naturalkeys);
                                    dataSourcechg.put("tables",params.get("port_table").toString());
                                    dataSourcechg.put("deal_date",params.get("deal_date").toString());
                                    dataSourcechg.put("change_field",remind_field_concat);
                                    dataSourcechg.put("field_old",row.get("olddata"));
                                    dataSourcechg.put("field_new",row.get("newdata"));
                                    dataSourcechg.put("oldid",row.getString("oldid"));
                                    dataSourcechg.put("newid",row.getString("newid"));
                                    dataSourcechg.put("addflag",flagMap.get("addflag").toString());
                                    switch (remind_type_forchg){
                                        //未报备状态下自动确认
                                        case "01" :
                                            //是否已报备 true已报备，手动确认；false未报备，自动确认
                                            dataSourcechg.put("status","0");
                                            boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                            if(!register_status){
                                                dataSourcechg.put("status","1");
                                            }
                                            break;
                                        //固定自动确认
                                        case "02" :
                                            dataSourcechg.put("status","1");
                                            break;
                                        //固定人工确认
                                        case "03" :
                                            dataSourcechg.put("status","0");
                                            break;
                                        default:
                                    }
                                    //差异数据存入
                                    dealValuePortDao.addSourceChgInfo(dataSourcechg);
                                }
                            }

                        //特殊化处理
//                        if("rms_stg_pms_cpdm".equals(params.get("port_table").toString())){
//                            dealSpecial(row,params,dealValuePortDao);
//                        }
                        if(endfalsg){
                            //有重复oldid，根据设置进行调整
                            if(diffflag){
                                switch (remind_type_forchg){
                                    //未报备状态下自动确认
                                    case "01" :
                                        boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                        if(!register_status){
                                            //新旧都失效
                                            oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }else{
                                            //旧数据生效，新数据失效
                                            oldChangeNewEffectiveConcat(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }
                                        break;
                                    //固定自动确认
                                    case "02" :
                                        //新旧都失效
                                        oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        break;
                                    //固定人工确认
                                    case "03" :
                                        //旧数据生效，新数据失效
                                        if(!samediffflag){
                                            oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }else{
                                            oldChangeNewEffectiveConcat(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }
                                    default:
                                }
                            }else{
                                //没有差异的情况下，新旧都失效
                                //旧数据生效，新数据失效
                                if(!samediffflag){
                                    oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                }else{
                                    oldChangeNewEffectiveConcat(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                }
                            }
                        }else{
                            if(diffflag){
                                switch (remind_type_forchg){
                                    //未报备状态下自动确认
                                    case "01" :
                                        boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                        if(!register_status){
                                            //旧数据进行失效状态变更,新数据生效
                                            oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                                        }
                                        break;
                                    //固定自动确认
                                    case "02" :
                                        //需要将旧数据进行失效状态变更,新数据生效
                                        oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                                        break;
                                    //固定人工确认
                                    case "03" :
                                        break;
                                    default:
                                }
                            }else{
                                //没有差异的情况下，旧数据失效，新数据生效
                                oldChangeNewEffectiveConcat(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                            }
                        }
                        oldDataListForNK.clear();
                    }
                }
            }else{
                //没有差异或者没有数据的情况下，新插入数据需要生效，旧数据失效
                oldChangeNewEffective2(is_effectives,oldDataListForNK,dealValuePortDao);
            }
            logger.info("================结束处理源数据差异变化================");

    }

    public static void dealSourceDataChg(Map<String, Object> params, DealValuePortDao dealValuePortDao) throws Exception {
        //有配置才进行后续处理
        if("1".equals(params.get("addcolumsflag").toString())){
            // 查询 对比类型
            List<SqlRow> list =  dealValuePortDao.findComType(params);
            if( null!=list && list.size()>0 && !StringUtil.isBlank(list.get(0).getString("comparison_rules")) && list.get(0).getString("comparison_rules").equals("02")){
                //拼接对比逻辑
                dealSourceDataChgConcat(params,dealValuePortDao);
            }else {
                //普通字段对比逻辑 （旧逻辑 ）
                dealSourceDataChgField(params,dealValuePortDao);
            }
        }



    }

    /**
     * 处理源数据变化信息
     * **/
    public static void dealSourceDataChgField(Map<String, Object> params, DealValuePortDao dealValuePortDao) throws Exception {

            logger.info("================开始处理源数据差异变化================");
            List<Map<String,Object>> oldDataListForNK = new ArrayList<>();
            Map<String, Object> dataSourcechg = new HashMap<>();
            //根据接口类型进行资产处理
            dataSourcechg.put("port_type",params.get("port_type").toString());
            List<String> natural_keys = (List<String>) params.get("natural_keys");
            List<String> remind_fields = (List<String>) params.get("remind_fields");
            List<String> field_type = (List<String>) params.get("field_type");
            //查询旧数据与待确认数据
            List<SqlRow> oldData = dealValuePortDao.findOldData(params);
            String remind_type_forchg = (String) params.get("remind_type_forchg");
            //状态变更信息
            Map<String, Object> is_effectives = new HashMap<>();
            is_effectives.put("port_table",params.get("port_table").toString());
            String oldidflag ="";
            //终结标志
            boolean endfalsg = false;
            //差异标志
            boolean diffflag = false;
            //同oldid下差异标志
            boolean samediffflag = false;
            //删除未确认的旧变化数据
            dealValuePortDao.deleteSourceChgInfo(params);
            if(oldData.size()>0){
                for(SqlRow row : oldData){
                    if("".equals(row.getString("oldid"))||row.getString("oldid")==null){
                        //当没有旧数据时为新增
                        is_effectives.put("newid",row.getString("newid"));
                        oldChangeNewEffective3(is_effectives,dealValuePortDao);
                        continue;
                    }else{
                        if(!oldidflag.equals(row.getString("oldid"))){
                            //不存在重复oldid
                            oldidflag = row.getString("oldid");
                            endfalsg = false;
                        }else{
                            samediffflag = diffflag;
                            endfalsg = true;
                        }
                        diffflag = false;
                        //主键信息存储
                        oldDataListForNK = saveNaturalKeys(natural_keys,oldDataListForNK,row);
                        for (int i = 0; i < remind_fields.size(); i++) {
                            if(StringUtil.isBlank(row.getString("oldid"))){
                                //没有旧数据则跳出
                                diffflag = false;
                                //updatenew(is_effectives,"1",row.getString("newid"),dealValuePortDao);
                                continue;
                            }
                            //比较信息
                            Map<String,Object> flagMap = diffSourceDate(row.get("olddata"+i),row.get("newdata"+i),field_type.get(i));
                            if(flagMap.get("flag").equals(false)){
                                diffflag = true;
                                if(!endfalsg){
                                    //有差异进行数据存储
                                    String naturalkeys = "";
                                    for (int j = 0; j < oldDataListForNK.size(); j++) {
                                        Map<String, Object> oldDataMap = oldDataListForNK.get(j);
                                        Set<String> keyset = oldDataMap.keySet();
                                        for (String key : keyset){
                                            if(j==oldDataListForNK.size()-1){
                                                naturalkeys += key+":"+oldDataMap.get(key).toString();
                                            }else{
                                                naturalkeys += key+":"+oldDataMap.get(key).toString()+",";
                                            }
                                        }
                                    }
                                    dataSourcechg.put("natural_keys",naturalkeys);
                                    dataSourcechg.put("tables",params.get("port_table").toString());
                                    dataSourcechg.put("deal_date",params.get("deal_date").toString());
                                    dataSourcechg.put("change_field",remind_fields.get(i));
                                    dataSourcechg.put("field_old",row.get("olddata"+i));
                                    dataSourcechg.put("field_new",row.get("newdata"+i));
                                    dataSourcechg.put("oldid",row.getString("oldid"));
                                    dataSourcechg.put("newid",row.getString("newid"));
                                    dataSourcechg.put("addflag",flagMap.get("addflag").toString());
                                    switch (remind_type_forchg){
                                        //未报备状态下自动确认
                                        case "01" :
                                            //是否已报备 true已报备，手动确认；false未报备，自动确认
                                            dataSourcechg.put("status","0");
                                            boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                            if(!register_status){
                                                dataSourcechg.put("status","1");
                                            }
                                            break;
                                        //固定自动确认
                                        case "02" :
                                            dataSourcechg.put("status","1");
                                            break;
                                        //固定人工确认
                                        case "03" :
                                            dataSourcechg.put("status","0");
                                            break;
                                        default:
                                    }
                                    //差异数据存入
                                    dealValuePortDao.addSourceChgInfo(dataSourcechg);
                                }
                            }
                        }
                        //特殊化处理
                        if("rms_stg_pms_cpdm".equals(params.get("port_table").toString())){
                            dealSpecial(row,params,dealValuePortDao);
                        }
                        if(endfalsg){
                            //有重复oldid，根据设置进行调整
                            if(diffflag){
                                switch (remind_type_forchg){
                                    //未报备状态下自动确认
                                    case "01" :
                                        boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                        if(!register_status){
                                            //新旧都失效
                                            oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }else{
                                            //旧数据生效，新数据失效
                                            oldChangeNewEffective(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }
                                        break;
                                    //固定自动确认
                                    case "02" :
                                        //新旧都失效
                                        oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        break;
                                    //固定人工确认
                                    case "03" :
                                        //旧数据生效，新数据失效
                                        if(!samediffflag){
                                            oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }else{
                                            oldChangeNewEffective(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                        }
                                    default:
                                }
                            }else{
                                //没有差异的情况下，新旧都失效
                                //旧数据生效，新数据失效
                                if(!samediffflag){
                                    oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                }else{
                                    oldChangeNewEffective(is_effectives,"1",row.getString("oldid"),"0",row.getString("newid"),dealValuePortDao);
                                }
                            }
                        }else{
                            if(diffflag){
                                switch (remind_type_forchg){
                                    //未报备状态下自动确认
                                    case "01" :
                                        boolean register_status = dealValuePortDao.isSubmit(params,tableMapping,row,oldDataListForNK);
                                        if(!register_status){
                                            //旧数据进行失效状态变更,新数据生效
                                            oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                                        }
                                        break;
                                    //固定自动确认
                                    case "02" :
                                        //需要将旧数据进行失效状态变更,新数据生效
                                        oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                                        break;
                                    //固定人工确认
                                    case "03" :
                                        break;
                                    default:
                                }
                            }else{
                                //没有差异的情况下，旧数据失效，新数据生效
                                oldChangeNewEffective(is_effectives,"0",row.getString("oldid"),"1",row.getString("newid"),dealValuePortDao);
                            }
                        }
                        oldDataListForNK.clear();
                    }
                }
            }else{
                //没有差异或者没有数据的情况下，新插入数据需要生效，旧数据失效
                oldChangeNewEffective2(is_effectives,oldDataListForNK,dealValuePortDao);
            }
            logger.info("================结束处理源数据差异变化================");

    }

    /**
     * 对比源数据信息
     * */
    private static Map<String,Object> diffSourceDate(Object olddata, Object newdata, String fieldtype) throws ParseException {
        Map<String,Object> flagMap = new HashMap<>();
        boolean flag = true;
        if((olddata==null||olddata=="")&&(newdata==null||newdata=="")){
            //两者都为空无法比较
            flagMap.put("flag", true);
            flagMap.put("addflag","");
            return flagMap;
        }else if(((olddata==null||olddata=="")&&(newdata!=null&&newdata!=""))||((olddata!=null&&olddata!="")&&(newdata==null||newdata==""))){
            //两者有一个为空
            flagMap.put("flag", false);
            flagMap.put("addflag","");
            return flagMap;
        }
        switch (fieldtype){
            //字符串类型与枚举类型
            case  "VARCHAR" :
            case  "CHAR" :
            case  "TEXT" :
            case  "TINYTEXT" :
            case  "MEDIUMTEXT" :
            case  "LONGTEXT" :
            case  "ENUM" :
                if(!olddata.toString().equals(newdata.toString())){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","var");
                break;
            //整数类型
            case "TINYINT" :
            case "SMALLINT" :
            case "MEDIUMINT" :
            case "INT" :
            case "INTEGER" :
            case "BIGINT" :
                if(Integer.parseInt(olddata.toString())!=Integer.parseInt(newdata.toString())){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","int");
                break;
            //浮点类型
            case "FLOAT" :
            case "DOUBLE" :
                double epsilon = 1e-10;
                if(Double.parseDouble(olddata.toString())-Double.parseDouble(newdata.toString())>=epsilon){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","db");
                break;
            case "DECIMAL" :
                BigDecimal olddataDecimal = new BigDecimal(olddata.toString());
                BigDecimal newdataDecimal = new BigDecimal(newdata.toString());
                if(olddataDecimal.compareTo(newdataDecimal)!=0){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","var");
                break;
            //日期类型
            case "DATE" :
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                if(!sf.parse(olddata.toString()).equals(sf.parse(newdata.toString()))){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","dt");
                break;
            case "TIME" :
                if(!Time.valueOf(olddata.toString()).equals(Time.valueOf(newdata.toString()))){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","tm");
                break;
            case "TIMESTAMP" :
            case "DATETIME" :
            case "YEAR" :
                if(!Timestamp.valueOf(olddata.toString()).equals(Timestamp.valueOf(newdata.toString()))){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","dts");
                break;
            //布尔类型
            case "BOOLEAN" :
            case "BOOL" :
            case "TINYINT(1)" :
                if(Boolean.parseBoolean(olddata.toString())!=Boolean.parseBoolean(newdata.toString())){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","bool");
                break;
            //集合类型
            case "SET" :
                Set<String> olddataSet = (Set<String>) olddata;
                Set<String> newdataSet = (Set<String>) newdata;
                if(!olddataSet.equals(newdataSet)){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","var");
                break;
            //二进制数据类型
            case "BINARY" :
            case "VARBINARY" :
            case "BLOB" :
            case "TINYBLOB" :
            case "MEDIUMBLOB" :
            case "LONGBLOB" :
                byte[] olddataByte = (byte[]) olddata;
                byte[] newdataByte = (byte[]) newdata;
                if(!olddataByte.equals(newdataByte)){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","blob");
                break;
            //几何类型
            case "GEOMETRY" :
            case "POINT" :
            case "LINESTRING" :
            case "POLYGON" :
            case "MULTIPOINT" :
            case "MULTILINESTRING" :
            case "MULTIPOLYGON" :
            case "GEOMETRYCOLLECTION" :
                if(!olddata.equals(newdata)){
                    flag = false;
                }
                flagMap.put("flag",flag);
                flagMap.put("addflag","var");
                break;
            default:
        }
        return flagMap;
    }

    private static void dealSpecial(SqlRow row, Map<String, Object> params, DealValuePortDao dealValuePortDao) throws Exception {
        List<String> special_fields = (List<String>) params.get("special_fields");
        Map<String, Object> dataSourcechg = new HashMap<>();
        dataSourcechg.put("tables",params.get("port_table").toString());
        dataSourcechg.put("dealid",row.getString("newid"));
        String oldid = params.get("oldid")==null?"":params.get("oldid").toString();
        if(!"".equals(oldid)){
            //有旧数据时才进行处理
            for (int i = 0; i < special_fields.size(); i++) {
                String key = special_fields.get(i);
                if("cpzt".equals(key)){
                    //产品状态
                    String cpztnew = "".equals(row.getString("specnew"+i))?null:row.getString("specnew"+i);
                    String cpztold = "".equals(row.getString("specold"+i))?null:row.getString("specnew"+i);
                    String dqrqnew = "".equals(row.getString("specnew"+(i+1)))?null:row.getString("specnew"+(i+1));
                    String dqrqold = "".equals(row.getString("specold"+(i+1)))?null:row.getString("specold"+(i+1));
                    dataSourcechg.put("cpzt",cpztnew);
                    dataSourcechg.put("dqrq",dqrqnew);
                    if("104".equals(cpztnew)||"105".equals(cpztnew)||"106".equals(cpztnew)||"107".equals(cpztnew)||"108".equals(cpztnew)){
                        //无用状态需要跳过
                        dataSourcechg.put("cpzt",cpztold);
                    }else if(!("101".equals(cpztnew)||"111".equals(cpztnew)||"201".equals(cpztnew)||"203".equals(cpztnew))){
                        //以上状态不可随意更改
                        dataSourcechg.put("dqrq",dqrqold);
                    }else{
                        if(!dqrqnew.equals(dqrqold)){
                            //不一致时更新实际到期日
                            dataSourcechg.put("sjdqrq",dqrqnew);
                        }
                    }
                }
            }
            dealValuePortDao.upSpecialField(dataSourcechg);
        }
    }

    private static List<Map<String, Object>> saveNaturalKeys(List<String> natural_keys, List<Map<String, Object>> oldDataListForNK, SqlRow row) {
        for (int j = 0; j < natural_keys.size(); j++) {
            Map<String,Object> oldDataMapForNK = new HashMap<>();
            oldDataMapForNK.put(natural_keys.get(j),row.getString(natural_keys.get(j)));
            oldDataListForNK.add(oldDataMapForNK);
        }
        return oldDataListForNK;
    }


    private static void oldChangeNewEffectiveConcat(Map<String, Object> is_effectives,String updateeffectivesOld, String whereidOld,String updateeffectivesNew, String whereidNew, DealValuePortDao dealValuePortDao) throws Exception {
        // 兼容旧id 拼接的清空
        String []  oldIds = whereidOld.split(",");
        for(String oldId:oldIds){
            dealValuePortDao.updateOldData(is_effectives,updateeffectivesOld,oldId);
        }
        String []  newIds = whereidNew.split(",");
        for(String newId:newIds){
            dealValuePortDao.updateOldData(is_effectives,updateeffectivesNew,newId);
        }

    }


    private static void oldChangeNewEffective(Map<String, Object> is_effectives,String updateeffectivesOld, String whereidOld,String updateeffectivesNew, String whereidNew, DealValuePortDao dealValuePortDao) throws Exception {
        dealValuePortDao.updateOldData(is_effectives,updateeffectivesOld,whereidOld);
        dealValuePortDao.updateOldData(is_effectives,updateeffectivesNew,whereidNew);
    }

    private static void updatenew(Map<String, Object> is_effectives,String updateeffectivesNew, String whereidNew, DealValuePortDao dealValuePortDao) throws Exception {
        dealValuePortDao.updateOldData(is_effectives,updateeffectivesNew,whereidNew);
    }

    private static void oldChangeNewEffective2(Map<String, Object> is_effectives, List<Map<String, Object>> oldDataListForNK, DealValuePortDao dealValuePortDao) throws Exception {
        is_effectives.put("updateeffectives","0");
        is_effectives.put("whereeffectives","1");
        dealValuePortDao.updateOldData2(is_effectives,oldDataListForNK);
        is_effectives.put("updateeffectives","1");
        is_effectives.put("whereeffectives","2");
        dealValuePortDao.updateOldData2(is_effectives,oldDataListForNK);
    }

    private static void oldChangeNewEffective3(Map<String, Object> is_effectives,  DealValuePortDao dealValuePortDao) throws Exception {
        dealValuePortDao.updateOldData(is_effectives,"1",is_effectives.get("newid").toString());
    }


    private static void oldChangeNewEffective3Concat(Map<String, Object> is_effectives,  DealValuePortDao dealValuePortDao) throws Exception {
        String [] newIds  = is_effectives.get("newid").toString().split(",");
        for(String newId : newIds){
            dealValuePortDao.updateOldData(is_effectives,"1",newId);
        }

    }
}
