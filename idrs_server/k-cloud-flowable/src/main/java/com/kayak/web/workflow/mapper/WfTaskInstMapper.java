package com.kayak.web.workflow.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.HashSet;
import java.util.List;

/**
 * 任务实例流程Mapper接口
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
public interface WfTaskInstMapper extends BaseMapperPlus<WfTaskInstMapper, WfTaskVo, WfTaskVo> {

    Page<WfTaskVo> queryPageFinishedProcessList(@Param(Constants.WRAPPER) QueryWrapper wrapper, Page<Object> build);

    Page<WfTaskVo> queryPageOwnProcessList(@Param(Constants.WRAPPER) QueryWrapper wrapper, Page<Object> build);

    List<WfTaskVo> queryHistoryTaskList(@Param("procInsId") String procInsId, @Param("defaultDate") DateTime defaultDate, @Param("taskTypes") HashSet<String> taskTypes);
}
