package com.kayak.bak.model.dto;

import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 数据还原
 */
@Data
@Accessors(chain = true)
public class RedoDataDTO {

    /**
     * 归档配置id
     */
    private String id;

    /**
     * 还原目标数据库
     */
    private String targetDb;

    /**
     * 还原目标表
     */
    private String targetTable;

    /**
     * 数据起始日期
     */
    private String redoStartDate;

    /**
     * 数据结束日期
     */
    private String redoEndDate;

    /**
     * 归档表名
     */
    private String bakTable;

    /**
     * 目标表识别日期字段
     */
    private String targetField;

    /**
     * 字段列表
     */
    private List<BakFieldDTO> fieldDTOList;

    /**
     * 转化待还原数据
     * @return
     */
    public RedoDataDTO convertRedoList(SysBakConfigPO po, List<BakFieldDTO> fieldDTOList, SysBakCollectionPO collectionPO) {
        return this.setId(po.getId())
                .setTargetField(po.getTargetField())
                .setBakTable(collectionPO.getBakTable())
                .setFieldDTOList(fieldDTOList);
    }
}
