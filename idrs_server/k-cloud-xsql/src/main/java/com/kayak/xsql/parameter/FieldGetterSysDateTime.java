package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;

import java.util.Date;

public class FieldGetterSysDateTime extends FieldGetter{
    @Override
    public Object get(Object params) {
        return Tools.getStringFromDate("yyyyMMddhhmmss",new Date());
    }
}
