package com.kayak.utils;

import com.kayak.base.dao.ComnDao;
import com.kayak.cache.util.CacheUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.system.SysUtil;
import com.kayak.core.util.Tools;
import com.kayak.pms.T81.dao.T8ProdDocInfoDao;
import com.kayak.pms.onlineEdit.model.T8OnlineWordValue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zhangchangsi
 * @version 1.0
 * @date 2021/4/2 11:35
 */
public class OnlineUtils {
    private static final Logger logger = LoggerFactory.getLogger(OnlineUtils.class);
    final static List  docNames = Arrays.asList(new String[] {"公募定开产品合同模板-不含份额分类.docx","公募定开产品说明书模板-含份额分类.docx",
			"公募定开产品合同模板-不含份额分类+封闭期投资日.docx","公募定开产品说明书模板-含份额分类+封闭期投资日.docx",
			"公募封闭产品合同模板-不含份额分类.docx","公募封闭产品合同模板-含份额分类.docx",
			"公募最低持有期产品合同模板（定开净值）-不含份额分类+含产品开放日.docx","公募最低持有期产品合同模板（天天净值）-不含份额分类+含产品开放日.docx"});
    /**
     * 将list中的默认值中的变量做二次替换
     * @param T8OnlineList
     * @return
     */
    public static List<T8OnlineWordValue> getNewList(List<T8OnlineWordValue> T8OnlineList, List<Map<String, Object>> mapList) throws Exception {
        ComnDao comnDao = SysBeans.getBean("comnDao");
        for(T8OnlineWordValue t8OnlineWordValue:T8OnlineList){
            String wordValue = t8OnlineWordValue.getWordValue();
            if(StringUtils.isNotEmpty(wordValue) && wordValue.contains("${")){
                logger.info("默认数据源>>{}", wordValue);
                List<String> keys = getKeys(wordValue);
                for (String key:keys){
                    for(Map<String , Object> map : mapList){
                        /*如果默认值里面有一个展位符的值为空者清空整段默认值并本次退出循环*/
                        if(map.get(key) == null || StringUtils.isEmpty(map.get(key).toString())){
                            logger.info("默认数据源>>{}中占位符>>{}值为空>>{}",wordValue,key, map.get(key));
                            wordValue = "";
                            break;
                        }else{
                            String s =  map.get(key).toString();
                            if (StringUtils.isNotBlank(key)) {
                                //从数据库查询该key是否需要保留小数
                                String sql  = String.format("select data_digits,data_type from t8_online_word_table_columns where column_name = '%s'", key);
                                List<SqlRow> rows = comnDao.findRows(sql);
                                if (rows != null && rows.size() > 0) {
                                    //对值进行处理
                                    s =  dataTypeHandler(t8OnlineWordValue,(String)rows.get(0).get("data_type"), (String)rows.get(0).get("data_digits"),map.get(key).toString());
                                }
                            }
                            logger.info("默认数据源>>{}中占位符>>{}值不为空>>{}",wordValue,key, s);
                            /*如果默认值里面的占位符不为空者进行替换*/
                            wordValue = wordValue.replace("${" + key+ "}",  s);
                        }
                    }
                }
                logger.info("替换后的数据源>>{}",wordValue);
                t8OnlineWordValue.setWordValue(wordValue);
            }
        }
        return T8OnlineList;
    }

    /**
     * 获取字符串中标识符
     * @param text
     * @return
     */
    public static List<String> getKeys(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String openToken="${";
        String closeToken="}";
        List<String> res = new ArrayList<String>();
        // search open token
        int start = text.indexOf(openToken,0);
        for(int i  = 0;i<text.length();i++){
            if(start > -1){
                i = start + openToken.length();
                int end = text.indexOf(closeToken, i);
                String key = text.substring(i,end);
                //logger.info("key::::"+key);
                res.add(key);
                i = end +closeToken.length();
            }
            start = text.indexOf(openToken, i);
        }
        return res;
    }

