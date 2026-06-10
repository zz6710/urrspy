package com.kayak.dps.ods.action;


import com.kayak.core.system.RequestSupport;
import com.kayak.dps.ods.service.DealDwdService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "/disclosure/evaluate/dwd")
public class DealDwdPortController {
    private static Logger logger = LogManager.getLogger(DealValuePortController.class);

    @Resource(name = "dealDwdService")
    private DealDwdService dealDwdService;

    //处理产品监管信息
    @RequestMapping(value = "/dealPordCbrdat.action")
    public String dealPordCbrdat() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealPordCbrdat(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }

    }

    //删除产品监管信息
    @RequestMapping(value = "/deletePordCbrdat.action")
    public String deletePordCbrdat() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.deletePordCbrdat(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();
        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //处理产品限制信息
    @RequestMapping(value = "/dealPordLimitInfo.action")
    public String dealPordLimitInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealPordLimitInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //删除产品限制信息
    @RequestMapping(value = "/deletePordLimitInfo.action")
    public String deletePordLimitInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.deletePordLimitInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();
        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //处理客戶基本信息
    @RequestMapping(value = "/dealInvestorIdentity.action")
    public String dealInvestorIdentity() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealInvestorIdentity(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }
    //处理客户交易明细
    @RequestMapping(value = "/dealInvestorDetail.action")
    public String dealInvestorDetail() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealInvestorDetail(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    //删除客户交易明细
    @RequestMapping(value = "/deleteInvestorDetail.action")
    public String deleteInvestorDetail() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.deleteInvestorDetail(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }



    //处理客户份额持仓明细
    @RequestMapping(value = "/dealVolBalanceSum.action")
    public String dealVolBalanceSum() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealVolBalanceSum(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //处理产品资产持仓明细
    @RequestMapping(value = "/dealAssetHodingDetail.action")
    public String dealAssetHodingDetail() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealAssetHodingDetail(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //处理产品资产净值信息
    @RequestMapping(value = "/dealNetProdInfo.action")
    public String dealNetProdInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealNetProdInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    //处理产品费用信息
    @RequestMapping(value = "/dealProdFeeInfo.action")
    public String dealProdFeeInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealProdFeeInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    //处理产品费用支付明细
    @RequestMapping(value = "/dealProdPayDetails.action")
    public String dealProdPayDetails() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealProdPayDetails(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }









    //处理产品基本信息
    @RequestMapping(value = "/dealProdBaseInfo.action")
    public String dealProdBaseInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealProdBaseInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }


    /**
     * 处理债券基本信息
     * @return
     */
    @RequestMapping(value = "/dealBondInfo.action")
    public String dealBondInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealBondInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    /**
     * 处理基金基本信息
     * @return
     */
    @RequestMapping(value = "/dealFundInfo.action")
    public String dealFundInfo() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealDwdService.dealFundInfo(params);
            return RequestSupport.updateReturnJson(true,"接口处理完成",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

}
