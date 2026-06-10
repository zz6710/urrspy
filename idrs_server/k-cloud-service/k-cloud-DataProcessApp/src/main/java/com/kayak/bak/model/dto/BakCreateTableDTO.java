package com.kayak.bak.model.dto;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.enums.SpecialColumnEnum;
import com.kayak.bak.model.po.SysBakCollectionPO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class BakCreateTableDTO {

    /**
     * 表字段信息
     */
    private List<BakFieldDTO> bakFieldDTOList;

    /**
     * 配置所属id
     */
    private String bakConfigId;

    /**
     * 拼接出的建表字段语句
     */
    private String fields;

    /**
     * 目标数据库
     */
    private String targetDb;

    /**
     * 目标表
     */
    private String targetTable;

    /**
     * 备份表排序数
     */
    private Integer bakSort;

    /**
     * 表名
     */
    private String bakTableName;

    /**
     * 表引擎
     */
    private String tbType;

    /**
     * 表注释
     */
    private String tbDesc;

    /**
     * 拼接字段建表语句
     * @param dtoList
     * @return
     */
    public BakCreateTableDTO createFields(List<BakFieldDTO> dtoList) throws Exception {
        StringBuffer fields = new StringBuffer();

        for (BakFieldDTO fieldDTO : dtoList) {
            String size = " (" + fieldDTO.getSize() + ") ";
            if (ObjectUtil.isNotEmpty(fieldDTO.getDigits())) {
                //浮点数处理字符长度限制
                size = " (" + fieldDTO.getSize() + "," + fieldDTO.getDigits() + ") ";
            } else if (SpecialColumnEnum.containVal(fieldDTO.getType())) {
                //LONG_TEXT等类型字段有长度为空处理
                size = "";
            }
            fields.append(fieldDTO.getColName()+" "+fieldDTO.getType()+size+" COMMENT '"+fieldDTO.getRemark()+"',");
        }
        return this.setFields(String.valueOf(fields.deleteCharAt(fields.length() - 1)));
    }

    /**
     * 计算创建仓库序列号
     * @param collectionPO
     * @return
     */
    public BakCreateTableDTO createSortNum(SysBakCollectionPO collectionPO) {
        // 初始化表sort为1
        if (ObjectUtil.isNull(collectionPO)) {
            return this.setBakSort(new Integer(1));
        }
        return this.setBakSort(collectionPO.getBakSort() + 1);
    }

    /**
     * 加载建表信息
     * @param dtoList
     * @param dto
     * @return
     */
    public BakCreateTableDTO initTableInfo(List<BakFieldDTO> dtoList, BakTaskDTO dto) throws Exception {
        return new BakCreateTableDTO().setBakConfigId(dto.getBakConfigId())
                .setTargetDb(dto.getTargetDb())
                .setTargetTable(dto.getTargetTable())
                .setBakFieldDTOList(dtoList)
                .createFields(dtoList)
                .createSortNum(dto.getSysBakCollectionPO())
                .initTbName();
    }

    /**
     * 生成备份仓库名称
     * @return
     */
    public BakCreateTableDTO initTbName() {
        return this.setBakTableName(bakSort + "_" + targetDb + "_" + targetTable);
    }
}
