package com.kayak.dps.ods.action;

import com.alibaba.fastjson.JSONObject;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.model.MidDirectFusion;
import com.kayak.dps.app.service.MidDirectFusionService;
import com.kayak.graphql.model.FetcherData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/DrectFunsion")
public class DrectFunsionController {
    @Resource(name = "midDirectFusionService")
    private MidDirectFusionService midDirectFusionService;
    /**
     * 补录债券信息
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editDrectFunsion.json")
    public String editDrectFunsion() throws Exception {
        Map<String, Object> param = RequestSupport.getParameters();
        List<MidDirectFusion> embOptFGridDataList = null;
        List<MidDirectFusion> isRepaidGridDataList = null;
        List<MidDirectFusion> couponTypeGridDataList = null;
        if (param.get("embOptFGridData")!=null){
            if (!"".equals(param.get("embOptFGridData").toString()))
            embOptFGridDataList = JSONObject.parseObject(param.get("embOptFGridData").toString(), MidDirectFusion.class).getEmbOptFGridData();
            param.remove("embOptFGridData");
        }
        if (param.get("isRepaidGridData")!=null){
            if (!"".equals(param.get("isRepaidGridData").toString()))
            isRepaidGridDataList = JSONObject.parseObject(param.get("isRepaidGridData").toString(), MidDirectFusion.class).getIsRepaidGridData();
            param.remove("isRepaidGridData");
        }
        if (param.get("couponTypeGridData")!=null){
            if (!"".equals(param.get("couponTypeGridData").toString()))
            couponTypeGridDataList = JSONObject.parseObject(param.get("couponTypeGridData").toString(), MidDirectFusion.class).getCouponTypeGridData();
            param.remove("couponTypeGridData");
        }
        SqlParam<MidDirectFusion> sqlParam = new FetcherData<>(param, MidDirectFusion.class);
        sqlParam.getModel().setEmbOptFGridData(embOptFGridDataList);
        sqlParam.getModel().setIsRepaidGridData(isRepaidGridDataList);
        sqlParam.getModel().setCouponTypeGridData(couponTypeGridDataList);
        return midDirectFusionService.updateMidDirectSupplyFusion(sqlParam);
    }


}
