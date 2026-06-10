package com.kayak.rpt.zz.manage.util;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.rpt.zz.manage.enums.ExFmt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//axin
//中债直连工具类
public class CheckDataUtils {
    private static final Logger log = LoggerFactory.getLogger(CheckDataUtils.class);

    /**
     * 校验dataname是否包含regex中字符集
     * @param dataname
     * dataname.contains(regexs[i]  并不能完全判定值域包含 例如 001 新增  {00,01,02}  将返回true
     * @param regex
     * @return
     */
    public static int conUtil(String dataname,String regex){
        int ret = -1;
        if(dataname.contains(" ")){//如果存在空格  01 新增
            dataname=dataname.substring(0,dataname.indexOf(" ")); //01
        }
        String[] split = regex.split(",");//得到字符串数组
        List<String> strings = Arrays.asList(split);//将数组转化为List
        return strings.contains(dataname)?0:-1;
    }


    /**
     * 获取修正后的值
     * @return
     */
    public static Object getCorrectValue(SqlRow row, String key, Object value){
        Object val = value==null?"":value;
        if(row!=null){
            if(row.get(key)!=null&&!"".equals(row.get(key))){
                val = row.get(key);
            }
        }

        return val.toString();
    }


    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDict(String dict, String value) throws Exception {
        String desc = "";
        if("".equals(value) || value==null){
            return desc;
        }
        if(!getDictCheckKey(dict, value)){
            desc = "不在值域范围内。<br/>";
        }
        return desc;
    }

    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDictValue(String dict, String value) throws Exception {
        String desc = "";
        if("".equals(value) || value==null){
            return desc;
        }
        if(!getDictCheckValue(dict, value)){
            desc = "不在值域范围内。<br/>";
        }
        return desc;
    }

    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDictValue(String dict, String key,String value) {
        String desc = "";
        if("".equals(value) || value==null){
            return desc;
        }
        try{
            if(!value.equals(getDictNameSys(dict, key))){
                desc = "不在值域范围内。<br/>";
            }
        }catch (Exception e){
            log.info(e.getMessage());
            desc = "不在值域范围内。<br/>";
        }
        return desc;
    }

    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDictAll(String dict, String value) throws Exception {
        String desc = "";
        if("".equals(value) || value==null){
            return desc;
        }
        if(!getDictValueCheckAll(dict, value)){
            desc = "不在值域范围内。<br/>";
        }
        return desc;
    }
    /**
     * 检查字段是否在字典范围内
     * @param dict
     * @param value
     * @return
     */
    public static String checkDict(String dict, String value, String name,boolean isExport) throws Exception {
        String desc = "";
        if ("".equals(value) || value == null) {
            return desc;
        }
        //如果是导入的校验 value格式 01 新增，如果是数据生成的 格式 01
        if(isExport){
            if (!getDictValueEx(dict, value)) {
                desc =getErrInt()+name + "不在值域范围内。<br/>";
            }
        }else{
            if (!get202DictValue(dict, value)) {
                desc = getErrInt()+name + "不在值域范围内。<br/>";
            }
        }

        return desc;
    }

    public static String getErrInt(){
        return ++CheckDataParams.errInt+"：";
    }


    /**
     * 获取数据字典值， 用逗号隔开
     *
     * @param dict
     * @return
     */
    public static boolean getDictValueEx(String dict, String value) throws Exception {
        if (CheckDataParams.dict_mapex.get(dict) == null)
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        return CheckDataParams.dict_mapex.get(dict).contains(value);
    }

    /**
     * 获取数据字典值， 用逗号隔开
     *
     * @param dict
     * @return
     */
    public static boolean get202DictValue(String dict, String value) throws Exception {
        if (CheckDataParams.dict_map_vol.get(dict) == null)
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        return CheckDataParams.dict_map_vol.get(dict).contains(value);
    }


    public static boolean getDictCheckKey(String dict, String value) throws Exception {
        if (CheckDataParams.dict_map_check.get(dict) == null)
            throw new Exception("数据字典sys_dict_item：" + dict + "不存在");
        return CheckDataParams.dict_map_check.get(dict).contains(value);
    }

