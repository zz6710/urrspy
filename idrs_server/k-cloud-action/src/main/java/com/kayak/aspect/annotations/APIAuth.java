package com.kayak.aspect.annotations;

/**
 * @version 1.0
 * @author: beacon
 * @Date: 2019-03-20   21:01
 * @Description
 */
public enum APIAuth {

    YES(true,"需要授权"),

    NO(false,"不需要授权");

    private boolean dvalue;

    private String name;

    APIAuth(boolean dvalue,String name){
        this.dvalue = dvalue;
        this.name = name;
    }

    public boolean isDvalue() {
        return dvalue;
    }

    public void setDvalue(boolean dvalue) {
        this.dvalue = dvalue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
