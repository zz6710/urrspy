package com.kayak.web.workflow.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.workflow.domain.WfBusConfig;
import com.kayak.web.workflow.domain.vo.WfBusConfigVo;
import org.apache.ibatis.annotations.Param;

/**
 * 业务流程Mapper接口
 *
 * @author yuanjinqiao
 * @date 2022-09-14
 */
public interface WfBusConfigMapper extends BaseMapperPlus<WfBusConfigMapper, WfBusConfig, WfBusConfigVo> {

    Page<WfBusConfigVo> queryPageList(@Param(Constants.WRAPPER) QueryWrapper<WfBusConfig> wrapper, Page<Object> build);
}
