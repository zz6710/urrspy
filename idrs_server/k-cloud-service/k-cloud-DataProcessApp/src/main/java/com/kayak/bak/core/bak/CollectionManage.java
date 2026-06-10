package com.kayak.bak.core.bak;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.BakCollectionDao;
import com.kayak.bak.business.dao.BakPhaseDao;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.enums.BakAutoTableEnum;
import com.kayak.bak.enums.FieldChangeEnum;
import com.kayak.bak.model.dto.BakCreateTableDTO;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.BakTaskDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.kayak.bak.core.config.SubConfig.SUB_NUM;

/**
 * 分表-仓库分表管理
 */
@Component
public class CollectionManage {

    @Resource
    private BakPhaseDao bakPhaseDao;
    @Resource
    private BakCollectionDao bakCollectionDao;
    @Resource
    private BakRecord bakRecord;

    /**
     * 备份分表分析与处理，获取仓库表名
     * @param dto
     * @return
     * @throws Exception
     */
    public BakTaskDTO subTable(BakTaskDTO dto) throws Exception {
        //获取目标表字段信息
        List<BakFieldDTO> bakCreateFieldDTOList = bakPhaseDao.getFieldInfo(
                dto.getTargetDb(), dto.getTargetTable()
        );
        //自动建表和手动建表配置分别处理
        if (BakAutoTableEnum.AUTO_TYPE.getVal().equals(dto.getAutoTable())) {
            dto = autoTableManage(dto, bakCreateFieldDTOList);
        } else if (BakAutoTableEnum.NOT_AUTO_TYPE.getVal().equals(dto.getAutoTable())) {
            if (ObjectUtil.isEmpty(dto.getSysBakCollectionPO())) {
                throw new Exception("手动建表配置未找到可用表配置");
            }
            dto.setBakSort(dto.getSysBakCollectionPO().getBakSort());
        }
        //自动处理处理表字段变动问题
        if (FieldChangeEnum.FIELD_AUTO.getVal().equals(dto.getFieldChange())) {
            handleChangeField(bakCreateFieldDTOList, dto);
        }
        //添加仓库表信息
        return dto.setBakFieldDTOList(bakCreateFieldDTOList).initBakTableName();
    }

    /**
     * 自动处理变更字段问题
     * @param newFieldDTOList 目标表最新字段信息
     * @param dto
     */
    private void handleChangeField(List<BakFieldDTO> newFieldDTOList, BakTaskDTO dto) throws Exception {
        //已有仓库集合列表为空，不做字段相关处理
        if (ObjectUtil.isEmpty(dto.getSysBakCollectionPOList())) {
            return;
        }
        //获取一号仓库表名称
        List<BakFieldDTO> bakFieldDTOList = bakPhaseDao.getFieldInfo(
                SourceConfig.BAK_NAME, "1_" + dto.getTargetDb() + "_" + dto.getTargetTable()
        );
        //缺失字段列表
        List<BakFieldDTO> changeFieldList  = new ArrayList<>();
        //匹配缺失字段
        for (BakFieldDTO newDTO : newFieldDTOList) {
            int i = 0;
            for (BakFieldDTO oldDTO : bakFieldDTOList) {
                i++;
                if (ObjectUtil.equals(newDTO.getColName(), oldDTO.getColName())) {
                    break;
                }
                if (i == bakFieldDTOList.size()) {
                    changeFieldList.add(newDTO);
                }
            }
        }
        //仓库表缺失字段新增
        for (BakFieldDTO fieldDTO : changeFieldList) {
            for (SysBakCollectionPO bakCollectionPO : dto.getSysBakCollectionPOList()) {
                bakCollectionDao.alterBakField(bakCollectionPO, fieldDTO);
            }
        }
    }


    /**
     * 自动建表配置下获取备份仓库
     * @param dto
     * @param bakCreateFieldDTOList
     * @return
     * @throws Exception
     */
    private BakTaskDTO autoTableManage(BakTaskDTO dto, List<BakFieldDTO> bakCreateFieldDTOList) throws Exception {
        //得到仓库序列
        if (!analysisTable(dto)) {
            //无仓库或仓库数据量已过阈值，建立新的仓库
            BakCreateTableDTO bakCreateTableDTO = new BakCreateTableDTO().initTableInfo(bakCreateFieldDTOList, dto);
            bakPhaseDao.createBakTable(bakCreateTableDTO);
            //更新仓库记录
            bakPhaseDao.addCollection(dto.loadBakCreateTableDTO(bakCreateTableDTO));
            dto.setBakSort(bakCreateTableDTO.getBakSort());
            //添加操作记录
            bakRecord.addRecordLog(bakCreateTableDTO);
        } else {
            dto.setBakSort(dto.getSysBakCollectionPO().getBakSort());
        }
        return dto;
    }


    /**
     * 备份仓库分析()
     * @param dto
     * @return
     */
    private Boolean analysisTable(BakTaskDTO dto) {
        SysBakCollectionPO collectionPO = dto.getSysBakCollectionPO();
        // 无备份仓库集合模型，需要初始化备份仓库
        if (ObjectUtil.isNull(collectionPO)) {
            return false;
        }
        // 数据量超过阈值，需要建立新的备份仓库
        Integer subNum = dto.getSysBakConfigPO().getThreshold();
        subNum = ObjectUtil.isNull(subNum)?SUB_NUM:subNum;
        if(Integer.parseInt(collectionPO.getBakNum()) > subNum) {
            return false;
        }

        return true;
    }

}