    public static boolean getDictCheckValue(String dict, String value) throws Exception {
        value = value.replace("（","(").replace("）",")");
        if (CheckDataParams.dict_map_check_value.get(dict) == null)
            throw new Exception("数据字典sys_dict_item：" + dict + "不存在");
        return CheckDataParams.dict_map_check_value.get(dict).contains(value);
    }
    /**
     * 存放(key+空格+值)，用于校验合法性
     * @param dict
     * @param value(key+空格+值)
     * @return
     * @throws Exception
     */
    public static boolean getDictValueCheckAll(String dict, String value) throws Exception {
        value = value.replace("（","(").replace("）",")");
        if (CheckDataParams.dict_map_check_all.get(dict) == null)
            throw new Exception("数据字典sys_dict_item：" + dict + "不存在");
        return CheckDataParams.dict_map_check_all.get(dict).contains(value);
    }




    /**
     * 获取字典名称
     * @param dict
     * @param key
     * @return
     * @throws Exception
     */
    public static String getDictName(String dict, String key) throws Exception {
        String out_value = "";

        if("".equals(key)||key==null){
            return out_value;
        }
        if(key.contains(" ")){
            key = key.split(" ")[0];
        }

        if (CheckDataParams.dict_name.get(dict) == null) {
            throw new Exception("数据字典base_ex_map：" + dict + "不存在");
        }
        out_value = CheckDataParams.dict_name.get(dict).get(key);
        out_value = out_value==null?"":out_value;
        return out_value;
    }


    /**
     * 获取系统中的字典值
     * @param dict
     * @param key
     * @return
     * @throws Exception
     */
    public static String getDictNameSys(String dict, String key) throws Exception {
        if (CheckDataParams.dict_name.get(dict) != null && ObjectUtil.isNotEmpty(CheckDataParams.dict_name.get(dict).get(key))) {
            return CheckDataParams.dict_name.get(dict).get(key);
        }
        if (CheckDataParams.dict_name_sys.get(dict) != null && ObjectUtil.isNotEmpty(CheckDataParams.dict_name_sys.get(dict).get(key))) {
            return CheckDataParams.dict_name_sys.get(dict).get(key);
        }

        throw new Exception("数据字典sys_dict_item、base_ex_map：" + dict + ":" + key + "不存在");
    }


    /**
     * 检查数值类型
     * @param exfmts
     * @param key
     * @param value
     * @return
     */
    public static String checkFMT(List<SqlRow> exfmts, String key, Object value){
        String desc = "";
        SqlRow exFmt = null;
        for(int i=0; i<exfmts.size(); i++){
            SqlRow tmp = exfmts.get(i);
            if(tmp.getString("fld").equals(key.toUpperCase())){
                exFmt = tmp;
                break;
            }
        }
        if(exFmt!=null && value!=null && !"".equals(value)){
            if(exFmt.getString("itmtp").equals("C")){
                int len = value.toString().length();
                if(Double.parseDouble(exFmt.getString("itmprc")) - len!=0){
                    log.info(exFmt.getString("itmprc") + "|" + value.toString().length());
                    return "长度不对";
                }
            }
        }

        return desc;
    }

    /**
     * 检查数值类型
     *
     * @param exfmts
     * @param key
     * @param value
     * @return
     */
    public static String checkFMTEx(List<ExFmt> exfmts, String key,
                                       Object value, String name) {
        String desc = "";
        ExFmt exFmt = null;
        for (int i = 0; i < exfmts.size(); i++) {
            ExFmt tmp = exfmts.get(i);
            if (tmp.getFld().equals(key.toUpperCase())) {
                exFmt = tmp;
                break;
            }
        }
        if (exFmt != null && value != null && !"".equals(value)) {
            if ("C,V".indexOf(exFmt.getDictItmtp()) > -1) {
                int len =  value.toString().getBytes().length;//length((String) value);
                if (exFmt.getItmprc() - len < 0) {
                    log.info(exFmt.getItmprc() + "|"
                            + value.toString().getBytes().length);
                    return getErrInt()+name + "过长。<br/>";
                }
            }
        }

        return desc;
    }


