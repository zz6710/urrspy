package com.kayak.xsql.parameter;

import com.kayak.core.system.SysUtil;

public class FieldGetterLogin extends FieldGetter {

    private String name;

    public FieldGetterLogin(String name) {
        this.name = name;
    }

    @Override
    public Object get(Object params) {
        if ("sys_user_userid".equals(name)) {
            return SysUtil.getLoginUserid();
        } else {
            return SysUtil.getSysUserParams().get(name.replace("sys_user_", ""));
        }
    }
}
