package com.kayak.web.business.mapper;

import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.business.domain.BaseAccountMergeOrder;
import com.kayak.web.business.domain.ImportMenuFileManage;
import org.apache.ibatis.annotations.Param;

public interface ImportMenuFileManageMapper extends BaseMapperPlus<ImportMenuFileManageMapper, ImportMenuFileManage, ImportMenuFileManage> {

    void updateStatusById(@Param("status") String status, @Param("id") String id);

}
