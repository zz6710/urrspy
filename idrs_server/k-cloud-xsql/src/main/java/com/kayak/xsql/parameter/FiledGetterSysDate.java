package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;

public class FiledGetterSysDate extends FieldGetter{
    @Override
    public Object get(Object params) {
        return Tools.getCurrentDate();
    }
}
