package com.kayak.bak.core.abs;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.bak.business.utils.BakDateUtil;
import com.kayak.bak.model.dto.BakConfigDTO;
import org.springframework.stereotype.Service;

import java.text.ParseException;

/**
 * 按周频率计算归档时间
 */
@Service("BakTypeWeekExt")
public class BakTypeWeekExt extends BakTypeAbstract{


    @Override
    //计算下次更新日期
    public BakConfigDTO getNextDate(BakConfigDTO dto, BakConfigDTO newDto) throws ParseException {
        //上次归档日期为空，通过生效日期计算下次归档日期
        if (ObjectUtil.isEmpty(dto.getNextDate())) {
            newDto.setNextDate(
                    BakDateUtil.getNextWeekMonday(dto.getBeginDate(), dto.getFrequency())
            );
        } else {
            //上次归档日期非空，通上次归档日期计算下次归档日期
            newDto.setLastDate(newDto.getNextDate());
            newDto.setNextDate(
                    BakDateUtil.getNextWeekMonday(dto.getNextDate(), dto.getFrequency())
            );
        }
        return newDto;
    }


}
