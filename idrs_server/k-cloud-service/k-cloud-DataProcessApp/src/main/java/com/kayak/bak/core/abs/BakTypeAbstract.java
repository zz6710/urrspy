package com.kayak.bak.core.abs;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.utils.BakDateUtil;
import com.kayak.bak.model.dto.BakConfigDTO;

import java.text.ParseException;

public abstract class BakTypeAbstract {

    //计算上次更新日期
    public BakConfigDTO getLastDate(BakConfigDTO dto, BakConfigDTO newDto) {
        if (ObjectUtil.isNotEmpty(dto.getNextDate())) {
            newDto.setLastDate(dto.getNextDate());
        }
        return newDto;
    }

    //计算下次更新日期
    public BakConfigDTO getNextDate(BakConfigDTO dto, BakConfigDTO newDto) throws ParseException {
        if (ObjectUtil.isAllEmpty(dto.getLastDate(), dto.getNextDate())) {
            //上次归档日期和下次归档日期均为空，首次计算日期，通过生效日期计算
            newDto.setNextDate(
                    BakDateUtil.add(dto.getBeginDate(), "yyyyMMdd", dto.getFrequency())
            );
        } else {
            //上次归档日期非空，通上次归档日期计算下次归档日期
            newDto.setLastDate(newDto.getNextDate());
            newDto.setNextDate(
                    BakDateUtil.add(newDto.getNextDate(), "yyyyMMdd", dto.getFrequency())
            );
        }
        return newDto;
    }

    //计算上次更新日期区间
    public BakConfigDTO getLastDateRage(BakConfigDTO dto, BakConfigDTO newDto) {
        newDto.setLastLeftDate(dto.getNextLeftDate());
        newDto.setLastRightDate(dto.getNextRightDate());
        return newDto;
    }

    //计算下次更新日期区间(左区间)
    public BakConfigDTO getNextLeftDate(BakConfigDTO dto, BakConfigDTO newDto) throws ParseException {
        //计算下次归档数据的日期区间
        if (ObjectUtil.isNotEmpty(dto.getNextDate())) {
            //下次归档的数据起始日期为--上次归档日期+1
            newDto.setNextLeftDate(BakDateUtil.add(dto.getNextDate(), "yyyyMMdd", 1));
        } else if (ObjectUtil.isEmpty(dto.getLastDate())) {
            //无上次归档日期，初次归档配置，归档日期前全部数据
            newDto.setNextLeftDate("00000000");
        }
        return newDto;
    }

    /**
     * 计算下次日期区间（右区间）
     * @param newDto
     * @return
     */
    public BakConfigDTO getNextRightDate(BakConfigDTO newDto) throws ParseException {
        //下次归档数据结束日期为--下次归档日期
        newDto.setNextRightDate(newDto.getNextDate() );
        return newDto;
    }
}
