package com.kayak.dps.dws.service;

import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.util.DateUtil;
import com.kayak.dps.dws.dao.DwsBondCurrentRatDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BondNewestRatCompService {

    @Autowired
    private DwsBondCurrentRatDao dwsBondCurrentRatDao;

    public void DwsBondCurrentRatInfoGen (Map<String, Object> params) throws Exception {
        String settle_date = String.valueOf(params.get("deal_date"));
        /**
         * 仅统计截止月底债券评级信息，
         * 判断处理日期是否为次月第一个自然日，处理上月月底数据,非月初直接返回不执行
         */
        /*if(!settle_date.equals(DateUtil.getFirstDayDateOfMonth(settle_date))){
            log.info("非本月月初,跳过处理债券最新债项及主体评级数据");
            return ;
        }*/

        List<String> bondList = dwsBondCurrentRatDao.getBondScrIdList(settle_date);//获取所有需要处理评级的债券唯一编码scr_id
        for (String scr_id : bondList) {
            DaoUtil.doTrans(()-> {
                Map<String, Object> param_crd = new HashMap<>();
                /**
                 * 根据评级取数规则进行匹配
                 */
                param_crd.put("scr_id", scr_id);
                param_crd.put("deal_date", params.get("deal_date"));
                param_crd = dwsBondCurrentRatDao.getBondCurrentIssuerRat(param_crd);//获取该支债券最新主体评级数据
                param_crd = dwsBondCurrentRatDao.getBondCurrentBondRat(param_crd);//获取该支债券最新债项评级数据

                if (!"".equals(param_crd.get("BND_CRD_RAT")) || !"".equals(param_crd.get("ISU_CRD_RAT"))) {
                    try {
                        dwsBondCurrentRatDao.genBondLatestRatInfo(param_crd);//往债券信息最新评级表中插入数据
                    } catch (Exception e) {
                        log.info("证券编号: " + scr_id + " 在处理最新评级数据异常:" + e.getMessage());
                    }
                }
            });
        }
    }

}
