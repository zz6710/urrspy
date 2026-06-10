package com.kayak.bak.core.bak;

import com.kayak.bak.business.dao.BakConfigDao;
import com.kayak.bak.business.dao.BakPhaseDao;
import com.kayak.bak.business.dao.RedoDao;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.RedoDataDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 归档数据还原服务
 */
@Service
public class TableRedo {

    @Resource
    private RedoDao redoDao;

    @Resource
    private BakPhaseDao bakPhaseDao;

    @Resource
    private BakConfigDao bakConfigDao;

    @Resource
    private TableBak tableBak;

    @Resource
    private BakRecord bakRecord;

    /**
     * 还原数据
     * @param dto
     * @throws Exception
     */
    public void redoData(RedoDataDTO dto) throws Exception {
        //查询归档相关配置信息
        SysBakConfigPO bakConfigPO = bakConfigDao.getBakConfig(dto.getId());
        //查询已归档集合
        List<SysBakCollectionPO> collectionPOList = redoDao.getBakCollection(dto);
        //查询目标表最新的表字段信息
        List<BakFieldDTO> fieldDTOList = bakPhaseDao.getFieldInfo(
                dto.getTargetDb(), dto.getTargetTable()
        );

        //准备参数
        List<RedoDataDTO> redoDataDTOList = collectionPOList.stream().map(
                po -> dto.convertRedoList(bakConfigPO, fieldDTOList, po)
        ).collect(Collectors.toList());

        //查询待还原的数据
        List<SqlRow> sqlRowList = redoDao.getRedoData(redoDataDTOList);

        //还原数据入源数据库
        String sql = tableBak.getSqlStr(fieldDTOList, bakConfigPO.getTargetTable());
        redoDao.addRedoData(sql, sqlRowList, fieldDTOList, SourceConfig.getDataSource(bakConfigPO.getTargetDb()));

        //记录还原动作
        bakRecord.addRecordLog(dto, sqlRowList.size());
    }


}
