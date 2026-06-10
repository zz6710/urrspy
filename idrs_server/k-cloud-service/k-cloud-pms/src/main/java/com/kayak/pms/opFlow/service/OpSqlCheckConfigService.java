package com.kayak.pms.opFlow.service;

import com.kayak.aspect.annotations.APIDefine;
import com.kayak.pms.opFlow.model.OpSqlCheckConfig;
import org.springframework.stereotype.Service;

@Service
@APIDefine(desc = "sql校验服务", model = OpSqlCheckConfig.class)
public class OpSqlCheckConfigService {
}
