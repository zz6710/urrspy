package com.kayak.bak.core.abs;

import cn.hutool.core.bean.BeanUtil;
import com.kayak.bak.enums.BakTaskTypeEnum;
import com.kayak.bak.model.dto.BakConfigDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BakTypeContext {

    private BakTypeAbstract bakTypeAbstract;

    @Resource
    private Map<String, BakTypeAbstract> map = new ConcurrentHashMap();

    public void loadContext(String type) throws Exception {
        String server = BakTaskTypeEnum.getServer(type);
        bakTypeAbstract = map.get(server);
    }

    /**
     * 加载计算后的时间
     * @param dto
     * @return
     * @throws ParseException
     */
    public BakConfigDTO loadDate(BakConfigDTO dto) throws ParseException {
        BakConfigDTO newDto = BeanUtil.copyProperties(dto, BakConfigDTO.class);
        newDto = bakTypeAbstract.getLastDate(dto, newDto);
        newDto = bakTypeAbstract.getLastDateRage(dto, newDto);
        newDto = bakTypeAbstract.getNextLeftDate(dto, newDto);
        newDto = bakTypeAbstract.getNextDate(dto, newDto);
        //下次归档日期计算后再计算下次归档终止日期
        newDto = bakTypeAbstract.getNextRightDate(newDto);
        return newDto;
    }
}
