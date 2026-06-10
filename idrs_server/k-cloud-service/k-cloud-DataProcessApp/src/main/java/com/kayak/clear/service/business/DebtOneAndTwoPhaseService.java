package com.kayak.clear.service.business;

import cn.hutool.core.util.ObjectUtil;
import com.kayak.clear.req.PubReq;
import com.kayak.dps.check.enums.DebtOneAndTwoEnum;
import com.kayak.dps.direct.util.DirectParams;
import com.kayakwise.kcloud.batch.annotation.StepNo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 批处理-中债一二期直连
 * axin
 * 20220708
 * @author lll
 */
@Slf4j
@Component
@Scope("prototype")
public class DebtOneAndTwoPhaseService extends DebtDirectConnected {

    // 需要直连报送的文件
    String isRegisterFile;

    // 数据合法性检查
    @StepNo(stepNo = 1)
    protected void debtDirectConnectedStepTwo(PubReq request) throws Exception{
        isRegisterFile = DebtOneAndTwoEnum.getEnumVal(request.getTaskId()).getRegisterFile();
        // 前置校验结果验证关闭，跳过数据合法性检查
        if (ObjectUtil.equals(DirectParams.appCheck, "0")) {
            return;
        }
        dataValidityCheckA(isRegisterFile);
    }

    // 生成中债一二期报送文件并发送
    @StepNo(stepNo = 2)
    protected void debtDirectConnectedStepThree(PubReq request) throws Exception{
        exportRegisterFileBRXL(isRegisterFile);
    }

    // 发送报送文件
    /*@StepNo(stepNo = 3)
    protected void debtDirectConnectedStepFive(PubReq request) throws Exception{
        sendRegisterFile(isRegisterFile);
    }*/

    // 获取获取反馈文件
    @StepNo(stepNo = 3)
    protected void debtDirectConnectedStepSix(PubReq request) throws Exception{
        //getResultFile(isRegisterFile);
    }

    // 数据归档
    /*@StepNo(stepNo = 5)
    protected void debtDirectConnectedStepSeven(PubReq request) throws Exception{
        dataArchiving(isRegisterFile);
    }*/
}
