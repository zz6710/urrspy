package com.kayak.pms.prodLiquidation.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.kayak.aspect.annotations.API;
import com.kayak.aspect.annotations.APIAuth;
import com.kayak.aspect.annotations.APIDefine;
import com.kayak.aspect.annotations.APIOperation;
import com.kayak.base.dao.util.DaoUtil;
import com.kayak.core.sql.SqlParam;
import com.kayak.core.sql.SqlResult;
import com.kayak.core.sql.SqlRow;
import com.kayak.core.system.RequestSupport;
import com.kayak.core.system.ResponseResult;
import com.kayak.core.system.SysUtil;

import com.kayak.pms.channelInterface.channelEnum.ParamStatusEnum;
import com.kayak.pms.channelInterface.service.ChannelInterfaceTaskService;
import com.kayak.utils.DateHelper;
import com.kayak.pms.opFlow.engine.entity.ProcessInstance;
import com.kayak.pms.opFlow.engine.service.ProcessInstanceService;
import com.kayak.pms.prodLiquidation.dao.ProdFlowDao;

import com.kayak.pms.prodLiquidation.model.ProdFlow;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shexianyu
 * @date 2022/9/28 15:01
 * @desc
 */
@Service
@APIDefine(desc = "产品发起流程服务", model = ProdFlow.class,log = false)
public class ProdFlowService {
    private static final Logger logger = LoggerFactory.getLogger(ProdFlowService.class);

    @Autowired
    private ProdFlowDao prodFlowDao;

    @Autowired
    private ProcessInstanceService processInstanceService;

    @Autowired
    private ChannelInterfaceTaskService channelInterfaceTaskService;

