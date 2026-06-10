package com.kayak.dps.ods.action;

import com.kayak.core.sql.SqlParam;
import com.alibaba.fastjson.JSONObject;
import com.kayak.core.system.RequestSupport;
import com.kayak.dps.app.model.BondInfoModel;
import com.kayak.dps.ods.service.AssetCollectionService;
import com.kayak.dps.ods.service.BondInfoService;
import com.kayak.graphql.model.FetcherData;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bondInfo")
public class BondInfoController {
    @Resource(name = "bondInfoService")
    private BondInfoService bondInfoService;

    @Resource(name = "assetCollectionService")
    private AssetCollectionService assetCollectionService;
    /**
     * 新增债券信息
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/addBondInfoModel.json")
    public String addBondInfoModel() throws Exception {
        Map<String, Object> param = RequestSupport.getParameters();
        //校验唯一性
        param.put("checkTableName","ods_bond_bas_inf");
        if(assetCollectionService.isOnlyOne(param)>0){
            return RequestSupport.updateReturnJson(false,  "该债券已存在！", null).toString();
        }
        List<BondInfoModel> embOptFGridDataList = null;
        List<BondInfoModel> isRepaidGridDataList = null;
        List<BondInfoModel> couponTypeGridDataList = null;
        if (param.get("embOptFGridData")!=null){
            if (!"".equals(param.get("embOptFGridData").toString()))
            embOptFGridDataList = JSONObject.parseObject(param.get("embOptFGridData").toString(), BondInfoModel.class).getEmbOptFGridData();
            param.remove("embOptFGridData");
        }
        if (param.get("isRepaidGridData")!=null){
            if (!"".equals(param.get("isRepaidGridData").toString()))
            isRepaidGridDataList = JSONObject.parseObject(param.get("isRepaidGridData").toString(), BondInfoModel.class).getIsRepaidGridData();
            param.remove("isRepaidGridData");
        }
        if (param.get("couponTypeGridData")!=null){
            if (!"".equals(param.get("couponTypeGridData").toString()))
            couponTypeGridDataList = JSONObject.parseObject(param.get("couponTypeGridData").toString(), BondInfoModel.class).getCouponTypeGridData();
            param.remove("couponTypeGridData");
        }
        SqlParam<BondInfoModel> sqlParam = new FetcherData<>(param, BondInfoModel.class);
        sqlParam.getModel().setEmbOptFGridData(embOptFGridDataList);
        sqlParam.getModel().setIsRepaidGridData(isRepaidGridDataList);
        sqlParam.getModel().setCouponTypeGridData(couponTypeGridDataList);
        return bondInfoService.addBondInfoModel(sqlParam);
    }
    /**
     * 修改债券信息
     * @param
     * @throws Exception
     */
    @RequestMapping(value = "/editBondInfoModel.json")
    public String editBondInfoModel() throws Exception {
        Map<String, Object> param = RequestSupport.getParameters();
        List<BondInfoModel> embOptFGridDataList = null;
        List<BondInfoModel> isRepaidGridDataList = null;
        List<BondInfoModel> couponTypeGridDataList = null;
        if (param.get("embOptFGridData")!=null){
            if (!"".equals(param.get("embOptFGridData").toString()))
            embOptFGridDataList = JSONObject.parseObject(param.get("embOptFGridData").toString(), BondInfoModel.class).getEmbOptFGridData();
            param.remove("embOptFGridData");
        }
        if (param.get("isRepaidGridData")!=null){
            if (!"".equals(param.get("isRepaidGridData").toString()))
            isRepaidGridDataList = JSONObject.parseObject(param.get("isRepaidGridData").toString(), BondInfoModel.class).getIsRepaidGridData();
            param.remove("isRepaidGridData");
        }
        if (param.get("couponTypeGridData")!=null){
            if (!"".equals(param.get("couponTypeGridData").toString()))
            couponTypeGridDataList = JSONObject.parseObject(param.get("couponTypeGridData").toString(), BondInfoModel.class).getCouponTypeGridData();
            param.remove("couponTypeGridData");
        }
        SqlParam<BondInfoModel> sqlParam = new FetcherData<>(param, BondInfoModel.class);
        sqlParam.getModel().setEmbOptFGridData(embOptFGridDataList);
        sqlParam.getModel().setIsRepaidGridData(isRepaidGridDataList);
        sqlParam.getModel().setCouponTypeGridData(couponTypeGridDataList);
        return bondInfoService.updateBondInfoModel(sqlParam);
    }


}
