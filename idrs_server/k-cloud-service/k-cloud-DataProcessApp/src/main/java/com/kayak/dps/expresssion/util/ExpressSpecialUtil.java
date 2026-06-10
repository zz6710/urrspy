package com.kayak.dps.expresssion.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysBeans;
import com.kayak.core.util.BigDecimalUtil;
import com.kayak.dps.check.dao.DataValidateDao;
import com.kayak.dps.expresssion.enums.ExpressionRegrexEnum;
import com.kayak.dps.expresssion.enums.ExpressionSpecialEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 特殊定制化Express函数处理
 */
@Slf4j
public class ExpressSpecialUtil {

    private static DataValidateDao dataValidateDao = SysBeans.getBean("dataValidateDao");

    /**
     * 验证如投资资产种类及比例字段，符合格式及数据源要求例： 10%股票(二级市场);10%-20%公司债券;70%-80%:信贷资产
     * @param val
     * @return
     * @throws Exception
     */
    public static Boolean checkTypeAndRatio(String val) throws Exception {
        String reg = "^([0-9]\\d{0,1}|100)(\\.\\d{1,2})?%{1}:[^%:;-]+$";
        String reg2 = "^([0-9]\\d{0,1}|100)(\\.\\d{1,2})?%{1}-([0-9]\\d{0,1}|100)(\\.\\d{1,2})?%{1}:[^%:;-]+$";
        //分割为多参数匹配正则
        String[] valArr = val.split(";");
        for (int i=0;i<valArr.length;i++) {
            Boolean ret = ReUtil.isMatch(reg, valArr[i]);
            Boolean ret2 = ReUtil.isMatch(reg2, valArr[i]);
            if (!ret && !ret2) {
                return false;
            }
        }
        return true;
    }

    /**
     * 中间层行内资产/负债编码系统验证
     * @return
     */
    public static Boolean splitCode(String valStr,String dict,List<String> targetList) throws Exception {
        //获取资产负债已登记的编码
        if (ObjectUtil.isEmpty(targetList)) {
            targetList = dataValidateDao.getAssetCode(dict);
        }
        if(ObjectUtil.isEmpty(targetList)) {
                        return false;
                    }
        String[] codeArr = valStr.split(",");
        //校验编码是否存在
        for (String code : codeArr) {
            if (!targetList.contains(code)) {
                return false;
                }
            }
        return true;
    }


    // TODO: 2023/09/15 临时写死校验，后续升级可嵌套函数校验后改造
    /**
     * 校验定制函数募集金额
     * @return
     */
    public static Boolean cpxMj(String key, String dict, String prodCode, String startDate,String zonClcAmt, String prodCcy) {
        try {
            // 特殊函数定制制定魔法值,校验【产品销售区域】值域是否在公募/私募产品申报登记选择的范围时。
            if ("031035".equals(dict)) {
                String[] keyArr = key.split(";");
                for (String val : keyArr) {
                    String[] valArr = val.split(",");
                    String prodArea = valArr[0].split(" ")[0];
                    String prodAreaRegister = dataValidateDao.getProdArea(prodCode, startDate);
                    if (!prodAreaRegister.contains(prodArea)) {
                        return false;
                    }
                }
            }
            //【实际募集金额（元）】实际募集金额(元) =∑各区域募集金额（元）=∑各币种折算人民币金额（元）
            if ("031050".equals(dict)) {
                //多参数切割CNY,0,0
                List<String[]> zonArrList = getDataForRepeat(zonClcAmt, ";", ",");
                List<String[]> ccyArrList = getDataForRepeat(prodCcy, ";", ",");
                BigDecimal zonSum = new BigDecimal(0);
                BigDecimal ccySum = new BigDecimal(0);
                for (String[] valArr : zonArrList) {
                    zonSum = zonSum.add(BigDecimalUtil.getBigDecimal(valArr[1]));
                    }
                for (String[] valArr : ccyArrList) {
                    ccySum = ccySum.add(BigDecimalUtil.getBigDecimal(valArr[1]));
                }
                return zonSum.compareTo(ccySum) == 0 && BigDecimalUtil.getBigDecimal(key).compareTo(zonSum) == 0;
            }
        } catch (Exception e) {
            log.error("指标校验，募集金额格式异常，设定为不通过", e);
            return false;
        }
        return true;
    }

