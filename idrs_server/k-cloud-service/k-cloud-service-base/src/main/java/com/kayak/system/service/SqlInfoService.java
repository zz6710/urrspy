package com.kayak.system.service;

import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.system.dao.DictDao;
import com.kayak.system.model.DictItem;
import com.kayak.system.model.ServerMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SqlInfoService {

    @Autowired
    private ComnDao comnDao;

    public List<SqlRow> sqlInfoQuery(String id) throws Exception {
        return comnDao.findRows(
            comnDao.findRow(
                String.class, "SELECT DATA_SQLINFO FROM sys_report_css where id = $S{id} limit 1", 0, id
            )
        );
    }
}
