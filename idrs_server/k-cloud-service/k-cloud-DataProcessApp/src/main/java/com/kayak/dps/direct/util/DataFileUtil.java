package com.kayak.dps.direct.util;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.core.util.Tools;
import com.kayak.dps.direct.model.ExFmt;
import org.dom4j.Element;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * 中债直连报送数据处理工具
 */
public class DataFileUtil {

    /**
     * 行添加方法
     * @param fieldVal 值
     * @param exFmt 数据项信息
     * @param tuple 行构造
     * @param xbrl 行构造信息
     * @param unit 数值单位
     * @param idn fid序列
     * @throws Exception
     */
    public static void addField(String fieldVal, ExFmt exFmt, Element tuple, Element xbrl, HashMap<String, Element> unit,
                          Integer idn) throws Exception {
        //空字段值或“*”配符，跳过
        if ((ObjectUtil.isEmpty(fieldVal) || "null".equalsIgnoreCase(fieldVal)) && !exFmt.getFld().equals("*")) {
            return;
        }

        //行值构造开始
        Element field = tuple.addElement("wemax:" + exFmt.getId().getItmnm());
        //时点配置选择
        String durationDate = "C-D-" + DirectParams.preWorkDate + "-" + DirectParams.preWorkDate;
        String instantDate = "C-I-" + DirectParams.preWorkDate;
        if (exFmt.getDictItmmem() != null && (exFmt.getDictItmmem().equals(DirectParams.INSTANT_DATE))) {
            field.addAttribute("contextRef", instantDate);
            if (!unit.containsKey("context"))
                unit.put("context", DirectUtils.createInstantId(xbrl, instantDate));
        } else {
            field.addAttribute("contextRef", durationDate);
        }
        //fid写入
        idn++;
        field.addAttribute("id", "f" + idn);
        //接口数据项类型、数值单位处理
        if (exFmt.getDictItmtp() != null) {
            if (exFmt.getDictItmtp().equals(DirectParams.FLDTYPE_NUMBER)) {

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "PURE");
                DirectUtils.addUnit(xbrl, unit, "xbrli:pure");

            }
            if (exFmt.getDictItmtp().equals(DirectParams.FLDTYPE_NUM100)) {
                fieldVal = new BigDecimal(fieldVal).divide(new BigDecimal(100),12,BigDecimal.ROUND_HALF_DOWN).toString();
                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "PURE");
                DirectUtils.addUnit(xbrl, unit, "xbrli:pure");

            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_CUR)) {

                DirectUtils.addUnit(xbrl, unit, "iso4217:CNY");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "CNY");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_PERSON)) {

                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Person");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "Person");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_MONTH)) {
                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Month");
                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "Month");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_DAY)) {

                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Day");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "Day");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_PRODUCT)) {

                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Product");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "Product");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_COUNT)) {

                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Count");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "Count");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.UNIT_MONTHPERCOUNT)) {

                DirectUtils.addUnit(xbrl, unit, "wemax-unit:Month/wemax-uinit:Count");

                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), "MonthPerCount");
            }
            if (exFmt.getDictItmtp().equals(DirectParams.DATE_RANG)) {

                fieldVal = Tools.dateFormat(String.valueOf(fieldVal));
            }
            /*if (exFmt.getDictItmtp().equals(DirectParams.REGISTER_SERNO)) {

                fieldVal = String.valueOf(fieldVal).substring(2);
            }*/
//            if (exFmt.getDictItmtp().equals(DirectParams.GIVEN_CUR)) {
//
//                DirectUtils.addUnit(xbrl, unit, "iso4217:" + uncur);
//
//                fieldVal = DirectUtils.addNumField(fieldVal, field, exFmt.getItmscl(), uncur);
//            }
        }

        //存在dictItmDic字典配置，字典值转换为Key+“ ”+Value形式（币种类型字段除外）
        if (ObjectUtil.isNotEmpty(exFmt.getDictItmdic()) && !"tr_cur".equalsIgnoreCase(exFmt.getDictItmdic())) {
            String dictKey = DirectUtils.getDictNameSys(exFmt.getDictItmdic(), fieldVal);
            fieldVal += " " + dictKey;
        }

        field.addText(String.valueOf(fieldVal));

    }
}