    /**
     * 生成新版本的数据
     * @param oldT8OnlineWordValueList 原始模板版本list
     * @param newT8OnlineWordValueList 当前最大版本数据list
     * @param mapList 数据源查询出来的map
     * @param params 产品基本信息map
     */
    public static void DataReplace(List<T8OnlineWordValue> oldT8OnlineWordValueList, List<T8OnlineWordValue> newT8OnlineWordValueList, List<Map<String, Object>> mapList, Map<String, Object> params) throws Exception {
        ComnDao comnDao = SysBeans.getBean("comnDao");
        
        T8ProdDocInfoDao t8ProdDocInfoDao = SysBeans.getBean("t8ProdDocInfoDao");
        boolean excessStatus = false;
        for (Map<String, Object> dataMap : mapList) {
        	 if((dataMap.get("excess_status")!=null)&&("true".equals(dataMap.get("excess_status")))) {         	
             	excessStatus=true;	
             }
            for (T8OnlineWordValue t8OnlineWordValue : oldT8OnlineWordValueList) {
                /*---------------------对不可可编辑的字段进行替换----------------------------------*/
                if ("0".equals(t8OnlineWordValue.getIsDisabled())) {
                    if (StringUtils.isNotBlank(t8OnlineWordValue.getDict())) {
                        //对存在数据字典的值进行处理
                        dicthandler(t8OnlineWordValue, dataMap);
                    } else {
                        if (StringUtils.isBlank(t8OnlineWordValue.getDefaultValue())) {
                            String val = String.valueOf(dataMap.get(t8OnlineWordValue.getColumnName()));

                            if (StringUtils.isNotBlank(val)) {
                                //根据数据类型进行处理
                                val = dataTypeHandler(t8OnlineWordValue, val);
                            }
                            logger.info("模板字段:{}数据源字段{}-不存在数据字典、不存在默认值、不可编辑，尝试将值进行替换为:{}", t8OnlineWordValue.getWordKey(), t8OnlineWordValue.getColumnName(), val);
                            //不可编辑、不存在默认值的直接替换
                            t8OnlineWordValue.setWordValue(val);
                        } else {
                            logger.info("模板字段:{}数据源字段{}-不存在数据字典、存在默认值、不可编辑，不进行替换,原值为{}", t8OnlineWordValue.getWordKey(), t8OnlineWordValue.getColumnName(), t8OnlineWordValue.getWordValue());

                        }
                    }
                } else {
                    if (StringUtils.isNotBlank(t8OnlineWordValue.getDict())) {
                        //对存在数据字典的值进行处理
                        dicthandler(t8OnlineWordValue, dataMap);
                    } else {
                        //判断是否存在默认值
                        if (StringUtils.isBlank(t8OnlineWordValue.getDefaultValue())) {
                            logger.info("模板字段:{}数据源字段{}-可编辑，不存在数据字典、不存在默认值,尝试将原始模板版本的值替换为:{}", t8OnlineWordValue.getWordKey(), t8OnlineWordValue.getColumnName(), dataMap.get(t8OnlineWordValue.getColumnName()));
                            t8OnlineWordValue.setWordValue(String.valueOf(dataMap.get(t8OnlineWordValue.getColumnName())));
                        } else {
                            logger.info("模板字段:{}数据源字段{}-可编辑,不存在数据字典、存在默认值,不进行替换,,原值为{}", t8OnlineWordValue.getWordKey(), t8OnlineWordValue.getColumnName(), t8OnlineWordValue.getWordValue());
                        }
                    }
                }
            }
        }

        logger.info("原始模板List:{}", oldT8OnlineWordValueList);
        //将值进行二次替换
        getNewList(oldT8OnlineWordValueList, mapList);
        SqlRow sqlRow = t8ProdDocInfoDao.getRiskNum(params);
        int riskNum = StringUtils.isEmpty(sqlRow==null?null:sqlRow.getString("risk_num"))==true?0:Integer.parseInt(sqlRow.getString("risk_num"));
        boolean flag = false;
       
        for (T8OnlineWordValue t8OnlineWordValue : oldT8OnlineWordValueList) {
        	
            //如果存在新版本的数据则再将可编辑的数据进行替换
            if (!CollectionUtils.isEmpty(newT8OnlineWordValueList)) {
                //将上一版本可编辑的值替换的list
                if ("1".equals(t8OnlineWordValue.getIsDisabled())) {
                    for (T8OnlineWordValue newT8OnlineWordValue : newT8OnlineWordValueList) {
                        //获取两个list相同的key,然后将数据进行替换
                        if (t8OnlineWordValue.getWordKey().equals(newT8OnlineWordValue.getWordKey())) {
                            logger.info("模板字段:{}数据源字段{}-可编辑尝试获取上一版本的值然后替换到当前版本，上一版本值为:{}", t8OnlineWordValue.getWordKey(),t8OnlineWordValue.getColumnName(), newT8OnlineWordValue.getWordValue());
                            t8OnlineWordValue.setWordValue(newT8OnlineWordValue.getWordValue());
                        }
                    }
                }
            }

            /*如果配置了单独数据源则进行查询*/
            if (StringUtils.isNotBlank(t8OnlineWordValue.getSqlInfo())) {
                //使用单独数据源查询值
                String sql  = t8OnlineWordValue.getSqlInfo().trim();
                try {
                    List<SqlRow> rows = comnDao.findRows(sql, params);
                    
                    StringBuffer sbf = new StringBuffer();
                    for(SqlRow row :rows) {
                    	
                    	if("other_risk".equals(t8OnlineWordValue.getWordKey())){
                    		riskNum++;
                    		sbf.append("\n"+"\u3000"+"\u3000"+riskNum+"."+row.getString("val"));
                    	}else {
                    		if(rows.size()>1) {
                        		sbf.append("\n"+"\u3000"+"\u3000"+row.getString("val"));	
                        	}else{
                        		sbf.append(row.getString("val"));	
                        	}
                    	}
                    	
                    }             
                    logger.info("模板字段{}使用单独数据源进行查询值为:{}", t8OnlineWordValue.getWordKey(),sbf.toString());
                    t8OnlineWordValue.setWordValue(sbf.toString());
                }catch (Exception e) {
                    logger.error("在线编辑生产文档执行数据源发生错误:{}", e.getMessage());
                }
            }

            if("has_other_risk".equals(t8OnlineWordValue.getWordKey())) {
            	t8OnlineWordValue.setWordValue(String.valueOf(riskNum+1));
            }
            if(("redemption_fee".equals(t8OnlineWordValue.getWordKey())&&StringUtils.isNotEmpty(t8OnlineWordValue.getWordValue()))) {         	
            	flag=true;	
            }
            
           
            //对空值进行处理
            if (StringUtils.isBlank(t8OnlineWordValue.getWordValue()) || "null".equals(t8OnlineWordValue.getWordValue())) {
                logger.info("值-----------{}为空", t8OnlineWordValue.getWordKey());
                if (StringUtils.isNotBlank(t8OnlineWordValue.getEmptyDefaultVal())) {
                    logger.info("值-----------{}为空,尝试将其替换为:{}", t8OnlineWordValue.getWordKey(), t8OnlineWordValue.getEmptyDefaultVal());
                    t8OnlineWordValue.setWordValue(t8OnlineWordValue.getEmptyDefaultVal());
                }
            }
        }
        if(sqlRow !=null) {
        	String fileName = sqlRow.getString("temp_name");
            for(T8OnlineWordValue temp :oldT8OnlineWordValueList) {
            	if(flag==true&&"redemption_fee_num".equals(temp.getWordKey()))
            	temp.setWordValue("10.2.6");
            	
            	
            	if(excessStatus==false&&temp.getWordKey().contains("excess_performance_reward")) {
            		logger.info("文档模板名称-------------{}",fileName.replace(" ",""));
            		if(docNames.contains(fileName.replace(" ",""))) {
            			temp.setWordValue("本产品不收取超额业绩报酬。");
            		}else {
            			temp.setWordValue("本产品暂不收取超额业绩报酬。");
            		}
            		
            	}
                	
            	if(excessStatus==false&&"excess_perf_explain".equals(temp.getWordKey())) {
            		logger.info("文档模板名称-------------{}",fileName.replace(" ",""));
            		if(docNames.contains(fileName.replace(" ",""))) {
            			temp.setWordValue("本产品不收取超额业绩报酬。");
            		}else {
            			temp.setWordValue("本产品暂不收取超额业绩报酬。");
            		}
            	}
            		
        	}
        }

    }

