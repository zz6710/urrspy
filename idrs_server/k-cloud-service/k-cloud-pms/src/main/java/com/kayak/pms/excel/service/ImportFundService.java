package com.kayak.pms.excel.service;

import com.kayak.base.dao.util.DaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class ImportFundService extends ExcelService {

    @Override
    public Integer setId() {
        return 2;
    }

    @Override
    public void saveData() throws Exception {

        //解析数据存储
        DaoUtil.doTrans(super::saveParseData);

    }

}
