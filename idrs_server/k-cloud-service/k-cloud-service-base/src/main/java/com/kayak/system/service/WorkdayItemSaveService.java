package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.core.exception.PromptException;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.util.Tools;
import com.kayak.system.dao.WorkdayDao;
import com.kayak.system.model.WorkdayItem;
import com.kayak.system.model.WorkdayItemSave;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@APIDefine(desc = "工作日保存服务", model = WorkdayItemSave.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class WorkdayItemSaveService {

    private final WorkdayDao workdayDao;

    @API(desc = "保存工作日列表", operation = APIOperation.UPDATE)
    public String save(SqlParam<WorkdayItemSave> params) throws Exception {
        WorkdayItemSave item = params.getModel();
        String workdays = item.getWorkdays();
        String pgmno = item.getPgmno();
        String year = item.getYear();
        if (Tools.isBlank(pgmno) || Tools.isBlank(year)) {
            log.error("保存工作日列表，pgmno[{}],year[{}]", pgmno, year);
            throw new PromptException("保存失败，缺少必要参数");
        }
        String[] workdayArr = workdays.split(",");
        List<WorkdayItem> workdayItems = new ArrayList<>();
        for (String workday : workdayArr) {
            if (Tools.isBlank(workday)) {
                continue;
            }
            WorkdayItem workdayItem = new WorkdayItem();
            workdayItems.add(workdayItem);
            workdayItem.setPgmno(pgmno);
            workdayItem.setWorkday(workday.trim());
        }
        workdayDao.saveWorkdayItems(pgmno, year, workdayItems);
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }
}
