package com.kayak.bak.core.config;


public class SubConfig {

    /**
     * 备份仓库数据阀值（单表未配置则使用此公共阈值，大于则新生成备份仓库）
     */
    public static int SUB_NUM =10000;

    /**
     * 分批入库分页值
     */
    public static Integer BATCH_INSERT_NUM = 50000;


    /**
     * 还原数据阈值(写入生产库数据，谨慎操作，过大数据量暂不支持)
     */
    public static Integer MAX_REDO_NUM = 10000;

}
