package com.kayak.core.desensitized;

import com.kayak.core.util.Tools;

public class CustNameDesensitized implements Desensitized {

    @Override
    public String desensitized(Object value) {
        String name = Tools.obj2Str(value);
        if (name == null || name.isEmpty()) {
            return "";
        }
        String myName = null;
        char[] chars = name.toCharArray();
        if (chars.length == 1) {
            myName = name;
        }
        if (chars.length == 2) {
            myName = name.replaceFirst(name.substring(1), "*");
        }
        if (chars.length > 2) {
            myName = name.replaceAll(name.substring(1, chars.length - 1), "*");

        }
        return myName;
    }
}


