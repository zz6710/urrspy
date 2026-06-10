package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;

import java.util.Date;

public class FieldGetterSysTime extends FieldGetter{
    @Override
    public Object get(Object params) {
        return Tools.getStringFromDate("hhmmss",new Date());
    }
}
