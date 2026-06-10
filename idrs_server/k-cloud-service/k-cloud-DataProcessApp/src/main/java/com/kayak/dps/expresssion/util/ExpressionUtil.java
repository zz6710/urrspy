package com.kayak.dps.expresssion.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.dps.check.constants.ErrorCollectionConstants;
import com.kayak.dps.check.util.PrimaryDataCheckUtil;
import com.kayak.dps.direct.model.dto.IndexCodeResultDTO;
import com.kayak.dps.expresssion.enums.ExpressionEnum;
import com.kayak.dps.expresssion.model.dto.ExpressDTO;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.util.*;

public class ExpressionUtil {

    private static ThreadLocal<Map<String, String>> map = new ThreadLocal<Map<String, String>>();
    // js引擎
    private static ScriptEngineManager manager = new ScriptEngineManager();
    private static ThreadLocal<ScriptEngine> engine = new ThreadLocal<ScriptEngine>();

    /**
     * 二维表间校验日志展示值
     */
    private static List<String> c2RowLogList = new ArrayList<>();

    /**
     * 校验表达式-指标校验结果计算
     * val 校验公式
     * params 数据表
     * indexCode 指标编号
     * @return
     */
    public static IndexCodeResultDTO checkExpressCorOne(ExpressDTO expressDTO, Map<String, String> params) {
        c2RowLogList.clear();
        map.remove();
        engine.remove();
        Boolean ret = false;
        String val = expressDTO.getExpressVal();


        // TODO: 2023/5/19 临时处理误差值的扩展计算情况
        if (val.contains("VALUE_P(")) {
            expressDTO.setDeviation(expressDTO.getDeviation().multiply(new BigDecimal(100)));
        }
        try {
            engine.set(manager.getEngineByName("js"));
            map.set(params);
            val = equalHandle(val);

            val = dictHandle(val);
            val = dictXHandle(val);
            val = dictSHandle(val);
            val = nullHandle(val);
            val = nullNHandle(val);
            val = condHandle(val);
            val = keydHandle(val);
            val = absHandle(val);
            val = contHandle(val);
            val = contNHandle(val);
            val = dateRHandle(val);
            val = dateHandle(val);
            val = dateSHandle(val);
            val = dateLastDayHandle(val);
            val = regRexHandle(val);
            val = valueDMHandle(val);
            val = valueAHandle(val);
            val = valuePHandle(val);
            val = minusDHandle(val);
            val = difSHandle(val);
            val = exCharHandel(val);
            val = subStrHandel(val);
            val = lengthXHandel(val);
            val = numberXHandel(val);
            val = numberDHandel(val);
            val = repeatXHandel(val);
            val = repeatNullNHandel(val);
            val = repeatValueRepeatNumHandel(val);
            val = checkENCode(val);
            val = checkNCode(val);
            val = checkINTCode(val);
            val = checkCCode(val);
            val = cpxsmjHandel(val);
            val = cpmjbzHandel(val);
            val = ccdjAsCdHandel(val);
            val = multiSpecialValHandle(val);
            val = bankCodeHandel(val);

            //白名单
            val = whiteCharHandel(val);
            val = whiteAllHandel(val);
            val = whiteXHandel(val);
            val = whiteThAllHandel(val);
            val = whiteThXHandel(val);

            val = typeAndRatioHandle(val);

            val = deviationHandle(val, expressDTO.getDeviation());

            ret = (Boolean) engine.get().eval(val);
        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + expressDTO.getIndexCode() + " 数据校验异常: " + e.getMessage();
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
        }

        return IndexCodeResultDTO.initDTO().setIndexCode(expressDTO.getIndexCode()).setExpressVal(val).setRetVal(ret);
    }

