package com.kayak.bak.core.bak;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.dao.*;
import com.kayak.bak.enums.SourceDataEnum;
import com.kayak.bak.model.dto.BakLogDTO;
import com.kayak.bak.model.po.SysBakCollectionPO;
import com.kayak.bak.model.po.SysBakConfigPO;
import com.kayak.base.dao.DataSourceProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TableDeleteService {

    @Resource
    private BakPhaseDao bakPhaseDao;
    @Autowired
    BakDeleteDao bakDeleteDao;
    @Autowired
    BakCollectionDao bakCollectionDao;
    @Autowired
    BakConfigDao bakConfigDao;
    @Resource
    private BakLogDao bakLogDao;

    public void execute(String taskDate) throws Exception {
        //查询需要备份的表配置
        List<SysBakConfigPO> sysBakConfigPOList = bakPhaseDao.getSysBakConfig(taskDate);
        if (ObjectUtil.isEmpty(sysBakConfigPOList)) {
            return ;
        }
        //查询备份仓库集合表信息
        List<SysBakCollectionPO> sysBakCollectionPOList = bakPhaseDao.getSysBakCollection(sysBakConfigPOList);
        Map<String, List<SysBakCollectionPO>> listMap = sysBakCollectionPOList.stream().collect(Collectors.groupingBy(SysBakCollectionPO::getTargetTable, Collectors.toList()));
        // 读取配置进行删除
        for (SysBakConfigPO sysBakConfigPO : sysBakConfigPOList) {
            Instant startTime = Instant.now();
            // 备份表列表
            List<SysBakCollectionPO> bakTableList = listMap.get(sysBakConfigPO.getTargetTable());
            //最左区间
            if ("1".equals(sysBakConfigPO.getDeleteType())) {
                sysBakConfigPO.setDeleteStartDate(addDays(sysBakConfigPO.getDeleteStartDate(), 1));//日期+1
                sysBakConfigPO.setDeleteEndDate(addDays(sysBakConfigPO.getDeleteEndDate(), 1));
                leftTruncateTable(sysBakConfigPO, bakTableList);
            }
            //完全匹配
            else if ("2".equals(sysBakConfigPO.getDeleteType())) {
                sysBakConfigPO.setDeleteStartDate(addDays(sysBakConfigPO.getDeleteStartDate(), 30));//日期+30
                sysBakConfigPO.setDeleteEndDate(addDays(sysBakConfigPO.getDeleteEndDate(), 30));
                minTruncateTable(sysBakConfigPO, bakTableList);
            }
            //精确删除
            else if ("3".equals(sysBakConfigPO.getDeleteType())) {
                sysBakConfigPO.setDeleteStartDate(addDays(sysBakConfigPO.getDeleteStartDate(), 7));//日期+7
                sysBakConfigPO.setDeleteEndDate(addDays(sysBakConfigPO.getDeleteEndDate(), 7));
                deleteTable(sysBakConfigPO, bakTableList);
            }
            int tableBakNum = 0;
            for (SysBakCollectionPO sysBakCollectionPO : bakTableList) {
                String bakTableName = sysBakCollectionPO.getBakTable();
                // 查询数量
                int count = bakCollectionDao.getCountForDb(DataSourceProperty.BAK, bakTableName);
                tableBakNum += count;
                // 更新集合信息
                sysBakCollectionPO.setBakNum(String.valueOf(count));
                bakCollectionDao.updateBakCollectionBakNum(sysBakCollectionPO);
            }
            Instant endTime = Instant.now();
            // 计算时间间隔
            Duration duration = Duration.between(startTime, endTime);
            long seconds = duration.getSeconds();
            log.info("本次删除表[{}]用时{}秒", sysBakConfigPO.getTargetTable(), seconds);
            // 更新配置信息
            sysBakConfigPO.setTableBakNum(tableBakNum);

            bakConfigDao.updateBakConfig(sysBakConfigPO);
            // 记录日志
            BakLogDTO bakLogDTO = new BakLogDTO().convertBakTableDelDTO(sysBakConfigPO, bakTableList, seconds);
            bakLogDao.addRecordLog(bakLogDTO);
        }
    }

    /**
     * 最左区间删除
     * @param sysBakConfigPO
     * @param bakTableList
     * @throws Exception
     */
    public void leftTruncateTable(SysBakConfigPO sysBakConfigPO, List<SysBakCollectionPO> bakTableList) throws Exception {
        String deleteStartDate = sysBakConfigPO.getDeleteStartDate();
        String deleteEndDate = sysBakConfigPO.getDeleteEndDate();
        // 删除表
        for (SysBakCollectionPO sysBakCollectionPO : bakTableList) {
            if (deleteStartDate.compareTo(sysBakCollectionPO.getEndDate()) < 0) {
                bakDeleteDao.truncate(sysBakCollectionPO.getBakTable());
            }
        }
    }

    /**
     * 最小区间删除
     * @param sysBakConfigPO
     * @param bakTableList
     * @throws Exception
     */
    public void minTruncateTable(SysBakConfigPO sysBakConfigPO, List<SysBakCollectionPO> bakTableList) throws Exception {
        String deleteStartDate = sysBakConfigPO.getDeleteStartDate();
        String deleteEndDate = sysBakConfigPO.getDeleteEndDate();
        for (SysBakCollectionPO sysBakCollectionPO : bakTableList) {
            if (deleteStartDate.compareTo(sysBakCollectionPO.getStartDate()) < 0 && deleteEndDate.compareTo(sysBakCollectionPO.getEndDate()) > 0) {
                bakDeleteDao.truncate(sysBakCollectionPO.getBakTable());
            }
        }
    }

    /**
     * 精确删除
     * @param sysBakConfigPO
     * @param bakTableList
     * @throws Exception
     */
    public void deleteTable(SysBakConfigPO sysBakConfigPO, List<SysBakCollectionPO> bakTableList) throws Exception {
        String deleteStartDate = sysBakConfigPO.getDeleteStartDate();
        String deleteEndDate = sysBakConfigPO.getDeleteEndDate();
        for (SysBakCollectionPO sysBakCollectionPO : bakTableList) {
            String bakTableName = sysBakCollectionPO.getBakTable();
            // 需要truncate的表
            if (deleteStartDate.compareTo(sysBakCollectionPO.getStartDate()) < 0 && deleteEndDate.compareTo(sysBakCollectionPO.getEndDate()) > 0) {
                bakDeleteDao.truncate(bakTableName);
            }

            // 需要删除的表
            else if (deleteStartDate.compareTo(sysBakCollectionPO.getEndDate()) < 0) {
                bakDeleteDao.delete(bakTableName, sysBakConfigPO.getTargetField(), deleteStartDate, deleteEndDate);
            }
            // 需要删除的表
            else if (deleteEndDate.compareTo(sysBakCollectionPO.getEndDate()) > 0) {
                bakDeleteDao.delete(bakTableName, sysBakConfigPO.getTargetField(), deleteStartDate, deleteEndDate);
            }
        }
    }

    public String addDays(String date, int number) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate resDate = localDate.plusDays(number);
        return resDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

}
