package com.kayak.core.desensitized;

import com.kayak.core.util.Tools;

public class Blankcarddesensitized implements Desensitized {

    @Override
    public String desensitized(Object value) {
        String _vaule = Tools.obj2Str(value);

        if (Tools.strIsEmpty(_vaule) || _vaule.length() < 19) {
            return "";
        }
        return _vaule.substring(0,3)+"***" + _vaule.substring(_vaule.length() - 4, _vaule.length());
    }

}
