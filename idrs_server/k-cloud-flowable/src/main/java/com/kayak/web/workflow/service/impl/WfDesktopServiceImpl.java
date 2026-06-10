package com.kayak.web.workflow.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.entity.page.PageQuery;
import com.kayak.common.entity.page.TableDataInfo;
import com.kayak.factory.FlowServiceFactory;
import com.kayak.utils.SysUtil;
import com.kayak.web.workflow.domain.WfBusiInfo;
import com.kayak.web.workflow.domain.bo.WfTaskQueryBo;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import com.kayak.web.workflow.mapper.WfBusiInfoMapper;
import com.kayak.web.workflow.mapper.WfDesktopMapper;
import com.kayak.web.workflow.service.IWfDesktopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ty
 * @since 2023-05-15 15:48:50
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class WfDesktopServiceImpl extends FlowServiceFactory implements IWfDesktopService {
    private final WfBusiInfoMapper wfBusiInfoMapper;
    private final WfDesktopMapper wfDesktopMapper;

    @Override
    public TableDataInfo<WfTaskVo> queryPageTodoProcessList(WfTaskQueryBo bo, PageQuery pageQuery) {
        return null;
    }

    @Override
    public TableDataInfo<WfTaskVo> queryPageOwnProcessList(WfTaskQueryBo bo, PageQuery pageQuery) {
        String userId = SysUtil.getCurrentUserId();
        String username = SysUtil.getCurrentUserUsername();
        Page<WfTaskVo> result = wfDesktopMapper.queryPageOwnProcessList(userId, pageQuery.build());
        List<WfTaskVo> records = result.getRecords();
        for(WfTaskVo record : records){
            record.setStartUserName(username);
            //流程业务主键与名称
            List<WfBusiInfo> valuesNameList = wfBusiInfoMapper.findNameByValues(record.getProcInsId());
            record.setValues(valuesNameList.size()>0?valuesNameList.get(0).getValues():"");
            record.setValuesName(valuesNameList.size()>0?valuesNameList.get(0).getValuesName():"");
        }
        return TableDataInfo.build(result);
    }
}
