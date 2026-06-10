package com.kayak.report.service;

import com.alibaba.fastjson.JSONObject;
import com.kayak.report.dao.ReportSqlResultDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReportSqlResultService {

    @Autowired
    private ReportSqlResultDao reportSqlResultDao;

    public JSONObject getRes(Map<String, Object> params) throws Exception{
        JSONObject pdunitM5  = new JSONObject();
        try {
            pdunitM5 = reportSqlResultDao.getExeReult(params);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return pdunitM5;
    }
}