    /**
     * 校验表达式-指标校验结果计算
     * val 校验公式
     * params 数据表
     * indexCode 指标编号
     * @return
     */
    public static IndexCodeResultDTO checkExpress(ExpressDTO expressDTO, Map<String, String> params, List<Map<String, String>> paramList, Map<String, Object> c2RowDataMap) {
        c2RowLogList.clear();
        map.remove();
        engine.remove();
        Boolean ret = false;
        String val = expressDTO.getExpressVal();

        // TODO: 2023/5/19 临时处理误差值的扩展计算情况
        if (val.contains("VALUE_P(")) {
            expressDTO.setDeviation(expressDTO.getDeviation().multiply(new BigDecimal(100)));
        } else if (val.contains("COOR(")) {
            expressDTO.setDeviation(expressDTO.getDeviation().multiply(new BigDecimal(1000000)));
        }
        try {
            engine.set(manager.getEngineByName("js"));
            map.set(params);
            val = equalHandle(val);

            val = dictHandle(val);
            val = dictXHandle(val);
            val = dictSHandle(val);
            val = nullHandle(val);
            val = nullNHandle(val);
            val = condHandle(val);
            val = keydHandle(val);
            val = absHandle(val);
            val = contHandle(val);
            val = contNHandle(val);
            val = dateRHandle(val);
            val = dateHandle(val);
            val = dateSHandle(val);
            val = dateLastDayHandle(val);
            val = regRexHandle(val);
            val = valueDMHandle(val);
            val = valueAHandle(val);
            val = valuePHandle(val);
            val = valuePNMHandle(val, paramList);
            val = minusDHandle(val);
            val = difHandle(val, paramList);
            val = difSHandle(val);
            val = exCharHandel(val);
            val = subStrHandel(val);
            val = lengthXHandel(val);
            val = numberXHandel(val);
            val = numberDHandel(val);
            val = repeatXHandel(val);
            val = repeatNullNHandel(val);
            val = repeatValueRepeatNumHandel(val);
            val = checkENCode(val);
            val = checkNCode(val);
            val = checkINTCode(val);
            val = checkCCode(val);
            val = coorHandel(val, c2RowDataMap);
            val = cpxsmjHandel(val);
            val = cpmjbzHandel(val);
            val = ccdjAsCdHandel(val);
            val = multiSpecialValHandle(val);
            val = bankCodeHandel(val);

            //白名单
            val = whiteCharHandel(val);
            val = whiteAllHandel(val);
            val = whiteXHandel(val);
            val = whiteThAllHandel(val);
            val = whiteThXHandel(val);

            val = typeAndRatioHandle(val);

            val = deviationHandle(val, expressDTO.getDeviation());

            ret = (Boolean) engine.get().eval(val);
        } catch (Exception e) {
            String error_mess = "报送数据: 校验指标" + expressDTO.getIndexCode() + " 数据校验异常: " + e.getMessage();
            PrimaryDataCheckUtil.ErrorInfoRecordHandle(ErrorCollectionConstants.ERROR_DATA_VALIDATE_REPORT, error_mess);//base_error_message记录异常报错报错信息
        }
        return IndexCodeResultDTO.initDTO().setIndexCode(expressDTO.getIndexCode()).setExpressVal(val).setRetVal(ret).setC2RowLogList(c2RowLogList);
    }


