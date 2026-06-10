package com.kayak.web.workflow.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.workflow.domain.vo.WfTaskVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author Ty
 * @since 2023-05-15 16:14:40
 */
public interface WfDesktopMapper extends BaseMapperPlus<WfTaskInstMapper, WfTaskVo, WfTaskVo> {
    Page<WfTaskVo> queryPageOwnProcessList(@Param("startUserId") String startUserId, Page<Object> build);

}
