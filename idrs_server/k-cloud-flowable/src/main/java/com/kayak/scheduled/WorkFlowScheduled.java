package com.kayak.scheduled;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.kayak.common.constant.BusinessStatus;
import com.kayak.common.enums.ProcessInstanceStatusEnum;
import com.kayak.utils.ThreadPoolUtils;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.RejectedExecutionException;

/**
 * 工作流回调定时任务+线程池
 *
 * @author yuanjinqiao
 * @date 2020-2-3
 */
@Slf4j
@Component
public class WorkFlowScheduled {
    @Autowired
    private WfBusiInfoMapper busiInfoDao;
    @Autowired
    private WorkFlowCallbackService callbackService;

    @Scheduled(cron = "${workflow.scheduled.cron:0,52 * * * * ? }")
    public void exec() throws Exception {
        //查询待回调流程
        LambdaQueryWrapper<WfBusiInfo> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(WfBusiInfo::getProcessStatus, ProcessInstanceStatusEnum.FINISH.getType());
        lambdaQuery.eq(WfBusiInfo::getBusStatus, BusinessStatus.READY);
        lambdaQuery.orderByDesc(WfBusiInfo::getUpdateTime);
        List<WfBusiInfo> busiInfoList = busiInfoDao.selectList(lambdaQuery);
        if (CollectionUtil.isEmpty(busiInfoList)) {
            log.info("没有需要回调的流程");
            return;
        }

        /**
         * 循环每笔发起业务回调
         */
        for (WfBusiInfo busiInfo : busiInfoList) {
            try {
                ThreadPoolUtils.execute(() -> callbackService.execute(busiInfo));
            } catch (RejectedExecutionException e) {
                // 阻塞队列已满后恢复为准备状态
                log.error(" 回调失败: ", e);
            }
        }
    }

}
