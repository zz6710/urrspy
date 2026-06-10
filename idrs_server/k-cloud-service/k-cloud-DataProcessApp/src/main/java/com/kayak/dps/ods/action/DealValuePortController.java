package com.kayak.dps.ods.action;

import com.kayak.core.system.RequestSupport;

import com.kayak.dps.ods.service.DealPortFileService;
import com.kayak.dps.ods.service.DealValuePortService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping(value = "/disclosure/evaluate/emp")
public class DealValuePortController {

    private static Logger logger = LoggerFactory.getLogger(DealValuePortController.class);

    @Autowired
    private DealPortFileService dealPortFileService;

    @Resource(name = "dealValuePortService")
    private DealValuePortService dealValuePortService;


    /**
     * 估值回传文件解析入库
     * @return
     */
    @RequestMapping(value="/parseFile.action")
    public String dealResvFile() {
        Map<String, Object> params = RequestSupport.getParameters();
        try{
            dealPortFileService.dealResvFile(params);
            return RequestSupport.updateReturnJson(true,"接口接收成功！",null).toString();
        } catch(Exception e){
            logger.error(" 接口接收失败: ", e);
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    /**
     * 估值、减值文件生成
     * @return
     */
    @RequestMapping(value="/dealValuePort.action")
    public String dealSendFile() {
        logger.info("****************接口发送 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            dealPortFileService.dealSendFile(params);
            return RequestSupport.updateReturnJson(true,"接口发送成功！",null).toString();

        } catch (Exception e) {
            logger.error(" 接口发送失败: ", e);
            logger.info("****************接口发送 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }

    /**
     * 接口管理-文件接收
     * @return
     */
    @RequestMapping(value="/portBatchRecv.action")
    public String portBatchRecv() {
        logger.info("****************接口接收 Start*********************");
        Map<String, Object> params = RequestSupport.getParameters();
        try {
            new Thread(()->{
                try {
                    dealPortFileService.dealAllPortInfo(params);
                } catch (Exception e) {
                    logger.error("接口接收失败，原因是{}", e);
                }
            }).start();

            return RequestSupport.updateReturnJson(true,"接口接收处理中,请稍后查看！",null).toString();

        } catch (Exception e) {
            logger.error(" 接口接收失败: ", e);
            logger.info("****************接口接收 end*********************");
            return RequestSupport.updateReturnJson(false,e.getMessage(),null).toString();
        }
    }



}
