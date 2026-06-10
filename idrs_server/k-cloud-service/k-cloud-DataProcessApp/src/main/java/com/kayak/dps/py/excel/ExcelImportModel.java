package com.kayak.dps.py.excel;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Data
public class ExcelImportModel {
    /**
     * 缓存大小
     */
    @Value("${excel.batch.size:1000}")
    private int batchSize;
}
