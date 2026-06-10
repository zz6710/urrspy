package com.kayak.bak.model.dto;

import com.kayak.bak.business.utils.BakDateUtil;
import com.kayak.bak.enums.OpreateLogEnum;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.bak.model.po.SysBakLogPO;

import java.util.List;
import java.util.stream.Collectors;

public class BakLogDTO extends SysBakLogPO {

    /**
     * 归档数据库
     */
    private String db;

    /**
     * 归档表
     */
    private String table;

    /**
     * 转换仓库表创建模型
     * @return
     */
    public BakLogDTO convertBakCreateTableDTO(BakCreateTableDTO dto) {
        this.setOperateDesc("创建归档仓库表, 表：" + dto.getBakTableName())
                .setBakConfigId(dto.getBakConfigId())
                .setOperateDate(BakDateUtil.getNowDate())
                .setDuration("")
                .setType(OpreateLogEnum.CREATE_BAK.getVal())
                .setCreateTime(BakDateUtil.getTimestamp14())
                .setUpdateTime(BakDateUtil.getTimestamp14());
        return this;
    }

    /**
     * 转换归档数据任务模型
     * @return
     */
    public BakLogDTO convertBakTaskDTO(BakTaskDTO dto, Integer size,Long duration) {
        this.setOperateDesc("归档数据入库, 仓库表：" + dto.getBakTableName() + ",入库数据：" + size + "条")
                .setOperateDate(BakDateUtil.getNowDate())
                .setDuration(String.valueOf(duration))
                .setType(OpreateLogEnum.INSERT_BAK.getVal())
                .setBakConfigId(dto.getBakConfigId())
                .setCreateTime(BakDateUtil.getTimestamp14())
                .setUpdateTime(BakDateUtil.getTimestamp14());
        return this;
    }

    /**
     * 转换归档数据任务模型
     * @return
     */
    public BakLogDTO convertBakTaskDelDTO(BakTaskDTO dto, Integer size) {
        this.setOperateDesc("删除源表数据, 源表：" + dto.getTargetTable() + ",删除数据：" + size + "条")
                .setOperateDate(BakDateUtil.getNowDate())
                .setDuration("")
                .setType(OpreateLogEnum.DELETE_BAK.getVal())
                .setBakConfigId(dto.getBakConfigId())
                .setCreateTime(BakDateUtil.getTimestamp14())
                .setUpdateTime(BakDateUtil.getTimestamp14());
        return this;
    }

    /**
     * 转换还原数据任务模型
     * @return
     */
    public BakLogDTO convertRecordDTO(RedoDataDTO dto, Integer size) {
        this.setOperateDesc("数据还原, 源表：" + dto.getTargetTable() + ",还原数据：" + size + "条")
                .setOperateDate(BakDateUtil.getNowDate())
                .setDuration("")
                .setType(OpreateLogEnum.DELETE_BAK.getVal())
                .setBakConfigId(dto.getId())
                .setCreateTime(BakDateUtil.getTimestamp14())
                .setUpdateTime(BakDateUtil.getTimestamp14());
        return this;
    }

    public BakLogDTO convertBakTableDelDTO(SysBakConfigPO sysBakConfigPO, List<SysBakCollectionPO> bakTableList, long seconds) {
        String bakTableNameList = bakTableList.stream().map(SysBakCollectionPO::getBakTable).collect(Collectors.joining(","));
        this.setOperateDesc("删除备份表数据, 源表：" + sysBakConfigPO.getTargetTable() + ",备份表：" + bakTableNameList)
                .setOperateDate(BakDateUtil.getNowDate())
                .setDuration(String.valueOf(seconds))
                .setType(OpreateLogEnum.DELETE_BAK.getVal())
                .setBakConfigId(sysBakConfigPO.getId())
                .setCreateTime(BakDateUtil.getTimestamp14())
                .setUpdateTime(BakDateUtil.getTimestamp14());
        return this;
    }
}
