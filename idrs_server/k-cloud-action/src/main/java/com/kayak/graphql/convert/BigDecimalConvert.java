package com.kayak.graphql.convert;

import com.kayak.core.util.Tools;

import java.lang.reflect.Field;
import java.math.BigDecimal;

public class BigDecimalConvert implements Convert {
    @Override
    public Object convert(Field field, String value) {
        if (Tools.isEmpty(value)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }
}
