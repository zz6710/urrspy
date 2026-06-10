package com.kayak.xsql.parameter;

import com.kayak.core.util.Tools;

public class FieldGetterUUID extends FieldGetter{
    @Override
    public Object get(Object params) {
        return Tools.getStringRandom(32);
    }
}
