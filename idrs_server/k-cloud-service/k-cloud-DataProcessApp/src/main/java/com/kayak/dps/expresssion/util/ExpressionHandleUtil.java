package com.kayak.dps.expresssion.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.kayak.clear.utils.DateUtils;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.BigDecimalUtil;
import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.check.model.ExpressDictDTO;
import com.kayak.dps.check.util.ExpressDictUtil;
import com.kayak.dps.expresssion.enums.ExpressionRegrexEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ExpressionHandleUtil {

    private static final String regex = "[1-9NY]{1}[1-9]{1}[1-6]{1}[0-9]{5}[0123456789ABCDEFGHIJKLMNOPQRSTUVWXY]{10}";//统一社会信用编码正则验证1
    private static final int[] weight = {1,3,9,27,19,26,16,17,20,29,25,13,8,24,10,30,28};
    private static final String base_code_string = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXY";
    private static final char[] base_code_array = base_code_string.toCharArray();
    private static final List<Character> base_codes = new ArrayList<>();

    /**
     * 登记管理部门+机构类别代码
     */
    private static final List<String> REGISTER_CODE = Arrays.asList("11","12","13","19",
            "21","29",
            "31","32","33","34","35","39",
            "41","49",
            "51","52","53","59",
            "61","62","69",
            "71","72","79",
            "81","89",
            "91","92","93",
            "A1","A9",
            "N1","N2","N3","N9",
            "Y1");

    /**
     * 省级行政区划分代码
     */
    private static final List<String>  PROVINCE_REGION_CODE = Arrays.asList("11","12","13","14","15",
            "21","22","23",
            "31","32","33","34","35","36","37",
            "41","42","43","45","46",
            "50","51","52","53","54",
            "61","62","63","64","65",
            "71",
            "81","82",
            "91");

    /**
     * unicode编码半角（全角）符号白名单
     */
    private static final List<String> WHITE_CHAR_UNICODE = Arrays.asList("0020", "0021", "0022", "0023", "0024", "0025",
            "0026", "0027", "0028", "0029", "002A", "002B", "002C", "002D", "002E", "002F", "003A", "003B", "003C", "003D", "003E",
            "003F", "0040", "005B", "005C", "005D", "005E", "005F", "0060", "007B", "007D", "007E");

    static {
        for(char cs : base_code_array){
            base_codes.add(cs);
        }
    }

    private static DataValidateDao dataValidateDao = SysBeans.getBean("dataValidateDao");

    /**
     * 截取获得多参数
     * @return
     */
    public static String[] getDoubleData(String key) throws Exception {
        String[] keys = key.split(",");

        return keys;
    }

    /**
     * 截取获得repeatx、dictx定制函数双参数
     * @return
     */
    public static String[] getDoubleDataForRep(String key) throws Exception {
        String[] keys = new String[2];
        keys[0] = key.substring(0, key.indexOf(","));
        keys[1] = key.substring(key.indexOf(",") + 1);

        return keys;
    }

    /**
     * 截取获得表、字段参数
     * @return
     */
    public static String[] getTableData(String key) throws Exception {
        if (!key.contains(".")) {
            throw new Exception("无分区参数");
        }
        String[] keys = key.split("\\.");
        if (keys.length != 2) {
            throw new Exception("切 割表.字段 形式超出预期长度2，不符合格式");
        }
        return keys;
    }

    /**
     * 取YYYY-MM-DD格式中的月份
     * @return
     */
    public static String getDateMonth(String key) throws Exception {
        if (ObjectUtil.isEmpty(key) || !com.kayak.core.util.DateUtil.isLegalDate(key)) {
            throw new Exception("解析VALUE_DM关键字，不符合日期格式规范");
        }
        return key.substring(5, 7);
    }

    /**
     * 判断是否符合特定的日期格式
     * @return
     */
    public static Boolean valDatePa(String key) throws Exception {
        String pattern1 = "^([\\d]{4}((((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-8])))))|((((([02468][048])|([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-9]))))){4})$";
        String pattern2 = "^(?:(?!0000)[0-9]{4}年(?:(?:0?[1-9]|1[0-2])月(?:0?[1-9]|1[0-9]|2[0-8])日|(?:0?[13-9]|1[0-2])月((?:29|30)日)|(?:0?[13578]|1[02])月31日)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)\\年0?2\\月29日)$";
        String pattern3 = "^(?:(?!0000)[0-9]{4}\\/(?:(?:0?[1-9]|1[0-2])\\/(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\\/(?:29|30)|(?:0?[13578]|1[02])\\/31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)\\/0?2\\/29)$";
        String pattern4 = "^(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)$";

        return ReUtil.isMatch(pattern1, key) || ReUtil.isMatch(pattern2, key)
                || ReUtil.isMatch(pattern3, key) || ReUtil.isMatch(pattern4, key);
    }


    /**
     * 判断是否符合制定的日期格式，如yyyyMMdd
     * @return
     */
    public static Boolean valDatePa(String key, String target) throws Exception {
        if (ObjectUtil.isEmpty(key) || key.length() != target.length()) {
            return false;
        }
        try {
            DateUtil.parse(key, target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验日期是否为最后一个自然日（日期格式限制为yyyyMMdd）
     * @param key
     * @return
     * @throws Exception
     */
    public static Boolean valDateLastDay(String key) throws Exception {
        String dateLastDay = DateUtils.getLastDayOfMonth(key);
        return ObjectUtil.equals(key, dateLastDay);
    }

    /**
     * 日期Between判断
     * @return
     */
    public static Boolean bettwenDate(String key, String startDate, String endDate) throws Exception {
        if (DateUtil.parse(key).compareTo(DateUtil.parse(startDate)) > 0 &&
                DateUtil.parse(key).compareTo(DateUtil.parse(endDate)) < 0) {
            return true;
        }
        return false;
    }


    /**
     * 解析map获取表、字段对应的值
     * keyData: 表.字段结构
     * map: key为表.字段结构， value为值
     */
    public static Object getMapValue(String keyData, Map<String, String> map) {
        return map.get(keyData);
    }

    /**
     * 解析map获取表、字段对应的值(针对二维报表坐标)
     * keyData: 表.字段结构
     * map: key为表.字段结构， value为值
     */
    public static Object getCoorMapValue(String keyData, Map<String, Object> map) {
        return map.get(keyData);
    }

    /**
     * date校验相差天数计算
     * @return
     */
    public static Integer difDays(String type, String startDate, String endDate) throws Exception {
        // 先判断两个日期是否格式正确并相同

        Integer difDay = new Integer(0);

        if ("自然日".equals(type)) {
            difDay = com.kayak.core.util.DateUtil.computeTwoDateDays(startDate, endDate, "yyyy-MM-dd");
        }

        if ("工作日".equals(type)) {
            difDay = dataValidateDao.getWorkDays(startDate, endDate);
        }

        return difDay;
    }

    /**
     * 字段组合唯一性校验
     * @return
     */
    public static Boolean difUniqueCheck(String[] valueData, Map<String, String> map, List<Map<String, String>> paramList) throws Exception {
        AtomicReference<Integer> ret = new AtomicReference<>(0);
        paramList.stream().forEach(
                o -> {
                    int j = 0;
                    for (int i = 0; i < valueData.length; i++) {
                        if (ObjectUtil.equals(o.get(valueData[i]), map.get(valueData[i]))) {
                            j++;
                        }
                    }
                    if (j == valueData.length) {
                        ret.getAndSet(ret.get() + 1);
                        if (ret.get() > 2) {
                            return;
                        }
                    }
                }
        );

        return ret.get() < 2;
    }

    /**
     * 字段在系统表中重复性查询
     * key 表名.字段
     * val 值
     *
     * @return
     */
    public static Boolean difSUniqueCheck(String key, String val) throws Exception {
        String[] valueData = key.split("\\.");
        return dataValidateDao.getUniqueIndexTable(valueData, val);
    }

    /**
     * 字典值查询
     * @return
     */
    public static String checkDictItem(String[] valueData) throws Exception {
        List<ExpressDictDTO> dictList = ExpressDictUtil.dictMap.get(valueData[0]);
        //k、v、k+空格+v 三种形式值域均可通过
        for (ExpressDictDTO expressDictDTO : dictList) {
            if (ObjectUtil.equals(valueData[1], expressDictDTO.getDictVal())) {
                return expressDictDTO.getDictKey();
            }
        }
        return dataValidateDao.getDictKey(valueData[0], valueData[1]);
    }


    /**
     * 字典值存在校验
     * @param key 字典的key
     * @param value 校验的值
     * @return
     * @throws Exception
     */
    public static Boolean existDictItem(String key, String value) throws Exception {
        return dataValidateDao.exitDictKey(key, value);
    }

    /**
     * 多选字典值存在校验
     * @param repeat 是否可重复
     * @return
     * @throws Exception
     */
    public static Boolean existDictItems(String key, String value, Boolean repeat) throws Exception {
        //支持分号;和逗号,分割
        String[] dictValues = value.split(",");
        if (value.contains(";")) {
            dictValues = value.split(";");
        }
        //验证重复值
        if (!repeat) {
        Set<String> dictSet = new HashSet<>();
        for (int i=0;i<dictValues.length;i++) {
            dictSet.add(dictValues[i]);
        }
        if (dictValues.length != dictSet.size()) {
            return false;
        }
        }
        return dataValidateDao.exitDictKeys(key, dictValues);
    }

    /**
     * 正则分类校验
     * @return
     */
    public static Boolean regRexExp(String key, String val) throws Exception {

        if (ExpressionRegrexEnum.EMAIL.getVal().equals(key)) {
            String reg = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-.]+(\\.\\w+)+$";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.IDENT.getVal().equals(key)) {
            String reg = "^[a-zA-Z0-9]{32}$|^[a-zA-Z0-9]{46}$";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.LCZS.getVal().equals(key)) {
            String reg = "LC[0-9]{12}";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.YHDM.getVal().equals(key)) {
            String reg = "C[0-9]{5}";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.TEL.getVal().equals(key)) {
            String reg = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.FIX_TEL.getVal().equals(key)) {
            String reg = "^\\d{7,8}|\\d{3,4}-\\d{7,8}|\\d{7,8}-\\d{1,8}|\\d{3,4}-\\d{7,8}-\\d{1,8}$";
            return ReUtil.isMatch(reg, val) || "11111111".equals(val);
        }
        if (ExpressionRegrexEnum.RATE.getVal().equals(key)) {
            return !"%".equals(val.substring(val.length() - 1));
        }
        if (ExpressionRegrexEnum.CREDIT_CD.getVal().equals(key)) {
            String reg = "[a-zA-Z0-9]{15}||[a-zA-Z0-9]{18}";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.HHMMSS.getVal().equals(key)) {
            String reg = "([0-1]?[0-9]|2[0-3])([0-5][0-9])([0-5][0-9])";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.SPV.getVal().equals(key)) {
            // 查询发行机构代码
            String paraValue = dataValidateDao.getParaValueForFXJGDM();
            String reg = "^"+paraValue+"1[0-9]{2}[0-9]{5}$";
            return SPVRegrex(reg, val);
        }
        if (ExpressionRegrexEnum.OCC_PRC_CD.getVal().equals(key)) {
            String reg = "[a-zA-Z0-9]{9}";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.RZR_ZJ_CD.getVal().equals(key)) {
            String reg = "[A-Z0-9]{9}||[A-Z0-9]{18}";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.ZJ_TG_ZH.getVal().equals(key)) {
            String reg = "[A-Za-z0-9-]*";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.SFZ_CD.getVal().equals(key)) {
            String reg = "([1-9][0-9]{14})|([1-9][0-9]{16}[0-9xX])";
            return ReUtil.isMatch(reg, val);
        }
        if (ExpressionRegrexEnum.SOC_CRED_CD.getVal().equals(key)) {
            //校验社会统一信用编码
            if (StringUtils.isEmpty(val)) {
                return true;
            }
            //长度验证
            if(val.length() != 18){
                return false;
            }
            //机构管理部门+机构类别代码验证
            if(!REGISTER_CODE.contains(val.substring(0,2))){
                return false;

            }
            //省级行政区划分代码验证
            if(!PROVINCE_REGION_CODE.contains(val.substring(2,4))){
                return false;
            }
            //正则验证1
            if(!Pattern.matches(regex,val)){
                return false;
            }
            //校验码验证
            char[] front17 = val.toCharArray();
            String check = Character.toString(front17[17]);
            int sum = 0;
            for(int i=0; i<17; i++){
                char keyNum = front17[i];
                sum += (Integer.parseInt(String.valueOf(base_codes.indexOf(keyNum))) * weight[i]);
            }

            int check_code = 31 - sum % 31;
            String last = Character.toString(base_code_array[check_code % 31]);//根据规则获取最后一位编码

            return last.equalsIgnoreCase(check);//比对判断得出的最后一位编码是否与传入值一致
        }
        throw new Exception("Regrex未匹配到类型");
    }

    /**
     * SPV校验处理
     * @return
     */
    private static Boolean SPVRegrex(String reg, String val) throws Exception {
        // 非15位，校验错误
        if (val.length() != 15) {
            return false;
        }
        String left = val.substring(0, 14);
        String right = val.substring(14);
        if (!NumberUtil.isNumber(right)) {
            throw new Exception("末位非数字, 暂不支持非标准化的SPV值校验");
        }

        // 前14位非SPV规则，校验错误
        if (!ReUtil.contains(reg, left)) {
            return false;
        }

        // 计算最后一个校验位
        int mod = 0;
        int mid = 0;
        String[] leftArr = left.split("");
        for (int i = 0; i < leftArr.length; i++) {
            // 将大写字母转换为其对应的小写罗马数字
            if (i == 0) {
                if (leftArr[i].matches("[A-Z]")) {
                    mid = leftArr[i].charAt(0) - 64;
                    if (mid >= 10) {
                        mod += (mid / 10 + mid % 10);
                    } else {
                        mod += mid;
                    }
                    continue;
                }
            }
            if (i % 2 != 0) {
                mid = Integer.parseInt(leftArr[i]) * 2;
                if (mid >= 10) {
                    mod += (mid / 10 + mid % 10);
                } else {
                    mod += mid;
                }
            } else {
                mod += Integer.parseInt(leftArr[i]);
            }
        }
        if (mod % 10 == 0) {
            return Integer.parseInt(right) == 0;
        } else {
            return Integer.parseInt(right) == 10 - (mod % 10);
        }
    }


    /**
     * 字符长度符合校验
     * @return
     */
    public static Boolean exCharLength(String key, String length) throws Exception {
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        if (!NumberUtil.isInteger(length)) {
            throw new Exception("CHAR函数长度参数异常");
        }
        return Integer.valueOf(length) == key.length();
    }

    /**
     * 小数位符合长度校验
     * @return
     */
    public static Boolean numberXLength(String key, Integer length, Integer dLength) throws Exception {
        //检测为0直接通过
        if (ObjectUtil.isNotEmpty(key) && NumberUtil.isNumber(key) && BigDecimal.ZERO.equals(new BigDecimal(key))) {
            return true;
        }
        //不为空、不为数字
        if (ObjectUtil.isNull(key)|| !NumberUtil.isNumber(key)) {
            return false;
        }
        //校验总位数不大于设定值
        if (key.length() > length + 1) {
             return false;
        }
        //不为小数且长度超过阀值
        if (!key.contains(".") && key.length() > length) {
            return false;
        }

        //小数不大于设定值
        if (key.contains(".") ) {
            String pre = key.substring(0, key.indexOf("."));
            String dict = key.substring(key.indexOf(".") + 1);
            if (pre.length() > length - dLength || dict.length() > dLength) {
                return false;
            }
        }

        return true;
    }

    /**
     * 校验数字且长度小于等于参数
     * @return
     */
    public static Boolean numberDLength(String key, Integer length) throws Exception {
        // 不为空、不为数字
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        // 校验总位数
        if (key.length() > length) {
            return false;
        }

        return true;
    }

    /**
     * 校验数组重复性
     * @return
     */
    public static Boolean repeatXHandle(String key, String tag) throws Exception {
        // 不为空
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        String[] keyArr = key.split("\\" + tag);
        Set<String> keySet = new HashSet<>();
        for (int i = 0; i < keyArr.length; i++) {
            keySet.add(keyArr[i]);
        }

        return keySet.size() == keyArr.length;
    }

    /**
     * 校验逗号隔开的多选字段不能有空
     * @return
     */
    public static Boolean repeatNullNHandel(String key) throws Exception {
        //不为空
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        String[] keyArr = key.split(",");
        Set<String> keySet = new HashSet<>();
        for (int i = 0; i < keyArr.length; i++) {
            if(ObjectUtil.isEmpty(keyArr[i])) {
               return false;
            }
        }
        return true;
    }

    /**
     * 校验数组重复性
     * @return
     */
    public static int repeatValueRepeatNumHandel(String key, String tag) throws Exception {
        String[] keyArr = key.split("\\" + tag);
        return keyArr.length;
    }

    /**
     * 校验字符串是否全为英文或数字
     * @return
     */
    public static Boolean checkEN(String key) throws Exception {
        // 不为空
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        String reg = "[a-zA-Z0-9]+";
        return ReUtil.isMatch(reg, key);
    }

    /**
     * 校验字符串是否包含中文
     * @return
     */
    public static Boolean checkC(String key) throws Exception {
        // 不为空
        if (ObjectUtil.isNull(key)) {
            return false;
        }
        String reg = ".*[\\u4e00-\\u9fa5]+.*";
        return key.matches(reg);
    }



    /**
     * 验证银行代码是否为系统参数对应代码
     * @param val
     * @return
     */
    public static Boolean bankCode(String val) throws Exception {
        if (ObjectUtil.isEmpty(val)) {
            return false;
        }
        String sysVal = dataValidateDao.getParaValueForFXJGDM();
        return ObjectUtil.equals(val, sysVal);
    }

    /**
     * 校验字符串为英文、数字、白名单内的符号组成
     * @param key
     * @return
     * @throws Exception
     */
    public static Boolean whiteChar(String key) throws Exception {

        // 特殊函数定制制定魔法值,校验所有募集金额大于0
        for (int i = 0; i < key.length(); i++) {
            if (!checkEN(String.valueOf(key.charAt(i))) && WHITE_CHAR_UNICODE.contains(String.valueOf(key.charAt(i)))) {
                return false;
            }
        }

        return true;
    }


}
