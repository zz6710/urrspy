package com.kayak.pms.opFlow.engine.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BusinessLabelInfo {

   private String id;

   private String processInstanceId;

   private String data;
}