    /**
     * 将20200101转变为2020-01-01
     * @param date
     * @return  date.matches("/^\d{8}$/")  for update   zhanglei  限定格式更改只限用于 yyyymmdd格式
     */
    public static String getS_d(String date){
        if(date.length() == 8 && date.matches("^\\d{8}$")){
            GregorianCalendar cal = new GregorianCalendar();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            try {
                cal.setTime(sdf.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateFormat.format(cal.getTime());
        }else{
            return date;
        }
    }

    /**
     * 验证日期是否合法
     * @param length
     * @param sDate
     * @param format
     * @return
     */
    public static boolean isLegalDate(int length, String sDate,String format) {
        int legalLen = length;
        if ((sDate == null) || (sDate.length() != legalLen)) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(sDate);
            return sDate.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }



    public static String replace(String text, String searchString,
                                 String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    public static String format(String date, String format) {
        String year = "";
        String month = "";
        String day = "";
        String retDate = "";

        if (date == null)
            return "";

        date = date.trim();
        if (date.length() < 4) {
            return date;
        }
        if (date.length() >= 4) {
            year = date.substring(0, 4);
        }
        if (date.length() >= 6) {
            month = date.substring(4, 6);
        }
        if (date.length() >= 8) {
            day = date.substring(6, 8);
        }
        retDate = format;
        retDate = retDate.toUpperCase();
        retDate = StringUtils.replaceOnce(retDate, "YYYY", year);
        retDate = StringUtils.replaceOnce(retDate, "MM", month);
        retDate = StringUtils.replaceOnce(retDate, "DD", day);

        return retDate;
    }


    public static String dateAdd(String start, int days) {
        int year;
        int month;
        int day = 0;
        start = start.trim();
        if (start.length() >= 6) {
            year = Long.valueOf(start.substring(0, 4)).intValue();
            month = Long.valueOf(start.substring(4, 6)).intValue();
            if (start.length() >= 8) {
                day = Long.valueOf(start.substring(6, 8)).intValue();
            }
        } else {
            return start;
        }
        GregorianCalendar curCal = new GregorianCalendar(year, month - 1, day);
        if (start.length() >= 8) {
            curCal.add(GregorianCalendar.DAY_OF_MONTH, days);
        } else {
            curCal.add(GregorianCalendar.MONTH, days + 1);
        }

        String date = "";
        String tmpStr = "";
        date = String.valueOf(curCal.get(Calendar.YEAR));
        tmpStr = String.valueOf(curCal.get(Calendar.MONTH) + 1);
        if (tmpStr.length() < 2) {
            tmpStr = "0" + tmpStr;
        }
        date += tmpStr;
        tmpStr = String.valueOf(curCal.get(Calendar.DAY_OF_MONTH));
        if (tmpStr.length() < 2) {
            tmpStr = "0" + tmpStr;
        }
        if (day > 0) {
            date += tmpStr;
        }

        return date;
    }

    /**
     * 用于增加空值，null值及字符串“null”、“NULL”等值的判断
     * @return
     */
    public static Boolean NotEmptyAndNull(Object val) {
        if (ObjectUtil.isEmpty(val)) {
            return false;
        }
        if ("null".equals(String.valueOf(val).toLowerCase())) {
            return false;
        }
        return true;
    }

    /**
     * 检查字段超长
     * @param value 字段值
     * @param length 限定长度
     * @return  true 超长，false 不超
     */
    public static boolean checkStringLengthOver(String value,int length){
        if(value == null){
            return false;
        }else if(StringUtils.isNotBlank(value) && value.codePoints().count() > length){
           return true;
        }else{
            return false;
        }
    }
    /**
     * 检查字段超长
     * @param value 值
     * @param valueDesc 值的中文描述
     * @param length 值的限长
     * @param noEmpty 0-不必填，1必填
     * @return
     */
    public static String checkStringLength(String value,String valueDesc,int length,String noEmpty){
        String errString = "";
        if(noEmpty.equals("1")){
            if(StringUtils.isBlank(value)){
                errString = valueDesc + "要素不可为空。<br/>";
            }else if(value.codePoints().count() > length){
                errString = valueDesc +"过长，字段限长为" + length+"。<br/>";
            }
        }else if(noEmpty.equals("0")){
            if(StringUtils.isNotBlank(value) && value.codePoints().count() > length){
                errString = valueDesc +"过长，字段限长为" + length+"。<br/>";
            }
        }
        return errString;
    }
    /**
     * 返回检查金额
     * @param amount 金额
     * @param amountDesc 中文描述
     * @param regex  表达式
     * @param regexDesc  描述格式
     * @param noEmpty  0-不必填，1必填
     * @param noLessZero  0-不校验，1-必须大于等于0
     * @return
     */
    public static String checkMoney(String amount,String amountDesc,String regex,String regexDesc,String noEmpty,String noLessZero){
        String stringErr ="";
        int isBaifen = 0;
        if(regexDesc.equals("n（8,5）") || regexDesc.equals("n(8,5)")){
            isBaifen = 1;
        }
        String amount1=amount;
        if(StringUtils.isNotBlank(amount) && amount.startsWith("-") && amount.length()>1){
            amount1=amount.substring(1);
        }
        if(noEmpty.equals("1") && noLessZero.equals("1")){//不可空，必须控制大于等于0
            if(StringUtils.isBlank(amount)){
                stringErr = amountDesc +"要素不可为空。<br/>";
            } else {
                Pattern p=Pattern.compile(regex);
                Matcher m=p.matcher(amount1);
                if( isBaifen == 0 && !m.matches()){
                    stringErr = amountDesc +"必须为"+regexDesc+"格式。<br/>";
                }else if(isBaifen == 1 && !m.matches()){
                    stringErr = amountDesc +"以百分数形式表示时必须为n（8,5）格式。<br/>";
                }
                if(!"".equals(stringErr)){
                    return stringErr;
                }
                if(amount.compareTo("0") < 0){
                    stringErr = amountDesc +"必须大于等于0。<br/>";
                }
            }
        }else if(noEmpty.equals("1") && noLessZero.equals("0")){//不可空，不判断0
            if(StringUtils.isBlank(amount)){
                stringErr = amountDesc +"要素不可为空。<br/>";
            } else {
                Pattern p=Pattern.compile(regex);
                Matcher m=p.matcher(amount1);
                if( isBaifen == 0 && !m.matches()){
                    stringErr = amountDesc +"必须为"+regexDesc+"格式。<br/>";
                }else if(isBaifen == 1 && !m.matches()){
                    stringErr = amountDesc +"以百分数形式表示时必须为n（8,5）格式。<br/>";
                }
            }
        }else if(noEmpty.equals("0") && noLessZero.equals("1")){//可空，必须控制大于等于0
            if(StringUtils.isNotBlank(amount)){
                Pattern p=Pattern.compile(regex);
                Matcher m=p.matcher(amount1);
                if( isBaifen == 0 && !m.matches()){
                    stringErr = amountDesc +"必须为"+regexDesc+"格式。<br/>";
                }else if(isBaifen == 1 && !m.matches()){
                    stringErr = amountDesc +"以百分数形式表示时必须为n（8,5）格式。<br/>";
                }
                if(!"".equals(stringErr)){
                    return stringErr;
                }
                if(amount.compareTo("0") < 0){
                    stringErr = amountDesc +"必须大于等于0。<br/>";
                }
            }
        }else if(noEmpty.equals("0") && noLessZero.equals("0")){//可空，不判断0
            if(StringUtils.isNotBlank(amount)){
                Pattern p=Pattern.compile(regex);
                Matcher m=p.matcher(amount1);
                if( isBaifen == 0 && !m.matches()){
                    stringErr = amountDesc +"必须为"+regexDesc+"格式。<br/>";
                }else if(isBaifen == 1 && !m.matches()){
                    stringErr = amountDesc +"以百分数形式表示时必须为n（8,5）格式。<br/>";
                }
            }
        }
        return stringErr;
    }

    /**
     * 非空校验返回
     * @param value
     * @param valueDesc
     * @return
     */
    public static String isNotEmpty(String value,String valueDesc){
        String stringErr = "";
        if (StringUtils.isBlank(value)) {
            stringErr = valueDesc+"要素不可为空。<br/>";
        }
        return stringErr;
    }

    /**
     * 条件性必填校验
     * @param value
     * @param valueDesc
     * @param noEmptyType
     * @param type
     * @param typeDesc
     * @return
     */
    public static String checkAssetEmpty(String value, String valueDesc, String noEmptyType, String type, String typeDesc,String noEmpty){
        String stringErr = "";
        if(noEmpty.equals("0")){
            if(!noEmptyType.contains(type) && StringUtils.isNotBlank(value) ){
                stringErr = "当资产/负债类别不为"+ typeDesc +"时，"+ valueDesc +"要素必须为空。<br/>";
            }
        }else if (noEmpty.equals("1") && StringUtils.isNotBlank(noEmptyType) && StringUtils.isNotBlank(type) ) {
            if(noEmptyType.contains(type) && StringUtils.isBlank(value) ){
                stringErr = "当资产/负债类别为"+ typeDesc +"时，"+ valueDesc +"要素不可为空。<br/>";
            }else if(!noEmptyType.contains(type) && StringUtils.isNotBlank(value) ){
                stringErr = "当资产/负债类别不为"+ typeDesc +"时，"+ valueDesc +"要素必须为空。<br/>";
            }
        }
        return stringErr;
    }

    /**
     * 检查日期格式
     * @param value
     * @param valueDesc
     * @return
     */
    public static String checkDate(String value, String valueDesc){
        String stringErr = "";
        if(StringUtils.isNotBlank(value)){
            if (value.codePoints().count() == 8) {
                Pattern p = Pattern.compile("^(\\d{8})");
                Matcher m = p.matcher(value);
                if (!m.matches()) {
                    stringErr = valueDesc+"必须为日期格式（YYYYMMDD）。<br/>";
                } else if (!CheckDataUtils.isLegalDate(8, value, "yyyyMMdd")) {
                    stringErr = valueDesc+"必须为正确日期。<br/>";
                }
            } else {
                Pattern p = Pattern.compile("^(\\d{4}\\-\\d{2}\\-\\d{2})");
                Matcher m = p.matcher(value);
                if (!m.matches()) {
                    stringErr = valueDesc+"必须为日期格式（YYYY-MM-DD）。<br/>";
                } else if (!CheckDataUtils.isLegalDate(10, value, "yyyy-MM-dd")) {
                    stringErr = valueDesc+"必须为正确日期。<br/>";
                }
            }
        }
        return stringErr;
    }

    public static String checkDateAfer(String value, String valueDesc){
        String stringErr="";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate inputDate = LocalDate.parse(value.replace("-",""),dateTimeFormatter);
        LocalDate nowDate = LocalDate.now();
        if(inputDate.isAfter(nowDate)){
            stringErr = valueDesc+"不得晚于当前日期。<br/>";
        }

        return stringErr;
    }

    /**
     * 比较两个string的大小，前面是大的，后面是小的
     * @param min
     * @param max
     * @return
     */
    public static int compareTo(String max,String min){
        Pattern p=Pattern.compile("^(\\d{1,30}(\\.\\d{1,10})?)");
        int result = 0;
        if(StringUtils.isNotBlank(max) && StringUtils.isNotBlank(min) ){
            if(min.contains("-") && max.contains("-")){
                max = max.replaceAll("-","");
                min = min.replaceAll("-","");
            }else if(min.contains(",") && max.contains(",")){
                max = max.replaceAll(",","");
                min = min.replaceAll(",","");
            }
            Matcher m1=p.matcher(min);
            Matcher m2=p.matcher(max);
            if(m1.matches() && m2.matches()){
                result = new BigDecimal(max).compareTo(new BigDecimal(min));
            }else {
                result = max.compareTo(min);
            }
        }
        return result;

    }

    /**
     * 检查多选框币种字段是否在字典范围内
     * @param dict
     * @param value
     * @param isKey 0-判断key值，1-判断value
     * @return
     */
    public static String checkMultipleDict(String dict, String value,int isKey) throws Exception {
        String desc = "";

        if("".equals(value) || value==null){
            return desc;
        }else if(value.contains(",")){
            String[] valueArr = value.split(",");
            List<String> valueList = Arrays.asList(valueArr);
            if (hasDuplicates(valueList)) {
                desc = "不可填写重复选项。<br/>";
                Set<String> duplicates = findDuplicates(valueList);
                log.info("重复选项: " + duplicates);
            }

            for (String s : valueList) {
                if(StringUtils.isBlank(s) || s.equals(" ")){
                    desc = "拼接字段存在空值。<br/>";
                    break;
                }else{
                    if(isKey == 0 && !getDictCheckKey(dict, s)){
                        desc = "不在值域范围内。<br/>";
                        break;
                    }else if(isKey == 1 && !getDictCheckValue(dict, s)){
                        desc = "不在值域范围内。<br/>";
                        break;
                    }
                }
            }
        }else{
            if(isKey == 0 && !getDictCheckKey(dict, value)){
                desc = "不在值域范围内。<br/>";
            }else if(isKey == 1&& !getDictCheckValue(dict, value)){
                desc = "不在值域范围内。<br/>";
            }
        }
        return desc;
    }

    public static boolean hasDuplicates(List<String> list) {
        Set<String> seen = new HashSet<>();
        for (String item : list) {
            if (!seen.add(item)) {  // add() 返回 false 表示已存在
                return true;         // 发现重复项
            }
        }
        return false;
    }
    public static Set<String> findDuplicates(List<String> list) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String item : list) {
            if (!seen.add(item) && duplicates.add(item)) {
                // 仅当是第一次发现重复时添加到 duplicates
            }
        }
        return duplicates;
    }
}