    /**
     * 募集总量登记币种字段定制函数
     * @param key
     * @param dict
     * @return
     * @throws Exception
     */
    public static Boolean cpmBz(String key, String dict) throws Exception {
        try {
        if (ObjectUtil.isEmpty(key)) {
            return false;
        }
            //多参数切割CNY,0,0
        List<String[]> dataArrList = getDataForRepeat(key, ";", ",");
            if (ObjectUtil.isEmpty(dataArrList)) {
                return false;
            }
            if ("031062".equals(dict)) {
                //认购金额的币种为“CNY 人民币”时，该要素必须等于认购金额
            for (String[] strArr : dataArrList) {
                    if ("CNY".equals(strArr[0])) {
                        if (BigDecimalUtil.getBigDecimal(strArr[1]).compareTo(BigDecimalUtil.getBigDecimal(strArr[2])) != 0) {
                            return false;
            }
        }
            }
        }
        } catch (Exception ex) {
            log.error("指标校验，币种及认购金额、折算人民币异常，设定为不通过", ex);
            return false;
            }
        return true;
        }

    /**
     *
     * @param val 校验值
     * @param k1 分割参数顺序位置
     * @param k2 分类参数
     * @param k3 分类参数为dict则为值域参数，为number_x则为整体长度位数
     * @param k4 分类参数为number_x则为小数长度位数
     * @return
     * @throws Exception
     */
    public static Boolean multiSpecialVal(String val, String k1, String k2, String k3, String k4) throws Exception {
        //多参数切割CNY,0,0
        List<String[]> dataArrList = getDataForRepeat(val, ";", ",");
        if (ObjectUtil.isEmpty(dataArrList)) {
            return false;
        }

        try {
            //必填校验
            if (ExpressionSpecialEnum.NULL_N.getVal().equals(k2)) {
            for (String[] strArr : dataArrList) {
                    if (ObjectUtil.isEmpty(strArr[Integer.valueOf(k1)])) {
                        return false;
            }
        }
            }
            //校验长度和小数位数(参数3为总长度、参数4为小数长度)
            if (ExpressionSpecialEnum.NUMBER_X.getVal().equals(k2)) {
            for (String[] strArr : dataArrList) {
                    if (!ExpressionHandleUtil.numberXLength(strArr[Integer.valueOf(k1)], Integer.valueOf(k3), Integer.valueOf(k4))) {
                        return false;
            }
        }
            }
            //校验为数值且需要大于等于0(参数3、参数4为空)
            if (ExpressionSpecialEnum.NUMBER_Z.getVal().equals(k2)) {
            for (String[] strArr : dataArrList) {
                    if (!NumberUtil.isNumber(strArr[Integer.valueOf(k1)])
                            || BigDecimalUtil.getBigDecimal(strArr[Integer.valueOf(k1)]).compareTo(BigDecimal.ZERO) < 0) {
                        return false;
            }
        }
            }
            //校验值域多选不可重复校验(参数3、参数4为空)
            if (ExpressionSpecialEnum.REPEAT_X.getVal().equals(k2)) {
                Set<String> strSet = new HashSet<>();
            for (String[] strArr : dataArrList) {
                    strSet.add(strArr[Integer.valueOf(k1)]);
            }
                if (dataArrList.size() != strSet.size()) {
                    return false;
        }
            }
            //校验值域(参数3为值域字典，参数4为空)
            if (ExpressionSpecialEnum.DICT.getVal().equals(k2)) {
            for (String[] strArr : dataArrList) {
                    if (!ExpressionHandleUtil.existDictItem(k3, strArr[Integer.valueOf(k1)])) {
                        return false;
            }
        }
            }
            //比较大小(参数3为比较的对象位数，固定为大于,参数4为空)
            if (ExpressionSpecialEnum.COMPARE.getVal().equals(k2)) {
            for (String[] strArr : dataArrList) {
                    if (BigDecimalUtil.getBigDecimal(strArr[Integer.valueOf(k1)]).compareTo(BigDecimalUtil.getBigDecimal(strArr[Integer.valueOf(k3)])) < 0) {
                        return false;
            }
        }
            }
        } catch (Exception e) {
            throw new Exception("multiSpecial函数未匹配到合适类型，请检查参数是否正确！");
        }
        return true;
    }


    /**
     * 获取单字段重复存值类型方法
     * @param val  ex:CNY,0,0,0
     * @param reTag  ex: ;  批量数据分隔符
     * @param dataTag  ex: ,  多值之间的分隔符
     * @return
     */
    private static List<String[]> getDataForRepeat(String val, String reTag, String dataTag) {
        if (ObjectUtil.isEmpty(val)) {
            return null;
        }
        List<String[]> valList = new ArrayList<>();
        String[] dataArr = val.split(reTag);
        for (String data : dataArr) {
            if (!data.contains(dataTag)) {
                continue;
            }
            valList.add(data.split(dataTag));
        }
        return valList;
    }
}
