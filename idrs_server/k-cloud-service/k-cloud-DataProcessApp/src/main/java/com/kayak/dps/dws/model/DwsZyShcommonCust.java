package com.kayak.dps.dws.model;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @className: DwsZyShcommonCust
 * @description: 上海国际集团共同客户名录  dws
 * @author: lc-renxw
 * @date: 2024/11/5 9:42
 * @version: 1.0
 */
@Data
public class DwsZyShcommonCust {
    //日期
    @ExcelProperty("日期")
    private String actDt;
    //客户名称
    @ExcelProperty("客户名称")
    private String custName;
    //统一社会信用代码
    @ExcelProperty("统一社会信用代码")
    private String registernumber;
    //组织机构代码
    @ExcelProperty("组织机构代码")
    private String infoOrgCode;
    //其他代码
    @ExcelProperty("其他代码")
    private String infoOthCode;
    //其他代码类型
    @ExcelProperty("其他代码类型")
    private String infoOthType;
    //行内客户号
    @ExcelProperty("行内客户号")
    private String custNumber;
    //国民经济行业分类
    @ExcelProperty("国民经济行业分类")
    private String neIndCode;
    //经济成分
    @ExcelProperty("经济成分")
    private String neIndType;
    //是否关联方
    @ExcelProperty("是否为关联方")
    private String relevance;

}
