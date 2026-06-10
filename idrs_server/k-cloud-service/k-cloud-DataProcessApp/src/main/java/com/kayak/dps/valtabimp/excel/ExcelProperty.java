package com.kayak.dps.valtabimp.excel;

import java.lang.annotation.*;

/**
 * <li>完成日期：2020/1/14</li>
 * <li>修改记录: 无</li>
 *
 * @author yangzh
 * @version 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ExcelProperty {
    String[] value() default {""};
}
