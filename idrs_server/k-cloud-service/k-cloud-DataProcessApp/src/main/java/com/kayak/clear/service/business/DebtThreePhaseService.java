package com.kayak.clear.service.business;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.dps.check.enums.DebtOneAndTwoEnum;
import com.kayak.dps.check.enums.DebtThreeEnum;
import com.kayak.dps.direct.util.DirectParams;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 批处理-中债三期直连
 * axin
 * 20220708
 */

@Slf4j
@Component
@Scope("prototype")
public class DebtThreePhaseService extends DebtDirectConnected {

    //需要直连报送的文件
    String isRegisterFile;

    //数据合法性检查
    @StepNo(stepNo = 1)
    protected void debtDirectConnectedStepTwo(PubReq request) throws Exception{
        isRegisterFile = DebtThreeEnum.getEnumVal(request.getTaskId()).getRegisterFile();
        // 前置校验结果验证关闭，跳过数据合法性检查
        if (ObjectUtil.equals(DirectParams.appCheck, "0")) {
            return;
        }
        dataValidityCheckA(isRegisterFile);
    }
    //生成中债三期报送文件
    @StepNo(stepNo = 2)
    protected void debtDirectConnectedStepThree(PubReq request) throws Exception{
        exportRegisterFile(isRegisterFile);
    }
    /*//发送报送文件
    @StepNo(stepNo = 3)
    protected void debtDirectConnectedStepFive(PubReq request) throws Exception{
        sendRegisterFile(isRegisterFile);
    }*/
    //获取结果文件
    @StepNo(stepNo = 3)
    protected void debtDirectConnectedStepSix(PubReq request) throws Exception{
        //log.info(" ###### 获取结果文件开始");
        //reportClearService.getResultFile(isRegisterFile);
    }
    /*//数据归档
    @StepNo(stepNo = 5)
    protected void debtDirectConnectedStepSeven(PubReq request) throws Exception{
        dataArchiving(isRegisterFile);
    }*/
}