    /**
     * 对存在数据字典的值进行替换
     * @param t8OnlineWordValue
     * @param dataMap
     */
    private static void dicthandler(T8OnlineWordValue t8OnlineWordValue, Map<String, Object> dataMap){
        String wordKey = String.valueOf(dataMap.get(t8OnlineWordValue.getColumnName()));
        if (wordKey.contains(",")) {
            String[] wordKeys = wordKey.split(",");
            StringBuilder dictitems = new StringBuilder();
            for (int i = 0;i<wordKeys.length;i++){
                if(i == 0){
                    dictitems.append(CacheUtil.getDictItem(t8OnlineWordValue.getDict(),wordKeys[i]));
                }else{
                    dictitems.append("、").append(CacheUtil.getDictItem(t8OnlineWordValue.getDict(),wordKeys[i]));
                }
            }
            if (StringUtils.isNotBlank(dictitems)) {
                logger.info("模板字段:{}数据源字段{}-存在数据字典且可能存在多个值、值不为空，尝试将值进行替换为:{}" , t8OnlineWordValue.getWordKey(),t8OnlineWordValue.getColumnName(), dictitems.toString());
                t8OnlineWordValue.setWordValue(dictitems.toString());
            }

        } else {
            String dictitem = CacheUtil.getDictItem(t8OnlineWordValue.getDict(), String.valueOf(dataMap.get(t8OnlineWordValue.getColumnName())));
            if (StringUtils.isNotBlank(dictitem)&& !"null".equals(dictitem)) {
                t8OnlineWordValue.setWordValue(dictitem);
            } else {
                    logger.info("模板字段:{}数据源字段{}-存在数据字典只有一个值且值为空，尝试将值替换为:{}" , t8OnlineWordValue.getWordKey(),t8OnlineWordValue.getColumnName(), wordKey);
                    t8OnlineWordValue.setWordValue(wordKey);
            }
        }
    }

