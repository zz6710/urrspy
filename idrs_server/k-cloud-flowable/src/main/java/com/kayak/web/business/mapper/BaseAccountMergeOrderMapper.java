package com.kayak.web.business.mapper;

import com.kayak.common.mapper.BaseMapperPlus;
import com.kayak.web.business.domain.BaseAccountMergeOrder;
import org.apache.ibatis.annotations.Param;

public interface BaseAccountMergeOrderMapper extends BaseMapperPlus<BaseAccountMergeOrderMapper, BaseAccountMergeOrder, BaseAccountMergeOrder> {

    void updateMrgStsById(@Param("mrgSts") String mrgSts, @Param("id") String id);

}
