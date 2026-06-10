package com.kayak.pms.opFlow.engine.model;

import com.kayak.pms.opFlow.engine.handlers.IHandler;

import java.io.Serializable;

/**
 * Created by daniel on 19/03/2017.
 */
public class BaseModel implements Serializable {

    private static final long serialVersionUID = 2649990975111486594L;

    /**
     * 元素名称
     */
    private String name;

    /**
     * 显示名称
     */
    private String displayName;

    protected void fire(IHandler handler, Execution execution) throws Exception {
        handler.handle(execution);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
