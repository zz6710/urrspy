package com.kayak.auth.dao;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErrLogDao extends ComnDao {
    public void addErrLog(Map<String,Object> errLogMap) throws Exception {
       super.update("insert into sys_err_log(id,userid,ip,operation,operation_date,operation_time) values($AUTOIDS{id},$S{userid},$S{ip},$S{operation},$S{operation_date},$S{operation_time})",errLogMap);
    }
}