    /**
     *根据数据类型做处理
     * @param t8OnlineWordValue
     * @param val
     * @return
     */
    public static String dataTypeHandler(T8OnlineWordValue t8OnlineWordValue, String val){
        String dataType = t8OnlineWordValue.getDataType();
        String digits = t8OnlineWordValue.getDataDigits();
      return dataTypeHandler(t8OnlineWordValue,dataType,digits,val);
    }

    /**
     *根据数据类型做处理
     * @param t8OnlineWordValue
     * @param val
     * @return
     */
    public static String dataTypeHandler(T8OnlineWordValue t8OnlineWordValue, String dataType, String digits, String val){
        if (StringUtils.isBlank(dataType)) {
            return val;
        }
        /*数据类型:01-varchar 02-money 03-double 04-int 05-date(20210203--2021年02月03日) 06-格式化时间(150304--15:03) 07格式化时间(150304--15:03:04)*/
        logger.info("未转换前:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        if ("01".equals(dataType)) {
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
            //varchar暂时不做处理
        } else if ("02".equals(dataType)) {
            String digitsVal = "0.00";
            if (StringUtils.isNotBlank(digits)) {
                StringBuilder digs = new StringBuilder("0.");
                if (Integer.parseInt(digits) >= 0 && Integer.parseInt(digits) < 8) {
                    /*循环拼接小数位数*/
                    for (int i = 0; i < Integer.parseInt(digits); i++) {
                        digs.append("0");
                    }
                    digitsVal = digs.toString();
                }
            }
            DecimalFormat df = new DecimalFormat("#,##" + digitsVal);
            //判断是否是数字
            if (Tools.isNumber(val)) {
                //如果是数字则进行格式化
                val = df.format(Double.parseDouble(val));
            }
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        } else if ("03".equals(dataType)) {
            if (StringUtils.isNotBlank(digits) && Tools.isNumber(val)) {
                StringBuilder format = new StringBuilder("%.");
                format.append(digits).append("f");
                val = String.format(format.toString(), Double.parseDouble(val));
            }
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        } else if ("04".equals(dataType)) {
            //int暂时不做处理
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        } else if("05".equals(dataType)){
            //将日期格式的数据进行格式化
            if (StringUtils.isNotBlank(val) && val.length() == 8 && Tools.isNumber(val)) {
                LocalDate localDate = LocalDate.parse(val, DateTimeFormatter.ofPattern("yyyyMMdd"));
                LocalDateTime localDateTime = localDate.atStartOfDay();
                val = localDateTime.format(DateTimeFormatter.ofPattern("yyyy年M月d日"));
            }
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        }
        else if("06".equals(dataType)){
            //将日期格式的数据进行格式化
            if (StringUtils.isNotBlank(val) && val.length() == 6 && Tools.isNumber(val)) {
                LocalTime parse = LocalTime.parse(val, DateTimeFormatter.ofPattern("HHmmss"));
                val = parse.format(DateTimeFormatter.ofPattern("HH:mm"));
            }
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        }
        else if("07".equals(dataType)){
            //将日期格式的数据进行格式化
            if (StringUtils.isNotBlank(val) && val.length() == 6 && Tools.isNumber(val)) {
                LocalTime parse = LocalTime.parse(val, DateTimeFormatter.ofPattern("HHmmss"));
                val = parse.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            }
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        } else{
            logger.info("转换后:数据类型:{},数据源key:{},数据val:{}", dataType, t8OnlineWordValue.getColumnName(), val);
        }

        return val;
    }

    /**
     * 获取在线编辑文档保存路径
     * @return
     */
    public static String getOnlinepath (String wordPath) throws Exception {
        String fileStorePath;
        String separate = "/";
        String s;
        String os = System.getProperty("os.name");
        String path = "";
        if(os.toLowerCase().startsWith("win")){
            fileStorePath = wordPath;
        }else{
            s="80000080003";
            path = SysUtil.getSystemParamsByParaid(s);
            fileStorePath = path + separate;
        }
        return fileStorePath;
    }
    

}


