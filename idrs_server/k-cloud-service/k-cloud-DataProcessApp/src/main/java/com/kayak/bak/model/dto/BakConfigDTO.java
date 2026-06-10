package com.kayak.bak.model.dto;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.utils.BakDateUtil;
import com.kayak.bak.core.config.SubConfig;
import com.kayak.bak.model.po.SysBakConfigPO;

import java.text.ParseException;

/**
 * 归档配置数据传输模型
 */
public class BakConfigDTO extends SysBakConfigPO {

    /**
     * 初始化归档配置数据(创建配置时)
     * @return
     */
    public BakConfigDTO initAddData() throws ParseException {
        //加载初始数据量
        setTableBakNum(new Integer(0));
        setCreateTime(BakDateUtil.getTimestamp19());
        setUpdateTime(BakDateUtil.getTimestamp19());
        if (ObjectUtil.isEmpty(getThreshold())) {
            setThreshold(SubConfig.SUB_NUM);
        }
        return this;
    }

    /**
     * 初始化归档配置数据(更新配置时)
     * @param tbNum
     * @return
     */
    public BakConfigDTO initUpData(int tbNum) throws ParseException {
        //加载初始数据量
        setTableBakNum(getTableBakNum() + tbNum);
        setUpdateTime(BakDateUtil.getTimestamp19());
        return this;
    }

}
