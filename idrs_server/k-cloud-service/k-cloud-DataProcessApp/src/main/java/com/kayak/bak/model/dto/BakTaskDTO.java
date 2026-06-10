package com.kayak.bak.model.dto;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 备份任务传输模型
 */
@Data
@Accessors(chain = true)
public class BakTaskDTO {

    /**
     * 备份配置表id
     */
    private String bakConfigId;

    /**
     * 表字段信息
     */
    private List<BakFieldDTO> bakFieldDTOList;

    /**
     * 备份仓库模型
     */
    private SysBakConfigPO sysBakConfigPO;

    /**
     * 备份仓库最新模型
     */
    private SysBakCollectionPO sysBakCollectionPO;

    /**
     * 备份仓库模型集合
     */
    private List<SysBakCollectionPO> sysBakCollectionPOList;

    /**
     * 目标数据库
     */
    private String targetDb;

    /**
     * 目标表
     */
    private String targetTable;

    /**
     * 目标表数据标识字段
     */
    private String targetField;

    /**
     * 建表方式（1：自动建表 2：手动建表）
     */
    private String autoTable;

    /**
     * 字段自动适配（1：自动新增 2：非自动新增）
     */
    private String fieldChange;

    /**
     * 入库仓库表序列
     */
    private Integer bakSort;

    /**
     * 备份仓库表
     */
    private String bakTableName;

    /**
     * 数据起始日期
     */
    private String nextLeftDate;

    /**
     * 数据结束日期
     */
    private String nextRightDate;


    public static BakTaskDTO dto() {
        return new BakTaskDTO();
    }

    /**
     * 转换配置实体, 准备归档任务参数
     * @param po
     * @return
     */
    public BakTaskDTO convertSysBakConfigPO(SysBakConfigPO po) {
        BeanUtil.copyProperties(po, this);
        return this.setBakConfigId(po.getId()).setSysBakConfigPO(po);
    }

    /**
     * 加载最新（序列号靠后）备份集合信息表实体
     * @param pos
     * @return
     */
    public BakTaskDTO loadSysBakCollectionPO(List<SysBakCollectionPO> pos) {
        //此配置关联的仓库集合
        List<SysBakCollectionPO> poList = new ArrayList<>();
        // 筛选出最新（序列号靠后）备份集合信息表实体
        pos.stream().forEach(
                po -> {
                    if (ObjectUtil.equals(this.getBakConfigId(), po.getBakConfigId())) {
                        this.setSysBakCollectionPO(po);
                        poList.add(po);
                    }
                }
        );
        //加载关联的所有仓库
        this.setSysBakCollectionPOList(poList);
        return this;
    }

    /**
     * 加载新创建的备份表仓库信息
     * @param dto
     * @return
     */
    public BakTaskDTO loadBakCreateTableDTO(BakCreateTableDTO dto) {
        return this.setTargetTable(dto.getTargetTable())
                .setBakTableName(dto.getBakTableName())
                .setBakSort(dto.getBakSort());
    }

    /**
     * 生成备份仓库名称
     * @return
     */
    public BakTaskDTO initBakTableName() {
        return this.setBakTableName(bakSort + "_" + targetDb + "_" + targetTable);
    }

    /**
     *
     * @param sysBakConfigPOS 归档配置实体列表
     * @param sysBakCollectionPOS 归档集合实体列表
     * @return
     */
    public List<BakTaskDTO> initList(List<SysBakConfigPO> sysBakConfigPOS, List<SysBakCollectionPO> sysBakCollectionPOS) {
        return sysBakConfigPOS.stream().map(
                po ->  BakTaskDTO.dto().convertSysBakConfigPO(po).loadSysBakCollectionPO(sysBakCollectionPOS)
        ).collect(Collectors.toList());
    }




}
