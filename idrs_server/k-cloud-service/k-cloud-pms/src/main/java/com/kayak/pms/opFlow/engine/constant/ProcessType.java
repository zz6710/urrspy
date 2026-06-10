package com.kayak.pms.opFlow.engine.constant;

/**
 * @author libo
 */
public class ProcessType {

    private ProcessType() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 入库
     */
    public static final String AGGRESSIVE = "1";

    /**
     * 不入库
     */
    public static final String PASSIVE = "2";

}
