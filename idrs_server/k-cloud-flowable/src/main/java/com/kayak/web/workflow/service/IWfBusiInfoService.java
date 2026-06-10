package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.bo.WfBusiInfoBo;
import com.kayak.web.workflow.domain.vo.WfBusiInfoVo;

/**
 * 业务审批Service接口
 *
 * @author yuanjinqiao
 * @date 2022-09-15
 */
public interface IWfBusiInfoService {

    /**
     * 查询业务审批列表
     */
    TableDataInfo<WfBusiInfoVo> queryPageList(WfBusiInfoBo bo, PageQuery pageQuery);

    boolean errorConfirm(WfBusiInfo bo);

    boolean updateProcStatus(String procInsId, String status);
}