    /**
     * 公用占位符临时替换
     * @return
     */
    private static String commonTagRep(String val, String tag) {
        return val.replace(tag + "(", ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
    }


    /**
     * 等号扩充
     * @return
     */
    public static String equalHandle(String val) {
        if (val.contains("=")) {
            val = val.replaceAll("=", "==");
        }
        if (val.contains("!==")) {
            val = val.replaceAll("!==", "!=");
        }
        if (val.contains(">==")) {
            val = val.replaceAll(">==", ">=");
        }
        if (val.contains("<==")) {
            val = val.replaceAll("<==", "<=");
        }
        if (val.contains("<>")) {
            val = val.replaceAll("<>", "!=");
        }
        return val;
    }

    /**
     * 误差值计算
     * @return
     */
    private static String deviationHandle(String val, BigDecimal deviation) throws ScriptException {

        if (!val.contains("==") || (val.contains("?")&&val.contains(":")) || deviation.compareTo(new BigDecimal(0)) == 0) {
            return val;
        }

        // 计算 || 的情况
        if (val.contains("||")) {
            String[] valArr = val.split("||");
            // 分类计算多个等式的误差情况
            for (int i = 0; i < valArr.length; i++) {
                if (deviationCount(valArr[i], deviation)) {
                    return "true";
                }
            }
            return "false";
        }

        // 计算 && 的情况
        if (val.contains("&&")) {
            String[] valArr = val.split("&&");
            // 分类计算多个等式的误差情况
            for (int i = 0; i < valArr.length; i++) {
                if (!deviationCount(valArr[i], deviation)) {
                    return "false";
                }
            }
            return "true";
        }

        // 单等号计算
        if (deviationCount(val, deviation)) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * 数字相等情况下的误差值计算
     * @return
     */
    private static Boolean deviationCount(String val, BigDecimal deviation) throws ScriptException {

        String[] valT = val.split("==");
        String left = valT[0].trim();
        String right = valT[1].trim();

        /**
         * 双边为null，返回true
         */
        if (left.contains("null") && right.contains("null")) {
            return true;
        }

        /**
         * 单边为null，返回false
         */
        if (left.contains("null") || right .contains("null")) {
            return false;
        }

        BigDecimal retL = new BigDecimal(String.valueOf(engine.get().eval(valT[0].trim())));
        BigDecimal retR = new BigDecimal(String.valueOf(engine.get().eval(valT[1].trim())));
        if (retL.subtract(retR).abs().compareTo(deviation.multiply(new BigDecimal(100))) > 0) {
            return false;
        }

        return true;
    }


    /**
     * 处理DICT关键字
     * @return
     */
    public static String dictHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DICT.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.existDictItem(valueData[1], (String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DICT_X关键字
     * @return
     */
    public static String dictXHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DICT_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleDataForRep(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.existDictItems(valueData[1], (String) keyObj, false) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }


    /**
     * 处理DICT_S关键字
     * @return
     */
    public static String dictSHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DICT_S.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleDataForRep(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.existDictItems(valueData[1], (String) keyObj, true) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 字段非空判断
     * @param val
     * @return
     * @throws Exception
     */
    public static String nullNHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.NULL_N.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());

                valArr[i] = ObjectUtil.isNotEmpty(keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }

        return newVal;
    }

    /**
     * 字段空值判断
     * @param val
     * @return
     * @throws Exception
     */
    public static String nullHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.NULL.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());

                valArr[i] = ObjectUtil.isEmpty(keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal.replaceAll("NULY_N", "NULL_N");
    }

    /**
     * 处理COND关键字函数
     * @param val 待处理字符串
     * @return
     */
    public static String condHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.COND.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                if (ObjectUtil.isNotEmpty(keyObj)) {
                    engine.get().put((String) keyObj, keyObj);
                    if (NumberUtil.isNumber((CharSequence) keyObj)) {
                        BigDecimal keyDec = new BigDecimal(String.valueOf(keyObj));
                        valArr[i] = keyDec.multiply(new BigDecimal(100)) + valArr[i].substring(rightK + 1);
                    } else {
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                } else {
                    valArr[i] = null + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理VALUE_A关键字
     * @param val
     * @return
     */
    public static String valueAHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.VALUE_A.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                if (ObjectUtil.isNotEmpty(keyObj)) {
                    engine.get().put((String) keyObj, keyObj);
                    if (NumberUtil.isNumber((CharSequence) keyObj)) {
                        BigDecimal keyDec = new BigDecimal(String.valueOf(keyObj));
                        valArr[i] = keyDec + valArr[i].substring(rightK + 1);
                    } else {
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                } else {
                    valArr[i] = null + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理VALUE_P关键字
     * @param val
     * @return
     */
    public static String valuePHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.VALUE_P.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                if (ObjectUtil.isNotEmpty(keyObj)) {
                    // 判断为数值的情况
                    if (NumberUtil.isNumber((CharSequence) keyObj)) {
                        BigDecimal keyDec = new BigDecimal(String.valueOf(keyObj));
                        valArr[i] = keyDec.multiply(new BigDecimal(100)) + valArr[i].substring(rightK + 1);
                    } else {
                        engine.get().put((String) keyObj, keyObj);
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                } else {
                    valArr[i] = null + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }


    /**
     * 处理VALUE_P_NM关键字,一维、二维表间校验专属
     * @param val
     * @return
     */
    public static String valuePNMHandle(String val, List<Map<String, String>> paramList) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.VALUE_P_NM.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, paramList.get(0));
                if (ObjectUtil.isNotEmpty(keyObj)) {
                    engine.get().put((String) keyObj, keyObj);
                    if (NumberUtil.isNumber((CharSequence) keyObj)) {
                        BigDecimal keyDec = new BigDecimal(String.valueOf(keyObj));
                        valArr[i] = keyDec.multiply(new BigDecimal(1000000)) + valArr[i].substring(rightK + 1);
                    } else {
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                } else {
                    valArr[i] = "0" + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理KEY_D关键字
     * @param val
     * @return
     */
    public static String keydHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.KEY_D.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应表的字段值
                String keyObj = ExpressionHandleUtil.checkDictItem(valueData);

                if (ObjectUtil.isNotEmpty(keyObj)) {
                    engine.get().put(keyObj, keyObj);
                    if (NumberUtil.isNumber(keyObj)) {
                        BigDecimal keyDec = new BigDecimal(keyObj);
                        valArr[i] = keyDec.multiply(new BigDecimal(100)) + valArr[i].substring(rightK + 1);
                    } else {
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                } else {
                    valArr[i] = null + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理ABS
     * @param val
     * @return
     */
    public static String absHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.ABS.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 获取keyData对应表的字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                if (ObjectUtil.isEmpty(keyObj) || !NumberUtil.isNumber((String) keyObj)) {
                    throw new Exception("abs函数不支持此类型参数：" + keyObj);
                }
                double num = Double.parseDouble((String) keyObj);
                valArr[i] = Math.abs(num) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CONT关键字
     * @return
     */
    public static String contHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CONT.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ObjectUtil.contains(keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CONT_N关键字
     * @return
     */
    public static String contNHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CONT_N.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());
        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                // TODO: 2022/12/8 key需要调用去表值方法

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = !ObjectUtil.contains(keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DATE_R关键字
     * @return
     */
    public static String dateRHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DATE_R.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 三参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                // 拼接dict函数字段校验结果
                if (ObjectUtil.isEmpty(keyObj)) {
                    valArr[i] = false + valArr[i].substring(rightK + 1);
                } else {
                    valArr[i] = ExpressionHandleUtil.bettwenDate(
                            String.valueOf(keyObj), String.valueOf(valueData[1]), String.valueOf(valueData[2])
                    ) + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DATE关键字
     * @return
     */
    public static String dateHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DATE.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                // TODO: 2022/12/8 key需要调用去表值方法

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.valDatePa(String.valueOf(keyObj)) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DATE_LAST_DAY关键字
     * @return
     */
    public static String dateLastDayHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DATE_LAST_DAY.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.valDateLastDay(String.valueOf(keyObj)) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    public static String dateSHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DATE_S.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                // TODO: 2022/12/8 key需要调用去表值方法

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.valDatePa(String.valueOf(keyObj),valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理REGREX关键字
     * @return
     */
    public static String regRexHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.REGREX.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 双参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                if (ObjectUtil.isEmpty(keyObj)) {
                    valArr[i] = false + valArr[i].substring(rightK + 1);
                } else {
                    valArr[i] = ExpressionHandleUtil.regRexExp(valueData[1], (String) keyObj) + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理MULTI_SPECIAL_VAL关键字
     * @param val
     * @return
     * @throws Exception
     */
    public static String multiSpecialValHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.MULTI_SPECIAL_VAL.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                // 获取keyData对应的表、字段值
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                if (ObjectUtil.isEmpty(keyObj)) {
                    valArr[i] = false + valArr[i].substring(rightK + 1);
                } else {
                    valArr[i] = ExpressSpecialUtil.multiSpecialVal((String) keyObj, valueData[1], valueData[2], valueData[3], valueData[4]) + valArr[i].substring(rightK + 1);
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理VALUE_DM关键字
     * @return
     */
    public static String valueDMHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.VALUE_DM.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.getDateMonth((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理MINUS_D关键字
     * @return
     */
    public static String minusDHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.MINUS_D.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                // 获取keyData对应的表、字段值
                Integer keyObj = ExpressionHandleUtil.difDays(valueData[0], valueData[2], valueData[1]);
                keyObj = keyObj < 0?0:keyObj;
                // 拼接dict函数字段校验结果
                valArr[i] = keyObj + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DIF关键字
     * @return
     */
    public static String difHandle(String val, List<Map<String, String>> paramList) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DIF.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.difUniqueCheck(valueData, map.get(), paramList) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理DIF关键字
     * @return
     */
    public static String difSHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.DIF_S.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.difSUniqueCheck(key, (String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CHAR关键字
     * @return
     */
    public static String exCharHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CHAR.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.exCharLength((String) keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理SUBSTR关键字
     * @return
     */
    public static String subStrHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.SUBSTR.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                if (ObjectUtil.isEmpty(keyObj)) {
                    valArr[i] = null + valArr[i].substring(rightK + 1);
                } else {
                    valArr[i] = "'"+String.valueOf(keyObj)
                            .substring(Integer.valueOf(valueData[1]), Integer.valueOf(valueData[1]) + Integer.valueOf(valueData[2]))+"'"
                            + valArr[i].substring(rightK + 1);
                }


            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理LENGTH_X关键字,返回字段长度
     * @return
     */
    public static String lengthXHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.LENGTH_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());

                long keyLength = ObjectUtil.isNull(keyObj)?0:String.valueOf(keyObj).codePoints().count();
                // 拼接dict函数字段校验结果
                valArr[i] = keyLength + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理numberX(), 校验小数长度
     * @return
     */
    public static String numberXHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.NUMBER_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                if (ObjectUtil.isNull(valueData[1]) || ObjectUtil.isNull(valueData[2])
                || !NumberUtil.isInteger(valueData[1]) || !NumberUtil.isInteger(valueData[2])) {
                    throw new Exception("参数异常");
                }
                // 拼接numberX函数字段校验结果
                valArr[i] = ExpressionHandleUtil.numberXLength(
                            (String) keyObj, Integer.valueOf(valueData[1]), Integer.valueOf(valueData[2])
                        ) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理numberD(), 校验数字长度
     * @return
     */
    public static String numberDHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.NUMBER_D.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                if (ObjectUtil.isNull(valueData[1]) || !NumberUtil.isInteger(valueData[1])) {
                    throw new Exception("参数异常");
                }
                // 拼接numberD函数字段校验结果
                valArr[i] = ExpressionHandleUtil.numberDLength((String) keyObj, Integer.valueOf(valueData[1])) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }


    /**
     * 处理REPEAT_X(), 校验字段重复性
     * @return
     */
    public static String repeatXHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.REPEAT_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleDataForRep(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                // 拼接numberD函数字段校验结果
                valArr[i] = ExpressionHandleUtil.repeatXHandle((String) keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理REPEAT_NULL_N(), 校验字段重复性
     * @return
     */
    public static String repeatNullNHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.REPEAT_NOT_N.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接numberD函数字段校验结果
                valArr[i] = ExpressionHandleUtil.repeatNullNHandel((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理REPEAT_NULL_N(), 校验字段重复性
     * @return
     */
    public static String repeatValueRepeatNumHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.VALUE_REPEAT_NUM.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleDataForRep(key);
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());
                // 拼接numberD函数字段校验结果
                valArr[i] = ExpressionHandleUtil.repeatValueRepeatNumHandel((String) keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }
    /**
     * 处理CHECK_EN关键字,校验为数字或英文
     * @return
     */
    public static String checkENCode(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CHECK_EN.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.checkEN(String.valueOf(keyObj)) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CHECK_N关键字,校验为数字
     * @return
     */
    public static String checkNCode(String val) {
        String[] valArr = commonTagRep(val, ExpressionEnum.CHECK_N.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = NumberUtil.isNumber((CharSequence) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }


    /**
     * 处理CHECK_INT关键字,校验为整数
     * @return
     */
    public static String checkINTCode(String val) {
        String[] valArr = commonTagRep(val, ExpressionEnum.CHECK_INT.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = NumberUtil.isInteger(String.valueOf(keyObj)) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CHECK_C关键字,校验包含中文
     * @return
     */
    public static String checkCCode(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CHECK_C.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接dict函数字段校验结果
                valArr[i] = ExpressionHandleUtil.checkC((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理COOR关键字,获取二维坐标下的数值，null值默认为0
     * @return
     */
    public static String coorHandel(String val, Map<String, Object> c2RowDataMap) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.COOR.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Map<String, Object> paramsMap = (Map<String, Object>) c2RowDataMap.get(valueData[0]);
                Object keyObj = ExpressionHandleUtil.getCoorMapValue(valueData[1], paramsMap);



                // 拼接dict函数字段校验结果
                if (ObjectUtil.isEmpty(keyObj)) {
                    valArr[i] = 0 + valArr[i].substring(rightK + 1);
                    // 存入日志详情描述记录值
                    c2RowLogList.add("0");
                } else {
                    // 存入日志详情描述记录值
                    c2RowLogList.add(String.valueOf(keyObj));
                    if (NumberUtil.isNumber((CharSequence) keyObj)) {
                        BigDecimal keyDec = new BigDecimal(String.valueOf(keyObj));
                        valArr[i] = keyDec.multiply(new BigDecimal(1000000)) + valArr[i].substring(rightK + 1);
                    } else {
                        valArr[i] = "'" + keyObj + "'" + valArr[i].substring(rightK + 1);
                    }
                }
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CPXS_MJ关键字,校验特殊定制格式
     * @return
     */
    public static String cpxsmjHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CPXS_MJ.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                //获取产品登记编码
                Object prodCode = ExpressionHandleUtil.getMapValue("app_initial_sub_regist_info.PROD_CODE", map.get());
                //获取数据日期
                Object startDate = ExpressionHandleUtil.getMapValue("app_initial_sub_regist_info.THEORY_REPORT_START_DATE", map.get());
                //获取区域募集金额字段
                Object zonClcAmt = ExpressionHandleUtil.getMapValue("app_initial_sub_regist_info.ZON_CLC_AMT", map.get());
                //获取认购币种字段
                Object prodCcy = ExpressionHandleUtil.getMapValue("app_initial_sub_regist_info.PROD_CCY", map.get());

                valArr[i] = ExpressSpecialUtil.cpxMj((String) keyObj, valueData[1], (String) prodCode, (String) startDate
                        , (String) zonClcAmt, (String) prodCcy) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CPXS_BZ关键字,校验特殊定制格式(验证产品存续期登记，币种和申购兑付信息是否合规)
     * @return
     */
    public static String cpmjbzHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CPMJ_BZ.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = ExpressSpecialUtil.cpmBz((String) keyObj, valueData[1]) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 处理CCDJ_AS_CD关键字,校验特殊定制格式(验证产品持仓登记，MEZZANINE_ASSET_CODE中间层行内资产/负债编码)
     * @return
     */
    public static String ccdjAsCdHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.CCDJ_AS_CD.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);

                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = ExpressSpecialUtil.splitCode((String) keyObj, valueData[1], null) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 定制函数校验,BANK_CODE发行机构代码校验
     * @return
     */
    public static String bankCodeHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.BANK_CODE_CHECK.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                // 拼接函数字段校验结果
                valArr[i] = ExpressionHandleUtil.bankCode((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 校验数字英文及字符白名单（老）
     * @return
     */
    public static String whiteCharHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.WHITE_CHAR_UNICODE.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());

                valArr[i] = ExpressionHandleUtil.whiteChar((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 校验每个字符均处于白名单内（一、二期）
     * @return
     */
    public static String whiteAllHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.WHITE_ALL.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = WhiteOneTwoCheckUtil.whiteAll((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 校验每个字符均处于白名单内（三期）
     * @return
     */
    public static String whiteThAllHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.WHITE_TH_ALL.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = WhiteThreeCheckUtil.whiteAll((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 校验每个字符均处于白名单内（一、二期）
     * @return
     */
    public static String whiteXHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.WHITE_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = WhiteOneTwoCheckUtil.whiteX((String) keyObj, valueData) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 校验每个字符均处于白名单内（三期）
     * @return
     */
    public static String whiteThXHandel(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.WHITE_TH_X.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);

                // 多参数方法，切割
                String[] valueData = ExpressionHandleUtil.getDoubleData(key);
                Object keyObj = ExpressionHandleUtil.getMapValue(valueData[0], map.get());

                valArr[i] = WhiteThreeCheckUtil.whiteX((String) keyObj, valueData) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

    /**
     * 验证如投资资产种类及比例字段，符合格式及数据源要求例： 10%股票(二级市场);10%-20%公司债券;70%-80%:信贷资产
     * @param val
     * @return
     * @throws Exception
     */
    public static String typeAndRatioHandle(String val) throws Exception {
        String[] valArr = commonTagRep(val, ExpressionEnum.INVEST_TYPE_AND_RATIO.getVal())
                .split(ExpressionEnum.SYSTEM_HOLD_SPLIT.getVal());

        String newVal = new String();
        for (int i = 0; i<valArr.length; i++) {
            if (!"".equals(valArr[i]) && i > 0) {
                int rightK = valArr[i].indexOf(")");
                String key = valArr[i].substring(0, rightK);
                Object keyObj = ExpressionHandleUtil.getMapValue(key, map.get());
                valArr[i] = ExpressSpecialUtil.checkTypeAndRatio((String) keyObj) + valArr[i].substring(rightK + 1);
            }
            newVal += valArr[i];
        }
        return newVal;
    }

}
