package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;

import java.util.Date;

public class FieldGetterTimestamp extends FieldGetter{
    @Override
    public Object get(Object params) {
        return new Date().getTime();
    }
}