    @API(desc = "产品产品清盘方案流程数据", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ProdFlow> findProdFlowLiquidation(SqlParam<ProdFlow> param) throws Exception {
        return prodFlowDao.findProdFlowLiquidation(param);
    }

    @API(desc = "查询产品清盘信息", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ProdFlow> findProdFlowLiquidation1(SqlParam<ProdFlow> param) throws Exception {
        return prodFlowDao.findProdFlowLiquidation1(param);
    }

    @API(desc = "查询产品流程产品代码", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public SqlResult<ProdFlow> findProdCodeFlow(SqlParam<ProdFlow> param) throws Exception {
        return prodFlowDao.findProdCodeFlow(param);
    }

    @API(desc = "新增产品流程数据（清盘方案）", auth = APIAuth.NO, operation = APIOperation.INSTER)
    public String addProdFlowLiquidation(SqlParam<ProdFlow> param) throws Exception {
        List<ProdFlow> prodFlowList = param.getModel().getProdFlowList();
        try {
            DaoUtil.doTrans(() -> {
                for (ProdFlow prodFlow : prodFlowList) {
                    prodFlow.setCrtUser(SysUtil.getLoginUserid());
                    prodFlow.setCrtDate(DateHelper.getCurrentDateTime());
                    //新增产品操作流程数据
                    prodFlowDao.addProdFlowLiquidation(prodFlow);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "保存失败：" + e, null).toString();
        }
        return RequestSupport.updateReturnJson(true, "保存成功", null).toString();
    }

    @API(desc = "更新产品操作流程信息", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateProdFlowInfo(SqlParam<ProdFlow> param) throws Exception {
        param.getModel().setUpdUser(SysUtil.getLoginUserid());
        param.getModel().setUpdDate(DateHelper.getCurrentDateTime());
        try {
            int effect = prodFlowDao.updateProdFlowInfo(param.getModel()).getEffect();
            if (effect > 0)
                return RequestSupport.updateReturnJson(true, "更新成功", null).toString();
            return RequestSupport.updateReturnJson(false, "更新失败：", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "更新失败", null).toString();
        }
    }

    @API(desc = "更新产品操作流程状态", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String updateProdFlowStatus(SqlParam<ProdFlow> param) throws Exception {
        param.getModel().setUpdUser(SysUtil.getLoginUserid());
        param.getModel().setUpdDate(DateHelper.getCurrentDateTime());
        try {
            int effect = prodFlowDao.updateProdFlowStatus(param.getModel()).getEffect();
            if (effect > 0)
                return RequestSupport.updateReturnJson(true, "更新成功", null).toString();
            return RequestSupport.updateReturnJson(false, "更新失败：", null).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "更新失败", null).toString();
        }
    }

    @API(desc = "撤销清盘流程", auth = APIAuth.NO, operation = APIOperation.UPDATE)
    public String revokeProdFlowStatus(SqlParam<ProdFlow> param) throws Exception {
        param.getModel().setUpdUser(SysUtil.getLoginUserid());
        param.getModel().setUpdDate(DateHelper.getCurrentDateTime());
        try {
            DaoUtil.doTrans(()->{
                //删除操作流-流程相关数据
                prodFlowDao.deleteOpfLog(param.getModel().getOpProcessId());
                prodFlowDao.deleteOpfHisTask(param.getModel().getOpProcessId());
                prodFlowDao.deleteOpfProcessInstance(param.getModel().getOpProcessId());
                prodFlowDao.deleteOpfFormData(param.getModel().getOpProcessId());
                prodFlowDao.deleteOpfSubmitParams(param.getModel().getOpProcessId());
                prodFlowDao.deleteOpfTask(param.getModel().getOpProcessId());
                param.getModel().setOpProcessId("");
                prodFlowDao.updateProdFlowInfo(param.getModel());//更新状态为流程撤销
            });
        }catch (Exception e){
            e.printStackTrace();
            return RequestSupport.updateReturnJson(false, "撤销清盘流程失败", null).toString();
        }
        return RequestSupport.updateReturnJson(true, "撤销清盘流程成功", null).toString();
    }

    @API(desc = "清盘-产品经理录入提交审批(操作流)", auth = APIAuth.NO, operation = APIOperation.UPDATE, StatusChangeFlow = "StatusChangeFlow")
    public String prodManagerInputApproval(SqlParam<ProdFlow> param) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        // sxy 审批通过-流转到操作流流程
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessId((String) params.get("opProcessId"));
        processInstance.setProcessVersion((String) params.get("processVersion"));
        processInstance.setProcessInstanceId((String) params.get("opProcessInstance"));
        processInstance.setCurrentNode((String) params.get("currentNode"));
        processInstanceService.approvePass(processInstance, (String) params.get("processInstanceId"));

        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    @API(desc = "清盘-投资经理录入提交审批(操作流)", auth = APIAuth.NO, operation = APIOperation.UPDATE, StatusChangeFlow = "StatusChangeFlow")
    public String investManagerInputApproval(SqlParam<ProdFlow> param) throws Exception {
        Map<String, Object> params = RequestSupport.getParameters();
        // sxy 审批通过-流转到操作流流程
        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setProcessId((String) params.get("opProcessId"));
        processInstance.setProcessVersion((String) params.get("processVersion"));
        processInstance.setProcessInstanceId((String) params.get("opProcessInstance"));
        processInstance.setCurrentNode((String) params.get("currentNode"));
        processInstanceService.approvePass(processInstance, (String) params.get("processInstanceId"));
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }









    @API(desc = "测试审批流方法(无任何业务逻辑)", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String testProcessMethod(SqlParam<ProdFlow> param) throws Exception {
        return RequestSupport.updateReturnJson(true, "操作成功", null).toString();
    }

    /**
     * 更新产品代码到流程表中
     * @throws Exception
     */
    @API(desc = "清盘定时任务", auth = APIAuth.NO, operation = APIOperation.SELECT)
    public void updateProdInfoLiquidation(SqlParam<ProdFlow> param) throws Exception {
        List<SqlRow> sqlRowList = prodFlowDao.findProdCodeAndEndDate();
        String currentDate = DateHelper.getCurrentDate();
        if (sqlRowList.size()>0) {
            for (SqlRow sqlRow : sqlRowList) {
                String prodCode = sqlRow.getString("prod_code");
                String endDate = sqlRow.getString("end_date");
                String prodStatus = sqlRow.getString("prod_status");
                SqlRow row = prodFlowDao.findProdFlowByCode(prodCode);
                if (row != null) {
                    String count = row.getString("count");
                    if ("0".equals(count)) {
                        //清盘流程中无产品代码-则新增
                        ProdFlow prodFlow = new ProdFlow();
                        prodFlow.setProdCode(prodCode);
                        prodFlow.setType("1");//清盘（1为新增）
                        prodFlowDao.addProdFlowLiquidation(prodFlow);
                    } else {
                        ProdFlow prodFlowInfo = prodFlowDao.findProdFlowInfoByCode(prodCode);
                        String processStatus = prodFlowInfo.getProcessStatus();
                        //获取清盘类型为1的数据且为未发起的
                        if ("1".equals(prodFlowInfo.getType())&&"1".equals(processStatus)){
                            if (StringUtils.isNotBlank(endDate)) {
                                Date end = DateUtil.parse(endDate);
                                Date current = DateUtil.parse(currentDate);
                                logger.info("到期日{}",end);
                                logger.info("当前日期{}",current);
                                long day = DateUtil.betweenDay(current, end, false);
                                logger.info("相差天数{}",day);
                                if (day <= 10) {
                                    prodFlowInfo.setProdCode(prodCode);
                                    prodFlowInfo.setType("3");//修改type类型为3，则表示为待办
                                    //更新流程状态
                                    prodFlowDao.updateProdFlowType(prodFlowInfo);
                                }
                            }
                        }

                    }
                }
            }
        }
    }


    //仅用于权限控制
    @API(desc = "查询清盘信息", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public void find1(SqlParam<ProdFlow> param) throws Exception {
    }

    @API(desc = "撤销清盘流程", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public void revoke(SqlParam<ProdFlow> param) throws Exception {
    }

    @API(desc = "发起清盘流程", auth = APIAuth.YES, operation = APIOperation.SELECT)
    public void send(SqlParam<ProdFlow> param) throws Exception {
    }

    @API(desc = "实际到期日调整", auth = APIAuth.YES, operation = APIOperation.UPDATE)
    public String realEndDateAdjust(SqlParam<ProdFlow> param) throws Exception {
        String prodCode = param.getModel().getProdCode();
        String realEndDate = param.getModel().getRealEndDate();
        int effect = prodFlowDao.update("UPDATE t8_prod_period SET real_end_date = '"+realEndDate+"' WHERE prod_code = '"+prodCode+"' ").getEffect();
        if (effect > 0){
            return RequestSupport.updateReturnJson(true,"操作成功!",null).toString();
        }
        return RequestSupport.updateReturnJson(false,"操作失败!",null).toString();
    }
}
