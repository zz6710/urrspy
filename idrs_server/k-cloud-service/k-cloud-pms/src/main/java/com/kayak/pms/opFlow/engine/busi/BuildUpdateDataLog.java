package com.kayak.pms.opFlow.engine.busi;

import java.util.List;

@FunctionalInterface
public interface BuildUpdateDataLog {

        /**
         * 拼装更新的数据
         *
         * @return
         */
        List<SaveTaskUpdateBusinessLogService.UpdateDataElement> buildUpdateData();
}
