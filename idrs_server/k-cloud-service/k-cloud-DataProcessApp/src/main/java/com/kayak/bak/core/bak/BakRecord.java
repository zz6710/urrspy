package com.kayak.bak.core.bak;

import com.kayak.bak.business.dao.BakLogDao;
import com.kayak.bak.model.dto.BakCreateTableDTO;
import com.kayak.bak.model.dto.BakLogDTO;
import com.kayak.bak.model.dto.BakTaskDTO;
import com.kayak.bak.model.dto.RedoDataDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 归档动作记录
 */
@Service
public class BakRecord {

    @Resource
    private BakLogDao bakLogDao;

    /**
     * 记录归档操作信息(创建仓库表)
     * @param dto
     */
    public void addRecordLog(BakCreateTableDTO dto) throws Exception{
        BakLogDTO bakLogDTO = new BakLogDTO().convertBakCreateTableDTO(dto);
        bakLogDao.addRecordLog(bakLogDTO);
    }

    /**
     * 记录归档操作信息(数据入库)
     * @param dto 任务模型
     * @param dto 入库数据量
     */
    public void addRecordLog(BakTaskDTO dto, Integer size,Long duration) throws Exception{
        BakLogDTO bakLogDTO = new BakLogDTO().convertBakTaskDTO(dto, size,duration);
        bakLogDao.addRecordLog(bakLogDTO);
    }

    /**
     * 记录归档操作信息(源表数据删除)
     * @param dto 任务模型
     * @param dto 入库数据量
     */
    public void addRecordDelLog(BakTaskDTO dto, Integer size) throws Exception{
        BakLogDTO bakLogDTO = new BakLogDTO().convertBakTaskDelDTO(dto, size);
        bakLogDao.addRecordLog(bakLogDTO);
    }

    /**
     * 记录还原操作信息
     * @param dto 任务模型
     * @param dto 入库数据量
     */
    public void addRecordLog(RedoDataDTO dto, Integer size) throws Exception{
        BakLogDTO bakLogDTO = new BakLogDTO().convertRecordDTO(dto, size);
        bakLogDao.addRecordLog(bakLogDTO);
    }

}
