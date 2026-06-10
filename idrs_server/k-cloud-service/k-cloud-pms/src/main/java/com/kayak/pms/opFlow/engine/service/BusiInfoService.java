package com.kayak.pms.opFlow.engine.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.kayak.helper.StringHelper;
import com.kayak.pms.opFlow.engine.constant.BusinessStatus;
import com.kayak.pms.opFlow.engine.constant.ProcessInstanceStatus;
import com.kayak.pms.opFlow.engine.dao.BusiInfoDao;
import com.kayak.pms.opFlow.engine.entity.BusiInfo;
import com.kayak.pms.opFlow.engine.entity.vo.BootstrapTableVO;
import com.kayak.pms.opFlow.engine.exception.WorkflowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 流程业务服务
 * @author xiamh
 * @date 2021/1/25
 */
@Service
public class BusiInfoService {

    @Autowired
    private BusiInfoDao busiInfoDao;

    public BootstrapTableVO<BusiInfo> list(Map<String, Object> queryCriteria) {
//        PageHelper.offsetPage((Integer) queryCriteria.get("offset"), (Integer) queryCriteria.get("limit"));
        List<BusiInfo> busiInfoList = busiInfoDao.list(queryCriteria);
//        AuthObjectUtil.complementUserName(busiInfoList, MapUtil.builder(new HashMap<String, String>()).put("userid", "creatorName").build());
//        PageInfo page = new PageInfo<BusiInfo>(busiInfoList);
        return new BootstrapTableVO<>(busiInfoList, busiInfoList.size());
    }

    public String save(BusiInfo busiInfo) {
        busiInfo.setBusiId(StringHelper.getPrimaryKey())
                .setProcessStatus(ProcessInstanceStatus.PREPARATION)
                .setBusStatus(BusinessStatus.READY)
                .setStartDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN))
                .setStartTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_TIME_PATTERN))
                .setUpdateDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN))
                .setUpdateTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_TIME_PATTERN));
        int ret = busiInfoDao.save(busiInfo);
        if(ret == 0){
            throw new WorkflowException("流程发起失败：新增业务审批表失败");
        }
        return busiInfo.getBusiId();
    }

//    @Override
//    public String save(String server, String url, String unKey, String values, String userid, String processId) {
//        BusiInfo busiInfo = BusiInfo.builder()
//                .busiId(StringHelper.getPrimaryKey())
//                .server(server)
//                .url(url)
//                .keys(unKey)
//                .values(values)
//                .processId(processId)
//                .processStatus(WfProcessInstanceStatusConstant.PREPARATION)
//                .busStatus(WfBusinessStatus.READY)
//                .userid(userid)
//                .startDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN))
//                .startTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_TIME_PATTERN))
//                .updateDate(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN))
//                .updateTime(LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_TIME_PATTERN))
//                .build();
//        int ret = busiInfoDao.save(busiInfo);
//        if(ret == 0){
//            throw new WorkflowException("流程发起失败：新增业务审批表失败");
//        }
//        return busiInfo.getBusiId();
//    }

    public int updateProcessStatusByInstanceId(String processInstanceId, String processStatus) {
        int ret = busiInfoDao.updateProcessStatusByInstanceId(processInstanceId, processStatus, LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_DATE_PATTERN), LocalDateTimeUtil.format(LocalDateTime.now(), DatePattern.PURE_TIME_PATTERN));
        if(ret != 1){
            throw new WorkflowException("状态更新失败:" + ret);
        }
        return ret;
    }


}
