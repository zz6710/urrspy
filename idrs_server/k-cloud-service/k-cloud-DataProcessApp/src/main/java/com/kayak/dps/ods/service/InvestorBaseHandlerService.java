package com.kayak.dps.ods.service;

import com.kayak.base.dao.ComnDao;
import com.kayak.core.sql.SqlRow;
import com.kayak.dps.ods.dao.InvestorBaseHandlerDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvestorBaseHandlerService {

    private static final String INVESTOR_CHANNEL_PRIORITY_009 = "1";//销售渠道:母行009

    private static final String INVESTOR_CHANNEL_PRIORITY_009A = "2";//销售渠道:理财宝009A

    private static final String INVESTOR_CHANNEL_PRIORITY_OTHER = "3";//销售渠道:其他代销渠道

    private static final Logger log = LoggerFactory.getLogger(InvestorBaseHandlerService.class);

    @Autowired
    public ComnDao comnDao;

    @Resource
    private InvestorBaseHandlerDao investorBaseHandlerDao;

    /**
     *
     * @param deal_date
     * @param task_id
     */
    public void investorBaseInfoProcess (String deal_date, String task_id) {
        List<SqlRow> changeList = null;
        Map<String,Object> params = new HashMap<>();
        params.put("deal_date", deal_date);
        //获取需要变更的投资者数据集
        try {
            changeList = investorBaseHandlerDao.getInvestorChangeList(params);
        } catch (Exception e) {
            log.info("---------- " + task_id + "任务: 获取需要变更全量投资者信息异常 -----------");
        }

        log.info("---------- " + task_id + "任务: 需要处理变更全量投资者信息条数" + changeList.size() + "-----------");
        //逐条对数据集进行逻辑处理
        if(changeList.size() > 0) {
            for (SqlRow row : changeList) {
                row.getString("sllr_cd");
            }
        }

    }

}
