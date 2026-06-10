package com.kayak.web.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kayak.common.entity.BaseEntity;
import lombok.Data;

@Data
@TableName("base_account_merge_order")
public class BaseAccountMergeOrder extends BaseEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    private String cstmDtF;
    private String cstmDtE;
    private String cstmAccF;
    private String cstmAccT;
    private String optDt;
    private String optTm;
    private String optUserId;
    private String optUserNm;
    private String mrgSts;
}
