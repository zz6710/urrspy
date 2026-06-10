package com.kayak.clear.service.business;

import com.kayak.clear.req.PubReq;
import com.kayak.dps.direct.service.DealValueDataService;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 批处理-估值表解析
 * axin
 * 20220713
 */
@Slf4j
@Component
@Scope("prototype")
public class DealValueTaskService extends BusinessBaseTaskService{

    @Autowired
    public DealValueDataService dealValueDataService;

    @StepNo(stepNo = 1)
    protected void dealGZBValueData(PubReq request) throws Exception{

        dealValueDataService.dealProdAssetBln(request);

    }
}
