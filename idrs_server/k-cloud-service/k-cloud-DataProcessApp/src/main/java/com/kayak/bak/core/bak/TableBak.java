package com.kayak.bak.core.bak;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.BakCollectionDao;
import com.kayak.bak.business.dao.BakPhaseDao;
import com.kayak.bak.core.abs.BakTypeContext;
import com.kayak.bak.core.config.SourceConfig;
import com.kayak.bak.core.config.SubConfig;
import com.kayak.bak.enums.SourceDataEnum;
import com.kayak.bak.model.dto.BakConfigDTO;
import com.kayak.bak.model.dto.BakFieldDTO;
import com.kayak.bak.model.dto.BakTaskDTO;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.SysUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class TableBak {

    @Resource
    private BakPhaseDao bakPhaseDao;

    @Resource
    private BakCollectionDao bakCollectionDao;

    @Resource
    private BakRecord bakRecord;

    @Resource
    private BakTypeContext bakTypeContext;

    /**
     * 数据归档
     * @param dto
     * @throws Exception
     */
    public void subTable(BakTaskDTO dto) throws Exception {
        Instant startTime = Instant.now();
        //拼接归档预处理sql
        String sql = getSqlStr(dto.getBakFieldDTOList(), dto.getBakTableName());
        //获取待备份数据量
        Integer bakNum = bakPhaseDao.getBakNum(dto);
        for (int i=0; i<=bakNum/SubConfig.BATCH_INSERT_NUM; i++) {
            Integer left = i * SubConfig.BATCH_INSERT_NUM;
            Integer right = SubConfig.BATCH_INSERT_NUM;
            if (left == bakNum/SubConfig.BATCH_INSERT_NUM) {
                right += bakNum - i*SubConfig.BATCH_INSERT_NUM;
            }
            Integer finalRight = right;
            //加载字段对应的数据值, 批量入库
            try {
                //按顺序获取要备份的数据值
                List<SqlRow> sqlRowList = bakPhaseDao.getTargetData(dto, left, finalRight);
                log.info("入库：" + left + " " + Thread.currentThread() );
                if (ObjectUtil.isEmpty(sqlRowList)) {
                    log.info("该归档目标表空值, 跳过此次归档动作，库名： " + dto.getTargetDb() + " 表名：" + dto.getTargetTable());
                    break;
                }
                bakPhaseDao.batchInsertBak(sql, sqlRowList, dto.getBakFieldDTOList(), SourceConfig.BAK);
                sqlRowList.clear();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        Instant endTime = Instant.now();
        // 计算时间间隔
        Duration duration = Duration.between(startTime, endTime);
        long seconds = duration.getSeconds();
        log.info("本次备份表[{}]{}条用时{}秒",dto.getTargetTable(),bakNum,seconds);
        //记录
        bakRecord.addRecordLog(dto, bakNum,seconds);
        if (SourceDataEnum.HAS_DATA.getVal().equals(dto.getSysBakConfigPO().getSourceData())) {
            bakPhaseDao.deleteTargetData(dto);
            bakRecord.addRecordDelLog(dto, bakNum);
        }
        //更新归档配置表及集合表信息
        handleUpdate(dto, bakNum);
    }

    /**
     * 加载占位符和占位字段
     * @param fieldDTOList 字段列表
     * @param tableName 入库目标表名
     * @return
     */
    protected String getSqlStr(List<BakFieldDTO> fieldDTOList, String tableName) {
        //准备字段与占位字符
        StringBuffer fieldStr = new StringBuffer();
        StringBuffer codeStr = new StringBuffer();
        for (BakFieldDTO fieldDTO : fieldDTOList) {
            fieldStr.append(fieldDTO.getColName()).append(",");
            codeStr.append("?").append(",");
        }
        fieldStr.deleteCharAt(fieldStr.length() - 1);
        codeStr.deleteCharAt(codeStr.length() - 1);

        return "insert into "+ tableName +" ( " + fieldStr + " ) values (" + codeStr + ")";
    }

    /**
     * 归档完成后的配置相关信息更新
     * @param dto
     * @param size 本次备份数据量大小
     */
    private void handleUpdate(BakTaskDTO dto, Integer size) throws Exception {
        //计算下一次归档时间及范围,及表数据量
        BakConfigDTO configDTO = BeanUtil.copyProperties(dto.getSysBakConfigPO(), BakConfigDTO.class);

        bakTypeContext.loadContext(configDTO.getType());
        configDTO = bakTypeContext.loadDate(configDTO);
        configDTO.initUpData(size);
        //更新配置信息
        bakPhaseDao.updateAfterBak(configDTO);
        //更新集合表信息
        bakCollectionDao.updateAfterCollection(dto, size);
    }
}
