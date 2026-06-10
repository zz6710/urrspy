package com.kayak.web.business.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.business.domain.BaseReportExportLog;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import org.apache.ibatis.annotations.Param;

public interface BaseReportExportLogMapper extends BaseMapperPlus<BaseReportExportLogMapper, BaseReportExportLog, BaseReportExportLog> {

    BaseReportExportLog selectByProcessInstanceId(@Param("processInstanceId") String processInstanceId);

}
