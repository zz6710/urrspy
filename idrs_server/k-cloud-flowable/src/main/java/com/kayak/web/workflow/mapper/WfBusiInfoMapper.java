package com.kayak.web.workflow.mapper;

import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.vo.WfBusiInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 业务审批Mapper接口
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
public interface WfBusiInfoMapper extends BaseMapperPlus<WfBusiInfoMapper, WfBusiInfo, WfBusiInfoVo> {

    /**
     * unkey是否有未完成的审批
     *
     * @param processKey
     * @param values
     * @param unKey
     * @param procInsFinish
     * @param procInsRefuse
     * @param busiFinish
     * @param busiErrorConfirmed
     * @return
     */
    List<WfBusiInfo> findNotFinishDataByUnKey(@Param("processKey") String processKey,
                                              @Param("values") String values,
                                              @Param("unKey") String unKey,
                                              @Param("procInsFinish") String procInsFinish,
                                              @Param("procInsRefuse") String procInsRefuse,
                                              @Param("busiFinish") String busiFinish,
                                              @Param("busiErrorConfirmed") String busiErrorConfirmed);

    /**
     * 根据流程实例id查询是否有未完成的审批
     *
     * @param processInstanceId
     * @param procInsFinish
     * @param procInsRefuse
     * @param busiFinish
     * @param busiErrorConfirmed
     * @return
     */
    WfBusiInfo findNotFinishDataByProcessInstanceId(@Param("processInstanceId") String processInstanceId,
                                                    @Param("procInsFinish") String procInsFinish,
                                                    @Param("procInsRefuse") String procInsRefuse,
                                                    @Param("busiFinish") String busiFinish,
                                                    @Param("busiErrorConfirmed") String busiErrorConfirmed);

    void updateBusStatus(@Param("busStatus") String busStatus, @Param("message") String message,
                         @Param("userId") String userId, @Param("processInstanceId") String processInstanceId, @Param("updateTime") Date updateTime);

    List<WfBusiInfo> findNameByValues(@Param("processInstanceId") String processInstanceId);

}
