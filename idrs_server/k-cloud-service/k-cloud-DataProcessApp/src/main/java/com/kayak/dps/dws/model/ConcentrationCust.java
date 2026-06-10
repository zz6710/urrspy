package com.kayak.dps.dws.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @className: ConcentrationCust
 * @description: 客户集中度内容
 * @author: lc-renxw
 * @date: 2024/10/28 9:20
 * @version: 1.0
 */
@Data
public class ConcentrationCust {

    @ExcelProperty(index = 0)
    private String xhStr;
    @ExcelProperty(index = 1)
    private String custName;

}
