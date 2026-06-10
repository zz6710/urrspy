package com.kayak.bak.model.dto;

import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.core.util.DateUtil;

public class BakCollectionDTO extends SysBakCollectionPO {

    /**
     * 初始化归档手动添加集合表配置
     * @return
     */
    public BakCollectionDTO initAddData() {
        initBakName();
        this.setCreateTime(DateUtil.getTimestamp14());
        this.setUpdateTime(DateUtil.getTimestamp14());
        return this;
    }

    /**
     * 自动生成仓库命名
     */
    private void initBakName() {
        this.setBakTable(this.getBakSort() + "_" + this.getTargetDb() + "_" + this.getTargetTable());
    }
}
