package com.kayak.pms.disclosureControl.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.pms.disclosureControl.dao.ScheduleProdRuleDao;
import com.kayak.pms.disclosureControl.model.DisclosureProdTask;
import com.kayak.pms.disclosureControl.model.ScheduleProdRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@APIDefine(desc = "产品信披规则服务", model = ScheduleProdRule.class)
public class ScheduleProdRuleService {

    @Autowired
    private ScheduleProdRuleDao disclosureProdRuleDao;

    public List<ScheduleProdRule> findProdRules(ScheduleProdRule scheduleProdRule) throws Exception {

        return disclosureProdRuleDao.findProdRules(scheduleProdRule);
    }

    public ScheduleProdRule findRuleById(DisclosureProdTask prodTask) throws Exception {
        ScheduleProdRule rule = disclosureProdRuleDao.findRuleById(prodTask.getT8DisclosureProdRuleId());
        return rule;
    }

}
