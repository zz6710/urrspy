package com.kayak.web.workflow.service;

import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.web.workflow.domain.bo.WfCopyBo;
import com.kayak.web.workflow.domain.bo.WfCopyTaskQueryBo;
import com.kayak.web.workflow.domain.bo.WfTaskBo;
import com.kayak.web.workflow.domain.vo.WfCopyVo;
import com.kayak.web.workflow.domain.vo.WfFormConfVO;
import org.flowable.bpmn.model.UserTask;

import java.util.List;

/**
 * 流程抄送Service接口
 *
 * @author yuanjinqiao
 * @date 2022-05-19
 */
public interface IWfCopyService {

    /**
     * 查询流程抄送列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<WfCopyVo> queryPageList(WfCopyTaskQueryBo bo, PageQuery pageQuery);

    /**
     * 抄送
     *
     * @param procInsId
     * @param userTask
     * @return
     */
    void makeCopy(String procInsId, UserTask userTask);

    /**
     * 已阅
     *
     * @param bo
     */
    void read(WfCopyBo bo);

    /**
     * 获取抄送表单配置
     *
     * @param procDefId
     * @param taskDefKey
     * @return
     */
    WfFormConfVO getFormConf(String procDefId, String taskDefKey);
}
